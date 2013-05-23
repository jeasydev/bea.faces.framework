/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.waypoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.EnhancedAttachable;
import org.primefaces.extensions.event.WaypointEvent;
import org.primefaces.util.Constants;

/**
 * Waypoint.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.6
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"), @ResourceDependency(library = "primefaces-extensions", name = "waypoint/waypoint.js") })
public class Waypoint extends UIComponentBase implements Widget, EnhancedAttachable, ClientBehaviorHolder {

    /**
     * PropertyKeys
     * 
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        widgetVar,
        forValue("for"),
        forSelector,
        forContext,
        forContextSelector,
        offset,
        continuous,
        onlyOnScroll,
        triggerOnce;

        private String toString;

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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Waypoint";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.WaypointRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("reached"));

    public Waypoint() {
        setRendererType(Waypoint.DEFAULT_RENDERER);
    }

    @Override
    public Collection<String> getEventNames() {
        return Waypoint.EVENT_NAMES;
    }

    @Override
    public String getFamily() {
        return Waypoint.COMPONENT_FAMILY;
    }

    @Override
    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public String getForContext() {
        return (String) getStateHelper().eval(PropertyKeys.forContext, null);
    }

    public String getForContextSelector() {
        return (String) getStateHelper().eval(PropertyKeys.forContextSelector, null);
    }

    @Override
    public String getForSelector() {
        return (String) getStateHelper().eval(PropertyKeys.forSelector, null);
    }

    public String getOffset() {
        return (String) getStateHelper().eval(PropertyKeys.offset, null);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public boolean isContinuous() {
        return (Boolean) getStateHelper().eval(PropertyKeys.continuous, true);
    }

    public boolean isOnlyOnScroll() {
        return (Boolean) getStateHelper().eval(PropertyKeys.onlyOnScroll, false);
    }

    private boolean isSelfRequest(final FacesContext fc) {
        return this.getClientId(fc).equals(fc.getExternalContext().getRequestParameterMap()
                                               .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isTriggerOnce() {
        return (Boolean) getStateHelper().eval(PropertyKeys.triggerOnce, false);
    }

    @Override
    public void processDecodes(final FacesContext fc) {
        if (isSelfRequest(fc)) {
            decode(fc);
        } else {
            super.processDecodes(fc);
        }
    }

    @Override
    public void processUpdates(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processUpdates(fc);
        }
    }

    @Override
    public void processValidators(final FacesContext fc) {
        if (!isSelfRequest(fc)) {
            super.processValidators(fc);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (isSelfRequest(fc)) {
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if ("reached".equals(eventName)) {
                final String direction = params.get(this.getClientId(fc) + "_direction");
                final String waypointId = params.get(this.getClientId(fc) + "_waypointId");

                final WaypointEvent waypointEvent = new WaypointEvent(this,
                                                                      behaviorEvent.getBehavior(),
                                                                      (direction != null ? WaypointEvent.Direction
                                                                          .valueOf(direction
                                                                              .toUpperCase(Locale.ENGLISH)) : null),
                                                                      waypointId);
                waypointEvent.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(waypointEvent);

                return;
            }
        }

        super.queueEvent(event);
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

        if (userWidgetVar != null) {
            return userWidgetVar;
        }

        return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAttribute(final PropertyKeys property, final Object value) {
        getStateHelper().put(property, value);

        @SuppressWarnings("unchecked")
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Waypoint.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }

        if (setAttributes != null && value == null) {
            final String attributeName = property.toString();
            final ValueExpression ve = getValueExpression(attributeName);
            if (ve == null) {
                setAttributes.remove(attributeName);
            } else if (!setAttributes.contains(attributeName)) {
                setAttributes.add(attributeName);
            }
        }
    }

    public void setContinuous(final boolean continuous) {
        setAttribute(PropertyKeys.continuous, continuous);
    }

    @Override
    public void setFor(final String forValue) {
        setAttribute(PropertyKeys.forValue, forValue);
    }

    public void setForContext(final String forContext) {
        setAttribute(PropertyKeys.forContext, forContext);
    }

    public void setForContextSelector(final String forContextSelector) {
        setAttribute(PropertyKeys.forContextSelector, forContextSelector);
    }

    @Override
    public void setForSelector(final String forSelector) {
        setAttribute(PropertyKeys.forSelector, forSelector);
    }

    public void setOffset(final String offset) {
        setAttribute(PropertyKeys.offset, offset);
    }

    public void setOnlyOnScroll(final boolean onlyOnScroll) {
        setAttribute(PropertyKeys.onlyOnScroll, onlyOnScroll);
    }

    public void setTriggerOnce(final boolean triggerOnce) {
        setAttribute(PropertyKeys.triggerOnce, triggerOnce);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }
}
