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
package org.primefaces.component.galleria;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class GalleriaRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Galleria galleria = (Galleria) component;

        encodeMarkup(context, galleria);
        encodeScript(context, galleria);
    }

    public void encodeMarkup(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Galleria galleria = (Galleria) component;
        final String var = galleria.getVar();
        final String style = galleria.getStyle();
        String styleClass = galleria.getStyleClass();
        styleClass = (styleClass == null) ? Galleria.CONTAINER_CLASS : Galleria.CONTAINER_CLASS + " " + styleClass;
        final UIComponent content = galleria.getFacet("content");

        writer.startElement("div", component);
        writer.writeAttribute("id", galleria.getClientId(context), "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        writer.startElement("ul", component);
        writer.writeAttribute("class", Galleria.PANEL_WRAPPER_CLASS, null);

        if (var == null) {
            for (final UIComponent child : galleria.getChildren()) {
                if (child.isRendered()) {
                    writer.startElement("li", null);
                    writer.writeAttribute("class", Galleria.PANEL_CLASS, null);
                    child.encodeAll(context);

                    if (content != null) {
                        writer.startElement("div", null);
                        writer.writeAttribute("class", Galleria.PANEL_CONTENT_CLASS, null);
                        content.encodeAll(context);
                        writer.endElement("div");
                    }

                    writer.endElement("li");
                }
            }
        } else {
            final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            final Collection<?> value = (Collection<?>) galleria.getValue();
            if (value != null) {
                for (final Object name : value) {
                    requestMap.put(var, name);

                    writer.startElement("li", null);
                    writer.writeAttribute("class", Galleria.PANEL_CLASS, null);
                    renderChildren(context, galleria);

                    if (content != null) {
                        writer.startElement("div", null);
                        writer.writeAttribute("class", Galleria.PANEL_CONTENT_CLASS, null);
                        content.encodeAll(context);
                        writer.endElement("div");
                    }

                    writer.endElement("li");
                }
            }

            requestMap.remove(var);
        }

        writer.endElement("ul");

        writer.endElement("div");
    }

    public void encodeScript(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Galleria galleria = (Galleria) component;
        final String clientId = galleria.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Galleria", galleria.resolveWidgetVar(), clientId, "galleria", false);

        wb.attr("showFilmstrip", galleria.isShowFilmstrip(), true).attr("frameWidth", galleria.getFrameWidth(), 60)
            .attr("frameHeight", galleria.getFrameHeight(), 40).attr("autoPlay", galleria.isAutoPlay(), true)
            .attr("transitionInterval", galleria.getTransitionInterval(), 4000).attr("effect", galleria.getEffect(),
                                                                                     "fade")
            .attr("effectSpeed", galleria.getEffectSpeed(), 500).attr("showCaption", galleria.isShowCaption(), false)
            .attr("panelWidth", galleria.getPanelWidth(), Integer.MIN_VALUE).attr("panelHeight",
                                                                                  galleria.getPanelHeight(),
                                                                                  Integer.MIN_VALUE)
            .attr("custom", (galleria.getFacet("content") != null));

        startScript(writer, clientId);
        writer.write("$(window).load(function(){");
        writer.write(wb.build());
        writer.write("});");
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}