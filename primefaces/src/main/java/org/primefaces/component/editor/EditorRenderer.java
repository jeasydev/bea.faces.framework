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
package org.primefaces.component.editor;

import java.io.IOException;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class EditorRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Editor editor = (Editor) component;
        final String inputParam = editor.getClientId(context) + "_input";
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String value = params.get(inputParam);

        if (value != null && value.equals("<br/>")) {
            value = "";
        }

        editor.setSubmittedValue(value);
    }

    @Override
    public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
        final Editor editor = (Editor) component;

        encodeMarkup(facesContext, editor);
        encodeScript(facesContext, editor);
    }

    protected void encodeMarkup(final FacesContext context, final Editor editor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = editor.getClientId(context);
        final String valueToRender = ComponentUtils.getValueToRender(context, editor);
        final String inputId = clientId + "_input";

        String style = editor.getStyle();
        style = style == null ? "visibility:hidden" : "visibility:hidden;" + style;

        writer.startElement("div", editor);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("style", style, null);
        if (editor.getStyleClass() != null) {
            writer.writeAttribute("class", editor.getStyleClass(), null);
        }

        writer.startElement("textarea", null);
        writer.writeAttribute("id", inputId, null);
        writer.writeAttribute("name", inputId, null);

        if (valueToRender != null) {
            writer.write(valueToRender);
        }

        writer.endElement("textarea");

        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final Editor editor) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = editor.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Editor", editor.resolveWidgetVar(), clientId, "editor", true).attr("disabled", editor.isDisabled(),
                                                                                      false).attr("invalid",
                                                                                                  editor.isValid(),
                                                                                                  true)
            .attr("controls", editor.getControls(), null).attr("width", editor.getWidth(), Integer.MIN_VALUE)
            .attr("height", editor.getHeight(), Integer.MIN_VALUE).attr("maxlength", editor.getMaxlength(),
                                                                        Integer.MAX_VALUE).callback("change",
                                                                                                    "function(e)",
                                                                                                    editor
                                                                                                        .getOnchange());

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        final Editor editor = (Editor) component;
        final String value = (String) submittedValue;
        final Converter converter = editor.getConverter();

        // first ask the converter
        if (converter != null) {
            return converter.getAsObject(context, editor, value);
        }
        // Try to guess
        else {
            final ValueExpression ve = editor.getValueExpression("value");

            if (ve != null) {
                final Class<?> valueType = ve.getType(context.getELContext());
                final Converter converterForType = context.getApplication().createConverter(valueType);

                if (converterForType != null) {
                    return converterForType.getAsObject(context, editor, value);
                }
            }
        }

        return value;
    }
}