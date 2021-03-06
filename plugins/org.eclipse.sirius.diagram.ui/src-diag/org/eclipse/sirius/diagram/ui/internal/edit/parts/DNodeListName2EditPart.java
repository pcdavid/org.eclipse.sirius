/*******************************************************************************
 * Copyright (c) 2007, 2015 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.diagram.ui.internal.edit.parts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.ui.internal.providers.SiriusElementTypes;

/**
 * @was-generated
 */
public class DNodeListName2EditPart extends AbstractDiagramListNameEditPart implements ITextAwareEditPart {

    /**
     * @was-generated
     */
    public static final int VISUAL_ID = 5004;

    /**
     * @was-generated
     */
    public DNodeListName2EditPart(final View view) {
        super(view);
    }

    @Override
    protected IElementType getParserElementType() {
        return SiriusElementTypes.DNodeList_3009;
    }
}
