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
package org.eclipse.sirius.business.internal.session.danalysis.adapters;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl;
import org.eclipse.sirius.viewpoint.DAnalysis;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

// CHECKSTYLE:OFF
public class SiriusRepresentationResourceAdapter extends SiriusResourceAdapter {

    public SiriusRepresentationResourceAdapter(DAnalysisSessionImpl session) {
        super(session);
    }

    @Override
    protected void addModelRootAdapterOn(EObject root) {
        if (root instanceof DAnalysis) {
            root.eAdapters().add(new DAnalysisAdapter(session));
        }
    }
    
    @Override
    protected void removeModelRootAdapterFrom(EObject root) {
        if (root instanceof DAnalysis) {
            Iterables.removeIf(root.eAdapters(), Predicates.instanceOf(DAnalysisAdapter.class));
        }
    }
}
