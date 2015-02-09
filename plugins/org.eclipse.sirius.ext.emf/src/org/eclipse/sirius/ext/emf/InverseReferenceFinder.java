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
package org.eclipse.sirius.ext.emf;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Represents the generic service of locating all the references that point to a
 * given model element.
 * 
 * @author pcdavid
 */
public interface InverseReferenceFinder {
    /**
     * Find all the references in the context that point to the specified
     * {@link EObject}.
     * 
     * @param eObject
     *            the target eObject
     * @return all the references that point to the target.
     */
    Collection<EStructuralFeature.Setting> getInverseReferences(EObject eObject);

    /**
     * Find all the references in the context that point to the specified
     * {@link EObject}.
     * 
     * @param eObject
     *            the target eObject
     * @param resolve
     *            .
     * @return all the references that point to the target.
     */
    Collection<EStructuralFeature.Setting> getInverseReferences(EObject eObject, boolean resolve);
}
