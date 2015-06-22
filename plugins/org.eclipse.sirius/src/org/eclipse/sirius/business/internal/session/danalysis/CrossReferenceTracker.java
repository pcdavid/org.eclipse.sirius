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
package org.eclipse.sirius.business.internal.session.danalysis;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;

/**
 * Interface to get notifications about the addition or removal of
 * cross-references between model elements.
 * 
 * @author pcdavid
 */
public interface CrossReferenceTracker {
    /**
     * Called when a new cross-reference is created between two objects.
     * 
     * @param source
     *            the source element.
     * @param ref
     *            the reference through which the source points to the target.
     * @param target
     *            the target element.
     */
    void onReferenceAdded(InternalEObject source, EReference ref, EObject target);

    /**
     * Called when a cross-reference between two objects is removed.
     * 
     * @param source
     *            the source element.
     * @param ref
     *            the reference through which the source pointed to the target.
     * @param target
     *            the target element.
     */
    void onReferenceRemoved(EObject source, EReference ref, EObject target);
}
