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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.ext.emf.xref.XRefFinder;

/**
 * Concrete implementation of {@link XRefFinder.Factory.Descriptor}.
 * 
 * @author <a href="mailto:esteban.dugueperoux@obeo.fr">Esteban Dugueperoux</a>
 */
public class XRefFinderFactoryDescriptor implements XRefFinder.Factory.Descriptor {

    private static final String CLASS_ATTRIBUTE = "class";

    private static final String RESOURCE_TYPE_ATTRIBUTE = "resourceType";

    private static final String OVERRIDE_ATTRIBUTE = "override";

    private IConfigurationElement element;

    private String id;

    private XRefFinder.Factory xRefFinderFactoryInstance;

    private Class<? extends Resource.Factory> resourceType;

    private String override;

    /**
     * Default constructor.
     * 
     * @param element
     *            {@link IConfigurationElement}
     */
    public XRefFinderFactoryDescriptor(IConfigurationElement element) {
        this.element = element;
        id = element.getDeclaringExtension().getUniqueIdentifier();
        override = element.getAttribute(OVERRIDE_ATTRIBUTE);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public XRefFinder.Factory getFactory() {
        if (xRefFinderFactoryInstance == null) {
            try {
                xRefFinderFactoryInstance = (XRefFinder.Factory) element.createExecutableExtension(CLASS_ATTRIBUTE);
            } catch (CoreException e) {
                throw new WrappedException(e);
            }
        }
        return xRefFinderFactoryInstance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Resource.Factory> getResourceType() {
        if (resourceType == null) {
            try {
                String resourceTypeAttribute = element.getAttribute(RESOURCE_TYPE_ATTRIBUTE);
                if (resourceTypeAttribute != null) {
                    resourceType = (Class<? extends Resource.Factory>) element.createExecutableExtension(RESOURCE_TYPE_ATTRIBUTE).getClass();
                }
            } catch (CoreException e) {
                throw new WrappedException(e);
            }
        }
        return resourceType;
    }

    @Override
    public String getOverride() {
        return override;
    }

}
