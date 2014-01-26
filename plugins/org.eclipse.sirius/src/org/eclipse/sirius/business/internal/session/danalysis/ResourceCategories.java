package org.eclipse.sirius.business.internal.session.danalysis;

import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.viewpoint.DAnalysis;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

// CHECKSTYLE:OFF
public class ResourceCategories {
    private final ResourceCategorizer categorizer;

    private final Multimap<ResourceKind, Resource> categories = LinkedHashMultimap.create();

    public ResourceCategories(DAnalysis analysis) {
        this.categorizer = new ResourceCategorizer(analysis);
    }

    public void refresh() {
        for (ResourceKind kind : ResourceKind.values()) {
            categories.get(kind).clear();
            categories.putAll(kind, categorizer.getResources(kind));
        }
    }

    public Set<Resource> getResources(ResourceKind kind) {
        return ImmutableSet.copyOf(categories.get(kind));
    }
    
    public Set<Resource> getReferencedSessionResources() {
        return categorizer.getReferencedSessionResources();
    }
}
