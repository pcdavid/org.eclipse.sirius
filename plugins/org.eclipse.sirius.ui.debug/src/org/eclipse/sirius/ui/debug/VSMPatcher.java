/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.debug;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.sirius.business.api.migration.AbstractVSMMigrationParticipant;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.osgi.framework.Version;

public class VSMPatcher extends AbstractVSMMigrationParticipant {

    public static final Version MIGRATION_VERSION = new Version("12.1.0.201809181200"); //$NON-NLS-1$

    @Override
    public Version getMigrationVersion() {
        return MIGRATION_VERSION;
    }

    @Override
    protected void postLoad(Group group, Version loadedVersion) {
        super.postLoad(group, loadedVersion);
        transformVSM(group);
    }

    private void transformVSM(Group group) {
        // Put your VSM transformation here.
        if ("EcoreTools".equals(group.getName())) {
            Collection<Viewpoint> toRemove = new ArrayList<>();
            for (Viewpoint vp : group.getOwnedViewpoints()) {
                if ("Design".equals(vp.getName())) {
                    vp.setName("Conception");
                }
                if ("Archetype".equals(vp.getName())) {
                    toRemove.add(vp);
                }
            }
            group.getOwnedViewpoints().removeAll(toRemove);
        }
    }

}
