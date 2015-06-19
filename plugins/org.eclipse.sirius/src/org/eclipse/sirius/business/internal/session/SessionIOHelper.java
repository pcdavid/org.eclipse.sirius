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

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Helper to find the {@code SessionIOHandler} appropriate to use for a given
 * resource.
 * 
 * @author pcdavid
 */
public final class SessionIOHelper {
    private SessionIOHelper() {
        // Prevent instanciation.
    }

    /**
     * Finds the most appropriate {@code SessionIOHandler} to use for the
     * specified resource.
     * 
     * @param resource
     *            the resource.
     * @return the prefered {@link SessionIOHandler} to use for IO-related
     *         operations on the resource.
     */
    public static SessionIOHandler getHandlerFor(Resource resource) {
        return new BasicSessionIOHandler();
    }

}
