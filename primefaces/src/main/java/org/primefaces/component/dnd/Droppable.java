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
package org.primefaces.component.dnd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Droppable extends UIComponentBase implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        forValue("for"),
        disabled,
        hoverStyleClass,
        activeStyleClass,
        onDrop,
        accept,
        scope,
        tolerance,
        datasource;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Droppable";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.DroppableRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private final static String DEFAULT_EVENT = "drop";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList(Droppable.DEFAULT_EVENT));

    public Droppable() {
        setRendererType(Droppable.DEFAULT_RENDERER);
    }

    protected UIData findDatasource(final FacesContext context, final Droppable droppable, final String datasourceId) {
        final UIComponent datasource = droppable.findComponent(datasourceId);

        if (datasource == null)
            throw new FacesException("Cannot find component \"" + datasourceId + "\" in view.");
        else return (UIData) datasource;
    }

    public java.lang.String getAccept() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.accept, null);
    }

    public java.lang.String getActiveStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.activeStyleClass, null);
    }

    public java.lang.String getDatasource() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.datasource, null);
    }

    @Override
    public String getDefaultEventName() {
        return Droppable.DEFAULT_EVENT;
    }

    @Override
    public Collection<String> getEventNames() {
        return Droppable.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Droppable.COMPONENT_FAMILY;
    }

    public java.lang.String getFor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public java.lang.String getHoverStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.hoverStyleClass, null);
    }

    public java.lang.String getOnDrop() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onDrop, null);
    }

    public java.lang.String getScope() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.scope, null);
    }

    public java.lang.String getTolerance() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.tolerance, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Droppable.OPTIMIZED_PACKAGE)) {
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

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    private boolean isRequestSource(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (isRequestSource(context)) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = getClientId(context);

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("drop")) {
                final String dragId = params.get(clientId + "_dragId");
                final String dropId = params.get(clientId + "_dropId");
                DragDropEvent dndEvent = null;
                final String datasourceId = getDatasource();

                if (datasourceId != null) {
                    final UIData datasource = findDatasource(context, this, datasourceId);
                    final String[] idTokens = dragId.split(String.valueOf(UINamingContainer.getSeparatorChar(context)));
                    final int rowIndex = Integer.parseInt(idTokens[idTokens.length - 2]);
                    datasource.setRowIndex(rowIndex);
                    final Object data = datasource.getRowData();
                    datasource.setRowIndex(-1);

                    dndEvent = new DragDropEvent(this, behaviorEvent.getBehavior(), dragId, dropId, data);
                } else {
                    dndEvent = new DragDropEvent(this, behaviorEvent.getBehavior(), dragId, dropId);
                }

                super.queueEvent(dndEvent);
            }

        } else {
            super.queueEvent(event);
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

    public void setAccept(final java.lang.String _accept) {
        getStateHelper().put(PropertyKeys.accept, _accept);
        handleAttribute("accept", _accept);
    }

    public void setActiveStyleClass(final java.lang.String _activeStyleClass) {
        getStateHelper().put(PropertyKeys.activeStyleClass, _activeStyleClass);
        handleAttribute("activeStyleClass", _activeStyleClass);
    }

    public void setDatasource(final java.lang.String _datasource) {
        getStateHelper().put(PropertyKeys.datasource, _datasource);
        handleAttribute("datasource", _datasource);
    }

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setFor(final java.lang.String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
        handleAttribute("forValue", _for);
    }

    public void setHoverStyleClass(final java.lang.String _hoverStyleClass) {
        getStateHelper().put(PropertyKeys.hoverStyleClass, _hoverStyleClass);
        handleAttribute("hoverStyleClass", _hoverStyleClass);
    }

    public void setOnDrop(final java.lang.String _onDrop) {
        getStateHelper().put(PropertyKeys.onDrop, _onDrop);
        handleAttribute("onDrop", _onDrop);
    }

    public void setScope(final java.lang.String _scope) {
        getStateHelper().put(PropertyKeys.scope, _scope);
        handleAttribute("scope", _scope);
    }

    public void setTolerance(final java.lang.String _tolerance) {
        getStateHelper().put(PropertyKeys.tolerance, _tolerance);
        handleAttribute("tolerance", _tolerance);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}