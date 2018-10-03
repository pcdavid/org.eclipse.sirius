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

import java.util.Objects;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.business.api.query.IdentifiedElementQuery;
import org.eclipse.sirius.workflow.ActivityDescription;

/**
 * Runtime API to query and manipulate an {@link ActivityDescription} as part of
 * a {@link Workflow}.
 *
 * @author pcdavid
 */
public final class Activity {
    /**
     * The workflow this activity is part of.
     */
    private final Workflow workflow;

    /**
     * The VSM element which describes the activity.
     */
    private final ActivityDescription description;

    private Activity(Workflow workflow, ActivityDescription description) {
        this.workflow = Objects.requireNonNull(workflow);
        this.description = Objects.requireNonNull(description);
    }

    /**
     * Create a new {@link Activity}.
     *
     * @param workflow
     *            the workflow this activity is part of.
     * @param description
     *            the VSM element which describes the activity.
     * @return an {@link Activity} that can be used to query and manipulate the
     *         {@link ActivityDescription}.
     */
    public static Activity create(Workflow workflow, ActivityDescription description) {
        return new Activity(workflow, description);
    }

    /**
     * Returns the VSM element that describes this activity.
     *
     * @return the VSM element that describes this activity.
     */
    public ActivityDescription getDescription() {
        return description;
    }

    /**
     * Execute the behavior associated with the activity.
     *
     * @return the status of the operation
     */
    public IStatus invoke() {
        URI taskURI = EcoreUtil.getURI(description.getOperation());
        return workflow.executeReadWriteOperation(getLabel(), () -> {
            EObject self = workflow.getDefaultContext();
            new SiriusToolServices().executeOperation(self, taskURI.toString());
            return Status.OK_STATUS;
        });
    }

    private String getLabel() {
        return new IdentifiedElementQuery(description).getLabel();
    }
}
