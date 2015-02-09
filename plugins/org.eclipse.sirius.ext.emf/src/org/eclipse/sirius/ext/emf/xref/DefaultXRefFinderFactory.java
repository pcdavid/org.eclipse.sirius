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

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.ext.emf.xref.XRefFinder.Factory;

/**
 * A Factory to create {@link DefaultXRefFinder}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class DefaultXRefFinderFactory implements Factory {

    @Override
    public XRefFinder createFinder(ResourceSet resourceSet) {
        return new DefaultXRefFinder(resourceSet);
    }

}
