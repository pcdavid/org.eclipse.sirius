/*******************************************************************************
 * Copyright (c) 2017, 2021 THALES GLOBAL SERVICES and others.
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
package org.eclipse.sirius.diagram.ui.tools.internal.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;

/**
 * Cache of pre-rendered images.
 */
public class ImageCache {
    /**
     * The maximum size of the cache, in bytes.
     */
    private static final int MAX_WEIGHT;

    static {
        String s = System.getProperty("org.eclipse.sirius.diagram.ui.svg.maxCacheSizeMB"); //$NON-NLS-1$
        int mb;
        try {
            mb = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            mb = 50;
        }
        MAX_WEIGHT = mb * 1024 * 1024;
    }

    /**
     * The rendered bitmaps, organized by key.
     */
    // @formatter:off
    private final Cache<String, Image> images = CacheBuilder.newBuilder()
                                                            .maximumWeight(ImageCache.MAX_WEIGHT)
                                                            .removalListener(this::onImageRemoved)
                                                            .weigher(this::imageWeigh)
                                                            .build();
    // @formatter:on

    /**
     * Get the image cached or create new one and cache it.
     *
     * @param key
     *            the key
     * @param renderer
     *            the (possibly costly) code to invoke to perform the rendering is the key is missing.
     * @return an image store in a cache
     */
    public synchronized Image getImage(String key, Supplier<Optional<Image>> renderer) {
        Image result = images.getIfPresent(key);
        if (result == null || result.isDisposed()) {
            Optional<Image> optionalImage = renderer.get();
            if (optionalImage.isPresent()) {
                result = optionalImage.get();
            }
            if (result != null) {
                images.put(key, result);
            }
        }
        return result;
    }

    /**
     * Remove all entries whose key begins with the given key. Remove from the document map, the entries with the given
     * keys to force to re-read the file.
     *
     * @param documentKey
     *            the document key.
     * @return true of something was removed.
     */
    public synchronized boolean doRemoveFromCache(String documentKey) {
        if (!StringUtil.isEmpty(documentKey)) {
            boolean remove = false;
            Collection<String> keyToRemove = new ArrayList<>();
            for (String key : images.asMap().keySet()) {
                if (key.startsWith(documentKey)) {
                    keyToRemove.add(key);
                }
            }

            for (String toRemove : keyToRemove) {
                images.invalidate(toRemove);
                remove = true;
            }
            return remove;
        }
        return false;
    }
    
    private int imageWeigh(String key, Image value) {
        if (value != null) {
            synchronized (value) {
                if (!value.isDisposed()) {
                    Rectangle bounds = value.getBounds();
                    return bounds.width * bounds.height * 4;
                }
            }
        }
        return 0;
    }
    
    private void onImageRemoved(RemovalNotification<String, Image> notification) {
        Image img = notification.getValue();
        synchronized (img) {
            img.dispose();
        }
    }
}
