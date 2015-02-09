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

import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * A inteface to get inverse cross referencing {@link EObject objects}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public interface XRefFinder {

    /**
     * Get inverse cross referencing objects as {@link XRefResult} set, i.e.
     * from the contextual {@link ResourceSet}, a specified set of referenced
     * objects
     * 
     * Note: all parameters of type {@link Notifier} and {@link EObject} must be
     * of the same {@link org.eclipse.emf.ecore.resource.ResourceSet}
     * 
     * @param referencedEObject
     *            the referenced object for which search referencing objects
     * @return the set of {@link Setting} corresponding to parameters
     */
    Set<Setting> getSettings(EObject referencedEObject);

    /**
     * Get inverse cross referencing objects as {@link XRefResult} set, i.e.
     * from the contextual {@link ResourceSet}, a specified set of referenced
     * objects
     * 
     * Note: all parameters of type {@link Notifier} and {@link EObject} must be
     * of the same {@link org.eclipse.emf.ecore.resource.ResourceSet}
     * 
     * @param referencedEObject
     *            the referenced object for which search referencing objects
     * @return the set of {@link XRefResult} corresponding to parameters
     */
    Set<XRefResult> getXRefs(EObject referencedEObject);

    /**
     * Get inverse cross referencing objects as {@link XRefResult} set, i.e.
     * from the contextual {@link ResourceSet}, a specified set of referenced
     * objects
     * 
     * Note: all parameters of type {@link Notifier} and {@link EObject} must be
     * of the same {@link org.eclipse.emf.ecore.resource.ResourceSet}
     * 
     * @param referencedEObjects
     *            the referenced objects for which search referencing objects
     * @return the set of {@link XRefResult} corresponding to parameters
     */
    Set<XRefResult> getXRefs(Set<? extends EObject> referencedEObjects);

    /**
     * Get inverse cross referencing objects as {@link XRefResult} set, i.e.
     * from the contextual {@link ResourceSet}, a specified set of referenced
     * objects
     * 
     * Note: all parameters of type {@link Notifier} and {@link EObject} must be
     * of the same {@link org.eclipse.emf.ecore.resource.ResourceSet}
     * 
     * @param referencedEObjects
     *            the referenced objects for which search referencing objects
     * @param criteria
     *            the {@link Criteria} to filter result, if null it means don't
     *            filter
     * @return the set of {@link XRefResult} corresponding to parameters
     */
    Set<XRefResult> getXRefs(Set<? extends EObject> referencedEObjects, Criteria criteria);

    /**
     * Get inverse cross referencing objects as {@link XRefResult} set, i.e.
     * from a specified set of contexts, a specified set of referenced objects
     * 
     * Note: all parameters of type {@link Notifier} and {@link EObject} must be
     * of the same {@link org.eclipse.emf.ecore.resource.ResourceSet}
     * 
     * @param context
     *            the context in which search inverse cross referencing objects
     * @param referencedEObjects
     *            the referenced objects for which search referencing objects
     * @param criteria
     *            the {@link Criteria} to filter result, if null it means don't
     *            filter
     * @return the set of {@link XRefResult} corresponding to parameters
     */
    Set<XRefResult> getXRefs(Set<? extends Notifier> context, Set<? extends EObject> referencedEObjects, Criteria criteria);

    /**
     * Factory for {@link XRefFinder}.
     */
    interface Factory {

        XRefFinder createFinder(ResourceSet resourceSet);

        /**
         * Descriptor for a {@link Factory}.
         */
        interface Descriptor {

            String getId();

            Factory getFactory();

            Class<? extends Resource.Factory> getResourceType();

            String getOverride();

        }

        /**
         * Registry of all {@link Factory}.
         */
        interface Registry {

            /**
             * Get a {@link XRefFinder.Factory} for a specified
             * {@link Resource.Factory resource type}. Will return always one.
             * 
             * @param resourceType
             *            the {@link Resource.Factory resource type}
             * @return a {@link XRefFinder.Factory}
             */
            Factory getFactory(Resource.Factory resourceType);

        }
    }
}
