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
package org.primefaces.component.selectonemenu;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import org.primefaces.component.column.Column;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.renderkit.SelectOneRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class SelectOneMenuRenderer extends SelectOneRenderer {

    protected String calculateWrapperHeight(final SelectOneMenu menu, final int itemSize) {
        final int height = menu.getHeight();

        if (height != Integer.MAX_VALUE) {
            return height + "px";
        } else if (itemSize > 10) {
            return 200 + "px";
        }

        return "auto";
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        if (!InputRenderer.shouldDecode(component)) {
            return;
        }

        final SelectOneMenu menu = (SelectOneMenu) component;
        if (menu.isEditable()) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

            decodeBehaviors(context, menu);

            menu.setSubmittedValue(params.get(menu.getClientId(context) + "_editableInput"));
        } else {
            super.decode(context, component);
        }
    }

    @Override
    public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectOneMenu menu = (SelectOneMenu) component;

        encodeMarkup(context, menu);
        encodeScript(context, menu);
    }

    protected void encodeFilter(final FacesContext context, final SelectOneMenu menu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String id = menu.getClientId(context) + "_filter";

        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-selectonemenu-filter-container", null);

        writer.startElement("input", null);
        writer
            .writeAttribute("class",
                            "ui-selectonemenu-filter ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all",
                            null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("autocomplete", "off", null);

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-icon ui-icon-search", id);
        writer.endElement("span");

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeInput(final FacesContext context,
                               final SelectOneMenu menu,
                               final String clientId,
                               final List<SelectItem> selectItems,
                               final Object values,
                               final Object submittedValues,
                               final Converter converter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String inputId = clientId + "_input";
        final String focusId = clientId + "_focus";

        writer.startElement("div", menu);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("select", menu);
        writer.writeAttribute("id", inputId, "id");
        writer.writeAttribute("name", inputId, null);
        if (menu.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        if (menu.getOnkeydown() != null) writer.writeAttribute("onkeydown", menu.getOnkeydown(), null);
        if (menu.getOnkeyup() != null) writer.writeAttribute("onkeyup", menu.getOnkeyup(), null);

        encodeSelectItems(context, menu, selectItems, values, submittedValues, converter);

        writer.endElement("select");

        writer.endElement("div");

        writer.startElement("div", menu);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        // input for accessibility
        writer.startElement("input", menu);
        writer.writeAttribute("id", focusId, null);
        writer.writeAttribute("name", focusId, null);
        writer.writeAttribute("type", "text", null);
        if (menu.getTabindex() != null) writer.writeAttribute("tabindex", menu.getTabindex(), null);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeItem(final FacesContext context,
                              final SelectOneMenu menu,
                              final SelectItem selectItem,
                              final String styleClass) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String itemLabel = selectItem.getLabel();
        itemLabel = isValueBlank(itemLabel) ? "&nbsp;" : itemLabel;

        writer.startElement("li", null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("data-label", itemLabel, null);
        if (selectItem.getDescription() != null) {
            writer.writeAttribute("title", selectItem.getDescription(), null);
        }

        if (itemLabel.equals("&nbsp;"))
            writer.write(itemLabel);
        else {
            if (selectItem.isEscape())
                writer.writeText(itemLabel, "value");
            else writer.write(itemLabel);
        }

        writer.endElement("li");
    }

    protected void encodeLabel(final FacesContext context, final SelectOneMenu menu, final List<SelectItem> selectItems)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String valueToRender = ComponentUtils.getValueToRender(context, menu);

        if (menu.isEditable()) {
            writer.startElement("input", null);
            writer.writeAttribute("type", "text", null);
            writer.writeAttribute("name", menu.getClientId() + "_editableInput", null);
            writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);

            if (menu.getTabindex() != null) {
                writer.writeAttribute("tabindex", menu.getTabindex(), null);
            }

            if (menu.isDisabled()) {
                writer.writeAttribute("disabled", "disabled", null);
            }

            if (valueToRender != null) {
                writer.writeAttribute("value", valueToRender, null);
            }

            if (menu.getMaxlength() != Integer.MAX_VALUE) {
                writer.writeAttribute("maxlength", menu.getMaxlength(), null);
            }

            writer.endElement("input");
        } else {
            writer.startElement("label", null);
            writer.writeAttribute("id", menu.getClientId() + "_label", null);
            writer.writeAttribute("class", SelectOneMenu.LABEL_CLASS, null);
            writer.write("&nbsp;");
            writer.endElement("label");
        }
    }

    protected void encodeMarkup(final FacesContext context, final SelectOneMenu menu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final List<SelectItem> selectItems = getSelectItems(context, menu);
        final String clientId = menu.getClientId(context);
        final Converter converter = menu.getConverter();
        final Object values = getValues(menu);
        final Object submittedValues = getSubmittedValues(menu);
        final boolean valid = menu.isValid();

        final String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        styleClass = styleClass == null ? SelectOneMenu.STYLE_CLASS : SelectOneMenu.STYLE_CLASS + " " + styleClass;
        styleClass = !valid ? styleClass + " ui-state-error" : styleClass;
        styleClass = menu.isDisabled() ? styleClass + " ui-state-disabled" : styleClass;

        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleclass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeInput(context, menu, clientId, selectItems, values, submittedValues, converter);
        encodeLabel(context, menu, selectItems);
        encodeMenuIcon(context, menu, valid);
        encodePanel(context, menu, selectItems);

        writer.endElement("div");
    }

    protected void encodeMenuIcon(final FacesContext context, final SelectOneMenu menu, final boolean valid)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String iconClass = valid ? SelectOneMenu.TRIGGER_CLASS : SelectOneMenu.TRIGGER_CLASS + " ui-state-error";

        writer.startElement("div", menu);
        writer.writeAttribute("class", iconClass, null);

        writer.startElement("span", menu);
        writer.writeAttribute("class", "ui-icon ui-icon-triangle-1-s", null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeOption(final FacesContext context,
                                final SelectOneMenu menu,
                                final SelectItem option,
                                final Object values,
                                final Object submittedValues,
                                final Converter converter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        if (option instanceof SelectItemGroup) {
            final SelectItemGroup group = (SelectItemGroup) option;
            for (final SelectItem groupItem : group.getSelectItems()) {
                encodeOption(context, menu, groupItem, values, submittedValues, converter);
            }
        } else {
            final String itemValueAsString = getOptionAsString(context, menu, converter, option.getValue());
            final boolean disabled = option.isDisabled();

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

            writer.startElement("option", null);
            writer.writeAttribute("value", itemValueAsString, null);
            if (disabled) writer.writeAttribute("disabled", "disabled", null);
            if (selected) writer.writeAttribute("selected", "selected", null);

            if (option.isEscape())
                writer.writeText(option.getLabel(), "value");
            else writer.write(option.getLabel());

            writer.endElement("option");
        }
    }

    protected void encodeOptionsAsList(final FacesContext context,
                                       final SelectOneMenu menu,
                                       final List<SelectItem> selectItems) throws IOException {
        for (int i = 0; i < selectItems.size(); i++) {
            final SelectItem selectItem = selectItems.get(i);

            if (selectItem instanceof SelectItemGroup) {
                final SelectItemGroup group = (SelectItemGroup) selectItem;

                encodeItem(context, menu, group, SelectOneMenu.ITEM_GROUP_CLASS);
                encodeOptionsAsList(context, menu, Arrays.asList(group.getSelectItems()));
            } else {
                encodeItem(context, menu, selectItem, SelectOneMenu.ITEM_CLASS);
            }
        }
    }

    protected void encodeOptionsAsTable(final FacesContext context,
                                        final SelectOneMenu menu,
                                        final List<SelectItem> selectItems) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String var = menu.getVar();
        final List<Column> columns = menu.getColums();

        for (final SelectItem selectItem : selectItems) {
            String itemLabel = selectItem.getLabel();
            itemLabel = isValueBlank(itemLabel) ? "&nbsp;" : itemLabel;
            final Object itemValue = selectItem.getValue();

            context.getExternalContext().getRequestMap().put(var, selectItem.getValue());

            writer.startElement("tr", null);
            writer.writeAttribute("class", SelectOneMenu.ROW_CLASS, null);
            writer.writeAttribute("data-label", itemLabel, null);
            if (selectItem.getDescription() != null) {
                writer.writeAttribute("title", selectItem.getDescription(), null);
            }

            if (itemValue instanceof String) {
                writer.startElement("td", null);
                writer.writeAttribute("colspan", columns.size(), null);
                writer.writeText(selectItem.getLabel(), null);
                writer.endElement("td");
            } else {
                for (final Column column : columns) {
                    writer.startElement("td", null);
                    column.encodeAll(context);
                    writer.endElement("td");
                }
            }

            writer.endElement("tr");
        }

        context.getExternalContext().getRequestMap().put(var, null);
    }

    protected void encodePanel(final FacesContext context, final SelectOneMenu menu, final List<SelectItem> selectItems)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final boolean customContent = menu.getVar() != null;
        final String panelStyle = menu.getPanelStyle();
        String panelStyleClass = menu.getPanelStyleClass();
        panelStyleClass = panelStyleClass == null ? SelectOneMenu.PANEL_CLASS : SelectOneMenu.PANEL_CLASS + " "
            + panelStyleClass;

        writer.startElement("div", null);
        writer.writeAttribute("id", menu.getClientId(context) + "_panel", null);
        writer.writeAttribute("class", panelStyleClass, null);
        if (panelStyle != null) {
            writer.writeAttribute("style", panelStyle, null);
        }

        if (menu.isFilter()) {
            encodeFilter(context, menu);
        }

        writer.startElement("div", null);
        writer.writeAttribute("class", SelectOneMenu.ITEMS_WRAPPER_CLASS, null);
        writer.writeAttribute("style", "height:" + calculateWrapperHeight(menu, selectItems.size()), null);

        if (customContent) {
            writer.startElement("table", menu);
            writer.writeAttribute("class", SelectOneMenu.TABLE_CLASS, null);
            writer.startElement("tbody", menu);
            encodeOptionsAsTable(context, menu, selectItems);
            writer.endElement("tbody");
            writer.endElement("table");
        } else {
            writer.startElement("ul", menu);
            writer.writeAttribute("class", SelectOneMenu.LIST_CLASS, null);
            encodeOptionsAsList(context, menu, selectItems);
            writer.endElement("ul");
        }

        writer.endElement("div");
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final SelectOneMenu menu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = menu.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectOneMenu", menu.resolveWidgetVar(), clientId, true).attr("effect", menu.getEffect(), null)
            .attr("effectSpeed", menu.getEffectSpeed(), null).attr("editable", menu.isEditable(), false)
            .callback("onchange", "function()", menu.getOnchange());

        if (menu.isFilter()) {
            wb.attr("filter", true).attr("filterMatchMode", menu.getFilterMatchMode(), null)
                .attr("filterFunction", menu.getFilterFunction(), null).attr("caseSensitive", menu.isCaseSensitive(),
                                                                             false);
        }

        encodeClientBehaviors(context, menu, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSelectItems(final FacesContext context,
                                     final SelectOneMenu menu,
                                     final List<SelectItem> selectItems,
                                     final Object values,
                                     final Object submittedValues,
                                     final Converter converter) throws IOException {
        for (final SelectItem selectItem : selectItems) {
            encodeOption(context, menu, selectItem, values, submittedValues, converter);
        }
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        return context.getRenderKit().getRenderer("javax.faces.SelectOne", "javax.faces.Menu")
            .getConvertedValue(context, component, submittedValue);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    protected String getSubmitParam(final FacesContext context, final UISelectOne selectOne) {
        return selectOne.getClientId(context) + "_input";
    }
}