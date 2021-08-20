/*******************************************************************************
 * Copyright (c) 2014, 2019 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.ModelChangeTrigger;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.DslCommonPlugin;
import org.eclipse.sirius.common.tools.api.util.SiriusCrossReferenceAdapter;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.ext.emf.EReferencePredicate;
import org.eclipse.sirius.tools.api.profiler.SiriusTasksKey;
import org.eclipse.sirius.tools.api.ui.RefreshEditorsPrecommitListener;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.Messages;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

/**
 * A {@link ModelChangeTrigger} which remove all the potential dangling references when an EObject is deleted using the
 * Sirius logic (checking for authorizations, "special" references and so on).
 * 
 * @author cbrun
 * 
 */
public class DanglingRefRemovalTrigger implements ModelChangeTrigger {

    /**
     * Priority of this {@link ModelChangeTrigger}.
     */
    public static final int DANGLING_REFERENCE_REMOVAL_PRIORITY = 0;

    /**
     * Filter {@link Notification}s which are not a detachment. A detachment is : an EObject being removed from the
     * reference it is contained in.
     */
    public static final Predicate<Notification> IS_DETACHMENT = new Predicate<Notification>() {
        @Override
        public boolean test(Notification input) {
            boolean potentialExplicitDetachment = input.getEventType() == Notification.REMOVE || input.getEventType() == Notification.REMOVE_MANY || input.getEventType() == Notification.UNSET;
            boolean potentialImplicitDetachment = input.getEventType() == Notification.SET && input.getNewValue() == null;
            if (potentialExplicitDetachment || potentialImplicitDetachment) {

                boolean representationDetachment = false;
                // A null feature corresponds to a detachment of a root element
                // in a resource.
                if (input.getFeature() == null) {
                    // we ignore object that has been uncontrolled that is
                    // object detached from the resource but not attached to its
                    // parent because it already is.
                    // In other words, we consider only the DRepresentation that
                    // is the only object that can be moved as root object from
                    // a resource to another
                    Object value = input.getOldValue() != null ? input.getOldValue() : input.getNewValue();
                    representationDetachment = value instanceof DRepresentation;
                }
                return representationDetachment || input.getFeature() instanceof EReference && ((EReference) input.getFeature()).isContainment();
            }
            return false;
        }

    };

    /**
     * Filter {@link Notification}s which are not an attachment. An attachment is : an EObject is attached from the
     * reference it is contained in.
     */
    public static final Predicate<Notification> IS_ATTACHMENT = new Predicate<Notification>() {
        @Override
        public boolean test(Notification input) {
            if (input.getEventType() == Notification.ADD || input.getEventType() == Notification.ADD_MANY || input.getEventType() == Notification.SET) {
                // The input.getNewValue() check required to make IS_ATTACHMENT
                // and IS_DETACHMENT mutually exclusive.
                // Null feature corresponds to an attachment as root element in
                // a resource.
                return (input.getFeature() == null || (input.getFeature() instanceof EReference && ((EReference) input.getFeature()).isContainment())) && input.getNewValue() != null;
            }
            return false;
        }

    };

    /**
     * A predicate to ignore DSemanticDecorator references in the dangling references deletion. Allows to avoid changes
     * in non opened diagrams and the corresponding abusive locks.
     */
    public static final EReferencePredicate DSEMANTICDECORATOR_REFERENCE_TO_IGNORE_PREDICATE = new EReferencePredicate() {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean apply(EReference reference) {
            // We should ignore any EReference which container is a
            // DSemanticDecorator (or any subclass)
            return ViewpointPackage.Literals.DSEMANTIC_DECORATOR.isSuperTypeOf(reference.getEContainingClass());
        }
    };

    /**
     * A predicate to ignore org.eclipse.gmf.runtime.notation.View#element references in the dangling references
     * deletion.
     */
    public static final EReferencePredicate NOTATION_VIEW_ELEMENT_REFERENCE_TO_IGNORE_PREDICATE = new EReferencePredicate() {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean apply(EReference eReference) {
            // We should ignore View.element reference because not doing
            // that have the effect to have the EditParts having
            // "semantic element" the DSemanticDiagram because
            // View.getElement() if View.element == null returns the
            // View.element of the parent
            // It is not a problem to leave a dangling reference on
            // View.element because the canonical refresh in precommit will
            // remove these Views referencing a deleted DDiagramElement

            // ignoring the View.element reference
            return NOTATION_VIEW_ELEMENT_REFERENCE_TO_IGNORE.equals(eReference.getName()) && NOTATION_VIEW_ELEMENT_REFERENCE_CONTAINER_TO_IGNORE.equals(eReference.getContainerClass().getName());
        }
    };

    /**
     * A predicate to ignore EPackage#eFactoryInstance references in the dangling references deletion.
     */
    public static final Predicate<EReference> EPACKAGE_EFACTORYINSTANCE_REFERENCE_TO_IGNORE_PREDICATE = new Predicate<EReference>() {
        @Override
        public boolean test(EReference eReference) {
            // ignoring the EPackage.eFactoryInstance reference
            return EcorePackage.eINSTANCE.getEPackage_EFactoryInstance().equals(eReference);
        }
    };

    /**
     * Name of the GMF View feature to ignore in the remove dangling references.
     */
    private static final String NOTATION_VIEW_ELEMENT_REFERENCE_TO_IGNORE = "element"; //$NON-NLS-1$

    /**
     * Container name of the GMF View feature to ignore in the remove dangling references.
     */
    private static final String NOTATION_VIEW_ELEMENT_REFERENCE_CONTAINER_TO_IGNORE = "org.eclipse.gmf.runtime.notation.View"; //$NON-NLS-1$

    private Session session;

    /**
     * Create a new instance which can be associated with a SessionEventBroker to automatically remove the dangling
     * references.
     * 
     * @param session
     *            the {@link Session}
     */
    public DanglingRefRemovalTrigger(Session session) {
        this.session = session;
    }

    @Override
    public Option<Command> localChangesAboutToCommit(Collection<Notification> notifications) {
        final Set<EObject> allDetachedObjects = getChangedEObjectsAndChildren(Iterables.filter(notifications, IS_DETACHMENT::test), null);
        if (allDetachedObjects.size() > 0) {
            DslCommonPlugin.PROFILER.startWork(SiriusTasksKey.CLEANING_REMOVEDANGLING_KEY);

            // Predicate to ignore attachments to detached elements.
            Collection<Notifier> allDetachedObjectsAsNotifier = new ArrayList<>();
            for (Notifier notifier : allDetachedObjects) {
                allDetachedObjectsAsNotifier.add(notifier);
            }
            Predicate<Notifier> ignoreNotifierInDetachedObjects = allDetachedObjectsAsNotifier::contains;
            final Set<EObject> allAttachedObjects = getChangedEObjectsAndChildren(Iterables.filter(notifications, IS_ATTACHMENT::test), ignoreNotifierInDetachedObjects);
            final Set<EObject> toRemoveXRefFrom = Sets.difference(allDetachedObjects, allAttachedObjects);
            if (toRemoveXRefFrom.size() > 0) {
                EReferencePredicate refToIgnore = new EReferencePredicate() {
                    @Override
                    public boolean apply(EReference ref) {
                        return DSEMANTICDECORATOR_REFERENCE_TO_IGNORE_PREDICATE.apply(ref) || NOTATION_VIEW_ELEMENT_REFERENCE_TO_IGNORE_PREDICATE.apply(ref)
                                || EPACKAGE_EFACTORYINSTANCE_REFERENCE_TO_IGNORE_PREDICATE.test(ref);
                    }
                };

                Command removeDangling = new RemoveDanglingReferencesCommand(session, toRemoveXRefFrom, refToIgnore);
                DslCommonPlugin.PROFILER.stopWork(SiriusTasksKey.CLEANING_REMOVEDANGLING_KEY);
                return Options.newSome(removeDangling);
            }
            DslCommonPlugin.PROFILER.stopWork(SiriusTasksKey.CLEANING_REMOVEDANGLING_KEY);
        }
        return Options.newNone();
    }

    /**
     * Return the EObjects which have been changed by the given notifications and their children.
     * 
     * @param notifications
     *            notifications to process.
     * @param notifierToIgnore
     *            a predicate indicating if a given notification should be ignored regarding its EObject notifier or not
     *            (can be null if all notifications should be considered)
     * @return the EObjects which have been changed by the given notifications and their children.
     */
    protected Set<EObject> getChangedEObjectsAndChildren(Iterable<Notification> notifications, Predicate<Notifier> notifierToIgnore) {
        final Set<EObject> changedEObjects = new LinkedHashSet<>();
        for (Notification notification : notifications) {
            Object notifier = notification.getNotifier();
            if (notifier instanceof Notifier) {
                if (notifierToIgnore == null || !notifierToIgnore.test((Notifier) notifier)) {
                    for (EObject root : getNotificationValues(notification)) {
                        // Add the element and all its contents to the
                        // changedEObjects set only once.
                        if (!root.eIsProxy() && !changedEObjects.contains(root)) {
                            changedEObjects.add(root);
                            Iterators.addAll(changedEObjects, root.eAllContents());
                        }
                    }
                }
            }
        }
        return changedEObjects;
    }

    /**
     * Return the added or removed EObjects from an attachment or a detachment notification.
     * 
     * @param notification
     *            the change.
     * @return the added or removed EObjects from an attachment or a detachment notification.
     */
    protected Set<EObject> getNotificationValues(Notification notification) {
        final Set<EObject> values = new LinkedHashSet<>();
        Object value = notification.getOldValue();
        if (IS_ATTACHMENT.test(notification)) {
            // IS_DETACHMENT is the default case : notification.getOldValue and
            // the two predicates are mutually exclusive: see the SET case.
            value = notification.getNewValue();
        }
        if (value instanceof Collection) {
            Iterables.addAll(values, Iterables.filter((Collection<?>) value, EObject.class));
        } else if (value instanceof EObject) {
            values.add((EObject) value);
        }
        return values;
    }

    @Override
    public int priority() {
        return DANGLING_REFERENCE_REMOVAL_PRIORITY;
    }

    /**
     * Specific command to remove dangling references.
     */
    private static class RemoveDanglingReferencesCommand extends RecordingCommand {

        private final Collection<EObject> toRemoveXRefFrom = new LinkedHashSet<>();

        private final EReferencePredicate isReferenceToIgnorePredicate;

        private ModelAccessor modelAccessor;

        private ECrossReferenceAdapter xReferencer;

        private Collection<Resource> semanticResources;

        private RefreshEditorsPrecommitListener refreshEditorsPrecommitListener;

        private TransactionalEditingDomain domain;

        private Session session;

        /**
         * Constructor.
         * 
         * @param session
         *            the {@link Session}
         * @param toRemoveXRefFrom
         *            the elements to remove cross references.
         * @param isReferenceToIgnore
         *            a predicate indicating if a given reference should be ignored during deletion or not (can be null
         *            if all references should be considered)
         */
        RemoveDanglingReferencesCommand(Session session, Set<EObject> toRemoveXRefFrom, EReferencePredicate isReferenceToIgnore) {
            super(session.getTransactionalEditingDomain(), Messages.DanglingRefRemovalTrigger_removeDanglingCmdLabel);
            this.domain = session.getTransactionalEditingDomain();
            this.modelAccessor = session.getModelAccessor();
            this.xReferencer = session.getSemanticCrossReferencer();
            this.semanticResources = session.getSemanticResources();
            this.refreshEditorsPrecommitListener = session.getRefreshEditorsListener();
            this.toRemoveXRefFrom.addAll(toRemoveXRefFrom);
            this.isReferenceToIgnorePredicate = isReferenceToIgnore;
            this.session = session;
        }

        @Override
        protected void doExecute() {
            Collection<EObject> impactedEObjects = new ArrayList<EObject>();
            DslCommonPlugin.PROFILER.startWork(SiriusTasksKey.CLEANING_REMOVEDANGLING_KEY);
            for (EObject eObject : toRemoveXRefFrom) {

                // ModelAccessor will access to the inverse references.
                // RemoveDanglingReferencesCommand should remove only inverse
                // cross references from non detached EObjects. This allows to
                // trigger a second refresh by the
                // RefreshEditorsPrecommitListener only when the command removed
                // some dangling reference.
                ECrossReferenceAdapter filteredCrossReferencer = new SiriusCrossReferenceAdapter() {
                    @Override
                    public Collection<Setting> getInverseReferences(EObject eObject, boolean resolve) {
                        Collection<Setting> settings = xReferencer.getInverseReferences(eObject, resolve);
                        Collection<Setting> settingsToTryToUnset = new ArrayList<>();
                        for (Setting s : settings) {
                            if (!toRemoveXRefFrom.contains(s.getEObject())) {
                                settingsToTryToUnset.add(s);
                            }
                        }
                        return settingsToTryToUnset;
                    }
                };

                // No need to call eDelete here: only remove dangling references
                // to the detached EObject and avoid to trigger the creation of
                // new REMOVE notifications by removing the indirectly detached
                // object from their container.
                impactedEObjects.addAll(modelAccessor.eRemoveInverseCrossReferences(eObject, filteredCrossReferencer, isReferenceToIgnorePredicate));
            }
            for (EObject impactedEObject : impactedEObjects) {
                Resource resource = impactedEObject.eResource();
                if (semanticResources.contains(resource)) {
                    refreshEditorsPrecommitListener.disable();
                    break;
                }
            }
            DslCommonPlugin.PROFILER.stopWork(SiriusTasksKey.CLEANING_REMOVEDANGLING_KEY);

            toRemoveXRefFrom.clear();
            modelAccessor = null;
            xReferencer = null;
            semanticResources = null;
            refreshEditorsPrecommitListener = null;
        }
    };
}
