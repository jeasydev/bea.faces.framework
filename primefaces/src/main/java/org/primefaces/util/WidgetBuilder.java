/*
 * Copyright 2009-2012 Prime Teknoloji.
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
 */
package org.primefaces.util;

/**
 * Helper to generate javascript code of an ajax call
 */
public class WidgetBuilder {

    protected StringBuilder buffer;

    protected boolean onload = false;

    protected String resourcePath = null;

    public WidgetBuilder() {
        buffer = new StringBuilder();
    }

    public WidgetBuilder append(final Number number) {
        buffer.append(number);

        return this;
    }

    public WidgetBuilder append(final String str) {
        buffer.append(str);

        return this;
    }

    public WidgetBuilder attr(final String name, final boolean value) {
        buffer.append(",").append(name).append(":").append(value);

        return this;
    }

    public WidgetBuilder attr(final String name, final boolean value, final boolean defaultValue) {
        if (value != defaultValue) {
            buffer.append(",").append(name).append(":").append(value);
        }

        return this;
    }

    public WidgetBuilder attr(final String name, final double value, final double defaultValue) {
        if (value != defaultValue) {
            buffer.append(",").append(name).append(":").append(value);
        }

        return this;
    }

    public WidgetBuilder attr(final String name, final int value, final int defaultValue) {
        if (value != defaultValue) {
            buffer.append(",").append(name).append(":").append(value);
        }

        return this;
    }

    public WidgetBuilder attr(final String name, final Number value) {
        buffer.append(",").append(name).append(":").append(value);

        return this;
    }

    public WidgetBuilder attr(final String name, final String value) {
        buffer.append(",").append(name).append(":'").append(value).append("'");

        return this;
    }

    public WidgetBuilder attr(final String name, final String value, final String defaultValue) {
        if (value != null && !value.equals(defaultValue)) {
            buffer.append(",").append(name).append(":'").append(value).append("'");
        }

        return this;
    }

    public String build() {
        buffer.append("}");

        if (resourcePath != null) {
            buffer.append(",'").append(resourcePath).append("'");
        }

        buffer.append(");");

        if (onload) {
            buffer.append("});");
        }

        final String script = buffer.toString();

        reset();

        return script;
    }

    public WidgetBuilder callback(final String name, final String signature, final String callback) {
        if (callback != null) {
            buffer.append(",").append(name).append(":").append(signature).append("{").append(callback).append("}");
        }

        return this;
    }

    public WidgetBuilder nativeAttr(final String name, final String value) {
        buffer.append(",").append(name).append(":").append(value);

        return this;
    }

    public void reset() {
        buffer.setLength(0);
        onload = false;
        resourcePath = null;
    }

    /**
     * 
     * @param widgetClass Constructor name of the widget
     * @param widgetVar Name of the client side widget
     * @param id Client id of the component
     * @param onload Flag to define if widget should be created on document load
     */
    public WidgetBuilder widget(final String widgetClass, final String widgetVar, final String id, final boolean onload) {
        this.onload = onload;
        if (this.onload) {
            buffer.append("$(function(){");
        }

        buffer.append("PrimeFaces.cw('").append(widgetClass).append("','").append(widgetVar).append("',{");
        buffer.append("id:'").append(id).append("'");

        return this;
    }

    /**
     * 
     * @param widgetClass Constructor name of the widget
     * @param widgetVar Name of the client side widget
     * @param id Client id of the component
     * @param resourcePath Path for dynamic resource loading
     * @param onload Flag to define if widget should be created on document load
     */
    public WidgetBuilder widget(final String widgetClass,
                                final String widgetVar,
                                final String id,
                                final String resourcePath,
                                final boolean onload) {
        this.widget(widgetClass, widgetVar, id, onload);
        this.resourcePath = resourcePath;

        return this;
    }
}
