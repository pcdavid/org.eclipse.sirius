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
 * A factory for Criteria.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public interface CriteriaFactory {

    /**
     * Create a default {@link Criteria}.
     * 
     * @return a default {@link Criteria}
     */
    Criteria createDefaultCriteria();

    /**
     * Create a {@link Criteria} from a specified <code>criteriaId</code> .
     * 
     * @param criteriaId
     *            criteria id
     * @return a {@link Criteria}
     */
    Criteria createCriteria(String criteriaId);

}
