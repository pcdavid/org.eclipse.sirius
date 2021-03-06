/*******************************************************************************
 * Copyright (c) 2007, 2008 THALES GLOBAL SERVICES.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.business.internal.dialect;

import org.eclipse.sirius.business.api.dialect.Dialect;
import org.eclipse.sirius.business.api.dialect.DialectServices;

/**
 * Dialect API fullfilement for diagram representations.
 * 
 * @author cbrun
 * 
 */
public class DiagramDialect implements Dialect {

    private DialectServices services;

    /**
     * 
     * {@inheritDoc}
     */
    public String getName() {
        return "diagram"; //$NON-NLS-1$
    }

    /**
     * 
     * {@inheritDoc}
     */
    public DialectServices getServices() {
        if (services == null) {
            services = new DiagramDialectServices();
        }
        return services;
    }

}
