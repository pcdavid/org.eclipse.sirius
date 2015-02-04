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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl;

import com.google.common.base.Preconditions;

// CHECKSTYLE:OFF
public abstract class SiriusResourceAdapter extends SiriusAdapter {
    private Resource resource;

    public SiriusResourceAdapter(DAnalysisSessionImpl session) {
        super(session);
    }

    @Override
    public void setTarget(Notifier newTarget) {
        Preconditions.checkArgument(newTarget instanceof Resource);
        super.setTarget(newTarget);
        this.resource = (Resource) newTarget;
        installRootAdapters();
    }

    @Override
    public void notifyChanged(Notification msg) {
        if (msg.getNotifier() != resource) {
            return;
        }
        switch (msg.getFeatureID(ResourceSet.class)) {
        case Resource.RESOURCE__CONTENTS:
            handleContentsChange(msg);
            break;
        }
    }

    private void handleContentsChange(Notification msg) {
        switch (msg.getEventType()) {
        case Notification.ADD:
        case Notification.ADD_MANY:
            for (Object root : getAddedElements(msg)) {
                if (root instanceof EObject) {
                    addModelRootAdapterOn((EObject) root);
                }
            }
            break;
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
            for (Object root : getRemovedElements(msg)) {
                if (root instanceof EObject) {
                    removeModelRootAdapterFrom((EObject) root);
                }
            }
            break;
        }
    }

    private void installRootAdapters() {
        for (EObject root : resource.getContents()) {
            addModelRootAdapterOn(root);
        }
    }

    protected abstract void addModelRootAdapterOn(EObject root);

    protected abstract void removeModelRootAdapterFrom(EObject root);

}
