/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.properties.editor.properties.filters.properties.abstractdynamicmappingfordescription;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.editor.properties.filters.common.ViewpointPropertyFilter;
import org.eclipse.sirius.properties.PropertiesPackage;

/**
 * A filter for the force refresh property section.
 * 
 * @author sbegaudeau
 */
public class AbstractDynamicMappingForDescriptionForceRefreshFilterSpec extends ViewpointPropertyFilter {
    @Override
    protected EStructuralFeature getFeature() {
        return PropertiesPackage.eINSTANCE.getAbstractDynamicMappingForDescription_ForceRefresh();
    }

    @Override
    protected boolean isRightInputType(Object arg0) {
        return arg0 instanceof org.eclipse.sirius.properties.AbstractDynamicMappingForDescription;
    }
}
