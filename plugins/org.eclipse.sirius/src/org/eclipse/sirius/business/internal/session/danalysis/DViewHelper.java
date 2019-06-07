/*******************************************************************************
 * Copyright (c) 2018 Obeo.
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
package org.eclipse.sirius.business.internal.session.danalysis;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.query.DViewQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.DView;

/**
 * Helper operations on {@link DView}.
 * 
 * @author pcdavid
 */
public final class DViewHelper {
    private DViewHelper() {
        // Prevents instantiation.
    }

    /**
     * Refresh all the (loaded) representations inside the view and delete the obsolete ones.
     * 
     * @param self
     *            the view to refresh.
     */
    public static void refreshViewContents(DView self) {
        final Set<DRepresentation> representationsToDelete = new LinkedHashSet<>();
        for (DRepresentation representation : new DViewQuery(self).getLoadedRepresentations()) {
            if (isDangling(representation)) {
                representationsToDelete.add(representation);
            } else {
                DialectManager.INSTANCE.refresh(representation, new NullProgressMonitor());
            }
        }
        Session.of(self).ifPresent(session -> {
            ModelAccessor ma = session.getModelAccessor();
            ECrossReferenceAdapter xref = session.getSemanticCrossReferencer();
            for (DRepresentation dangling : representationsToDelete) {
                ma.eDelete(dangling, xref);
            }
        });
    }

    private static boolean isDangling(DRepresentation representation) {
        return representation instanceof DSemanticDecorator && ((DSemanticDecorator) representation).getTarget() == null;
    }
}
