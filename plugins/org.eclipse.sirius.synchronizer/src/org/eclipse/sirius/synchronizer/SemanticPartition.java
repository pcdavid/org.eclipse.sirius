/*******************************************************************************
 * Copyright (c) 2011, 2021 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.synchronizer;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

/**
 * A {@link SemanticPartition} represents a set of elements in the input model.
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 */
public interface SemanticPartition {

    /**
     * Constant representing no SemanticPartition.
     */
    SemanticPartition NONE = new SemanticPartition() {

        public EvaluatedSemanticPartition evaluate(EObject context, CreatedOutput parentElement) {
            return new EvaluatedSemanticPartition() {

                public boolean isElementOf(EObject sem) {
                    return false;
                }

                public Iterator<EObject> elements() {
                    return Collections.emptyIterator();
                }
            };
        }
    };

    /**
     * Evaluate the semantic partition on the given context for the given parent
     * output.
     * 
     * @param context
     *            the EObject instance to evaluate on.
     * @param parentElement
     *            the output element which will become the parent of output
     *            elements created from the evaluation result.
     * @return the semantic partition evaluation result.
     */
    EvaluatedSemanticPartition evaluate(EObject context, CreatedOutput parentElement);

}
