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
package org.primefaces.component.resizable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.ResizeEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Resizable extends UIComponentBase implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        forValue("for"),
        aspectRatio,
        proxy,
        handles,
        ghost,
        animate,
        effect,
        effectDuration,
        maxWidth,
        maxHeight,
        minWidth,
        minHeight,
        containment,
        grid,
        onStart,
        onResize,
        onStop;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Resizable";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.ResizableRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private final static String DEFAULT_EVENT = "resize";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList(Resizable.DEFAULT_EVENT));

    public Resizable() {
        setRendererType(Resizable.DEFAULT_RENDERER);
    }

    @Override
    public String getDefaultEventName() {
        return Resizable.DEFAULT_EVENT;
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, "swing");
    }

    public java.lang.String getEffectDuration() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effectDuration, "normal");
    }

    @Override
    public Collection<String> getEventNames() {
        return Resizable.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Resizable.COMPONENT_FAMILY;
    }

    public java.lang.String getFor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public int getGrid() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.grid, 1);
    }

    public java.lang.String getHandles() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.handles, null);
    }

    public int getMaxHeight() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxHeight, Integer.MAX_VALUE);
    }

    public int getMaxWidth() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxWidth, Integer.MAX_VALUE);
    }

    public int getMinHeight() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minHeight, Integer.MIN_VALUE);
    }

    public int getMinWidth() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minWidth, Integer.MIN_VALUE);
    }

    public java.lang.String getOnResize() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onResize, null);
    }

    public java.lang.String getOnStart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onStart, null);
    }

    public java.lang.String getOnStop() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onStop, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Resizable.OPTIMIZED_PACKAGE)) {
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

    public boolean isAnimate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.animate, false);
    }

    public boolean isAspectRatio() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.aspectRatio, false);
    }

    public boolean isContainment() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.containment, false);
    }

    public boolean isGhost() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.ghost, false);
    }

    public boolean isProxy() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.proxy, false);
    }

    private boolean isRequestSource(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        if (isRequestSource(context)) {
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = getClientId(context);

            if (eventName.equals("resize")) {
                final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
                final int width = Integer.parseInt(params.get(clientId + "_width"));
                final int height = Integer.parseInt(params.get(clientId + "_height"));

                super.queueEvent(new ResizeEvent(this, behaviorEvent.getBehavior(), width, height));
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

    public void setAnimate(final boolean _animate) {
        getStateHelper().put(PropertyKeys.animate, _animate);
        handleAttribute("animate", _animate);
    }

    public void setAspectRatio(final boolean _aspectRatio) {
        getStateHelper().put(PropertyKeys.aspectRatio, _aspectRatio);
        handleAttribute("aspectRatio", _aspectRatio);
    }

    public void setContainment(final boolean _containment) {
        getStateHelper().put(PropertyKeys.containment, _containment);
        handleAttribute("containment", _containment);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectDuration(final java.lang.String _effectDuration) {
        getStateHelper().put(PropertyKeys.effectDuration, _effectDuration);
        handleAttribute("effectDuration", _effectDuration);
    }

    public void setFor(final java.lang.String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
        handleAttribute("forValue", _for);
    }

    public void setGhost(final boolean _ghost) {
        getStateHelper().put(PropertyKeys.ghost, _ghost);
        handleAttribute("ghost", _ghost);
    }

    public void setGrid(final int _grid) {
        getStateHelper().put(PropertyKeys.grid, _grid);
        handleAttribute("grid", _grid);
    }

    public void setHandles(final java.lang.String _handles) {
        getStateHelper().put(PropertyKeys.handles, _handles);
        handleAttribute("handles", _handles);
    }

    public void setMaxHeight(final int _maxHeight) {
        getStateHelper().put(PropertyKeys.maxHeight, _maxHeight);
        handleAttribute("maxHeight", _maxHeight);
    }

    public void setMaxWidth(final int _maxWidth) {
        getStateHelper().put(PropertyKeys.maxWidth, _maxWidth);
        handleAttribute("maxWidth", _maxWidth);
    }

    public void setMinHeight(final int _minHeight) {
        getStateHelper().put(PropertyKeys.minHeight, _minHeight);
        handleAttribute("minHeight", _minHeight);
    }

    public void setMinWidth(final int _minWidth) {
        getStateHelper().put(PropertyKeys.minWidth, _minWidth);
        handleAttribute("minWidth", _minWidth);
    }

    public void setOnResize(final java.lang.String _onResize) {
        getStateHelper().put(PropertyKeys.onResize, _onResize);
        handleAttribute("onResize", _onResize);
    }

    public void setOnStart(final java.lang.String _onStart) {
        getStateHelper().put(PropertyKeys.onStart, _onStart);
        handleAttribute("onStart", _onStart);
    }

    public void setOnStop(final java.lang.String _onStop) {
        getStateHelper().put(PropertyKeys.onStop, _onStop);
        handleAttribute("onStop", _onStop);
    }

    public void setProxy(final boolean _proxy) {
        getStateHelper().put(PropertyKeys.proxy, _proxy);
        handleAttribute("proxy", _proxy);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}