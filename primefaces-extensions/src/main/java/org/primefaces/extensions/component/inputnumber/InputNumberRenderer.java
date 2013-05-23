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

package org.primefaces.extensions.component.inputnumber;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

/**
 * InputNumberRenderer
 * 
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class InputNumberRenderer extends InputRenderer {

    private static final Logger LOGGER = Logger.getLogger(InputNumberRenderer.class.getName());

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final InputNumber inputNumber = (InputNumber) component;

        if (inputNumber.isDisabled() || inputNumber.isReadonly()) {
            return;
        }

        decodeBehaviors(context, inputNumber);

        final String inputId = inputNumber.getClientId(context) + "_hinput";
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(inputId);

        if (submittedValue != null) {
            inputNumber.setSubmittedValue(submittedValue);
        }

    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final InputNumber inputNumber = (InputNumber) component;
        encodeMarkup(context, inputNumber);
        encodeScript(context, inputNumber);
    }

    protected void encodeInput(final FacesContext context, final InputNumber inputNumber, final String clientId)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String inputId = clientId + "_hinput";

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);

        if (inputNumber.getOnchange() != null) {
            writer.writeAttribute("onchange", inputNumber.getOnchange(), null);
        }

        writer.endElement("input");

    }

    protected void encodeMarkup(final FacesContext context, final InputNumber inputNumber) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputNumber.getClientId(context);

        String styleClass = inputNumber.getStyleClass();
        styleClass = styleClass == null ? InputNumber.INPUTNUMBER_CLASS : InputNumber.INPUTNUMBER_CLASS + " "
            + styleClass;

        writer.startElement("span", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");

        encodeOutput(context, inputNumber, clientId);
        encodeInput(context, inputNumber, clientId);

        writer.endElement("span");
    }

    protected void encodeOutput(final FacesContext context, final InputNumber inputNumber, final String clientId)
        throws IOException {

        final ResponseWriter writer = context.getResponseWriter();
        final String inputId = clientId + "_input";

        String defaultClass = InputText.STYLE_CLASS;
        defaultClass = inputNumber.isValid() ? defaultClass : defaultClass + " ui-state-error";
        defaultClass = !inputNumber.isDisabled() ? defaultClass : defaultClass + " ui-state-disabled";

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", "text", null);

        renderPassThruAttributes(context, inputNumber, HTML.INPUT_TEXT_ATTRS);

        if (inputNumber.isReadonly()) {
            writer.writeAttribute("readonly", "readonly", "readonly");
        }
        if (inputNumber.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }
        if (inputNumber.getStyle() != null) {
            writer.writeAttribute("style", inputNumber.getStyle(), "style");
        }

        writer.writeAttribute("class", defaultClass, "");

        writer.endElement("input");
    }

    protected void encodeScript(final FacesContext context, final InputNumber inputNumber) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputNumber.getClientId(context);
        startScript(writer, clientId);
        String valueToRender = ComponentUtils.getValueToRender(context, inputNumber);
        if (valueToRender == null) {
            valueToRender = "";
        }

        writer.write("$(function() {");
        writer.write("PrimeFacesExt.cw('InputNumber','" + inputNumber.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",disabled:" + inputNumber.isDisabled());
        writer.write(",valueToRender:'" + formatForPlugin(valueToRender, inputNumber) + "'");

        final String metaOptions = getOptions(inputNumber);
        if (!metaOptions.isEmpty()) {
            writer.write(",pluginOptions:" + metaOptions);
        }
        encodeClientBehaviors(context, inputNumber);
        writer.write("});});");

        endScript(writer);
    }

    private String formatForPlugin(final String valueToRender, final InputNumber inputNumber) {

        if (valueToRender == null || valueToRender.isEmpty()) {
            return "";
        } else {

            try {
                Object objectToRender;
                if (inputNumber.getValue() instanceof BigDecimal) {
                    objectToRender = new BigDecimal(valueToRender);
                } else {
                    objectToRender = new Double(valueToRender);
                }

                final NumberFormat formatter = new DecimalFormat("#0.0#");
                formatter.setRoundingMode(RoundingMode.FLOOR);
                // autoNumeric jquery plugin max and min limits
                formatter.setMinimumFractionDigits(15);
                formatter.setMaximumFractionDigits(15);
                formatter.setMaximumIntegerDigits(20);
                String f = formatter.format(objectToRender);

                // force to english decimal separator
                f = f.replace(',', '.');
                return f;
            } catch (final Exception e) {
                throw new IllegalArgumentException("Error converting  [" + valueToRender + "] to a double value;", e);
            }
        }
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {

        final InputNumber inputNumber = (InputNumber) component;
        final Converter converter = inputNumber.getConverter();
        final String submittedValueString = (String) submittedValue;

        if (converter != null) {
            final Object doubleConverted = converter.getAsObject(context, inputNumber, submittedValueString);
            return doubleConverted;
        } else {
            if (submittedValueString != null && !submittedValueString.isEmpty()) {
                if (inputNumber.getValue() instanceof BigDecimal) {
                    return new BigDecimal(submittedValueString);
                } else {
                    return new Double(submittedValueString);
                }
            }
            return null;
        }
    }

    private String getOptions(final InputNumber inputNumber) {

        final String decimalSeparator = inputNumber.getDecimalSeparator();
        final String thousandSeparator = inputNumber.getThousandSeparator();
        final String symbol = inputNumber.getSymbol();
        final String symbolPosition = inputNumber.getSymbolPosition();
        final String minValue = inputNumber.getMinValue();
        final String maxValue = inputNumber.getMaxValue();
        final String roundMethod = inputNumber.getRoundMethod();
        final String decimalPlaces = inputNumber.getDecimalPlaces();
        final String emptyValue = inputNumber.getEmptyValue();

        String options = "";
        options += decimalSeparator.isEmpty() ? "" : "aDec: '" + decimalSeparator + "',";
        // empty thousandSeparator must be explicity defined.
        options += thousandSeparator.isEmpty() ? "aSep:''," : "aSep: '" + thousandSeparator + "',";
        options += symbol.isEmpty() ? "" : "aSign: '" + symbol + "',";
        options += symbolPosition.isEmpty() ? "" : "pSign: '" + symbolPosition + "',";
        options += minValue.isEmpty() ? "" : "vMin: '" + minValue + "',";
        options += maxValue.isEmpty() ? "" : "vMax: '" + maxValue + "',";
        options += roundMethod.isEmpty() ? "" : "mRound: '" + roundMethod + "',";
        options += decimalPlaces.isEmpty() ? "" : "mDec: '" + decimalPlaces + "',";
        options += "wEmpty: '" + emptyValue + "',";

        // if all options are empty return empty
        if (options.isEmpty()) {
            return "";
        }

        // delete the last comma
        final int lastInd = options.length() - 1;
        if (options.charAt(lastInd) == ',') {
            options = options.substring(0, lastInd);
        }
        return "{" + options + "}";

    }

}
