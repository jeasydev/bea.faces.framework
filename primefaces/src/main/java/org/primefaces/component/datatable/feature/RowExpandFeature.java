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
package org.primefaces.component.datatable.feature;

import java.io.IOException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;
import org.primefaces.component.rowexpansion.RowExpansion;

public class RowExpandFeature implements DataTableFeature {

    @Override
    public void decode(final FacesContext context, final DataTable table) {
        throw new RuntimeException("RowExpandFeature should not encode.");
    }

    @Override
    public void encode(final FacesContext context, final DataTableRenderer renderer, final DataTable table)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final int expandedRowIndex = Integer.parseInt(params.get(table.getClientId(context) + "_expandedRowIndex"));
        final String rowIndexVar = table.getRowIndexVar();
        final RowExpansion rowExpansion = table.getRowExpansion();

        String styleClass = DataTable.EXPANDED_ROW_CONTENT_CLASS + " ui-widget-content";
        if (rowExpansion.getStyleClass() != null) {
            styleClass = styleClass + " " + rowExpansion.getStyleClass();
        }

        table.setRowIndex(expandedRowIndex);

        if (rowIndexVar != null) {
            context.getExternalContext().getRequestMap().put(rowIndexVar, expandedRowIndex);
        }

        writer.startElement("tr", null);
        writer.writeAttribute("style", "display:none", null);
        writer.writeAttribute("class", styleClass, null);

        writer.startElement("td", null);
        writer.writeAttribute("colspan", table.getColumnsCount(), null);

        table.getRowExpansion().encodeAll(context);

        writer.endElement("td");

        writer.endElement("tr");

        table.setRowIndex(-1);
    }

    @Override
    public boolean shouldDecode(final FacesContext context, final DataTable table) {
        return false;
    }

    @Override
    public boolean shouldEncode(final FacesContext context, final DataTable table) {
        return context.getExternalContext().getRequestParameterMap().containsKey(table.getClientId(context)
                                                                                     + "_rowExpansion");
    }

}
