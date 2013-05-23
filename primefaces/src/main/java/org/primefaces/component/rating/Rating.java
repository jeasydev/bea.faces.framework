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
package org.primefaces.component.rating;

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
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.RateEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Rating extends UIInput implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        stars,
        disabled,
        readonly,
        onRate,
        style,
        styleClass,
        cancel;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Rating";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.RatingRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String CONTAINER_CLASS = "ui-rating";

    public static final String CANCEL_CLASS = "ui-rating-cancel";

    public static final String STAR_CLASS = "ui-rating-star";
    public static final String STAR_ON_CLASS = "ui-rating-star ui-rating-star-on";

    private final static String DEFAULT_EVENT = "rate";
    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("rate",
                                                                                                           "cancel"));

    private final Map<String, AjaxBehaviorEvent> customEvents = new HashMap<String, AjaxBehaviorEvent>();

    public Rating() {
        setRendererType(Rating.DEFAULT_RENDERER);
    }

    @Override
    public String getDefaultEventName() {
        return Rating.DEFAULT_EVENT;
    }

    @Override
    public Collection<String> getEventNames() {
        return Rating.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Rating.COMPONENT_FAMILY;
    }

    public java.lang.String getOnRate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onRate, null);
    }

    public int getStars() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.stars, 5);
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
            if (cname != null && cname.startsWith(Rating.OPTIMIZED_PACKAGE)) {
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

    public boolean isCancel() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.cancel, true);
    }

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public boolean isReadonly() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.readonly, false);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (event instanceof AjaxBehaviorEvent) {
            final String eventName = context.getExternalContext().getRequestParameterMap()
                .get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

            if (eventName.equals("rate")) {
                customEvents.put(eventName, (AjaxBehaviorEvent) event);
            } else if (eventName.equals("cancel")) {
                super.queueEvent(event);
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

    public void setCancel(final boolean _cancel) {
        getStateHelper().put(PropertyKeys.cancel, _cancel);
        handleAttribute("cancel", _cancel);
    }

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setOnRate(final java.lang.String _onRate) {
        getStateHelper().put(PropertyKeys.onRate, _onRate);
        handleAttribute("onRate", _onRate);
    }

    public void setReadonly(final boolean _readonly) {
        getStateHelper().put(PropertyKeys.readonly, _readonly);
        handleAttribute("readonly", _readonly);
    }

    public void setStars(final int _stars) {
        getStateHelper().put(PropertyKeys.stars, _stars);
        handleAttribute("stars", _stars);
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

    @Override
    public void validate(final FacesContext context) {
        super.validate(context);

        if (isValid()) {
            for (final String string : customEvents.keySet()) {
                final AjaxBehaviorEvent behaviorEvent = customEvents.get(string);
                final RateEvent rateEvent = new RateEvent(this, behaviorEvent.getBehavior(), getValue());

                rateEvent.setPhaseId(behaviorEvent.getPhaseId());

                super.queueEvent(rateEvent);
            }
        }
    }
}