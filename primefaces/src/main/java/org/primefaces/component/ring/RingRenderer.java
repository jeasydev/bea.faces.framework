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
package org.primefaces.component.ring;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class RingRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Ring ring = (Ring) component;

        encodeMarkup(context, ring);
        encodeScript(context, ring);
    }

    public void encodeMarkup(final FacesContext context, final Ring ring) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        ring.getValue();
        ring.getVar();
        final String style = ring.getStyle();
        String styleClass = ring.getStyleClass();
        styleClass = styleClass == null ? Ring.STYLE_CLASS : Ring.STYLE_CLASS + " " + ring.getStyleClass();

        writer.startElement("ul", ring);
        writer.writeAttribute("id", ring.getClientId(context), null);
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        for (int rowIndex = 0; rowIndex < ring.getRowCount(); rowIndex++) {
            ring.setRowIndex(rowIndex);

            writer.startElement("li", ring);
            writer.writeAttribute("class", "ui-state-default ui-corner-all", null);

            for (final UIComponent child : ring.getChildren()) {
                child.encodeAll(context);
            }

            writer.endElement("li");
        }

        ring.setRowIndex(-1);

        writer.endElement("ul");
    }

    public void encodeScript(final FacesContext context, final Ring ring) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = ring.getClientId(context);
        ring.getEasing();

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Ring", ring.resolveWidgetVar(), clientId, "ring", true).attr("startingChild", ring.getFirst())
            .attr("easing", ring.getEasing(), null);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
