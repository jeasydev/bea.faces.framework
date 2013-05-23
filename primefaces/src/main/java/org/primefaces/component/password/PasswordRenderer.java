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
package org.primefaces.component.password;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class PasswordRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Password password = (Password) component;

        if (password.isDisabled() || password.isReadonly()) {
            return;
        }

        decodeBehaviors(context, password);

        final String submittedValue = context.getExternalContext().getRequestParameterMap()
            .get(password.getClientId(context));

        if (submittedValue != null) {
            password.setSubmittedValue(submittedValue);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Password password = (Password) component;

        encodeMarkup(context, password);
        encodeScript(context, password);
    }

    protected void encodeMarkup(final FacesContext context, final Password password) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = password.getClientId(context);
        final boolean disabled = password.isDisabled();

        String inputClass = Password.STYLE_CLASS;
        inputClass = password.isValid() ? inputClass : inputClass + " ui-state-error";
        inputClass = !disabled ? inputClass : inputClass + " ui-state-disabled";
        final String styleClass = password.getStyleClass() == null ? inputClass : inputClass + " "
            + password.getStyleClass();

        writer.startElement("input", password);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("type", "password", null);
        writer.writeAttribute("class", styleClass, null);
        if (password.getStyle() != null) {
            writer.writeAttribute("style", password.getStyle(), null);
        }

        final String valueToRender = ComponentUtils.getValueToRender(context, password);
        if (!isValueEmpty(valueToRender) && password.isRedisplay()) {
            writer.writeAttribute("value", valueToRender, null);
        }

        renderPassThruAttributes(context, password, HTML.INPUT_TEXT_ATTRS);

        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (password.isReadonly()) writer.writeAttribute("readonly", "readonly", null);

        writer.endElement("input");
    }

    protected void encodeScript(final FacesContext context, final Password password) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = password.getClientId(context);
        final boolean feedback = password.isFeedback();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Password", password.resolveWidgetVar(), clientId, true);

        if (feedback) {
            wb.attr("feedback", true).attr("inline", password.isInline()).attr("promptLabel",
                                                                               password.getPromptLabel(), null)
                .attr("weakLabel", password.getWeakLabel(), null).attr("goodLabel", password.getGoodLabel(), null)
                .attr("strongLabel", password.getStrongLabel(), null);
        }

        encodeClientBehaviors(context, password, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }
}