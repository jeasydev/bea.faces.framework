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
package org.primefaces.component.orderlist;

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
import org.primefaces.component.column.Column;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class OrderListRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final OrderList pickList = (OrderList) component;
        final Map<String, String[]> params = context.getExternalContext().getRequestParameterValuesMap();
        final String values = pickList.getClientId(context) + "_values";

        if (values != null) {
            pickList.setSubmittedValue(params.get(values));
        }
    }

    protected void encodeButton(final FacesContext context,
                                final String title,
                                final String styleClass,
                                final String icon) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("button", null);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", HTML.BUTTON_ICON_ONLY_BUTTON_CLASS + " " + styleClass, null);
        writer.writeAttribute("title", title, null);

        // icon
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_LEFT_ICON_CLASS + " " + icon, null);
        writer.endElement("span");

        // text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.write("ui-button");
        writer.endElement("span");

        writer.endElement("button");
    }

    protected void encodeCaption(final FacesContext context, final UIComponent caption) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", OrderList.CAPTION_CLASS, null);
        caption.encodeAll(context);
        writer.endElement("div");
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    protected void encodeControls(final FacesContext context, final OrderList ol) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", null);
        writer.writeAttribute("class", OrderList.CONTROLS_CLASS, null);
        encodeButton(context, ol.getMoveUpLabel(), OrderList.MOVE_UP_BUTTON_CLASS, OrderList.MOVE_UP_BUTTON_ICON_CLASS);
        encodeButton(context, ol.getMoveTopLabel(), OrderList.MOVE_TOP_BUTTON_CLASS,
                     OrderList.MOVE_TOP_BUTTON_ICON_CLASS);
        encodeButton(context, ol.getMoveDownLabel(), OrderList.MOVE_DOWN_BUTTON_CLASS,
                     OrderList.MOVE_DOWN_BUTTON_ICON_CLASS);
        encodeButton(context, ol.getMoveBottomLabel(), OrderList.MOVE_BOTTOM_BUTTON_CLASS,
                     OrderList.MOVE_BOTTOM_BUTTON_ICON_CLASS);
        writer.endElement("td");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final OrderList ol = (OrderList) component;

        encodeMarkup(context, ol);
        encodeScript(context, ol);
    }

    protected void encodeInput(final FacesContext context, final String clientId) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("select", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("multiple", "true", null);
        writer.writeAttribute("class", "ui-helper-hidden", null);

        // options generated at client side

        writer.endElement("select");
    }

    protected void encodeList(final FacesContext context, final OrderList ol) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ol.getClientId(context);
        final UIComponent caption = ol.getFacet("caption");
        String listStyleClass = OrderList.LIST_CLASS;

        writer.startElement("td", null);

        if (caption != null) {
            encodeCaption(context, caption);
            listStyleClass += " ui-corner-bottom";
        } else {
            listStyleClass += " ui-corner-all";
        }

        writer.startElement("ul", null);
        writer.writeAttribute("class", listStyleClass, null);

        encodeOptions(context, ol, (List) ol.getValue());

        writer.endElement("ul");

        encodeInput(context, clientId + "_values");

        writer.endElement("td");
    }

    protected void encodeMarkup(final FacesContext context, final OrderList ol) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ol.getClientId(context);
        final String controlsLocation = ol.getControlsLocation();
        final String style = ol.getStyle();
        String styleClass = ol.getStyleClass();
        styleClass = styleClass == null ? OrderList.CONTAINER_CLASS : OrderList.CONTAINER_CLASS + " " + styleClass;

        if (ol.isDisabled()) {
            styleClass = styleClass + " ui-state-disabled";
        }

        writer.startElement("table", ol);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        writer.startElement("tbody", null);
        writer.startElement("tr", null);

        if (controlsLocation.equals("left")) {
            encodeControls(context, ol);
        }

        encodeList(context, ol);

        if (controlsLocation.equals("right")) {
            encodeControls(context, ol);
        }

        writer.endElement("tr");
        writer.endElement("tbody");
        writer.endElement("table");
    }

    @SuppressWarnings("unchecked")
    protected void encodeOptions(final FacesContext context, final OrderList old, final List model) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String var = old.getVar();
        final Converter converter = old.getConverter();

        for (final Iterator it = model.iterator(); it.hasNext();) {
            final Object item = it.next();
            context.getExternalContext().getRequestMap().put(var, item);
            final String value = converter != null ? converter.getAsString(context, old, old.getItemValue()) : old
                .getItemValue().toString();

            writer.startElement("li", null);
            writer.writeAttribute("class", OrderList.ITEM_CLASS, null);
            writer.writeAttribute("data-item-value", value, null);

            if (old.getChildCount() > 0) {

                writer.startElement("table", null);
                writer.startElement("tbody", null);
                writer.startElement("tr", null);

                for (final UIComponent kid : old.getChildren()) {
                    if (kid instanceof Column && kid.isRendered()) {
                        final Column column = (Column) kid;

                        writer.startElement("td", null);
                        if (column.getStyle() != null) writer.writeAttribute("style", column.getStyle(), null);
                        if (column.getStyleClass() != null)
                            writer.writeAttribute("class", column.getStyleClass(), null);

                        kid.encodeAll(context);
                        writer.endElement("td");
                    }
                }

                writer.endElement("tr");
                writer.endElement("tbody");
                writer.endElement("table");
            } else {
                writer.writeText(old.getItemLabel(), null);
            }

            writer.endElement("li");
        }

        context.getExternalContext().getRequestMap().remove(var);
    }

    protected void encodeScript(final FacesContext context, final OrderList ol) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ol.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("OrderList", ol.resolveWidgetVar(), clientId, false).attr("effect", ol.getEffect(), null);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        try {
            final OrderList ol = (OrderList) component;
            final List orderedList = new ArrayList();
            final Converter converter = ol.getConverter();
            final String[] values = (String[]) submittedValue;

            for (final String item : values) {
                if (isValueBlank(item)) continue;

                final Object convertedValue = converter != null ? converter.getAsObject(context, ol, item) : item;

                if (convertedValue != null) orderedList.add(convertedValue);
            }

            return orderedList;
        } catch (final Exception exception) {
            throw new ConverterException(exception);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
