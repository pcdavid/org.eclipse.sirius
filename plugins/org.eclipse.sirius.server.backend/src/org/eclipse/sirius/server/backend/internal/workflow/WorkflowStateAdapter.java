/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.server.backend.internal.workflow;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.workflow.WorkflowDescription;

/**
 * Decorates a {@link WorkflowDescription} loaded in a particular session with
 * the current value of it's
 * {@link WorkflowDescription#getStateComputationExpression()
 * stateComputationExpression}.
 *
 * @author pcdavid
 */
public class WorkflowStateAdapter extends AdapterImpl {
    /**
     * Indicates that the value must be re-computed the next time it is needed.
     */
    private boolean needsUpdate = true;

    /**
     * The last computed, non-invalidated value of the state expression of the
     * target workflow.
     */
    private Object stateValue;

    /**
     * Finds (or creates) the {@link WorkflowStateAdapter} for a given
     * {@link WorkflowDescription} instance.
     *
     * @param workflow
     *            the workflow to work with.
     * @return the {@link WorkflowStateAdapter} for the specified workflow
     *         instance.
     */
    public static WorkflowStateAdapter on(WorkflowDescription workflow) {
        Adapter existing = EcoreUtil.getExistingAdapter(workflow, WorkflowStateAdapter.class);
        if (existing instanceof WorkflowStateAdapter) {
            return (WorkflowStateAdapter) existing;
        } else if (existing == null) {
            WorkflowStateAdapter wsa = new WorkflowStateAdapter();
            workflow.eAdapters().add(wsa);
            return wsa;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean isAdapterForType(Object type) {
        return type == WorkflowStateAdapter.class;
    }

    /**
     * Indicates that the last computed state value is obsolete and will need to
     * be recomputed next time it is needed.
     */
    public void invalidate() {
        synchronized (this) {
            this.needsUpdate = true;
            /*
             * The value should not be used anymore, make it reclaimable by the
             * GC immediatly.
             */
            this.stateValue = null;
        }
    }

    /**
     * Returns the up to date value of the workflow's state, re-computing it if
     * needed.
     *
     * @return the current state value of the workflow.
     */
    public Object getStateValue() {
        synchronized (this) {
            if (needsUpdate) {
                stateValue = computeValue();
                needsUpdate = false;
            }
            return stateValue;
        }
    }

    /**
     * Removes the adapter from the {@link WorkflowDescription} it is attached
     * to.
     */
    public void uninstall() {
        invalidate();
        target = this.getTarget();
        if (target != null) {
            target.eAdapters().remove(this);
        }
    }

    private Object computeValue() {
        if (target instanceof WorkflowDescription) {
            WorkflowDescription workflow = (WorkflowDescription) target;
            return Session.of(workflow).map(session -> WorkflowExpressions.evaluateStateExpression(session, workflow)).orElse(null);
        }
        return null;
    }
}
