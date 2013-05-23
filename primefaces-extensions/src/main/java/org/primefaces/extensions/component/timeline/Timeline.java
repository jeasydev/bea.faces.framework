/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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
package org.primefaces.extensions.component.timeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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
import org.primefaces.extensions.event.timeline.TimelineAddEvent;
import org.primefaces.extensions.event.timeline.TimelineModificationEvent;
import org.primefaces.extensions.event.timeline.TimelineRangeEvent;
import org.primefaces.extensions.event.timeline.TimelineSelectEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.DateUtils;
import org.primefaces.util.Constants;

/**
 * Timeline component class.
 * 
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since 0.7 (reimplemented)
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css"), @ResourceDependency(library = "primefaces-extensions", name = "timeline/timeline.js"), @ResourceDependency(library = "primefaces-extensions", name = "timeline/timeline.css") })
public class Timeline extends UIComponentBase implements Widget, ClientBehaviorHolder {

    /**
     * PropertyKeys
     * 
     * @author Oleg Varaksin / last modified by $Author: $
     * @version $Revision: 1.0 $
     */
    enum PropertyKeys {

        widgetVar,
        value,
        var,
        locale,
        timeZone,
        style,
        styleClass,
        height,
        minHeight,
        width,
        responsive,
        axisOnTop,
        dragAreaWidth,
        editable,
        selectable,
        zoomable,
        moveable,
        start,
        end,
        min,
        max,
        zoomMin,
        zoomMax,
        eventMargin,
        eventMarginAxis,
        eventStyle,
        groupsChangeable,
        groupsOnRight,
        groupsWidth,
        snapEvents,
        stackEvents,
        showCurrentTime,
        showMajorLabels,
        showMinorLabels,
        showButtonNew,
        showNavigation;

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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.Timeline";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    public static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimelineRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList("add", "change", "edit", "delete", "select", "rangechange", "rangechanged"));

    public Timeline() {
        setRendererType(Timeline.DEFAULT_RENDERER);
    }

    public int getDragAreaWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.dragAreaWidth, 10);
    }

    public Date getEnd() {
        return (Date) getStateHelper().eval(PropertyKeys.end, null);
    }

    public int getEventMargin() {
        return (Integer) getStateHelper().eval(PropertyKeys.eventMargin, 10);
    }

    public int getEventMarginAxis() {
        return (Integer) getStateHelper().eval(PropertyKeys.eventMarginAxis, 10);
    }

    @Override
    public Collection<String> getEventNames() {
        return Timeline.EVENT_NAMES;
    }

    public String getEventStyle() {
        return (String) getStateHelper().eval(PropertyKeys.eventStyle, "box");
    }

    @Override
    public String getFamily() {
        return Timeline.COMPONENT_FAMILY;
    }

    public String getGroupsWidth() {
        return (String) getStateHelper().eval(PropertyKeys.groupsWidth, null);
    }

    public String getHeight() {
        return (String) getStateHelper().eval(PropertyKeys.height, "auto");
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public Date getMax() {
        return (Date) getStateHelper().eval(PropertyKeys.max, null);
    }

    public Date getMin() {
        return (Date) getStateHelper().eval(PropertyKeys.min, null);
    }

    public int getMinHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.minHeight, 0);
    }

    public Date getStart() {
        return (Date) getStateHelper().eval(PropertyKeys.start, null);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public Object getTimeZone() {
        return getStateHelper().eval(PropertyKeys.timeZone, null);
    }

    public TimelineModel getValue() {
        return (TimelineModel) getStateHelper().eval(PropertyKeys.value, null);
    }

    public String getVar() {
        return (String) getStateHelper().eval(PropertyKeys.var, null);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public String getWidth() {
        return (String) getStateHelper().eval(PropertyKeys.width, "100%");
    }

    public long getZoomMax() {
        return (Long) getStateHelper().eval(PropertyKeys.zoomMax, 315360000000000L);
    }

    public long getZoomMin() {
        return (Long) getStateHelper().eval(PropertyKeys.zoomMin, 10L);
    }

    public boolean isAxisOnTop() {
        return (Boolean) getStateHelper().eval(PropertyKeys.axisOnTop, false);
    }

    public boolean isEditable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.editable, false);
    }

    public boolean isGroupsChangeable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.groupsChangeable, true);
    }

    public boolean isGroupsOnRight() {
        return (Boolean) getStateHelper().eval(PropertyKeys.groupsOnRight, false);
    }

    public boolean isMoveable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.moveable, true);
    }

    public boolean isResponsive() {
        return (Boolean) getStateHelper().eval(PropertyKeys.responsive, true);
    }

    public boolean isSelectable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.selectable, true);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isShowButtonNew() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showButtonNew, false);
    }

    public boolean isShowCurrentTime() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showCurrentTime, true);
    }

    public boolean isShowMajorLabels() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showMajorLabels, true);
    }

    public boolean isShowMinorLabels() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showMinorLabels, true);
    }

    public boolean isShowNavigation() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showNavigation, false);
    }

    public boolean isSnapEvents() {
        return (Boolean) getStateHelper().eval(PropertyKeys.snapEvents, true);
    }

    public boolean isStackEvents() {
        return (Boolean) getStateHelper().eval(PropertyKeys.stackEvents, true);
    }

    public boolean isZoomable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.zoomable, true);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
        final String clientId = this.getClientId(context);

        if (isSelfRequest(context)) {
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if ("add".equals(eventName)) {
                // preset start / end date and the group
                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                final TimeZone timeZone = ComponentUtils.resolveTimeZone(getTimeZone());
                final TimelineAddEvent te = new TimelineAddEvent(this, behaviorEvent.getBehavior(), DateUtils
                    .toUtcDate(calendar, timeZone, params.get(clientId + "_startDate")), DateUtils
                    .toUtcDate(calendar, timeZone, params.get(clientId + "_endDate")), params.get(clientId + "_group"));
                te.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(te);

                return;
            } else if ("change".equals(eventName)) {
                TimelineEvent clonedEvent = null;
                final TimelineEvent timelineEvent = getValue().getEvent(params.get(clientId + "_eventIdx"));

                if (timelineEvent != null) {
                    clonedEvent = new TimelineEvent();
                    clonedEvent.setData(timelineEvent.getData());
                    clonedEvent.setEditable(timelineEvent.isEditable());
                    clonedEvent.setStyleClass(timelineEvent.getStyleClass());

                    // update start / end date and the group
                    final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    final TimeZone timeZone = ComponentUtils.resolveTimeZone(getTimeZone());
                    clonedEvent.setStartDate(DateUtils.toUtcDate(calendar, timeZone, params
                        .get(clientId + "_startDate")));
                    clonedEvent.setEndDate(DateUtils.toUtcDate(calendar, timeZone, params.get(clientId + "_endDate")));
                    clonedEvent.setGroup(params.get(clientId + "_group"));
                }

                final TimelineModificationEvent te = new TimelineModificationEvent(this,
                                                                                   behaviorEvent.getBehavior(),
                                                                                   clonedEvent);
                te.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(te);

                return;
            } else if ("edit".equals(eventName) || "delete".equals(eventName)) {
                TimelineEvent clonedEvent = null;
                final TimelineEvent timelineEvent = getValue().getEvent(params.get(clientId + "_eventIdx"));

                if (timelineEvent != null) {
                    clonedEvent = new TimelineEvent();
                    clonedEvent.setData(timelineEvent.getData());
                    clonedEvent.setStartDate((Date) timelineEvent.getStartDate().clone());
                    clonedEvent.setEndDate(timelineEvent.getEndDate() != null ? (Date) timelineEvent.getEndDate()
                        .clone() : null);
                    clonedEvent.setEditable(timelineEvent.isEditable());
                    clonedEvent.setGroup(timelineEvent.getGroup());
                    clonedEvent.setStyleClass(timelineEvent.getStyleClass());
                }

                final TimelineModificationEvent te = new TimelineModificationEvent(this,
                                                                                   behaviorEvent.getBehavior(),
                                                                                   clonedEvent);
                te.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(te);

                return;
            } else if ("select".equals(eventName)) {
                final TimelineEvent timelineEvent = getValue().getEvent(params.get(clientId + "_eventIdx"));
                final TimelineSelectEvent te = new TimelineSelectEvent(this, behaviorEvent.getBehavior(), timelineEvent);
                te.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(te);

                return;
            } else if ("rangechange".equals(eventName)) {
                // get start / end date
                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                final TimeZone timeZone = ComponentUtils.resolveTimeZone(getTimeZone());
                final TimelineRangeEvent te = new TimelineRangeEvent(this, behaviorEvent.getBehavior(), DateUtils
                    .toUtcDate(calendar, timeZone, params.get(clientId + "_startDate")), DateUtils
                    .toUtcDate(calendar, timeZone, params.get(clientId + "_endDate")));
                te.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(te);

                return;
            } else if ("rangechanged".equals(eventName)) {
                // get start / end date
                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                final TimeZone timeZone = ComponentUtils.resolveTimeZone(getTimeZone());
                final TimelineRangeEvent te = new TimelineRangeEvent(this, behaviorEvent.getBehavior(), DateUtils
                    .toUtcDate(calendar, timeZone, params.get(clientId + "_startDate")), DateUtils
                    .toUtcDate(calendar, timeZone, params.get(clientId + "_endDate")));
                te.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(te);

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
            if (cname != null && cname.startsWith(Timeline.OPTIMIZED_PACKAGE)) {
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

    public void setAxisOnTop(final boolean axisOnTop) {
        setAttribute(PropertyKeys.axisOnTop, axisOnTop);
    }

    public void setDragAreaWidth(final int dragAreaWidth) {
        setAttribute(PropertyKeys.dragAreaWidth, dragAreaWidth);
    }

    public void setEditable(final boolean editable) {
        setAttribute(PropertyKeys.editable, editable);
    }

    public void setEnd(final Date end) {
        setAttribute(PropertyKeys.end, end);
    }

    public void setEventMargin(final int eventMargin) {
        setAttribute(PropertyKeys.eventMargin, eventMargin);
    }

    public void setEventMarginAxis(final int eventMarginAxis) {
        setAttribute(PropertyKeys.eventMarginAxis, eventMarginAxis);
    }

    public void setEventStyle(final String eventStyle) {
        setAttribute(PropertyKeys.eventStyle, eventStyle);
    }

    public void setGroupsChangeable(final boolean groupsChangeable) {
        setAttribute(PropertyKeys.groupsChangeable, groupsChangeable);
    }

    public void setGroupsOnRight(final boolean groupsOnRight) {
        setAttribute(PropertyKeys.groupsOnRight, groupsOnRight);
    }

    public void setGroupsWidth(final String groupsWidth) {
        setAttribute(PropertyKeys.groupsWidth, groupsWidth);
    }

    public void setHeight(final String height) {
        setAttribute(PropertyKeys.height, height);
    }

    public void setLocale(final Object locale) {
        setAttribute(PropertyKeys.locale, locale);
    }

    public void setMax(final Date max) {
        setAttribute(PropertyKeys.max, max);
    }

    public void setMin(final Date min) {
        setAttribute(PropertyKeys.min, min);
    }

    public void setMinHeight(final int minHeight) {
        setAttribute(PropertyKeys.minHeight, minHeight);
    }

    public void setMoveable(final boolean moveable) {
        setAttribute(PropertyKeys.moveable, moveable);
    }

    public void setResponsive(final boolean responsive) {
        setAttribute(PropertyKeys.responsive, responsive);
    }

    public void setSelectable(final boolean selectable) {
        setAttribute(PropertyKeys.selectable, selectable);
    }

    public void setShowButtonNew(final boolean showButtonNew) {
        setAttribute(PropertyKeys.showButtonNew, showButtonNew);
    }

    public void setShowCurrentTime(final boolean showCurrentTime) {
        setAttribute(PropertyKeys.showCurrentTime, showCurrentTime);
    }

    public void setShowMajorLabels(final boolean showMajorLabels) {
        setAttribute(PropertyKeys.showMajorLabels, showMajorLabels);
    }

    public void setShowMinorLabels(final boolean showMinorLabels) {
        setAttribute(PropertyKeys.showMinorLabels, showMinorLabels);
    }

    public void setShowNavigation(final boolean showNavigation) {
        setAttribute(PropertyKeys.showNavigation, showNavigation);
    }

    public void setSnapEvents(final boolean snapEvents) {
        setAttribute(PropertyKeys.snapEvents, snapEvents);
    }

    public void setStackEvents(final boolean stackEvents) {
        setAttribute(PropertyKeys.stackEvents, stackEvents);
    }

    public void setStart(final Date start) {
        setAttribute(PropertyKeys.start, start);
    }

    public void setStyle(final String style) {
        setAttribute(PropertyKeys.style, style);
    }

    public void setStyleClass(final String styleClass) {
        setAttribute(PropertyKeys.styleClass, styleClass);
    }

    public void setTimeZone(final Object timeZone) {
        setAttribute(PropertyKeys.timeZone, timeZone);
    }

    public void setValue(final TimelineModel value) {
        setAttribute(PropertyKeys.value, value);
    }

    public void setVar(final String var) {
        setAttribute(PropertyKeys.var, var);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }

    public void setWidth(final String width) {
        setAttribute(PropertyKeys.width, width);
    }

    public void setZoomable(final boolean zoomable) {
        setAttribute(PropertyKeys.zoomable, zoomable);
    }

    public void setZoomMax(final long zoomMax) {
        setAttribute(PropertyKeys.zoomMax, zoomMax);
    }

    public void setZoomMin(final long zoomMin) {
        setAttribute(PropertyKeys.zoomMin, zoomMin);
    }
}
