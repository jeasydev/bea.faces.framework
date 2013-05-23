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
package org.primefaces.component.inputtextarea;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.AutoCompleteEvent;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;

public class InputTextareaRenderer extends InputRenderer {

    protected String createStyleClass(final InputTextarea inputTextarea) {
        String defaultClass = InputTextarea.STYLE_CLASS;
        defaultClass = inputTextarea.isValid() ? defaultClass : defaultClass + " ui-state-error";
        defaultClass = !inputTextarea.isDisabled() ? defaultClass : defaultClass + " ui-state-disabled";

        String styleClass = inputTextarea.getStyleClass();
        styleClass = styleClass == null ? defaultClass : defaultClass + " " + styleClass;

        if (inputTextarea.isAutoResize()) {
            styleClass = styleClass + " ui-inputtextarea-resizable";
        }

        return styleClass;
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final InputTextarea inputTextarea = (InputTextarea) component;

        if (inputTextarea.isDisabled() || inputTextarea.isReadonly()) {
            return;
        }

        decodeBehaviors(context, inputTextarea);

        final String clientId = inputTextarea.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String submittedValue = params.get(clientId);

        inputTextarea.setSubmittedValue(submittedValue);

        // AutoComplete event
        final String query = params.get(clientId + "_query");
        if (query != null) {
            final AutoCompleteEvent autoCompleteEvent = new AutoCompleteEvent(inputTextarea, query);
            autoCompleteEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            inputTextarea.queueEvent(autoCompleteEvent);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final InputTextarea inputTextarea = (InputTextarea) component;
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String query = params.get(inputTextarea.getClientId(context) + "_query");

        if (query != null) {
            encodeSuggestions(context, inputTextarea, query);
        } else {
            encodeMarkup(context, inputTextarea);
            encodeScript(context, inputTextarea);
        }
    }

    protected void encodeMarkup(final FacesContext context, final InputTextarea inputTextarea) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputTextarea.getClientId(context);

        writer.startElement("textarea", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);

        renderPassThruAttributes(context, inputTextarea, HTML.INPUT_TEXTAREA_ATTRS);

        if (inputTextarea.isDisabled()) writer.writeAttribute("disabled", "disabled", null);
        if (inputTextarea.isReadonly()) writer.writeAttribute("readonly", "readonly", null);
        if (inputTextarea.getStyle() != null) writer.writeAttribute("style", inputTextarea.getStyle(), null);

        writer.writeAttribute("class", createStyleClass(inputTextarea), "styleClass");

        final String valueToRender = ComponentUtils.getValueToRender(context, inputTextarea);
        if (valueToRender != null) {
            writer.writeText(valueToRender, "value");
        }

        writer.endElement("textarea");
    }

    protected void encodeScript(final FacesContext context, final InputTextarea inputTextarea) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = inputTextarea.getClientId(context);
        final boolean autoResize = inputTextarea.isAutoResize();
        final String counter = inputTextarea.getCounter();

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("InputTextarea", inputTextarea.resolveWidgetVar(), clientId, true).attr("autoResize", autoResize)
            .attr("maxlength", inputTextarea.getMaxlength(), Integer.MAX_VALUE);

        if (counter != null) {
            final UIComponent counterComponent = inputTextarea.findComponent(counter);
            if (counterComponent == null) {
                throw new FacesException("Cannot find component \"" + counter + "\" in view.");
            }

            wb.attr("counter", counterComponent.getClientId(context)).attr("counterTemplate",
                                                                           inputTextarea.getCounterTemplate(), null);
        }

        if (inputTextarea.getCompleteMethod() != null) {
            wb.attr("autoComplete", true).attr("minQueryLength", inputTextarea.getMinQueryLength())
                .attr("queryDelay", inputTextarea.getQueryDelay()).attr("scrollHeight",
                                                                        inputTextarea.getScrollHeight(),
                                                                        Integer.MAX_VALUE);
        }

        encodeClientBehaviors(context, inputTextarea, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @SuppressWarnings("unchecked")
    public void encodeSuggestions(final FacesContext context, final InputTextarea inputTextarea, final String query)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final List<Object> items = inputTextarea.getSuggestions();

        writer.startElement("ul", inputTextarea);
        writer.writeAttribute("class", AutoComplete.LIST_CLASS, null);

        for (final Object item : items) {
            writer.startElement("li", null);
            writer.writeAttribute("class", AutoComplete.ITEM_CLASS, null);
            writer.writeAttribute("data-item-value", item, null);
            writer.writeText(item, null);

            writer.endElement("li");
        }

        writer.endElement("ul");
    }
}
