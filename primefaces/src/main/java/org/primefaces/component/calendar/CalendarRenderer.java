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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.MessageFactory;
import org.primefaces.util.WidgetBuilder;

public class CalendarRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Calendar calendar = (Calendar) component;

        if (calendar.isDisabled() || calendar.isReadonly()) {
            return;
        }

        final String param = calendar.getClientId(context) + "_input";
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(param);

        if (submittedValue != null) {
            calendar.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(context, calendar);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Calendar calendar = (Calendar) component;
        final String markupValue = CalendarUtils.getValueAsString(context, calendar);
        final String widgetValue = calendar.isTimeOnly()
                                                        ? CalendarUtils.getTimeOnlyValueAsString(context, calendar)
                                                        : markupValue;

        encodeMarkup(context, calendar, markupValue);
        encodeScript(context, calendar, widgetValue);
    }

    protected void encodeInput(final FacesContext context,
                               final Calendar calendar,
                               final String id,
                               final String value,
                               final boolean popup) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String type = popup ? "text" : "hidden";
        final boolean disabled = calendar.isDisabled();

        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("type", type, null);

        if (!isValueBlank(value)) {
            writer.writeAttribute("value", value, null);
        }

        if (popup) {
            String inputStyleClass = Calendar.INPUT_STYLE_CLASS;
            if (disabled) inputStyleClass = inputStyleClass + " ui-state-disabled";
            if (!calendar.isValid()) inputStyleClass = inputStyleClass + " ui-state-error";

            writer.writeAttribute("class", inputStyleClass, null);

            renderPassThruAttributes(context, calendar, HTML.INPUT_TEXT_ATTRS);

            if (calendar.isReadonly() || calendar.isReadonlyInput())
                writer.writeAttribute("readonly", "readonly", null);
            if (calendar.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        }

        writer.endElement("input");
    }

    protected void encodeMarkup(final FacesContext context, final Calendar calendar, final String value)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = calendar.getClientId(context);
        final String inputId = clientId + "_input";
        final boolean popup = calendar.isPopup();

        writer.startElement("span", calendar);
        writer.writeAttribute("id", clientId, null);
        if (calendar.getStyle() != null) writer.writeAttribute("style", calendar.getStyle(), null);
        if (calendar.getStyleClass() != null) writer.writeAttribute("class", calendar.getStyleClass(), null);

        // inline container
        if (!popup) {
            writer.startElement("div", null);
            writer.writeAttribute("id", clientId + "_inline", null);
            writer.endElement("div");
        }

        // input
        encodeInput(context, calendar, inputId, value, popup);

        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext context, final Calendar calendar, final String value)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = calendar.getClientId(context);
        final Locale locale = calendar.calculateLocale(context);
        final String pattern = calendar.isTimeOnly() ? calendar.calculateTimeOnlyPattern() : calendar
            .calculatePattern();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Calendar", calendar.resolveWidgetVar(), clientId, true);

        wb.attr("popup", calendar.isPopup()).attr("locale", locale.toString()).attr("dateFormat",
                                                                                    CalendarUtils
                                                                                        .convertPattern(pattern));

        // default date
        final Object pagedate = calendar.getPagedate();
        String defaultDate = null;

        if (calendar.isConversionFailed()) {
            defaultDate = CalendarUtils.getValueAsString(context, calendar, new Date());
        } else if (!isValueBlank(value)) {
            defaultDate = value;
        } else if (pagedate != null) {
            defaultDate = CalendarUtils.getValueAsString(context, calendar, pagedate);
        }

        wb.attr("defaultDate", defaultDate, null).attr("numberOfMonths", calendar.getPages(), 1)
            .attr("minDate", CalendarUtils.getValueAsString(context, calendar, calendar.getMindate()), null)
            .attr("maxDate", CalendarUtils.getValueAsString(context, calendar, calendar.getMaxdate()), null)
            .attr("showButtonPanel", calendar.isShowButtonPanel(), false)
            .attr("showWeek", calendar.isShowWeek(), false).attr("disabledWeekends", calendar.isDisabledWeekends(),
                                                                 false).attr("disabled", calendar.isDisabled(), false)
            .attr("yearRange", calendar.getYearRange(), null);

        if (calendar.isNavigator()) {
            wb.attr("changeMonth", true).attr("changeYear", true);
        }

        if (calendar.getEffect() != null) {
            wb.attr("showAnim", calendar.getEffect()).attr("duration", calendar.getEffectDuration());
        }

        final String beforeShowDay = calendar.getBeforeShowDay();
        if (beforeShowDay != null) {
            wb.nativeAttr("preShowDay", beforeShowDay);
        }

        final String showOn = calendar.getShowOn();
        if (!showOn.equalsIgnoreCase("focus")) {
            wb.attr("showOn", showOn);
        }

        if (calendar.isShowOtherMonths()) {
            wb.attr("showOtherMonths", true).attr("selectOtherMonths", true);
        }

        if (calendar.hasTime()) {
            wb.attr("timeOnly", calendar.isTimeOnly()).attr("stepHour", calendar.getStepHour())
                .attr("stepMinute", calendar.getStepMinute()).attr("stepSecond", calendar.getStepSecond())
                .attr("hourMin", calendar.getMinHour()).attr("hourMax", calendar.getMaxHour())
                .attr("minuteMin", calendar.getMinMinute()).attr("minuteMax", calendar.getMaxMinute())
                .attr("secondMin", calendar.getMinSecond()).attr("secondMax", calendar.getMaxSecond());
        }

        encodeClientBehaviors(context, calendar, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object value)
        throws ConverterException {
        final Calendar calendar = (Calendar) component;
        final String submittedValue = (String) value;
        final Converter converter = calendar.getConverter();

        if (isValueBlank(submittedValue)) {
            return null;
        }

        // Delegate to user supplied converter if defined
        if (converter != null) {
            return converter.getAsObject(context, calendar, submittedValue);
        }

        // Use built-in converter
        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat(calendar.calculatePattern(), calendar.calculateLocale(context));
            format.setTimeZone(calendar.calculateTimeZone());

            final Object date = format.parse(submittedValue);

            return date;
        } catch (final ParseException e) {
            calendar.setConversionFailed(true);

            FacesMessage message = null;
            final Object[] params = new Object[3];
            params[0] = submittedValue;
            params[1] = format.format(new Date());
            params[2] = MessageFactory.getLabel(context, calendar);

            if (calendar.isTimeOnly()) {
                message = MessageFactory.getMessage("javax.faces.converter.DateTimeConverter.TIME",
                                                    FacesMessage.SEVERITY_ERROR, params);
            } else if (calendar.hasTime()) {
                message = MessageFactory.getMessage("javax.faces.converter.DateTimeConverter.DATETIME",
                                                    FacesMessage.SEVERITY_ERROR, params);
            } else {
                message = MessageFactory.getMessage("javax.faces.converter.DateTimeConverter.DATE",
                                                    FacesMessage.SEVERITY_ERROR, params);
            }

            throw new ConverterException(message);
        }
    }
}
