/*******************************************************************************
 * Copyright (c) 2007, 2015 THALES GLOBAL SERVICES and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.diagram.ui.graphical.edit.policies;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.tools.api.command.IDiagramCommandFactory;
import org.eclipse.sirius.diagram.tools.api.command.IDiagramCommandFactoryProvider;
import org.eclipse.sirius.diagram.ui.internal.edit.policies.SiriusComponentEditPolicy;
import org.eclipse.sirius.diagram.ui.provider.DiagramUIPlugin;
import org.eclipse.sirius.diagram.ui.tools.api.command.GMFCommandWrapper;
import org.eclipse.sirius.diagram.ui.tools.api.editor.DDiagramEditor;
import org.eclipse.sirius.diagram.ui.tools.internal.graphical.edit.policies.DeleteHelper;
import org.eclipse.sirius.diagram.ui.tools.internal.preferences.SiriusDiagramUiInternalPreferencesKeys;
import org.eclipse.sirius.ecore.extender.business.api.permission.IPermissionAuthority;
import org.eclipse.sirius.ecore.extender.business.api.permission.PermissionAuthorityRegistry;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

/**
 * This {@link org.eclipse.gef.EditPolicy} is used to handle the deletion of a
 * node element. It delete the semantic element in the meantime.
 * 
 * @author ymortier
 */
public class NodeDeletionEditPolicy extends SiriusComponentEditPolicy {
    TransactionalEditingDomain editingDomain;

    /**
     * Build the edit policy.
     * 
     * @param editingDomain
     *            current {@link org.eclipse.emf.edit.domain.EditingDomain}.
     */
    public NodeDeletionEditPolicy(final TransactionalEditingDomain editingDomain) {
        this.editingDomain = editingDomain;
    }

    @Override
    protected Command createDeleteSemanticCommand(final GroupRequest deleteRequest) {
        // Delete from model.
        final CompoundCommand cc = new CompoundCommand();
        final View view = (View) getHost().getModel();
        if (getHost().isActive() && view.getElement() instanceof DDiagramElement) {
            final TransactionalEditingDomain transactionalEditingDomain = TransactionUtil.getEditingDomain(view);
            final IAdaptable editor = (IAdaptable) this.getHost().getViewer().getProperty(DDiagramEditor.EDITOR_ID);
            final Object adapter = editor.getAdapter(IDiagramCommandFactoryProvider.class);
            final IDiagramCommandFactoryProvider cmdFactoryProvider = (IDiagramCommandFactoryProvider) adapter;
            final IDiagramCommandFactory diagramCommandFactory = cmdFactoryProvider.getCommandFactory(transactionalEditingDomain);
            final DDiagramElement viewPointElement = (DDiagramElement) view.getElement();

            final IPermissionAuthority authority = PermissionAuthorityRegistry.getDefault().getPermissionAuthority(viewPointElement.getTarget());
            final EObject target = ((DSemanticDecorator) viewPointElement).getTarget();
            if (target == null || target.eResource() == null || authority != null && !authority.canDeleteInstance(target)) {
                return UnexecutableCommand.INSTANCE;
            }
            cc.add(buildGlobalDeleteCommand(diagramCommandFactory, view, viewPointElement));
        }
        return cc;
    }

    private Command buildGlobalDeleteCommand(final IDiagramCommandFactory commandFactory, final View view, final DDiagramElement diagramElement) {
        final CompositeCommand compositeCommand = new CompositeCommand("Delete element");
        org.eclipse.emf.common.command.Command buildedCommand = commandFactory.buildDeleteDiagramElement(diagramElement);
        boolean removeHideNote = DiagramUIPlugin.getPlugin().getPreferenceStore().getBoolean(SiriusDiagramUiInternalPreferencesKeys.PREF_REMOVE_HIDE_NOTE_WHEN_ANNOTED_ELEMENT_HIDDEN_OR_REMOVE.name());
        if (removeHideNote) {
            DeleteHelper.addDeleteLinkedNotesTask(buildedCommand, view);
        } else {
            DeleteHelper.addDeleteLinkedNoteAttachmentsTask(buildedCommand, view);
        }
        compositeCommand.add(new GMFCommandWrapper(editingDomain, buildedCommand));
        return new ICommandProxy(compositeCommand.reduce());
    }

    @Override
    protected boolean shouldDeleteSemantic() {

        if (getHost() instanceof ConnectionNodeEditPart && getHost().getParent() instanceof DiagramRootEditPart) {
            final EditPart content = ((DiagramRootEditPart) getHost().getParent()).getContents();
            if (content instanceof IGraphicalEditPart) {
                final CanonicalEditPolicy cep = (CanonicalEditPolicy) content.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
                if (cep != null) {
                    return cep.isEnabled();
                }
            }
        }
        return super.shouldDeleteSemantic();
    }
}
