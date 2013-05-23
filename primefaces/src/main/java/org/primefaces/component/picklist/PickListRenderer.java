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
package org.primefaces.component.picklist;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.primefaces.component.column.Column;
import org.primefaces.model.DualListModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.renderkit.RendererUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class PickListRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final PickList pickList = (PickList) component;
        final String clientId = pickList.getClientId(context);
        final Map<String, String[]> params = context.getExternalContext().getRequestParameterValuesMap();

        final String sourceParamKey = clientId + "_source";
        final String targetParamKey = clientId + "_target";

        final String[] sourceParam = params.containsKey(sourceParamKey) ? params.get(sourceParamKey) : new String[] {};
        final String[] targetParam = params.containsKey(targetParamKey) ? params.get(targetParamKey) : new String[] {};

        pickList.setSubmittedValue(new String[][] { sourceParam, targetParam });

        decodeBehaviors(context, pickList);
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
        writer.writeAttribute("class", PickList.CAPTION_CLASS, null);
        caption.encodeAll(context);
        writer.endElement("div");
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
        final PickList pickList = (PickList) component;

        encodeMarkup(facesContext, pickList);
        encodeScript(facesContext, pickList);
    }

    protected void encodeFilter(final FacesContext context, final PickList pickList, final String name)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", PickList.FILTER_CONTAINER, null);

        writer.startElement("input", null);
        writer.writeAttribute("id", name, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("class", PickList.FILTER_CLASS, null);
        writer.endElement("input");

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-icon ui-icon-search", null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeList(final FacesContext context,
                              final PickList pickList,
                              final String listId,
                              String styleClass,
                              final List model,
                              final UIComponent caption,
                              final boolean filter) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", null);

        if (filter) {
            encodeFilter(context, pickList, listId + "_filter");
        }

        if (caption != null) {
            encodeCaption(context, caption);
            styleClass += " ui-corner-bottom";
        } else {
            styleClass += " ui-corner-all";
        }

        writer.startElement("ul", null);
        writer.writeAttribute("class", styleClass, null);

        encodeOptions(context, pickList, model);

        writer.endElement("ul");

        encodeListInput(context, listId);

        writer.endElement("td");
    }

    protected void encodeListControls(final FacesContext context, final PickList pickList, final String styleClass)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", null);
        writer.writeAttribute("class", styleClass, null);
        encodeButton(context, pickList.getMoveUpLabel(), PickList.MOVE_UP_BUTTON_CLASS,
                     PickList.MOVE_UP_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getMoveTopLabel(), PickList.MOVE_TOP_BUTTON_CLASS,
                     PickList.MOVE_TOP_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getMoveDownLabel(), PickList.MOVE_DOWN_BUTTON_CLASS,
                     PickList.MOVE_DOWN_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getMoveBottomLabel(), PickList.MOVE_BOTTOM_BUTTON_CLASS,
                     PickList.MOVE_BOTTOM_BUTTON_ICON_CLASS);
        writer.endElement("td");
    }

    protected void encodeListInput(final FacesContext context, final String clientId) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("select", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("multiple", "true", null);
        writer.writeAttribute("class", "ui-helper-hidden", null);

        // items generated on client side

        writer.endElement("select");
    }

    protected void encodeMarkup(final FacesContext context, final PickList pickList) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = pickList.getClientId(context);
        final DualListModel model = (DualListModel) pickList.getValue();
        String styleClass = pickList.getStyleClass();
        styleClass = styleClass == null ? PickList.CONTAINER_CLASS : PickList.CONTAINER_CLASS + " " + styleClass;

        writer.startElement("table", pickList);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, null);
        if (pickList.getStyle() != null) {
            writer.writeAttribute("style", pickList.getStyle(), null);
        }

        writer.startElement("tbody", null);
        writer.startElement("tr", null);

        // Target List Reorder Buttons
        if (pickList.isShowSourceControls()) {
            encodeListControls(context, pickList, PickList.SOURCE_CONTROLS);
        }

        // Source List
        encodeList(context, pickList, clientId + "_source", PickList.SOURCE_CLASS, model.getSource(), pickList
            .getFacet("sourceCaption"), pickList.isShowSourceFilter());

        // Buttons
        writer.startElement("td", null);
        encodeButton(context, pickList.getAddLabel(), PickList.ADD_BUTTON_CLASS, PickList.ADD_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getAddAllLabel(), PickList.ADD_ALL_BUTTON_CLASS,
                     PickList.ADD_ALL_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getRemoveLabel(), PickList.REMOVE_BUTTON_CLASS,
                     PickList.REMOVE_BUTTON_ICON_CLASS);
        encodeButton(context, pickList.getRemoveAllLabel(), PickList.REMOVE_ALL_BUTTON_CLASS,
                     PickList.REMOVE_ALL_BUTTON_ICON_CLASS);
        writer.endElement("td");

        // Target List
        encodeList(context, pickList, clientId + "_target", PickList.TARGET_CLASS, model.getTarget(), pickList
            .getFacet("targetCaption"), pickList.isShowTargetFilter());

        // Target List Reorder Buttons
        if (pickList.isShowTargetControls()) {
            encodeListControls(context, pickList, PickList.TARGET_CONTROLS);
        }

        writer.endElement("tr");
        writer.endElement("tbody");

        writer.endElement("table");
    }

    @SuppressWarnings("unchecked")
    protected void encodeOptions(final FacesContext context, final PickList pickList, final List model)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String var = pickList.getVar();
        final Converter converter = pickList.getConverter();
        final boolean showCheckbox = pickList.isShowCheckbox();

        for (final Iterator it = model.iterator(); it.hasNext();) {
            final Object item = it.next();
            context.getExternalContext().getRequestMap().put(var, item);
            final String itemValue = converter != null ? converter.getAsString(context, pickList, pickList
                .getItemValue()) : pickList.getItemValue().toString();
            final String itemLabel = pickList.getItemLabel();
            final String itemClass = pickList.isItemDisabled() ? PickList.ITEM_CLASS + " "
                + PickList.ITEM_DISABLED_CLASS : PickList.ITEM_CLASS;

            writer.startElement("li", null);
            writer.writeAttribute("class", itemClass, null);
            writer.writeAttribute("data-item-value", itemValue, null);
            writer.writeAttribute("data-item-label", itemLabel, null);

            if (pickList.getChildCount() > 0) {
                writer.startElement("table", null);
                writer.startElement("tbody", null);
                writer.startElement("tr", null);

                if (showCheckbox) {
                    writer.startElement("td", null);
                    RendererUtils.encodeCheckbox(context, false);
                    writer.endElement("td");
                }

                for (final UIComponent kid : pickList.getChildren()) {
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
                if (showCheckbox) {
                    RendererUtils.encodeCheckbox(context, false);
                }

                writer.writeText(itemLabel, null);
            }

            writer.endElement("li");
        }

        context.getExternalContext().getRequestMap().remove(var);
    }

    protected void encodeScript(final FacesContext context, final PickList pickList) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = pickList.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("PickList", pickList.resolveWidgetVar(), clientId, true).attr("effect", pickList.getEffect())
            .attr("effectSpeed", pickList.getEffectSpeed()).attr("showSourceControls", pickList.isShowSourceControls(),
                                                                 false).attr("showTargetControls",
                                                                             pickList.isShowTargetControls(), false)
            .attr("disabled", pickList.isDisabled(), false).attr("filterMatchModel", pickList.getFilterMatchMode(),
                                                                 null).attr("filterFunction",
                                                                            pickList.getFilterFunction(), null)
            .attr("showCheckbox", pickList.isShowCheckbox(), false).callback("onTransfer", "function(e)",
                                                                             pickList.getOnTransfer());

        encodeClientBehaviors(context, pickList, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        try {
            final PickList pickList = (PickList) component;
            final String[][] value = (String[][]) submittedValue;
            final String[] sourceValue = value[0];
            final String[] targetValue = value[1];
            final DualListModel model = new DualListModel();

            pickList.populateModel(context, sourceValue, model.getSource());
            pickList.populateModel(context, targetValue, model.getTarget());

            return model;
        } catch (final Exception exception) {
            throw new ConverterException(exception);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}