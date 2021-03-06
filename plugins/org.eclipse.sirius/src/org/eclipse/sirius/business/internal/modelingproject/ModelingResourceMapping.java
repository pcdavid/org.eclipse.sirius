/*******************************************************************************
 * Copyright (c) 2011, 2018 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.business.internal.modelingproject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.resources.mapping.ResourceMappingContext;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Resource mapping for the modeling model provider.
 *
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public final class ModelingResourceMapping extends ResourceMapping {

    /** The resource to map */
    private final IResource fResource;

    /**
     * Creates a new java resource mapping.
     *
     * @param resource
     *            the resource to map
     */
    public ModelingResourceMapping(final IResource resource) {
        Assert.isNotNull(resource);
        fResource = resource;
    }

    @Override
    public Object getModelObject() {
        return fResource;
    }

    @Override
    public String getModelProviderId() {
        return ModelingModelProvider.MODELING_MODEL_PROVIDER_ID;
    }

    @Override
    public IProject[] getProjects() {
        return new IProject[] { fResource.getProject() };
    }

    @Override
    public ResourceTraversal[] getTraversals(final ResourceMappingContext context, final IProgressMonitor monitor) {
        return new ResourceTraversal[] { new ResourceTraversal(new IResource[] { fResource }, IResource.DEPTH_INFINITE, IResource.NONE) };
    }
}
