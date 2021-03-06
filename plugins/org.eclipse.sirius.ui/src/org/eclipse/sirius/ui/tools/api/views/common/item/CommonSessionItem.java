/*******************************************************************************
 * Copyright (c) 2009, 2010 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.ui.tools.api.views.common.item;

import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.ui.tools.api.view.common.item.CommonItem;
import org.eclipse.sirius.ext.base.Option;

/**
 * An interface to get the wrapped object.
 * 
 * @author mporhel
 */
public interface CommonSessionItem extends CommonItem {

    /**
     * Get the Session which manages these representations.
     * 
     * @return the Session which manages these representations
     */
    Option<Session> getSession();
}
