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
package org.eclipse.sirius.viewpoint.description.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.PasteTargetDescription;
import org.eclipse.sirius.viewpoint.description.tool.PasteDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Paste Target Description</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.impl.PasteTargetDescriptionImpl#getPasteDescriptions
 * <em>Paste Descriptions</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class PasteTargetDescriptionImpl extends EObjectImpl implements PasteTargetDescription {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * The cached value of the '{@link #getPasteDescriptions()
     * <em>Paste Descriptions</em>}' reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getPasteDescriptions()
     * @generated
     * @ordered
     */
    protected EList<PasteDescription> pasteDescriptions;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected PasteTargetDescriptionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return DescriptionPackage.Literals.PASTE_TARGET_DESCRIPTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EList<PasteDescription> getPasteDescriptions() {
        if (pasteDescriptions == null) {
            pasteDescriptions = new EObjectResolvingEList<PasteDescription>(PasteDescription.class, this, DescriptionPackage.PASTE_TARGET_DESCRIPTION__PASTE_DESCRIPTIONS);
        }
        return pasteDescriptions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case DescriptionPackage.PASTE_TARGET_DESCRIPTION__PASTE_DESCRIPTIONS:
            return getPasteDescriptions();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case DescriptionPackage.PASTE_TARGET_DESCRIPTION__PASTE_DESCRIPTIONS:
            getPasteDescriptions().clear();
            getPasteDescriptions().addAll((Collection<? extends PasteDescription>) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case DescriptionPackage.PASTE_TARGET_DESCRIPTION__PASTE_DESCRIPTIONS:
            getPasteDescriptions().clear();
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case DescriptionPackage.PASTE_TARGET_DESCRIPTION__PASTE_DESCRIPTIONS:
            return pasteDescriptions != null && !pasteDescriptions.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // PasteTargetDescriptionImpl
