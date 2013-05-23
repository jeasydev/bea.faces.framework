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
package org.primefaces.component.button;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.OutcomeTargetRenderer;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class ButtonRenderer extends OutcomeTargetRenderer {

    protected String buildOnclick(final FacesContext context, final Button button) {
        final String userOnclick = button.getOnclick();
        final StringBuilder onclick = new StringBuilder();
        final String targetURL = getTargetURL(context, button);

        if (userOnclick != null) {
            onclick.append(userOnclick).append(";");
        }

        final String onclickBehaviors = getOnclickBehaviors(context, button);
        if (onclickBehaviors != null) {
            onclick.append(onclickBehaviors).append(";");
        }

        if (targetURL != null) {
            onclick.append("window.location.href='").append(targetURL).append("'");
        }

        return onclick.toString();
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Button button = (Button) component;

        encodeMarkup(context, button);
        encodeScript(context, button);
    }

    public void encodeMarkup(final FacesContext context, final Button button) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = button.getClientId(context);
        final String value = (String) button.getValue();
        final String icon = button.resolveIcon();

        writer.startElement("button", button);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("name", clientId, "name");
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", button.resolveStyleClass(), "styleClass");

        renderPassThruAttributes(context, button, HTML.BUTTON_ATTRS, HTML.CLICK_EVENT);

        if (button.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");

        writer.writeAttribute("onclick", buildOnclick(context, button), null);

        // icon
        if (icon != null) {
            final String defaultIconClass = button.getIconPos().equals("left")
                                                                              ? HTML.BUTTON_LEFT_ICON_CLASS
                                                                              : HTML.BUTTON_RIGHT_ICON_CLASS;
            final String iconClass = defaultIconClass + " " + icon;

            writer.startElement("span", null);
            writer.writeAttribute("class", iconClass, null);
            writer.endElement("span");
        }

        // text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);

        if (value == null) {
            writer.write("ui-button");
        } else {
            if (button.isEscape())
                writer.writeText(value, "value");
            else writer.write(value);
        }

        writer.endElement("span");

        writer.endElement("button");
    }

    public void encodeScript(final FacesContext context, final Button button) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = button.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Button", button.resolveWidgetVar(), clientId, false);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

}