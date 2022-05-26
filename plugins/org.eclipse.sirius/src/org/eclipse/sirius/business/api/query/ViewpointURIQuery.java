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
package org.eclipse.sirius.business.api.query;

import java.io.IOException;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.viewpoint.description.DescriptionPackage;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import com.google.common.base.Preconditions;

/**
 * Queries about viewpoint URIs of the form
 * <code>viewpoint:/pluginId/SiriusId</code>.
 * 
 * @author pierre-charles.david@obeo.fr
 */
public class ViewpointURIQuery {
    /**
     * THe URI scheme used for Sirius URIs.
     */
    public static final String VIEWPOINT_URI_SCHEME = "viewpoint"; //$NON-NLS-1$

    /**
     * The URI to query.
     */
    private final URI uri;

    /**
     * Constructor.
     * 
     * @param uri
     *            the URI to query. Must be a {@link #isValidViewpointURI(URI)
     *            valid} Sirius URI.
     */
    public ViewpointURIQuery(URI uri) {
        Preconditions.checkArgument(ViewpointURIQuery.isValidViewpointURI(uri));
        this.uri = uri;
    }

    /**
     * Checks whether a URI is a valid Sirius URI.
     * 
     * @param uri
     *            the URI to test.
     * @return <code>true</code> of <code>uri</code> is a valid Sirius URI.
     */
    public static boolean isValidViewpointURI(URI uri) {
        boolean usesViewpointScheme = uri != null && ViewpointURIQuery.VIEWPOINT_URI_SCHEME.equals(uri.scheme());
        return usesViewpointScheme && uri.segmentCount() >= 2;
    }

    /**
     * Attempts to convert a concrete URI to a element from a VSM into a
     * corresponding logical Sirius URI.
     * 
     * @param uri
     *            the concrete URI to convert.
     * @param resourceSet
     *            a ResourceSet in which the concrete URI can be resolved into a
     *            VSM element.
     * @return a logical viewpoint URI equivalent to the concrete URI, if the
     *         conversion was successful.
     */
    public static Optional<URI> asViewpointURI(URI uri, ResourceSet resourceSet) {
        Optional<URI> result = Optional.empty();
        if (uri.isPlatform()) {
            EObject target = null;
            try {
                target = resourceSet.getEObject(uri, true);
            } catch (WrappedException e) {
                if (e.getCause() instanceof IOException && uri.isPlatformPlugin()) {
                    URI convertedResourceUri = URI.createPlatformResourceURI(uri.toPlatformString(true), true).appendFragment(uri.fragment());
                    // this resource is potentially in the workspace and should
                    // be a PlatformResource instead of ResourceResource
                    target = resourceSet.getEObject(convertedResourceUri, true);
                }
            }
            if (target != null) {
                String pluginId = uri.segment(1);
                if (target instanceof Viewpoint) {
                    String viewpointName = ((Viewpoint) target).getName();
                    URI logicalViewpointUri = URI.createURI(ViewpointURIQuery.VIEWPOINT_URI_SCHEME + ":/" + pluginId + "/" + viewpointName); //$NON-NLS-1$ //$NON-NLS-2$
                    result = Optional.of(logicalViewpointUri);
                } else {
                    Optional<EObject> viewpointContext = new EObjectQuery(target).getFirstAncestorOfType(DescriptionPackage.eINSTANCE.getViewpoint());
                    if (viewpointContext.isPresent()) {
                        String viewpointName = ((Viewpoint) viewpointContext.get()).getName();
                        URI logicalViewpointUri = URI.createURI(ViewpointURIQuery.VIEWPOINT_URI_SCHEME + ":/" + pluginId + "/" + viewpointName); //$NON-NLS-1$ //$NON-NLS-2$
                        result = Optional.of(logicalViewpointUri.appendFragment(uri.fragment()));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Return the identifier of the plug-in in which the viewpoint element
     * referenced by this URI is defined.
     * 
     * @return the plug-in identifier part of the URI.
     */
    public String getPluginId() {
        return uri.segment(0);
    }

    /**
     * Returns the name of the viewpoint in which the element referenced by this
     * URI is defined.
     * 
     * @return the Sirius name part of the URI.
     */
    public String getViewpointName() {
        return URI.decode(uri.segment(1));
    }

    /**
     * Returns the Viewpoint URI denoting the viewpoint itself.
     * 
     * @return the URI of the viewpoint
     */
    public URI getBaseURI() {
        return uri.trimFragment();
    }
}
