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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.sirius.business.internal.session.danalysis.DAnalysisSessionImpl;

import com.google.common.base.Preconditions;

// CHECKSTYLE:OFF
public abstract class SiriusAdapter extends AdapterImpl {
    protected final DAnalysisSessionImpl session;

    public SiriusAdapter(DAnalysisSessionImpl session) {
        this.session = Preconditions.checkNotNull(session);
    }

    protected Collection<?> getAddedElements(Notification msg) {
        return getElements(msg.getNewValue());
    }

    protected Collection<?> getRemovedElements(Notification msg) {
        return getElements(msg.getOldValue());
    }

    private Collection<?> getElements(final Object value) {
        final Collection<?> result;
        if (value instanceof Collection<?>) {
            result = (Collection<?>) value;
        } else if (value != null) {
            result = Collections.singleton(value);
        } else {
            result = Collections.emptyList();
        }
        return result;
    }

}
