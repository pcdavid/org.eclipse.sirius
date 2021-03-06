/*******************************************************************************
 * Copyright (c) 2010, 2015 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.diagram.ui.tools.internal.commands;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.ui.provider.Messages;

/**
 * Specific command to change synchronized diagram status.
 *
 * @author mporhel
 */
public class ChangeSynchronizedDagramStatusCommand extends RecordingCommand {

    private final DDiagram diagram;

    /**
     * Constructor.
     *
     * @param domain
     *            the editing domain.
     * @param diagram
     *            the diagram to update.
     */
    public ChangeSynchronizedDagramStatusCommand(TransactionalEditingDomain domain, DDiagram diagram) {
        super(domain, Messages.ChangeSynchronizedDagramStatusCommand_label);
        this.diagram = diagram;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doExecute() {
        if (diagram == null) {
            return;
        }

        diagram.setSynchronized(!diagram.isSynchronized());
    }
}
