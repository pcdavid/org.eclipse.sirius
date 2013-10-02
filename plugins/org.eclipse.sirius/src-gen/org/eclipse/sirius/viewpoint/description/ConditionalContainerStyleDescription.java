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
package org.eclipse.sirius.viewpoint.description;

import org.eclipse.sirius.viewpoint.description.style.ContainerStyleDescription;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Conditional Container Style</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.ConditionalContainerStyleDescription#getStyle
 * <em>Style</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.DescriptionPackage#getConditionalContainerStyleDescription()
 * @model
 * @generated
 */
public interface ConditionalContainerStyleDescription extends ConditionalStyleDescription {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Style</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Style</em>' containment reference.
     * @see #setStyle(ContainerStyleDescription)
     * @see org.eclipse.sirius.viewpoint.description.DescriptionPackage#getConditionalContainerStyleDescription_Style()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    ContainerStyleDescription getStyle();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.ConditionalContainerStyleDescription#getStyle
     * <em>Style</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Style</em>' containment reference.
     * @see #getStyle()
     * @generated
     */
    void setStyle(ContainerStyleDescription value);

} // ConditionalContainerStyleDescription
