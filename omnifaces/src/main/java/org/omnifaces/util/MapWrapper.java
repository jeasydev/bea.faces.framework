/*
 * Copyright 2012 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Map that wraps another map. This allows interception of one
 * or more method on this wrapped map.
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * 
 * @author Arjan Tijms
 */
public class MapWrapper<K, V> implements Map<K, V> {

    private final Map<K, V> map;

    /**
     * Initializes the wrapper with its wrapped map.
     * 
     * @param map the map to wrap.
     */
    public MapWrapper(final Map<K, V> map) {
        this.map = map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        getWrapped().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final Object key) {
        return getWrapped().containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(final Object value) {
        return getWrapped().containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return getWrapped().entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        return getWrapped().equals(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(final Object key) {
        return getWrapped().get(key);
    }

    public Map<K, V> getWrapped() {
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getWrapped().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return getWrapped().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<K> keySet() {
        return getWrapped().keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(final K key, final V value) {
        return getWrapped().put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        getWrapped().putAll(m);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(final Object key) {
        return getWrapped().remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return getWrapped().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        return getWrapped().values();
    }

}