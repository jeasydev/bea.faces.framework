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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.util.MessageUtils;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.MessageFactory;

/**
 * Renderer for the {@link TimePicker} component.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class TimePickerRenderer extends InputRenderer {

    protected static String getValueAsString(final FacesContext fc, final TimePicker timepicker) {
        final Object submittedValue = timepicker.getSubmittedValue();
        if (submittedValue != null) {
            return submittedValue.toString();
        }

        final Object value = timepicker.getValue();
        if (value == null) {
            return null;
        } else {
            if (timepicker.getConverter() != null) {
                // convert via registered converter
                return timepicker.getConverter().getAsString(fc, timepicker, value);
            } else {
                // use built-in converter
                SimpleDateFormat timeFormat;
                if (timepicker.isShowPeriod()) {
                    timeFormat = new SimpleDateFormat(timepicker.getTimePattern12(), timepicker.calculateLocale(fc));
                } else {
                    timeFormat = new SimpleDateFormat(timepicker.getTimePattern24(), timepicker.calculateLocale(fc));
                }

                return timeFormat.format(value);
            }
        }
    }

    @Override
    public void decode(final FacesContext fc, final UIComponent component) {
        final TimePicker timepicker = (TimePicker) component;

        if (timepicker.isDisabled() || timepicker.isReadonly()) {
            return;
        }

        final String param = timepicker.getClientId(fc) + "_input";
        final String submittedValue = fc.getExternalContext().getRequestParameterMap().get(param);

        if (submittedValue != null) {
            timepicker.setSubmittedValue(submittedValue);
        }

        decodeBehaviors(fc, timepicker);
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final TimePicker timepicker = (TimePicker) component;
        final String value = TimePickerRenderer.getValueAsString(fc, timepicker);

        encodeMarkup(fc, timepicker, value);
        encodeScript(fc, timepicker, value);
    }

    protected void encodeMarkup(final FacesContext fc, final TimePicker timepicker, final String value)
        throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = timepicker.getClientId(fc);
        final String inputId = clientId + "_input";

        writer.startElement("span", timepicker);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", TimePicker.CONTAINER_CLASS, null);

        if (timepicker.isInline()) {
            // inline container
            writer.startElement("div", null);
            writer.writeAttribute("id", clientId + "_inline", null);
            writer.endElement("div");
        }

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", timepicker.isInline() ? "hidden" : "text", null);
        writer.writeAttribute("autocomplete", "off", null);

        if (StringUtils.isNotBlank(value)) {
            writer.writeAttribute("value", value, null);
        }

        if (!timepicker.isInline()) {
            String styleClass = timepicker.getStyleClass();
            styleClass = (styleClass == null ? TimePicker.INPUT_CLASS : TimePicker.INPUT_CLASS + " " + styleClass);
            if (!timepicker.isValid()) {
                styleClass = styleClass + " ui-state-error";
            }

            writer.writeAttribute("class", styleClass, null);

            if (timepicker.getStyle() != null) {
                writer.writeAttribute("style", timepicker.getStyle(), null);
            }

            renderPassThruAttributes(fc, timepicker, TimePicker.INPUT_TEXT_ATTRS);
        }

        writer.endElement("input");

        if (timepicker.isSpinner()) {
            final boolean disabled = timepicker.isDisabled() || timepicker.isReadonly();
            encodeSpinnerButton(fc, TimePicker.UP_BUTTON_CLASS, TimePicker.UP_ICON_CLASS, disabled);
            encodeSpinnerButton(fc, TimePicker.DOWN_BUTTON_CLASS, TimePicker.DOWN_ICON_CLASS, disabled);
        }

        if (!"focus".equals(timepicker.getShowOn())) {
            writer.startElement("button", null);
            writer.writeAttribute("class", TimePicker.BUTTON_TRIGGER_CLASS, null);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("role", "button", null);

            writer.startElement("span", null);
            writer.writeAttribute("class", TimePicker.BUTTON_TRIGGER_ICON_CLASS, null);
            writer.endElement("span");

            writer.startElement("span", null);
            writer.writeAttribute("class", TimePicker.BUTTON_TRIGGER_TEXT_CLASS, null);
            writer.write("ui-button");
            writer.endElement("span");

            writer.endElement("button");
        }

        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext fc, final TimePicker timepicker, final String value)
        throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = timepicker.getClientId(fc);

        startScript(writer, clientId);
        writer.write("$(function(){");

        writer.write("PrimeFacesExt.cw('TimePicker', '" + timepicker.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",timeSeparator:'" + timepicker.getTimeSeparator() + "'");
        writer.write(",myPosition:'" + timepicker.getDialogPosition() + "'");
        writer.write(",atPosition:'" + timepicker.getInputPosition() + "'");
        writer.write(",showPeriod:" + timepicker.isShowPeriod());
        writer.write(",showPeriodLabels:" + (timepicker.isShowPeriod() ? "true" : "false"));
        writer.write(",modeInline:" + timepicker.isInline());
        writer.write(",modeSpinner:" + timepicker.isSpinner());
        writer.write(",hours:{starts:" + timepicker.getStartHours() + ",ends:" + timepicker.getEndHours() + "}");
        writer.write(",minutes:{starts:" + timepicker.getStartMinutes() + ",ends:" + timepicker.getEndMinutes()
            + ",interval:" + timepicker.getIntervalMinutes() + "}");
        writer.write(",rows:" + timepicker.getRows());
        writer.write(",showHours:" + timepicker.isShowHours());
        writer.write(",showMinutes:" + timepicker.isShowMinutes());
        writer.write(",showCloseButton:" + timepicker.isShowCloseButton());
        writer.write(",showNowButton:" + timepicker.isShowNowButton());
        writer.write(",showDeselectButton:" + timepicker.isShowDeselectButton());

        if (timepicker.getOnHourShow() != null) {
            writer.write(",onHourShow:" + timepicker.getOnHourShow());
        }

        if (timepicker.getOnMinuteShow() != null) {
            writer.write(",onMinuteShow:" + timepicker.getOnMinuteShow());
        }

        if (!"focus".equals(timepicker.getShowOn())) {
            writer.write(",showOn:'" + timepicker.getShowOn() + "'");
            writer.write(",button:'" + org.primefaces.util.ComponentUtils.escapeJQueryId(clientId)
                + " .pe-timepicker-trigger'");
        }

        writer.write(",locale:'" + timepicker.calculateLocale(fc).toString() + "'");
        writer.write(",disabled:" + (timepicker.isDisabled() || timepicker.isReadonly()));

        if (StringUtils.isBlank(value)) {
            writer.write(",defaultTime:''");
        } else if (timepicker.isInline()) {
            writer.write(",defaultTime:'" + value + "'");
        }

        encodeClientBehaviors(fc, timepicker);

        writer.write("},true);});");
        endScript(writer);
    }

    protected void encodeSpinnerButton(final FacesContext fc,
                                       String styleClass,
                                       final String iconClass,
                                       final boolean disabled) throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

        writer.startElement("a", null);

        writer.writeAttribute("class", styleClass, null);
        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-button-text", null);
        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");
        writer.endElement("span");

        writer.endElement("a");
    }

    @Override
    public Object getConvertedValue(final FacesContext fc, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        final String value = (String) submittedValue;
        if (StringUtils.isBlank(value)) {
            return null;
        }

        final TimePicker timepicker = (TimePicker) component;
        final Converter converter = timepicker.getConverter();

        // first ask the converter
        if (converter != null) {
            return converter.getAsObject(fc, timepicker, value);
        }

        // Try to guess
        /*
         * else { Class<?> valueType =
         * timepicker.getValueExpression("value").getType
         * (context.getELContext()); Converter converterForType =
         * context.getApplication().createConverter(valueType); if
         * (converterForType != null) { return
         * converterForType.getAsObject(context, timepicker, value); } }
         */

        // use built-in conversion
        SimpleDateFormat timeFormat = null;
        try {
            if (timepicker.isShowPeriod()) {
                timeFormat = new SimpleDateFormat(timepicker.getTimePattern12(), timepicker.calculateLocale(fc));
            } else {
                timeFormat = new SimpleDateFormat(timepicker.getTimePattern24(), timepicker.calculateLocale(fc));
            }

            return timeFormat.parse(value);
        } catch (final ParseException e) {
            throw new ConverterException(MessageUtils.getMessage(timepicker.calculateLocale(fc),
                                                                 TimePicker.TIME_MESSAGE_KEY, value, timeFormat
                                                                     .format(new Date(System.currentTimeMillis())),
                                                                 MessageFactory.getLabel(fc, component)), e);
        } catch (final Exception e) {
            throw new ConverterException(e);
        }
    }
}
