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
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.renderkit.OutcomeTargetRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public abstract class BaseMenuRenderer extends OutcomeTargetRenderer {

    @Override
    public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final AbstractMenu menu = (AbstractMenu) component;

        if (menu.isDynamic()) {
            menu.buildMenuFromModel();
        }

        encodeMarkup(context, menu);
        encodeScript(context, menu);
    }

    protected abstract void encodeMarkup(FacesContext context, AbstractMenu abstractMenu) throws IOException;

    protected void encodeMenuItem(final FacesContext context, final MenuItem menuItem) throws IOException {
        final String clientId = menuItem.getClientId(context);
        final ResponseWriter writer = context.getResponseWriter();
        final String icon = menuItem.getIcon();
        final String title = menuItem.getTitle();

        if (menuItem.shouldRenderChildren()) {
            renderChildren(context, menuItem);
        } else {
            final boolean disabled = menuItem.isDisabled();
            String onclick = menuItem.getOnclick();

            writer.startElement("a", null);
            writer.writeAttribute("id", menuItem.getClientId(context), null);
            if (title != null) {
                writer.writeAttribute("title", title, null);
            }

            String styleClass = menuItem.getStyleClass();
            styleClass = styleClass == null ? AbstractMenu.MENUITEM_LINK_CLASS : AbstractMenu.MENUITEM_LINK_CLASS + " "
                + styleClass;
            styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

            writer.writeAttribute("class", styleClass, null);

            if (menuItem.getStyle() != null) {
                writer.writeAttribute("style", menuItem.getStyle(), null);
            }

            // GET
            if (menuItem.getUrl() != null || menuItem.getOutcome() != null) {
                final String targetURL = getTargetURL(context, menuItem);
                final String href = disabled ? "javascript:void(0)" : targetURL;
                writer.writeAttribute("href", href, null);

                if (menuItem.getTarget() != null) {
                    writer.writeAttribute("target", menuItem.getTarget(), null);
                }
            }
            // POST
            else {
                writer.writeAttribute("href", "javascript:void(0)", null);

                final UIComponent form = ComponentUtils.findParentForm(context, menuItem);
                if (form == null) {
                    throw new FacesException("MenuItem must be inside a form element");
                }

                final String command = menuItem.isAjax()
                                                        ? buildAjaxRequest(context, menuItem, form)
                                                        : buildNonAjaxRequest(context, menuItem, form, clientId, true);

                onclick = onclick == null ? command : onclick + ";" + command;
            }

            if (onclick != null && !disabled) {
                writer.writeAttribute("onclick", onclick, null);
            }

            if (icon != null) {
                writer.startElement("span", null);
                writer.writeAttribute("class", AbstractMenu.MENUITEM_ICON_CLASS + " " + icon, null);
                writer.endElement("span");
            }

            if (menuItem.getValue() != null) {
                writer.startElement("span", null);
                writer.writeAttribute("class", AbstractMenu.MENUITEM_TEXT_CLASS, null);
                writer.writeText(menuItem.getValue(), "value");
                writer.endElement("span");
            }

            writer.endElement("a");
        }
    }

    protected void encodeOverlayConfig(final FacesContext context, final OverlayMenu menu, final WidgetBuilder wb)
        throws IOException {
        context.getResponseWriter();

        wb.attr("overlay", true).attr("my", menu.getMy()).attr("at", menu.getAt());

        final UIComponent trigger = ((UIComponent) menu).findComponent(menu.getTrigger());
        final String triggerClientId = trigger == null ? menu.getTrigger() : trigger.getClientId(context);

        wb.attr("trigger", triggerClientId).attr("triggerEvent", menu.getTriggerEvent());
    }

    protected abstract void encodeScript(FacesContext context, AbstractMenu abstractMenu) throws IOException;

    protected void encodeSeparator(final FacesContext context, final Separator separator) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String style = separator.getStyle();
        String styleClass = separator.getStyleClass();
        styleClass = styleClass == null ? AbstractMenu.SEPARATOR_CLASS : AbstractMenu.SEPARATOR_CLASS + " "
            + styleClass;

        // title
        writer.startElement("li", null);
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        writer.endElement("li");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
