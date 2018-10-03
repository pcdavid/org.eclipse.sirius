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
package org.eclipse.sirius.server.backend.internal.services.dashboard;

import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_OK;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.resources.IProject;
import org.eclipse.sirius.server.api.ISiriusServerService;
import org.eclipse.sirius.server.api.SiriusServerPath;
import org.eclipse.sirius.server.api.SiriusServerResponse;
import org.eclipse.sirius.server.backend.internal.utils.SiriusServerUtils;

/**
 * Service used to interact with the dashboard.
 *
 * @author sbegaudeau
 */
@SiriusServerPath("/dashboard")
public class SiriusServerDashboardService implements ISiriusServerService {

    /**
     * The number of projects to be displayed in the dashboard.
     */
    private static final int DASHBOARD_PROJECT_COUNT = 7;

    @Override
    public SiriusServerResponse doGet(HttpServletRequest request, Map<String, String> variables, String remainingPart) {
        return new SiriusServerResponse(STATUS_OK, this.getDashboard());
    }

    /**
     * Returns the dashboard.
     *
     * @return The dashboard
     */
    private SiriusServerDashboardDto getDashboard() {
        Stream<IProject> allModelingProjects = SiriusServerUtils.getModelingProjects();
        int projectsCount = Long.valueOf(allModelingProjects.count()).intValue();
        int viewpointsCount = SiriusServerUtils.getViewpointRegistry().getViewpoints().size();
        int metamodelsCount = SiriusServerUtils.getGlobalEPackagesRegistry().size();

        // @formatter:off
		List<SiriusServerDashboardProjectDto> projects = allModelingProjects
				.limit(DASHBOARD_PROJECT_COUNT)
				.map(this::convertToProject)
				.collect(Collectors.toList());
		// @formatter:on

        return new SiriusServerDashboardDto(projectsCount, viewpointsCount, metamodelsCount, projects);
    }

    /**
     * Converts the given project into a
     * {@link SiriusServerDashboardProjectDto}.
     *
     * @param project
     *            A project with the modeling project nature
     * @return The {@link SiriusServerDashboardProjectDto}
     */
    private SiriusServerDashboardProjectDto convertToProject(IProject project) {
        String name = project.getName();
        String description = SiriusServerUtils.getProjectDescription(project);
        return new SiriusServerDashboardProjectDto(name, description);
    }
}
