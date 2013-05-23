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
package org.primefaces.component.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.primefaces.component.datatable.DataTable;

public abstract class Exporter {

    protected enum ColumnType {
        HEADER("header"),
        FOOTER("footer");

        private final String facet;

        ColumnType(final String facet) {
            this.facet = facet;
        }

        public String facet() {
            return facet;
        }

        @Override
        public String toString() {
            return facet;
        }
    };

    public abstract void export(FacesContext facesContext,
                                DataTable table,
                                String outputFileName,
                                boolean pageOnly,
                                boolean selectionOnly,
                                String encodingType,
                                MethodExpression preProcessor,
                                MethodExpression postProcessor) throws IOException;

    protected String exportValue(final FacesContext context, final UIComponent component) {

        if (component instanceof HtmlCommandLink) { // support for PrimeFaces
                                                    // and standard
                                                    // HtmlCommandLink
            final HtmlCommandLink link = (HtmlCommandLink) component;
            final Object value = link.getValue();

            if (value != null) {
                return String.valueOf(value);
            } else {
                // export first value holder
                for (final UIComponent child : link.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return "";
            }
        } else if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                final Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            final ValueHolder valueHolder = (ValueHolder) component;
            final Object value = valueHolder.getValue();
            if (value == null) return "";

            // first ask the converter
            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(context, component, value);
            }
            // Try to guess
            else {
                final ValueExpression expr = component.getValueExpression("value");
                if (expr != null) {
                    final Class<?> valueType = expr.getType(context.getELContext());
                    if (valueType != null) {
                        final Converter converterForType = context.getApplication().createConverter(valueType);

                        if (converterForType != null) return converterForType.getAsString(context, component, value);
                    }
                }
            }

            // No converter found just return the value as string
            return value.toString();
        } else {
            // This would get the plain texts on UIInstructions when using
            // Facelets
            final String value = component.toString();

            if (value != null)
                return value.trim();
            else return "";
        }
    }

    protected List<UIColumn> getColumnsToExport(final UIData table) {
        final List<UIColumn> columns = new ArrayList<UIColumn>();
        for (final UIComponent child : table.getChildren()) {
            if (child instanceof UIColumn) {
                final UIColumn column = (UIColumn) child;
                columns.add(column);
            }
        }

        return columns;
    }

    protected boolean hasColumnFooter(final List<UIColumn> columns) {
        for (final UIColumn column : columns) {
            if (column.getFooter() != null) return true;
        }

        return false;
    }
}
