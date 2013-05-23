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
package org.primefaces.component.scrollpanel;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class ScrollPanel extends UIPanel implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        style,
        styleClass,
        mode;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.ScrollPanel";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.ScrollPanelRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String SCROLL_PANEL_CLASS = "ui-scrollpanel ui-widget ui-widget-content ui-corner-all";

    public static final String SCROLL_PANEL_NATIVE_CLASS = "ui-scrollpanel ui-scrollpanel-native ui-widget ui-widget-content ui-corner-all";

    public static final String SCROLL_PANEL_CONTAINER_CLASS = "ui-scrollpanel-container";
    public static final String SCROLL_PANEL_WRAPPER_CLASS = "ui-scrollpanel-wrapper";

    public static final String SCROLL_PANEL_CONTENT_CLASS = "ui-scrollpanel-content";
    public static final String SCROLL_PANEL_HBAR_CLASS = "ui-scrollpanel-hbar ui-widget-header ui-corner-bottom";

    public static final String SCROLL_PANEL_VBAR_CLASS = "ui-scrollpanel-vbar ui-widget-header ui-corner-right";
    public static final String SCROLL_PANEL_HANDLE_CLASS = "ui-scrollpanel-handle ui-state-default ui-corner-all";

    public static final String SCROLL_PANEL_BLEFT_CLASS = "ui-scrollpanel-bl ui-state-default ui-corner-bl";
    public static final String SCROLL_PANEL_BRIGHT_CLASS = "ui-scrollpanel-br ui-state-default ui-corner-br";

    public static final String SCROLL_PANEL_BTOP_CLASS = "ui-scrollpanel-bt ui-state-default ui-corner-tr";
    public static final String SCROLL_PANEL_BBOTTOM_CLASS = "ui-scrollpanel-bb ui-state-default ui-corner-br";
    public static final String SCROLL_PANEL_VGRIP_CLASS = "ui-icon ui-icon-grip-solid-vertical";
    public static final String SCROLL_PANEL_HGRIP_CLASS = "ui-icon ui-icon-grip-solid-horizontal";
    public static final String SCROLL_PANEL_IWEST_CLASS = "ui-icon ui-icon-triangle-1-w";
    public static final String SCROLL_PANEL_IEAST_CLASS = "ui-icon ui-icon-triangle-1-e";
    public static final String SCROLL_PANEL_INORTH_CLASS = "ui-icon ui-icon-triangle-1-n";
    public static final String SCROLL_PANEL_ISOUTH_CLASS = "ui-icon ui-icon-triangle-1-s";

    public ScrollPanel() {
        setRendererType(ScrollPanel.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return ScrollPanel.COMPONENT_FAMILY;
    }

    public java.lang.String getMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.mode, "default");
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
            if (cname != null && cname.startsWith(ScrollPanel.OPTIMIZED_PACKAGE)) {
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

    public void setMode(final java.lang.String _mode) {
        getStateHelper().put(PropertyKeys.mode, _mode);
        handleAttribute("mode", _mode);
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