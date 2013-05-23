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
package org.primefaces.component.tooltip;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class TooltipRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Tooltip tooltip = (Tooltip) component;
        final String target = getTarget(context, tooltip);

        encodeMarkup(context, tooltip, target);
        encodeScript(context, tooltip, target);
    }

    protected void encodeMarkup(final FacesContext context, final Tooltip tooltip, final String target)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        if (target != null) {
            String styleClass = tooltip.getStyleClass();
            styleClass = styleClass == null ? Tooltip.CONTAINER_CLASS : Tooltip.CONTAINER_CLASS + " " + styleClass;

            writer.startElement("div", tooltip);
            writer.writeAttribute("id", tooltip.getClientId(context), null);
            writer.writeAttribute("class", styleClass, "styleClass");

            if (tooltip.getStyle() != null) writer.writeAttribute("style", tooltip.getStyle(), "style");

            if (tooltip.getChildCount() > 0) {
                renderChildren(context, tooltip);
            } else {
                final String valueToRender = ComponentUtils.getValueToRender(context, tooltip);
                if (valueToRender != null) writer.writeText(valueToRender, "value");
            }

            writer.endElement("div");
        }
    }

    protected void encodeScript(final FacesContext context, final Tooltip tooltip, final String target)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = tooltip.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Tooltip", tooltip.resolveWidgetVar(), clientId, true)
            .attr("showEvent", tooltip.getShowEvent(), null).attr("hideEvent", tooltip.getHideEvent(), null)
            .attr("showEffect", tooltip.getShowEffect(), null).attr("hideEffect", tooltip.getHideEffect(), null);

        if (target != null) {
            wb.attr("target", target);
        }

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    protected String getTarget(final FacesContext context, final Tooltip tooltip) {
        final String _for = tooltip.getFor();

        if (_for != null) {
            final UIComponent forComponent = tooltip.findComponent(_for);

            if (forComponent == null)
                throw new FacesException("Cannot find component \"" + _for + "\" in view.");
            else return forComponent.getClientId(context);

        } else {
            return null;
        }
    }
}