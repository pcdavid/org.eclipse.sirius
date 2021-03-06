/*******************************************************************************
 * Copyright (c) 2007, 2008 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.business.api.session;

/**
 * The synchronization status of a Session.
 * 
 * @author cbrun
 * @since 0.9.0
 * 
 */
public enum SessionStatus {
    /**
     * The session is dirty and has data to synchronize.
     */
    DIRTY,
    /**
     * The session is in sync, all the data is saved.
     */
    SYNC
}
