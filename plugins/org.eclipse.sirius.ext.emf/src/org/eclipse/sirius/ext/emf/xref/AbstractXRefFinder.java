/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ext.emf.xref;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * An abstract {@link XRefFinder}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public abstract class AbstractXRefFinder implements XRefFinder {

    /** The contextual {@link ResourceSet}. */
    protected ResourceSet resourceSet;

    /**
     * Default constructor.
     *
     * @param resourceSet
     *            the {@link ResourceSet} on which query inverse cross
     *            references
     */
    public AbstractXRefFinder(ResourceSet resourceSet) {
        this.resourceSet = resourceSet;
    }

    @Override
    public Set<Setting> getSettings(EObject referencedEObject) {
        Set<Setting> settings = new HashSet<Setting>();
        Set<XRefResult> xRefResults = getXRefs(Collections.singleton(referencedEObject));
        for (XRefResult xRefResult : xRefResults) {
            InternalEObject referencingEObject = (InternalEObject) xRefResult.getReferencingEObject(true, true);
            settings.add(referencingEObject.eSetting(xRefResult.getEReference()));
        }
        return settings;
    }

    @Override
    public Set<XRefResult> getXRefs(EObject referencedEObject) {
        return getXRefs(Collections.singleton(referencedEObject));
    }

    @Override
    public Set<XRefResult> getXRefs(Set<? extends EObject> referencedEObjects) {
        return getXRefs(referencedEObjects, null);
    }

    @Override
    public Set<XRefResult> getXRefs(Set<? extends EObject> referencedEObjects, Criteria criteria) {
        return getXRefs(Collections.singleton(resourceSet), referencedEObjects, criteria);
    }

}
