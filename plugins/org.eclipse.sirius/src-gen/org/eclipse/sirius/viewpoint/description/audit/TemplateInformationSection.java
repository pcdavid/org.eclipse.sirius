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
package org.eclipse.sirius.viewpoint.description.audit;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Template Information Section</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc --> This information section is based on an Acceleo
 * template. <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.audit.TemplateInformationSection#getTemplatePath
 * <em>Template Path</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.audit.AuditPackage#getTemplateInformationSection()
 * @model
 * @generated
 */
public interface TemplateInformationSection extends InformationSection {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Template Path</b></em>' attribute. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Template Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc --> <!-- begin-model-doc --> The file path of the
     * template. <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Template Path</em>' attribute.
     * @see #setTemplatePath(String)
     * @see org.eclipse.sirius.viewpoint.description.audit.AuditPackage#getTemplateInformationSection_TemplatePath()
     * @model
     * @generated
     */
    String getTemplatePath();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.viewpoint.description.audit.TemplateInformationSection#getTemplatePath
     * <em>Template Path</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Template Path</em>' attribute.
     * @see #getTemplatePath()
     * @generated
     */
    void setTemplatePath(String value);

} // TemplateInformationSection
