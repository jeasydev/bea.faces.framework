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
package org.primefaces.component.selectcheckboxmenu;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import org.primefaces.renderkit.SelectManyRenderer;
import org.primefaces.util.WidgetBuilder;

public class SelectCheckboxMenuRenderer extends SelectManyRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectCheckboxMenu menu = (SelectCheckboxMenu) component;

        encodeMarkup(context, menu);
        encodeScript(context, menu);
    }

    protected void encodeInputs(final FacesContext context,
                                final SelectCheckboxMenu menu,
                                final List<SelectItem> selectItems) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Converter converter = menu.getConverter();
        final Object values = getValues(menu);
        final Object submittedValues = getSubmittedValues(menu);

        writer.startElement("div", menu);
        writer.writeAttribute("class", "ui-helper-hidden", null);

        int idx = -1;
        for (final SelectItem selectItem : selectItems) {
            idx++;

            encodeOption(context, menu, values, submittedValues, converter, selectItem, idx);
        }

        writer.endElement("div");
    }

    protected void encodeLabel(final FacesContext context,
                               final SelectCheckboxMenu menu,
                               final List<SelectItem> selectItems,
                               final boolean valid) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String label = menu.getLabel();
        final String labelClass = !valid
                                        ? SelectCheckboxMenu.LABEL_CLASS + " ui-state-error"
                                        : SelectCheckboxMenu.LABEL_CLASS;
        if (label == null) {
            label = "&nbsp;";
        }

        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("class", SelectCheckboxMenu.LABEL_CONTAINER_CLASS, null);
        if (menu.getTabindex() != null) {
            writer.writeAttribute("tabindex", menu.getTabindex(), null);
        }

        writer.startElement("label", null);
        writer.writeAttribute("class", labelClass, null);
        writer.write(label);
        writer.endElement("label");
        writer.endElement("a");
    }

    protected void encodeMarkup(final FacesContext context, final SelectCheckboxMenu menu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = menu.getClientId(context);
        final List<SelectItem> selectItems = getSelectItems(context, menu);
        final boolean valid = menu.isValid();

        final String style = menu.getStyle();
        String styleclass = menu.getStyleClass();
        styleclass = styleclass == null ? SelectCheckboxMenu.STYLE_CLASS : SelectCheckboxMenu.STYLE_CLASS + " "
            + styleclass;
        styleclass = menu.isDisabled() ? styleclass + " ui-state-disabled" : styleclass;
        styleclass = !valid ? styleclass + " ui-state-error" : styleclass;

        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleclass, "styleclass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeInputs(context, menu, selectItems);
        encodeLabel(context, menu, selectItems, valid);
        encodeMenuIcon(context, menu, valid);

        writer.endElement("div");
    }

    protected void encodeMenuIcon(final FacesContext context, final SelectCheckboxMenu menu, final boolean valid)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String iconClass = valid ? SelectCheckboxMenu.TRIGGER_CLASS : SelectCheckboxMenu.TRIGGER_CLASS
            + " ui-state-error";

        writer.startElement("div", menu);
        writer.writeAttribute("class", iconClass, null);

        writer.startElement("span", menu);
        writer.writeAttribute("class", "ui-icon ui-icon-triangle-1-s", null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeOption(final FacesContext context,
                                final SelectCheckboxMenu menu,
                                final Object values,
                                final Object submittedValues,
                                final Converter converter,
                                final SelectItem option,
                                final int idx) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, menu, converter, option.getValue());
        final String name = menu.getClientId(context);
        final String id = name + UINamingContainer.getSeparatorChar(context) + idx;
        final boolean disabled = option.isDisabled() || menu.isDisabled();

        Object valuesArray;
        Object itemValue;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = itemValueAsString;
        } else {
            valuesArray = values;
            itemValue = option.getValue();
        }

        final boolean checked = isSelected(context, menu, itemValue, valuesArray, converter);
        if (option.isNoSelectionOption() && values != null && !checked) {
            return;
        }

        // input
        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "checkbox", null);
        writer.writeAttribute("value", itemValueAsString, null);

        if (checked) writer.writeAttribute("checked", "checked", null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (menu.getOnchange() != null) writer.writeAttribute("onchange", menu.getOnchange(), null);

        writer.endElement("input");

        // label
        writer.startElement("label", null);
        writer.writeAttribute("for", id, null);
        if (disabled) writer.writeAttribute("class", "ui-state-disabled", null);

        if (option.isEscape())
            writer.writeText(option.getLabel(), null);
        else writer.write(option.getLabel());

        writer.endElement("label");
    }

    protected void encodeScript(final FacesContext context, final SelectCheckboxMenu menu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = menu.getClientId(context);

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectCheckboxMenu", menu.resolveWidgetVar(), clientId, true).callback("onShow", "function()",
                                                                                          menu.getOnShow())
            .callback("onHide", "function()", menu.getOnHide()).attr("scrollHeight", menu.getScrollHeight(),
                                                                     Integer.MAX_VALUE);

        if (menu.isFilter()) {
            wb.attr("filter", true).attr("filterMatchMode", menu.getFilterMatchMode(), null)
                .attr("filterFunction", menu.getFilterFunction(), null).attr("caseSensitive", menu.isCaseSensitive(),
                                                                             false);
        }

        wb.attr("panelStyle", menu.getPanelStyle(), null).attr("panelStyleClass", menu.getPanelStyleClass(), null);

        encodeClientBehaviors(context, menu, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
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
