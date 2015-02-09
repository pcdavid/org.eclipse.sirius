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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Default implementation of {@link XRefFinder}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class XRefFinderImpl extends AbstractXRefFinder implements XRefFinder {

    private XRefFinder.Factory.Registry registry;

    // When there is no more resource of a type, should you dispose the
    // associated XRefByResourceTypeFinder to avoid memory leak?
    private Map<Resource.Factory, XRefFinder> finders = new HashMap<Resource.Factory, XRefFinder>();

    /**
     * Default constructor.
     *
     * @param registry
     *            the {@link XRefFinder.Factory.Registry} to get
     *            {@link XRefFinder.Factory}
     * @param resourceSet
     *            the {@link ResourceSet} on which query inverse cross
     *            references
     */
    public XRefFinderImpl(XRefFinder.Factory.Registry registry, ResourceSet resourceSet) {
        super(resourceSet);
        this.registry = registry;
    }

    // FIXME : have a blacklist of context to ignore?
    @Override
    public Set<XRefResult> getXRefs(Set<? extends Notifier> context, Set<? extends EObject> referencedEObjects, Criteria criteria) {
        Set<XRefResult> result = new HashSet<XRefResult>();
        Multimap<Resource, Notifier> resourcesContextToQuery = HashMultimap.create();
        Multimap<Resource.Factory, Resource> resourcesByType = HashMultimap.create();
        // Get resources to query
        for (Notifier contextualElement : context) {
            // As a CDOResource is also a EObject but we
            // want manage it as a resource, we manage in this order
            if (contextualElement instanceof Resource) {
                Resource contextualResource = (Resource) contextualElement;
                resourcesContextToQuery.put(contextualResource, contextualResource);
                resourcesByType.put(Resource.Factory.Registry.INSTANCE.getFactory(contextualResource.getURI()), contextualResource);
            } else if (contextualElement instanceof EObject) {
                EObject contextualEObject = (EObject) contextualElement;
                Resource resource = contextualEObject.eResource();
                resourcesContextToQuery.put(resource, contextualEObject);
                resourcesByType.put(Resource.Factory.Registry.INSTANCE.getFactory(resource.getURI()), resource);
            } else if (contextualElement instanceof ResourceSet) {
                for (Resource resource : ((ResourceSet) contextualElement).getResources()) {
                    resourcesContextToQuery.put(resource, resource);
                    resourcesByType.put(Resource.Factory.Registry.INSTANCE.getFactory(resource.getURI()), resource);
                }
            }
        }
        assert resourceSet.getResources().containsAll(resourcesContextToQuery.keySet());
        for (Resource.Factory resourceType : resourcesByType.keySet()) {
            XRefFinder finder = finders.get(resourceType);
            if (finder == null) {
                finder = registry.getFactory(resourceType).createFinder(resourceSet);
                finders.put(resourceType, finder);
            }
            Collection<Resource> resourcesOfType = resourcesByType.get(resourceType);

            Set<Notifier> subContext = new HashSet<Notifier>();
            for (Resource resourceOfType : resourcesOfType) {
                subContext.addAll(resourcesContextToQuery.get(resourceOfType));
            }
            // resourcesContextToQuery.
            result.addAll(finder.getXRefs(subContext, referencedEObjects, criteria));
        }
        return result;
    }
}
