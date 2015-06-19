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
package org.eclipse.sirius.business.internal.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.session.Session;

/**
 * The simplest {@code SessionIOHandler} possible, which directly forwards each
 * request to the target resource.
 * 
 * @author pcdavid
 */
public class BasicSessionIOHandler implements SessionIOHandler {

    @Override
    public void load(Resource resource, Map<?, ?> options) throws IOException {
        resource.load(options);
    }

    @Override
    public void load(Resource resource, InputStream inputStream, Map<?, ?> options) throws IOException {
        resource.load(inputStream, options);
    }

    @Override
    public void save(Resource resource, Map<?, ?> options) throws IOException {
        resource.save(options);
    }

    @Override
    public void save(Resource resource, OutputStream outputStream, Map<?, ?> options) throws IOException {
        resource.save(outputStream, options);
    }

    @Override
    public void unload(Resource resource) {
        resource.unload();
    }

    @Override
    public void delete(Resource resource, Map<?, ?> options) throws IOException {
        resource.delete(options);
    }

    @Override
    public void execute(Session session, Runnable task) {
        task.run();
    }
    
}
