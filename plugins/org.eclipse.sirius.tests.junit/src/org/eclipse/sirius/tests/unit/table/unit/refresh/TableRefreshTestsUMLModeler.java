/*******************************************************************************
 * Copyright (c) 2010, 2014 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.tests.unit.table.unit.refresh;

import org.eclipse.sirius.tests.SiriusTestsPlugin;

/**
 * UML Table Refresh tests modeler constants.
 * 
 * @author mchauvin
 */
public interface TableRefreshTestsUMLModeler {
    
    String SEMANTIC_MODEL_PATH = "/" + SiriusTestsPlugin.PLUGIN_ID + "/data/table/unit/refresh/tables.uml";

    String MODELER_PATH = "/" + SiriusTestsPlugin.PLUGIN_ID + "/data/table/unit/refresh/tables.odesign";

    String VIEWPOINT_NAME = "UML2 tables for tests";

    String CROSS_TABLE_COLORS = "Cross Table Colors";

    String MODEL_GENERALIZATION_CROSS_TABLE = "Model Generalization Cross Table";

    String COLORED_CLASSES_TABLE = "Colored Classes Table";

}
