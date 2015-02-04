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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * The single adapter attached by Sirius on a ResourceSet when that ResourceSet
 * contains a Sirius session.
 * 
 * @author pcdavid
 */
// CHECKSTYLE:OFF
public class SiriusResourceSetAdapter extends SiriusAdapter {
    private ResourceSet resourceSet;

    public SiriusResourceSetAdapter(DAnalysisSessionImpl session) {
        super(session);
    }

    @Override
    public void setTarget(Notifier newTarget) {
        Preconditions.checkArgument(newTarget instanceof ResourceSet);
        super.setTarget(newTarget);
        this.resourceSet = (ResourceSet) newTarget;
        installResourceAdapters();
    }

    /**
     * Install the appropriate SiriusResourceAdapter on all resources currently
     * in the ResourceSet.
     */
    private void installResourceAdapters() {
        for (Resource res : resourceSet.getResources()) {
            addResourceAdapterOn(res);
        }
    }

    @Override
    public void notifyChanged(Notification msg) {
        if (msg.getNotifier() == resourceSet && msg.getFeatureID(ResourceSet.class) == ResourceSet.RESOURCE_SET__RESOURCES) {
            switch (msg.getEventType()) {
            case Notification.ADD:
            case Notification.ADD_MANY:
                for (Object resource : getAddedElements(msg)) {
                    if (resource instanceof Resource) {
                        addResourceAdapterOn((Resource) resource);
                    }
                }
                break;
            case Notification.REMOVE:
            case Notification.REMOVE_MANY:
                for (Object resource : getRemovedElements(msg)) {
                    if (resource instanceof Resource) {
                        removeResourceAdapterFrom((Resource) resource);
                    }
                }
                break;
            }
        }
    }

    private void addResourceAdapterOn(Resource res) {
        if (Objects.equal(res.getURI().fileExtension(), SiriusUtil.SESSION_RESOURCE_EXTENSION)) {
            res.eAdapters().add(new SiriusRepresentationResourceAdapter(session));
        }
    }
    
    private void removeResourceAdapterFrom(Resource res) {
        Iterables.removeIf(res.eAdapters(), Predicates.instanceOf(SiriusResourceAdapter.class));
    }
    
    @Override
    public String toString() {
        return "SiriusResourceSetAdapter for " + session + " on " + resourceSet.toString();
    }

}
