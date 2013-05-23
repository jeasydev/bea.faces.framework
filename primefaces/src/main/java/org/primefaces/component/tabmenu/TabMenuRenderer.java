/**
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
package org.primefaces.component.tabmenu;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.menu.AbstractMenu;
import org.primefaces.component.menu.BaseMenuRenderer;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.util.WidgetBuilder;

public class TabMenuRenderer extends BaseMenuRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    protected void encodeItem(final FacesContext context, final TabMenu menu, final MenuItem item, final boolean active)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String containerClass = active ? TabMenu.ACTIVE_TAB_HEADER_CLASS : TabMenu.INACTIVE_TAB_HEADER_CLASS;
        if (item.getIcon() != null) {
            containerClass += " ui-tabmenuitem-hasicon";
        }

        // header container
        writer.startElement("li", null);
        writer.writeAttribute("class", containerClass, null);
        writer.writeAttribute("role", "tab", null);
        writer.writeAttribute("aria-expanded", String.valueOf(active), null);

        encodeMenuItem(context, item);

        writer.endElement("li");
    }

    @Override
    protected void encodeMarkup(final FacesContext context, final AbstractMenu component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final TabMenu menu = (TabMenu) component;
        final String clientId = menu.getClientId(context);
        String styleClass = menu.getStyleClass();
        styleClass = styleClass == null ? TabMenu.CONTAINER_CLASS : TabMenu.CONTAINER_CLASS + " " + styleClass;
        final int activeIndex = menu.getActiveIndex();

        writer.startElement("div", menu);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, "styleClass");
        if (menu.getStyle() != null) {
            writer.writeAttribute("style", menu.getStyle(), "style");
        }

        writer.startElement("ul", null);
        writer.writeAttribute("class", TabMenu.NAVIGATOR_CLASS, null);
        writer.writeAttribute("role", "tablist", null);

        int i = 0;
        for (final UIComponent kid : menu.getChildren()) {
            if (kid.isRendered() && kid instanceof MenuItem) {
                encodeItem(context, menu, (MenuItem) kid, (i == activeIndex));
                i++;
            }
        }

        writer.endElement("ul");

        writer.endElement("div");
    }

    @Override
    protected void encodeScript(final FacesContext context, final AbstractMenu abstractMenu) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final TabMenu menu = (TabMenu) abstractMenu;
        final String clientId = menu.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("TabMenu", menu.resolveWidgetVar(), clientId, false);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
