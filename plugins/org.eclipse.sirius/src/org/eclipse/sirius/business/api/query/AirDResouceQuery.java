/*******************************************************************************
 * Copyright (c) 2010, 2015 THALES GLOBAL SERVICES.
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

import java.util.Optional;

import org.eclipse.sirius.business.api.session.resource.AirdResource;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.description.DAnnotationEntry;

/**
 * A class aggregating all the queries (read-only!) having a {@link AirDResouce}
 * as a starting point.
 * 
 * @author nlepine
 * 
 */
public class AirDResouceQuery {

    /**
     * the annotation source.
     */
    public static final String ANNOTATION_SOURCE = "Migration"; //$NON-NLS-1$

    private DAnalysis analysis;

    /**
     * Create a new query.
     * 
     * @param resource
     *            the element to query.
     */
    public AirDResouceQuery(AirdResource resource) {
        if (resource != null) {
            if (!resource.getContents().isEmpty() && resource.getContents().get(0) instanceof DAnalysis) {
                analysis = (DAnalysis) resource.getContents().get(0);
            }
        }
    }

    /**
     * return the DAnalysis of the resource.
     * 
     * @return the DAnalysis of the resource
     */
    public Option<DAnalysis> getDAnalysis() {
        if (analysis != null) {
            return Options.newSome(analysis);
        }
        return Options.newNone();
    }

    /**
     * Check if the analysis contains the migration annotation with
     * <code>tag</code>.
     * 
     * @param tag
     *            the tag to search.
     * @return true if the analysis contains the migration annotation with
     *         <code>tag</code>.
     */
    public boolean hasMigrationTag(String tag) {
        if (analysis == null) {
            return true;
        }
        final Optional<DAnnotationEntry> annotation = new DAnalysisQuery(analysis).getAnnotation(ANNOTATION_SOURCE, tag);
        return annotation.isPresent();
    }
}
