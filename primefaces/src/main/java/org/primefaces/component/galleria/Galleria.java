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
package org.primefaces.component.galleria;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "galleria/galleria.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "galleria/galleria.js") })
public class Galleria extends UIOutput implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        value,
        var,
        style,
        styleClass,
        effect,
        effectSpeed,
        frameWidth,
        frameHeight,
        showFilmstrip,
        autoPlay,
        transitionInterval,
        panelWidth,
        panelHeight,
        showCaption;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Galleria";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.GalleriaRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String CONTAINER_CLASS = "ui-galleria ui-widget ui-widget-content ui-corner-all";

    public final static String PANEL_WRAPPER_CLASS = "ui-galleria-panel-wrapper";

    public final static String PANEL_CLASS = "ui-galleria-panel ui-helper-hidden";
    public final static String PANEL_CONTENT_CLASS = "ui-galleria-panel-content";

    public Galleria() {
        setRendererType(Galleria.DEFAULT_RENDERER);
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, "fade");
    }

    public int getEffectSpeed() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.effectSpeed, 500);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Galleria.COMPONENT_FAMILY;
    }

    public int getFrameHeight() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.frameHeight, 40);
    }

    public int getFrameWidth() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.frameWidth, 60);
    }

    public int getPanelHeight() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.panelHeight, java.lang.Integer.MIN_VALUE);
    }

    public int getPanelWidth() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.panelWidth, java.lang.Integer.MIN_VALUE);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public int getTransitionInterval() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.transitionInterval, 4000);
    }

    @Override
    public java.lang.Object getValue() {
        return getStateHelper().eval(PropertyKeys.value, null);
    }

    public java.lang.String getVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.var, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Galleria.OPTIMIZED_PACKAGE)) {
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

    public boolean isAutoPlay() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoPlay, true);
    }

    public boolean isShowCaption() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showCaption, false);
    }

    public boolean isShowFilmstrip() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showFilmstrip, true);
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

    public void setAutoPlay(final boolean _autoPlay) {
        getStateHelper().put(PropertyKeys.autoPlay, _autoPlay);
        handleAttribute("autoPlay", _autoPlay);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectSpeed(final int _effectSpeed) {
        getStateHelper().put(PropertyKeys.effectSpeed, _effectSpeed);
        handleAttribute("effectSpeed", _effectSpeed);
    }

    public void setFrameHeight(final int _frameHeight) {
        getStateHelper().put(PropertyKeys.frameHeight, _frameHeight);
        handleAttribute("frameHeight", _frameHeight);
    }

    public void setFrameWidth(final int _frameWidth) {
        getStateHelper().put(PropertyKeys.frameWidth, _frameWidth);
        handleAttribute("frameWidth", _frameWidth);
    }

    public void setPanelHeight(final int _panelHeight) {
        getStateHelper().put(PropertyKeys.panelHeight, _panelHeight);
        handleAttribute("panelHeight", _panelHeight);
    }

    public void setPanelWidth(final int _panelWidth) {
        getStateHelper().put(PropertyKeys.panelWidth, _panelWidth);
        handleAttribute("panelWidth", _panelWidth);
    }

    public void setShowCaption(final boolean _showCaption) {
        getStateHelper().put(PropertyKeys.showCaption, _showCaption);
        handleAttribute("showCaption", _showCaption);
    }

    public void setShowFilmstrip(final boolean _showFilmstrip) {
        getStateHelper().put(PropertyKeys.showFilmstrip, _showFilmstrip);
        handleAttribute("showFilmstrip", _showFilmstrip);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setTransitionInterval(final int _transitionInterval) {
        getStateHelper().put(PropertyKeys.transitionInterval, _transitionInterval);
        handleAttribute("transitionInterval", _transitionInterval);
    }

    @Override
    public void setValue(final java.lang.Object _value) {
        getStateHelper().put(PropertyKeys.value, _value);
        handleAttribute("value", _value);
    }

    public void setVar(final java.lang.String _var) {
        getStateHelper().put(PropertyKeys.var, _var);
        handleAttribute("var", _var);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}