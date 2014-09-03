/*******************************************************************************
 * Copyright (c) 2008, 2010, 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.tools.internal.views.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.query.RepresentationDescriptionQuery;
import org.eclipse.sirius.business.api.query.ResourceQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.api.session.danalysis.DAnalysisSession;
import org.eclipse.sirius.common.tools.api.util.EqualityHelper;
import org.eclipse.sirius.ui.tools.api.views.common.item.CommonSessionItem;
import org.eclipse.sirius.ui.tools.internal.views.common.item.ControlledRoot;
import org.eclipse.sirius.ui.tools.internal.views.common.item.ResourcesFolderItemImpl;
import org.eclipse.sirius.ui.tools.internal.views.common.item.ViewpointsFolderItemImpl;
import org.eclipse.sirius.viewpoint.DAnalysisSessionEObject;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * ContentProvider handling sessions and mixing semantic/representations.
 * 
 * @author cbrun
 * 
 */
public class SessionWrapperContentProvider implements ITreeContentProvider {

    private ITreeContentProvider wrapped;

    private Collection<ITreeContentProvider> extensions;

    /**
     * create a new wrapper content provider.
     * 
     * @param wrapped
     *            the original content provider.
     */
    public SessionWrapperContentProvider(final ITreeContentProvider wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Set extensions to use to provide custom children.
     * 
     * @param extensions
     *            the extensions to use
     */
    public void setExtensions(Collection<ITreeContentProvider> extensions) {
        this.extensions = extensions;
    }

    @Override
    public Object[] getChildren(final Object parentElement) {
        Collection<Object> allChildren = Lists.newArrayList();
        addChildren(parentElement, allChildren, false);
        return allChildren.toArray();
    }

    private boolean addChildren(Object parentElement, Collection<Object> result, boolean checkOnly) {
        boolean hashChildren = false;
        try {
            hashChildren = addNativeChildren(parentElement, result, checkOnly);
            if (hashChildren && checkOnly) {
                return true;
            }
            hashChildren = hashChildren || addChildrenFromExtensions(parentElement, result, checkOnly);
        } catch (IllegalStateException e) {
            // Nothing to do, can happen with CDO
        }
        return hashChildren;
    }

    /**
     * Get custom children from declared extensions and adds them to the
     * specified collection.
     * 
     * @param parentElement
     *            the parent element.
     * @param result
     *            the collection in which to add the children contributed by th
     *            extension.
     * @param checkOnly
     *            if true, only check that whether there are any children, and
     *            return early if any if found.
     * @return <code>true</code> if there are children from extensions
     */
    public boolean addChildrenFromExtensions(Object parentElement, Collection<Object> result, boolean checkOnly) {
        boolean hasChildren = false;
        if (extensions != null) {
            for (final ITreeContentProvider extension : extensions) {
                List<Object> contrib = Arrays.asList(extension.getChildren(parentElement));
                hasChildren = hasChildren || !contrib.isEmpty();
                if (hasChildren && checkOnly) {
                    return true;
                }
                result.addAll(contrib);
            }
        }
        return hasChildren;
    }

    private boolean addNativeChildren(Object parentElement, Collection<Object> result, boolean checkOnly) {
        boolean hasChildren = false;
        if (parentElement instanceof Session) {
            hasChildren = addSessionChildren((Session) parentElement, result, checkOnly);
        } else if (parentElement instanceof CommonSessionItem) {
            Collection<?> children = ((CommonSessionItem) parentElement).getChildren();
            hasChildren = !children.isEmpty();
            if (!checkOnly) {
                result.addAll(children);
            }
        } else if (parentElement instanceof Collection) {
            Collection<?> coll = (Collection<?>) parentElement;
            hasChildren = !coll.isEmpty();
            if (!checkOnly) {
                result.addAll(coll);
            }
        } else if (parentElement instanceof Resource) {
            Resource res = (Resource) parentElement;
            Session session = SessionManager.INSTANCE.getSession(res);
            if (res.isLoaded()) {
                if (session instanceof DAnalysisSessionEObject && ((DAnalysisSessionEObject) session).getControlledResources().contains(parentElement)) {
                    for (EObject obj : res.getContents()) {
                        result.add(new ControlledRoot(obj, parentElement));
                        hasChildren = true;
                        if (checkOnly) {
                            break;
                        }
                    }
                } else {
                    hasChildren = addWrappedChildren(parentElement, result, checkOnly);
                }
            }
        } else if (parentElement instanceof EObject && !(parentElement instanceof DRepresentation)) {
            // Look for representation with current element as semantic
            // target.
            hasChildren = addWrappedChildren(parentElement, result, checkOnly);
            if (hasChildren && checkOnly) {
                return true;
            }
            hasChildren = hasChildren || addRepresentationsAssociatedToEObject((EObject) parentElement, result, checkOnly);
        }
        return hasChildren;
    }

    private boolean addWrappedChildren(Object parentElement, Collection<Object> result, boolean checkOnly) {
        boolean hasChildren = false;
        if (checkOnly) {
            hasChildren = wrapped.hasChildren(parentElement);
        } else {
            Object[] structuralChildren = wrapped.getChildren(parentElement);
            if (structuralChildren != null && structuralChildren.length > 0) {
                hasChildren = true;
                result.addAll(Arrays.asList(structuralChildren));
            }
        }
        return hasChildren;
    }

    private boolean addSessionChildren(Session session, Collection<Object> result, boolean checkOnly) {
        if (!checkOnly) {
            if (session instanceof DAnalysisSession && !session.getReferencedSessionResources().isEmpty()) {
                result.add(new ResourcesFolderItemImpl(session, session));
            }
    
            result.add(new ViewpointsFolderItemImpl(session, session));
            addSemanticResources(session, result);
        }
        return true;
    }

    /**
     * Return all the semantic resources of the session : <LI>
     * <UL>
     * except the aird and GMFResource resources
     * </UL>
     * <UL>
     * plus the parent of the semantic resources that are controlled.
     * </UL>
     * </LI>
     * 
     * @param session
     *            the concerned session
     * @return list of semantic resources
     */
    private void addSemanticResources(Session session, Collection<Object> result) {
        // Add semantic resources
        result.addAll(session.getSemanticResources());

        // Add controlled resources
        if (session instanceof DAnalysisSessionEObject) {
            result.addAll(((DAnalysisSessionEObject) session).getControlledResources());
        }
    }

    private boolean addRepresentationsAssociatedToEObject(EObject eObject, Collection<Object> result, boolean checkOnly) {
        boolean hasChildren = false;
        final Session session = SessionManager.INSTANCE.getSession(eObject);
        if (session != null && session.isOpen()) {
            Collection<DRepresentation> allRepresentations = DialectManager.INSTANCE.getRepresentations(eObject, session);
            List<DRepresentation> filteredReps = Lists.newArrayList(Iterables.filter(allRepresentations, new InViewpointPredicate(session.getSelectedViewpoints(false))));
            hasChildren = !filteredReps.isEmpty();
            if (hasChildren && checkOnly) {
                return true;
            }
            // Sort the available representations alphabetically by name
            Collections.sort(filteredReps, Ordering.natural().onResultOf(new Function<DRepresentation, String>() {
                public String apply(DRepresentation from) {
                    return from.getName();
                }
            }));
            result.addAll(filteredReps);
        }
        return hasChildren;
    }

    private Collection<Resource> filter(final Collection<Resource> resources) {
        final Collection<Resource> result = new ArrayList<Resource>(resources.size());
        for (final Resource res : resources) {
            if (!isFiltered(res)) {
                result.add(res);
            }

        }
        return result;
    }

    private boolean isFiltered(final Resource resource) {
        return new ResourceQuery(resource).isRepresentationsResource();
    }

    @Override
    public Object getParent(final Object element) {
        Object parent = getParentFromExtensions(element);

        if (parent == null) {
            if (element instanceof DRepresentation && element instanceof DSemanticDecorator) {
                parent = ((DSemanticDecorator) element).getTarget();
            } else if (element instanceof Resource) {
                parent = SessionManager.INSTANCE.getSession((Resource) element);
            } else if (element instanceof CommonSessionItem) {
                parent = ((CommonSessionItem) element).getParent();
            } else if (element instanceof EObject) {
                parent = wrapped.getParent(element);
            }
        }

        return parent;
    }

    /**
     * Get custom parent from declared extensions.
     * 
     * @param element
     *            the element.
     * 
     * @return custom parent computed by declared extensions.
     */
    public Object getParentFromExtensions(final Object element) {
        if (extensions != null) {
            for (final ITreeContentProvider extension : extensions) {
                Object parent = extension.getParent(element);
                if (parent != null) {
                    return parent;
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasChildren(final Object element) {
        Collection<Object> children = Lists.newArrayList();
        if (addChildren(element, children, true)) {
            return !children.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public Object[] getElements(final Object inputElement) {
        Object[] result = null;
        if (inputElement instanceof Session) {
            result = filter(((Session) inputElement).getSemanticResources()).toArray();
        }
        if (inputElement instanceof Collection) {
            result = ((Collection<?>) inputElement).toArray();
        }
        if (result == null) {
            return wrapped.getElements(inputElement);
        }
        return result;
    }

    @Override
    public void dispose() {
        try {
            wrapped.dispose();
            if (wrapped instanceof AdapterFactoryContentProvider) {
                if (((AdapterFactoryContentProvider) wrapped).getAdapterFactory() instanceof IDisposable) {
                    ((IDisposable) ((AdapterFactoryContentProvider) wrapped).getAdapterFactory()).dispose();
                }
            }
        } catch (NullPointerException e) {
            // can occur when using CDO (if the view is
            // closed when transactions have been closed )
        }
    }

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        if (wrapped != null) {
            wrapped.inputChanged(viewer, oldInput, newInput);
        }
    }

    private static class InViewpointPredicate implements Predicate<DRepresentation> {

        private final Collection<Viewpoint> scope;

        /**
         * Filter representations <code>allRepresentations</code> whose
         * description belongs to one the specified viewpoints.
         * 
         * @param scope
         *            the viewpoints to test.
         */
        public InViewpointPredicate(Collection<Viewpoint> scope) {
            this.scope = scope;
        }

        /**
         * {@inheritDoc}
         */
        public boolean apply(DRepresentation input) {
            if (input.eResource() != null) {
                RepresentationDescription description = DialectManager.INSTANCE.getDescription(input);
                if (description != null) {
                    Viewpoint reprViewpoint = new RepresentationDescriptionQuery(description).getParentViewpoint();
                    // representationDescription.eContainer() can be null in the
                    // case that the viewpoint has been renamed after the aird
                    // creation
                    if (reprViewpoint != null && !reprViewpoint.eIsProxy() && reprViewpoint.eResource() != null) {
                        for (final Viewpoint viewpoint : scope) {
                            if (EqualityHelper.areEquals(viewpoint, reprViewpoint)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

    }
}
