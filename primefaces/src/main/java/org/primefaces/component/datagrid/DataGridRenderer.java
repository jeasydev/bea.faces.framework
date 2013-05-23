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
package org.primefaces.component.datagrid;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.DataRenderer;
import org.primefaces.util.WidgetBuilder;

public class DataGridRenderer extends DataRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final DataGrid grid = (DataGrid) component;
        context.getExternalContext().getRequestParameterMap();

        if (grid.isPaginationRequest(context)) {
            grid.updatePaginationData(context, grid);

            if (grid.isLazy()) {
                grid.loadLazyData();
            }

            encodeTable(context, grid);
        } else {
            encodeMarkup(context, grid);
            encodeScript(context, grid);
        }
    }

    protected void encodeMarkup(final FacesContext context, final DataGrid grid) throws IOException {
        if (grid.isLazy()) {
            grid.loadLazyData();
        }

        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = grid.getClientId();
        final boolean hasPaginator = grid.isPaginator();
        final boolean empty = grid.getRowCount() == 0;
        final String paginatorPosition = grid.getPaginatorPosition();
        final String styleClass = grid.getStyleClass() == null ? DataGrid.DATAGRID_CLASS : DataGrid.DATAGRID_CLASS
            + " " + grid.getStyleClass();
        final String contentClass = empty ? DataGrid.EMPTY_CONTENT_CLASS : DataGrid.CONTENT_CLASS;

        if (hasPaginator) {
            grid.calculateFirst();
        }

        writer.startElement("div", grid);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");

        encodeFacet(context, grid, "header", DataGrid.HEADER_CLASS);

        if (hasPaginator && !paginatorPosition.equalsIgnoreCase("bottom")) {
            encodePaginatorMarkup(context, grid, "top");
        }

        writer.startElement("div", grid);
        writer.writeAttribute("id", clientId + "_content", null);
        writer.writeAttribute("class", contentClass, null);

        if (empty) {
            writer.write(grid.getEmptyMessage());
        } else {
            encodeTable(context, grid);
        }

        writer.endElement("div");

        if (hasPaginator && !paginatorPosition.equalsIgnoreCase("top")) {
            encodePaginatorMarkup(context, grid, "bottom");
        }

        encodeFacet(context, grid, "footer", DataGrid.FOOTER_CLASS);

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final DataGrid grid) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = grid.getClientId();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("DataGrid", grid.resolveWidgetVar(), clientId, false);

        if (grid.isPaginator()) {
            encodePaginatorConfig(context, grid, wb);
        }

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeTable(final FacesContext context, final DataGrid grid) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        final int columns = grid.getColumns();
        int rowIndex = grid.getFirst();
        final int rows = grid.getRows();
        final int itemsToRender = rows != 0 ? rows : grid.getRowCount();
        final int numberOfRowsToRender = (itemsToRender + columns - 1) / columns;
        int renderedItems = 0;

        writer.startElement("table", grid);
        writer.writeAttribute("class", DataGrid.TABLE_CLASS, null);
        writer.startElement("tbody", null);

        for (int i = 0; i < numberOfRowsToRender; i++) {
            writer.startElement("tr", null);
            writer.writeAttribute("class", DataGrid.TABLE_ROW_CLASS, null);

            for (int j = 0; j < columns; j++) {
                writer.startElement("td", null);
                writer.writeAttribute("class", DataGrid.TABLE_COLUMN_CLASS, null);

                if (renderedItems < itemsToRender) {
                    grid.setRowIndex(rowIndex);

                    if (grid.isRowAvailable()) {
                        renderChildren(context, grid);
                        rowIndex++;
                        renderedItems++;
                    }
                }

                writer.endElement("td");
            }
            writer.endElement("tr");
        }

        grid.setRowIndex(-1); // cleanup

        writer.endElement("tbody");
        writer.endElement("table");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}