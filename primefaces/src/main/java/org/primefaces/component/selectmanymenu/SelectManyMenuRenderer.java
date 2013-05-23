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
package org.primefaces.component.selectmanymenu;

import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import org.primefaces.component.column.Column;
import org.primefaces.component.selectonelistbox.SelectOneListbox;
import org.primefaces.renderkit.RendererUtils;
import org.primefaces.renderkit.SelectManyRenderer;
import org.primefaces.util.WidgetBuilder;

public class SelectManyMenuRenderer extends SelectManyRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectManyMenu menu = (SelectManyMenu) component;

        encodeMarkup(context, menu);
        encodeScript(context, menu);
    }

    protected void encodeInput(final FacesContext context,
                               final SelectManyMenu menu,
                               final String clientId,
                               final List<SelectItem> selectItems) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String inputid = clientId + "_input";

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("select", null);
        writer.writeAttribute("id", inputid, "id");
        writer.writeAttribute("name", inputid, null);
        writer.writeAttribute("multiple", "multiple", null);
        writer.writeAttribute("size", "2", null); // prevent browser to send
                                                  // value when no item is
                                                  // selected

        if (menu.getTabindex() != null) writer.writeAttribute("tabindex", menu.getTabindex(), null);
        if (menu.getOnchange() != null) writer.writeAttribute("onchange", menu.getOnchange(), null);

        encodeSelectItems(context, menu, selectItems);

        writer.endElement("select");

        writer.endElement("div");
    }

    protected void encodeItem(final FacesContext context,
                              final SelectManyMenu menu,
                              final SelectItem option,
                              final Object values,
                              final Object submittedValues,
                              final Converter converter,
                              final boolean customContent,
                              final boolean showCheckbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, menu, converter, option.getValue());
        final boolean disabled = option.isDisabled() || menu.isDisabled();
        String itemClass = disabled ? SelectManyMenu.ITEM_CLASS + " ui-state-disabled" : SelectManyMenu.ITEM_CLASS;

        Object valuesArray;
        Object itemValue;
        if (submittedValues != null) {
            valuesArray = submittedValues;
            itemValue = itemValueAsString;
        } else {
            valuesArray = values;
            itemValue = option.getValue();
        }

        final boolean selected = isSelected(context, menu, itemValue, valuesArray, converter);
        if (option.isNoSelectionOption() && values != null && !selected) {
            return;
        }

        if (selected) {
            itemClass = itemClass + " ui-state-highlight";
        }

        if (customContent) {
            final String var = menu.getVar();
            context.getExternalContext().getRequestMap().put(var, option.getValue());

            writer.startElement("tr", null);
            writer.writeAttribute("class", itemClass, null);
            if (option.getDescription() != null) {
                writer.writeAttribute("title", option.getDescription(), null);
            }

            if (showCheckbox) {
                writer.startElement("td", null);
                RendererUtils.encodeCheckbox(context, selected);
                writer.endElement("td");
            }

            for (final UIComponent child : menu.getChildren()) {
                if (child instanceof Column && child.isRendered()) {
                    writer.startElement("td", null);
                    child.encodeAll(context);
                    writer.endElement("td");
                }
            }

            writer.endElement("tr");
        } else {
            writer.startElement("li", null);
            writer.writeAttribute("class", itemClass, null);

            if (showCheckbox) {
                RendererUtils.encodeCheckbox(context, selected);
            }

            if (option.isEscape()) {
                writer.writeText(option.getLabel(), null);
            } else {
                writer.write(option.getLabel());
            }

            writer.endElement("li");
        }

    }

    protected void encodeList(final FacesContext context, final SelectManyMenu menu, final List<SelectItem> selectItems)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Converter converter = menu.getConverter();
        final Object values = getValues(menu);
        final Object submittedValues = getSubmittedValues(menu);
        final boolean customContent = menu.getVar() != null;
        final boolean showCheckbox = menu.isShowCheckbox();

        if (customContent) {
            writer.startElement("table", null);
            writer.writeAttribute("class", SelectOneListbox.LIST_CLASS, null);
            writer.startElement("tbody", null);
            for (final SelectItem selectItem : selectItems) {
                encodeItem(context, menu, selectItem, values, submittedValues, converter, customContent, showCheckbox);
            }
            writer.endElement("tbody");
            writer.endElement("table");
        } else {
            writer.startElement("ul", null);
            writer.writeAttribute("class", SelectOneListbox.LIST_CLASS, null);
            for (final SelectItem selectItem : selectItems) {
                encodeItem(context, menu, selectItem, values, submittedValues, converter, customContent, showCheckbox);
            }
            writer.endElement("ul");
        }
    }

    protected void encodeMarkup(final FacesContext context, final SelectManyMenu menu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = menu.getClientId(context);
        final List<SelectItem> selectItems = getSelectItems(context, menu);

        final String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        styleClass = styleClass == null ? SelectManyMenu.CONTAINER_CLASS : SelectManyMenu.CONTAINER_CLASS + " "
            + styleClass;
        styleClass = menu.isDisabled() ? styleClass + " ui-state-disabled" : styleClass;
        styleClass = !menu.isValid() ? styleClass + " ui-state-error" : styleClass;

        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) writer.writeAttribute("style", style, "style");

        encodeInput(context, menu, clientId, selectItems);
        encodeList(context, menu, selectItems);

        writer.endElement("div");
    }

    protected void encodeOption(final FacesContext context,
                                final SelectManyMenu menu,
                                final SelectItem option,
                                final Object values,
                                final Object submittedValues,
                                final Converter converter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String itemValueAsString = getOptionAsString(context, menu, converter, option.getValue());
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

        final boolean selected = isSelected(context, menu, itemValue, valuesArray, converter);
        if (option.isNoSelectionOption() && values != null && !selected) {
            return;
        }

        writer.startElement("option", null);
        writer.writeAttribute("value", itemValueAsString, null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (selected) writer.writeAttribute("selected", "selected", null);

        writer.write(option.getLabel());

        writer.endElement("option");
    }

    protected void encodeScript(final FacesContext context, final SelectManyMenu menu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = menu.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectManyMenu", menu.resolveWidgetVar(), clientId, false)
            .attr("disabled", menu.isDisabled(), false).attr("showCheckbox", menu.isShowCheckbox(), false);

        encodeClientBehaviors(context, menu, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSelectItems(final FacesContext context,
                                     final SelectManyMenu menu,
                                     final List<SelectItem> selectItems) throws IOException {
        final Converter converter = menu.getConverter();
        final Object values = getValues(menu);
        final Object submittedValues = getSubmittedValues(menu);

        for (final SelectItem selectItem : selectItems) {
            encodeOption(context, menu, selectItem, values, submittedValues, converter);
        }
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        return context.getRenderKit().getRenderer("javax.faces.SelectMany", "javax.faces.Menu")
            .getConvertedValue(context, component, submittedValue);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectMany selectMany) {
        return selectMany.getClientId(context) + "_input";
    }

}