/*******************************************************************************
 * Copyright (c) 2008, 2014 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tools.api.command;

import java.util.Stack;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.viewpoint.DRepresentation;

/**
 * This class keeps the trace of all contexts.
 * 
 * @author cbrun
 */
public class CommandContext {

    /** The stack of all context. */
    private Stack<EObject> targetStack = new Stack<EObject>();

    /** The initial push. */
    private EObject nextPushEObject;

    /**
     * Create a new {@link CommandContext}.
     * 
     * @param target
     *            the first context.
     * @since 4.0.0
     */
    public CommandContext(final EObject target) {
        this.nextPushEObject = target;
    }

    /**
     * Create a new {@link CommandContext}.
     * 
     * @param target
     *            the first context.
     * @param representation
     *            the current representation
     * @since 0.9.0
     * @deprecated Use {@link CommandContext#CommandContext(EObject)} instead.
     */
    @Deprecated
    public CommandContext(final EObject target, final DRepresentation representation) {
        this.nextPushEObject = target;
    }

    /**
     * Return the current context.
     * 
     * @return the current context.
     */
    public EObject getCurrentTarget() {
        if (this.targetStack.isEmpty()) {
            return nextPushEObject;
        }
        return this.targetStack.peek();
    }

    /**
     * Return the next element to push.
     * 
     * @return the next element to push.
     */
    public EObject getNextPush() {
        return this.nextPushEObject != null ? this.nextPushEObject : this.getCurrentTarget();
    }

    /**
     * Define the next push.
     * 
     * @param nextPushEObject
     *            the next push.
     */
    public void setNextPushEObject(final EObject nextPushEObject) {
        this.nextPushEObject = nextPushEObject;
    }

    /**
     * Pop and return the popped context. Return <code>null</code> if the is no
     * available context.
     * 
     * @return the popped context.
     */
    private EObject popTarget() {
        EObject result = null;

        if (!targetStack.isEmpty()) {
            result = targetStack.pop();
        }

        return result;
    }

    /**
     * Push the context of the specified command context.
     * 
     * @param context
     *            the command context.
     */
    public static void pushContext(final CommandContext context) {
        context.targetStack.push(context.getNextPush());
        context.nextPushEObject = null;
    }

    /**
     * Pop the context of the specified command context.
     * 
     * @param context
     *            the command context.
     */
    public static void popContext(final CommandContext context) {
        context.popTarget();
    }
}
