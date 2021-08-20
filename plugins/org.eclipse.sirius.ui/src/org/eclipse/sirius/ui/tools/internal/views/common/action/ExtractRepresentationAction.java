/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo and others.
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
package org.eclipse.sirius.ui.tools.internal.views.common.action;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ecore.extender.business.api.permission.IPermissionAuthority;
import org.eclipse.sirius.ecore.extender.business.api.permission.PermissionAuthorityRegistry;
import org.eclipse.sirius.ui.tools.internal.wizards.ExtractRepresentationsWizard;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.sirius.viewpoint.provider.Messages;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * An action to extract selected representations.
 * 
 * @author <a href="mailto:mickael.lanoe@obeo.fr">Mickael LANOE</a>
 */
public class ExtractRepresentationAction extends Action {
    private final Session session;

    private final Collection<DRepresentationDescriptor> repDescriptors;

    /**
     * Construct a new instance.
     * 
     * @param session
     *            the current session
     * @param selection
     *            the selected representations to extract
     */
    public ExtractRepresentationAction(Session session, Collection<DRepresentationDescriptor> selection) {
        super();
        this.session = session;
        this.repDescriptors = selection;

        final ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(SiriusEditPlugin.ID, "/icons/full/others/export.gif"); //$NON-NLS-1$
        this.setImageDescriptor(descriptor);

        this.setText(MessageFormat.format(Messages.ExtractRepresentationAction_label, SiriusUtil.SESSION_RESOURCE_EXTENSION));

        // Disable the action if the selection is not valid
        if (!isValidSelection()) {
            this.setEnabled(false);
        }
    }

    @Override
    public void run() {
        final TransactionalEditingDomain transDomain = session.getTransactionalEditingDomain();
        final ExtractRepresentationsWizard wizard = new ExtractRepresentationsWizard(session, transDomain, repDescriptors);
        final Shell defaultShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        final WizardDialog dialog = new WizardDialog(defaultShell, wizard);
        dialog.create();
        dialog.getShell().setText(Messages.ExtractRepresentationAction_dialogTitle);
        dialog.open();
    }

    /**
     * Test if the selection is valid.
     * 
     * @return true if the selection is valid
     */
    private boolean isValidSelection() {
        return repDescriptors.stream().noneMatch((DRepresentationDescriptor input) -> {
            EObject container = input.eContainer();
            if (container instanceof DView) {
                IPermissionAuthority permissionAuthority = PermissionAuthorityRegistry.getDefault().getPermissionAuthority(container);
                if (permissionAuthority != null && !permissionAuthority.canDeleteInstance(input)) {
                    return true;
                }
            }
            return false;
        });
    }
}
