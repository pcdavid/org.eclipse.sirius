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

/**
 * A criteria for a inverse cross references query.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public interface Criteria {

    /**
     * Tells if the specified {@link XRefResult} matches this
     * {@link Criteria}.
     * 
     * @param xRefResult
     *            the specified {@link XRefResult}
     * @return true if it matches, false otherwise
     */
    boolean matches(XRefResult xRefResult);

}
