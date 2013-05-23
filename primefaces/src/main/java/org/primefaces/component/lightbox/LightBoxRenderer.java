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
package org.primefaces.component.lightbox;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class LightBoxRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final LightBox lb = (LightBox) component;

        encodeMarkup(context, lb);
        encodeScript(context, lb);
    }

    public void encodeMarkup(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final LightBox lb = (LightBox) component;
        final String clientId = lb.getClientId(context);
        final UIComponent inline = lb.getFacet("inline");

        writer.startElement("div", lb);
        writer.writeAttribute("id", clientId, "id");
        if (lb.getStyle() != null) writer.writeAttribute("style", lb.getStyle(), null);
        if (lb.getStyleClass() != null) writer.writeAttribute("class", lb.getStyleClass(), null);

        renderChildren(context, lb);

        if (inline != null) {
            writer.startElement("div", null);
            writer.writeAttribute("class", "ui-lightbox-inline ui-helper-hidden", null);
            inline.encodeAll(context);
            writer.endElement("div");
        }

        writer.endElement("div");
    }

    public void encodeScript(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final LightBox lb = (LightBox) component;
        final String clientId = lb.getClientId(context);
        String mode = "image";
        if (lb.getFacet("inline") != null)
            mode = "inline";
        else if (lb.isIframe()) mode = "iframe";

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("LightBox", lb.resolveWidgetVar(), clientId, true).attr("mode", mode).attr("width", lb.getWidth(),
                                                                                             null).attr("height",
                                                                                                        lb.getHeight(),
                                                                                                        null)
            .attr("visible", lb.isVisible(), false).attr("iframeTitle", lb.getIframeTitle(), null)
            .callback("onShow", "function()", lb.getOnShow()).callback("onHide", "function()", lb.getOnHide());

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}