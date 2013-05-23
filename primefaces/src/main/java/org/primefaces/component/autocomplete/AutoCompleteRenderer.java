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
package org.primefaces.component.autocomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.PhaseId;
import org.primefaces.component.column.Column;
import org.primefaces.event.AutoCompleteEvent;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class AutoCompleteRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final AutoComplete ac = (AutoComplete) component;
        final String clientId = ac.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        if (ac.isDisabled() || ac.isReadonly()) {
            return;
        }

        if (ac.isMultiple()) {
            decodeMultiple(context, ac);
        } else {
            decodeSingle(context, ac);
        }

        decodeBehaviors(context, ac);

        // AutoComplete event
        final String query = params.get(clientId + "_query");
        if (query != null) {
            final AutoCompleteEvent autoCompleteEvent = new AutoCompleteEvent(ac, query);
            autoCompleteEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            ac.queueEvent(autoCompleteEvent);
        }
    }

    protected void decodeMultiple(final FacesContext context, final AutoComplete ac) {
        final Map<String, String[]> params = context.getExternalContext().getRequestParameterValuesMap();
        final String clientId = ac.getClientId(context);
        final String[] submittedValues = params.get(clientId + "_hinput");

        if (submittedValues != null) {
            ac.setSubmittedValue(submittedValues);
        } else {
            ac.setSubmittedValue("");
        }
    }

    protected void decodeSingle(final FacesContext context, final AutoComplete ac) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = ac.getClientId(context);
        final String valueParam = (ac.getVar() != null) ? clientId + "_hinput" : clientId + "_input";
        final String submittedValue = params.get(valueParam);

        if (submittedValue != null) {
            ac.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    protected void encodeDropDown(final FacesContext context, final AutoComplete ac) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("button", ac);
        writer
            .writeAttribute("class", "ui-button ui-widget ui-state-default ui-corner-right ui-button-icon-only", null);
        writer.writeAttribute("type", "button", null);
        if (ac.getTabindex() != null) {
            writer.writeAttribute("tabindex", ac.getTabindex(), null);
        }

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-button-icon-primary ui-icon ui-icon-triangle-1-s", null);
        writer.endElement("span");

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-button-text", null);
        writer.write("&nbsp;");
        writer.endElement("span");

        writer.endElement("button");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final AutoComplete autoComplete = (AutoComplete) component;
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String query = params.get(autoComplete.getClientId(context) + "_query");

        if (query != null) {
            encodeResults(context, component, query);
        } else {
            encodeMarkup(context, autoComplete);
            encodeScript(context, autoComplete);
        }
    }

    protected void encodeHiddenInput(final FacesContext context, final AutoComplete ac, final String clientId)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String valueToRender = ComponentUtils.getValueToRender(context, ac);

        writer.startElement("input", null);
        writer.writeAttribute("id", clientId + "_hinput", null);
        writer.writeAttribute("name", clientId + "_hinput", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("autocomplete", "off", null);
        if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, null);
        }
        writer.endElement("input");
    }

    protected void encodeHiddenSelect(final FacesContext context,
                                      final AutoComplete ac,
                                      final String clientId,
                                      final List<String> values) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String id = clientId + "_hinput";

        writer.startElement("select", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("multiple", "multiple", null);
        writer.writeAttribute("class", "ui-helper-hidden", null);

        if (ac.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }

        for (final String value : values) {
            writer.startElement("option", null);
            writer.writeAttribute("value", value, null);
            writer.writeAttribute("selected", "selected", null);
            writer.endElement("option");
        }

        writer.endElement("select");
    }

    protected void encodeInput(final FacesContext context, final AutoComplete ac, final String clientId)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final boolean disabled = ac.isDisabled();
        String itemLabel;
        final String defaultStyleClass = ac.isDropdown()
                                                        ? AutoComplete.INPUT_WITH_DROPDOWN_CLASS
                                                        : AutoComplete.INPUT_CLASS;
        String styleClass = disabled ? defaultStyleClass + " ui-state-disabled" : defaultStyleClass;
        styleClass = ac.isValid() ? styleClass : styleClass + " ui-state-error";

        writer.startElement("input", null);
        writer.writeAttribute("id", clientId + "_input", null);
        writer.writeAttribute("name", clientId + "_input", null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("autocomplete", "off", null);

        if (ac.getVar() == null) {
            itemLabel = ComponentUtils.getValueToRender(context, ac);

            if (itemLabel != null) {
                writer.writeAttribute("value", itemLabel, null);
            }
        } else {
            if (ac.isValid()) {
                context.getExternalContext().getRequestMap().put(ac.getVar(), ac.getValue());
                itemLabel = ac.getItemLabel();
            } else {
                final Object submittedValue = ac.getSubmittedValue();
                itemLabel = (submittedValue == null) ? null : String.valueOf(submittedValue);
            }

            if (itemLabel != null) {
                writer.writeAttribute("value", itemLabel, null);
            }
        }

        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (ac.isReadonly()) writer.writeAttribute("readonly", "readonly", null);

        renderPassThruAttributes(context, ac, HTML.INPUT_TEXT_ATTRS);

        writer.endElement("input");
    }

    protected void encodeMarkup(final FacesContext context, final AutoComplete ac) throws IOException {
        if (ac.isMultiple())
            encodeMultipleMarkup(context, ac);
        else encodeSingleMarkup(context, ac);
    }

    protected void encodeMultipleMarkup(final FacesContext context, final AutoComplete ac) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ac.getClientId(context);
        final String inputId = clientId + "_input";
        final List values = (List) ac.getValue();
        final List<String> stringValues = new ArrayList<String>();
        final Converter converter = findConverter(context, ac);
        final String var = ac.getVar();
        final boolean pojo = var != null;
        final boolean disabled = ac.isDisabled();

        String styleClass = ac.getStyleClass();
        styleClass = styleClass == null ? AutoComplete.MULTIPLE_STYLE_CLASS : AutoComplete.MULTIPLE_STYLE_CLASS + " "
            + styleClass;
        final String listClass = disabled
                                         ? AutoComplete.MULTIPLE_CONTAINER_CLASS + " ui-state-disabled"
                                         : AutoComplete.MULTIPLE_CONTAINER_CLASS;

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if (ac.getStyle() != null) {
            writer.writeAttribute("style", ac.getStyle(), null);
        }

        writer.startElement("ul", null);
        writer.writeAttribute("class", listClass, null);

        if (values != null && !values.isEmpty()) {
            for (final Iterator<Object> it = values.iterator(); it.hasNext();) {
                final Object value = it.next();
                Object itemValue = null;
                String itemLabel = null;

                if (pojo) {
                    context.getExternalContext().getRequestMap().put(var, value);
                    itemValue = ac.getItemValue();
                    itemLabel = ac.getItemLabel();
                } else {
                    itemValue = value;
                    itemLabel = String.valueOf(value);
                }

                final String tokenValue = converter != null ? converter.getAsString(context, ac, itemValue) : String
                    .valueOf(itemValue);

                writer.startElement("li", null);
                writer.writeAttribute("data-token-value", tokenValue, null);
                writer.writeAttribute("class", AutoComplete.TOKEN_DISPLAY_CLASS, null);

                writer.startElement("span", null);
                writer.writeAttribute("class", AutoComplete.TOKEN_LABEL_CLASS, null);
                writer.writeText(itemLabel, null);
                writer.endElement("span");

                writer.startElement("span", null);
                writer.writeAttribute("class", AutoComplete.TOKEN_ICON_CLASS, null);
                writer.endElement("span");

                writer.endElement("li");

                stringValues.add(tokenValue);
            }
        }

        writer.startElement("li", null);
        writer.writeAttribute("class", AutoComplete.TOKEN_INPUT_CLASS, null);
        writer.startElement("input", null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("autocomplete", "off", null);
        if (disabled) {
            writer.writeAttribute("disabled", "disabled", "disabled");
        }
        writer.endElement("input");
        writer.endElement("li");

        writer.endElement("ul");

        encodePanel(context, ac);

        encodeHiddenSelect(context, ac, clientId, stringValues);

        writer.endElement("div");
    }

    protected void encodePanel(final FacesContext context, final AutoComplete ac) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String styleClass = ac.getPanelStyleClass();
        styleClass = styleClass == null ? AutoComplete.PANEL_CLASS : AutoComplete.PANEL_CLASS + " " + styleClass;

        writer.startElement("div", null);
        writer.writeAttribute("id", ac.getClientId(context) + "_panel", null);
        writer.writeAttribute("class", styleClass, null);

        if (ac.getPanelStyle() != null) {
            writer.writeAttribute("style", ac.getPanelStyle(), null);
        }

        writer.endElement("div");
    }

    @SuppressWarnings("unchecked")
    public void encodeResults(final FacesContext context, final UIComponent component, final String query)
        throws IOException {
        final AutoComplete ac = (AutoComplete) component;
        List results = ac.getSuggestions();
        final int maxResults = ac.getMaxResults();

        if (maxResults != Integer.MAX_VALUE && results.size() > maxResults) {
            results = results.subList(0, ac.getMaxResults());
        }

        encodeSuggestions(context, ac, results);
    }

    protected void encodeScript(final FacesContext context, final AutoComplete ac) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ac.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("AutoComplete", ac.resolveWidgetVar(), clientId, true);

        wb.attr("minLength", ac.getMinQueryLength(), 1).attr("delay", ac.getQueryDelay(), 300)
            .attr("forceSelection", ac.isForceSelection(), false).attr("global", ac.isGlobal(), true)
            .attr("scrollHeight", ac.getScrollHeight(), Integer.MAX_VALUE).attr("multiple", ac.isMultiple(), false)
            .attr("process", ac.getProcess(), null);

        final String effect = ac.getEffect();
        if (effect != null) {
            wb.attr("effect", effect, null).attr("effectDuration", ac.getEffectDuration(), Integer.MAX_VALUE);
        }

        if (ac.getFacet("itemtip") != null) {
            wb.attr("itemtip", true, false).attr("itemtipMyPosition", ac.getItemtipMyPosition(), null)
                .attr("itemtipAtPosition", ac.getItemtipAtPosition(), null);
        }

        wb.callback("onstart", "function(request)", ac.getOnstart()).callback("oncomplete", "function(response)",
                                                                              ac.getOnstart());

        encodeClientBehaviors(context, ac, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSingleMarkup(final FacesContext context, final AutoComplete ac) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ac.getClientId(context);
        String styleClass = ac.getStyleClass();
        styleClass = styleClass == null ? AutoComplete.STYLE_CLASS : AutoComplete.STYLE_CLASS + " " + styleClass;

        writer.startElement("span", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);

        if (ac.getStyle() != null) {
            writer.writeAttribute("style", ac.getStyle(), null);
        }

        encodeInput(context, ac, clientId);

        if (ac.getVar() != null) {
            encodeHiddenInput(context, ac, clientId);
        }

        if (ac.isDropdown()) {
            encodeDropDown(context, ac);
        }

        encodePanel(context, ac);

        writer.endElement("span");
    }

    protected void encodeSuggestions(final FacesContext context, final AutoComplete ac, final List items)
        throws IOException {
        final boolean customContent = ac.getColums().size() > 0;
        final Converter converter = findConverter(context, ac);

        if (customContent) {
            encodeSuggestionsAsTable(context, ac, items, converter);
        } else {
            encodeSuggestionsAsList(context, ac, items, converter);
        }
    }

    protected void encodeSuggestionsAsList(final FacesContext context,
                                           final AutoComplete ac,
                                           final List items,
                                           final Converter converter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String var = ac.getVar();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final boolean pojo = var != null;
        final UIComponent itemtip = ac.getFacet("itemtip");

        writer.startElement("ul", ac);
        writer.writeAttribute("class", AutoComplete.LIST_CLASS, null);

        for (final Object item : items) {
            writer.startElement("li", null);
            writer.writeAttribute("class", AutoComplete.ITEM_CLASS, null);

            if (pojo) {
                requestMap.put(var, item);
                final String value = converter == null ? (String) ac.getItemValue() : converter
                    .getAsString(context, ac, ac.getItemValue());
                writer.writeAttribute("data-item-value", value, null);
                writer.writeAttribute("data-item-label", ac.getItemLabel(), null);

                writer.writeText(ac.getItemLabel(), null);
            } else {
                writer.writeAttribute("data-item-label", item, null);
                writer.writeAttribute("data-item-value", item, null);

                writer.writeText(item, null);
            }

            writer.endElement("li");

            if (itemtip != null && itemtip.isRendered()) {
                writer.startElement("li", null);
                writer.writeAttribute("class", AutoComplete.ITEMTIP_CONTENT_CLASS, null);
                itemtip.encodeAll(context);
                writer.endElement("li");
            }
        }

        writer.endElement("ul");

        if (pojo) {
            requestMap.remove(var);
        }
    }

    protected void encodeSuggestionsAsTable(final FacesContext context,
                                            final AutoComplete ac,
                                            final List items,
                                            final Converter converter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String var = ac.getVar();
        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final boolean pojo = var != null;
        final UIComponent itemtip = ac.getFacet("itemtip");
        boolean hasHeader = false;

        for (final Column column : ac.getColums()) {
            if (column.isRendered() && (column.getHeaderText() != null || column.getFacet("header") != null)) {
                hasHeader = true;
                break;
            }
        }

        writer.startElement("table", ac);
        writer.writeAttribute("class", AutoComplete.TABLE_CLASS, null);

        if (hasHeader) {
            writer.startElement("thead", ac);
            for (final Column column : ac.getColums()) {
                if (!column.isRendered()) {
                    return;
                }

                final String headerText = column.getHeaderText();
                final UIComponent headerFacet = column.getFacet("header");
                final String styleClass = column.getStyleClass() == null ? "ui-state-default" : "ui-state-default "
                    + column.getStyleClass();

                writer.startElement("th", null);
                writer.writeAttribute("class", styleClass, null);

                if (column.getStyle() != null) writer.writeAttribute("style", column.getStyle(), null);

                if (headerFacet != null)
                    headerFacet.encodeAll(context);
                else if (headerText != null) writer.write(headerText);

                writer.endElement("th");
            }
            writer.endElement("thead");
        }

        writer.startElement("tbody", ac);

        for (final Object item : items) {
            writer.startElement("tr", null);
            writer.writeAttribute("class", AutoComplete.ROW_CLASS, null);

            if (pojo) {
                requestMap.put(var, item);
                final String value = converter == null ? (String) ac.getItemValue() : converter
                    .getAsString(context, ac, ac.getItemValue());
                writer.writeAttribute("data-item-value", value, null);
                writer.writeAttribute("data-item-label", ac.getItemLabel(), null);
            }

            for (final Column column : ac.getColums()) {
                if (column.isRendered()) {
                    writer.startElement("td", null);
                    if (column.getStyle() != null) writer.writeAttribute("style", column.getStyle(), null);
                    if (column.getStyleClass() != null) writer.writeAttribute("class", column.getStyleClass(), null);

                    column.encodeAll(context);

                    writer.endElement("td");
                }
            }

            if (itemtip != null && itemtip.isRendered()) {
                writer.startElement("td", null);
                writer.writeAttribute("class", AutoComplete.ITEMTIP_CONTENT_CLASS, null);
                itemtip.encodeAll(context);
                writer.endElement("td");
            }

            writer.endElement("tr");
        }

        writer.endElement("tbody");
        writer.endElement("table");
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        if (submittedValue == null || submittedValue.equals("")) {
            return null;
        }

        final AutoComplete ac = (AutoComplete) component;
        final Converter converter = findConverter(context, component);

        if (ac.isMultiple()) {
            final String[] values = (String[]) submittedValue;
            final List list = new ArrayList();

            for (final String value : values) {
                if (isValueBlank(value)) {
                    continue;
                }

                final Object convertedValue = converter != null ? converter.getAsObject(context, ac, value) : value;

                if (convertedValue != null) {
                    list.add(convertedValue);
                }
            }

            return list;
        } else {
            if (converter != null)
                return converter.getAsObject(context, component, (String) submittedValue);
            else return submittedValue;
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
