/*******************************************************************************
 * Copyright (c) 2018 Obeo.
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
package org.eclipse.sirius.ui.debug;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * An arbitrary action that can be invoked from the debug view.
 * 
 * @author pcdavid
 */
public class DebugAction {
    private final String label;

    private final String description;

    private final Consumer<Object> body;

    public DebugAction(String label, String description, Consumer<Object> body) {
        this.label = Objects.requireNonNull(label);
        this.description = description;
        this.body = Objects.requireNonNull(body);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public void execute(Object selection) {
        body.accept(selection);
    }
}
