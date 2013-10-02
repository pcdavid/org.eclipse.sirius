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

import org.eclipse.sirius.viewpoint.BackgroundStyle;
import org.eclipse.sirius.viewpoint.description.ColorDescription;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Flat Container Style Description</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getBackgroundStyle
 * <em>Background Style</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getBackgroundColor
 * <em>Background Color</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getForegroundColor
 * <em>Foreground Color</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getLabelBorderStyle
 * <em>Label Border Style</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getFlatContainerStyleDescription()
 * @model
 * @generated
 */
public interface FlatContainerStyleDescription extends ContainerStyleDescription, SizeComputationContainerStyleDescription {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Background Color</b></em>' attribute.
     * The default value is <code>"gray"</code>. The literals are from the
     * enumeration {@link org.eclipse.sirius.DefaultColors}. <!-- begin-user-doc
     * --> <!-- end-user-doc --> <!-- begin-model-doc --> The background color.
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Background Color</em>' attribute.
     * @see org.eclipse.sirius.DefaultColors
     * @see #setBackgroundColor(DefaultColors)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getFlatContainerStyleDescription_BackgroundColor()
     * @model default="gray"
     * @generated
     */
    ColorDescription getBackgroundColor();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getBackgroundColor
     * <em>Background Color</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Background Color</em>' reference.
     * @see #getBackgroundColor()
     * @generated
     */
    void setBackgroundColor(ColorDescription value);

    /**
     * Returns the value of the '<em><b>Background Style</b></em>' attribute.
     * The literals are from the enumeration
     * {@link org.eclipse.sirius.viewpoint.BackgroundStyle}. <!-- begin-user-doc
     * --> <!-- end-user-doc --> <!-- begin-model-doc --> The background style.
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Background Style</em>' attribute.
     * @see org.eclipse.sirius.viewpoint.BackgroundStyle
     * @see #setBackgroundStyle(BackgroundStyle)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getFlatContainerStyleDescription_BackgroundStyle()
     * @model required="true"
     * @generated
     */
    BackgroundStyle getBackgroundStyle();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getBackgroundStyle
     * <em>Background Style</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Background Style</em>' attribute.
     * @see org.eclipse.sirius.viewpoint.BackgroundStyle
     * @see #getBackgroundStyle()
     * @generated
     */
    void setBackgroundStyle(BackgroundStyle value);

    /**
     * Returns the value of the '<em><b>Foreground Color</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Foreground Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> The color to use. <!--
     * end-model-doc -->
     * 
     * @return the value of the '<em>Foreground Color</em>' reference.
     * @see #setForegroundColor(ColorDescription)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getFlatContainerStyleDescription_ForegroundColor()
     * @model required="true"
     * @generated
     */
    ColorDescription getForegroundColor();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getForegroundColor
     * <em>Foreground Color</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Foreground Color</em>' reference.
     * @see #getForegroundColor()
     * @generated
     */
    void setForegroundColor(ColorDescription value);

    /**
     * Returns the value of the '<em><b>Label Border Style</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label Border Style</em>' reference isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Label Border Style</em>' reference.
     * @see #setLabelBorderStyle(LabelBorderStyleDescription)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getFlatContainerStyleDescription_LabelBorderStyle()
     * @model
     * @generated
     */
    LabelBorderStyleDescription getLabelBorderStyle();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.FlatContainerStyleDescription#getLabelBorderStyle
     * <em>Label Border Style</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Label Border Style</em>' reference.
     * @see #getLabelBorderStyle()
     * @generated
     */
    void setLabelBorderStyle(LabelBorderStyleDescription value);

} // FlatContainerStyleDescription
