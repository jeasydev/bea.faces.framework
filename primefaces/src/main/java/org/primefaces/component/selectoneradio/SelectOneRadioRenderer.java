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
package org.primefaces.component.selectoneradio;

import java.io.IOException;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import org.primefaces.component.radiobutton.RadioButton;
import org.primefaces.renderkit.SelectOneRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class SelectOneRadioRenderer extends SelectOneRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectOneRadio radio = (SelectOneRadio) component;

        encodeMarkup(context, radio);
        encodeScript(context, radio);
    }

    protected void encodeMarkup(final FacesContext context, final SelectOneRadio radio) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = radio.getClientId(context);
        final String style = radio.getStyle();
        String styleClass = radio.getStyleClass();
        styleClass = styleClass == null ? SelectOneRadio.STYLE_CLASS : SelectOneRadio.STYLE_CLASS + " " + styleClass;
        final String layout = radio.getLayout();
        final boolean custom = layout != null && layout.equals("custom");

        final List<SelectItem> selectItems = getSelectItems(context, radio);

        if (custom) {
            // populate selectitems for radiobutton access
            radio.setSelectItems(getSelectItems(context, radio));

            // render dummy markup to enable processing of ajax behaviors
            // (finding form on client side)
            writer.startElement("span", radio);
            writer.writeAttribute("id", radio.getClientId(context), "id");
            writer.endElement("span");
        } else {
            writer.startElement("table", radio);
            writer.writeAttribute("id", clientId, "id");
            writer.writeAttribute("class", styleClass, "styleClass");
            if (style != null) {
                writer.writeAttribute("style", style, "style");
            }

            encodeSelectItems(context, radio, selectItems, layout);

            writer.endElement("table");
        }
    }

    protected void encodeOption(final FacesContext context,
                                final SelectOneRadio radio,
                                final SelectItem option,
                                final String id,
                                final String name,
                                final Converter converter,
                                final boolean selected,
                                final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, radio, converter, option.getValue());
        final String styleClass = radio.isPlain() ? HTML.RADIOBUTTON_NATIVE_CLASS : HTML.RADIOBUTTON_CLASS;

        writer.startElement("td", null);

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        encodeOptionInput(context, radio, id, name, selected, disabled, itemValueAsString);
        encodeOptionOutput(context, radio, selected, disabled);

        writer.endElement("div");
        writer.endElement("td");

        writer.startElement("td", null);
        encodeOptionLabel(context, radio, id, option, disabled);
        writer.endElement("td");
    }

    protected void encodeOptionInput(final FacesContext context,
                                     final SelectOneRadio radio,
                                     final String id,
                                     final String name,
                                     final boolean checked,
                                     final boolean disabled,
                                     final String value) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "radio", null);
        writer.writeAttribute("value", value, null);

        if (radio.getTabindex() != null) writer.writeAttribute("tabindex", radio.getTabindex(), null);
        if (checked) writer.writeAttribute("checked", "checked", null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (radio.getOnchange() != null) writer.writeAttribute("onchange", radio.getOnchange(), null);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionLabel(final FacesContext context,
                                     final SelectOneRadio radio,
                                     final String containerClientId,
                                     final SelectItem option,
                                     final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("label", null);
        writer.writeAttribute("for", containerClientId, null);
        if (disabled) writer.writeAttribute("class", "ui-state-disabled", null);

        if (option.isEscape())
            writer.writeText(option.getLabel(), null);
        else writer.write(option.getLabel());

        writer.endElement("label");
    }

    protected void encodeOptionOutput(final FacesContext context,
                                      final SelectOneRadio radio,
                                      final boolean selected,
                                      final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String boxClass = HTML.RADIOBUTTON_BOX_CLASS;
        boxClass = selected ? boxClass + " ui-state-active" : boxClass;
        boxClass = disabled ? boxClass + " ui-state-disabled" : boxClass;
        boxClass = !radio.isValid() ? boxClass + " ui-state-error" : boxClass;

        String iconClass = HTML.RADIOBUTTON_ICON_CLASS;
        iconClass = selected ? iconClass + " " + HTML.RADIOBUTTON_CHECKED_ICON_CLASS : iconClass;

        writer.startElement("div", null);
        writer.writeAttribute("class", boxClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeRadioButton(final FacesContext context, final SelectOneRadio radio, final RadioButton button)
        throws IOException {
        context.getResponseWriter();
    }

    protected void encodeScript(final FacesContext context, final SelectOneRadio radio) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = radio.getClientId(context);
        final String layout = radio.getLayout();
        final boolean custom = layout != null && layout.equals("custom");

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectOneRadio", radio.resolveWidgetVar(), clientId, true).attr("custom", custom, false);

        encodeClientBehaviors(context, radio, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSelectItems(final FacesContext context,
                                     final SelectOneRadio radio,
                                     final List<SelectItem> selectItems,
                                     final String layout) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Converter converter = radio.getConverter();
        final String name = radio.getClientId(context);
        final boolean pageDirection = layout != null && layout.equals("pageDirection");
        Object value = radio.getSubmittedValue();
        if (value == null) {
            value = radio.getValue();
        }
        final Class type = value == null ? String.class : value.getClass();

        int idx = -1;
        for (final SelectItem selectItem : selectItems) {
            idx++;
            final boolean disabled = selectItem.isDisabled() || radio.isDisabled();
            final String id = name + UINamingContainer.getSeparatorChar(context) + idx;
            final Object coercedItemValue = coerceToModelType(context, selectItem.getValue(), type);
            final boolean selected = (coercedItemValue != null) && coercedItemValue.equals(value);

            if (pageDirection) {
                writer.startElement("tr", null);
            }

            encodeOption(context, radio, selectItem, id, name, converter, selected, disabled);

            if (pageDirection) {
                writer.endElement("tr");
            }
        }
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        return context.getRenderKit().getRenderer("javax.faces.SelectOne", "javax.faces.Radio")
            .getConvertedValue(context, component, submittedValue);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectOne selectOne) {
        return selectOne.getClientId(context);
    }

    protected Class getValueType(final FacesContext context, final UIInput input) {
        final ValueExpression ve = input.getValueExpression("value");
        final Class type = ve == null ? String.class : ve.getType(context.getELContext());

        return type == null ? String.class : type;
    }
}
