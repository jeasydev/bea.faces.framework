/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.timepicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
import org.apache.commons.lang3.LocaleUtils;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.event.BeforeShowEvent;
import org.primefaces.extensions.event.CloseEvent;
import org.primefaces.extensions.event.TimeSelectEvent;
import org.primefaces.util.ArrayUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.HTML;

/**
 * <code>TimePicker</code> component.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"), @ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.css"), @ResourceDependency(library = "primefaces-extensions", name = "timepicker/timepicker.js") })
public class TimePicker extends HtmlInputText implements Widget {

    /**
     * PropertyKeys
     * 
     * @author oleg / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        widgetVar,
        timeSeparator,
        showPeriod,
        dialogPosition,
        inputPosition,
        mode /* 'popup', 'spinner', 'inline' */,
        startHours,
        endHours,
        startMinutes,
        endMinutes,
        intervalMinutes,
        rows,
        showHours,
        showMinutes,
        showCloseButton,
        showNowButton,
        showDeselectButton,
        onHourShow,
        onMinuteShow,
        showOn,
        locale;

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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.TimePicker";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.TimePickerRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";
    public static final String CONTAINER_CLASS = "pe-timepicker ui-widget ui-corner-all";
    public static final String INPUT_CLASS = "ui-inputfield pe-timepicker-input ui-state-default ui-corner-all";
    public static final String UP_BUTTON_CLASS = "pe-timepicker-button pe-timepicker-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only";
    public static final String DOWN_BUTTON_CLASS = "pe-timepicker-button pe-timepicker-down ui-corner-br ui-button ui-widget ui-state-default ui-button-text-only";
    public static final String UP_ICON_CLASS = "ui-icon ui-icon-triangle-1-n";
    public static final String DOWN_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";
    public static final String BUTTON_TRIGGER_CLASS = "pe-timepicker-trigger ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only";
    public static final String BUTTON_TRIGGER_ICON_CLASS = "ui-button-icon-left ui-icon ui-icon-clock";

    public static final String BUTTON_TRIGGER_TEXT_CLASS = "ui-button-text";

    public static final String[] INPUT_TEXT_ATTRS = ArrayUtils
        .concat(new String[] { "accesskey", "alt", "autocomplete", "dir", "lang", "maxlength", "size", "tabindex", "title" },
                HTML.COMMON_EVENTS, HTML.CHANGE_SELECT_EVENTS, HTML.BLUR_FOCUS_EVENTS);

    public static final String TIME_MESSAGE_KEY = "javax.faces.converter.DateTimeConverter.TIME";

    private Locale appropriateLocale;

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList("blur", "change", "valueChange", "click", "dblclick", "focus", "keydown", "keypress", "keyup",
                "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "beforeShow", "timeSelect", "close"));

    private final Map<String, AjaxBehaviorEvent> customEvents = new HashMap<String, AjaxBehaviorEvent>();

    public TimePicker() {
        setRendererType(TimePicker.DEFAULT_RENDERER);
    }

    public Locale calculateLocale(final FacesContext fc) {
        if (appropriateLocale == null) {
            final Object userLocale = getLocale();
            if (userLocale != null) {
                if (userLocale instanceof String) {
                    appropriateLocale = LocaleUtils.toLocale((String) userLocale);
                } else if (userLocale instanceof java.util.Locale) {
                    appropriateLocale = (Locale) userLocale;
                } else {
                    throw new IllegalArgumentException(userLocale.getClass() + " is not a valid locale for timepicker:"
                        + this.getClientId(fc));
                }
            } else {
                appropriateLocale = fc.getViewRoot().getLocale();
            }
        }

        return appropriateLocale;
    }

    public String getDialogPosition() {
        return (String) getStateHelper().eval(PropertyKeys.dialogPosition, "left top");
    }

    public int getEndHours() {
        return (Integer) getStateHelper().eval(PropertyKeys.endHours, 23);
    }

    public int getEndMinutes() {
        return (Integer) getStateHelper().eval(PropertyKeys.endMinutes, 55);
    }

    @Override
    public Collection<String> getEventNames() {
        return TimePicker.EVENT_NAMES;
    }

    @Override
    public String getFamily() {
        return TimePicker.COMPONENT_FAMILY;
    }

    public String getInputPosition() {
        return (String) getStateHelper().eval(PropertyKeys.inputPosition, "left bottom");
    }

    public int getIntervalMinutes() {
        return (Integer) getStateHelper().eval(PropertyKeys.intervalMinutes, 5);
    }

    public Object getLocale() {
        return getStateHelper().eval(PropertyKeys.locale, null);
    }

    public String getMode() {
        return (String) getStateHelper().eval(PropertyKeys.mode, "spinner");
    }

    public String getOnHourShow() {
        return (String) getStateHelper().eval(PropertyKeys.onHourShow, null);
    }

    public String getOnMinuteShow() {
        return (String) getStateHelper().eval(PropertyKeys.onMinuteShow, null);
    }

    public int getRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.rows, 4);
    }

    public String getShowOn() {
        return (String) getStateHelper().eval(PropertyKeys.showOn, "focus");
    }

    public int getStartHours() {
        return (Integer) getStateHelper().eval(PropertyKeys.startHours, 0);
    }

    public int getStartMinutes() {
        return (Integer) getStateHelper().eval(PropertyKeys.startMinutes, 0);
    }

    public String getTimePattern12() {
        return "hh" + getTimeSeparator() + "mm a";
    }

    public String getTimePattern24() {
        return "HH" + getTimeSeparator() + "mm";
    }

    public String getTimeSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.timeSeparator, ":");
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public boolean isInline() {
        return getMode().equalsIgnoreCase("inline");
    }

    private boolean isSelfRequest(final FacesContext fc) {
        return this.getClientId(fc).equals(fc.getExternalContext().getRequestParameterMap()
                                               .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isShowCloseButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showCloseButton, false);
    }

    public boolean isShowDeselectButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showDeselectButton, false);
    }

    public boolean isShowHours() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showHours, true);
    }

    public boolean isShowMinutes() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showMinutes, true);
    }

    public boolean isShowNowButton() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showNowButton, false);
    }

    public boolean isShowPeriod() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showPeriod, false);
    }

    public boolean isSpinner() {
        return getMode().equalsIgnoreCase("spinner");
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final String eventName = fc.getExternalContext().getRequestParameterMap()
            .get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (isSelfRequest(fc) && event instanceof AjaxBehaviorEvent) {
            if ("timeSelect".equals(eventName)) {
                customEvents.put("timeSelect", (AjaxBehaviorEvent) event);

                return;
            } else if ("beforeShow".equals(eventName)) {
                final BeforeShowEvent beforeShowEvent = new BeforeShowEvent(this, ((AjaxBehaviorEvent) event)
                    .getBehavior());
                beforeShowEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(beforeShowEvent);

                return;
            } else if ("close".equals(eventName)) {
                final CloseEvent closeEvent = new CloseEvent(this, ((AjaxBehaviorEvent) event).getBehavior());
                closeEvent.setPhaseId(event.getPhaseId());
                super.queueEvent(closeEvent);

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
            if (cname != null && cname.startsWith(TimePicker.OPTIMIZED_PACKAGE)) {
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

    public void setDialogPosition(final String dialogPosition) {
        setAttribute(PropertyKeys.dialogPosition, dialogPosition);
    }

    public void setEndHours(final int endHours) {
        setAttribute(PropertyKeys.endHours, endHours);
    }

    public void setEndMinutes(final int endMinutes) {
        setAttribute(PropertyKeys.endMinutes, endMinutes);
    }

    public void setInputPosition(final String inputPosition) {
        setAttribute(PropertyKeys.inputPosition, inputPosition);
    }

    public void setIntervalMinutes(final int intervalMinutes) {
        setAttribute(PropertyKeys.intervalMinutes, intervalMinutes);
    }

    public void setLocale(final Object locale) {
        setAttribute(PropertyKeys.locale, locale);
    }

    public void setMode(final String mode) {
        setAttribute(PropertyKeys.mode, mode);
    }

    public void setOnHourShow(final String onHourShow) {
        setAttribute(PropertyKeys.onHourShow, onHourShow);
    }

    public void setOnMinuteShow(final String onMinuteShow) {
        setAttribute(PropertyKeys.onMinuteShow, onMinuteShow);
    }

    public void setRows(final int rows) {
        setAttribute(PropertyKeys.rows, rows);
    }

    public void setShowCloseButton(final boolean showCloseButton) {
        setAttribute(PropertyKeys.showCloseButton, showCloseButton);
    }

    public void setShowDeselectButton(final boolean showDeselectButton) {
        setAttribute(PropertyKeys.showDeselectButton, showDeselectButton);
    }

    public void setShowHours(final boolean showHours) {
        setAttribute(PropertyKeys.showHours, showHours);
    }

    public void setShowMinutes(final boolean showMinutes) {
        setAttribute(PropertyKeys.showMinutes, showMinutes);
    }

    public void setShowNowButton(final boolean showNowButton) {
        setAttribute(PropertyKeys.showNowButton, showNowButton);
    }

    public void setShowOn(final String showOn) {
        setAttribute(PropertyKeys.showOn, showOn);
    }

    public void setShowPeriod(final boolean showPeriod) {
        setAttribute(PropertyKeys.showPeriod, showPeriod);
    }

    public void setStartHours(final int startHours) {
        setAttribute(PropertyKeys.startHours, startHours);
    }

    public void setStartMinutes(final int startMinutes) {
        setAttribute(PropertyKeys.startMinutes, startMinutes);
    }

    public void setTimeSeparator(final String timeSeparator) {
        setAttribute(PropertyKeys.timeSeparator, timeSeparator);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }

    @Override
    public void validate(final FacesContext fc) {
        super.validate(fc);

        if (isValid()) {
            for (final String string : customEvents.keySet()) {
                final AjaxBehaviorEvent behaviorEvent = customEvents.get(string);
                final TimeSelectEvent timeSelectEvent = new TimeSelectEvent(this,
                                                                            behaviorEvent.getBehavior(),
                                                                            (Date) getValue());

                if (behaviorEvent.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
                    timeSelectEvent.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
                }

                super.queueEvent(timeSelectEvent);
            }
        }
    }
}
