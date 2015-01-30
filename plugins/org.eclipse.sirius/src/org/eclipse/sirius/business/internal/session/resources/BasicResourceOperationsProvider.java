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
 * Provides basic implementations of the standard resource operations which
 * should be suitable for all EMF resources but possibly sub-optimal for many.
 * 
 * @author pcdavid
 */
public class BasicResourceOperationsProvider implements ResourceOperationsProvider {
    @Override
    public boolean provides(URI resourceURI) {
        return true;
    }

    @Override
    public IStatus unload(Resource res, IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IStatus reload(Resource res, IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<URI> findAllReferencedResources(URI resource, IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<URI> findAllMetamodels(URI resource, IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        return null;
    }

}
