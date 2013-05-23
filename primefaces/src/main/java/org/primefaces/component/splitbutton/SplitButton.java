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
package org.primefaces.component.splitbutton;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class SplitButton extends HtmlCommandButton implements org.primefaces.component.api.AjaxSource,
    org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        ajax,
        async,
        process,
        update,
        onstart,
        oncomplete,
        onerror,
        onsuccess,
        global,
        icon,
        iconPos,
        inline,
        partialSubmit;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.SplitButton";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.SplitButtonRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String STYLE_CLASS = "ui-splitbutton ui-buttonset ui-widget";

    public static final String BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-left ui-button-text-icon-left";

    public static final String BUTTON_TEXT_ICON_RIGHT_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-left ui-button-text-icon-right";
    public static final String MENU_ICON_BUTTON_CLASS = "ui-splitbutton-menubutton  ui-button ui-widget ui-state-default ui-corner-right ui-button-icon-only";

    public final static String BUTTON_TEXT_ONLY_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-left ui-button-text-only";
    public final static String BUTTON_ICON_ONLY_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-left ui-button-icon-only";

    public SplitButton() {
        setRendererType(SplitButton.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return SplitButton.COMPONENT_FAMILY;
    }

    public java.lang.String getIcon() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.icon, null);
    }

    public java.lang.String getIconPos() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.iconPos, "left");
    }

    @Override
    public java.lang.String getOncomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    @Override
    public java.lang.String getOnerror() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    @Override
    public java.lang.String getOnstart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    @Override
    public java.lang.String getOnsuccess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    @Override
    public java.lang.String getProcess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
    }

    @Override
    public java.lang.String getUpdate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.update, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(SplitButton.OPTIMIZED_PACKAGE)) {
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

    public boolean isAjax() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.ajax, true);
    }

    @Override
    public boolean isAsync() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    @Override
    public boolean isGlobal() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public boolean isInline() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.inline, false);
    }

    @Override
    public boolean isPartialSubmit() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
    }

    @Override
    public boolean isPartialSubmitSet() {
        return (getStateHelper().get(PropertyKeys.partialSubmit) != null)
            || (getValueExpression("partialSubmit") != null);
    }

    public String resolveStyleClass() {
        final String icon = getIcon();
        final Object value = getValue();
        String styleClass = "";

        if (value != null && icon == null) {
            styleClass = SplitButton.BUTTON_TEXT_ONLY_BUTTON_CLASS;
        } else if (value != null && icon != null) {
            styleClass = getIconPos().equals("left")
                                                    ? SplitButton.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS
                                                    : SplitButton.BUTTON_TEXT_ICON_RIGHT_BUTTON_CLASS;
        } else if (value == null && icon != null) {
            styleClass = SplitButton.BUTTON_ICON_ONLY_BUTTON_CLASS;
        }

        if (isDisabled()) {
            styleClass = styleClass + " ui-state-disabled";
        }

        return styleClass;
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

    public void setAjax(final boolean _ajax) {
        getStateHelper().put(PropertyKeys.ajax, _ajax);
        handleAttribute("ajax", _ajax);
    }

    public void setAsync(final boolean _async) {
        getStateHelper().put(PropertyKeys.async, _async);
        handleAttribute("async", _async);
    }

    public void setGlobal(final boolean _global) {
        getStateHelper().put(PropertyKeys.global, _global);
        handleAttribute("global", _global);
    }

    public void setIcon(final java.lang.String _icon) {
        getStateHelper().put(PropertyKeys.icon, _icon);
        handleAttribute("icon", _icon);
    }

    public void setIconPos(final java.lang.String _iconPos) {
        getStateHelper().put(PropertyKeys.iconPos, _iconPos);
        handleAttribute("iconPos", _iconPos);
    }

    public void setInline(final boolean _inline) {
        getStateHelper().put(PropertyKeys.inline, _inline);
        handleAttribute("inline", _inline);
    }

    public void setOncomplete(final java.lang.String _oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
        handleAttribute("oncomplete", _oncomplete);
    }

    public void setOnerror(final java.lang.String _onerror) {
        getStateHelper().put(PropertyKeys.onerror, _onerror);
        handleAttribute("onerror", _onerror);
    }

    public void setOnstart(final java.lang.String _onstart) {
        getStateHelper().put(PropertyKeys.onstart, _onstart);
        handleAttribute("onstart", _onstart);
    }

    public void setOnsuccess(final java.lang.String _onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, _onsuccess);
        handleAttribute("onsuccess", _onsuccess);
    }

    public void setPartialSubmit(final boolean _partialSubmit) {
        getStateHelper().put(PropertyKeys.partialSubmit, _partialSubmit);
        handleAttribute("partialSubmit", _partialSubmit);
    }

    public void setProcess(final java.lang.String _process) {
        getStateHelper().put(PropertyKeys.process, _process);
        handleAttribute("process", _process);
    }

    public void setUpdate(final java.lang.String _update) {
        getStateHelper().put(PropertyKeys.update, _update);
        handleAttribute("update", _update);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}