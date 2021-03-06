/*******************************************************************************
 * Copyright (c) 2012 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.common.ui.tools.api.view;


/**
 * Interface for views which support reveal.
 * <p>
 * This interface may be implemented by subclasses of <code>ViewPart</code>.
 * </p>
 * 
 * @see org.eclipse.ui.IViewPart
 * @see org.eclipse.ui.part.ViewPart
 * @see org.eclipse.ui.part.ISetSelectionTarget
 * 
 * @author <a href="mailto:laurent.redor@obeo.fr">Laurent Redor</a>
 */
public interface IExpandSelectionTarget {
    /**
     * Expands the given element within this target view.
     * 
     * @param elementOrTreePath
     *            the element to expand
     */
    void expand(Object elementOrTreePath);
}
