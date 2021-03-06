/*******************************************************************************
 * Copyright (c) 2007, 2018 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.editor.properties.filters.description.diagramelementmapping;

// Start of user code specific imports

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.diagram.description.DescriptionPackage;
import org.eclipse.sirius.diagram.description.EdgeMapping;
import org.eclipse.sirius.editor.properties.filters.common.ViewpointPropertyFilter;

// End of user code specific imports

/**
 * A filter for the semanticCandidatesExpression property section.
 */
public class DiagramElementMappingSemanticCandidatesExpressionFilter extends ViewpointPropertyFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    protected EStructuralFeature getFeature() {
        return DescriptionPackage.eINSTANCE.getDiagramElementMapping_SemanticCandidatesExpression();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRightInputType(Object arg0) {
        return arg0 instanceof org.eclipse.sirius.diagram.description.DiagramElementMapping;
    }

    // Start of user code user methods
    @Override
    public boolean select(Object arg0) {
        if (isEdgeMapping(arg0) && isNormalEdgeMapping(arg0) && !((EdgeMapping) arg0).isUseDomainElement()) {
            return false;
        }
        return super.select(arg0);
    }

    private boolean isEdgeMapping(Object obj) {
        return DescriptionPackage.eINSTANCE.getEdgeMapping().isInstance(obj);
    }

    private boolean isNormalEdgeMapping(Object obj) {
        return ((EObject) obj).eClass().equals(DescriptionPackage.eINSTANCE.getEdgeMapping());
    }
    // End of user code user methods

}
