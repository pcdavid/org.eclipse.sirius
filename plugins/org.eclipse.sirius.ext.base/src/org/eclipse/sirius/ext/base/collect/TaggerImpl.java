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

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * Basic implementation of {@link Tagger}.
 * 
 * @param <V>
 *            the type of tagged values.
 * @param <T>
 *            the type of the tags.
 * @author pcdavid
 */
public class TaggerImpl<V, T> implements Tagger<V, T> {
    private final Multimap<T, V> tagged = HashMultimap.create();

    /**
     * Create a new tagger.
     * 
     * @param <V>
     *            the type of tagged values.
     * @param <T>
     *            the type of the tags.
     * @return a new tagger.
     */
    public static final <V, T> TaggerImpl<V, T> create() {
        return new TaggerImpl<V, T>();
    }

    @Override
    public boolean tag(V value, T... tags) {
        Preconditions.checkNotNull(value, "Can not tag null");
        if (tags != null) {
            boolean result = false;
            for (T tag : tags) {
                boolean modified = tagged.put(tag, value);
                result = result || modified;
            }
            return result;
        }
        return false;
    }

    @Override
    public boolean untag(V value, T... tags) {
        Preconditions.checkNotNull(value, "Can not untag null");
        if (tags != null) {
            boolean result = false;
            for (T tag : tags) {
                boolean modified = tagged.remove(tag, value);
                result = result || modified;
            }
            return result;
        }
        return false;
    }

    @Override
    public boolean hasTag(V value, T tag) {
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(tag);
        return tagged.containsEntry(tag, value);
    }

    @Override
    public Set<V> find(T... tags) {
        Preconditions.checkArgument(tags != null && tags.length > 0, "A non-empty list of tags is required.");
        Set<V> result = Sets.newHashSet(tagged.get(tags[0]));
        for (int i = 1; i < tags.length; i++) {
            result.retainAll(tagged.get(tags[i]));
        }
        return ImmutableSet.copyOf(result);
    }

    @Override
    public Set<V> taggedValues() {
        return ImmutableSet.copyOf(tagged.values());
    }

    @Override
    public Set<T> tags() {
        return ImmutableSet.copyOf(tagged.keySet());
    }

    @Override
    public void clear() {
        tagged.clear();
    }
}
