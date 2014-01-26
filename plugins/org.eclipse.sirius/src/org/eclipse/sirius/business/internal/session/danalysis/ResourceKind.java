/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

/**
 * The different (exclusive) categories of resources which can be found in a
 * Sirius Session.
 */
public enum ResourceKind {
    /**
     * Session resources contain the session's state and the representation's
     * data. They are normally stored in <code>*.aird</code> files.
     */
    SESSION,
    /**
     * The definitions for all the viewpoints referenced by the session, and any
     * anciliarry data (e.g. the environment) correspond to the specification
     * for the various representations in the session.
     */
    SPECIFICATION,
    /**
     * All resources which are not in any of the first two categories are by
     * definition semantic data.
     */
    SEMANTIC
}
