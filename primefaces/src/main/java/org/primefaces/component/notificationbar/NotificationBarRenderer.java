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
package org.primefaces.component.notificationbar;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class NotificationBarRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final NotificationBar bar = (NotificationBar) component;

        encodeMarkup(context, bar);
        encodeScript(context, bar);
    }

    protected void encodeMarkup(final FacesContext context, final NotificationBar bar) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String styleClass = bar.getStyleClass();
        styleClass = styleClass == null ? NotificationBar.STYLE_CLASS : NotificationBar.STYLE_CLASS + " " + styleClass;

        writer.startElement("div", bar);
        writer.writeAttribute("id", bar.getClientId(context), null);
        writer.writeAttribute("class", styleClass, null);
        if (bar.getStyle() != null) {
            writer.writeAttribute("style", bar.getStyle(), null);
        }

        renderChildren(context, bar);

        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final NotificationBar bar) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = bar.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("NotificationBar", bar.resolveWidgetVar(), clientId, true).attr("position", bar.getPosition())
            .attr("effect", bar.getEffect()).attr("effectSpeed", bar.getEffectSpeed()).attr("autoDisplay",
                                                                                            bar.isAutoDisplay(), false);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}