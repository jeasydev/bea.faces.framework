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
package org.primefaces.renderkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.UIData;
import org.primefaces.component.paginator.CurrentPageReportRenderer;
import org.primefaces.component.paginator.FirstPageLinkRenderer;
import org.primefaces.component.paginator.JumpToPageDropdownRenderer;
import org.primefaces.component.paginator.LastPageLinkRenderer;
import org.primefaces.component.paginator.NextPageLinkRenderer;
import org.primefaces.component.paginator.PageLinksRenderer;
import org.primefaces.component.paginator.PaginatorElementRenderer;
import org.primefaces.component.paginator.PrevPageLinkRenderer;
import org.primefaces.component.paginator.RowsPerPageDropdownRenderer;
import org.primefaces.util.WidgetBuilder;

public class DataRenderer extends CoreRenderer {

    protected Map<String, PaginatorElementRenderer> paginatorElements;

    public DataRenderer() {
        paginatorElements = new HashMap<String, PaginatorElementRenderer>();
        paginatorElements.put("{CurrentPageReport}", new CurrentPageReportRenderer());
        paginatorElements.put("{FirstPageLink}", new FirstPageLinkRenderer());
        paginatorElements.put("{PreviousPageLink}", new PrevPageLinkRenderer());
        paginatorElements.put("{NextPageLink}", new NextPageLinkRenderer());
        paginatorElements.put("{LastPageLink}", new LastPageLinkRenderer());
        paginatorElements.put("{PageLinks}", new PageLinksRenderer());
        paginatorElements.put("{RowsPerPageDropdown}", new RowsPerPageDropdownRenderer());
        paginatorElements.put("{JumpToPageDropdown}", new JumpToPageDropdownRenderer());
    }

    public void encodeFacet(final FacesContext context, final UIData data, final String facet, final String styleClass)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent component = data.getFacet(facet);

        if (component != null && component.isRendered()) {
            writer.startElement("div", null);
            writer.writeAttribute("class", styleClass, null);
            component.encodeAll(context);
            writer.endElement("div");
        }
    }

    protected void encodePaginatorConfig(final FacesContext context, final UIData uidata, final WidgetBuilder wb)
        throws IOException {
        context.getResponseWriter();
        final String clientId = uidata.getClientId(context);
        final String paginatorPosition = uidata.getPaginatorPosition();
        String paginatorContainers = null;
        final String currentPageTemplate = uidata.getCurrentPageReportTemplate();

        if (paginatorPosition.equalsIgnoreCase("both"))
            paginatorContainers = "'" + clientId + "_paginator_top','" + clientId + "_paginator_bottom'";
        else paginatorContainers = "'" + clientId + "_paginator_" + paginatorPosition + "'";

        wb.append(",paginator:{").append("id:[").append(paginatorContainers).append("]").append(",rows:")
            .append(uidata.getRows()).append(",rowCount:").append(uidata.getRowCount()).append(",page:")
            .append(uidata.getPage());

        if (currentPageTemplate != null) wb.append(",currentPageTemplate:'").append(currentPageTemplate).append("'");

        if (uidata.getPageLinks() != 10) wb.append(",pageLinks:").append(uidata.getPageLinks());

        if (!uidata.isPaginatorAlwaysVisible()) wb.append(",alwaysVisible:false");

        wb.append("}");
    }

    protected void encodePaginatorMarkup(final FacesContext context, final UIData uidata, final String position)
        throws IOException {
        if (!uidata.isPaginatorAlwaysVisible() && uidata.getPageCount() <= 1) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final boolean isTop = position.equals("top");
        final boolean hidden = !uidata.isPaginatorAlwaysVisible() && uidata.getPageCount() == 1;

        String styleClass = isTop ? UIData.PAGINATOR_TOP_CONTAINER_CLASS : UIData.PAGINATOR_BOTTOM_CONTAINER_CLASS;
        final String id = uidata.getClientId(context) + "_paginator_" + position;

        // add corners
        if (!isTop && uidata.getFooter() == null) {
            styleClass = styleClass + " ui-corner-bottom";
        } else if (isTop && uidata.getHeader() == null) {
            styleClass = styleClass + " ui-corner-top";
        }

        writer.startElement("div", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("class", styleClass, null);
        if (hidden) {
            writer.writeAttribute("style", "display:none", null);
        }

        final String[] elements = uidata.getPaginatorTemplate().split(" ");
        for (final String element : elements) {
            final PaginatorElementRenderer renderer = paginatorElements.get(element);
            if (renderer != null)
                renderer.render(context, uidata);
            else writer.write(element + " ");
        }

        writer.endElement("div");
    }
}
