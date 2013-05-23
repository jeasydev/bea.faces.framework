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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.omnifaces.el.functions.Strings;

/**
 * A simple JSON encoder.
 * 
 * @author Bauke Scholtz
 * @since 1.2
 */
public final class Json {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    private static final String ERROR_INVALID_BEAN = "Cannot introspect object of type '%s' as bean.";
    private static final String ERROR_INVALID_GETTER = "Cannot invoke getter of property '%s' of bean '%s'.";

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Encodes the given object as JSON. This supports the standard types
     * {@link Boolean}, {@link Number}, {@link CharSequence} and {@link Date}.
     * If the given object type does not match any of them, then it will attempt
     * to inspect the object as a javabean whereby the public properties (with
     * public getters) will be encoded as a JS object. It also supports
     * {@link Collection}s, {@link Map}s and arrays of them, even nested ones.
     * The {@link Date} is formatted in RFC 1123 format, so you can if necessary
     * just pass it straight to <code>new Date()</code> in JavaScript.
     * 
     * @param object The object to be encoded as JSON.
     * @return The JSON-encoded representation of the given object.
     * @throws IllegalArgumentException When the given object or one of its
     *             properties cannot be inspected as a bean.
     */
    public static String encode(final Object object) {
        final StringBuilder builder = new StringBuilder();
        Json.encode(object, builder);
        return builder.toString();
    }

    // Encode
    // ---------------------------------------------------------------------------------------------------------

    /**
     * Method allowing tail recursion (prevents potential stack overflow on
     * deeply nested structures).
     */
    private static void encode(final Object object, final StringBuilder builder) {
        if (object == null) {
            builder.append("null");
        } else if (object instanceof Boolean || object instanceof Number) {
            builder.append(object.toString());
        } else if (object instanceof CharSequence) {
            builder.append('"').append(Strings.escapeJS(object.toString())).append('"');
        } else if (object instanceof Date) {
            builder.append('"').append(Utils.formatRFC1123((Date) object)).append('"');
        } else if (object instanceof Collection<?>) {
            Json.encodeCollection((Collection<?>) object, builder);
        } else if (object.getClass().isArray()) {
            Json.encodeArray(object, builder);
        } else if (object instanceof Map<?, ?>) {
            Json.encodeMap((Map<?, ?>) object, builder);
        } else {
            Json.encodeBean(object, builder);
        }
    }

    /**
     * Encode a Java array as JS array.
     */
    private static void encodeArray(final Object array, final StringBuilder builder) {
        builder.append('[');
        final int length = Array.getLength(array);

        for (int i = 0; i < length; i++) {
            if (i > 0) {
                builder.append(',');
            }

            Json.encode(Array.get(array, i), builder);
        }

        builder.append(']');
    }

    /**
     * Encode a Java bean as JS object.
     */
    private static void encodeBean(final Object bean, final StringBuilder builder) {
        BeanInfo beanInfo;

        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException(String.format(Json.ERROR_INVALID_BEAN, bean.getClass()), e);
        }

        builder.append('{');
        int i = 0;

        for (final PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
            if (property.getReadMethod() == null || "class".equals(property.getName())) {
                continue;
            }

            Object value;

            try {
                value = property.getReadMethod().invoke(bean);
            } catch (final Exception e) {
                throw new IllegalArgumentException(String.format(Json.ERROR_INVALID_GETTER, property.getName(), bean
                    .getClass()), e);
            }

            if (value == null) {
                continue;
            }

            if (i++ > 0) {
                builder.append(',');
            }

            Json.encode(property.getName(), builder);
            builder.append(':');
            Json.encode(value, builder);
        }

        builder.append('}');
    }

    /**
     * Encode a Java collection as JS array.
     */
    private static void encodeCollection(final Collection<?> collection, final StringBuilder builder) {
        builder.append('[');
        int i = 0;

        for (final Object element : collection) {
            if (i++ > 0) {
                builder.append(',');
            }

            Json.encode(element, builder);
        }

        builder.append(']');
    }

    /**
     * Encode a Java map as JS object.
     */
    private static void encodeMap(final Map<?, ?> map, final StringBuilder builder) {
        builder.append('{');
        int i = 0;

        for (final Entry<?, ?> entry : map.entrySet()) {
            if (i++ > 0) {
                builder.append(',');
            }

            Json.encode(String.valueOf(entry.getKey()), builder);
            builder.append(':');
            Json.encode(entry.getValue(), builder);
        }

        builder.append('}');
    }

    private Json() {
        // Hide constructor.
    }

}