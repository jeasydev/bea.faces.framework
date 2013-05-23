/*
 * Generated, Do Not Modify
 */
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
package org.primefaces.component.columns;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.UIData;
import org.primefaces.component.celleditor.CellEditor;

@ResourceDependencies({

})
public class Columns extends UIData implements org.primefaces.component.api.UIColumn {

    protected enum PropertyKeys {

        sortBy,
        style,
        styleClass,
        sortFunction,
        filterBy,
        filterStyle,
        filterStyleClass,
        filterOptions,
        filterMatchMode,
        filterPosition,
        rowspan,
        colspan,
        headerText,
        footerText,
        filterMaxLength,
        resizable,
        exportable,
        width;

        String toString;

        PropertyKeys() {
        }

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    public static final String COMPONENT_TYPE = "org.primefaces.component.Columns";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public Columns() {
        setRendererType(null);
    }

    @Override
    public CellEditor getCellEditor() {
        return null;
    }

    @Override
    public int getColspan() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.colspan, 1);
    }

    public java.lang.String getColumnIndexVar() {
        return super.getRowIndexVar();
    }

    @Override
    public String getColumnKey() {
        return this.getClientId();
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Columns.COMPONENT_FAMILY;
    }

    public java.lang.Object getFilterBy() {
        return getStateHelper().eval(PropertyKeys.filterBy, false);
    }

    @Override
    public java.lang.String getFilterMatchMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterMatchMode, "startsWith");
    }

    @Override
    public int getFilterMaxLength() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.filterMaxLength, java.lang.Integer.MAX_VALUE);
    }

    @Override
    public java.lang.Object getFilterOptions() {
        return getStateHelper().eval(PropertyKeys.filterOptions, null);
    }

    @Override
    public java.lang.String getFilterPosition() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterPosition, "bottom");
    }

    @Override
    public java.lang.String getFilterStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterStyle, null);
    }

    @Override
    public java.lang.String getFilterStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterStyleClass, null);
    }

    @Override
    public java.lang.String getFooterText() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.footerText, null);
    }

    @Override
    public java.lang.String getHeaderText() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.headerText, null);
    }

    @Override
    public int getRowspan() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.rowspan, 1);
    }

    @Override
    public String getSelectionMode() {
        return null;
    }

    public java.lang.Object getSortBy() {
        return getStateHelper().eval(PropertyKeys.sortBy, null);
    }

    @Override
    public javax.el.MethodExpression getSortFunction() {
        return (javax.el.MethodExpression) getStateHelper().eval(PropertyKeys.sortFunction, null);
    }

    @Override
    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    @Override
    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    @Override
    public java.lang.String getWidth() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.width, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Columns.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                final ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                } else if (!setAttributes.contains(name)) {
                    setAttributes.add(name);
                }
            }
        }
    }

    @Override
    public boolean isDisabledSelection() {
        return false;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean isExportable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.exportable, true);
    }

    @Override
    public boolean isResizable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resizable, true);
    }

    public void setColspan(final int _colspan) {
        getStateHelper().put(PropertyKeys.colspan, _colspan);
        handleAttribute("colspan", _colspan);
    }

    public void setColumnIndexVar(final String _columnIndexVar) {
        super.setRowIndexVar(_columnIndexVar);
    }

    public void setExportable(final boolean _exportable) {
        getStateHelper().put(PropertyKeys.exportable, _exportable);
        handleAttribute("exportable", _exportable);
    }

    public void setFilterBy(final java.lang.Object _filterBy) {
        getStateHelper().put(PropertyKeys.filterBy, _filterBy);
        handleAttribute("filterBy", _filterBy);
    }

    public void setFilterMatchMode(final java.lang.String _filterMatchMode) {
        getStateHelper().put(PropertyKeys.filterMatchMode, _filterMatchMode);
        handleAttribute("filterMatchMode", _filterMatchMode);
    }

    public void setFilterMaxLength(final int _filterMaxLength) {
        getStateHelper().put(PropertyKeys.filterMaxLength, _filterMaxLength);
        handleAttribute("filterMaxLength", _filterMaxLength);
    }

    public void setFilterOptions(final java.lang.Object _filterOptions) {
        getStateHelper().put(PropertyKeys.filterOptions, _filterOptions);
        handleAttribute("filterOptions", _filterOptions);
    }

    public void setFilterPosition(final java.lang.String _filterPosition) {
        getStateHelper().put(PropertyKeys.filterPosition, _filterPosition);
        handleAttribute("filterPosition", _filterPosition);
    }

    public void setFilterStyle(final java.lang.String _filterStyle) {
        getStateHelper().put(PropertyKeys.filterStyle, _filterStyle);
        handleAttribute("filterStyle", _filterStyle);
    }

    public void setFilterStyleClass(final java.lang.String _filterStyleClass) {
        getStateHelper().put(PropertyKeys.filterStyleClass, _filterStyleClass);
        handleAttribute("filterStyleClass", _filterStyleClass);
    }

    public void setFooterText(final java.lang.String _footerText) {
        getStateHelper().put(PropertyKeys.footerText, _footerText);
        handleAttribute("footerText", _footerText);
    }

    public void setHeaderText(final java.lang.String _headerText) {
        getStateHelper().put(PropertyKeys.headerText, _headerText);
        handleAttribute("headerText", _headerText);
    }

    public void setResizable(final boolean _resizable) {
        getStateHelper().put(PropertyKeys.resizable, _resizable);
        handleAttribute("resizable", _resizable);
    }

    public void setRowspan(final int _rowspan) {
        getStateHelper().put(PropertyKeys.rowspan, _rowspan);
        handleAttribute("rowspan", _rowspan);
    }

    public void setSortBy(final java.lang.Object _sortBy) {
        getStateHelper().put(PropertyKeys.sortBy, _sortBy);
        handleAttribute("sortBy", _sortBy);
    }

    public void setSortFunction(final javax.el.MethodExpression _sortFunction) {
        getStateHelper().put(PropertyKeys.sortFunction, _sortFunction);
        handleAttribute("sortFunction", _sortFunction);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setWidth(final java.lang.String _width) {
        getStateHelper().put(PropertyKeys.width, _width);
        handleAttribute("width", _width);
    }
}