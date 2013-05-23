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
package org.primefaces.component.layout;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class LayoutRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Layout layout = (Layout) component;
        final String clientId = layout.getClientId(context);

        encodeScript(context, layout);

        if (layout.isElementLayout()) {
            writer.startElement("div", layout);
            writer.writeAttribute("id", clientId, "id");

            if (layout.getStyle() != null) writer.writeAttribute("style", layout.getStyle(), "style");
            if (layout.getStyleClass() != null) writer.writeAttribute("class", layout.getStyleClass(), "styleClass");
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Layout layout = (Layout) component;

        if (layout.isElementLayout()) {
            writer.endElement("div");
        }
    }

    protected void encodeScript(final FacesContext context, final Layout layout) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = layout.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Layout", layout.resolveWidgetVar(), clientId, "layout", true).attr("full", layout.isFullPage(),
                                                                                      false).attr("useStateCookie",
                                                                                                  layout.isStateful(),
                                                                                                  false);

        if (layout.isNested()) {
            wb.attr("parent", layout.getParent().getClientId(context));
        }

        wb.callback("onToggle", "function(e)", layout.getOnToggle()).callback("onClose", "function(e)",
                                                                              layout.getOnClose())
            .callback("onResize", "function(e)", layout.getOnResize());

        encodeUnits(context, layout, wb);
        encodeClientBehaviors(context, layout, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeUnits(final FacesContext context, final Layout layout, final WidgetBuilder wb)
        throws IOException {
        for (final UIComponent child : layout.getChildren()) {
            if (child.isRendered() && child instanceof LayoutUnit) {
                final LayoutUnit unit = (LayoutUnit) child;

                wb.append(",").append(unit.getPosition()).append(":{").append("paneSelector:'")
                    .append(ComponentUtils.escapeJQueryId(unit.getClientId(context))).append("'").attr("size",
                                                                                                       unit.getSize())
                    .attr("resizable", unit.isResizable()).attr("closable", unit.isCollapsible())
                    .attr("minSize", unit.getMinSize(), 50).attr("maxSize", unit.getMaxSize(), 0);

                if (unit.isCollapsible()) {
                    wb.attr("spacing_open", unit.getGutter()).attr("spacing_closed", unit.getCollapseSize());
                }

                wb.attr("initHidden", unit.isVisible(), true).attr("initClosed", unit.isCollapsed(), false)
                    .attr("fxName", unit.getEffect(), null).attr("fxSpeed", unit.getEffectSpeed(), null)
                    .attr("resizerTip", layout.getResizeTitle(), null).attr("togglerTip_closed",
                                                                            layout.getExpandTitle(), null);

                wb.append("}");
            }
        }
    }
}
