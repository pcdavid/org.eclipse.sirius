/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.ui.business.internal.session.factory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;

/**
 * This listener will allow us to be aware of contribution changes against the
 * uiSessionFactory extension point.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
// NOTE : IRegistryEventListener doesn't exists with Eclipse 3.3
public class UISessionFactoryDescriptorRegistryListener implements IRegistryChangeListener {

    /** Name of the extension point to parse for extensions. */
    public static final String UI_SESSION_FACTORY_EXTENSION_POINT = SiriusEditPlugin.ID + ".uiSessionFactory"; //$NON-NLS-1$

    /** Name of the extension point's "uiSessionFactory" tag. */
    private static final String UI_SESSION_FACTORY_TAG_EXTENSION = "uiSessionFactory"; //$NON-NLS-1$

    /**
     * Register this listener and parse initial contributions.
     */
    public void init() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        registry.addRegistryChangeListener(this, UI_SESSION_FACTORY_EXTENSION_POINT);
        parseInitialContributions();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtension[])
     */
    public void added(IExtension[] extensions) {
        for (IExtension extension : extensions) {
            parseExtension(extension);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtensionPoint[])
     */
    public void added(IExtensionPoint[] extensionPoints) {
        // no need to listen to this event
    }

    /**
     * Though this listener reacts to the extension point changes, there could
     * have been contributions before it's been registered. This will parse
     * these initial contributions.
     */
    public void parseInitialContributions() {
        final IExtensionRegistry registry = Platform.getExtensionRegistry();

        for (IExtension extension : registry.getExtensionPoint(UI_SESSION_FACTORY_EXTENSION_POINT).getExtensions()) {
            parseExtension(extension);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtension[])
     */
    public void removed(IExtension[] extensions) {
        for (IExtension extension : extensions) {
            final IConfigurationElement[] configElements = extension.getConfigurationElements();
            for (IConfigurationElement elem : configElements) {
                if (UI_SESSION_FACTORY_TAG_EXTENSION.equals(elem.getName())) {
                    final String extensionClassName = elem.getAttribute(EclipseUISessionFactoryDescriptor.UI_SESSION_FACTORY_CLASS_ATTRIBUTE);
                    UISessionFactoryDescriptorRegistry.removeExtension(extensionClassName);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.IRegistryEventListener#removed(org.eclipse.core.runtime.IExtensionPoint[])
     */
    public void removed(IExtensionPoint[] extensionPoints) {
        // no need to listen to this event
    }

    /**
     * Parses a single extension contribution.
     * 
     * @param extension
     *            Parses the given extension and adds its contribution to the
     *            registry.
     */
    private void parseExtension(IExtension extension) {
        final IConfigurationElement[] configElements = extension.getConfigurationElements();
        for (IConfigurationElement elem : configElements) {
            if (UI_SESSION_FACTORY_TAG_EXTENSION.equals(elem.getName())) {
                UISessionFactoryDescriptorRegistry.addExtension(new EclipseUISessionFactoryDescriptor(elem));
            }
        }
    }

    /**
     * Overridden to support TP 3.3, because IRegistryEventListener exists only
     * from 3.4.
     * 
     * {@inheritDoc}
     */
    public void registryChanged(IRegistryChangeEvent event) {
        IExtensionDelta[] deltas = event.getExtensionDeltas();
        List<IExtension> addedExtensions = new ArrayList<IExtension>();
        List<IExtension> removedExtensions = new ArrayList<IExtension>();
        for (int i = 0; i < deltas.length; i++) {
            if (!UI_SESSION_FACTORY_EXTENSION_POINT.equals(deltas[i].getExtensionPoint().getUniqueIdentifier()))
                continue;
            if (deltas[i].getKind() == IExtensionDelta.ADDED) {
                IExtension extension = deltas[i].getExtension();
                addedExtensions.add(extension);
            } else if (deltas[i].getKind() == IExtensionDelta.REMOVED) {
                IExtension extension = deltas[i].getExtension();
                removedExtensions.add(extension);
            }
        }
        added(addedExtensions.toArray(new IExtension[0]));
        removed(addedExtensions.toArray(new IExtension[0]));
    }

    /**
     * Remove this listener and flush the associated registry.
     */
    public void dispose() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        registry.removeRegistryChangeListener(this);
        UISessionFactoryDescriptorRegistry.clearRegistry();
    }

}
