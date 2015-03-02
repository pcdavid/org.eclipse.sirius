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
package org.eclipse.sirius.ext.base.collect;

import java.util.Set;

/**
 * Associate arbitrary sets of tags to elements, and retrieve the elements
 * efficiently according to their tags.
 * 
 * @param <V>
 *            the type of tagged values.
 * @param <T>
 *            the type of the tags.
 * @author pcdavid
 */
public interface Tagger<V, T> {
    /**
     * Marks a value with all the specified tags.
     * 
     * @param value
     *            a non-null value.
     * @param tags
     *            all the tags to associate with the value.
     * @return <code>true</code> iff at least one of the tags was not already
     *         associated with the value.
     */
    boolean tag(V value, T... tags);

    /**
     * Dissociate the specified tags from a value.
     * 
     * @param value
     *            a non-null value.
     * @param tags
     *            all the tags to associate with the value.
     * @return <code>true</code> iff at least one of the tags was not already
     *         associated with the value.
     */
    boolean untag(V value, T... tags);

    /**
     * Tests if a value has a specific tag associated to it.
     * 
     * @param value
     *            the value.
     * @param tag
     *            the tag.
     * @return <code>true</code> iff the tag is currently associated to the
     *         value.
     */
    boolean hasTag(V value, T tag);

    /**
     * Finds all the values which have all the specified tags associated to
     * them.
     * 
     * @param tags
     *            all the tags which must be associated with the searched
     *            values. Must be non-empty.
     * @return all the values that have all the specified tags associated to
     *         them.
     */
    Set<V> find(T... tags);

    /**
     * Returns all the values which have at least one tag associated to them.
     * 
     * @return all the tagged values.
     */
    Set<V> taggedValues();

    /**
     * Returns all the tags currently associated with at least one value.
     * 
     * @return all the tags currently used.
     */
    Set<T> tags();
    
    /**
     * Clears all associations between any value and tags.
     */
    void clear();
}
