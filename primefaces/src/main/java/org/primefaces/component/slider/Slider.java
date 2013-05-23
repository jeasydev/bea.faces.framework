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
package org.primefaces.component.slider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import org.primefaces.event.SlideEndEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Slider extends UIComponentBase implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        forValue("for"),
        display,
        minValue,
        maxValue,
        style,
        styleClass,
        animate,
        type,
        step,
        disabled,
        onSlideStart,
        onSlide,
        onSlideEnd,
        range,
        displayTemplate;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Slider";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.SliderRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private final static String DEFAULT_EVENT = "slideEnd";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList(Slider.DEFAULT_EVENT));

    private final Map<String, AjaxBehaviorEvent> customEvents = new HashMap<String, AjaxBehaviorEvent>();

    public Slider() {
        setRendererType(Slider.DEFAULT_RENDERER);
    }

    @Override
    public String getDefaultEventName() {
        return Slider.DEFAULT_EVENT;
    }

    public java.lang.String getDisplay() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.display, null);
    }

    public java.lang.String getDisplayTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.displayTemplate, null);
    }

    @Override
    public Collection<String> getEventNames() {
        return Slider.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Slider.COMPONENT_FAMILY;
    }

    public java.lang.String getFor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public int getMaxValue() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxValue, 100);
    }

    public int getMinValue() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minValue, 0);
    }

    public java.lang.String getOnSlide() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onSlide, null);
    }

    public java.lang.String getOnSlideEnd() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onSlideEnd, null);
    }

    public java.lang.String getOnSlideStart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onSlideStart, null);
    }

    public int getStep() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.step, 1);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getType() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.type, "horizontal");
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Slider.OPTIMIZED_PACKAGE)) {
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
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.animate, true);
    }

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public boolean isRange() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.range, false);
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

            if (eventName.equals("slideEnd")) {
                final int sliderValue = Integer.parseInt(params.get(clientId + "_slideValue"));
                final SlideEndEvent slideEndEvent = new SlideEndEvent(this, behaviorEvent.getBehavior(), sliderValue);
                slideEndEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(slideEndEvent);
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

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setDisplay(final java.lang.String _display) {
        getStateHelper().put(PropertyKeys.display, _display);
        handleAttribute("display", _display);
    }

    public void setDisplayTemplate(final java.lang.String _displayTemplate) {
        getStateHelper().put(PropertyKeys.displayTemplate, _displayTemplate);
        handleAttribute("displayTemplate", _displayTemplate);
    }

    public void setFor(final java.lang.String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
        handleAttribute("forValue", _for);
    }

    public void setMaxValue(final int _maxValue) {
        getStateHelper().put(PropertyKeys.maxValue, _maxValue);
        handleAttribute("maxValue", _maxValue);
    }

    public void setMinValue(final int _minValue) {
        getStateHelper().put(PropertyKeys.minValue, _minValue);
        handleAttribute("minValue", _minValue);
    }

    public void setOnSlide(final java.lang.String _onSlide) {
        getStateHelper().put(PropertyKeys.onSlide, _onSlide);
        handleAttribute("onSlide", _onSlide);
    }

    public void setOnSlideEnd(final java.lang.String _onSlideEnd) {
        getStateHelper().put(PropertyKeys.onSlideEnd, _onSlideEnd);
        handleAttribute("onSlideEnd", _onSlideEnd);
    }

    public void setOnSlideStart(final java.lang.String _onSlideStart) {
        getStateHelper().put(PropertyKeys.onSlideStart, _onSlideStart);
        handleAttribute("onSlideStart", _onSlideStart);
    }

    public void setRange(final boolean _range) {
        getStateHelper().put(PropertyKeys.range, _range);
        handleAttribute("range", _range);
    }

    public void setStep(final int _step) {
        getStateHelper().put(PropertyKeys.step, _step);
        handleAttribute("step", _step);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setType(final java.lang.String _type) {
        getStateHelper().put(PropertyKeys.type, _type);
        handleAttribute("type", _type);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}