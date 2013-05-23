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
package org.primefaces.component.confirmdialog;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class ConfirmDialog extends UIPanel implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.RTLAware {

    protected enum PropertyKeys {

        widgetVar,
        message,
        header,
        severity,
        width,
        height,
        style,
        styleClass,
        closable,
        appendToBody,
        visible,
        showEffect,
        hideEffect,
        closeOnEscape,
        dir;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.ConfirmDialog";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.ConfirmDialogRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String CONTAINER_CLASS = "ui-confirm-dialog ui-dialog ui-widget ui-widget-content ui-overlay-hidden ui-corner-all ui-shadow";

    public static final String BUTTONPANE_CLASS = "ui-dialog-buttonpane ui-widget-content ui-helper-clearfix";

    public static final String SEVERITY_ICON_CLASS = "ui-confirm-dialog-severity";

    public ConfirmDialog() {
        setRendererType(ConfirmDialog.DEFAULT_RENDERER);
    }

    public java.lang.String getDir() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dir, "ltr");
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return ConfirmDialog.COMPONENT_FAMILY;
    }

    public java.lang.String getHeader() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.header, null);
    }

    public java.lang.String getHeight() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.height, null);
    }

    public java.lang.String getHideEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.hideEffect, null);
    }

    public java.lang.String getMessage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.message, null);
    }

    public java.lang.String getSeverity() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.severity, "alert");
    }

    public java.lang.String getShowEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.showEffect, null);
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

    public java.lang.String getWidth() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.width, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(ConfirmDialog.OPTIMIZED_PACKAGE)) {
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

    public boolean isAppendToBody() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.appendToBody, false);
    }

    public boolean isClosable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.closable, true);
    }

    public boolean isCloseOnEscape() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.closeOnEscape, false);
    }

    @Override
    public boolean isRTL() {
        return getDir().equalsIgnoreCase("rtl");
    }

    public boolean isVisible() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.visible, false);
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

    public void setAppendToBody(final boolean _appendToBody) {
        getStateHelper().put(PropertyKeys.appendToBody, _appendToBody);
        handleAttribute("appendToBody", _appendToBody);
    }

    public void setClosable(final boolean _closable) {
        getStateHelper().put(PropertyKeys.closable, _closable);
        handleAttribute("closable", _closable);
    }

    public void setCloseOnEscape(final boolean _closeOnEscape) {
        getStateHelper().put(PropertyKeys.closeOnEscape, _closeOnEscape);
        handleAttribute("closeOnEscape", _closeOnEscape);
    }

    public void setDir(final java.lang.String _dir) {
        getStateHelper().put(PropertyKeys.dir, _dir);
        handleAttribute("dir", _dir);
    }

    public void setHeader(final java.lang.String _header) {
        getStateHelper().put(PropertyKeys.header, _header);
        handleAttribute("header", _header);
    }

    public void setHeight(final java.lang.String _height) {
        getStateHelper().put(PropertyKeys.height, _height);
        handleAttribute("height", _height);
    }

    public void setHideEffect(final java.lang.String _hideEffect) {
        getStateHelper().put(PropertyKeys.hideEffect, _hideEffect);
        handleAttribute("hideEffect", _hideEffect);
    }

    public void setMessage(final java.lang.String _message) {
        getStateHelper().put(PropertyKeys.message, _message);
        handleAttribute("message", _message);
    }

    public void setSeverity(final java.lang.String _severity) {
        getStateHelper().put(PropertyKeys.severity, _severity);
        handleAttribute("severity", _severity);
    }

    public void setShowEffect(final java.lang.String _showEffect) {
        getStateHelper().put(PropertyKeys.showEffect, _showEffect);
        handleAttribute("showEffect", _showEffect);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setVisible(final boolean _visible) {
        getStateHelper().put(PropertyKeys.visible, _visible);
        handleAttribute("visible", _visible);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }

    public void setWidth(final java.lang.String _width) {
        getStateHelper().put(PropertyKeys.width, _width);
        handleAttribute("width", _width);
    }
}