/*******************************************************************************
 * Copyright (c) 2011, 2015 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.tools.internal.views.common.item;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.sirius.business.api.modelingproject.ModelingProject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.ui.tools.api.views.common.item.ProjectDependenciesItem;
import org.eclipse.sirius.viewpoint.DAnalysisSessionEObject;
import org.eclipse.sirius.viewpoint.provider.Messages;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.graphics.Image;

import com.google.common.collect.Sets;

/**
 * Display the semantic models associated to the session but not in the current
 * project.
 *
 * @author mchauvin
 */
public class ProjectDependenciesItemImpl implements ProjectDependenciesItem {

    /**
     * Current project, in which the session should be.
     */
    private final ModelingProject project;

    /**
     * Construct a new instance.
     *
     * @param project
     *            the project
     */
    public ProjectDependenciesItemImpl(ModelingProject project) {
        this.project = project;
    }

    @Override
    public boolean hasChildren() {
        Collection<Object> children = Sets.newLinkedHashSet();
        return addChildren(children, true);
    }

    /**
     * Get the children of this item.
     *
     * @return the children, never <code>null</code>
     */
    @Override
    public Collection<?> getChildren() {
        Collection<Object> children = Sets.newLinkedHashSet();
        addChildren(children, false);
        return children;
    }

    // CHECKSTYLE:OFF
    private boolean addChildren(Collection<Object> result, boolean checkOnly) {
        boolean hasChildren = false;
        Session session = project.getSession();
        if (session != null) {
            /*
             * Retrieve all resources of the session not located directly in the
             * project
             */
            hasChildren = addProjectDependencies(session.getSemanticResources(), result, checkOnly);
            if (hasChildren && checkOnly) {
                return true;
            }
            if (session instanceof DAnalysisSessionEObject) {
                hasChildren = hasChildren || addProjectDependencies(((DAnalysisSessionEObject) session).getControlledResources(), result, checkOnly);
                if (hasChildren && checkOnly) {
                    return true;
                }
            }

            Collection<Object> analysesDeps = Sets.newLinkedHashSet();
            hasChildren = hasChildren || addProjectDependencies(session.getAllSessionResources(), analysesDeps, checkOnly);
            if (hasChildren && checkOnly) {
                return true;
            }
            for (Object analysisRes : analysesDeps) {
                if (analysisRes instanceof Resource) {
                    hasChildren = true;
                    if (checkOnly) {
                        return true;
                    }
                    result.add(new AnalysisResourceItemImpl(session, (Resource) analysisRes, this));
                }
            }
        }
        return hasChildren;
    }
    // CHECKSTYLE:ON

    private boolean addProjectDependencies(Iterable<Resource> dependencies, Collection<Object> result, boolean checkOnly) {
        boolean hasChildren = false;
        for (Resource resource : dependencies) {
            final URI uri = resource.getURI();
            if (uri.isPlatformResource()) {
                final IFile file = WorkspaceSynchronizer.getFile(resource);
                if (file != null) {
                    if (!project.getProject().equals(file.getProject())) {
                        hasChildren = true;
                        if (checkOnly) {
                            break;
                        }
                        result.add(resource);
                    }
                }
            } else {
                /*
                 * resource do not have a platform uri, so it could not be
                 * directly in the project
                 */
                hasChildren = true;
                if (checkOnly) {
                    break;
                }
                result.add(resource);
            }
        }
        return hasChildren;
    }

    @Override
    public String getText() {
        return Messages.ProjectDependenciesItemImpl_text;
    }

    @Override
    public Image getImage() {
        return SiriusEditPlugin.getPlugin().getBundledImage("icons/obj16/ProjectDependencies.gif"); //$NON-NLS-1$
    }

    /**
     * Get the parent project.
     *
     * @return the parent project, could not be <code>null</code>
     */
    public IProject getProject() {
        return project == null ? null : project.getProject();
    }

    @Override
    @Override
    public Object getParent() {
        return getProject();
    }

    /**
     * Get the parent {@link ModelingProject} project.
     *
     * @return the parent project, could not be <code>null</code>
     */
    public ModelingProject getModelingProject() {
        return this.project;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((project == null) ? 0 : project.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = true;
        if (this == obj) {
            equals = true;
        } else if (obj == null) {
            equals = false;
        } else if (getClass() != obj.getClass()) {
            equals = false;
        } else {
            ProjectDependenciesItemImpl other = (ProjectDependenciesItemImpl) obj;
            if (project == null) {
                if (other.project != null) {
                    equals = false;
                }
            } else if (!project.equals(other.project)) {
                equals = false;
            }
        }
        return equals;
    }

    /**
     * Return the session of the modeling project.
     *
     * @return the session of the modeling project
     */
    @Override
    public Option<Session> getSession() {
        if (project != null) {
            return Options.newSome(project.getSession());
        }
        return Options.newNone();
    }

}
