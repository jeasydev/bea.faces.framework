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
package org.primefaces.component.selectbooleanbutton;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class SelectBooleanButtonRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final SelectBooleanButton button = (SelectBooleanButton) component;

        if (button.isDisabled()) {
            return;
        }

        decodeBehaviors(context, button);

        final String clientId = button.getClientId(context);
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId + "_input");

        if (submittedValue != null && submittedValue.equalsIgnoreCase("on")) {
            button.setSubmittedValue("true");
        } else {
            button.setSubmittedValue("false");
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectBooleanButton button = (SelectBooleanButton) component;

        encodeMarkup(context, button);
        encodeScript(context, button);
    }

    protected void encodeMarkup(final FacesContext context, final SelectBooleanButton button) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = button.getClientId(context);
        final boolean checked = Boolean.valueOf(ComponentUtils.getValueToRender(context, button));
        final boolean disabled = button.isDisabled();
        final String inputId = clientId + "_input";
        final String label = checked ? button.getOnLabel() : button.getOffLabel();
        final String icon = checked ? button.getOnIcon() : button.getOffIcon();

        // button
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", button.resolveStyleClass(checked, disabled), null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (button.getTitle() != null) writer.writeAttribute("title", button.getTitle(), null);
        if (button.getStyle() != null) writer.writeAttribute("style", button.getStyle(), "style");

        // input
        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, "id");
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", "checkbox", null);
        writer.writeAttribute("class", "ui-helper-hidden", null);

        if (checked) writer.writeAttribute("checked", "checked", null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (button.getOnchange() != null) writer.writeAttribute("onchange", button.getOnchange(), null);

        writer.endElement("input");

        // icon
        if (icon != null) {
            writer.startElement("span", null);
            writer.writeAttribute("class", HTML.BUTTON_LEFT_ICON_CLASS + " " + icon, null);
            writer.endElement("span");
        }

        // label
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.writeText(label, "value");
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final SelectBooleanButton button) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = button.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectBooleanButton", button.resolveWidgetVar(), clientId, false).attr("onLabel",
                                                                                          button.getOnLabel())
            .attr("offLabel", button.getOffLabel()).attr("onIcon", button.getOnIcon(), null).attr("offIcon",
                                                                                                  button.getOffIcon(),
                                                                                                  null);

        encodeClientBehaviors(context, button, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }
}
