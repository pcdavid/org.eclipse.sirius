/*******************************************************************************
 * Copyright (c) 2013 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.common.ui.business.api.views.properties.tabbed;

import org.eclipse.jface.viewers.ILabelProvider;

/**
 * A provider of {@link ILabelProvider}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public interface ILabelProviderProvider {

    /**
     * Get a {@link ILabelProvider}.
     * 
     * @return the {@link ILabelProvider}
     */
    ILabelProvider getLabelProvider();

    /**
     * Tells if for the specified <code>selection</code> this
     * {@link ILabelProviderProvider} provides a {@link ILabelProvider}.
     * 
     * @param selection
     *            the specified selected {@link Object}
     * @return true if this {@link ILabelProviderProvider} provides a
     *         {@link ILabelProvider} for the specified selection
     */
    boolean provides(Object selection);
}
