/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ext.emf.watchers;

/**
 * Represents the different moments at which a model change can be notified to a
 * watcher.
 * 
 * @author pcdavid
 */
public final class NotificationTime {
    /**
     * Indicates that the watcher should be notified as soon as the change is
     * performed (during the normal EMF eNotify() call).
     */
    public static final NotificationTime IMMEDIATE = new NotificationTime(0x01);

    /**
     * Indicates that the watcher should be notified only when a transaction is
     * about to be commited.
     */
    public static final NotificationTime PRE_COMMIT = new NotificationTime(0x02);

    /**
     * Indicates that the watcher should be notified only when a transaction has
     * already been successfuly commited.
     */
    public static final NotificationTime POST_COMMIT = new NotificationTime(0x04);

    private final int flag;

    private NotificationTime(int flags) {
        this.flag = flags;
    }

}
