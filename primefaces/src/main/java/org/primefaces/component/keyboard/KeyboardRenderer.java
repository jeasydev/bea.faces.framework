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
package org.primefaces.component.keyboard;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class KeyboardRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Keyboard keyboard = (Keyboard) component;

        if (keyboard.isDisabled() || keyboard.isReadonly()) {
            return;
        }

        decodeBehaviors(context, keyboard);

        final String clientId = keyboard.getClientId(context);
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId);

        if (submittedValue != null) {
            keyboard.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Keyboard keyboard = (Keyboard) component;

        encodeMarkup(context, keyboard);
        encodeScript(context, keyboard);
    }

    protected void encodeMarkup(final FacesContext context, final Keyboard keyboard) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = keyboard.getClientId(context);
        final String type = keyboard.isPassword() ? "password" : "text";
        final String defaultClass = Keyboard.STYLE_CLASS;
        String styleClass = keyboard.getStyleClass();
        styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;
        final String valueToRender = ComponentUtils.getValueToRender(context, keyboard);

        writer.startElement("input", keyboard);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", type, null);

        if (valueToRender != null) {
            writer.writeAttribute("value", valueToRender, "value");
        }

        renderPassThruAttributes(context, keyboard, HTML.INPUT_TEXT_ATTRS);

        writer.writeAttribute("class", styleClass, "styleClass");

        if (keyboard.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");
        if (keyboard.isReadonly()) writer.writeAttribute("readonly", "readonly", "readonly");
        if (keyboard.getStyle() != null) writer.writeAttribute("style", keyboard.getStyle(), "style");

        writer.endElement("input");
    }

    protected void encodeScript(final FacesContext context, final Keyboard keyboard) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = keyboard.getClientId(context);

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Keyboard", keyboard.resolveWidgetVar(), clientId, false).attr("showOn", keyboard.getShowMode())
            .attr("showAnim", keyboard.getEffect()).attr("buttonImageOnly", keyboard.isButtonImageOnly(), false)
            .attr("duration", keyboard.getEffectDuration(), null);

        if (keyboard.getButtonImage() != null) {
            wb.attr("buttonImage", getResourceURL(context, keyboard.getButtonImage()));
        }

        if (!keyboard.isKeypadOnly()) {
            wb.attr("keypadOnly", false).attr("layoutName", keyboard.getLayout()).attr("layoutTemplate",
                                                                                       keyboard.getLayoutTemplate(),
                                                                                       null);
        }

        wb.attr("keypadClass", keyboard.getStyleClass(), null).attr("prompt", keyboard.getPromptLabel(), null)
            .attr("backText", keyboard.getBackspaceLabel(), null).attr("clearText", keyboard.getClearLabel(), null)
            .attr("closeText", keyboard.getCloseLabel(), null);

        encodeClientBehaviors(context, keyboard, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }
}