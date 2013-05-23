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
package org.primefaces.component.overlaypanel;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class OverlayPanel extends UIPanel implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        style,
        styleClass,
        forValue("for"),
        showEvent,
        hideEvent,
        showEffect,
        hideEffect,
        appendToBody,
        onShow,
        onHide,
        my,
        at,
        dynamic;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.OverlayPanel";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.OverlayPanelRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String STYLE_CLASS = "ui-overlaypanel ui-widget ui-widget-content ui-overlay-hidden ui-corner-all ui-shadow";

    public OverlayPanel() {
        setRendererType(OverlayPanel.DEFAULT_RENDERER);
    }

    public java.lang.String getAt() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.at, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return OverlayPanel.COMPONENT_FAMILY;
    }

    public java.lang.String getFor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public java.lang.String getHideEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.hideEffect, null);
    }

    public java.lang.String getHideEvent() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.hideEvent, null);
    }

    public java.lang.String getMy() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.my, null);
    }

    public java.lang.String getOnHide() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onHide, null);
    }

    public java.lang.String getOnShow() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onShow, null);
    }

    public java.lang.String getShowEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.showEffect, null);
    }

    public java.lang.String getShowEvent() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.showEvent, null);
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
            if (cname != null && cname.startsWith(OverlayPanel.OPTIMIZED_PACKAGE)) {
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

    public boolean isContentLoadRequest(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context)
                                                                                     + "_contentLoad");
    }

    public boolean isDynamic() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.dynamic, false);
    }

    private boolean isRequestSource(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (isRequestSource(context)) {
            decode(context);
        } else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isRequestSource(context)) {
            super.processUpdates(context);
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isRequestSource(context)) {
            super.processValidators(context);
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

    public void setAppendToBody(final boolean _appendToBody) {
        getStateHelper().put(PropertyKeys.appendToBody, _appendToBody);
        handleAttribute("appendToBody", _appendToBody);
    }

    public void setAt(final java.lang.String _at) {
        getStateHelper().put(PropertyKeys.at, _at);
        handleAttribute("at", _at);
    }

    public void setDynamic(final boolean _dynamic) {
        getStateHelper().put(PropertyKeys.dynamic, _dynamic);
        handleAttribute("dynamic", _dynamic);
    }

    public void setFor(final java.lang.String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
        handleAttribute("forValue", _for);
    }

    public void setHideEffect(final java.lang.String _hideEffect) {
        getStateHelper().put(PropertyKeys.hideEffect, _hideEffect);
        handleAttribute("hideEffect", _hideEffect);
    }

    public void setHideEvent(final java.lang.String _hideEvent) {
        getStateHelper().put(PropertyKeys.hideEvent, _hideEvent);
        handleAttribute("hideEvent", _hideEvent);
    }

    public void setMy(final java.lang.String _my) {
        getStateHelper().put(PropertyKeys.my, _my);
        handleAttribute("my", _my);
    }

    public void setOnHide(final java.lang.String _onHide) {
        getStateHelper().put(PropertyKeys.onHide, _onHide);
        handleAttribute("onHide", _onHide);
    }

    public void setOnShow(final java.lang.String _onShow) {
        getStateHelper().put(PropertyKeys.onShow, _onShow);
        handleAttribute("onShow", _onShow);
    }

    public void setShowEffect(final java.lang.String _showEffect) {
        getStateHelper().put(PropertyKeys.showEffect, _showEffect);
        handleAttribute("showEffect", _showEffect);
    }

    public void setShowEvent(final java.lang.String _showEvent) {
        getStateHelper().put(PropertyKeys.showEvent, _showEvent);
        handleAttribute("showEvent", _showEvent);
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