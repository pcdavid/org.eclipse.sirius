/**
 *  Copyright (c) 2018 Obeo.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  Contributors:
 *     Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.workflow;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.viewpoint.description.DocumentedElement;
import org.eclipse.sirius.viewpoint.description.Extension;
import org.eclipse.sirius.viewpoint.description.IdentifiedElement;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Description</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.sirius.workflow.WorkflowDescription#getPreconditionExpression <em>Precondition
 * Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.workflow.WorkflowDescription#getStateExpression <em>State Expression</em>}</li>
 * <li>{@link org.eclipse.sirius.workflow.WorkflowDescription#getPages <em>Pages</em>}</li>
 * </ul>
 *
 * @see org.eclipse.sirius.workflow.WorkflowPackage#getWorkflowDescription()
 * @model
 * @generated
 */
public interface WorkflowDescription extends Extension, IdentifiedElement, DocumentedElement {
    /**
     * Returns the value of the '<em><b>Precondition Expression</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Precondition Expression</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Precondition Expression</em>' attribute.
     * @see #setPreconditionExpression(String)
     * @see org.eclipse.sirius.workflow.WorkflowPackage#getWorkflowDescription_PreconditionExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getPreconditionExpression();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.workflow.WorkflowDescription#getPreconditionExpression
     * <em>Precondition Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Precondition Expression</em>' attribute.
     * @see #getPreconditionExpression()
     * @generated
     */
    void setPreconditionExpression(String value);

    /**
     * Returns the value of the '<em><b>State Expression</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>State Expression</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>State Expression</em>' attribute.
     * @see #setStateExpression(String)
     * @see org.eclipse.sirius.workflow.WorkflowPackage#getWorkflowDescription_StateExpression()
     * @model dataType="org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getStateExpression();

    /**
     * Sets the value of the '{@link org.eclipse.sirius.workflow.WorkflowDescription#getStateExpression <em>State
     * Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>State Expression</em>' attribute.
     * @see #getStateExpression()
     * @generated
     */
    void setStateExpression(String value);

    /**
     * Returns the value of the '<em><b>Pages</b></em>' containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.workflow.PageDescription}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pages</em>' containment reference list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Pages</em>' containment reference list.
     * @see org.eclipse.sirius.workflow.WorkflowPackage#getWorkflowDescription_Pages()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<PageDescription> getPages();

} // WorkflowDescription
