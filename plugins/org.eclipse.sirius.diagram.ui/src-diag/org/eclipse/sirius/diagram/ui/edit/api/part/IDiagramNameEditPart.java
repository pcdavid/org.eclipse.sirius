/*******************************************************************************
 * Copyright (c) 2007, 2008, 2009 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.ui.edit.api.part;

import java.util.function.Supplier;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;

/**
 * An interface for edit part which can have a label.
 * 
 * @author ymortier
 */
public interface IDiagramNameEditPart extends IDiagramElementEditPart, ITextAwareEditPart {

    /**
     * Sets the label of the figure.
     * 
     * @param label
     *            the label.
     */
    void setLabel(IFigure label);

    /**
     * Sets the text of the element's tooltip.
     * 
     * @param text
     *            the text to show in the tooltip. If <code>null</code> or the empty string, the element's tooltip is
     *            disabled.
     * @since 0.9.0
     * @deprecated Use {@link #setTooltipTextProvider(Supplier)}
     */
    @Deprecated
    void setTooltipText(String text);

    /**
     * Sets the text of the element's tooltip.
     * 
     * @param textSupplier
     *            the supplier from which to obtain the text to show in the tooltip. If it returns <code>null</code> or
     *            the empty string, the element's tooltip is disabled.
     * @since 6.4.0
     */
    void setTooltipTextProvider(Supplier<String> textSupplier);

}
