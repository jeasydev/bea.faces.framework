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
package org.primefaces.component.selectmanycheckbox;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import org.primefaces.renderkit.SelectManyRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class SelectManyCheckboxRenderer extends SelectManyRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectManyCheckbox checkbox = (SelectManyCheckbox) component;

        encodeMarkup(context, checkbox);
        encodeScript(context, checkbox);
    }

    protected void encodeMarkup(final FacesContext context, final SelectManyCheckbox checkbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = checkbox.getClientId(context);
        final String style = checkbox.getStyle();
        String styleClass = checkbox.getStyleClass();
        styleClass = styleClass == null ? SelectManyCheckbox.STYLE_CLASS : SelectManyCheckbox.STYLE_CLASS + " "
            + styleClass;

        writer.startElement("table", checkbox);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) writer.writeAttribute("style", style, "style");

        encodeSelectItems(context, checkbox);

        writer.endElement("table");
    }

    protected void encodeOption(final FacesContext context,
                                final UIInput component,
                                final Object values,
                                final Object submittedValues,
                                final Converter converter,
                                final SelectItem option,
                                final int idx) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final SelectManyCheckbox checkbox = (SelectManyCheckbox) component;
        final String itemValueAsString = getOptionAsString(context, component, converter, option.getValue());
        final String name = checkbox.getClientId(context);
        final String id = name + UINamingContainer.getSeparatorChar(context) + idx;
        final boolean disabled = option.isDisabled() || checkbox.isDisabled();

        Object valuesArray;
        Object itemValue;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = itemValueAsString;
        } else {
            valuesArray = values;
            itemValue = option.getValue();
        }

        final boolean selected = isSelected(context, component, itemValue, valuesArray, converter);
        if (option.isNoSelectionOption() && values != null && !selected) {
            return;
        }

        writer.startElement("td", null);

        writer.startElement("div", null);
        writer.writeAttribute("class", HTML.CHECKBOX_CLASS, null);

        encodeOptionInput(context, checkbox, id, name, selected, disabled, itemValueAsString);
        encodeOptionOutput(context, checkbox, selected, disabled);

        writer.endElement("div");
        writer.endElement("td");

        writer.startElement("td", null);
        encodeOptionLabel(context, checkbox, id, option, disabled);
        writer.endElement("td");
    }

    protected void encodeOptionInput(final FacesContext context,
                                     final SelectManyCheckbox checkbox,
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
        writer.writeAttribute("type", "checkbox", null);
        writer.writeAttribute("value", value, null);

        if (checked) writer.writeAttribute("checked", "checked", null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (checkbox.getOnchange() != null) writer.writeAttribute("onchange", checkbox.getOnchange(), null);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeOptionLabel(final FacesContext context,
                                     final SelectManyCheckbox checkbox,
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
                                      final SelectManyCheckbox checkbox,
                                      final boolean checked,
                                      final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String boxClass = HTML.CHECKBOX_BOX_CLASS;
        boxClass = checked ? boxClass + " ui-state-active" : boxClass;
        boxClass = disabled ? boxClass + " ui-state-disabled" : boxClass;
        boxClass = !checkbox.isValid() ? boxClass + " ui-state-error" : boxClass;

        String iconClass = HTML.CHECKBOX_ICON_CLASS;
        iconClass = checked ? iconClass + " " + HTML.CHECKBOX_CHECKED_ICON_CLASS : iconClass;

        writer.startElement("div", null);
        writer.writeAttribute("class", boxClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final SelectManyCheckbox checkbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = checkbox.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectManyCheckbox", checkbox.resolveWidgetVar(), clientId, false);
        encodeClientBehaviors(context, checkbox, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSelectItems(final FacesContext context, final SelectManyCheckbox checkbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final List<SelectItem> selectItems = getSelectItems(context, checkbox);
        final Converter converter = checkbox.getConverter();
        final Object values = getValues(checkbox);
        final Object submittedValues = getSubmittedValues(checkbox);
        final String layout = checkbox.getLayout();
        final boolean pageDirection = layout != null && layout.equals("pageDirection");

        int idx = -1;
        for (final SelectItem selectItem : selectItems) {
            idx++;
            if (pageDirection) {
                writer.startElement("tr", null);
            }

            encodeOption(context, checkbox, values, submittedValues, converter, selectItem, idx);

            if (pageDirection) {
                writer.endElement("tr");
            }
        }
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        return context.getRenderKit().getRenderer("javax.faces.SelectMany", "javax.faces.Checkbox")
            .getConvertedValue(context, component, submittedValue);
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectMany selectMany) {
        return selectMany.getClientId(context);
    }
}
