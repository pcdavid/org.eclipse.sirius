/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.common.acceleo.mtl.business.internal.interpreter;

import java.util.Set;

import org.eclipse.acceleo.common.utils.IAcceleoCrossReferenceProvider;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.sirius.ext.emf.InverseReferenceFinder;

import com.google.common.collect.Sets;

/**
 * Implements the {@link IAcceleoCrossReferenceProvider} interface in order to
 * use the viewpoint cross referencer for "eInverse()" calls.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class AcceleoSiriusCrossReferencerProvider implements IAcceleoCrossReferenceProvider {
    /** The delegate cross referencer we'll be using. */
    private final InverseReferenceFinder crossReferencer;

    private final ECrossReferenceAdapter xref;

    /**
     * Instantiate our cross referencer provider given its delegate.
     * 
     * @param crossReferencer
     *            The delegate cross referencer.
     */
    public AcceleoSiriusCrossReferencerProvider(InverseReferenceFinder crossReferencer) {
        this.crossReferencer = crossReferencer;
        this.xref = null;
    }

    /**
     * Instantiate our cross referencer provider given its delegate.
     * 
     * @param xref
     *            The delegate cross referencer.
     */
    public AcceleoSiriusCrossReferencerProvider(ECrossReferenceAdapter xref) {
        this.crossReferencer = null;
        this.xref = xref;
    }

    @Override
    public Set<EObject> getInverseReferences(EObject eObject) {
        final Set<EObject> result = Sets.newLinkedHashSet();
        if (crossReferencer != null) {
            for (EStructuralFeature.Setting setting : crossReferencer.getInverseReferences(eObject)) {
                result.add(setting.getEObject());
            }
        } else {
            for (EStructuralFeature.Setting setting : xref.getInverseReferences(eObject)) {
                result.add(setting.getEObject());
            }
        }
        return result;
    }

    @Override
    public Set<EObject> getInverseReferences(EObject eObject, EClassifier filter) {
        final Set<EObject> result = Sets.newLinkedHashSet();
        if (crossReferencer != null) {
            for (EStructuralFeature.Setting setting : crossReferencer.getInverseReferences(eObject)) {
                final EObject eObj = setting.getEObject();
                if (filter == null || filter.isInstance(eObj)) {
                    result.add(eObj);
                }
            }
        } else {
            for (EStructuralFeature.Setting setting : xref.getInverseReferences(eObject)) {
                final EObject eObj = setting.getEObject();
                if (filter == null || filter.isInstance(eObj)) {
                    result.add(eObj);
                }
            }
        }
        return result;
    }
}
