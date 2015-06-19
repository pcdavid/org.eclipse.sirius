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
 * Facade to all the I/O operations performed on a session's resources.
 * 
 * @author pcdavid
 */
public interface SessionIOHandler {
    /**
     * Invokes {@link Resource#load(Map)} on the specified resource, which must
     * be part of the session's resource set.
     * 
     * @param resource
     *            the resource to load.
     * @param options
     *            the load options.
     * @throws IOException
     *             if the resource could not be loaded.
     */
    void load(Resource resource, Map<?, ?> options) throws IOException;

    /**
     * Invokes {@link Resource#load(InputStream, Map)} on the specified
     * resource, which must be part of the session's resource set.
     * 
     * @param resource
     *            the resource to load.
     * @param inputStream
     *            the stream from which to load the resource.
     * @param options
     *            the load options.
     * @throws IOException
     *             if the resource could not be loaded.
     */
    void load(Resource resource, InputStream inputStream, Map<?, ?> options) throws IOException;

    /**
     * Invokes {@link Resource#save(Map)} on the specified resource, which must
     * be part of the session's resource set.
     * 
     * @param resource
     *            the resource to save.
     * @param options
     *            the save options.
     * @throws IOException
     *             if the resource could not be saved.
     */
    void save(Resource resource, Map<?, ?> options) throws IOException;

    /**
     * Invokes {@link Resource#save(InputStream, Map)} on the specified
     * resource, which must be part of the session's resource set.
     * 
     * @param resource
     *            the resource to save.
     * @param outputStream
     *            the stream to which to save the resource.
     * @param options
     *            the save options.
     * @throws IOException
     *             if the resource could not be saved.
     */
    void save(Resource resource, OutputStream outputStream, Map<?, ?> options) throws IOException;

    /**
     * Invokes {@link Resource#unload()} on the specified resource, which must
     * be part of the session's resource set.
     * 
     * @param resource
     *            the resource to unload.
     */
    void unload(Resource resource);

    /**
     * Invokes {@link Resource#delete(Map)} on the specified resource, which
     * must be part of the session's resource set.
     * 
     * @param resource
     *            the resource to delete.
     * @param options
     *            the delete options.
     * @throws IOException
     *             if the resource could not be deleted.
     */
    void delete(Resource resource, Map<?, ?> options) throws IOException;

    /**
     * Execute an opaque task which may perform direct IO operations in the
     * session's resource, bypassing the methods in this interface.
     * Implementation <em>must</em> be re-entrant, and support executing tasks
     * which aggregate calls to this {@link SessionIOHandler}'s instance.
     * 
     * @param session
     *            the session on behalf of which the task is performed.
     * @param task
     *            the task to execute.
     */
    void execute(Session session, Runnable task);
}
