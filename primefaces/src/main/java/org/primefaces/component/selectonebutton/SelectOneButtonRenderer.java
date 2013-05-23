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
package org.primefaces.component.selectonebutton;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import org.primefaces.renderkit.SelectOneRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class SelectOneButtonRenderer extends SelectOneRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectOneButton button = (SelectOneButton) component;

        encodeMarkup(context, button);
        encodeScript(context, button);
    }

    protected void encodeMarkup(final FacesContext context, final SelectOneButton button) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = button.getClientId(context);
        final String style = button.getStyle();
        String styleClass = button.getStyleClass();
        styleClass = styleClass == null ? SelectOneButton.STYLE_CLASS : SelectOneButton.STYLE_CLASS + " " + styleClass;

        writer.startElement("div", button);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeSelectItems(context, button);

        writer.endElement("div");
    }

    protected void encodeOption(final FacesContext context,
                                final SelectOneButton button,
                                final SelectItem option,
                                final String id,
                                final String name,
                                final Converter converter,
                                final boolean selected,
                                final boolean disabled,
                                final int idx,
                                final int size) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, button, converter, option.getValue());

        String buttonStyle = HTML.BUTTON_TEXT_ONLY_BUTTON_FLAT_CLASS;
        if (idx == 0)
            buttonStyle = buttonStyle + " ui-corner-left";
        else if (idx == (size - 1)) buttonStyle = buttonStyle + " ui-corner-right";

        buttonStyle = selected ? buttonStyle + " ui-state-active" : buttonStyle;
        buttonStyle = disabled ? buttonStyle + " ui-state-disabled" : buttonStyle;
        buttonStyle = !button.isValid() ? buttonStyle + " ui-state-error" : buttonStyle;

        // button
        writer.startElement("div", null);
        writer.writeAttribute("class", buttonStyle, null);
        if (option.getDescription() != null) writer.writeAttribute("title", option.getDescription(), null);

        // input
        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "radio", null);
        writer.writeAttribute("value", itemValueAsString, null);
        writer.writeAttribute("class", "ui-helper-hidden", null);

        if (selected) writer.writeAttribute("checked", "checked", null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (button.getOnchange() != null) writer.writeAttribute("onchange", button.getOnchange(), null);

        writer.endElement("input");

        // item label
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.writeText(option.getLabel(), "itemLabel");
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final SelectOneButton button) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = button.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectOneButton", button.resolveWidgetVar(), clientId, false);
        encodeClientBehaviors(context, button, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSelectItems(final FacesContext context, final SelectOneButton button) throws IOException {
        final List<SelectItem> selectItems = getSelectItems(context, button);
        final int selectItemsSize = selectItems.size();
        final Converter converter = button.getConverter();
        final String name = button.getClientId(context);
        Object value = button.getSubmittedValue();
        if (value == null) {
            value = button.getValue();
        }
        final Class type = value == null ? String.class : value.getClass();

        int idx = -1;
        for (final SelectItem selectItem : selectItems) {
            idx++;
            final boolean disabled = selectItem.isDisabled() || button.isDisabled();
            final String id = name + UINamingContainer.getSeparatorChar(context) + idx;
            final Object coercedItemValue = coerceToModelType(context, selectItem.getValue(), type);
            final boolean selected = (coercedItemValue != null) && coercedItemValue.equals(value);

            encodeOption(context, button, selectItem, id, name, converter, selected, disabled, idx, selectItemsSize);
        }
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        return context.getRenderKit().getRenderer("javax.faces.SelectOne", "javax.faces.Radio")
            .getConvertedValue(context, component, submittedValue);
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectOne selectOne) {
        return selectOne.getClientId(context);
    }
}
