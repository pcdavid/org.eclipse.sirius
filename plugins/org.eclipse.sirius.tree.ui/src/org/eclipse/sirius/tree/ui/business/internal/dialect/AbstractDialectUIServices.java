/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tree.ui.business.internal.dialect;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.tree.ui.tools.internal.editor.DTreeEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIServices;
import org.eclipse.sirius.ui.business.api.dialect.ExportFormat;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.ui.IEditorPart;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * Common abstract class to help bootstrap concrete implementations of
 * {@code DialectUIServices}.
 * 
 * @author pcdavid
 */
public abstract class AbstractDialectUIServices implements DialectUIServices {

    @Override
    public boolean closeEditor(IEditorPart editorPart, boolean save) {
        boolean result = false;
        if (canHandleEditor(editorPart)) {
            try {
                ((DTreeEditor) editorPart).close(save);
            } catch (NullPointerException e) {
                // we might have an exception closing an editor which is
                // already in trouble
            }
            // We suppose it is closed.
            result = true;
        }
        return result;
    }

    @Override
    public Collection<CommandParameter> provideTools(EObject object) {
        return Collections.emptyList();
    }

    @Override
    public Collection<CommandParameter> provideAdditionalMappings(EObject object) {
        return Collections.emptyList();
    }

    @Override
    public boolean canExport(ExportFormat format) {
        return false;
    }

    @Override
    public void export(DRepresentation representation, Session session, IPath path, ExportFormat format, IProgressMonitor monitor) {
        // Nothing to do for trees.
    }

    @Override
    public Collection<DSemanticDecorator> getSelection(DialectEditor editor) {
        Collection<DSemanticDecorator> selection = Sets.newLinkedHashSet();
        if (canHandleEditor(editor)) {
            ISelection sel = editor.getSite().getSelectionProvider().getSelection();
            if (sel instanceof IStructuredSelection) {
                Iterables.addAll(selection, Iterables.filter(((IStructuredSelection) sel).toList(), DSemanticDecorator.class));
            }
        }
        return selection;
    }

}
