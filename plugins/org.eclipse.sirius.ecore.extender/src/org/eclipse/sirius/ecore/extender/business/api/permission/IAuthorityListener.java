/*******************************************************************************
 * Copyright (c) 2007, 2011 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.ecore.extender.business.api.permission;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

/**
 * Listener for the {@link IPermissionAuthority} state changes.
 * 
 * @author cbrun
 * 
 */
public interface IAuthorityListener {
    /**
     * Called when an instance is locked.
     * 
     * @param instance
     *            any instance.
     */
    void notifyIsLocked(EObject instance);

    /**
     * Called when an instance is un-locked.
     * 
     * @param instance
     *            any instance.
     */
    void notifyIsReleased(EObject instance);
    
    /**
     * Called when many elements are locked.
     * 
     * @param instances
     *             locked instances.
     */
    void notifyIsLocked(Collection<EObject> instances);

    /**
     * Called when many elements are  unlocked.
     * 
     * @param instances
     *            unlocked instances.
     */
    void notifyIsReleased(Collection<EObject> instances);
}
