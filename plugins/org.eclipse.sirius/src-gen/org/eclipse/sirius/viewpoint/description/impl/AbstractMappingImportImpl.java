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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.sirius.viewpoint.description.AbstractMappingImport;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Abstract Mapping Import</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.impl.AbstractMappingImportImpl#isHideSubMappings
 * <em>Hide Sub Mappings</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.viewpoint.description.impl.AbstractMappingImportImpl#isInheritsAncestorFilters
 * <em>Inherits Ancestor Filters</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class AbstractMappingImportImpl extends EObjectImpl implements AbstractMappingImport {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final String copyright = "Copyright (c) 2007, 2013 THALES GLOBAL SERVICES.\nAll rights reserved. This program and the accompanying materials\nare made available under the terms of the Eclipse Public License v1.0\nwhich accompanies this distribution, and is available at\nhttp://www.eclipse.org/legal/epl-v10.html\n\nContributors:\n   Obeo - initial API and implementation\n";

    /**
     * The default value of the '{@link #isHideSubMappings()
     * <em>Hide Sub Mappings</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isHideSubMappings()
     * @generated
     * @ordered
     */
    protected static final boolean HIDE_SUB_MAPPINGS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isHideSubMappings()
     * <em>Hide Sub Mappings</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isHideSubMappings()
     * @generated
     * @ordered
     */
    protected boolean hideSubMappings = HIDE_SUB_MAPPINGS_EDEFAULT;

    /**
     * The default value of the '{@link #isInheritsAncestorFilters()
     * <em>Inherits Ancestor Filters</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isInheritsAncestorFilters()
     * @generated
     * @ordered
     */
    protected static final boolean INHERITS_ANCESTOR_FILTERS_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isInheritsAncestorFilters()
     * <em>Inherits Ancestor Filters</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isInheritsAncestorFilters()
     * @generated
     * @ordered
     */
    protected boolean inheritsAncestorFilters = INHERITS_ANCESTOR_FILTERS_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected AbstractMappingImportImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return DescriptionPackage.Literals.ABSTRACT_MAPPING_IMPORT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isHideSubMappings() {
        return hideSubMappings;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setHideSubMappings(boolean newHideSubMappings) {
        boolean oldHideSubMappings = hideSubMappings;
        hideSubMappings = newHideSubMappings;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DescriptionPackage.ABSTRACT_MAPPING_IMPORT__HIDE_SUB_MAPPINGS, oldHideSubMappings, hideSubMappings));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isInheritsAncestorFilters() {
        return inheritsAncestorFilters;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setInheritsAncestorFilters(boolean newInheritsAncestorFilters) {
        boolean oldInheritsAncestorFilters = inheritsAncestorFilters;
        inheritsAncestorFilters = newInheritsAncestorFilters;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DescriptionPackage.ABSTRACT_MAPPING_IMPORT__INHERITS_ANCESTOR_FILTERS, oldInheritsAncestorFilters, inheritsAncestorFilters));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__HIDE_SUB_MAPPINGS:
            return isHideSubMappings();
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__INHERITS_ANCESTOR_FILTERS:
            return isInheritsAncestorFilters();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__HIDE_SUB_MAPPINGS:
            setHideSubMappings((Boolean) newValue);
            return;
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__INHERITS_ANCESTOR_FILTERS:
            setInheritsAncestorFilters((Boolean) newValue);
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
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__HIDE_SUB_MAPPINGS:
            setHideSubMappings(HIDE_SUB_MAPPINGS_EDEFAULT);
            return;
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__INHERITS_ANCESTOR_FILTERS:
            setInheritsAncestorFilters(INHERITS_ANCESTOR_FILTERS_EDEFAULT);
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
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__HIDE_SUB_MAPPINGS:
            return hideSubMappings != HIDE_SUB_MAPPINGS_EDEFAULT;
        case DescriptionPackage.ABSTRACT_MAPPING_IMPORT__INHERITS_ANCESTOR_FILTERS:
            return inheritsAncestorFilters != INHERITS_ANCESTOR_FILTERS_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (hideSubMappings: ");
        result.append(hideSubMappings);
        result.append(", inheritsAncestorFilters: ");
        result.append(inheritsAncestorFilters);
        result.append(')');
        return result.toString();
    }

} // AbstractMappingImportImpl
