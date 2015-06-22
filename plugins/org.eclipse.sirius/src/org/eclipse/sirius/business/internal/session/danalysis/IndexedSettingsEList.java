/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

import java.util.BitSet;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;

import com.google.common.collect.Maps;

// CHECKSTYLE:OFF
public class IndexedSettingsEList extends BasicEList<EStructuralFeature.Setting> {

    private static final long serialVersionUID = 1L;

    private Map<EObject, BitSet> knowns;

    @Override
    protected Object[] newData(int capacity) {
        return new EStructuralFeature.Setting[capacity];
    }

    @Override
    public boolean add(EStructuralFeature.Setting setting) {
        EObject eObject = setting.getEObject();
        if (knowns == null) {
            knowns = Maps.newHashMap();
        }
        BitSet knownFeaturesForThisEObject = getOrCreateBitset(eObject);
        boolean isKnownFeature = knownFeaturesForThisEObject.get(setting.getEStructuralFeature().getFeatureID());
        boolean toBeAdded = true;
        if (isKnownFeature) {
            EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
            EStructuralFeature.Setting[] settingData = (EStructuralFeature.Setting[]) data;
            for (int i = 0; i < size; ++i) {
                EStructuralFeature.Setting containedSetting = settingData[i];
                if (containedSetting.getEObject() == eObject && containedSetting.getEStructuralFeature() == eStructuralFeature) {
                    toBeAdded = false;
                    break;
                }
            }
        }
        if (toBeAdded) {
            addUnique(setting);
        }
        return toBeAdded;
    }

    private BitSet getOrCreateBitset(EObject eObject) {
        BitSet knownFeaturesForThisEObject = knowns.get(eObject);
        if (knownFeaturesForThisEObject == null) {
            knownFeaturesForThisEObject = new BitSet();
            knowns.put(eObject, knownFeaturesForThisEObject);
        }
        return knownFeaturesForThisEObject;
    }

    @Override
    protected Setting assign(int index, Setting object) {
        if (object != null && knowns != null && object.getEObject() != null && object.getEStructuralFeature() != null) {
            BitSet isKnown = getOrCreateBitset(object.getEObject());
            isKnown.set(object.getEStructuralFeature().getFeatureID());
        }
        return super.assign(index, object);
    }

    @Override
    public void clear() {
        super.clear();
        if (knowns != null) {
            knowns.clear();
        }
    }

}
