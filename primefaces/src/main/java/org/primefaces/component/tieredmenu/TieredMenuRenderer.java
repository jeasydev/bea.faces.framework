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
package org.primefaces.component.tieredmenu;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.component.menu.BaseMenuRenderer;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.util.WidgetBuilder;

public class TieredMenuRenderer extends BaseMenuRenderer {

    @Override
    protected void encodeMarkup(final FacesContext context, final AbstractMenu abstractMenu) throws IOException {
        final TieredMenu menu = (TieredMenu) abstractMenu;
        final String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        final String defaultStyleClass = menu.isOverlay()
                                                         ? TieredMenu.DYNAMIC_CONTAINER_CLASS
                                                         : TieredMenu.STATIC_CONTAINER_CLASS;
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;

        encodeMenu(context, menu, style, styleClass, "menu");
    }

    protected void encodeMenu(final FacesContext context,
                              final AbstractMenu component,
                              final String style,
                              final String styleClass,
                              final String role) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent optionsFacet = component.getFacet("options");

        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(context), "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("role", "menubar", null);

        writer.startElement("ul", null);
        writer.writeAttribute("class", AbstractMenu.LIST_CLASS, null);

        encodeMenuContent(context, component);

        if (optionsFacet != null) {
            writer.startElement("li", null);
            writer.writeAttribute("class", AbstractMenu.OPTIONS_CLASS, null);
            writer.writeAttribute("role", "menuitem", null);
            optionsFacet.encodeAll(context);
            writer.endElement("li");
        }

        writer.endElement("ul");

        writer.endElement("div");
    }

    protected void encodeMenuContent(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        for (UIComponent child : component.getChildren()) {
            if (child.isRendered()) {

                if (child instanceof MenuItem) {
                    writer.startElement("li", null);
                    writer.writeAttribute("class", AbstractMenu.MENUITEM_CLASS, null);
                    writer.writeAttribute("role", "menuitem", null);
                    encodeMenuItem(context, (MenuItem) child);
                    writer.endElement("li");
                } else if (child instanceof Submenu) {
                    final Submenu submenu = (Submenu) child;
                    final String style = submenu.getStyle();
                    String styleClass = submenu.getStyleClass();
                    styleClass = styleClass == null
                                                   ? AbstractMenu.TIERED_SUBMENU_CLASS
                                                   : AbstractMenu.TIERED_SUBMENU_CLASS + " " + styleClass;

                    writer.startElement("li", null);
                    if (shouldWriteId(submenu)) {
                        writer.writeAttribute("id", submenu.getClientId(context), null);
                    }
                    writer.writeAttribute("class", styleClass, null);
                    if (style != null) {
                        writer.writeAttribute("style", style, null);
                    }
                    writer.writeAttribute("role", "menuitem", null);
                    writer.writeAttribute("aria-haspopup", "true", null);
                    encodeSubmenu(context, (Submenu) child);
                    writer.endElement("li");
                } else if (child instanceof Separator) {
                    encodeSeparator(context, (Separator) child);
                }
            }
        }
    }

    @Override
    protected void encodeScript(final FacesContext context, final AbstractMenu abstractMenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final TieredMenu menu = (TieredMenu) abstractMenu;
        final String clientId = menu.getClientId(context);

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("TieredMenu", menu.resolveWidgetVar(), clientId, true).attr("autoDisplay", menu.isAutoDisplay());

        if (menu.isOverlay()) {
            encodeOverlayConfig(context, menu, wb);
        }

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSubmenu(final FacesContext context, final Submenu submenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String icon = submenu.getIcon();
        final String label = submenu.getLabel();

        // title
        writer.startElement("a", null);
        writer.writeAttribute("href", "javascript:void(0)", null);
        writer.writeAttribute("class", AbstractMenu.MENUITEM_LINK_CLASS, null);

        if (icon != null) {
            writer.startElement("span", null);
            writer.writeAttribute("class", AbstractMenu.MENUITEM_ICON_CLASS + " " + icon, null);
            writer.endElement("span");
        }

        if (label != null) {
            writer.startElement("span", null);
            writer.writeAttribute("class", AbstractMenu.MENUITEM_TEXT_CLASS, null);
            writer.writeText(submenu.getLabel(), "value");
            writer.endElement("span");
        }

        encodeSubmenuIcon(context, submenu);

        writer.endElement("a");

        // submenus and menuitems
        if (submenu.getChildCount() > 0) {
            writer.startElement("ul", null);
            writer.writeAttribute("class", AbstractMenu.TIERED_CHILD_SUBMENU_CLASS, null);
            writer.writeAttribute("role", "menu", null);

            encodeMenuContent(context, submenu);

            writer.endElement("ul");
        }
    }

    protected void encodeSubmenuIcon(final FacesContext context, final Submenu submenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("span", null);
        writer.writeAttribute("class", AbstractMenu.SUBMENU_RIGHT_ICON_CLASS, null);
        writer.endElement("span");
    }
}