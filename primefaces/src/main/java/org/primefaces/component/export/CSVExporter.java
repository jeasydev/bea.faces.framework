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
package org.primefaces.component.export;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.util.Constants;

public class CSVExporter extends Exporter {

    protected void addColumnFacets(final Writer writer, final DataTable table, final ColumnType columnType)
        throws IOException {
        boolean firstCellWritten = false;

        for (final UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                if (firstCellWritten) {
                    writer.write(",");
                }

                addColumnValue(writer, col.getFacet(columnType.facet()));
                firstCellWritten = true;
            }
        }

        writer.write("\n");
    }

    protected void addColumnValue(final Writer writer, final List<UIComponent> components) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final FacesContext context = FacesContext.getCurrentInstance();

        for (final UIComponent component : components) {
            if (component.isRendered()) {
                String value = exportValue(context, component);

                // escape double quotes
                value = value.replaceAll("\"", "\"\"");

                builder.append(value);
            }
        }

        writer.write("\"" + builder.toString() + "\"");
    }

    protected void addColumnValue(final Writer writer, final UIComponent component) throws IOException {
        String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);

        // escape double quotes
        value = value.replaceAll("\"", "\"\"");

        writer.write("\"" + value + "\"");
    }

    protected void addColumnValues(final Writer writer, final List<UIColumn> columns) throws IOException {
        for (final Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
            addColumnValue(writer, iterator.next().getChildren());

            if (iterator.hasNext()) writer.write(",");
        }
    }

    protected void configureResponse(final ExternalContext externalContext, final String filename) {
        externalContext.setResponseContentType("text/csv");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + filename + ".csv");
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
    }

    @Override
    public void export(final FacesContext context,
                       final DataTable table,
                       final String filename,
                       final boolean pageOnly,
                       final boolean selectionOnly,
                       final String encodingType,
                       final MethodExpression preProcessor,
                       final MethodExpression postProcessor) throws IOException {
        final ExternalContext externalContext = context.getExternalContext();
        configureResponse(externalContext, filename);
        final Writer writer = externalContext.getResponseOutputWriter();

        addColumnFacets(writer, table, ColumnType.HEADER);

        if (pageOnly) {
            exportPageOnly(context, table, writer);
        } else if (selectionOnly) {
            exportSelectionOnly(context, table, writer);
        } else {
            exportAll(context, table, writer);
        }

        if (table.hasFooterColumn()) {
            addColumnFacets(writer, table, ColumnType.FOOTER);
        }

        writer.flush();
        writer.close();

        externalContext.responseFlushBuffer();
    }

    protected void exportAll(final FacesContext context, final DataTable table, final Writer writer) throws IOException {
        final int first = table.getFirst();
        final int rowCount = table.getRowCount();
        final int rows = table.getRows();
        final boolean lazy = table.isLazy();

        if (lazy) {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                if (rowIndex % rows == 0) {
                    table.setFirst(rowIndex);
                    table.loadLazyData();
                }

                exportRow(table, writer, rowIndex);
            }

            // restore
            table.setFirst(first);
            table.loadLazyData();
        } else {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, writer, rowIndex);
            }

            // restore
            table.setFirst(first);
        }
    }

    protected void exportCells(final DataTable table, final Writer writer) throws IOException {
        boolean firstCellWritten = false;

        for (final UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                if (firstCellWritten) {
                    writer.write(",");
                }

                addColumnValue(writer, col.getChildren());
                firstCellWritten = true;
            }
        }
    }

    protected void exportPageOnly(final FacesContext context, final DataTable table, final Writer writer)
        throws IOException {
        final int first = table.getFirst();
        final int rowsToExport = first + table.getRows();

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(table, writer, rowIndex);
        }
    }

    protected void exportRow(final DataTable table, final Writer writer, final int rowIndex) throws IOException {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, writer);

        writer.write("\n");
    }

    protected void exportSelectionOnly(final FacesContext context, final DataTable table, final Writer writer)
        throws IOException {
        final Object selection = table.getSelection();
        final String var = table.getVar();

        if (selection != null) {
            final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            if (selection.getClass().isArray()) {
                final int size = Array.getLength(selection);

                for (int i = 0; i < size; i++) {
                    requestMap.put(var, Array.get(selection, i));

                    exportCells(table, writer);
                }
            } else {
                requestMap.put(var, selection);

                exportCells(table, writer);
            }
        }
    }
}
