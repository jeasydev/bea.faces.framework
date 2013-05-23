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
package org.primefaces.component.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import org.primefaces.event.SelectEvent;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Calendar extends HtmlInputText implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.InputHolder {

    protected enum PropertyKeys {

        widgetVar,
        mindate,
        maxdate,
        pages,
        mode,
        pattern,
        locale,
        navigator,
        timeZone,
        readonlyInput,
        showButtonPanel,
        effect,
        effectDuration,
        showOn,
        showWeek,
        disabledWeekends,
        showOtherMonths,
        selectOtherMonths,
        yearRange,
        timeOnly,
        stepHour,
        stepMinute,
        stepSecond,
        minHour,
        maxHour,
        minMinute,
        maxMinute,
        minSecond,
        maxSecond,
        pagedate,
        beforeShowDay;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Calendar";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.CalendarRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String INPUT_STYLE_CLASS = "ui-inputfield ui-widget ui-state-default ui-corner-all";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList("blur", "change", "valueChange", "click", "dblclick", "focus", "keydown", "keypress", "keyup",
                "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "select", "dateSelect"));

    private final Map<String, AjaxBehaviorEvent> customEvents = new HashMap<String, AjaxBehaviorEvent>();
    private java.util.Locale calculatedLocale;

    private java.util.TimeZone appropriateTimeZone;
    private String calculatedPattern = null;

    private String timeOnlyPattern = null;
    private boolean conversionFailed = false;

    public Calendar() {
        setRendererType(Calendar.DEFAULT_RENDERER);
    }

    public java.util.Locale calculateLocale(final FacesContext facesContext) {
        if (calculatedLocale == null) {
            final Object userLocale = getLocale();
            if (userLocale != null) {
                if (userLocale instanceof String) {
                    calculatedLocale = ComponentUtils.toLocale((String) userLocale);
                } else if (userLocale instanceof java.util.Locale)
                    calculatedLocale = (java.util.Locale) userLocale;
                else throw new IllegalArgumentException("Type:" + userLocale.getClass()
                    + " is not a valid locale type for calendar:" + this.getClientId(facesContext));
            } else {
                calculatedLocale = facesContext.getViewRoot().getLocale();
            }
        }

        return calculatedLocale;
    }

    public String calculatePattern() {
        if (calculatedPattern == null) {
            final String pattern = getPattern();
            final Locale locale = calculateLocale(getFacesContext());
            if (pattern == null) {
                calculatedPattern = ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale))
                    .toPattern();
            } else {
                calculatedPattern = pattern;
            }

        }

        return calculatedPattern;
    }

    public String calculateTimeOnlyPattern() {
        if (timeOnlyPattern == null) {
            final String localePattern = ((SimpleDateFormat) DateFormat
                .getDateInstance(DateFormat.SHORT, calculateLocale(getFacesContext()))).toPattern();
            final String userTimePattern = getPattern();

            timeOnlyPattern = localePattern + " " + userTimePattern;
        }

        return timeOnlyPattern;
    }

    public java.util.TimeZone calculateTimeZone() {
        if (appropriateTimeZone == null) {
            final Object usertimeZone = getTimeZone();
            if (usertimeZone != null) {
                if (usertimeZone instanceof String)
                    appropriateTimeZone = java.util.TimeZone.getTimeZone((String) usertimeZone);
                else if (usertimeZone instanceof java.util.TimeZone)
                    appropriateTimeZone = (java.util.TimeZone) usertimeZone;
                else throw new IllegalArgumentException("TimeZone could be either String or java.util.TimeZone");
            } else {
                appropriateTimeZone = java.util.TimeZone.getDefault();
            }
        }

        return appropriateTimeZone;
    }

    public java.lang.String getBeforeShowDay() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.beforeShowDay, null);
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, null);
    }

    public java.lang.String getEffectDuration() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effectDuration, "normal");
    }

    @Override
    public Collection<String> getEventNames() {
        return Calendar.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Calendar.COMPONENT_FAMILY;
    }

    @Override
    public String getInputClientId() {
        return this.getClientId(getFacesContext()) + "_input";
    }

    public java.lang.Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public java.lang.Object getMaxdate() {
        return getStateHelper().eval(PropertyKeys.maxdate, null);
    }

    public int getMaxHour() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxHour, 23);
    }

    public int getMaxMinute() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxMinute, 59);
    }

    public int getMaxSecond() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxSecond, 59);
    }

    public java.lang.Object getMindate() {
        return getStateHelper().eval(PropertyKeys.mindate, null);
    }

    public int getMinHour() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minHour, 0);
    }

    public int getMinMinute() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minMinute, 0);
    }

    public int getMinSecond() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minSecond, 0);
    }

    public java.lang.String getMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.mode, "popup");
    }

    public java.lang.Object getPagedate() {
        return getStateHelper().eval(PropertyKeys.pagedate, null);
    }

    public int getPages() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.pages, 1);
    }

    public java.lang.String getPattern() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.pattern, null);
    }

    public java.lang.String getShowOn() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.showOn, "focus");
    }

    public int getStepHour() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.stepHour, 1);
    }

    public int getStepMinute() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.stepMinute, 1);
    }

    public int getStepSecond() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.stepSecond, 1);
    }

    public java.lang.Object getTimeZone() {
        return getStateHelper().eval(PropertyKeys.timeZone, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public java.lang.String getYearRange() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.yearRange, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Calendar.OPTIMIZED_PACKAGE)) {
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

    public boolean hasTime() {
        final String pattern = getPattern();

        return (pattern != null && pattern.indexOf(":") != -1);
    }

    public boolean isConversionFailed() {
        return conversionFailed;
    }

    public boolean isDisabledWeekends() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabledWeekends, false);
    }

    public boolean isNavigator() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.navigator, false);
    }

    public boolean isPopup() {
        return getMode().equalsIgnoreCase("popup");
    }

    public boolean isReadonlyInput() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.readonlyInput, false);
    }

    public boolean isSelectOtherMonths() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.selectOtherMonths, false);
    }

    public boolean isShowButtonPanel() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showButtonPanel, false);
    }

    public boolean isShowOtherMonths() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showOtherMonths, false);
    }

    public boolean isShowWeek() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showWeek, false);
    }

    public boolean isTimeOnly() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.timeOnly, false);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final String eventName = context.getExternalContext().getRequestParameterMap()
            .get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && eventName.equals("dateSelect") && event instanceof AjaxBehaviorEvent) {
            customEvents.put("dateSelect", (AjaxBehaviorEvent) event);
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

    public void setBeforeShowDay(final java.lang.String _beforeShowDay) {
        getStateHelper().put(PropertyKeys.beforeShowDay, _beforeShowDay);
        handleAttribute("beforeShowDay", _beforeShowDay);
    }

    public void setConversionFailed(final boolean value) {
        conversionFailed = value;
    }

    public void setDisabledWeekends(final boolean _disabledWeekends) {
        getStateHelper().put(PropertyKeys.disabledWeekends, _disabledWeekends);
        handleAttribute("disabledWeekends", _disabledWeekends);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectDuration(final java.lang.String _effectDuration) {
        getStateHelper().put(PropertyKeys.effectDuration, _effectDuration);
        handleAttribute("effectDuration", _effectDuration);
    }

    public void setLocale(final java.lang.Object _locale) {
        getStateHelper().put(PropertyKeys.locale, _locale);
        handleAttribute("locale", _locale);
    }

    public void setMaxdate(final java.lang.Object _maxdate) {
        getStateHelper().put(PropertyKeys.maxdate, _maxdate);
        handleAttribute("maxdate", _maxdate);
    }

    public void setMaxHour(final int _maxHour) {
        getStateHelper().put(PropertyKeys.maxHour, _maxHour);
        handleAttribute("maxHour", _maxHour);
    }

    public void setMaxMinute(final int _maxMinute) {
        getStateHelper().put(PropertyKeys.maxMinute, _maxMinute);
        handleAttribute("maxMinute", _maxMinute);
    }

    public void setMaxSecond(final int _maxSecond) {
        getStateHelper().put(PropertyKeys.maxSecond, _maxSecond);
        handleAttribute("maxSecond", _maxSecond);
    }

    public void setMindate(final java.lang.Object _mindate) {
        getStateHelper().put(PropertyKeys.mindate, _mindate);
        handleAttribute("mindate", _mindate);
    }

    public void setMinHour(final int _minHour) {
        getStateHelper().put(PropertyKeys.minHour, _minHour);
        handleAttribute("minHour", _minHour);
    }

    public void setMinMinute(final int _minMinute) {
        getStateHelper().put(PropertyKeys.minMinute, _minMinute);
        handleAttribute("minMinute", _minMinute);
    }

    public void setMinSecond(final int _minSecond) {
        getStateHelper().put(PropertyKeys.minSecond, _minSecond);
        handleAttribute("minSecond", _minSecond);
    }

    public void setMode(final java.lang.String _mode) {
        getStateHelper().put(PropertyKeys.mode, _mode);
        handleAttribute("mode", _mode);
    }

    public void setNavigator(final boolean _navigator) {
        getStateHelper().put(PropertyKeys.navigator, _navigator);
        handleAttribute("navigator", _navigator);
    }

    public void setPagedate(final java.lang.Object _pagedate) {
        getStateHelper().put(PropertyKeys.pagedate, _pagedate);
        handleAttribute("pagedate", _pagedate);
    }

    public void setPages(final int _pages) {
        getStateHelper().put(PropertyKeys.pages, _pages);
        handleAttribute("pages", _pages);
    }

    public void setPattern(final java.lang.String _pattern) {
        getStateHelper().put(PropertyKeys.pattern, _pattern);
        handleAttribute("pattern", _pattern);
    }

    public void setReadonlyInput(final boolean _readonlyInput) {
        getStateHelper().put(PropertyKeys.readonlyInput, _readonlyInput);
        handleAttribute("readonlyInput", _readonlyInput);
    }

    public void setSelectOtherMonths(final boolean _selectOtherMonths) {
        getStateHelper().put(PropertyKeys.selectOtherMonths, _selectOtherMonths);
        handleAttribute("selectOtherMonths", _selectOtherMonths);
    }

    public void setShowButtonPanel(final boolean _showButtonPanel) {
        getStateHelper().put(PropertyKeys.showButtonPanel, _showButtonPanel);
        handleAttribute("showButtonPanel", _showButtonPanel);
    }

    public void setShowOn(final java.lang.String _showOn) {
        getStateHelper().put(PropertyKeys.showOn, _showOn);
        handleAttribute("showOn", _showOn);
    }

    public void setShowOtherMonths(final boolean _showOtherMonths) {
        getStateHelper().put(PropertyKeys.showOtherMonths, _showOtherMonths);
        handleAttribute("showOtherMonths", _showOtherMonths);
    }

    public void setShowWeek(final boolean _showWeek) {
        getStateHelper().put(PropertyKeys.showWeek, _showWeek);
        handleAttribute("showWeek", _showWeek);
    }

    public void setStepHour(final int _stepHour) {
        getStateHelper().put(PropertyKeys.stepHour, _stepHour);
        handleAttribute("stepHour", _stepHour);
    }

    public void setStepMinute(final int _stepMinute) {
        getStateHelper().put(PropertyKeys.stepMinute, _stepMinute);
        handleAttribute("stepMinute", _stepMinute);
    }

    public void setStepSecond(final int _stepSecond) {
        getStateHelper().put(PropertyKeys.stepSecond, _stepSecond);
        handleAttribute("stepSecond", _stepSecond);
    }

    public void setTimeOnly(final boolean _timeOnly) {
        getStateHelper().put(PropertyKeys.timeOnly, _timeOnly);
        handleAttribute("timeOnly", _timeOnly);
    }

    public void setTimeZone(final java.lang.Object _timeZone) {
        getStateHelper().put(PropertyKeys.timeZone, _timeZone);
        handleAttribute("timeZone", _timeZone);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }

    public void setYearRange(final java.lang.String _yearRange) {
        getStateHelper().put(PropertyKeys.yearRange, _yearRange);
        handleAttribute("yearRange", _yearRange);
    }

    @Override
    public void validate(final FacesContext context) {
        super.validate(context);

        if (isValid()) {
            for (final String string : customEvents.keySet()) {
                final AjaxBehaviorEvent behaviorEvent = customEvents.get(string);
                final SelectEvent selectEvent = new SelectEvent(this, behaviorEvent.getBehavior(), getValue());

                if (behaviorEvent.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
                    selectEvent.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
                } else {
                    selectEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
                }

                super.queueEvent(selectEvent);
            }
        }
    }
}