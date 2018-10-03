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
package org.eclipse.sirius.server.backend.internal.services.project;

import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_INTERNAL_SERVER_ERROR;
import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_NOT_FOUND;
import static org.eclipse.sirius.server.api.SiriusServerResponse.STATUS_OK;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.modelingproject.ModelingProject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.danalysis.DAnalysisSession;
import org.eclipse.sirius.common.interpreter.api.IEvaluationResult;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.server.api.ISiriusServerService;
import org.eclipse.sirius.server.api.SiriusServerPath;
import org.eclipse.sirius.server.api.SiriusServerResponse;
import org.eclipse.sirius.server.backend.internal.SiriusServerBackendPlugin;
import org.eclipse.sirius.server.backend.internal.utils.SiriusServerUtils;
import org.eclipse.sirius.server.backend.internal.workflow.SiriusBackendInterpreter;
import org.eclipse.sirius.server.backend.internal.workflow.Workflow;
import org.eclipse.sirius.server.backend.internal.workflow.WorkflowExpressions;
import org.eclipse.sirius.server.backend.internal.workflow.WorkflowStateAdapter;
import org.eclipse.sirius.table.metamodel.table.description.TableDescription;
import org.eclipse.sirius.tree.description.TreeDescription;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.workflow.ActivityDescription;
import org.eclipse.sirius.workflow.SectionDescription;
import org.eclipse.sirius.workflow.WorkflowDescription;

/**
 * Service used to manipulate a specific project.
 *
 * @author sbegaudeau
 */
@SiriusServerPath("/projects/{projectName}")
public class SiriusServerProjectService implements ISiriusServerService {

    /**
     * The name of the variable used to capture the name of the project.
     */
    private static final Object PROJECT_NAME = "projectName"; //$NON-NLS-1$

    @Override
    public SiriusServerResponse doGet(HttpServletRequest request, Map<String, String> variables, String remainingPart) {
        Optional<String> optionalProjectName = Optional.ofNullable(variables.get(PROJECT_NAME));
        Optional<ModelingProject> optionalModelingProject = optionalProjectName.flatMap(SiriusServerUtils::findModelingProjectByName);
        Optional<SiriusServerProjectDto> optionalProject = optionalModelingProject.map(this::getProjectFromModelingProject);
        return SiriusServerResponse.ofOptional(optionalProject);
    }

    /**
     * Converts the given modeling project into a project to be returned by the
     * service.
     *
     * @param modelingProject
     *            The modeling project
     * @return The project to be returned by the service
     */
    private SiriusServerProjectDto getProjectFromModelingProject(ModelingProject modelingProject) {
        Session session = SiriusServerUtils.getSession(modelingProject);

        IProject project = modelingProject.getProject();
        String projectName = project.getName();
        String description = SiriusServerUtils.getProjectDescription(project);
        List<AbstractSiriusServerRepresentationDto> representations = this.getRepresentations(session);
        List<SiriusServerSemanticResourceDto> semanticResources = this.getSemanticResources(modelingProject);
        List<SiriusServerPageDto> pages = this.getPages(modelingProject, session);
        List<SiriusServerSectionDto> currentPageSections = this.getFirstPageSections(session);
        return new SiriusServerProjectDto(projectName, description, representations, semanticResources, pages, currentPageSections);
    }

    /**
     * Returns the list of workflow page from the given session.
     *
     * @param modelingProject
     *            The modeling project
     * @param session
     *            The session
     * @return The list of workflow page from the given session
     */
    private List<SiriusServerPageDto> getPages(ModelingProject modelingProject, Session session) {
        return Workflow.of(session).getPageDescriptions().map(page -> {
            DAnalysis self = ((DAnalysisSession) session).allAnalyses().stream().findFirst().orElse(null);
            WorkflowDescription workflow = (WorkflowDescription) page.eContainer();
            Map<String, Object> variables = new HashMap<>();
            WorkflowExpressions.Variable.SELF.define(variables, self);
            WorkflowExpressions.Variable.STATE.define(variables, WorkflowStateAdapter.on(workflow).getStateValue());
            SiriusBackendInterpreter interpreter = new SiriusBackendInterpreter(session);
            String title = interpreter.evaluateExpression(variables, page.getTitleExpression()).asString();
            String identifier = page.getName();
            return new SiriusServerPageDto(identifier, title);
        }).collect(Collectors.toList());
    }

    /**
     * Returns the list of the sections of the current page.
     *
     * @param session
     *            The session
     * @return The list of the sections of the current page
     */
    private List<SiriusServerSectionDto> getFirstPageSections(Session session) {
        // @formatter:off
        return Workflow.of(session)
                .getFirstPageSections().stream()
                .map(sectionDescription -> this.convertSection(session, sectionDescription))
                .collect(Collectors.toList());
        // @formatter:on
    }

    /**
     * Converts the given {@link SectionDescription}.
     *
     * @param session
     *            The Sirius session
     * @param sectionDescription
     *            The section description
     * @return The section DTO
     */
    private SiriusServerSectionDto convertSection(Session session, SectionDescription sectionDescription) {
        String sectionIdentifier = sectionDescription.getName();

        DAnalysis self = ((DAnalysisSession) session).allAnalyses().stream().findFirst().orElse(null);
        Map<String, Object> variables = new HashMap<>();
        WorkflowExpressions.Variable.SELF.define(variables, self);
        IEvaluationResult result = new SiriusBackendInterpreter(session).evaluateExpression(variables, sectionDescription.getTitleExpression());
        String sectionName = result.asString();

        // @formatter:off
        List<SiriusServerActivityDto> activities = sectionDescription.getActivities().stream()
                .map(activityDescription -> this.convertActivity(session, activityDescription))
                .collect(Collectors.toList());
        // @formatter:on

        return new SiriusServerSectionDto(sectionIdentifier, sectionName, activities);
    }

    /**
     * Converts the given {@link ActivityDescription}.
     *
     * @param session
     *            The Sirius session
     * @param activityDescription
     *            The activity description
     * @return The activity DTO
     */
    private SiriusServerActivityDto convertActivity(Session session, ActivityDescription activityDescription) {
        String activityIdentifier = activityDescription.getName();

        DAnalysis self = ((DAnalysisSession) session).allAnalyses().stream().findFirst().orElse(null);
        Map<String, Object> variables = new HashMap<>();
        WorkflowExpressions.Variable.SELF.define(variables, self);
        IEvaluationResult result = new SiriusBackendInterpreter(session).evaluateExpression(variables, activityDescription.getLabelExpression());
        String activityName = result.asString();

        return new SiriusServerActivityDto(activityIdentifier, activityName);
    }

    /**
     * Returns the list of representations from the given session.
     *
     * @param session
     *            The session
     * @return The list of representations from the given session
     */
    private List<AbstractSiriusServerRepresentationDto> getRepresentations(Session session) {
        Collection<DRepresentationDescriptor> representationDescriptors = DialectManager.INSTANCE.getAllRepresentationDescriptors(session);
        // @formatter:off
		return representationDescriptors.stream()
			.filter(descriptor -> !descriptor.getDescription().eIsProxy())
			.map(this::convertToRepresentation)
			.collect(Collectors.toList());
		// @formatter:on
    }

    /**
     * Converts the given {@link DRepresentationDescriptor} into an
     * {@link AbstractSiriusServerRepresentationDto}.
     *
     * @param descriptor
     *            The descriptor
     * @return The {@link AbstractSiriusServerRepresentationDto}
     */
    private AbstractSiriusServerRepresentationDto convertToRepresentation(DRepresentationDescriptor descriptor) {
        String name = descriptor.getName();

        RepresentationDescription description = descriptor.getDescription();
        String descriptionName = description.getName();

        // @formatter:off
		String viewpointName = Optional.of(description.eContainer())
				.filter(Viewpoint.class::isInstance)
				.map(Viewpoint.class::cast)
				.map(Viewpoint::getName)
				.orElse(""); //$NON-NLS-1$
		// @formatter:on

        AbstractSiriusServerRepresentationDto representation = null;
        if (description instanceof DiagramDescription) {
            representation = new SiriusServerDiagramDto(viewpointName, descriptionName, name);
        } else if (description instanceof TableDescription) {
            representation = new SiriusServerTableDto(viewpointName, descriptionName, name);
        } else if (description instanceof TreeDescription) {
            representation = new SiriusServerTreeDto(viewpointName, descriptionName, name);
        }

        return representation;
    }

    /**
     * Returns the list of semantic resources from the given session.
     *
     * @param session
     *            The Sirius session
     * @return The list of semantic resources from the given session
     */
    private List<SiriusServerSemanticResourceDto> getSemanticResources(ModelingProject modelingProject) {
        // @formatter:off
		return SiriusServerUtils.getSemanticResources(modelingProject)
				.map(this::convertToSemanticResource)
				.collect(Collectors.toList());
		// @formatter:on
    }

    /**
     * Converts the given IFile into a {@link SiriusServerSemanticResourceDto}.
     *
     * @param iFile
     *            The semantic resource
     * @return A {@link SiriusServerSemanticResourceDto}.
     */
    private SiriusServerSemanticResourceDto convertToSemanticResource(IFile iFile) {
        String name = iFile.getName();
        String path = iFile.getProjectRelativePath().toString();

        long size = 0;

        File file = iFile.getLocation().toFile();
        try {
            size = Files.size(file.toPath());
        } catch (IOException exception) {
            IStatus status = new Status(IStatus.ERROR, SiriusServerBackendPlugin.PLUGIN_ID, exception.getMessage(), exception);
            SiriusServerBackendPlugin.getPlugin().log(status);
        }

        String sizeLabel = this.getSizeLabel(size);

        return new SiriusServerSemanticResourceDto(path, name, sizeLabel);
    }

    /**
     * Returns a label displaying the given size in KB or MB.
     *
     * @param size
     *            The size
     * @return The label
     */
    private String getSizeLabel(long size) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00"); //$NON-NLS-1$

        double kb = 1024d;
        double mb = kb * 1024d;

        if (size > mb) {
            return decimalFormat.format(size / mb) + "MB"; //$NON-NLS-1$
        }
        return decimalFormat.format(size / kb) + "KB"; //$NON-NLS-1$
    }

    @Override
    public SiriusServerResponse doPut(HttpServletRequest request, Map<String, String> variables, String remainingPart) {
        SiriusServerResponse response = null;
        try {
            Reader reader = new InputStreamReader(request.getInputStream(), SiriusServerUtils.UTF_8);
            SiriusServerUpdateProjectDescriptionDto updateProjectDescription = new Gson().fromJson(reader, SiriusServerUpdateProjectDescriptionDto.class);

            String projectName = variables.get(PROJECT_NAME);
            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
            if (project.exists()) {
                IProjectDescription description = project.getDescription();
                description.setComment(updateProjectDescription.getDescription());
                project.setDescription(description, new NullProgressMonitor());
                response = new SiriusServerResponse(STATUS_OK, new SiriusServerProjectDescriptionUpdatedDto(description.getComment()));
            } else {
                response = new SiriusServerResponse(STATUS_NOT_FOUND);
            }
        } catch (@SuppressWarnings("unused") IOException | CoreException exception) {
            // We don't want to send back the message of the exception
            response = new SiriusServerResponse(STATUS_INTERNAL_SERVER_ERROR);
        }

        return response;
    }
}
