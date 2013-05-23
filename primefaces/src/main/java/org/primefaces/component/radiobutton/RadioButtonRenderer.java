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
package org.primefaces.component.radiobutton;

import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.HTML;

public class RadioButtonRenderer extends InputRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final RadioButton radioButton = (RadioButton) component;
        final SelectOneRadio selectOneRadio = findSelectOneRadio(radioButton);

        encodeMarkup(context, radioButton, selectOneRadio);
    }

    protected void encodeMarkup(final FacesContext context, final RadioButton radio, final SelectOneRadio selectOneRadio)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String masterClientId = selectOneRadio.getClientId(context);
        final String inputId = selectOneRadio.getRadioButtonId(context);
        final String clientId = radio.getClientId(context);
        final boolean disabled = radio.isDisabled() || selectOneRadio.isDisabled();
        final Converter converter = getConverter(context, selectOneRadio);
        final SelectItem selecItem = selectOneRadio.getSelectItems().get(radio.getItemIndex());
        final Object itemValue = selecItem.getValue();
        final String itemValueAsString = getOptionAsString(context, selectOneRadio, converter, itemValue);

        // selected
        Object value = selectOneRadio.getSubmittedValue();
        if (value == null) {
            value = selectOneRadio.getValue();
        }
        final Class type = value == null ? String.class : value.getClass();
        final Object coercedItemValue = coerceToModelType(context, itemValue, type);
        final boolean selected = (coercedItemValue != null) && coercedItemValue.equals(value);

        // render markup
        final String style = radio.getStyle();
        final String defaultStyleClass = selectOneRadio.isPlain()
                                                                 ? HTML.RADIOBUTTON_NATIVE_CLASS
                                                                 : HTML.RADIOBUTTON_CLASS;
        String styleClass = radio.getStyleClass();
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        encodeOptionInput(context, selectOneRadio, radio, inputId, masterClientId, selected, disabled,
                          itemValueAsString);
        encodeOptionOutput(context, selected, disabled);

        writer.endElement("div");
    }

    protected void encodeOptionInput(final FacesContext context,
                                     final SelectOneRadio radio,
                                     final RadioButton button,
                                     final String id,
                                     final String name,
                                     final boolean checked,
                                     final boolean disabled,
                                     final String value) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String tabindex = button.getTabindex();
        if (tabindex == null) {
            tabindex = radio.getTabindex();
        }

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "radio", null);
        writer.writeAttribute("value", value, null);

        if (tabindex != null) writer.writeAttribute("tabindex", tabindex, null);
        if (checked) writer.writeAttribute("checked", "checked", null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);

        // onchange
        final StringBuilder onchangeBuilder = new StringBuilder();
        if (radio.getOnchange() != null) onchangeBuilder.append(radio.getOnchange()).append(";");
        if (button.getOnchange() != null) onchangeBuilder.append(button.getOnchange()).append(";");
        if (onchangeBuilder.length() > 0) {
            writer.writeAttribute("onchange", onchangeBuilder.toString(), null);
        }

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionOutput(final FacesContext context, final boolean selected, final boolean disabled)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String boxClass = HTML.RADIOBUTTON_BOX_CLASS;
        boxClass = selected ? boxClass + " ui-state-active" : boxClass;
        boxClass = disabled ? boxClass + " ui-state-disabled" : boxClass;

        String iconClass = HTML.RADIOBUTTON_ICON_CLASS;
        iconClass = selected ? iconClass + " " + HTML.RADIOBUTTON_CHECKED_ICON_CLASS : iconClass;

        writer.startElement("div", null);
        writer.writeAttribute("class", boxClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }

    public SelectOneRadio findSelectOneRadio(final RadioButton radioButton) {
        final UIComponent target = radioButton.findComponent(radioButton.getFor());

        if (target == null) {
            throw new FacesException("Cannot find component '" + radioButton.getFor() + "' in view.");
        }

        return (SelectOneRadio) target;
    }

    protected Converter getConverter(final FacesContext context, final SelectOneRadio selectOneRadio) {
        final Converter converter = selectOneRadio.getConverter();

        if (converter != null) {
            return converter;
        } else {
            final ValueExpression ve = selectOneRadio.getValueExpression("value");

            if (ve != null) {
                final Class<?> valueType = ve.getType(context.getELContext());

                if (valueType != null) return context.getApplication().createConverter(valueType);
            }
        }

        return null;
    }
}
