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
package org.primefaces.component.panel;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.menu.Menu;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class PanelRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Panel panel = (Panel) component;
        final String clientId = panel.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        // Restore toggle state
        final String collapsedParam = params.get(clientId + "_collapsed");
        if (collapsedParam != null) {
            panel.setCollapsed(Boolean.valueOf(collapsedParam));
        }

        // Restore visibility state
        final String visibleParam = params.get(clientId + "_visible");
        if (visibleParam != null) {
            panel.setVisible(Boolean.valueOf(visibleParam));
        }

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    protected void encodeContent(final FacesContext facesContext, final Panel panel) throws IOException {
        final ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("id", panel.getClientId() + "_content", null);
        writer.writeAttribute("class", Panel.PANEL_CONTENT_CLASS, null);
        if (panel.isCollapsed()) {
            writer.writeAttribute("style", "display:none", null);
        }

        renderChildren(facesContext, panel);

        writer.endElement("div");
    }

    @Override
    public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
        final Panel panel = (Panel) component;

        encodeMarkup(facesContext, panel);
        encodeScript(facesContext, panel);
    }

    protected void encodeFooter(final FacesContext facesContext, final Panel panel) throws IOException {
        final ResponseWriter writer = facesContext.getResponseWriter();
        final UIComponent footer = panel.getFacet("footer");
        final String footerText = panel.getFooter();

        if (footer != null || footerText != null) {
            writer.startElement("div", null);
            writer.writeAttribute("id", panel.getClientId(facesContext) + "_footer", null);
            writer.writeAttribute("class", Panel.PANEL_FOOTER_CLASS, null);

            if (footer != null) {
                renderChild(facesContext, footer);
            } else if (footerText != null) {
                writer.write(footerText);
            }

            writer.endElement("div");
        }
    }

    protected void encodeHeader(final FacesContext context, final Panel panel) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent header = panel.getFacet("header");
        final String headerText = panel.getHeader();
        final String clientId = panel.getClientId(context);

        if (headerText == null && header == null) {
            return;
        }

        writer.startElement("div", null);
        writer.writeAttribute("id", panel.getClientId(context) + "_header", null);
        writer.writeAttribute("class", Panel.PANEL_TITLEBAR_CLASS, null);

        // Title
        writer.startElement("span", null);
        writer.writeAttribute("class", Panel.PANEL_TITLE_CLASS, null);

        if (header != null) {
            renderChild(context, header);
        } else if (headerText != null) {
            writer.write(headerText);
        }

        writer.endElement("span");

        // Options
        if (panel.isClosable()) {
            encodeIcon(context, panel, "ui-icon-closethick", clientId + "_closer", panel.getCloseTitle());
        }

        if (panel.isToggleable()) {
            final String icon = panel.isCollapsed() ? "ui-icon-plusthick" : "ui-icon-minusthick";
            encodeIcon(context, panel, icon, clientId + "_toggler", panel.getToggleTitle());
        }

        if (panel.getOptionsMenu() != null) {
            encodeIcon(context, panel, "ui-icon-gear", clientId + "_menu", panel.getMenuTitle());
        }

        // Actions
        final UIComponent actionsFacet = panel.getFacet("actions");
        if (actionsFacet != null) {
            actionsFacet.encodeAll(context);
        }

        writer.endElement("div");
    }

    protected void encodeIcon(final FacesContext context,
                              final Panel panel,
                              final String iconClass,
                              final String id,
                              final String title) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("a", null);
        if (id != null) {
            writer.writeAttribute("id", id, null);
        }
        writer.writeAttribute("href", "javascript:void(0)", null);
        writer.writeAttribute("class", Panel.PANEL_TITLE_ICON_CLASS, null);
        if (title != null) {
            writer.writeAttribute("title", title, null);
        }

        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-icon " + iconClass, null);
        writer.endElement("span");

        writer.endElement("a");
    }

    protected void encodeMarkup(final FacesContext context, final Panel panel) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = panel.getClientId(context);
        final Menu optionsMenu = panel.getOptionsMenu();
        final boolean collapsed = panel.isCollapsed();
        final boolean visible = panel.isVisible();

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        String styleClass = panel.getStyleClass() == null ? Panel.PANEL_CLASS : Panel.PANEL_CLASS + " "
            + panel.getStyleClass();

        if (collapsed) {
            styleClass += " ui-hidden-container";

            if (panel.getToggleOrientation().equals("horizontal")) {
                styleClass += " ui-panel-collapsed-h";
            }
        }

        if (!visible) {
            styleClass += " ui-helper-hidden";
        }

        writer.writeAttribute("class", styleClass, "styleClass");

        if (panel.getStyle() != null) {
            writer.writeAttribute("style", panel.getStyle(), "style");
        }

        encodeHeader(context, panel);
        encodeContent(context, panel);
        encodeFooter(context, panel);

        if (panel.isToggleable()) {
            encodeStateHolder(context, panel, clientId + "_collapsed", String.valueOf(collapsed));
        }

        if (panel.isClosable()) {
            encodeStateHolder(context, panel, clientId + "_visible", String.valueOf(visible));
        }

        if (optionsMenu != null) {
            optionsMenu.setOverlay(true);
            optionsMenu.setTrigger(clientId + "_menu");
            optionsMenu.setMy("left top");
            optionsMenu.setAt("left bottom");

            optionsMenu.encodeAll(context);
        }

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final Panel panel) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = panel.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Panel", panel.resolveWidgetVar(), clientId, false);

        if (panel.isToggleable()) {
            wb.attr("toggleable", true).attr("toggleSpeed", panel.getToggleSpeed()).attr("collapsed",
                                                                                         panel.isCollapsed())
                .attr("toggleOrientation", panel.getToggleOrientation());
        }

        if (panel.isClosable()) {
            wb.attr("closable", true).attr("closeSpeed", panel.getCloseSpeed());
        }

        if (panel.getOptionsMenu() != null) {
            wb.attr("hasMenu", true);
        }

        encodeClientBehaviors(context, panel, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeStateHolder(final FacesContext context,
                                     final Panel panel,
                                     final String name,
                                     final String value) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", name, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("value", value, null);
        writer.endElement("input");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
