/*******************************************************************************
 * Copyright (c) 2009 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.tools.api.command.ui;

import org.eclipse.sirius.tools.internal.command.ui.RefreshFilterManagerImpl;

/**
 * The refresh filter manager.
 * 
 * @author mchauvin
 */
public interface RefreshFilterManager extends RefreshFilter {

    /**
     * Singleton instance of the refresh manager.
     */
    RefreshFilterManager INSTANCE = RefreshFilterManagerImpl.init();

    /**
     * Clear all the {@link RefreshFilter} associated with this
     * {@link EMFCommandFactory}.
     */
    void clearRefreshFilter();

    /**
     * Add a new {@link RefreshFilter}. It will be used in automatic refresh
     * mode to decide whether a {@link org.eclipse.sirius.diagram.DDiagram}
     * should be refreshed or not.
     * 
     * @param filter
     *            new filter.
     */
    void addRefreshFilter(RefreshFilter filter);

    /**
     * Remove a {@link RefreshFilter} from the current {@link EMFCommandFactory}
     * .
     * 
     * @param filter
     *            filter to remove.
     */
    void removeRefreshFilter(RefreshFilter filter);

}
