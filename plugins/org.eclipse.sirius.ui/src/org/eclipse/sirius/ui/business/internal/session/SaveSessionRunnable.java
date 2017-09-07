/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.business.internal.session;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.ui.business.api.editor.ISiriusEditor;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.progress.IJobRunnable;

/**
 * A {@link IJobRunnable} to save a {@link Session session}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class SaveSessionRunnable implements IJobRunnable {

    /** The {@link Session} to save. */
    protected Session session;

    /**
     * Default constructor.
     * 
     * @param session
     *            the Session to save
     */
    public SaveSessionRunnable(Session session) {
        this.session = session;
    }

    @Override
    public IStatus run(IProgressMonitor monitor) {
        if (session != null) {
            Optional<IEditingSession> uiSession = Optional.ofNullable(SessionUIManager.INSTANCE.getUISession(session));
            Collection<ISiriusEditor> attachedEditors = uiSession.map(IEditingSession::getSiriusEditors).orElse(Collections.emptyList());
            IEditorPart activeEditor = EclipseUIUtil.getActiveEditor();
            if (attachedEditors.contains(activeEditor)) {
                /*
                 * If the active editor is an ISiriusEditor attached to this session: forward the save request to it.
                 */
                activeEditor.doSave(monitor);
            } else if (!attachedEditors.isEmpty()) {
                /*
                 * The active editor is not an ISiriusEditor, but there's at least one opened editor attached to the
                 * session: forward the the first (arbitrary choice).
                 */
                attachedEditors.iterator().next().doSave(monitor);
            } else {
                /*
                 * No editors associated with the session currently opened: save the session directly.
                 */
                session.save(monitor);
            }
        }
        return Status.OK_STATUS;
    }
}
