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

import java.util.Set;

import org.eclipse.emf.ecore.EReference;

/**
 * A default implementation of {@link Criteria}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public interface DefaultCriteria extends Criteria {

    /**
     * Get {@link EReference references} to consider.
     * 
     * @return {@link EReference references} to consider
     */
    Set<EReference> getReferencesToConsider();

    /**
     * Get {@link EReference references} to ignore.
     * 
     * @return {@link EReference references} to ignore
     */
    Set<EReference> getReferencesToIgnore();

    /**
     * Get {@link EReference references} to consider.
     * 
     * @param referencesToConsider
     *            {@link EReference references} to consider
     */
    void setReferencesToConsider(Set<EReference> referencesToConsider);

    /**
     * Get {@link EReference references} to ignore.
     * 
     * @param referencesToIgnore
     *            {@link EReference references} to ignore
     */
    void setReferencesToIgnore(Set<EReference> referencesToIgnore);
}
