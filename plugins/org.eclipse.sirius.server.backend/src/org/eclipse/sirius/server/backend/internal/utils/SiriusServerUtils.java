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
package org.eclipse.sirius.server.backend.internal.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.modelingproject.ModelingProject;
import org.eclipse.sirius.business.api.session.DefaultLocalSessionCreationOperation;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionCreationOperation;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.server.backend.internal.SiriusServerBackendPlugin;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

/**
 * Utility class.
 *
 * @author sbegaudeau
 */
public final class SiriusServerUtils {

    /**
     * The UTF-8 encoding.
     */
    public static final String UTF_8 = "UTF-8"; //$NON-NLS-1$

    /**
     * The constructor.
     */
    private SiriusServerUtils() {
        // prevent instantiation
    }

    /**
     * Returns the {@link ViewpointRegistry} used by this server instance.
     *
     * @return the {@link ViewpointRegistry} used by this server instance.
     */
    public static ViewpointRegistry getViewpointRegistry() {
        return ViewpointRegistry.getInstance();
    }

    /**
     * Returns the global EPackage registry used by this server instance.
     *
     * @return the global EPackage registry used by this server instance.
     */
    public static EPackage.Registry getGlobalEPackagesRegistry() {
        return EPackage.Registry.INSTANCE;
    }

    /**
     * Checks whether a project with the given name already exists.
     *
     * @param projectName
     *            the name to test.
     * @return whether or not there is already a project with the given name.
     */
    public static boolean projectExists(String projectName) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).exists();
    }

    /**
     * Create a new empty modeling project with the given name.
     *
     * @param name
     *            the name of the new project.
     * @return the newly created project, with the corresponding session already
     *         opened.
     * @throws CoreException
     *             if the project could not be created.
     */
    public static IProject createProject(String name) throws CoreException {
        // TODO Improve this code once this issue is fixed:
        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=533931
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
        IProjectDescription projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(name);
        projectDescription.setNatureIds(new String[] { ModelingProject.NATURE_ID });
        project.create(projectDescription, new NullProgressMonitor());
        project.open(new NullProgressMonitor());

        URI representationsURI = URI.createPlatformResourceURI(project.getFullPath().append(ModelingProject.DEFAULT_REPRESENTATIONS_FILE_NAME).toString(), true);
        SessionCreationOperation sessionCreationOperation = new DefaultLocalSessionCreationOperation(representationsURI, new NullProgressMonitor());
        sessionCreationOperation.execute();
        return project;
    }

    /**
     * Finds the modeling project with the given name.
     *
     * @param projectName
     *            The name of the project
     * @return An optional with the modeling project or an empty optional if it
     *         could not be found
     */
    public static Optional<ModelingProject> findModelingProjectByName(String projectName) {
        Optional<IProject> optionalProject = Optional.ofNullable(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName));
        // @formatter:off
        return optionalProject.filter(ModelingProject::hasModelingProjectNature)
                .filter(IProject::isOpen)
                .map(iProject -> ModelingProject.asModelingProject(iProject).get()); // FIXME Sirius Optional removal!
        // @formatter:on
    }

    /**
     * Returns the session from the name of a modeling project in the workspace.
     *
     * @param projectName
     *            the name of the modeling project
     * @return the session if the modeling project exists.
     */
    public static Optional<Session> getSessionFromProject(String projectName) {
        // @formatter:off
        Optional<IProject> optionalProject = Optional.ofNullable(ResourcesPlugin.getWorkspace().getRoot().getProject(projectName));
        Optional<ModelingProject> optionalModelingProject = optionalProject.filter(ModelingProject::hasModelingProjectNature)
                .filter(IProject::isOpen)
                .map(iProject -> ModelingProject.asModelingProject(iProject).get());
        // @formatter:on
        if (optionalModelingProject.isPresent()) {
            ModelingProject modelingProject = optionalModelingProject.get();
            return Optional.of(getSession(modelingProject));
        }
        return Optional.empty();
    }

    /**
     * Returns the session of the given modeling project or open a new one and
     * return it.
     *
     * @param modelingProject
     *            The modeling project
     * @return The session
     */
    public static Session getSession(ModelingProject modelingProject) {
        Optional<Session> optionalSession = Optional.ofNullable(modelingProject.getSession());
        Session session = optionalSession.orElseGet(() -> {
            // FIXME SBE: proper management of optionals once Sirius has
            // switched to Java optional
            URI sessionResourceURI = modelingProject.getMainRepresentationsFileURI(new NullProgressMonitor()).get();
            return SessionManager.INSTANCE.openSession(sessionResourceURI, new NullProgressMonitor(), SiriusPlugin.getDefault().getUiCallback());
        });
        return session;
    }

    /**
     * Returns a stream of the projects with the modeling project nature in the
     * workspace.
     *
     * @return A stream of the projects with the modeling project nature in the
     *         workspace
     */
    public static Stream<IProject> getModelingProjects() {
        IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        // @formatter:off
        return Arrays.stream(allProjects)
                .filter(ModelingProject::hasModelingProjectNature)
                .filter(IProject::isOpen);
        // @formatter:on
    }

    /**
     * Returns the description from the given project.
     *
     * @param project
     *            The project
     * @return The description from the given project
     */
    public static String getProjectDescription(IProject project) {
        String description = null;

        try {
            IProjectDescription iProjectDescription = project.getDescription();
            String comment = iProjectDescription.getComment();
            if (comment != null && comment.trim().length() > 0) {
                description = comment;
            }
        } catch (CoreException e) {
            IStatus status = new Status(IStatus.ERROR, SiriusServerBackendPlugin.PLUGIN_ID, e.getMessage(), e);
            SiriusServerBackendPlugin.getPlugin().log(status);
        }
        return description;
    }

    /**
     * Returns the list of semantic resources from the given project.
     *
     * @param modelingProject
     *            the modeling project
     * @return the list of semantic resources from the given project.
     */
    public static Stream<IFile> getSemanticResources(ModelingProject modelingProject) {
        Session session = getSession(modelingProject);
        // @formatter:off
        return session.getSemanticResources().stream()
                .map(Resource::getURI)
                .filter(URI::isPlatformResource)
                .map(uri -> {
                    String platformString = uri.toPlatformString(true);
                    return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
                })
                .filter(iFile -> iFile.getProject().equals(modelingProject.getProject()));
        // @formatter:on
    }

}
