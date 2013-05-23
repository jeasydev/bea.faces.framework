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
package org.primefaces.component.api;

import java.io.IOException;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.columns.Columns;

public class DynamicColumn implements UIColumn {

    private final int index;
    private final Columns columns;
    private String columnKey;

    public DynamicColumn(final int index, final Columns columns) {
        this.index = index;
        this.columns = columns;
    }

    public void applyModel() {
        columns.setRowIndex(index);
    }

    public void applyStatelessModel() {
        columns.setRowModel(index);
    }

    @Override
    public void encodeAll(final FacesContext context) throws IOException {
        columns.encodeAll(context);
    }

    @Override
    public CellEditor getCellEditor() {
        return columns.getCellEditor();
    }

    @Override
    public List<UIComponent> getChildren() {
        return columns.getChildren();
    }

    @Override
    public String getClientId() {
        return columns.getClientId();
    }

    @Override
    public String getClientId(final FacesContext context) {
        return columns.getClientId(context);
    }

    @Override
    public int getColspan() {
        return columns.getColspan();
    }

    @Override
    public String getColumnKey() {
        return columnKey;
    }

    @Override
    public String getContainerClientId(final FacesContext context) {
        return columns.getContainerClientId(context);
    }

    @Override
    public UIComponent getFacet(final String facet) {
        return columns.getFacet(facet);
    }

    @Override
    public String getFilterMatchMode() {
        return columns.getFilterMatchMode();
    }

    @Override
    public int getFilterMaxLength() {
        return columns.getFilterMaxLength();
    }

    @Override
    public Object getFilterOptions() {
        return columns.getFilterOptions();
    }

    @Override
    public String getFilterPosition() {
        return columns.getFilterPosition();
    }

    @Override
    public String getFilterStyle() {
        return columns.getFilterStyle();
    }

    @Override
    public String getFilterStyleClass() {
        return columns.getFilterStyleClass();
    }

    @Override
    public String getFooterText() {
        return columns.getFooterText();
    }

    @Override
    public String getHeaderText() {
        return columns.getHeaderText();
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int getRowspan() {
        return columns.getRowspan();
    }

    @Override
    public String getSelectionMode() {
        return columns.getSelectionMode();
    }

    @Override
    public MethodExpression getSortFunction() {
        return columns.getSortFunction();
    }

    @Override
    public String getStyle() {
        return columns.getStyle();
    }

    @Override
    public String getStyleClass() {
        return columns.getStyleClass();
    }

    @Override
    public ValueExpression getValueExpression(final String property) {
        return columns.getValueExpression(property);
    }

    @Override
    public String getWidth() {
        return columns.getWidth();
    }

    @Override
    public boolean isDisabledSelection() {
        return columns.isDisabledSelection();
    }

    @Override
    public boolean isDynamic() {
        return columns.isDynamic();
    }

    @Override
    public boolean isExportable() {
        return columns.isExportable();
    }

    @Override
    public boolean isRendered() {
        return columns.isRendered();
    }

    @Override
    public boolean isResizable() {
        return columns.isResizable();
    }

    public void setColumnKey(final String columnKey) {
        this.columnKey = columnKey;
    }
}
