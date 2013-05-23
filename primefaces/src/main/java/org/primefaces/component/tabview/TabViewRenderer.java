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
package org.primefaces.component.tabview;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class TabViewRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final TabView tabView = (TabView) component;
        final String activeIndexValue = params.get(tabView.getClientId(context) + "_activeIndex");

        if (!isValueEmpty(activeIndexValue)) {
            tabView.setActiveIndex(Integer.parseInt(activeIndexValue));
        }

        decodeBehaviors(context, component);
    }

    protected void encodeActiveIndexHolder(final FacesContext facesContext, final TabView tabView) throws IOException {
        final ResponseWriter writer = facesContext.getResponseWriter();
        final String paramName = tabView.getClientId(facesContext) + "_activeIndex";

        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", paramName, null);
        writer.writeAttribute("name", paramName, null);
        writer.writeAttribute("value", tabView.getActiveIndex(), null);
        writer.writeAttribute("autocomplete", "off", null);
        writer.endElement("input");
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    protected void encodeContents(final FacesContext context, final TabView tabView) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String var = tabView.getVar();
        int activeIndex = tabView.getActiveIndex();
        final boolean dynamic = tabView.isDynamic();

        writer.startElement("div", null);
        writer.writeAttribute("class", TabView.PANELS_CLASS, null);

        if (var == null) {
            int i = 0;
            for (final UIComponent kid : tabView.getChildren()) {
                if (kid.isRendered() && kid instanceof Tab) {
                    encodeTabContent(context, (Tab) kid, (i == activeIndex), dynamic);
                    i++;
                }
            }
        } else {
            final int dataCount = tabView.getRowCount();

            // boundary check
            activeIndex = activeIndex >= dataCount ? 0 : activeIndex;

            final Tab tab = (Tab) tabView.getChildren().get(0);

            for (int i = 0; i < dataCount; i++) {
                tabView.setRowIndex(i);

                encodeTabContent(context, tab, (i == activeIndex), dynamic);
            }

            tabView.setRowIndex(-1);
        }

        writer.endElement("div");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final TabView tabView = (TabView) component;
        final String clientId = tabView.getClientId(context);
        final String var = tabView.getVar();

        if (tabView.isContentLoadRequest(context)) {
            Tab tabToLoad = null;

            if (var == null) {
                final String tabClientId = params.get(clientId + "_newTab");
                tabToLoad = tabView.findTab(tabClientId);

                tabToLoad.encodeAll(context);
                tabToLoad.setLoaded(true);
            } else {
                final int tabindex = Integer.parseInt(params.get(clientId + "_tabindex"));
                tabView.setRowIndex(tabindex);
                tabToLoad = (Tab) tabView.getChildren().get(0);
                tabToLoad.encodeAll(context);
                tabView.setRowIndex(-1);
            }

        } else {
            encodeMarkup(context, tabView);
            encodeScript(context, tabView);
        }
    }

    protected void encodeHeaders(final FacesContext context, final TabView tabView) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String var = tabView.getVar();
        int activeIndex = tabView.getActiveIndex();

        writer.startElement("ul", null);
        writer.writeAttribute("class", TabView.NAVIGATOR_CLASS, null);
        writer.writeAttribute("role", "tablist", null);

        if (var == null) {
            int i = 0;
            for (final UIComponent kid : tabView.getChildren()) {
                if (kid.isRendered() && kid instanceof Tab) {
                    encodeTabHeader(context, tabView, (Tab) kid, (i == activeIndex));
                    i++;
                }
            }
        } else {
            final int dataCount = tabView.getRowCount();

            // boundary check
            activeIndex = activeIndex >= dataCount ? 0 : activeIndex;

            final Tab tab = (Tab) tabView.getChildren().get(0);

            for (int i = 0; i < dataCount; i++) {
                tabView.setRowIndex(i);

                encodeTabHeader(context, tabView, tab, (i == activeIndex));
            }

            tabView.setRowIndex(-1);
        }

        writer.endElement("ul");
    }

    protected void encodeMarkup(final FacesContext context, final TabView tabView) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = tabView.getClientId(context);
        final String orientation = tabView.getOrientation();
        String styleClass = tabView.getStyleClass();
        final String defaultStyleClass = TabView.CONTAINER_CLASS + " ui-tabs-" + orientation;
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;

        if (ComponentUtils.isRTL(context, tabView)) {
            styleClass += " ui-tabs-rtl";
        }

        writer.startElement("div", tabView);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        if (tabView.getStyle() != null) {
            writer.writeAttribute("style", tabView.getStyle(), "style");
        }

        if (orientation.equals("bottom")) {
            encodeContents(context, tabView);
            encodeHeaders(context, tabView);

        } else {
            encodeHeaders(context, tabView);
            encodeContents(context, tabView);
        }

        encodeActiveIndexHolder(context, tabView);

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final TabView tabView) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = tabView.getClientId(context);
        final boolean dynamic = tabView.isDynamic();

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("TabView", tabView.resolveWidgetVar(), clientId, false);

        if (dynamic) {
            wb.attr("dynamic", true).attr("cache", tabView.isCache());
        }

        wb.callback("onTabChange", "function(index)", tabView.getOnTabChange()).callback("onTabShow",
                                                                                         "function(index)",
                                                                                         tabView.getOnTabShow())
            .callback("onTabClose", "function(index)", tabView.getOnTabClose());

        wb.attr("effect", tabView.getEffect(), null).attr("effectDuration", tabView.getEffectDuration(), null);

        encodeClientBehaviors(context, tabView, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeTabContent(final FacesContext context,
                                    final Tab tab,
                                    final boolean active,
                                    final boolean dynamic) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String styleClass = active ? TabView.ACTIVE_TAB_CONTENT_CLASS : TabView.INACTIVE_TAB_CONTENT_CLASS;

        writer.startElement("div", null);
        writer.writeAttribute("id", tab.getClientId(context), null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("role", "tabpanel", null);
        writer.writeAttribute("aria-hidden", String.valueOf(!active), null);

        if (dynamic) {
            if (active) {
                tab.encodeAll(context);
                tab.setLoaded(true);
            }
        } else {
            tab.encodeAll(context);
        }

        writer.endElement("div");
    }

    protected void encodeTabHeader(final FacesContext context,
                                   final TabView tabView,
                                   final Tab tab,
                                   final boolean active) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String defaultStyleClass = active ? TabView.ACTIVE_TAB_HEADER_CLASS : TabView.INACTIVE_TAB_HEADER_CLASS;
        defaultStyleClass = defaultStyleClass + " ui-corner-" + tabView.getOrientation(); // cornering
        String styleClass = tab.getTitleStyleClass();
        styleClass = tab.isDisabled() ? styleClass + " ui-state-disabled" : styleClass;
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;
        final UIComponent titleFacet = tab.getFacet("title");

        // header container
        writer.startElement("li", null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("role", "tab", null);
        writer.writeAttribute("aria-expanded", String.valueOf(active), null);
        if (tab.getTitleStyle() != null) writer.writeAttribute("style", tab.getTitleStyle(), null);
        if (tab.getTitletip() != null) writer.writeAttribute("title", tab.getTitletip(), null);

        // title
        writer.startElement("a", null);
        writer.writeAttribute("href", "#" + tab.getClientId(context), null);
        if (titleFacet == null) {
            writer.write(tab.getTitle());
        } else {
            titleFacet.encodeAll(context);
        }
        writer.endElement("a");

        // closable
        if (tab.isClosable()) {
            writer.startElement("span", null);
            writer.writeAttribute("class", "ui-icon ui-icon-close", null);
            writer.endElement("span");
        }

        writer.endElement("li");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}