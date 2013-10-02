/*******************************************************************************
 * Copyright (c) 2007-2013 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.viewpoint.description.style;

import org.eclipse.sirius.viewpoint.description.ColorDescription;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Lozenge Node Description</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc --> The lozenge style to display a node as a lozenge.
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.LozengeNodeDescription#getWidthComputationExpression
 * <em>Width Computation Expression</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.LozengeNodeDescription#getHeightComputationExpression
 * <em>Height Computation Expression</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.LozengeNodeDescription#getColor
 * <em>Color</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getLozengeNodeDescription()
 * @model
 * @generated
 */
public interface LozengeNodeDescription extends NodeStyleDescription {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Width Computation Expression</b></em>'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
     * begin-model-doc --> The acceleo expression that computes the width of the
     * lozenge. <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Width Computation Expression</em>'
     *         attribute.
     * @see #setWidthComputationExpression(String)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getLozengeNodeDescription_WidthComputationExpression()
     * @model dataType="org.eclipse.sirius.description.AcceleoExpression"
     * @generated
     */
    String getWidthComputationExpression();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.LozengeNodeDescription#getWidthComputationExpression
     * <em>Width Computation Expression</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Width Computation Expression</em>'
     *            attribute.
     * @see #getWidthComputationExpression()
     * @generated
     */
    void setWidthComputationExpression(String value);

    /**
     * Returns the value of the '<em><b>Height Computation Expression</b></em>'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
     * begin-model-doc --> The acceleo expression that computes the height of
     * the lozenge. <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Height Computation Expression</em>'
     *         attribute.
     * @see #setHeightComputationExpression(String)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getLozengeNodeDescription_HeightComputationExpression()
     * @model dataType="org.eclipse.sirius.description.AcceleoExpression"
     * @generated
     */
    String getHeightComputationExpression();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.LozengeNodeDescription#getHeightComputationExpression
     * <em>Height Computation Expression</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Height Computation Expression</em>'
     *            attribute.
     * @see #getHeightComputationExpression()
     * @generated
     */
    void setHeightComputationExpression(String value);

    /**
     * Returns the value of the '<em><b>Color</b></em>' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
     * The color of the lozenge. <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Color</em>' containment reference.
     * @see #setColor(ColorMapping)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getLozengeNodeDescription_Color()
     * @model required="true"
     * @generated
     */
    ColorDescription getColor();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.LozengeNodeDescription#getColor
     * <em>Color</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Color</em>' reference.
     * @see #getColor()
     * @generated
     */
    void setColor(ColorDescription value);

} // LozengeNodeDescription
