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
package org.primefaces.component.scrollpanel;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class ScrollPanelRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ScrollPanel panel = (ScrollPanel) component;

        encodeMarkup(context, panel);
        encodeScript(context, panel);
    }

    protected void encodeMarkup(final FacesContext context, final ScrollPanel panel) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = panel.getClientId(context);
        final boolean nativeMode = panel.getMode().equals("native");

        final String defaultStyleClass = nativeMode
                                                   ? ScrollPanel.SCROLL_PANEL_NATIVE_CLASS
                                                   : ScrollPanel.SCROLL_PANEL_CLASS;
        final String style = panel.getStyle();
        String styleClass = panel.getStyleClass();
        styleClass = styleClass == null ? defaultStyleClass : defaultStyleClass + " " + styleClass;

        writer.startElement("div", panel);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        renderChildren(context, panel);

        // scrollpanel
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final ScrollPanel panel) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = panel.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("ScrollPanel", panel.resolveWidgetVar(), clientId, false).attr("mode", panel.getMode());

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
