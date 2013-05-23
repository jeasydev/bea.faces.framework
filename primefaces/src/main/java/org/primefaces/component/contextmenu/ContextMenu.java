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
package org.primefaces.component.contextmenu;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.menu.AbstractMenu;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class ContextMenu extends AbstractMenu implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        forValue("for"),
        style,
        styleClass,
        model,
        nodeType,
        event,
        beforeShow;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.ContextMenu";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.ContextMenuRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String CONTAINER_CLASS = "ui-menu ui-menu-dynamic ui-contextmenu ui-widget ui-widget-content ui-corner-all ui-helper-clearfix ui-shadow";

    public ContextMenu() {
        setRendererType(ContextMenu.DEFAULT_RENDERER);
    }

    public java.lang.String getBeforeShow() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.beforeShow, null);
    }

    public java.lang.String getEvent() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.event, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return ContextMenu.COMPONENT_FAMILY;
    }

    public java.lang.String getFor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    @Override
    public org.primefaces.model.MenuModel getModel() {
        return (org.primefaces.model.MenuModel) getStateHelper().eval(PropertyKeys.model, null);
    }

    public java.lang.String getNodeType() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.nodeType, null);
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
            if (cname != null && cname.startsWith(ContextMenu.OPTIMIZED_PACKAGE)) {
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
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setBeforeShow(final java.lang.String _beforeShow) {
        getStateHelper().put(PropertyKeys.beforeShow, _beforeShow);
        handleAttribute("beforeShow", _beforeShow);
    }

    public void setEvent(final java.lang.String _event) {
        getStateHelper().put(PropertyKeys.event, _event);
        handleAttribute("event", _event);
    }

    public void setFor(final java.lang.String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
        handleAttribute("forValue", _for);
    }

    public void setModel(final org.primefaces.model.MenuModel _model) {
        getStateHelper().put(PropertyKeys.model, _model);
        handleAttribute("model", _model);
    }

    public void setNodeType(final java.lang.String _nodeType) {
        getStateHelper().put(PropertyKeys.nodeType, _nodeType);
        handleAttribute("nodeType", _nodeType);
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