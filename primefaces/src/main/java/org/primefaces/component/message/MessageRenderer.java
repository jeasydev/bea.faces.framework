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
package org.primefaces.component.message;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.UINotificationRenderer;

public class MessageRenderer extends UINotificationRenderer {

    @Override
    public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
        final ResponseWriter writer = facesContext.getResponseWriter();
        final Message uiMessage = (Message) component;
        final UIComponent target = uiMessage.findComponent(uiMessage.getFor());
        final String display = uiMessage.getDisplay();
        final boolean iconOnly = display.equals("icon");
        final boolean escape = uiMessage.isEscape();

        if (target == null) {
            throw new FacesException("Cannot find component \"" + uiMessage.getFor() + "\" in view.");
        }

        final Iterator<FacesMessage> msgs = facesContext.getMessages(target.getClientId(facesContext));

        writer.startElement("div", uiMessage);
        writer.writeAttribute("id", uiMessage.getClientId(facesContext), null);
        writer.writeAttribute("aria-live", "polite", null);

        if (msgs.hasNext()) {
            final FacesMessage msg = msgs.next();
            final String severityName = getSeverityName(msg);

            if (!shouldRender(uiMessage, msg, severityName)) {
                writer.endElement("div");

                return;
            } else {
                final Severity severity = msg.getSeverity();
                String severityKey = null;

                if (severity.equals(FacesMessage.SEVERITY_ERROR))
                    severityKey = "error";
                else if (severity.equals(FacesMessage.SEVERITY_INFO))
                    severityKey = "info";
                else if (severity.equals(FacesMessage.SEVERITY_WARN))
                    severityKey = "warn";
                else if (severity.equals(FacesMessage.SEVERITY_FATAL)) severityKey = "fatal";

                String styleClass = "ui-message-" + severityKey + " ui-widget ui-corner-all";
                if (iconOnly) {
                    styleClass = styleClass + " ui-message-icon-only ui-helper-clearfix";
                }

                writer.writeAttribute("class", styleClass, null);

                if (!display.equals("text")) {
                    encodeIcon(writer, severityKey, msg.getDetail(), iconOnly);
                }

                if (!iconOnly) {
                    if (uiMessage.isShowSummary())
                        encodeText(writer, msg.getSummary(), severityKey + "-summary", escape);
                    if (uiMessage.isShowDetail()) encodeText(writer, msg.getDetail(), severityKey + "-detail", escape);
                }

                msg.rendered();
            }
        }

        writer.endElement("div");
    }

    protected void encodeIcon(final ResponseWriter writer,
                              final String severity,
                              final String title,
                              final boolean iconOnly) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-message-" + severity + "-icon", null);
        if (iconOnly) {
            writer.writeAttribute("title", title, null);
        }
        writer.endElement("span");
    }

    protected void encodeText(final ResponseWriter writer,
                              final String text,
                              final String severity,
                              final boolean escape) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", "ui-message-" + severity, null);

        if (escape)
            writer.writeText(text, null);
        else writer.write(text);

        writer.endElement("span");
    }
}