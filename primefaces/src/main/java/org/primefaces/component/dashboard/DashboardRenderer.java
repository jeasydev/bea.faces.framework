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
package org.primefaces.component.dashboard;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class DashboardRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Dashboard dashboard = (Dashboard) component;

        encodeMarkup(context, dashboard);
        encodeScript(context, dashboard);
    }

    protected void encodeMarkup(final FacesContext contextr, final Dashboard dashboard) throws IOException {
        final ResponseWriter writer = contextr.getResponseWriter();
        final String clientId = dashboard.getClientId(contextr);

        writer.startElement("div", dashboard);
        writer.writeAttribute("id", clientId, "id");
        final String styleClass = dashboard.getStyleClass() != null ? Dashboard.CONTAINER_CLASS + " "
            + dashboard.getStyleClass() : Dashboard.CONTAINER_CLASS;
        writer.writeAttribute("class", styleClass, "styleClass");
        if (dashboard.getStyle() != null) writer.writeAttribute("style", dashboard.getStyle(), "style");

        final DashboardModel model = dashboard.getModel();
        if (model != null) {
            for (final DashboardColumn column : model.getColumns()) {
                writer.startElement("div", null);
                writer.writeAttribute("class", Dashboard.COLUMN_CLASS, null);

                for (final String widgetId : column.getWidgets()) {
                    final Panel widget = findWidget(widgetId, dashboard);

                    if (widget != null) renderChild(contextr, widget);
                }

                writer.endElement("div");
            }
        }

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final Dashboard dashboard) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = dashboard.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Dashboard", dashboard.resolveWidgetVar(), clientId, false).attr("disabled", dashboard.isDisabled(),
                                                                                   false);

        encodeClientBehaviors(context, dashboard, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected Panel findWidget(final String id, final Dashboard dashboard) {
        for (final UIComponent child : dashboard.getChildren()) {
            final Panel panel = (Panel) child;

            if (panel.getId().equals(id)) return panel;
        }

        return null;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}