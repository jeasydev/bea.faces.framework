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
package org.primefaces.component.panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.menu.Menu;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Panel extends UIPanel implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        header,
        footer,
        toggleable,
        toggleSpeed,
        style,
        styleClass,
        collapsed,
        closable,
        closeSpeed,
        visible,
        closeTitle,
        toggleTitle,
        menuTitle,
        toggleOrientation;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Panel";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.PanelRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String PANEL_CLASS = "ui-panel ui-widget ui-widget-content ui-corner-all";

    public static final String PANEL_TITLEBAR_CLASS = "ui-panel-titlebar ui-widget-header ui-helper-clearfix ui-corner-all";

    public static final String PANEL_TITLE_CLASS = "ui-panel-title";
    public static final String PANEL_TITLE_ICON_CLASS = "ui-panel-titlebar-icon ui-corner-all ui-state-default";

    public static final String PANEL_CONTENT_CLASS = "ui-panel-content ui-widget-content";
    public static final String PANEL_FOOTER_CLASS = "ui-panel-footer ui-widget-content";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("toggle",
                                                                                                           "close"));
    private Menu optionsMenu;

    public Panel() {
        setRendererType(Panel.DEFAULT_RENDERER);
    }

    public int getCloseSpeed() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.closeSpeed, 500);
    }

    public java.lang.String getCloseTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.closeTitle, null);
    }

    @Override
    public Collection<String> getEventNames() {
        return Panel.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Panel.COMPONENT_FAMILY;
    }

    public java.lang.String getFooter() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.footer, null);
    }

    public java.lang.String getHeader() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.header, null);
    }

    public java.lang.String getMenuTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.menuTitle, null);
    }

    public Menu getOptionsMenu() {
        if (optionsMenu == null) {
            final UIComponent optionsFacet = getFacet("options");
            if (optionsFacet != null) {
                if (optionsFacet instanceof Menu)
                    optionsMenu = (Menu) optionsFacet;
                else optionsMenu = (Menu) optionsFacet.getChildren().get(0);
            }

        }

        return optionsMenu;
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getToggleOrientation() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.toggleOrientation, "vertical");
    }

    public int getToggleSpeed() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.toggleSpeed, 500);
    }

    public java.lang.String getToggleTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.toggleTitle, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Panel.OPTIMIZED_PACKAGE)) {
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

    public boolean isClosable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.closable, false);
    }

    public boolean isCollapsed() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.collapsed, false);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isToggleable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.toggleable, false);
    }

    public boolean isVisible() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.visible, true);
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (isSelfRequest(context)) {
            decode(context);
        } else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isSelfRequest(context)) {
            super.processUpdates(context);
        }

        final FacesContext facesContext = getFacesContext();
        final ELContext eLContext = facesContext.getELContext();

        final ValueExpression collapsedVE = getValueExpression("collapsed");
        if (collapsedVE != null) {
            collapsedVE.setValue(eLContext, isCollapsed());
            getStateHelper().put(Panel.PropertyKeys.collapsed, null);
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isSelfRequest(context)) {
            super.processValidators(context);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
        params.get(Constants.PARTIAL_SOURCE_PARAM);
        final String clientId = this.getClientId(context);

        if (isSelfRequest(context)) {
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("toggle")) {
                final boolean collapsed = Boolean.valueOf(params.get(clientId + "_collapsed"));
                final Visibility visibility = collapsed ? Visibility.HIDDEN : Visibility.VISIBLE;

                super.queueEvent(new ToggleEvent(this, behaviorEvent.getBehavior(), visibility));

            } else if (eventName.equals("close")) {
                super.queueEvent(new CloseEvent(this, behaviorEvent.getBehavior()));
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

    public void setClosable(final boolean _closable) {
        getStateHelper().put(PropertyKeys.closable, _closable);
        handleAttribute("closable", _closable);
    }

    public void setCloseSpeed(final int _closeSpeed) {
        getStateHelper().put(PropertyKeys.closeSpeed, _closeSpeed);
        handleAttribute("closeSpeed", _closeSpeed);
    }

    public void setCloseTitle(final java.lang.String _closeTitle) {
        getStateHelper().put(PropertyKeys.closeTitle, _closeTitle);
        handleAttribute("closeTitle", _closeTitle);
    }

    public void setCollapsed(final boolean _collapsed) {
        getStateHelper().put(PropertyKeys.collapsed, _collapsed);
        handleAttribute("collapsed", _collapsed);
    }

    public void setFooter(final java.lang.String _footer) {
        getStateHelper().put(PropertyKeys.footer, _footer);
        handleAttribute("footer", _footer);
    }

    public void setHeader(final java.lang.String _header) {
        getStateHelper().put(PropertyKeys.header, _header);
        handleAttribute("header", _header);
    }

    public void setMenuTitle(final java.lang.String _menuTitle) {
        getStateHelper().put(PropertyKeys.menuTitle, _menuTitle);
        handleAttribute("menuTitle", _menuTitle);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setToggleable(final boolean _toggleable) {
        getStateHelper().put(PropertyKeys.toggleable, _toggleable);
        handleAttribute("toggleable", _toggleable);
    }

    public void setToggleOrientation(final java.lang.String _toggleOrientation) {
        getStateHelper().put(PropertyKeys.toggleOrientation, _toggleOrientation);
        handleAttribute("toggleOrientation", _toggleOrientation);
    }

    public void setToggleSpeed(final int _toggleSpeed) {
        getStateHelper().put(PropertyKeys.toggleSpeed, _toggleSpeed);
        handleAttribute("toggleSpeed", _toggleSpeed);
    }

    public void setToggleTitle(final java.lang.String _toggleTitle) {
        getStateHelper().put(PropertyKeys.toggleTitle, _toggleTitle);
        handleAttribute("toggleTitle", _toggleTitle);
    }

    public void setVisible(final boolean _visible) {
        getStateHelper().put(PropertyKeys.visible, _visible);
        handleAttribute("visible", _visible);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}