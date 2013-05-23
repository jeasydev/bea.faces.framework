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
package org.primefaces.component.progressbar;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.context.RequestContext;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class ProgressBarRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final ProgressBar progressBar = (ProgressBar) component;
        final String clientId = progressBar.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        if (params.containsKey(clientId)) {
            RequestContext.getCurrentInstance().addCallbackParam(progressBar.getClientId(context) + "_value",
                                                                 progressBar.getValue());
        }

        decodeBehaviors(context, progressBar);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ProgressBar progressBar = (ProgressBar) component;

        encodeMarkup(context, progressBar);

        if (!progressBar.isDisplayOnly()) {
            encodeScript(context, progressBar);
        }
    }

    protected void encodeMarkup(final FacesContext context, final ProgressBar progressBar) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final int value = progressBar.getValue();
        final String labelTemplate = progressBar.getLabelTemplate();
        final String style = progressBar.getStyle();
        String styleClass = progressBar.getStyleClass();
        styleClass = styleClass == null ? ProgressBar.CONTAINER_CLASS : ProgressBar.CONTAINER_CLASS + " " + styleClass;

        if (progressBar.isDisabled()) {
            styleClass = styleClass + " ui-state-disabled";
        }

        writer.startElement("div", progressBar);
        writer.writeAttribute("id", progressBar.getClientId(context), "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) {
            writer.writeAttribute("style", style, "style");
        }

        // value
        writer.startElement("div", progressBar);
        writer.writeAttribute("class", ProgressBar.VALUE_CLASS, null);
        if (value != 0) {
            writer.writeAttribute("style", "display:block;width:" + value + "%", style);
        }
        writer.endElement("div");

        // label
        writer.startElement("div", progressBar);
        writer.writeAttribute("class", ProgressBar.LABEL_CLASS, null);
        if (labelTemplate != null && value != 0) {
            writer.writeAttribute("style", "display:block", style);
            writer.write(labelTemplate.replaceAll("\\{value\\}", String.valueOf(value)));
        }
        writer.endElement("div");

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final ProgressBar progressBar) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = progressBar.getClientId(context);
        final boolean isAjax = progressBar.isAjax();

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("ProgressBar", progressBar.resolveWidgetVar(), clientId, true).attr("initialValue",
                                                                                      progressBar.getValue())
            .attr("ajax", isAjax).attr("labelTemplate", progressBar.getLabelTemplate(), null);

        if (isAjax) {
            wb.attr("interval", progressBar.getInterval());

            encodeClientBehaviors(context, progressBar, wb);
        }

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }
}
