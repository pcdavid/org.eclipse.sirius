/*******************************************************************************
 * Copyright (c) 2020 Obeo.
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
package org.eclipse.sirius.diagram.ui.graphical.figures;

import java.util.Objects;
import java.util.function.Supplier;

import org.eclipse.draw2d.Label;

/**
 * A label whose text is not known at construction-time, but only computed on-demand.
 * 
 * @author pcdavid
 */
public class LazyLabel extends Label {
    
    private final Supplier<String> textSupplier;
    
    private boolean textComputed;

    /**
     * Constructor.
     * 
     * @param textSupplier the code to invoke to obtain the actual text.
     */
    public LazyLabel(Supplier<String> textSupplier) {
        this.textSupplier = Objects.requireNonNull(textSupplier);
    }
    
    @Override
    public String getText() {
        if (!textComputed) {
            this.setText(this.textSupplier.get());
        }
        return super.getText();
    }

}
