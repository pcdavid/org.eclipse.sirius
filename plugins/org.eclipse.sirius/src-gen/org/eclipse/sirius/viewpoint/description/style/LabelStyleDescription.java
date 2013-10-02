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

import org.eclipse.sirius.viewpoint.LabelAlignment;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Label Style Description</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc --> The style of a label. <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.style.LabelStyleDescription#getLabelAlignment
 * <em>Label Alignment</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getLabelStyleDescription()
 * @model
 * @generated
 */
public interface LabelStyleDescription extends BasicLabelStyleDescription {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Label Alignment</b></em>' attribute. The
     * literals are from the enumeration
     * {@link org.eclipse.sirius.viewpoint.LabelAlignment}. <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Label Alignment</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * 
     * @since 2.0 <!-- end-user-doc -->
     * @return the value of the '<em>Label Alignment</em>' attribute.
     * @see org.eclipse.sirius.viewpoint.LabelAlignment
     * @see #setLabelAlignment(LabelAlignment)
     * @see org.eclipse.sirius.viewpoint.description.style.StylePackage#getLabelStyleDescription_LabelAlignment()
     * @model
     * @generated
     */
    LabelAlignment getLabelAlignment();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.style.LabelStyleDescription#getLabelAlignment
     * <em>Label Alignment</em>}' attribute. <!-- begin-user-doc -->
     * 
     * @since 2.0 <!-- end-user-doc -->
     * @param value
     *            the new value of the '<em>Label Alignment</em>' attribute.
     * @see org.eclipse.sirius.viewpoint.LabelAlignment
     * @see #getLabelAlignment()
     * @generated
     */
    void setLabelAlignment(LabelAlignment value);

} // LabelStyleDescription
