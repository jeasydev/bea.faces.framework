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
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.util.Constants;

public class ExcelExporter extends Exporter {

    protected void addColumnFacets(final DataTable table, final Sheet sheet, final ColumnType columnType) {
        final int sheetRowIndex = columnType.equals(ColumnType.HEADER) ? 0 : (sheet.getLastRowNum() + 1);
        final Row rowHeader = sheet.createRow(sheetRowIndex);

        for (final UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                addColumnValue(rowHeader, col.getFacet(columnType.facet()));
            }
        }
    }

    protected void addColumnValue(final Row row, final List<UIComponent> components) {
        final int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
        final Cell cell = row.createCell(cellIndex);
        final StringBuilder builder = new StringBuilder();
        final FacesContext context = FacesContext.getCurrentInstance();

        for (final UIComponent component : components) {
            if (component.isRendered()) {
                final String value = exportValue(context, component);

                if (value != null) builder.append(value);
            }
        }

        cell.setCellValue(new HSSFRichTextString(builder.toString()));
    }

    protected void addColumnValue(final Row row, final UIComponent component) {
        final int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
        final Cell cell = row.createCell(cellIndex);
        final String value = component == null ? "" : exportValue(FacesContext.getCurrentInstance(), component);

        cell.setCellValue(new HSSFRichTextString(value));
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
        final Workbook wb = new HSSFWorkbook();
        final Sheet sheet = wb.createSheet();

        if (preProcessor != null) {
            preProcessor.invoke(context.getELContext(), new Object[] { wb });
        }

        addColumnFacets(table, sheet, ColumnType.HEADER);

        if (pageOnly) {
            exportPageOnly(context, table, sheet);
        } else if (selectionOnly) {
            exportSelectionOnly(context, table, sheet);
        } else {
            exportAll(context, table, sheet);
        }

        if (table.hasFooterColumn()) {
            addColumnFacets(table, sheet, ColumnType.FOOTER);
        }

        table.setRowIndex(-1);

        if (postProcessor != null) {
            postProcessor.invoke(context.getELContext(), new Object[] { wb });
        }

        writeExcelToResponse(context.getExternalContext(), wb, filename);
    }

    protected void exportAll(final FacesContext context, final DataTable table, final Sheet sheet) {
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

                exportRow(table, sheet, rowIndex);
            }

            // restore
            table.setFirst(first);
            table.loadLazyData();
        } else {
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                exportRow(table, sheet, rowIndex);
            }

            // restore
            table.setFirst(first);
        }
    }

    protected void exportCells(final DataTable table, final Sheet sheet) {
        final int sheetRowIndex = sheet.getLastRowNum() + 1;
        final Row row = sheet.createRow(sheetRowIndex);

        for (final UIColumn col : table.getColumns()) {
            if (!col.isRendered()) {
                continue;
            }

            if (col instanceof DynamicColumn) {
                ((DynamicColumn) col).applyModel();
            }

            if (col.isExportable()) {
                addColumnValue(row, col.getChildren());
            }
        }
    }

    protected void exportPageOnly(final FacesContext context, final DataTable table, final Sheet sheet) {
        final int first = table.getFirst();
        final int rowsToExport = first + table.getRows();

        for (int rowIndex = first; rowIndex < rowsToExport; rowIndex++) {
            exportRow(table, sheet, rowIndex);
        }
    }

    protected void exportRow(final DataTable table, final Sheet sheet, final int rowIndex) {
        table.setRowIndex(rowIndex);

        if (!table.isRowAvailable()) {
            return;
        }

        exportCells(table, sheet);
    }

    protected void exportSelectionOnly(final FacesContext context, final DataTable table, final Sheet sheet) {
        final Object selection = table.getSelection();
        final String var = table.getVar();

        if (selection != null) {
            final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

            if (selection.getClass().isArray()) {
                final int size = Array.getLength(selection);

                for (int i = 0; i < size; i++) {
                    requestMap.put(var, Array.get(selection, i));

                    exportCells(table, sheet);
                }
            } else {
                requestMap.put(var, selection);

                exportCells(table, sheet);
            }
        }
    }

    protected void writeExcelToResponse(final ExternalContext externalContext,
                                        final Workbook generatedExcel,
                                        final String filename) throws IOException {
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + filename + ".xls");
        externalContext.addResponseCookie(Constants.DOWNLOAD_COOKIE, "true", new HashMap<String, Object>());

        final OutputStream out = externalContext.getResponseOutputStream();
        generatedExcel.write(out);
        externalContext.responseFlushBuffer();
    }
}