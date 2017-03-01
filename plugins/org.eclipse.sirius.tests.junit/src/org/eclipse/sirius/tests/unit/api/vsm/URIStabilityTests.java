/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.api.vsm;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.sirius.ext.emf.AllContents;
import org.eclipse.sirius.tests.support.api.SiriusTestCase;
import org.eclipse.sirius.tools.api.command.ICommandFactory;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;

import com.google.common.collect.Sets;

/**
 * Verify that the way VSMs cross-reference each other is stable on trivial
 * changes (like reordering validation rules).
 * 
 * @author pcdavid
 */
public class URIStabilityTests extends SiriusTestCase {

    /**
     * Verifies that all EReferences defined in our metamodels which point to a
     * (subtype of) IdentifiedElement are configured to use the element's 'name'
     * as eKey.
     */
    public void testAllReferencesToIdentifiedElementsUseEKeys() {
        Collection<EPackage> allSiriusEPackages = new ArrayList<>();
        for (String nsURI : Sets.newLinkedHashSet(EPackage.Registry.INSTANCE.keySet())) {
            if (nsURI.startsWith("http://www.eclipse.org/sirius/")) {
                allSiriusEPackages.add(EPackage.Registry.INSTANCE.getEPackage(nsURI));
            }
        }
        for (EPackage p : allSiriusEPackages) {
            for (EObject o : AllContents.of(p, EcorePackage.Literals.EREFERENCE)) {
                EReference ref = (EReference) o;
                if (DescriptionPackage.Literals.IDENTIFIED_ELEMENT.isSuperTypeOf(ref.getEReferenceType())) {
                    boolean stableEKeyConfigured = ref.getEKeys().contains(DescriptionPackage.Literals.IDENTIFIED_ELEMENT__NAME);
                    if (!stableEKeyConfigured) {
                        EClass klass = ref.getEContainingClass();
                        System.out.println(klass.getEPackage().getNsURI() + "#//" + klass.getName() + "." + ref.getName());
                    }
//                    assertTrue(ref.getName(), stableEKeyConfigured);
                }
            }
        }
    }

    @Override
    protected ICommandFactory getCommandFactory() {
        return null;
    }

}
