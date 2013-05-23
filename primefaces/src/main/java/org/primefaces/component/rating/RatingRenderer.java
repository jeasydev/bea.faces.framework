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
package org.primefaces.component.rating;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.InputRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class RatingRenderer extends InputRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final Rating rating = (Rating) component;
        if (rating.isDisabled() || rating.isReadonly()) {
            return;
        }

        final String clientId = rating.getClientId();
        final String submittedValue = context.getExternalContext().getRequestParameterMap().get(clientId + "_input");

        rating.setSubmittedValue(submittedValue);

        decodeBehaviors(context, rating);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Rating rating = (Rating) component;

        encodeMarkup(context, rating);
        encodeScript(context, rating);
    }

    protected void encodeIcon(final FacesContext context, final String styleClass) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        writer.startElement("a", null);
        writer.endElement("a");

        writer.endElement("div");
    }

    protected void encodeInput(final FacesContext context, final String id, final String value) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("autocomplete", "off", null);
        if (value != null) {
            writer.writeAttribute("value", value, null);
        }
        writer.endElement("input");
    }

    protected void encodeMarkup(final FacesContext context, final Rating rating) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = rating.getClientId(context);
        final String valueToRender = ComponentUtils.getValueToRender(context, rating);
        final Integer value = isValueBlank(valueToRender) ? null : Integer.valueOf(valueToRender);
        final int stars = rating.getStars();
        final boolean disabled = rating.isDisabled();
        final boolean readonly = rating.isReadonly();
        final String style = rating.getStyle();
        String styleClass = rating.getStyleClass();
        styleClass = styleClass == null ? Rating.CONTAINER_CLASS : Rating.CONTAINER_CLASS + " " + styleClass;

        if (disabled) {
            styleClass = styleClass + " ui-state-disabled";
        }

        writer.startElement("div", rating);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, null);
        if (style != null) {
            writer.writeAttribute("style", style, null);
        }

        if (rating.isCancel() && !disabled && !readonly) {
            encodeIcon(context, Rating.CANCEL_CLASS);
        }

        for (int i = 0; i < stars; i++) {
            final String starClass = (value != null && i < value.intValue()) ? Rating.STAR_ON_CLASS : Rating.STAR_CLASS;
            encodeIcon(context, starClass);
        }

        encodeInput(context, clientId + "_input", valueToRender);

        writer.endElement("div");
    }

    private void encodeScript(final FacesContext context, final Rating rating) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = rating.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Rating", rating.resolveWidgetVar(), clientId, "rating", true).callback("onRate", "function(value)",
                                                                                          rating.getOnRate())
            .attr("readonly", rating.isReadonly(), false).attr("disabled", rating.isDisabled(), false);

        encodeClientBehaviors(context, rating, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }
}
