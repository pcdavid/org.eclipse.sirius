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
package org.eclipse.sirius.viewpoint.description.tool;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Container Model Operation</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.tool.ContainerModelOperation#getSubModelOperations
 * <em>Sub Model Operations</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.tool.ToolPackage#getContainerModelOperation()
 * @model abstract="true"
 * @generated
 */
public interface ContainerModelOperation extends ModelOperation {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Sub Model Operations</b></em>'
     * containment reference list. The list contents are of type
     * {@link org.eclipse.sirius.viewpoint.description.tool.ModelOperation}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sub Model Operations</em>' containment
     * reference list isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Sub Model Operations</em>' containment
     *         reference list.
     * @see org.eclipse.sirius.viewpoint.description.tool.ToolPackage#getContainerModelOperation_SubModelOperations()
     * @model containment="true" resolveProxies="true"
     * @generated
     */
    EList<ModelOperation> getSubModelOperations();

} // ContainerModelOperation
