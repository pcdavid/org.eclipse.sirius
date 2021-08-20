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
package org.eclipse.sirius.business.api.session;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.sirius.ext.base.Option;

/**
 * Interface allowing you to change something within a transaction before it is
 * applied by returning a command.
 * 
 * @author cbrun
 * 
 */
public interface ModelChangeTrigger {

    /**
     * Will be called when a Notification event gets sent from the
     * SiriusEventBroker during the firing of pre-commit events.
     * 
     * Might return an empty {@link Option} if there is nothing to do.
     * 
     * @param notifications
     *            the list of notifications of interest.
     * @return the command to do wrapped in a option.
     */
    Option<Command> localChangesAboutToCommit(Collection<Notification> notifications);

    /**
     * return an int representing the triggering priority, less meaning sooner,
     * 0 = first, 1 = after first trigger and so on.
     * 
     * @return an int representing the triggering priority, less meaning sooner,
     *         0 = first, 1 = after first trigger and so on.
     */
    int priority();
}
