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

/**
 * A type to contains result of inverse cross references query.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public interface XRefResult {

    /**
     * Get {@link URI} of referencing {@link EObject}.
     * 
     * @return {@link URI} of referencing {@link EObject}.
     */
    URI getReferencingEObjectURI();

    /**
     * Get referencing {@link EObject}.
     * 
     * @param load
     *            true to load the {@link EObject} if not loaded, if false null
     *            will be returned
     * @param resolve
     *            true to resolve the {@link EObject} proxy, imply load at true
     * @return referencing {@link EObject}.
     */
    EObject getReferencingEObject(boolean load, boolean resolve);

    /**
     * Get {@link URI} of referenced {@link EObject}.
     * 
     * @return {@link URI} of referenced {@link EObject}.
     */
    URI getReferencedEObjectURI();

    /**
     * Get referenced {@link EObject}.
     * 
     * @param load
     *            true to load the {@link EObject} if not loaded, if false null
     *            will be returned
     * @param resolve
     *            true to resolve the {@link EObject} proxy, imply load at true
     * @return referencing {@link EObject}.
     */
    EObject getReferencedEObject(boolean load, boolean resolve);

    /**
     * Get the {@link EReference} used for the cross reference.
     * 
     * @return the {@link EReference} used for the cross reference
     */
    EReference getEReference();
}
