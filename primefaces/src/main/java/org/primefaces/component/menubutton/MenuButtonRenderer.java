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
package org.primefaces.component.menubutton;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.component.menu.BaseMenuRenderer;
import org.primefaces.component.menu.Menu;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class MenuButtonRenderer extends BaseMenuRenderer {

    protected void encodeButton(final FacesContext context,
                                final MenuButton button,
                                final String buttonId,
                                final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String value = button.getValue();
        final String buttonClass = disabled
                                           ? HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS + " ui-state-disabled"
                                           : HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS;

        writer.startElement("button", null);
        writer.writeAttribute("id", buttonId, null);
        writer.writeAttribute("name", buttonId, null);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", buttonClass, buttonId);
        if (button.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }

        // button icon
        final String iconClass = HTML.BUTTON_LEFT_ICON_CLASS + " ui-icon-triangle-1-s";
        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        // text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);

        if (value == null)
            writer.write("ui-button");
        else writer.writeText(value, "value");

        writer.endElement("span");

        writer.endElement("button");
    }

    @Override
    protected void encodeMarkup(final FacesContext context, final AbstractMenu abstractMenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final MenuButton button = (MenuButton) abstractMenu;
        final String clientId = button.getClientId(context);
        String styleClass = button.getStyleClass();
        styleClass = styleClass == null ? MenuButton.CONTAINER_CLASS : MenuButton.CONTAINER_CLASS + " " + styleClass;
        final boolean disabled = button.isDisabled();

        writer.startElement("span", button);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "class");
        if (button.getStyle() != null) {
            writer.writeAttribute("style", button.getStyle(), "style");
        }

        encodeButton(context, button, clientId + "_button", disabled);
        if (!disabled) {
            encodeMenu(context, button, clientId + "_menu");
        }

        writer.endElement("span");
    }

    protected void encodeMenu(final FacesContext context, final MenuButton button, final String menuId)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("id", menuId, null);
        writer.writeAttribute("class", Menu.DYNAMIC_CONTAINER_CLASS, "styleClass");
        writer.writeAttribute("role", "menu", null);

        writer.startElement("ul", null);
        writer.writeAttribute("class", AbstractMenu.LIST_CLASS, "styleClass");

        for (final UIComponent child : button.getChildren()) {
            if (child.isRendered()) {
                if (child instanceof MenuItem) {
                    final MenuItem item = (MenuItem) child;

                    writer.startElement("li", item);
                    writer.writeAttribute("class", AbstractMenu.MENUITEM_CLASS, null);
                    writer.writeAttribute("role", "menuitem", null);
                    encodeMenuItem(context, item);
                    writer.endElement("li");
                } else if (child instanceof Separator) {
                    encodeSeparator(context, (Separator) child);
                }
            }
        }

        writer.endElement("ul");
        writer.endElement("div");

    }

    @Override
    protected void encodeScript(final FacesContext context, final AbstractMenu abstractMenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final MenuButton button = (MenuButton) abstractMenu;
        final String clientId = button.getClientId(context);

        final UIComponent form = ComponentUtils.findParentForm(context, button);
        if (form == null) {
            throw new FacesException("MenuButton : \"" + clientId + "\" must be inside a form element");
        }

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("MenuButton", button.resolveWidgetVar(), clientId, true);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }
}