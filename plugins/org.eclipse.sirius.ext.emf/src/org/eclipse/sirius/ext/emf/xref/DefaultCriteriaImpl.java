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
import java.util.Set;

import org.eclipse.emf.ecore.EReference;

/**
 * A default implementation of {@link DefaultCriteria}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class DefaultCriteriaImpl implements DefaultCriteria {

    private Set<EReference> referencesToConsider = Collections.emptySet();

    private Set<EReference> referencesToIgnore = Collections.emptySet();

    @Override
    public boolean matches(XRefResult xRefResult) {
        boolean matches = false;
        EReference eReference = xRefResult.getEReference();
        matches = referencesToConsider.contains(eReference) && !referencesToIgnore.contains(eReference);
        return matches;
    }

    @Override
    public Set<EReference> getReferencesToConsider() {
        return referencesToConsider;
    }

    @Override
    public void setReferencesToConsider(Set<EReference> referencesToConsider) {
        this.referencesToConsider = referencesToConsider;
    }

    @Override
    public Set<EReference> getReferencesToIgnore() {
        return referencesToIgnore;
    }

    @Override
    public void setReferencesToIgnore(Set<EReference> referencesToIgnore) {
        this.referencesToIgnore = referencesToIgnore;
    }

}
