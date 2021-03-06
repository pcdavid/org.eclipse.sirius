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
package org.eclipse.sirius.ui.tools.internal.views.modelexplorer.extension.tab;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;

/**
 * This listener will allow us to be aware of contribution changes against the
 * {@link ModelExplorerTabRegistryListener#MODEL_EXPLORER_TAB_EXTENSION_POINT}
 * extension point.
 * 
 * @author mporhel
 * 
 */
public class ModelExplorerTabRegistryListener implements IRegistryChangeListener {

    /** Name of the extension point to parse for extensions. */
    public static final String MODEL_EXPLORER_TAB_EXTENSION_POINT = "org.eclipse.sirius.ui.modelExplorerTabExtension"; //$NON-NLS-1$

    /** Name of the extension point's "Model Explorer Tab Extension" tag. */
    private static final String MODEL_EXPLORER_TAB_EXTENSION = "modelExplorerTabExtension"; //$NON-NLS-1$

    /**
     * initialize this listener.
     */
    public void init() {
        final IExtensionRegistry registry = Platform.getExtensionRegistry();
        registry.addRegistryChangeListener(this, ModelExplorerTabRegistryListener.MODEL_EXPLORER_TAB_EXTENSION_POINT);
        this.parseInitialContributions();
    }

    /**
     * Dispose this listener.
     */
    public void dispose() {
        final IExtensionRegistry registry = Platform.getExtensionRegistry();
        registry.removeRegistryChangeListener(this);
        ModelExplorerTabRegistry.clearRegistry();
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
            if (MODEL_EXPLORER_TAB_EXTENSION.equals(elem.getName())) {

                try {
                    ModelExplorerTabRegistry.addExtension(new ModelExplorerTabDescriptor(elem));
                } catch (IllegalArgumentException e) {
                    // Do nothing
                }
            }
        }
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.IRegistryEventListener#added(org.eclipse.core.runtime.IExtensionPoint[])
     */
    public void added(IExtensionPoint[] extensionPoints) {
        for (IExtensionPoint extensionPoint : extensionPoints) {
            for (IExtension extension : extensionPoint.getExtensions()) {
                parseExtension(extension);
            }
        }
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.IRegistryChangeListener#registryChanged(org.eclipse.core.runtime.IRegistryChangeEvent)
     */
    public void registryChanged(IRegistryChangeEvent event) {
        Set<IExtension> addedExtensions = new LinkedHashSet<>();
        for (IExtensionDelta extensionDelta : event.getExtensionDeltas()) {
            addedExtensions.add(extensionDelta.getExtension());
        }
        added(addedExtensions.toArray(new IExtension[addedExtensions.size()]));
    }

    /**
     * Behavior when the given extensions are added.
     * 
     * @param extensions
     *            the added extensions
     */
    public void added(IExtension[] extensions) {
        for (IExtension extension : extensions) {
            parseExtension(extension);
        }
    }

    /**
     * Though this listener reacts to the extension point changes, there could
     * have been contributions before it's been registered. This will parse
     * these initial contributions.
     */
    private void parseInitialContributions() {
        final IExtensionRegistry registry = Platform.getExtensionRegistry();

        for (IExtension extension : registry.getExtensionPoint(MODEL_EXPLORER_TAB_EXTENSION_POINT).getExtensions()) {
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
                if (MODEL_EXPLORER_TAB_EXTENSION_POINT.equals(elem.getName())) {
                    final String extensionClassName = elem.getAttribute(ModelExplorerTabDescriptor.MODEL_EXPLORER_TAB_CLASS_NAME);
                    ModelExplorerTabRegistry.removeExtension(extensionClassName);
                }
            }
        }
    }

}
