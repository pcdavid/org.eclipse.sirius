/*******************************************************************************
 * Copyright (c) 2017 Obeo
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/

package org.eclipse.sirius.ui.tools.internal.views.common.labeldecorator;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.tools.internal.views.common.FileSessionFinder;
import org.eclipse.sirius.ui.tools.internal.views.modelexplorer.ModelExplorerView;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.collect.Lists;

/**
 * This label decorator provides a specific icon for aird item that have corresponding session loaded to distinguish
 * aird loaded from the one not loaded.
 * 
 * @author <a href=mailto:pierre.guilet@obeo.fr>Pierre Guilet</a>
 *
 */
public class AirdLabelDecorator implements ILabelDecorator {
    /**
     * The image descriptor used to provide an image specific to aird that are loaded.
     */
    public static final ImageDescriptor SESSION_LOADED_IMAGE_DESCRIPTOR = AbstractUIPlugin.imageDescriptorFromPlugin(SiriusEditPlugin.ID, "/icons/obj16/sessionLoaded.gif"); //$NON-NLS-1$ ;
    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse. jface.viewers.ILabelProviderListener)
     */

    @Override
    public void addListener(ILabelProviderListener listener) {

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    @Override
    public void dispose() {

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang. Object, java.lang.String)
     */
    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.
     * jface.viewers.ILabelProviderListener)
     */
    @Override
    public void removeListener(ILabelProviderListener listener) {

    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelDecorator#decorateImage(org.eclipse.swt. graphics.Image, java.lang.Object)
     */
    @Override
    public Image decorateImage(Image image, Object element) {
        boolean useSessionLoadedImage = false;
        if (!ModelExplorerView.class.getTypeName().equals(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getClass().getTypeName()) && element instanceof IFile) {
            List<Session> relatedSessions = FileSessionFinder.getRelatedSessions(Lists.newArrayList((IFile) element), false, false);
            for (Session session : relatedSessions) {
                if (session.isOpen()) {
                    useSessionLoadedImage = true;
                    break;
                }
            }
        }
        if (useSessionLoadedImage) {
            return SiriusEditPlugin.getPlugin().getImage(SESSION_LOADED_IMAGE_DESCRIPTOR);
        }
        return image;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelDecorator#decorateText(java.lang.String, java.lang.Object)
     */
    @Override
    public String decorateText(String text, Object element) {
        return text;
    }

}
