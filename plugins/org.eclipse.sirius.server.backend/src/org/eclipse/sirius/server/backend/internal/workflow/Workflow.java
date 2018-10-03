/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.server.backend.internal.workflow;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.server.backend.internal.SiriusServerBackendPlugin;
import org.eclipse.sirius.server.backend.internal.SiriusServerMessages;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.IdentifiedElement;
import org.eclipse.sirius.workflow.PageDescription;
import org.eclipse.sirius.workflow.SectionDescription;
import org.eclipse.sirius.workflow.WorkflowDescription;

/**
 * Runtime API to query and manipulate the workflow of a given session.
 *
 * @author pcdavid
 */
// CHECKSTYLE:OFF
public final class Workflow {
    private final Session session;

    public static Workflow of(Session session) {
        return new Workflow(Objects.requireNonNull(session));
    }

    private Workflow(Session session) {
        this.session = session;
    }

    /**
     * Finds all the workflow pages that apply to the given session. This may
     * include pages defined in different workflows, but pages that originate
     * from a given workflow are guaranteed to be contiguous.
     *
     * @return all the workflow pages that apply to the session.
     */
    public Stream<PageDescription> getPageDescriptions() {
        return getWorkflowDescriptions().flatMap(w -> w.getPages().stream());
    }

    /**
     * Returns the workflow descriptions from the given session.
     *
     * @return The workflow descriptions from the given session
     */
    public Stream<WorkflowDescription> getWorkflowDescriptions() {
        // @formatter:off
        return session.getSelectedViewpoints(true).stream()
                .map(viewpoint -> new EObjectQuery(viewpoint).getFirstAncestorOfType(DescriptionPackage.Literals.GROUP))
                .filter(Option::some)
                .map(Option::get)
                .filter(Group.class::isInstance)
                .map(Group.class::cast)
                .flatMap(group -> group.getExtensions().stream())
                .filter(WorkflowDescription.class::isInstance)
                .map(WorkflowDescription.class::cast);
        // @formatter:on
    }

    public Optional<PageDescription> findPageById(String pageIdentifier) {
        return findById(getPageDescriptions(), pageIdentifier);
    }

    public Optional<SectionDescription> findSectionById(String pageId, String sectionId) {
        return findPageById(pageId).flatMap(page -> findById(page.getSections(), sectionId));
    }

    public Optional<Activity> findActivityById(String pageId, String sectionId, String activityId) {
        return findSectionById(pageId, sectionId).flatMap(section -> findById(section.getActivities(), activityId)).map(ad -> Activity.create(this, ad));
    }

    private <T extends IdentifiedElement> Optional<T> findById(Collection<T> candidates, String id) {
        return findById(candidates.stream(), id);
    }

    private <T extends IdentifiedElement> Optional<T> findById(Stream<T> candidates, String id) {
        return candidates.filter(elt -> Objects.equals(elt.getName(), id)).findFirst();
    }

    public void invalidateStateValues() {
        getWorkflowDescriptions().forEach(w -> WorkflowStateAdapter.on(w).invalidate());
    }

    public List<SectionDescription> getFirstPageSections() {
        Optional<PageDescription> optionalPageDescription = getPageDescriptions().findFirst();
        List<SectionDescription> sectionDescriptions = optionalPageDescription.map(PageDescription::getSections).orElseGet(BasicEList::new);
        return sectionDescriptions;
    }

    /**
     * Returns the default context ("self") object to be used for all workflow
     * related expressions and operations.
     *
     * @return the default context object.
     */
    public EObject getDefaultContext() {
        return session.getSessionResource().getContents().get(0);
    }

    IStatus executeReadWriteOperation(String title, Callable<IStatus> body) {
        IStatus[] result = { Status.CANCEL_STATUS };
        TransactionalEditingDomain ted = session.getTransactionalEditingDomain();
        ted.getCommandStack().execute(new RecordingCommand(ted, title) {
            @Override
            protected void doExecute() {
                try {
                    result[0] = body.call();
                } catch (Exception e) {
                    result[0] = new Status(IStatus.ERROR, SiriusServerBackendPlugin.PLUGIN_ID, MessageFormat.format(SiriusServerMessages.Workflow_readWriteOperation_error, title), e);
                }
            }
        });
        return result[0];
    }

}
