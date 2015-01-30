/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.resources;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * High-level operations on EMF resources and their contents, which can be
 * provided by different concrete implementations (with different performance
 * characteristics) depening on the resource type and context.
 * 
 * @author pcdavid
 */
// CHECKSTYLE:OFF
public interface ResourceOperationsProvider {
    boolean provides(URI resourceURI);

    IStatus unload(Resource res, IProgressMonitor monitor);

    IStatus reload(Resource res, IProgressMonitor monitor);

    Collection<URI> findAllReferencedResources(URI resource, IProgressMonitor monitor);

    Collection<URI> findAllMetamodels(URI resource, IProgressMonitor monitor);
}
