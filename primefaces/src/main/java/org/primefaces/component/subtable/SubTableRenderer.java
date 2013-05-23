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
package org.primefaces.component.subtable;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.row.Row;
import org.primefaces.renderkit.CoreRenderer;

public class SubTableRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    protected void encodeColumnFooter(final FacesContext context, final SubTable table, final Column column)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        final String style = column.getStyle();
        String styleClass = column.getStyleClass();
        styleClass = styleClass == null ? DataTable.COLUMN_FOOTER_CLASS : DataTable.COLUMN_FOOTER_CLASS + " "
            + styleClass;

        writer.startElement("td", null);
        writer.writeAttribute("class", styleClass, null);
        if (column.getRowspan() != 1) writer.writeAttribute("rowspan", column.getRowspan(), null);
        if (column.getColspan() != 1) writer.writeAttribute("colspan", column.getColspan(), null);
        if (style != null) writer.writeAttribute("style", style, null);

        // Footer content
        final UIComponent facet = column.getFacet("footer");
        final String text = column.getFooterText();
        if (facet != null) {
            facet.encodeAll(context);
        } else if (text != null) {
            writer.write(text);
        }

        writer.endElement("td");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final SubTable table = (SubTable) component;
        final int rowCount = table.getRowCount();

        // header
        encodeHeader(context, table);

        // rows
        for (int i = 0; i < rowCount; i++) {
            encodeRow(context, table, i);
        }

        // footer
        encodeFooter(context, table);
    }

    public void encodeFooter(final FacesContext context, final SubTable table) throws IOException {
        final ColumnGroup group = table.getColumnGroup("footer");

        if (group == null || !group.isRendered()) return;

        final ResponseWriter writer = context.getResponseWriter();

        for (final UIComponent child : group.getChildren()) {
            if (child.isRendered() && child instanceof Row) {
                final Row footerRow = (Row) child;

                writer.startElement("tr", null);
                writer.writeAttribute("class", "ui-widget-header", null);

                for (final UIComponent footerRowChild : footerRow.getChildren()) {
                    if (footerRowChild.isRendered() && footerRowChild instanceof Column) {
                        encodeColumnFooter(context, table, (Column) footerRowChild);
                    }
                }

                writer.endElement("tr");
            }
        }
    }

    public void encodeHeader(final FacesContext context, final SubTable table) throws IOException {
        final UIComponent header = table.getFacet("header");
        if (header == null) return;

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("tr", null);
        writer.writeAttribute("class", "ui-widget-header", null);

        writer.startElement("td", null);
        writer.writeAttribute("class", DataTable.SUBTABLE_HEADER, null);
        writer.writeAttribute("colspan", table.getColumns().size(), null);

        header.encodeAll(context);

        writer.endElement("td");
        writer.endElement("tr");
    }

    public void encodeRow(final FacesContext context, final SubTable table, final int rowIndex) throws IOException {
        table.setRowIndex(rowIndex);
        if (!table.isRowAvailable()) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = table.getClientId(context);

        writer.startElement("tr", null);
        writer.writeAttribute("id", clientId + "_row_" + rowIndex, null);
        writer.writeAttribute("class", DataTable.ROW_CLASS, null);

        for (final Column column : table.getColumns()) {
            final String style = column.getStyle();
            final String styleClass = column.getStyleClass();

            writer.startElement("td", null);
            if (style != null) writer.writeAttribute("style", style, null);
            if (styleClass != null) writer.writeAttribute("class", styleClass, null);

            column.encodeAll(context);

            writer.endElement("td");
        }

        writer.endElement("tr");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
