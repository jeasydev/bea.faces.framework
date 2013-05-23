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
package org.primefaces.component.fieldset;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class FieldsetRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Fieldset fieldset = (Fieldset) component;
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = fieldset.getClientId();
        final String toggleStateParam = clientId + "_collapsed";

        if (params.containsKey(toggleStateParam)) {
            fieldset.setCollapsed(Boolean.valueOf(params.get(toggleStateParam)));
        }

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    protected void encodeContent(final FacesContext context, final Fieldset fieldset) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", Fieldset.CONTENT_CLASS, null);
        if (fieldset.isCollapsed()) {
            writer.writeAttribute("style", "display:none", null);
        }

        renderChildren(context, fieldset);

        writer.endElement("div");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Fieldset fieldset = (Fieldset) component;

        encodeMarkup(context, fieldset);
        encodeScript(context, fieldset);
    }

    protected void encodeLegend(final FacesContext context, final Fieldset fieldset) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String legendText = fieldset.getLegend();
        final UIComponent legend = fieldset.getFacet("legend");

        if (legendText != null || legend != null) {
            writer.startElement("legend", null);
            writer.writeAttribute("class", Fieldset.LEGEND_CLASS, null);

            if (fieldset.isToggleable()) {
                final String togglerClass = fieldset.isCollapsed()
                                                                  ? Fieldset.TOGGLER_PLUS_CLASS
                                                                  : Fieldset.TOGGLER_MINUS_CLASS;

                writer.startElement("span", null);
                writer.writeAttribute("class", togglerClass, null);
                writer.endElement("span");
            }

            if (legend != null)
                legend.encodeAll(context);
            else writer.write(fieldset.getLegend());

            writer.endElement("legend");
        }
    }

    protected void encodeMarkup(final FacesContext context, final Fieldset fieldset) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = fieldset.getClientId(context);
        final boolean toggleable = fieldset.isToggleable();

        String styleClass = toggleable ? Fieldset.TOGGLEABLE_FIELDSET_CLASS : Fieldset.FIELDSET_CLASS;
        if (fieldset.isCollapsed()) {
            styleClass = styleClass + " ui-hidden-container";
        }
        if (fieldset.getStyleClass() != null) {
            styleClass = styleClass + " " + fieldset.getStyleClass();
        }

        writer.startElement("fieldset", fieldset);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (fieldset.getStyle() != null) {
            writer.writeAttribute("style", fieldset.getStyle(), "style");
        }

        encodeLegend(context, fieldset);

        encodeContent(context, fieldset);

        if (toggleable) {
            encodeStateHolder(context, fieldset);
        }

        writer.endElement("fieldset");
    }

    protected void encodeScript(final FacesContext context, final Fieldset fieldset) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = fieldset.getClientId(context);
        final boolean toggleable = fieldset.isToggleable();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Fieldset", fieldset.resolveWidgetVar(), clientId, false);

        if (toggleable) {
            wb.attr("toggleable", true).attr("collapsed", fieldset.isCollapsed()).attr("toggleSpeed",
                                                                                       fieldset.getToggleSpeed());
        }

        encodeClientBehaviors(context, fieldset, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeStateHolder(final FacesContext context, final Fieldset fieldset) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String name = fieldset.getClientId(context) + "_collapsed";

        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", name, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("value", String.valueOf(fieldset.isCollapsed()), null);
        writer.endElement("input");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}