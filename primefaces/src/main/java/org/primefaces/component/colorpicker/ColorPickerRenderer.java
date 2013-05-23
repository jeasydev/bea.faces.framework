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
package org.primefaces.component.colorpicker;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class ColorPickerRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final ColorPicker colorPicker = (ColorPicker) component;
        final String paramName = colorPicker.getClientId(context) + "_input";
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        if (params.containsKey(paramName)) {
            final String submittedValue = params.get(paramName);

            colorPicker.setSubmittedValue(submittedValue);
        }
    }

    protected void encodeButton(final FacesContext context, final String clientId, final String value)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("button", null);
        writer.writeAttribute("id", clientId + "_button", null);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_ONLY_BUTTON_CLASS, null);

        // text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);

        writer
            .write("<span id=\""
                + clientId
                + "_livePreview\" style=\"overflow:hidden;width:1em;height:1em;display:block;border:solid 1px #000;text-indent:1em;white-space:nowrap;");
        if (value != null) {
            writer.write("background-color:#" + value);
        }
        writer.write("\">Live Preview</span>");

        writer.endElement("span");

        writer.endElement("button");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ColorPicker colorPicker = (ColorPicker) component;

        encodeMarkup(context, colorPicker);
        encodeScript(context, colorPicker);
    }

    protected void encodeInline(final FacesContext context, final String clientId) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_inline", "id");
        writer.endElement("div");
    }

    protected void encodeMarkup(final FacesContext context, final ColorPicker colorPicker) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = colorPicker.getClientId(context);
        final String inputId = clientId + "_input";
        final String value = (String) colorPicker.getValue();
        final boolean isPopup = colorPicker.getMode().equals("popup");
        String styleClass = colorPicker.getStyleClass();
        styleClass = styleClass == null ? ColorPicker.STYLE_CLASS : ColorPicker.STYLE_CLASS + " " + styleClass;

        writer.startElement("span", null);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (colorPicker.getStyle() != null) writer.writeAttribute("style", colorPicker.getStyle(), "style");

        if (isPopup) {
            encodeButton(context, clientId, value);
        } else {
            encodeInline(context, clientId);
        }

        // Input
        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", "hidden", null);
        if (value != null) {
            writer.writeAttribute("value", value, null);
        }
        writer.endElement("input");

        writer.endElement("span");
    }

    protected void encodeScript(final FacesContext context, final ColorPicker colorPicker) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = colorPicker.getClientId(context);
        final String value = (String) colorPicker.getValue();
        final WidgetBuilder wb = getWidgetBuilder(context);

        wb.widget("ColorPicker", colorPicker.resolveWidgetVar(), clientId, "colorpicker", true).attr("mode",
                                                                                                     colorPicker
                                                                                                         .getMode())
            .attr("color", value, null);

        startScript(writer, clientId);
        writer.write(wb.build());

        endScript(writer);
    }
}