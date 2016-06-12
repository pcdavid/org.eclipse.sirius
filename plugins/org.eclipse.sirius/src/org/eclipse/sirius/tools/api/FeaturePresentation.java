/*******************************************************************************
 * Copyright (c) 2016 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tools.api;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

/**
 * Utilities use to tweak the way we present the VSM features in the editor
 * without changing the metamodel itself.
 *
 * @author pcdavid
 */
public final class FeaturePresentation {
    private FeaturePresentation() {
        // Prevents instantiation.
    }

    /**
     * Sorts the list of features of a VSM element in a way that is more logical
     * for the end users, independently of the way the metamodel was defined.
     * The rearrangement does not add or remove any feature to the list, and
     * keeps the original order for elements which do not need to be
     * re-arranged.
     * <p>
     * The current implementation applies the following changes:
     * <ul>
     * <li>Variables inside tool definitions are placed before all the other
     * elements in the tool definition (in particular before the "Begin").</li>
     * </ul>
     *
     * @param features
     *            the list of features to sort.
     * @return the same features as in the input, but sorted in a more logical
     *         way to present to the end-user.
     */
    public static List<EStructuralFeature> sortedFeatures(List<EStructuralFeature> features) {
        List<EStructuralFeature> result = Lists.newArrayList(features);
        Map<EStructuralFeature, Integer> initialIndex = Maps.newHashMap();
        for (int i = 0; i < features.size(); i++) {
            initialIndex.put(features.get(i), i);
        }
        Collections.sort(result, Ordering.from(new Comparator<EStructuralFeature>() {
            public int compare(EStructuralFeature o1, EStructuralFeature o2) {
                // Let the tie be decided by the original positions (see the
                // Ordering.compound() call below).
                int result = 0;
                if (isVariableDefinitionFeature(o1) && !isVariableDefinitionFeature(o2)) {
                    // Place variables before non-variables
                    result = -1;
                } else if (!isVariableDefinitionFeature(o1) && isVariableDefinitionFeature(o2)) {
                    // Place non-variables after variables
                    result = 1;
                }
                return result;
            }

            private boolean isVariableDefinitionFeature(EStructuralFeature esf) {
                return ((EClass) esf.getEType()).getEAllSuperTypes().contains(DescriptionPackage.Literals.ABSTRACT_VARIABLE);
            }
        }).compound(Ordering.natural().onResultOf(Functions.forMap(initialIndex))));
        return result;
    }
}
