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

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Additional Layer</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.AdditionalLayer#isActiveByDefault
 * <em>Active By Default</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.AdditionalLayer#isOptional
 * <em>Optional</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.DescriptionPackage#getAdditionalLayer()
 * @model
 * @generated
 */
public interface AdditionalLayer extends Layer {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Active By Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Active By Default</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Active By Default</em>' attribute.
     * @see #setActiveByDefault(boolean)
     * @see org.eclipse.sirius.viewpoint.description.DescriptionPackage#getAdditionalLayer_ActiveByDefault()
     * @model
     * @generated
     */
    boolean isActiveByDefault();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.AdditionalLayer#isActiveByDefault
     * <em>Active By Default</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Active By Default</em>' attribute.
     * @see #isActiveByDefault()
     * @generated
     */
    void setActiveByDefault(boolean value);

    /**
     * Returns the value of the '<em><b>Optional</b></em>' attribute. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Optional</em>' attribute isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Optional</em>' attribute.
     * @see #setOptional(boolean)
     * @see org.eclipse.sirius.viewpoint.description.DescriptionPackage#getAdditionalLayer_Optional()
     * @model
     * @generated
     */
    boolean isOptional();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.AdditionalLayer#isOptional
     * <em>Optional</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @param value
     *            the new value of the '<em>Optional</em>' attribute.
     * @see #isOptional()
     * @generated
     */
    void setOptional(boolean value);

} // AdditionalLayer
