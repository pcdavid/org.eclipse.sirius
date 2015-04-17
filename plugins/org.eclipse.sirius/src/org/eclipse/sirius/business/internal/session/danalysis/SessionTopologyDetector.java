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
package org.eclipse.sirius.business.internal.session.danalysis;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * From only the URI of the main representation file of a session and its
 * ResourceSet, discover all the resources which are part of session, their
 * types and relationships to each other. Implementations should assume no
 * resource is loaded at the beginning (not even the main aird), and should
 * strive to minimize the amount of resources they load in the ResourceSet in
 * order to do their work, although it is authorized (and indeed the default,
 * completely generic implementation relies on loading and resolving
 * everything).
 * 
 * @author pcdavid
 */
// CHECKSTYLE:OFF
public interface SessionTopologyDetector {
    /**
     * Every resource in the session must be in exactly one of the following
     * categories.
     * 
     * @author pcdavid
     */
    public enum ResourceKind {
        /**
         * Representation resources contain the session's state and the
         * representation's data. They are normally stored in
         * <code>*.aird</code> files.
         */
        REPRESENTATION,
        /**
         * The definitions for all the viewpoints referenced by the session, and
         * any anciliarry data (e.g. the environment) correspond to the
         * specification for the various representations in the session.
         */
        SPECIFICATION,
        /**
         * Metamodels are by definition resources whose URI can be found in the
         * {@code EPackage.Registry} of the session's {@code ResourceSet}.
         */
        METAMODEL,
        /**
         * Environment are special, Sirius-specific resources which hold
         * globally referencable constant values.
         */
        ENVIRONMENT,
        /**
         * All resources which are not in any of the other categories are by
         * definition semantic data.
         */
        SEMANTIC,
    }

    public enum ResourceFlag {
        /**
         * Applies only to ResourceKind.REPRESENTATION resources, and indicates
         * a resource is not the main session resource but is (directly or
         * indirectly) referenced by it.
         */
        REFERENCED,
        /**
         * Applies only to ResourceKind.SEMANTIC resources, and indicates the
         * resource contains at least one element which is referenced by/on a
         * representation in one of the session's representation resources.
         * <p>
         * Semantic resources may be present in the scope of the session even if
         * they are not represented, for example if they are explicitly bound to
         * it (see below) or referenced (directly or not)
         */
        REPRESENTED,
        /**
         * Applies only to ResourceKind.SEMANTIC resources, and indicates the
         * resource was explicilty requested by the user to be part of the
         * session, even though it may be neither
         */
        BOUND,
    }

    /**
     * Sets the session to analyze.
     * 
     * @param session
     *            the session to analyze.
     */
    void setSession(DAnalysisSessionImpl session);

    void analyze(IProgressMonitor pm);

}
