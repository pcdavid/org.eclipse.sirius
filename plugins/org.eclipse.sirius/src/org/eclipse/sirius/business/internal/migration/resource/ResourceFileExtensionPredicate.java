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
package org.eclipse.sirius.business.internal.migration.resource;

import java.util.function.Predicate;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * A Predicate checking for a given file extension.
 *
 * @author cbrun
 */
public class ResourceFileExtensionPredicate implements Predicate<Resource> {

    private final String fileExtension;

    private final boolean shouldBePlatformResource;

    /**
     * Default constructor.
     *
     * @param fileExtension
     *            The extension of the file to check.
     */
    public ResourceFileExtensionPredicate(final String fileExtension) {
        this(fileExtension, true);
    }

    /**
     * Default constructor.
     *
     * @param fileExtension
     *            The extension of the file to check.
     * @param shouldBePlatformResource
     *            true if we must check that this resource is a platform resource, false otherwise
     */
    public ResourceFileExtensionPredicate(final String fileExtension, final boolean shouldBePlatformResource) {
        this.fileExtension = fileExtension;
        this.shouldBePlatformResource = shouldBePlatformResource;
    }

    @Override
    public boolean test(Resource input) {
        boolean result = true;
        if (shouldBePlatformResource) {
            result = input.getURI().isPlatformResource();
        }
        return result && input.getURI() != null && input.getURI().fileExtension() != null && input.getURI().fileExtension().equals(fileExtension);
    }

}
