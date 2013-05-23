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
package org.primefaces.component.imagecompare;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class ImageCompareRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ImageCompare compare = (ImageCompare) component;

        encodeMarkup(context, compare);
        encodeScript(context, compare);
    }

    protected void encodeMarkup(final FacesContext context, final ImageCompare compare) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", compare);
        writer.writeAttribute("id", compare.getClientId(context), "id");
        renderImage(context, compare, "before", compare.getLeftImage());
        renderImage(context, compare, "fter", compare.getRightImage());
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final ImageCompare compare) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = compare.getClientId(context);

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("ImageCompare", compare.resolveWidgetVar(), clientId, "imagecompare", false)
            .attr("handle", getResourceRequestPath(context, "imagecompare/handle.gif"))
            .attr("lt", getResourceRequestPath(context, "imagecompare/lt-small.png"))
            .attr("rt", getResourceRequestPath(context, "imagecompare/rt-small.png"));

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    private void renderImage(final FacesContext context, final ImageCompare compare, final String type, final String src)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", compare);

        writer.startElement("img", null);
        writer.writeAttribute("alt", type, null);
        writer.writeAttribute("src", getResourceURL(context, src), null);
        writer.writeAttribute("width", compare.getWidth(), null);
        writer.writeAttribute("height", compare.getHeight(), null);
        writer.endElement("img");

        writer.endElement("div");
    }
}