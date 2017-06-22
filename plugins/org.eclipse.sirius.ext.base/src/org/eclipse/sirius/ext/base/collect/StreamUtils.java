/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ext.base.collect;

import java.util.stream.Stream;

/**
 * Utility methods on {@link Stream}s.
 * 
 * @author pcdavid
 */
public final class StreamUtils {
    private StreamUtils() {
        // Prevents instantiation.
    }

    /**
     * Filters a stream to retain only the elements which are <code>instanceof</code> a given type.
     * 
     * @param input
     *            the stream to filter.
     * @param klass
     *            the type of elements to keep.
     * @param <T>
     *            the type of elements to keep.
     * @return the filtered stream.
     * 
     */
    public <T> Stream<T> keepInstances(Stream<? extends T> input, Class<T> klass) {
        return input.filter(klass::isInstance).map(klass::cast);
    }
}
