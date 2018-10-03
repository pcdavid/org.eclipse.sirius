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

import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterWithDiagnostic;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterWithDiagnostic.IEvaluationResult;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.workflow.WorkflowDescription;

/**
 * Common utility for evaluation of interpreted expressions in the context of
 * workflows.
 *
 * @author pcdavid
 */
public final class WorkflowExpressions {

    /**
     * The standard variables used in when evaluating expressions in the context
     * of a workflow.
     *
     * @author pcdavid
     */
    @SuppressWarnings({ "checkstyle::javadocmethod", "checkstyle::javadocfield" })
    public enum Variable {

        /**
         * The default evaluation context of all the workflow expressions.
         */
        SELF("The top-level DAnalysis of the session", DAnalysis.class), //$NON-NLS-1$
        /**
         * The last computed value of the workflow's state expression.
         */
        STATE("The current state of the workflow", Object.class); //$NON-NLS-1$

        private final String documentation;

        private final Class<?> expectedType;

        Variable(String documentation, Class<?> expectedType) {
            this.documentation = documentation;
            this.expectedType = expectedType;
        }

        public String getName() {
            return this.toString().toLowerCase();
        }

        /**
         * Return the documentation.
         *
         * @return the documentation
         */
        public String getDocumentation() {
            return this.documentation;
        }

        /**
         * .
         *
         * @param context
         *            .
         * @param value
         *            .
         */
        public void define(Map<String, Object> context, Object value) {
            if (expectedType.isInstance(value)) {
                context.put(this.getName(), value);
            } else {
                throw new IllegalArgumentException("Type error"); //$NON-NLS-1$
            }
        }
    }

    private WorkflowExpressions() {
        // Prevents instanciation
    }

    /**
     * Computes the current value of the state expression of a workflow.
     *
     * @param session
     *            the session.
     * @param workflow
     *            the workflow whose state must be computed.
     * @return the result of the evaluation, if it could be computed without
     *         error.
     */
    public static Optional<Object> evaluateStateExpression(Session session, WorkflowDescription workflow) {
        EObject self = Workflow.of(session).getDefaultContext();
        try {
            IEvaluationResult result = ((IInterpreterWithDiagnostic) session.getInterpreter()).evaluateExpression(self, workflow.getStateExpression());
            if (result.getDiagnostic().getSeverity() == Diagnostic.OK) {
                return Optional.of(result.getValue()); // FIXME Does not handle
                                                       // null results
            } else {
                // TODO Log evaluation error.
            }
        } catch (EvaluationException e) {
            // TODO Log evaluation error.
        }
        return null;
    }
}
