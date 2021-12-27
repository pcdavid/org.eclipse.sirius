/*******************************************************************************
 * Copyright (c) 2010, 2021 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.business.api.query;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.description.DAnnotationEntry;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

/**
 * A class aggregating all the queries (read-only!) having a {@link DAnalysis} as a starting point.
 * 
 * @author nlepine
 * 
 */
@SuppressWarnings("restriction")
public class DAnalysisQuery {

    private final org.eclipse.sirius.model.business.internal.query.DAnalysisQuery internalQuery;

    /**
     * Create a new query.
     * 
     * @param analysis
     *            the element to query.
     */
    public DAnalysisQuery(DAnalysis analysis) {
        this.internalQuery = new org.eclipse.sirius.model.business.internal.query.DAnalysisQuery(Objects.requireNonNull(analysis));
    }

    /**
     * Get the annotation of a representation with id source and data eObject.
     * 
     * @param source
     *            the source of the annotation
     * @param detail
     *            the data of the annotation
     * @return the annotation entry
     */
    public Optional<DAnnotationEntry> getAnnotation(final String source, final String detail) {
        return internalQuery.getAnnotation(source, detail);
    }

    /**
     * Get all the annotations of a representation with id source.
     * 
     * @param source
     *            the source of the annotation
     * @return the annotation entries
     */
    public Optional<DAnnotationEntry> getAnnotation(final String source) {
        return internalQuery.getAnnotation(source);
    }

    /**
     * Return all valid (i.e. not null) analysis referenced by the given analysis (and its sub referenced analysis).
     * 
     * @return all valid (i.e. not null) analysis referenced by the given analysis (and its sub referenced analysis).
     */
    public Collection<DAnalysis> getAllReferencedAnalyses() {
        return internalQuery.getAllReferencedAnalyses();

    }

    /**
     * Get the first model of this analysis if it contains at least one model. The first model is the model used to
     * create the representations file.
     * 
     * @return an optional EObject representing the root of the main semantic model.
     */
    public Optional<EObject> getMainModel() {
        EList<EObject> models = internalQuery.getDAnalysis().getModels();
        if (models.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(models.get(0));
        }
    }

    /**
     * Build a list of main models. <br/>
     * First, get the first model of this analysis if it contains at least one model. The first model is the model used
     * to create the representations file. <br/>
     * Then add each non controlled models that is not a parent of the main model (which can be a model fragment if the
     * current DAnalysis is a referenced analysis and has been created during a previous control).
     * 
     * @return an {@link Set} of EObject representing the root of the main semantic models.
     */
    public Set<EObject> getMainModels() {
        Optional<EObject> optionalMainModel = getMainModel();
        // We need a list with the "main model" and other root models to allow
        // control on project with many models
        Set<EObject> releventModels = new LinkedHashSet<>();
        if (optionalMainModel.isPresent()) {
            releventModels.add(optionalMainModel.get());
            for (EObject model : internalQuery.getDAnalysis().getModels()) {
                if (!AdapterFactoryEditingDomain.isControlled(model) && !(new EObjectQuery(optionalMainModel.get()).isContainedIn(model))) {
                    releventModels.add(model);
                }
            }
        }
        return releventModels;
    }

    /**
     * Get selected {@link Viewpoint}s for the current {@link DAnalysis}.
     * 
     * @return selected {@link Viewpoint}s for the current {@link DAnalysis}
     */
    public Collection<Viewpoint> getSelectedViewpoints() {
        return internalQuery.getSelectedViewpoints();
    }

    /**
     * Find the first orphan {@link DView} (i.e. for which {@link DView#getSirius()} == null), null if no orphan
     * {@link DView} existing among the {@link DAnalysis#getOwnedViews()}.
     * 
     * @return the first orphan {@link DView}
     */
    public DView getFirstOrphanDView() {
        return internalQuery.getFirstOrphanDView();
    }
}
