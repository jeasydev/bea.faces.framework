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
package org.primefaces.component.ajaxstatus;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class AjaxStatusRenderer extends CoreRenderer {

    protected void encodeCallback(final FacesContext context,
                                  final AjaxStatus status,
                                  final String var,
                                  final String event,
                                  final String callback,
                                  final String facetName) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String fn = (String) status.getAttributes().get(callback);

        if (fn != null)
            writer.write(var + ".bindCallback('" + event + "',function(){" + fn + "});");
        else if (status.getFacet(facetName) != null)
            writer.write(var + ".bindFacet('" + event + "', '" + facetName + "');");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final AjaxStatus status = (AjaxStatus) component;

        encodeMarkup(context, status);
        encodeScript(context, status);
    }

    protected void encodeFacet(final FacesContext facesContext,
                               final String clientId,
                               final UIComponent facet,
                               final String facetName,
                               final boolean hidden) throws IOException {
        final ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_" + facetName, null);
        if (hidden) {
            writer.writeAttribute("style", "display:none", null);
        }

        renderChild(facesContext, facet);

        writer.endElement("div");
    }

    protected void encodeMarkup(final FacesContext context, final AjaxStatus status) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = status.getClientId(context);

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);

        if (status.getStyle() != null) writer.writeAttribute("style", status.getStyle(), "style");
        if (status.getStyleClass() != null) writer.writeAttribute("class", status.getStyleClass(), "styleClass");

        for (final String facetName : AjaxStatus.FACETS) {
            final UIComponent facet = status.getFacet(facetName);

            if (facet != null) {
                encodeFacet(context, clientId, facet, facetName, true);
            }
        }

        // Default facet
        final UIComponent defaultFacet = status.getFacet(AjaxStatus.DEFAULT_FACET);
        if (defaultFacet != null) {
            encodeFacet(context, clientId, defaultFacet, AjaxStatus.DEFAULT_FACET, false);
        }

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final AjaxStatus status) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = status.getClientId(context);
        final String widgetVar = status.resolveWidgetVar();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("AjaxStatus", widgetVar, clientId, false);

        startScript(writer, clientId);

        writer.write(wb.build());

        encodeCallback(context, status, widgetVar, "ajaxSend", "onprestart", AjaxStatus.PRESTART_FACET);
        encodeCallback(context, status, widgetVar, "ajaxStart", "onstart", AjaxStatus.START_FACET);
        encodeCallback(context, status, widgetVar, "ajaxError", "onerror", AjaxStatus.ERROR_FACET);
        encodeCallback(context, status, widgetVar, "ajaxSuccess", "onsuccess", AjaxStatus.SUCCESS_FACET);
        encodeCallback(context, status, widgetVar, "ajaxComplete", "oncomplete", AjaxStatus.COMPLETE_FACET);

        endScript(writer);
    }
}