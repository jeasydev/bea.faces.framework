/*
 * Copyright 2011-2012 PrimeFaces Extensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */

package org.primefaces.extensions.renderkit.widget;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.util.ComponentUtils;

/**
 * Utilities for rendering {@link Widget}s.
 * 
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class WidgetRenderer {

    private static final Map<String, AdvancedOptionContainer[]> PROPERTY_KEYS_CACHE = new ConcurrentHashMap<String, AdvancedOptionContainer[]>();

    private static AdvancedOptionContainer[] getOptionsFromPropertyKeys(final Class<?> widgetClass) {
        AdvancedOptionContainer[] options;

        // try from cache first
        if (WidgetRenderer.PROPERTY_KEYS_CACHE.containsKey(widgetClass.getName())) {
            options = WidgetRenderer.PROPERTY_KEYS_CACHE.get(widgetClass.getName());
        } else {
            synchronized (WidgetRenderer.PROPERTY_KEYS_CACHE) {
                if (WidgetRenderer.PROPERTY_KEYS_CACHE.containsKey(widgetClass.getName())) {
                    options = WidgetRenderer.PROPERTY_KEYS_CACHE.get(widgetClass.getName());
                } else {
                    Class<?> propertyKeysClass = null;

                    // find child class named "PropertyKeys"
                    final Class<?>[] childClasses = widgetClass.getDeclaredClasses();
                    for (final Class<?> childClass : childClasses) {
                        if (childClass.getSimpleName().equals("PropertyKeys")) {
                            propertyKeysClass = childClass;
                        }
                    }

                    if (propertyKeysClass == null) {
                        throw new FacesException("Widget '" + widgetClass.getName()
                            + "' does not has PropertyKeys class.");
                    }

                    final Enum<?>[] propertyKeys = (Enum<?>[]) propertyKeysClass.getEnumConstants();

                    final ArrayList<OptionContainer> optionContainerList = new ArrayList<OptionContainer>();

                    for (final Enum<?> propertyKey : propertyKeys) {
                        try {
                            final Field propertyKeyField = propertyKeysClass.getDeclaredField(propertyKey.name());

                            if (propertyKeyField.isAnnotationPresent(Option.class)) {
                                final AdvancedOptionContainer optionContainer = new AdvancedOptionContainer();
                                final Option option = propertyKeyField.getAnnotation(Option.class);
                                final String propertyKeyAsString = propertyKey.toString();
                                final String name = option.name().equals("") ? propertyKeyAsString : option.name();

                                optionContainer.setEscapeHTML(option.escapeHTML());
                                optionContainer.setEscapeText(option.escapeText());
                                optionContainer.setName(name);
                                optionContainer.setUseDoubleQuotes(option.useDoubleQuotes());
                                optionContainer.setPropertyName(propertyKeyAsString);
                                optionContainer.setReadMethod(new PropertyDescriptor(propertyKeyAsString, widgetClass)
                                    .getReadMethod());

                                optionContainerList.add(optionContainer);
                            }
                        } catch (final Exception e) {
                            throw new FacesException("Can not get property '" + propertyKey.name() + "' in '"
                                + widgetClass.getName() + "'", e);
                        }
                    }

                    // add to cache
                    WidgetRenderer.PROPERTY_KEYS_CACHE.put(widgetClass.getName(), optionContainerList
                        .toArray(new AdvancedOptionContainer[0]));

                    options = WidgetRenderer.PROPERTY_KEYS_CACHE.get(widgetClass.getName());
                }
            }
        }

        return options;
    }

    private static Object getPropertValue(final AdvancedOptionContainer optionContainer, final Widget widget) {
        try {
            return optionContainer.getReadMethod().invoke(widget);
        } catch (final Exception e) {
            throw new FacesException("Can not get value for property '" + optionContainer.getPropertyName()
                + "' and widget '" + widget.getClass().getName() + "'", e);
        }
    }

    public static void renderOption(final ResponseWriter writer,
                                    final OptionContainer optionContainer,
                                    final Object value) throws IOException {

        if (value != null) {
            if (String.class.isAssignableFrom(value.getClass()) || Character.class.isAssignableFrom(value.getClass())) {

                String stringValue = ((String) value).trim();

                if (optionContainer.isEscapeText()) {
                    stringValue = ComponentUtils.escapeText(stringValue);
                }

                // don't add quotes for objects or arrays
                if (stringValue.charAt(0) == '{' || stringValue.charAt(0) == '[') {
                    if (optionContainer.isEscapeHTML()) {
                        writer.writeText("," + optionContainer.getName() + ":" + stringValue, null);
                    } else {
                        writer.write("," + optionContainer.getName() + ":" + stringValue);
                    }
                } else {
                    if (optionContainer.isUseDoubleQuotes()) {
                        if (optionContainer.isEscapeHTML()) {
                            writer.writeText("," + optionContainer.getName() + ":\"" + stringValue + "\"", null);
                        } else {
                            writer.write("," + optionContainer.getName() + ":\"" + stringValue + "\"");
                        }
                    } else {
                        if (optionContainer.isEscapeHTML()) {
                            writer.writeText("," + optionContainer.getName() + ":'" + stringValue + "'", null);
                        } else {
                            writer.write("," + optionContainer.getName() + ":'" + stringValue + "'");
                        }
                    }
                }
            } else {
                if (optionContainer.isEscapeHTML()) {
                    writer.writeText("," + optionContainer.getName() + ":" + value, null);
                } else {
                    writer.write("," + optionContainer.getName() + ":" + value);
                }
            }
        }
    }

    public static final void renderOptions(final String clientId, final ResponseWriter writer, final Widget widget)
        throws IOException {

        final Class<?> widgetClass = widget.getClass();

        writer.write("id:'" + clientId + "'");

        final AdvancedOptionContainer[] optionContainers = WidgetRenderer.getOptionsFromPropertyKeys(widgetClass);
        for (final AdvancedOptionContainer optionContainer : optionContainers) {
            final Object propertyValue = WidgetRenderer.getPropertValue(optionContainer, widget);

            WidgetRenderer.renderOption(writer, optionContainer, propertyValue);
        }
    }

    public static void renderWidgetScript(final FacesContext context,
                                          final String clientId,
                                          final ResponseWriter writer,
                                          final Widget widget,
                                          final boolean hasStyleSheet) throws IOException {
        WidgetRenderer.renderWidgetScript(context, clientId, writer, widget, hasStyleSheet, null);
    }

    public static void renderWidgetScript(final FacesContext context,
                                          final String clientId,
                                          final ResponseWriter writer,
                                          final Widget widget,
                                          final boolean hasStyleSheet,
                                          final Map<OptionContainer, Object> additionalOptions) throws IOException {

        final Class<?> widgetClass = widget.getClass();
        final String widgetName = widgetClass.getSimpleName();
        final String widgetVar = widget.resolveWidgetVar();

        writer.write("$(function() {");
        writer.write("PrimeFacesExt.cw('" + widgetName + "', '" + widgetVar + "', {");

        // render mapped options
        WidgetRenderer.renderOptions(clientId, writer, widget);

        // render additional options
        if (additionalOptions != null) {
            for (final Entry<OptionContainer, Object> entry : additionalOptions.entrySet()) {
                WidgetRenderer.renderOption(writer, entry.getKey(), entry.getValue());
            }
        }

        writer.write("}, " + hasStyleSheet + ");});");
    }
}
