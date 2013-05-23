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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.util.Constants;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFExporter extends Exporter {

    private Font cellFont;
    private Font facetFont;

    protected void addColumnFacets(final DataTable table, final PdfPTable pdfTable, final ColumnType columnType) {
        for (final UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                addColumnValue(pdfTable, col.getFacet(columnType.facet()), facetFont);
            }
        }
    }

    protected void addColumnValue(final PdfPTable pdfTable, final List<UIComponent> components, final Font font) {
        final StringBuilder builder = new StringBuilder();
        final FacesContext context = FacesContext.getCurrentInstance();

        for (final UIComponent component : components) {
            if (component.isRendered()) {
                final String value = exportValue(context, component);

                if (value != null) builder.append(value);
            }
        }

        pdfTable.addCell(new Paragraph(builder.toString(), font));
    }

    protected void addColumnValue(final PdfPTable pdfTable, final UIComponent component, final Font font) {
        final String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);

        pdfTable.addCell(new Paragraph(value, font));
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
        try {
            final Document document = new Document();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            if (preProcessor != null) {
                preProcessor.invoke(context.getELContext(), new Object[] { document });
            }

            if (!document.isOpen()) {
                document.open();
            }

            document.add(exportPDFTable(context, table, pageOnly, selectionOnly, encodingType));

            if (postProcessor != null) {
                postProcessor.invoke(context.getELContext(), new Object[] { document });
            }

            document.close();

            writePDFToResponse(context.getExternalContext(), baos, filename);

        } catch (final DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }

    protected void exportAll(final FacesContext context, final DataTable table, final PdfPTable pdfTable) {
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

                exportRow(table, pdfTable, rowIndex);
            }

            // restore
            table.setFirst(first);
            table.loadLazyData();
        } else {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, pdfTable, rowIndex);
            }

            // restore
            table.setFirst(first);
        }
    }

    protected void exportCells(final DataTable table, final PdfPTable pdfTable) {
        for (final UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                addColumnValue(pdfTable, col.getChildren(), cellFont);
            }
        }
    }

    protected void exportPageOnly(final FacesContext context, final DataTable table, final PdfPTable pdfTable) {
        final int first = table.getFirst();
        final int rowsToExport = first + table.getRows();

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(table, pdfTable, rowIndex);
        }
    }

    protected PdfPTable exportPDFTable(final FacesContext context,
                                       final DataTable table,
                                       final boolean pageOnly,
                                       final boolean selectionOnly,
                                       final String encoding) {
        final int columnsCount = getColumnsCount(table);
        final PdfPTable pdfTable = new PdfPTable(columnsCount);
        cellFont = FontFactory.getFont(FontFactory.TIMES, encoding);
        facetFont = FontFactory.getFont(FontFactory.TIMES, encoding, Font.DEFAULTSIZE, Font.BOLD);

        addColumnFacets(table, pdfTable, ColumnType.HEADER);

        if (pageOnly) {
            exportPageOnly(context, table, pdfTable);
        } else if (selectionOnly) {
            exportSelectionOnly(context, table, pdfTable);
        } else {
            exportAll(context, table, pdfTable);
        }

        if (table.hasFooterColumn()) {
            addColumnFacets(table, pdfTable, ColumnType.FOOTER);
        }

        table.setRowIndex(-1);

        return pdfTable;
    }

    protected void exportRow(final DataTable table, final PdfPTable pdfTable, final int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, pdfTable);
    }

    protected void exportSelectionOnly(final FacesContext context, final DataTable table, final PdfPTable pdfTable) {
        final Object selection = table.getSelection();
        final String var = table.getVar();

        if (selection != null) {
            final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            if (selection.getClass().isArray()) {
                final int size = Array.getLength(selection);

                for (int i = 0; i < size; i++) {
                    requestMap.put(var, Array.get(selection, i));

                    exportCells(table, pdfTable);
                }
            } else {
                requestMap.put(var, selection);

                exportCells(table, pdfTable);
            }
        }
    }

    protected int getColumnsCount(final DataTable table) {
        int count = 0;

        for (final UIComponent child : table.getChildren()) {
            if (!child.isRendered()) {
                continue;
            }

            if (child instanceof Column) {
                final Column column = (Column) child;

                if (column.isExportable()) {
                    count++;
                }
            } else if (child instanceof Columns) {
                final Columns columns = (Columns) child;

                if (columns.isExportable()) {
                    count += columns.getRowCount();
                }
            }
        }

        return count;
    }

    protected void writePDFToResponse(final ExternalContext externalContext,
                                      final ByteArrayOutputStream baos,
                                      final String fileName) throws IOException, DocumentException {
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
        externalContext.setResponseContentLength(baos.size());
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());
        final OutputStream out = externalContext.getResponseOutputStream();
        baos.writeTo(out);
        externalContext.responseFlushBuffer();
    }
}