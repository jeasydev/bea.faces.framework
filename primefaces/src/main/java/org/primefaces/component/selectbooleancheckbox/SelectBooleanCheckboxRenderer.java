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
package org.primefaces.component.selectbooleancheckbox;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class SelectBooleanCheckboxRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;

        if (checkbox.isDisabled()) {
            return;
        }

        decodeBehaviors(context, checkbox);

        final String clientId = checkbox.getClientId(context);
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId + "_input");

        if (submittedValue != null && isChecked(submittedValue)) {
            checkbox.setSubmittedValue(true);
        } else {
            checkbox.setSubmittedValue(false);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SelectBooleanCheckbox checkbox = (SelectBooleanCheckbox) component;

        encodeMarkup(context, checkbox);
        encodeScript(context, checkbox);
    }

    protected void encodeInput(final FacesContext context,
                               final SelectBooleanCheckbox checkbox,
                               final String clientId,
                               final boolean checked,
                               final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String inputId = clientId + "_input";

        writer.startElement("div", checkbox);
        writer.writeAttribute("class", "ui-helper-hidden-accessible", null);

        writer.startElement("input", null);
        writer.writeAttribute("id", inputId, "id");
        writer.writeAttribute("name", inputId, null);
        writer.writeAttribute("type", "checkbox", null);

        if (checked) writer.writeAttribute("checked", "checked", null);
        if (disabled) writer.writeAttribute("disabled", "disabled", null);
        if (checkbox.getOnchange() != null) writer.writeAttribute("onchange", checkbox.getOnchange(), null);
        if (checkbox.getTabindex() != null) writer.writeAttribute("tabindex", checkbox.getTabindex(), null);

        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeItemLabel(final FacesContext context,
                                   final SelectBooleanCheckbox checkbox,
                                   final String clientId) throws IOException {
        final String label = checkbox.getItemLabel();

        if (label != null) {
            final ResponseWriter writer = context.getResponseWriter();

            writer.startElement("span", null);
            writer.writeAttribute("class", HTML.CHECKBOX_LABEL_CLASS, null);
            writer.writeText(label, "itemLabel");
            writer.endElement("span");
        }
    }

    protected void encodeMarkup(final FacesContext context, final SelectBooleanCheckbox checkbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = checkbox.getClientId(context);
        final boolean checked = Boolean.valueOf(ComponentUtils.getValueToRender(context, checkbox));
        final boolean disabled = checkbox.isDisabled();

        final String style = checkbox.getStyle();
        String styleClass = checkbox.getStyleClass();
        styleClass = styleClass == null ? HTML.CHECKBOX_CLASS : HTML.CHECKBOX_CLASS + " " + styleClass;

        writer.startElement("div", checkbox);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        encodeInput(context, checkbox, clientId, checked, disabled);
        encodeOutput(context, checkbox, checked, disabled);
        encodeItemLabel(context, checkbox, clientId);

        writer.endElement("div");
    }

    protected void encodeOutput(final FacesContext context,
                                final SelectBooleanCheckbox checkbox,
                                final boolean checked,
                                final boolean disabled) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        String styleClass = HTML.CHECKBOX_BOX_CLASS;
        styleClass = checked ? styleClass + " ui-state-active" : styleClass;
        styleClass = !checkbox.isValid() ? styleClass + " ui-state-error" : styleClass;
        styleClass = disabled ? styleClass + " ui-state-disabled" : styleClass;

        String iconClass = HTML.CHECKBOX_ICON_CLASS;
        iconClass = checked ? iconClass + " " + HTML.CHECKBOX_CHECKED_ICON_CLASS : iconClass;

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass, null);
        writer.endElement("span");

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final SelectBooleanCheckbox checkbox) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = checkbox.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("SelectBooleanCheckbox", checkbox.resolveWidgetVar(), clientId, false);

        encodeClientBehaviors(context, checkbox, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        return ((submittedValue instanceof Boolean) ? submittedValue : Boolean.valueOf(submittedValue.toString()));
    }

    protected boolean isChecked(final String value) {
        return value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
    }
}
