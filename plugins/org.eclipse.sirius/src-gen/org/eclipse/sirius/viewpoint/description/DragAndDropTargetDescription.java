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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.viewpoint.description.tool.ContainerDropDescription;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Drag And Drop Target Description</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc --> A DragAndDropTargetDescription is a Description or
 * Mapping that can have many DropTools <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.DragAndDropTargetDescription#getDropDescriptions
 * <em>Drop Descriptions</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.sirius.viewpoint.description.DescriptionPackage#getDragAndDropTargetDescription()
 * @model abstract="true"
 * @generated
 */
public interface DragAndDropTargetDescription extends EObject {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * Returns the value of the '<em><b>Drop Descriptions</b></em>' reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.viewpoint.description.tool.ContainerDropDescription}
     * . <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Drop Descriptions</em>' reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Drop Descriptions</em>' reference list.
     * @see org.eclipse.sirius.viewpoint.description.DescriptionPackage#getDragAndDropTargetDescription_DropDescriptions()
     * @model
     * @generated
     */
    EList<ContainerDropDescription> getDropDescriptions();

} // DragAndDropTargetDescription
