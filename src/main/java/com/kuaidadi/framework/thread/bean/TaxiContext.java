/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.kuaidadi.framework.thread.bean;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * The context is a key-value store used to pass configuration information
 * throughout the system.
 */
public class TaxiContext {

    private Map<String, String> parameters;

    public TaxiContext() {
        parameters = new HashMap<String, String>();
    }

    public TaxiContext(Map<String, String> paramters) {
        this();
        this.putAll(paramters);
    }

    /**
     * Gets a copy of the backing map structure.
     * 
     * @return immutable copy of backing map structure
     */
    public ImmutableMap<String, String> getParameters() {
        synchronized (parameters) {
            return ImmutableMap.copyOf(parameters);
        }
    }

    /**
     * Removes all of the mappings from this map.
     */
    public void clear() {
        parameters.clear();
    }

    /**
     * Associates all of the given map's keys and values in the Context.
     */
    public void putAll(Map<String, String> map) {
        parameters.putAll(map);
    }

    /**
     * Associates the specified value with the specified key in this context. If
     * the context previously contained a mapping for the key, the old value is
     * replaced by the specified value.
     * 
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            to be associated with the specified key
     */
    public void put(String key, String value) {
        parameters.put(key, value);
    }

    /**
     * Gets value mapped to key, returning defaultValue if unmapped.
     * 
     * @param key
     *            to be found
     * @param defaultValue
     *            returned if key is unmapped
     * @return value associated with key
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = get(key);
        if (value != null) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }

    /**
     * Gets value mapped to key, returning null if unmapped.
     * 
     * @param key
     *            to be found
     * @return value associated with key or null if unmapped
     */
    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    /**
     * Gets value mapped to key, returning defaultValue if unmapped.
     * 
     * @param key
     *            to be found
     * @param defaultValue
     *            returned if key is unmapped
     * @return value associated with key
     */
    public Integer getInteger(String key, Integer defaultValue) {
        String value = get(key);
        if (value != null) {
            return Integer.parseInt(value.trim());
        }
        return defaultValue;
    }

    /**
     * Gets value mapped to key, returning null if unmapped.
     * 
     * @param key
     *            to be found
     * @return value associated with key or null if unmapped
     */
    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    /**
     * Gets value mapped to key, returning defaultValue if unmapped.
     * 
     * @param key
     *            to be found
     * @param defaultValue
     *            returned if key is unmapped
     * @return value associated with key
     */
    public Long getLong(String key, Long defaultValue) {
        String value = get(key);
        if (value != null) {
            return Long.parseLong(value.trim());
        }
        return defaultValue;
    }

    /**
     * Gets value mapped to key, returning null if unmapped.
     * 
     * @param key
     *            to be found
     * @return value associated with key or null if unmapped
     */
    public Long getLong(String key) {
        return getLong(key, null);
    }

    /**
     * Gets value mapped to key, returning defaultValue if unmapped.
     * 
     * @param key
     *            to be found
     * @param defaultValue
     *            returned if key is unmapped
     * @return value associated with key
     */
    public String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }

    /**
     * Gets value mapped to key, returning null if unmapped.
     * 
     * @param key
     *            to be found
     * @return value associated with key or null if unmapped
     */
    public String getString(String key) {
        return get(key);
    }

    private String get(String key, String defaultValue) {
        String result = parameters.get(key);
        if (result != null) {
            return result;
        }
        return defaultValue;
    }

    private String get(String key) {
        return get(key, null);
    }

    @Override
    public String toString() {
        return "{ parameters:" + parameters + " }";
    }
}
