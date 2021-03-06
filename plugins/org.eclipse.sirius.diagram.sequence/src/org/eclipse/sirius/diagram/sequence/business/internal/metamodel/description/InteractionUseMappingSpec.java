/*******************************************************************************
 * Copyright (c) 2010, 2018 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.diagram.sequence.business.internal.metamodel.description;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.common.tools.api.util.EObjectCouple;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.business.internal.metamodel.description.extensions.IContainerMappingExt;
import org.eclipse.sirius.diagram.business.internal.metamodel.helper.ContainerMappingWithInterpreterHelper;
import org.eclipse.sirius.diagram.sequence.description.impl.InteractionUseMappingImpl;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

/**
 * Implementation of InteractionUseMapping.
 * 
 * @author pcdavid
 */
public class InteractionUseMappingSpec extends InteractionUseMappingImpl implements IContainerMappingExt {

    private final Map<EObject, EList<DSemanticDecorator>> viewContainerDone = new HashMap<EObject, EList<DSemanticDecorator>>();

    private final Map<EObjectCouple, EList<EObject>> candidatesCache = new WeakHashMap<EObjectCouple, EList<EObject>>();

    @Override
    public Map<EObject, EList<DSemanticDecorator>> getViewContainerDone() {
        return viewContainerDone;
    }

    @Override
    public Map<EObjectCouple, EList<EObject>> getCandidatesCache() {
        return candidatesCache;
    }


    @Override
    public EList<DDiagramElement> findDNodeFromEObject(final EObject object) {
        return ContainerMappingWithInterpreterHelper.findDNodeFromEObject(this, object);
    }


    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append(" ").append(getName()).toString(); //$NON-NLS-1$
    }
}
