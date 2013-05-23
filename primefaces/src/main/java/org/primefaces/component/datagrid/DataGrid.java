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
package org.primefaces.component.datagrid;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import org.primefaces.component.api.UIData;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class DataGrid extends UIData implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        columns,
        style,
        styleClass,
        emptyMessage;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.DataGrid";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.DataGridRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String DATAGRID_CLASS = "ui-datagrid ui-widget";

    public static final String CONTENT_CLASS = "ui-datagrid-content ui-widget-content";

    public static final String EMPTY_CONTENT_CLASS = "ui-datagrid-content  ui-datagrid-content-empty ui-widget-content";
    public static final String TABLE_CLASS = "ui-datagrid-data";

    public static final String TABLE_ROW_CLASS = "ui-datagrid-row";
    public static final String TABLE_COLUMN_CLASS = "ui-datagrid-column";

    public static final String HEADER_CLASS = "ui-datagrid-header ui-widget-header ui-corner-top";
    public static final String FOOTER_CLASS = "ui-datagrid-footer ui-widget-header ui-corner-bottom";

    public DataGrid() {
        setRendererType(DataGrid.DEFAULT_RENDERER);
    }

    public int getColumns() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.columns, 3);
    }

    public java.lang.String getEmptyMessage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.emptyMessage, "No records found.");
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return DataGrid.COMPONENT_FAMILY;
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(DataGrid.OPTIMIZED_PACKAGE)) {
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

    public void loadLazyData() {
        final DataModel model = getDataModel();

        if (model != null && model instanceof LazyDataModel) {
            final LazyDataModel lazyModel = (LazyDataModel) model;

            final List<?> data = lazyModel.load(getFirst(), getRows(), null, null, null);

            lazyModel.setPageSize(getRows());
            lazyModel.setWrappedData(data);

            // Update paginator for callback
            if (isPaginator()) {
                final RequestContext requestContext = RequestContext.getCurrentInstance();

                if (requestContext != null) {
                    requestContext.addCallbackParam("totalRecords", lazyModel.getRowCount());
                }
            }
        }
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setColumns(final int _columns) {
        getStateHelper().put(PropertyKeys.columns, _columns);
        handleAttribute("columns", _columns);
    }

    public void setEmptyMessage(final java.lang.String _emptyMessage) {
        getStateHelper().put(PropertyKeys.emptyMessage, _emptyMessage);
        handleAttribute("emptyMessage", _emptyMessage);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}