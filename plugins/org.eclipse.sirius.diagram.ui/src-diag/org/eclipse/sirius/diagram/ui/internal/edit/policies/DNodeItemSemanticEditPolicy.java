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
package org.eclipse.sirius.diagram.ui.internal.edit.policies;

import java.util.Objects;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DiagramPackage;
import org.eclipse.sirius.diagram.ui.internal.edit.commands.DNodeCreateCommand;
import org.eclipse.sirius.diagram.ui.internal.providers.SiriusElementTypes;

public class DNodeItemSemanticEditPolicy extends AbstractDNodeItemSemanticEditPolicy {

    private final IElementType elementType;

    public DNodeItemSemanticEditPolicy() {
        this(SiriusElementTypes.DNode_3001);
    }

    public DNodeItemSemanticEditPolicy(IElementType elementType) {
        this.elementType = Objects.requireNonNull(elementType);
    }

    @Override
    protected Command getCreateCommand(CreateElementRequest req) {
        if (elementType == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(DiagramPackage.eINSTANCE.getAbstractDNode_OwnedBorderedNodes());
            }
            return getGEFWrapper(new DNodeCreateCommand(req, DiagramPackage.eINSTANCE.getAbstractDNode()));
        }
        return super.getCreateCommand(req);
    }

    @Override
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        // addDestroyChildNodesCommand(cc);
        addDestroyShortcutsCommand(cc);
        View view = (View) getHost().getModel();
        if (view.getEAnnotation("Shortcut") != null) { //$NON-NLS-1$
            req.setElementToDestroy(view);
        }
        cc.add(getGEFWrapper(new DestroyElementCommand(req)));
        return cc.unwrap();
    }
}
