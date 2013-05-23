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
package org.primefaces.component.schedule;

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
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "schedule/schedule.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "schedule/schedule.js") })
public class Schedule extends UIComponentBase implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        value,
        locale,
        aspectRatio,
        view,
        initialDate,
        showWeekends,
        style,
        styleClass,
        draggable,
        resizable,
        showHeader,
        leftHeaderTemplate,
        centerHeaderTemplate,
        rightHeaderTemplate,
        allDaySlot,
        slotMinutes,
        firstHour,
        minTime,
        maxTime,
        axisFormat,
        timeFormat,
        timeZone;

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

    public static final String COMPONENT_TYPE = "org.primefaces.Schedule";
    public static final String COMPONENT_FAMILY = "org.primefaces";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.ScheduleRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList("dateSelect", "eventSelect", "eventMove", "eventResize"));

    private java.util.Locale appropriateLocale;

    private TimeZone appropriateTimeZone;

    public Schedule() {
        setRendererType(Schedule.DEFAULT_RENDERER);
    }

    java.util.Locale calculateLocale(final FacesContext facesContext) {
        if (appropriateLocale == null) {
            final Object userLocale = getLocale();
            if (userLocale != null) {
                if (userLocale instanceof String)
                    appropriateLocale = new java.util.Locale((String) userLocale, "");
                else if (userLocale instanceof java.util.Locale)
                    appropriateLocale = (java.util.Locale) userLocale;
                else throw new IllegalArgumentException("Type:" + userLocale.getClass()
                    + " is not a valid locale type for calendar:" + this.getClientId(facesContext));
            } else {
                appropriateLocale = facesContext.getViewRoot().getLocale();
            }
        }

        return appropriateLocale;
    }

    public java.util.TimeZone calculateTimeZone() {
        if (appropriateTimeZone == null) {
            final Object usertimeZone = getTimeZone();
            if (usertimeZone != null) {
                if (usertimeZone instanceof String)
                    appropriateTimeZone = TimeZone.getTimeZone((String) usertimeZone);
                else if (usertimeZone instanceof java.util.TimeZone)
                    appropriateTimeZone = (TimeZone) usertimeZone;
                else throw new IllegalArgumentException("TimeZone could be either String or java.util.TimeZone");
            } else {
                appropriateTimeZone = TimeZone.getDefault();
            }
        }

        return appropriateTimeZone;
    }

    public double getAspectRatio() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.aspectRatio, java.lang.Double.MIN_VALUE);
    }

    public java.lang.String getAxisFormat() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.axisFormat, null);
    }

    public java.lang.String getCenterHeaderTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.centerHeaderTemplate, "title");
    }

    @Override
    public Collection<String> getEventNames() {
        return Schedule.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Schedule.COMPONENT_FAMILY;
    }

    public int getFirstHour() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.firstHour, 6);
    }

    public java.lang.Object getInitialDate() {
        return getStateHelper().eval(PropertyKeys.initialDate, null);
    }

    public java.lang.String getLeftHeaderTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.leftHeaderTemplate, "prev,next today");
    }

    public java.lang.Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public java.lang.String getMaxTime() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.maxTime, null);
    }

    public java.lang.String getMinTime() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.minTime, null);
    }

    public java.lang.String getRightHeaderTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.rightHeaderTemplate, "month,agendaWeek,agendaDay");
    }

    public int getSlotMinutes() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.slotMinutes, 30);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getTimeFormat() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.timeFormat, null);
    }

    public java.lang.Object getTimeZone() {
        return getStateHelper().eval(PropertyKeys.timeZone, null);
    }

    public org.primefaces.model.ScheduleModel getValue() {
        return (org.primefaces.model.ScheduleModel) getStateHelper().eval(PropertyKeys.value, null);
    }

    public java.lang.String getView() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.view, "month");
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Schedule.OPTIMIZED_PACKAGE)) {
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

    public boolean isAllDaySlot() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.allDaySlot, true);
    }

    public boolean isDraggable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.draggable, true);
    }

    public boolean isResizable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resizable, true);
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isShowHeader() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showHeader, true);
    }

    public boolean isShowWeekends() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showWeekends, true);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
        final String clientId = this.getClientId(context);
        final TimeZone tz = calculateTimeZone();

        if (isSelfRequest(context)) {

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            FacesEvent wrapperEvent = null;

            if (eventName.equals("dateSelect")) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(params.get(clientId + "_selectedDate")));
                calendar.setTimeZone(tz);
                final Date selectedDate = calendar.getTime();
                final SelectEvent selectEvent = new SelectEvent(this, behaviorEvent.getBehavior(), selectedDate);
                selectEvent.setPhaseId(behaviorEvent.getPhaseId());

                wrapperEvent = selectEvent;
            } else if (eventName.equals("eventSelect")) {
                final String selectedEventId = params.get(clientId + "_selectedEventId");
                final ScheduleEvent selectedEvent = getValue().getEvent(selectedEventId);

                wrapperEvent = new SelectEvent(this, behaviorEvent.getBehavior(), selectedEvent);
            } else if (eventName.equals("eventMove")) {
                final String movedEventId = params.get(clientId + "_movedEventId");
                final ScheduleEvent movedEvent = getValue().getEvent(movedEventId);
                final int dayDelta = Integer.valueOf(params.get(clientId + "_dayDelta"));
                final int minuteDelta = Integer.valueOf(params.get(clientId + "_minuteDelta"));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(movedEvent.getStartDate());
                calendar.setTimeZone(tz);
                calendar.add(Calendar.DATE, dayDelta);
                calendar.add(Calendar.MINUTE, minuteDelta);
                movedEvent.getStartDate().setTime(calendar.getTimeInMillis());

                calendar = Calendar.getInstance();
                calendar.setTime(movedEvent.getEndDate());
                calendar.setTimeZone(tz);
                calendar.add(Calendar.DATE, dayDelta);
                calendar.add(Calendar.MINUTE, minuteDelta);
                movedEvent.getEndDate().setTime(calendar.getTimeInMillis());

                wrapperEvent = new ScheduleEntryMoveEvent(this,
                                                          behaviorEvent.getBehavior(),
                                                          movedEvent,
                                                          dayDelta,
                                                          minuteDelta);
            } else if (eventName.equals("eventResize")) {
                final String resizedEventId = params.get(clientId + "_resizedEventId");
                final ScheduleEvent resizedEvent = getValue().getEvent(resizedEventId);
                final int dayDelta = Integer.valueOf(params.get(clientId + "_dayDelta"));
                final int minuteDelta = Integer.valueOf(params.get(clientId + "_minuteDelta"));

                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(resizedEvent.getEndDate());
                calendar.setTimeZone(tz);
                calendar.add(Calendar.DATE, dayDelta);
                calendar.add(Calendar.MINUTE, minuteDelta);
                resizedEvent.getEndDate().setTime(calendar.getTimeInMillis());

                wrapperEvent = new ScheduleEntryResizeEvent(this,
                                                            behaviorEvent.getBehavior(),
                                                            resizedEvent,
                                                            dayDelta,
                                                            minuteDelta);
            }

            wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());

            super.queueEvent(wrapperEvent);
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

    public void setAllDaySlot(final boolean _allDaySlot) {
        getStateHelper().put(PropertyKeys.allDaySlot, _allDaySlot);
        handleAttribute("allDaySlot", _allDaySlot);
    }

    public void setAspectRatio(final double _aspectRatio) {
        getStateHelper().put(PropertyKeys.aspectRatio, _aspectRatio);
        handleAttribute("aspectRatio", _aspectRatio);
    }

    public void setAxisFormat(final java.lang.String _axisFormat) {
        getStateHelper().put(PropertyKeys.axisFormat, _axisFormat);
        handleAttribute("axisFormat", _axisFormat);
    }

    public void setCenterHeaderTemplate(final java.lang.String _centerHeaderTemplate) {
        getStateHelper().put(PropertyKeys.centerHeaderTemplate, _centerHeaderTemplate);
        handleAttribute("centerHeaderTemplate", _centerHeaderTemplate);
    }

    public void setDraggable(final boolean _draggable) {
        getStateHelper().put(PropertyKeys.draggable, _draggable);
        handleAttribute("draggable", _draggable);
    }

    public void setFirstHour(final int _firstHour) {
        getStateHelper().put(PropertyKeys.firstHour, _firstHour);
        handleAttribute("firstHour", _firstHour);
    }

    public void setInitialDate(final java.lang.Object _initialDate) {
        getStateHelper().put(PropertyKeys.initialDate, _initialDate);
        handleAttribute("initialDate", _initialDate);
    }

    public void setLeftHeaderTemplate(final java.lang.String _leftHeaderTemplate) {
        getStateHelper().put(PropertyKeys.leftHeaderTemplate, _leftHeaderTemplate);
        handleAttribute("leftHeaderTemplate", _leftHeaderTemplate);
    }

    public void setLocale(final java.lang.Object _locale) {
        getStateHelper().put(PropertyKeys.locale, _locale);
        handleAttribute("locale", _locale);
    }

    public void setMaxTime(final java.lang.String _maxTime) {
        getStateHelper().put(PropertyKeys.maxTime, _maxTime);
        handleAttribute("maxTime", _maxTime);
    }

    public void setMinTime(final java.lang.String _minTime) {
        getStateHelper().put(PropertyKeys.minTime, _minTime);
        handleAttribute("minTime", _minTime);
    }

    public void setResizable(final boolean _resizable) {
        getStateHelper().put(PropertyKeys.resizable, _resizable);
        handleAttribute("resizable", _resizable);
    }

    public void setRightHeaderTemplate(final java.lang.String _rightHeaderTemplate) {
        getStateHelper().put(PropertyKeys.rightHeaderTemplate, _rightHeaderTemplate);
        handleAttribute("rightHeaderTemplate", _rightHeaderTemplate);
    }

    public void setShowHeader(final boolean _showHeader) {
        getStateHelper().put(PropertyKeys.showHeader, _showHeader);
        handleAttribute("showHeader", _showHeader);
    }

    public void setShowWeekends(final boolean _showWeekends) {
        getStateHelper().put(PropertyKeys.showWeekends, _showWeekends);
        handleAttribute("showWeekends", _showWeekends);
    }

    public void setSlotMinutes(final int _slotMinutes) {
        getStateHelper().put(PropertyKeys.slotMinutes, _slotMinutes);
        handleAttribute("slotMinutes", _slotMinutes);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setTimeFormat(final java.lang.String _timeFormat) {
        getStateHelper().put(PropertyKeys.timeFormat, _timeFormat);
        handleAttribute("timeFormat", _timeFormat);
    }

    public void setTimeZone(final java.lang.Object _timeZone) {
        getStateHelper().put(PropertyKeys.timeZone, _timeZone);
        handleAttribute("timeZone", _timeZone);
    }

    public void setValue(final org.primefaces.model.ScheduleModel _value) {
        getStateHelper().put(PropertyKeys.value, _value);
        handleAttribute("value", _value);
    }

    public void setView(final java.lang.String _view) {
        getStateHelper().put(PropertyKeys.view, _view);
        handleAttribute("view", _view);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}