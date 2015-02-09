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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Default implementation of {@link XRefResult}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class XRefResultImpl implements XRefResult {

    private ResourceSet resourceSet;

    private URI referencingEObjectURI;

    private EObject referencingEObject;

    private URI referencedEObjectURI;

    private EObject referencedEObject;

    private EReference reference;

    /**
     * Default constructor.
     * 
     * @param resourceSet
     *            the contextual {@link ResourceSet}
     * @param referencingEObjectURI
     *            {@link URI} of the referencing {@link EObject}
     * @param referencingEObject
     *            referencing {@link EObject}
     * @param referencedEObjectURI
     *            {@link URI} of the referenced {@link EObject}
     * @param referencedEObject
     *            referenced {@link EObject}
     * @param reference
     *            the {@link EReference} from referencingEObject to
     *            referencedEObject
     */
    public XRefResultImpl(ResourceSet resourceSet, URI referencingEObjectURI, EObject referencingEObject, URI referencedEObjectURI, EObject referencedEObject, EReference reference) {
        this.resourceSet = resourceSet;
        this.referencingEObjectURI = referencingEObjectURI;
        this.referencingEObject = referencingEObject;
        this.referencedEObjectURI = referencedEObjectURI;
        this.referencedEObject = referencedEObject;
        this.reference = reference;
    }

    @Override
    public URI getReferencingEObjectURI() {
        return referencingEObjectURI;
    }

    @Override
    public EObject getReferencingEObject(boolean load, boolean resolve) {
        if (referencingEObject == null) {
            referencingEObject = resourceSet.getEObject(referencingEObjectURI, load);
        }
        if (referencingEObject != null && referencingEObject.eIsProxy()) {
            referencingEObject = EcoreUtil.resolve(referencingEObject, resourceSet);
        }
        return referencingEObject;
    }

    @Override
    public URI getReferencedEObjectURI() {
        return referencedEObjectURI;
    }

    @Override
    public EObject getReferencedEObject(boolean load, boolean resolve) {
        if (referencedEObject == null) {
            referencedEObject = resourceSet.getEObject(referencedEObjectURI, load);
        }
        if (referencedEObject != null && referencedEObject.eIsProxy()) {
            referencedEObject = EcoreUtil.resolve(referencedEObject, resourceSet);
        }
        return referencedEObject;
    }

    @Override
    public EReference getEReference() {
        return reference;
    }

}
