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
package org.eclipse.sirius.server.backend.internal.services.activities;

import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_NOT_FOUND;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.sirius.server.api.ISiriusServerService;
import org.eclipse.sirius.server.api.SiriusServerPath;
import org.eclipse.sirius.server.api.SiriusServerResponse;
import org.eclipse.sirius.server.backend.internal.utils.SiriusServerUtils;
import org.eclipse.sirius.server.backend.internal.workflow.Workflow;

/**
 * The service used to execute a specific activity of a workflow.
 *
 * @author sbegaudeau
 */
@SiriusServerPath("/projects/{projectName}/pages/{pageIdentifier}/sections/{sectionIdentifier}/activities/{activityIdentifier}/execute")
public class SiriusServerActivityExecutorService implements ISiriusServerService {

    /**
     * The name of the variable used to capture the name of the project.
     */
    private static final Object PROJECT_NAME = "projectName"; //$NON-NLS-1$

    /**
     * The name of the variable used to capture the identifier of the page.
     */
    private static final Object PAGE_IDENTIFIER = "pageIdentifier"; //$NON-NLS-1$

    /**
     * The name of the variable used to capture the identifier of the section.
     */
    private static final Object SECTION_IDENTIFIER = "sectionIdentifier"; //$NON-NLS-1$

    /**
     * The name of the variable used to capture the identifier of the activity.
     */
    private static final Object ACTIVITY_IDENTIFIER = "activityIdentifier"; //$NON-NLS-1$

    @Override
    public SiriusServerResponse doPost(HttpServletRequest request, Map<String, String> variables, String remainingPart) {
        String projectName = variables.get(PROJECT_NAME);
        String pageId = variables.get(PAGE_IDENTIFIER);
        String sectionId = variables.get(SECTION_IDENTIFIER);
        String activityId = variables.get(ACTIVITY_IDENTIFIER);

        SiriusServerResponse[] response = { new SiriusServerResponse(STATUS_NOT_FOUND) };

        SiriusServerUtils.getSessionFromProject(projectName).ifPresent(session -> {
            Workflow.of(session).findActivityById(pageId, sectionId, activityId).ifPresent(activity -> {
                IStatus status = activity.invoke();
                response[0] = SiriusServerResponse.ofStatus(status);
            });
        });

        return response[0];
    }

}
