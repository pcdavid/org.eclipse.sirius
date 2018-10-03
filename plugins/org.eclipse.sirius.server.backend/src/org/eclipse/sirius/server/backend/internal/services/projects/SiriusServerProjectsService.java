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
package org.eclipse.sirius.server.backend.internal.services.projects;

import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_BAD_REQUEST;
import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_CREATED;
import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_INTERNAL_SERVER_ERROR;
import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_OK;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.server.api.ISiriusServerService;
import org.eclipse.sirius.server.api.SiriusServerPath;
import org.eclipse.sirius.server.api.SiriusServerResponse;
import org.eclipse.sirius.server.backend.internal.SiriusServerMessages;
import org.eclipse.sirius.server.backend.internal.utils.SiriusServerErrorDto;
import org.eclipse.sirius.server.backend.internal.utils.SiriusServerUtils;

/**
 * Service used to manipulate the list of projects.
 *
 * @author sbegaudeau
 */
@SiriusServerPath("/projects")
public class SiriusServerProjectsService implements ISiriusServerService {

    @Override
    public SiriusServerResponse doGet(HttpServletRequest request, Map<String, String> variables, String remainingPart) {
        return new SiriusServerResponse(STATUS_OK, this.getModelingProjects());
    }

    /**
     * Returns all the modeling projects of the workspace.
     *
     * @return All the modeling projects of the workspace
     */
    private SiriusServerProjectsDto getModelingProjects() {
        // @formatter:off
		List<SiriusServerProjectDto> modelingProjects = SiriusServerUtils.getModelingProjects()
				.map(this::convertToProject)
				.collect(Collectors.toList());
		// @formatter:on
        return new SiriusServerProjectsDto(modelingProjects);
    }

    /**
     * Converts the given project with a modeling nature into a
     * {@link SiriusServerProjectDto}.
     *
     * @param project
     *            The project
     * @return The {@link SiriusServerProjectDto} created
     */
    private SiriusServerProjectDto convertToProject(IProject project) {
        String name = project.getName();
        String description = SiriusServerUtils.getProjectDescription(project);
        return new SiriusServerProjectDto(name, description);
    }

    @Override
    public SiriusServerResponse doPost(HttpServletRequest request, Map<String, String> variables, String remainingPart) {
        SiriusServerResponse response = null;
        try {
            Reader reader = new InputStreamReader(request.getInputStream(), SiriusServerUtils.UTF_8);
            SiriusServerNewProjectDto newProject = new Gson().fromJson(reader, SiriusServerNewProjectDto.class);
            if (StringUtil.isEmpty(newProject.getName())) {
                response = new SiriusServerResponse(STATUS_BAD_REQUEST);
            } else {
                response = createProject(newProject.getName());
            }
        } catch (@SuppressWarnings("unused") IOException | CoreException exception) {
            // We don't want to send back the message of the exception
            response = new SiriusServerResponse(STATUS_INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    private SiriusServerResponse createProject(String name) throws CoreException {
        SiriusServerResponse response;
        if (SiriusServerUtils.projectExists(name)) {
            String message = MessageFormat.format(SiriusServerMessages.SiriusServerProjectsService_projectAlreadyExists, name);
            response = new SiriusServerResponse(STATUS_BAD_REQUEST, new SiriusServerErrorDto(message));
        } else {
            IProject project = SiriusServerUtils.createProject(name);
            String description = SiriusServerUtils.getProjectDescription(project);
            response = new SiriusServerResponse(STATUS_CREATED, new SiriusServerProjectDto(name, description));
        }
        return response;
    }
}
