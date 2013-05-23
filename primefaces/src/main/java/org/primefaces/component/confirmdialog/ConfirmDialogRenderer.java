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
package org.primefaces.component.confirmdialog;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class ConfirmDialogRenderer extends CoreRenderer {

    protected void encodeButtonPane(final FacesContext context, final ConfirmDialog dialog) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", ConfirmDialog.BUTTONPANE_CLASS, null);

        renderChildren(context, dialog);

        writer.endElement("div");
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do Nothing
    }

    protected void encodeContent(final FacesContext context, final ConfirmDialog dialog) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String messageText = dialog.getMessage();
        final UIComponent messageFacet = dialog.getFacet("message");
        final String severityIcon = new StringBuilder().append("ui-icon ui-icon-").append(dialog.getSeverity())
            .append(" ").append(ConfirmDialog.SEVERITY_ICON_CLASS).toString();

        writer.startElement("div", null);
        writer.writeAttribute("class", Dialog.CONTENT_CLASS, null);

        writer.startElement("p", null);

        // severity
        writer.startElement("span", null);
        writer.writeAttribute("class", severityIcon, null);
        writer.endElement("span");

        if (messageFacet != null)
            messageFacet.encodeAll(context);
        else if (messageText != null) writer.writeText(messageText, null);

        writer.endElement("p");

        writer.endElement("div");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ConfirmDialog dialog = (ConfirmDialog) component;

        encodeScript(context, dialog);
        encodeMarkup(context, dialog);
    }

    protected void encodeHeader(final FacesContext context, final ConfirmDialog dialog) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String header = dialog.getHeader();
        final UIComponent headerFacet = dialog.getFacet("header");

        writer.startElement("div", null);
        writer.writeAttribute("class", Dialog.TITLE_BAR_CLASS, null);

        // title
        writer.startElement("span", null);
        writer.writeAttribute("class", Dialog.TITLE_CLASS, null);

        if (headerFacet != null)
            headerFacet.encodeAll(context);
        else if (header != null) writer.write(header);

        writer.endElement("span");

        if (dialog.isClosable()) {
            writer.startElement("a", null);
            writer.writeAttribute("href", "#", null);
            writer.writeAttribute("class", Dialog.TITLE_BAR_CLOSE_CLASS, null);

            writer.startElement("span", null);
            writer.writeAttribute("class", Dialog.CLOSE_ICON_CLASS, null);
            writer.endElement("span");

            writer.endElement("a");
        }

        writer.endElement("div");
    }

    protected void encodeMarkup(final FacesContext context, final ConfirmDialog dialog) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = dialog.getClientId(context);
        final String style = dialog.getStyle();
        String styleClass = dialog.getStyleClass();
        styleClass = styleClass == null ? ConfirmDialog.CONTAINER_CLASS : ConfirmDialog.CONTAINER_CLASS + " "
            + styleClass;

        if (ComponentUtils.isRTL(context, dialog)) {
            styleClass += " ui-dialog-rtl";
        }

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);

        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        encodeHeader(context, dialog);
        encodeContent(context, dialog);
        encodeButtonPane(context, dialog);

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final ConfirmDialog dialog) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = dialog.getClientId();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("ConfirmDialog", dialog.resolveWidgetVar(), clientId, true)
            .attr("visible", dialog.isVisible(), false).attr("width", dialog.getWidth(), null).attr("height",
                                                                                                    dialog.getHeight(),
                                                                                                    null)
            .attr("appendToBody", dialog.isAppendToBody(), false).attr("showEffect", dialog.getShowEffect(), null)
            .attr("hideEffect", dialog.getHideEffect(), null).attr("closeOnEscape", dialog.isCloseOnEscape(), false);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}