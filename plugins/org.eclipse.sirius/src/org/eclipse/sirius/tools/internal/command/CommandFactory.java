/*******************************************************************************
 * Copyright (c) 2010 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tools.internal.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * Helper class to convert an {@link AbstractModelChangeOperation}s into either
 * a plain EMF Transaction {@link RecordingCommand}.
 * 
 * @author pcdavid
 */
public final class CommandFactory {
    private CommandFactory() {
        // Prevent instantiation.
    }

    /**
     * Converts these operations into an EMF Transaction recording command.
     * 
     * @param ted
     *            the editing domain in which to command will execute.
     * @param operations
     *            the array of operation to convert.
     * 
     * @param <T>
     *            the return type of the operation.
     * @return a recording command which will execute this operation.
     */
    public static <T> RecordingCommand createRecordingCommand(TransactionalEditingDomain ted, final Collection<AbstractModelChangeOperation<T>> operations) {
        String name = operations.isEmpty() ? "Do nothing" : operations.iterator().next().getName();
        return new RecordingCommandsExecutor<T>(ted, name, operations);
    }

    /**
     * Converts this operation into an EMF Transaction recording command.
     * 
     * @param ted
     *            the editing domain in which to command will execute.
     * @param operation
     *            the operation to convert.
     * 
     * @param <T>
     *            the return type of the operation.
     * @return a recording command which will execute this operation.
     */
    public static <T> RecordingCommand createRecordingCommand(TransactionalEditingDomain ted, final AbstractModelChangeOperation<T> operation) {
        return CommandFactory.createRecordingCommand(ted, Collections.singleton(operation));
    }

    /**
     * Converts these operations into an EMF Transaction recording command.
     * 
     * @param ted
     *            the editing domain in which to command will execute.
     * @param operations
     *            the array of operation to convert.
     * 
     * @param <T>
     *            the return type of the operation.
     * @return a recording command which will execute this operation.
     */
    public static <T> RecordingCommand createRecordingCommand(TransactionalEditingDomain ted, final AbstractModelChangeOperation<T>... operations) {
        return CommandFactory.createRecordingCommand(ted, Arrays.asList(operations));
    }

}
