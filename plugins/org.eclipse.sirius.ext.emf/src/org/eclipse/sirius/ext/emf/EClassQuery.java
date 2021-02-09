/*******************************************************************************
 * Copyright (c) 2011, 2021 THALES GLOBAL SERVICES and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ext.emf;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Queries on {@link EClass}.
 * 
 * @author pierre-charles.david@obeo.fr
 */
public class EClassQuery {
    /**
     * The predicate used to identify containment features.
     * 
     * @author pierre-charles.david@obeo.fr
     */
    private static final class IsContaintmentFeature implements Predicate<EStructuralFeature> {
        @Override
        public boolean test(EStructuralFeature input) {
            return new EStructuralFeatureQuery(input).isContainment();
        }
    }

    private final EClass eClass;

    /**
     * Constructor.
     * 
     * @param eClass
     *            the EClass to query.
     */
    public EClassQuery(EClass eClass) {
        this.eClass = eClass;
    }

    /**
     * Returns all the containment features of the queried {@link EClass}.
     * 
     * @return all the containment features of the {@link EClass}.
     */
    public Collection<EStructuralFeature> getAllContainmentFeatures() {
        return Collections.unmodifiableList(eClass.getEAllStructuralFeatures().stream().filter(new IsContaintmentFeature()).collect(Collectors.toList()));
    }

    /**
     * Returns all the non-containment features of the queried {@link EClass}.
     * 
     * @return all the non-containment features of the {@link EClass}.
     */
    public Collection<EStructuralFeature> getAllNonContainmentFeatures() {
        return Collections.unmodifiableList(eClass.getEAllStructuralFeatures().stream().filter(new IsContaintmentFeature().negate()).collect(Collectors.toList()));
    }

    /**
     * Search structural features in <code>eClazz</code> and also in its children. For example GaugeSectionDescription
     * is not a StyleDescription but we should searched in its references (also for EdgeStyleDescription with Begin,
     * Center and EndLabelStyleDescription).
     * 
     * @param featureName
     *            The name of the searched EStructuralFeature
     * @param knownEClasses
     *            Already treated EClass (to avoid infinite loop)
     * @return All structural features that have the <code>featureName</code> as name.
     */
    public Set<EStructuralFeature> getEStructuralFeatures(String featureName, List<EClass> knownEClasses) {
        knownEClasses.add(eClass);
        Set<EStructuralFeature> result = new LinkedHashSet<>();
        // Search reference in this EClass,
        EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(featureName);
        if (eStructuralFeature instanceof EReference) {
            result.add(eStructuralFeature);
        }
        // and in all contents
        Function<EReference, EClass> toEType = (EReference reference) -> {
            if (reference.getEType() instanceof EClass) {
                return (EClass) reference.getEType();
            } else {
                return null;
            }
        };
        eClass.getEAllContainments().stream().map(toEType).forEach((EClass subPartMetaModelEClass) -> {
            if (subPartMetaModelEClass != null && !knownEClasses.contains(subPartMetaModelEClass)) {
                result.addAll(new EClassQuery(subPartMetaModelEClass).getEStructuralFeatures(featureName, knownEClasses));
            }
        });
        return result;
    }
}
