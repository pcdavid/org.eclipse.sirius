/*******************************************************************************
 * Copyright (c) 2007, 2018 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.diagram.ui.internal.edit.commands;

import java.util.Objects;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DiagramPackage;

/**
 * @was-generated
 */
public class DNodeCreateCommand extends CreateElementCommand {

    private final EClass eClassToEdit;

    public DNodeCreateCommand(CreateElementRequest req, EClass eClassToEdit) {
        super(req);
        this.eClassToEdit = Objects.requireNonNull(eClassToEdit);
    }

    public DNodeCreateCommand(CreateElementRequest req) {
        this(req, DiagramPackage.eINSTANCE.getDDiagram());
    }

    @Override
    protected EObject getElementToEdit() {
        EObject container = ((CreateElementRequest) getRequest()).getContainer();
        if (container instanceof View) {
            container = ((View) container).getElement();
        }
        return container;
    }

    @Override
    protected EClass getEClassToEdit() {
        return eClassToEdit;
    }

    @Override
    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        return null;
    }
}
