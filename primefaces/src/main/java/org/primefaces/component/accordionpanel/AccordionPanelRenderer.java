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
package org.primefaces.component.accordionpanel;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.tabview.Tab;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class AccordionPanelRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final AccordionPanel acco = (AccordionPanel) component;
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String active = params.get(acco.getClientId(context) + "_active");

        if (active != null) {
            if (isValueBlank(active)) {
                acco.setActiveIndex(null);
            } else {
                acco.setActiveIndex(active);
            }
        }

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final AccordionPanel acco = (AccordionPanel) component;
        final String clientId = acco.getClientId(context);
        final String var = acco.getVar();

        if (acco.isContentLoadRequest(context)) {
            if (var == null) {
                final String tabClientId = params.get(clientId + "_newTab");
                final Tab tabToLoad = acco.findTab(tabClientId);
                tabToLoad.encodeAll(context);
                tabToLoad.setLoaded(true);
            } else {
                final int index = Integer.parseInt(params.get(clientId + "_tabindex"));
                acco.setRowIndex(index);
                acco.getChildren().get(0).encodeAll(context);
                acco.setRowIndex(-1);
            }
        } else {
            encodeMarkup(context, acco);
            encodeScript(context, acco);
        }
    }

    protected void encodeMarkup(final FacesContext context, final AccordionPanel acco) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = acco.getClientId(context);
        String styleClass = acco.getStyleClass();
        styleClass = styleClass == null ? AccordionPanel.CONTAINER_CLASS : AccordionPanel.CONTAINER_CLASS + " "
            + styleClass;

        if (ComponentUtils.isRTL(context, acco)) {
            styleClass = styleClass + " ui-accordion-rtl";
        }

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        if (acco.getStyle() != null) {
            writer.writeAttribute("style", acco.getStyle(), null);
        }

        writer.writeAttribute("role", "tablist", null);

        encodeTabs(context, acco);

        encodeStateHolder(context, acco);

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final AccordionPanel acco) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = acco.getClientId(context);
        final boolean multiple = acco.isMultiple();
        final String activeIndex = acco.getActiveIndex();
        final String activeIndexValue = multiple ? "[" + activeIndex + "]" : activeIndex;

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("AccordionPanel", acco.resolveWidgetVar(), clientId, false);

        wb.nativeAttr("active", activeIndexValue);

        if (acco.isDynamic()) {
            wb.attr("dynamic", true, false);
            wb.attr("cache", acco.isCache(), true);
        }

        wb.attr("multiple", multiple, false).callback("onTabChange", "function(panel)", acco.getOnTabChange())
            .callback("onTabShow", "function(panel)", acco.getOnTabShow());

        encodeClientBehaviors(context, acco, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeStateHolder(final FacesContext context, final AccordionPanel accordionPanel)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = accordionPanel.getClientId(context);
        final String stateHolderId = clientId + "_active";

        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", stateHolderId, null);
        writer.writeAttribute("name", stateHolderId, null);
        writer.writeAttribute("value", accordionPanel.getActiveIndex(), null);
        writer.endElement("input");
    }

    protected void encodeTab(final FacesContext context,
                             final Tab tab,
                             final boolean active,
                             final boolean dynamic,
                             final boolean rtl) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        String headerClass = active ? AccordionPanel.ACTIVE_TAB_HEADER_CLASS : AccordionPanel.TAB_HEADER_CLASS;
        headerClass = tab.isDisabled() ? headerClass + " ui-state-disabled" : headerClass;
        headerClass = tab.getTitleStyleClass() == null ? headerClass : headerClass + " " + tab.getTitleStyleClass();
        final String iconClass = active
                                       ? AccordionPanel.ACTIVE_TAB_HEADER_ICON_CLASS
                                       : (rtl
                                             ? AccordionPanel.TAB_HEADER_ICON_RTL_CLASS
                                             : AccordionPanel.TAB_HEADER_ICON_CLASS);
        final String contentClass = active
                                          ? AccordionPanel.ACTIVE_TAB_CONTENT_CLASS
                                          : AccordionPanel.INACTIVE_TAB_CONTENT_CLASS;
        final UIComponent titleFacet = tab.getFacet("title");

        // header container
        writer.startElement("h3", null);
        writer.writeAttribute("class", headerClass, null);
        writer.writeAttribute("role", "tab", null);
        writer.writeAttribute("aria-expanded", String.valueOf(active), null);
        if (tab.getTitleStyle() != null) writer.writeAttribute("style", tab.getTitleStyle(), null);
        if (tab.getTitletip() != null) writer.writeAttribute("title", tab.getTitletip(), null);

        // icon
        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.startElement("a", null);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("tabindex", "-1", null);
        if (titleFacet == null) {
            writer.write(tab.getTitle());
        } else {
            titleFacet.encodeAll(context);
        }
        writer.endElement("a");

        writer.endElement("h3");

        // content
        writer.startElement("div", null);
        writer.writeAttribute("id", tab.getClientId(context), null);
        writer.writeAttribute("class", contentClass, null);
        writer.writeAttribute("role", "tabpanel", null);
        writer.writeAttribute("aria-hidden", String.valueOf(!active), null);

        if (dynamic) {
            if (active) {
                tab.encodeAll(context);
                tab.setLoaded(true);
            }
        } else tab.encodeAll(context);

        writer.endElement("div");
    }

    protected void encodeTabs(final FacesContext context, final AccordionPanel acco) throws IOException {
        final boolean dynamic = acco.isDynamic();
        final boolean multiple = acco.isMultiple();
        final String var = acco.getVar();
        final String activeIndex = acco.getActiveIndex();
        final boolean rtl = acco.getDir().equalsIgnoreCase("rtl");

        if (var == null) {
            int i = 0;

            for (final UIComponent child : acco.getChildren()) {
                if (child.isRendered() && child instanceof Tab) {
                    final boolean active = multiple ? activeIndex.indexOf(String.valueOf(i)) != -1 : activeIndex
                        .equals(String.valueOf(i));

                    encodeTab(context, (Tab) child, active, dynamic, rtl);

                    i++;
                }
            }
        } else {
            final int dataCount = acco.getRowCount();
            final Tab tab = (Tab) acco.getChildren().get(0);

            for (int i = 0; i < dataCount; i++) {
                acco.setRowIndex(i);
                final boolean active = multiple ? activeIndex.indexOf(String.valueOf(i)) != -1 : activeIndex
                    .equals(String.valueOf(i));

                encodeTab(context, tab, active, dynamic, rtl);
            }

            acco.setRowIndex(-1);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}