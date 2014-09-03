/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.tools.internal.views.common.item;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ext.base.Options;
import org.eclipse.sirius.ui.tools.api.views.common.item.ItemWrapper;

/**
 * Controlled resource root item wrapper class.
 * 
 * @author mporhel
 */
public class ControlledRoot implements ItemWrapper, IAdaptable {

    private final EObject root;

    private final Object parent;
    
    private Option<Session> optSession;

    /**
     * Construct a new resource item wrapper.
     * 
     * @param root
     *            the reresented controlled resource root EObject.
     * @param parent
     *            Parent tree item
     */
    public ControlledRoot(final EObject root, final Object parent) {
        this.root = root;
        this.parent = parent;
    }

    @Override
    public Object getWrappedObject() {
        return root;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((root == null) ? 0 : root.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = true;
        if (this == obj) {
            result = true;
        } else if (obj == null) {
            result = false;
        } else if (getClass() != obj.getClass()) {
            result = false;
        } else {
            ControlledRoot other = (ControlledRoot) obj;
            if (parent == null) {
                if (other.parent != null) {
                    result = false;
                }
            } else if (!parent.equals(other.parent)) {
                result = false;
            }
            if (root == null) {
                if (other.root != null) {
                    result = false;
                }
            } else if (!root.equals(other.root)) {
                result = false;
            }
        }
        return result;
    }
    
    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public Collection<?> getChildren() {
        // This controlled root is just here to show that there is a fragment,
        // and is not supposed to have children. Children will be displayed in
        // the parent resource (similar to the Ecore reflective editor).
        return Collections.emptyList();
    }

    @Override
    public Option<Session> getSession() {
        if (optSession == null) {
            Session session = null;
            if (root != null) {
                session = SessionManager.INSTANCE.getSession(root);
            }
            optSession = Options.newSome(session);
        }
        return optSession;
    }

    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        if (root != null && adapter == EObject.class) {
            return root;
        }

        return null;
    }

    @Override
    public Object getParent() {
        return parent;
    }
}
