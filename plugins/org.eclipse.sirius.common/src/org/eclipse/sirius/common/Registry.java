package org.eclipse.sirius.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

public class Registry<K, V> implements Iterable<V> {

    public interface Listener<V> {
        void registryChanged(Collection<V> registered, Collection<V> unregistered);
    }

    private final Function<V, K> keyFunction;

    private final Map<K, V> entries = Collections.synchronizedMap((new LinkedHashMap<K, V>()));
    
    private final CopyOnWriteArrayList<Listener<V>> listeners = new CopyOnWriteArrayList<Registry.Listener<V>>();
    
    public Registry(Function<V, K> keyFunction) {
        this.keyFunction = Preconditions.checkNotNull(keyFunction);
    }

    public boolean addListener(Listener<V> l) {
        Preconditions.checkNotNull(l);
        return listeners.addIfAbsent(l);
    }

    public boolean removeListener(Listener<V> l) {
        return listeners.remove(l);
    }

    public Optional<V> get(K key) {
        return Optional.fromNullable(entries.get(key));
    }

    public V register(V value) {
        K key = keyFunction.apply(value);
        if (key != null) {
            return entries.put(key, value);
        } else {
            return null;
        }
    }

    public boolean unregister(V value) {
        return false;
    }

    public boolean registerAll(Set<V> ts) {
        return false;
    }

    public boolean unregisterAll(Set<V> ts) {
        return false;
    }

    public Iterator<V> iterator() {
        return entries.values().iterator();
    }

    public ImmutableSet<K> keys() {
        return ImmutableSet.copyOf(entries.keySet());
    }

    public ImmutableSet<V> values() {
        return ImmutableSet.copyOf(entries.values());
    }
}
