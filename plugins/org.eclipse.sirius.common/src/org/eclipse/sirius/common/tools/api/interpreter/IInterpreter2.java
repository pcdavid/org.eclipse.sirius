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
package org.eclipse.sirius.common.tools.api.interpreter;

import org.eclipse.sirius.ext.emf.InverseReferenceFinder;

/**
 * Extends the base {@link IInterpreter} interface with the capbility to use an
 * arbitrary implementation of the generic {@link InverseReferenceFinder}
 * service instead of the hard-coded {@code ECrossReferenceAdapter} one.
 * 
 * @author pcdavid
 */
public interface IInterpreter2 extends IInterpreter {
    /**
     * Set the InverseReferenceFinder to use.
     * 
     * @param irf
     *            the InverseReferenceFinder to use.
     */
    void setInverseReferenceFinder(InverseReferenceFinder irf);
}
