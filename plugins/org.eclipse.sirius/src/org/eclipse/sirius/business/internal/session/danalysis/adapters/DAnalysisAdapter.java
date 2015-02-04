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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.ViewpointPackage;

import com.google.common.base.Preconditions;

// CHECKSTYLE:OFF
public class DAnalysisAdapter extends SiriusAdapter {
    private DAnalysis analysis;

    public DAnalysisAdapter(DAnalysisSessionImpl session) {
        super(session);
    }
    
    @Override
    public void setTarget(Notifier newTarget) {
        Preconditions.checkArgument(newTarget instanceof DAnalysis);
        super.setTarget(newTarget);
        this.analysis = (DAnalysis) newTarget;
        installViewsAdapters();
    }

    private void installViewsAdapters() {
        for (DView view : analysis.getOwnedViews()) {
            addDViewAdapterOn(view);
        }
    }

    @Override
    public void notifyChanged(Notification msg) {
        if (msg.getNotifier() != analysis) {
            return;
        }
        switch (msg.getFeatureID(DAnalysis.class)) {
        case ViewpointPackage.DANALYSIS__REFERENCED_ANALYSIS:
            break;
        case ViewpointPackage.DANALYSIS__MODELS:
            break;
        case ViewpointPackage.DANALYSIS__OWNED_VIEWS:
            for (Object newView : getAddedElements(msg)) {
                if (newView instanceof DView) {
                    addDViewAdapterOn((DView) newView);
                }
            }
            break;
        case ViewpointPackage.DANALYSIS__SELECTED_VIEWS:
            break;
        }
    }

    private void addDViewAdapterOn(DView view) {
        System.out.println("Dview registered: " + EcoreUtil.getURI(view));
    }

}
