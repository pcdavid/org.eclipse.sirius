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
package org.eclipse.sirius.ext.emf.xref;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * The default {@link XRefFinder} based on {@link ECrossReferenceAdapter}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class DefaultXRefFinder extends AbstractXRefFinder implements XRefFinder {

    /**
     * Default constructor.
     * 
     * @param resourceSet
     *            the contextual {@link ResourceSet}
     */
    public DefaultXRefFinder(ResourceSet resourceSet) {
        super(resourceSet);
    }

    @Override
    public Set<XRefResult> getXRefs(Set<? extends Notifier> context, Set<? extends EObject> referencedEObjects, Criteria criteria) {
        Set<XRefResult> result = new HashSet<XRefResult>();
        // Add lazily a ECrossReferenceAdapter on each context if not already
        // installed
        for (Notifier contextualNotifier : context) {
            ECrossReferenceAdapter crossReferenceAdapter = ECrossReferenceAdapter.getCrossReferenceAdapter(contextualNotifier);
            if (crossReferenceAdapter == null) {
                crossReferenceAdapter = new ECrossReferenceAdapter();
                contextualNotifier.eAdapters().add(crossReferenceAdapter);
            }
            for (EObject referencedEObject : referencedEObjects) {
                Collection<Setting> inverseReferences = crossReferenceAdapter.getInverseReferences(referencedEObject);
                URI referencedEObjectURI = EcoreUtil.getURI(referencedEObject);
                for (Setting setting : inverseReferences) {
                    EObject referencingEObject = setting.getEObject();
                    URI referencingEObjectURI = EcoreUtil.getURI(referencingEObject);
                    XRefResult xRefResult = new XRefResultImpl(resourceSet, referencingEObjectURI, referencingEObject, referencedEObjectURI, referencedEObject,
                            (EReference) setting.getEStructuralFeature());
                    if (criteria == null || criteria.matches(xRefResult)) {
                        result.add(xRefResult);
                    }
                }
            }
        }

        return result;
    }
}
