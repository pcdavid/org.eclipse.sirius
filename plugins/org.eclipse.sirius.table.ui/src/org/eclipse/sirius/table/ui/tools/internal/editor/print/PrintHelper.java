/*******************************************************************************
 * Copyright (c) 2010 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.table.ui.tools.internal.editor.print;

/**
 * Common interface for printer helpers.
 * 
 * @author mchauvin
 */
public interface PrintHelper {

    /**
     * Launch a print job. launching is currently done synchronously.
     * 
     * @param name
     *            the job name
     */
    void launchPrintJob(String name);

    /**
     * Dispose the system resources created.
     */
    void dispose();

}
