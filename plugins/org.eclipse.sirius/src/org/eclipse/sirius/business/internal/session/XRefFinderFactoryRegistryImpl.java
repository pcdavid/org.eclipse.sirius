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
package org.eclipse.sirius.business.internal.session;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.sirius.ext.emf.xref.DefaultXRefFinderFactory;
import org.eclipse.sirius.ext.emf.xref.XRefFinder;
import org.eclipse.sirius.viewpoint.SiriusPlugin;

/**
 * Registry of {@link XRefFinder.Factory}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class XRefFinderFactoryRegistryImpl implements XRefFinder.Factory.Registry, IRegistryEventListener {

    /** Name of the extension point to parse for template locations. */
    private static final String XREF_FINDER_FACTORY_EXTENSION_POINT = SiriusPlugin.ID + ".xRefFinderFactory"; //$NON-NLS-1$

    private static final XRefFinder.Factory DEFAULT_XREF_FINDER_FACTORY = new DefaultXRefFinderFactory();

    private Map<String, XRefFinder.Factory.Descriptor> descriptors = new HashMap<String, XRefFinder.Factory.Descriptor>();

    private Map<Resource.Factory, XRefFinder.Factory> factories = new HashMap<Resource.Factory, XRefFinder.Factory>();

    /**
     * Default constructor.
     */
    public XRefFinderFactoryRegistryImpl() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        registry.addListener(this, XREF_FINDER_FACTORY_EXTENSION_POINT);
        parseInitialContributions();
    }

    /**
     * Though this listener reacts to the extension point changes, there could
     * have been contributions before it's been registered. This will parse
     * these initial extensions.
     */
    public void parseInitialContributions() {
        for (IExtension extension : Platform.getExtensionRegistry().getExtensionPoint(XREF_FINDER_FACTORY_EXTENSION_POINT).getExtensions()) {
            parseExtension(extension);
        }
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
            XRefFinder.Factory.Descriptor descriptor = new XRefFinderFactoryDescriptor(elem);
            descriptors.put(descriptor.getId(), descriptor);
            // factories.put(descriptor.getResourceFactory(), descriptor);
        }
    }

    @Override
    public XRefFinder.Factory getFactory(Factory resourceType) {
        // TODO
        XRefFinder.Factory factory = factories.get(resourceType);
        for (XRefFinder.Factory.Descriptor descriptor : descriptors.values()) {
            if (descriptor.getResourceType().isInstance(resourceType)) {
                factory = descriptor.getFactory();
                break;
            }
        }
        if (factory == null) {
            factory = DEFAULT_XREF_FINDER_FACTORY;
        }
        return factory;
    }

    @Override
    public void added(IExtension[] extensions) {
        for (IExtension extension : extensions) {
            parseExtension(extension);
        }
    }

    @Override
    public void removed(IExtension[] extensions) {
        for (IExtension extension : extensions) {
            descriptors.remove(extension.getUniqueIdentifier());
        }
    }

    @Override
    public void added(IExtensionPoint[] extensionPoints) {
        // no need to listen to this event
    }

    @Override
    public void removed(IExtensionPoint[] extensionPoints) {
        // no need to listen to this event
    }

    /**
     * Dispose this registry.
     */
    public void dispose() {
        factories.clear();
    }

}
