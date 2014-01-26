/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.internal.session.danalysis;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.api.query.URIQuery;
import org.eclipse.sirius.ext.base.relations.Relation;
import org.eclipse.sirius.ext.base.relations.TransitiveClosure;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Keeps track of which resources in a Sirius session are of what kind (see
 * {@link ResourceKind}).
 * 
 * @author Pierre-Charles David <pierre-charles.david@obeo.fr>
 */
public class ResourceCategorizer {
    private static final Function<EObject, Resource> ERESOURCE_FN = new Function<EObject, Resource>() {
        @Override
        public Resource apply(EObject input) {
            return input.eResource();
        }
    };

    private static final Relation<DAnalysis> REFERENCED_ANALYSIS_REL = new Relation<DAnalysis>() {
        @Override
        public Set<DAnalysis> apply(DAnalysis from) {
            return ImmutableSet.copyOf(from.getReferencedAnalysis());
        }
    };

    private final DAnalysis sessionAnalysis;

    /**
     * Creates a new categorizer for the specified {@link DAnalysis}.
     * 
     * @param sessionAnalysis
     *            the (current) main {@link DAnalysis} of the Sirius session to
     *            handle.
     */
    public ResourceCategorizer(DAnalysis sessionAnalysis) {
        this.sessionAnalysis = sessionAnalysis;
    }

    /**
     * Returns the subset of resources from the session which are of the
     * specified kind.
     * 
     * @param kind
     *            the kind of resources to look for.
     * @return all the resources of the specified kind.
     */
    public Set<Resource> getResources(ResourceKind kind) {
        final Set<Resource> result;
        switch (kind) {
        case SESSION:
            result = getSessionResources();
            break;
        case SPECIFICATION:
            result = getSpecificationResources();
            break;
        case SEMANTIC:
            result = getSemanticResources();
            break;
        default:
            throw new IllegalArgumentException();
        }
        return result;
    }

    /**
     * Foo.
     * 
     * @return bla.
     */
    public Resource getMainSessionResource() {
        return sessionAnalysis.eResource();
    }

    /**
     * Foo.
     * 
     * @return bla.
     */
    public Set<Resource> getReferencedSessionResources() {
        return Sets.difference(getSessionResources(), ImmutableSet.of(sessionAnalysis.eResource()));
    }

    private Set<Resource> getSessionResources() {
        return ImmutableSet.copyOf(Iterables.filter(Iterables.transform(getAllAnalyses(), ERESOURCE_FN), Predicates.notNull()));
    }

    private Set<Resource> getSpecificationResources() {
        return ImmutableSet.<Resource> builder().addAll(Iterables.filter(Iterables.transform(getAllViewpoints(), ERESOURCE_FN), Predicates.notNull())).addAll(getSpecialSpecificationResources()).build();
    }

    private Set<Resource> getSpecialSpecificationResources() {
        Set<Resource> result = ImmutableSet.of();
        Resource sessionResource = sessionAnalysis.eResource();
        if (sessionResource != null) {
            ResourceSet rs = Preconditions.checkNotNull(sessionResource.getResourceSet());
            Resource env = rs.getResource(URIQuery.VIEWPOINT_ENVIRONMENT_QUERY, false);
            if (env != null) {
                result = ImmutableSet.of(env);
            }
        }
        return result;
    }

    private Set<Resource> getSemanticResources() {
        return ImmutableSet.copyOf(Sets.difference(getAllResources(), Sets.union(getSessionResources(), getSpecificationResources())));
    }

    private Set<Resource> getAllResources() {
        Resource sessionResource = sessionAnalysis.eResource();
        if (sessionResource == null) {
            return ImmutableSet.of();
        } else {
            ResourceSet rs = Preconditions.checkNotNull(sessionResource.getResourceSet());
            return ImmutableSet.copyOf(rs.getResources());
        }
    }

    private Set<DAnalysis> getAllAnalyses() {
        Set<DAnalysis> referenced = TransitiveClosure.of(REFERENCED_ANALYSIS_REL).apply(sessionAnalysis);
        return ImmutableSet.<DAnalysis> builder().add(sessionAnalysis).addAll(referenced).build();
    }

    private Set<Viewpoint> getAllViewpoints() {
        Builder<Viewpoint> result = ImmutableSet.<Viewpoint> builder();
        for (DAnalysis analysis : getAllAnalyses()) {
            for (DView view : analysis.getOwnedViews()) {
                if (view.getViewpoint() != null) {
                    result.add(view.getViewpoint());
                }
            }
        }
        return result.build();
    }
}
