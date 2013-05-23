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
package org.primefaces.component.menu;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.util.WidgetBuilder;

public class MenuRenderer extends BaseMenuRenderer {

    protected void encodeContent(final FacesContext context, final UIComponent component) throws IOException {
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
                    encodeSubmenu(context, (Submenu) child);
                } else if (child instanceof Separator) {
                    encodeSeparator(context, (Separator) child);
                }
            }
        }
    }

    @Override
    protected void encodeMarkup(final FacesContext context, final AbstractMenu abstractMenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Menu menu = (Menu) abstractMenu;
        final String clientId = menu.getClientId(context);
        final String style = menu.getStyle();
        String styleClass = menu.getStyleClass();
        final String defaultStyleClass = menu.isOverlay() ? Menu.DYNAMIC_CONTAINER_CLASS : Menu.STATIC_CONTAINER_CLASS;
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;

        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }
        writer.writeAttribute("role", "menu", null);

        writer.startElement("ul", null);
        writer.writeAttribute("class", AbstractMenu.LIST_CLASS, null);

        encodeContent(context, menu);

        writer.endElement("ul");

        writer.endElement("div");
    }

    @Override
    protected void encodeScript(final FacesContext context, final AbstractMenu abstractMenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Menu menu = (Menu) abstractMenu;
        final String clientId = menu.getClientId(context);

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("PlainMenu", menu.resolveWidgetVar(), clientId, true);

        if (menu.isOverlay()) {
            encodeOverlayConfig(context, menu, wb);
        }

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSubmenu(final FacesContext context, final Submenu submenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String label = submenu.getLabel();
        final String style = submenu.getStyle();
        String styleClass = submenu.getStyleClass();
        styleClass = styleClass == null ? Menu.SUBMENU_TITLE_CLASS : Menu.SUBMENU_TITLE_CLASS + " " + styleClass;

        // title
        writer.startElement("li", null);
        if (shouldWriteId(submenu)) {
            writer.writeAttribute("id", submenu.getClientId(context), null);
        }
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        writer.startElement("h3", null);
        if (label != null) {
            writer.writeText(label, "value");
        }
        writer.endElement("h3");

        writer.endElement("li");

        encodeContent(context, submenu);
    }
}