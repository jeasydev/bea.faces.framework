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
package org.primefaces.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.model.DataModel;

/**
 * Custom lazy loading DataModel to deal with huge datasets
 */
public abstract class LazyDataModel<T> extends DataModel<T> implements SelectableDataModel<T>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int rowIndex = -1;

    private int rowCount;

    private int pageSize;

    private List<T> data;

    public LazyDataModel() {
        super();
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public T getRowData() {
        return data.get(rowIndex);
    }

    @Override
    public T getRowData(final String rowKey) {
        throw new UnsupportedOperationException("getRowData(String rowKey) must be implemented when basic rowKey algorithm is not used.");
    }

    @Override
    public int getRowIndex() {
        return this.rowIndex;
    }

    @Override
    public Object getRowKey(final T object) {
        throw new UnsupportedOperationException("getRowKey(T object) must be implemented when basic rowKey algorithm is not used.");
    }

    @Override
    public Object getWrappedData() {
        return data;
    }

    @Override
    public boolean isRowAvailable() {
        if (data == null) {
            return false;
        }

        return rowIndex >= 0 && rowIndex < data.size();
    }

    public List<T> load(final int first,
                        final int pageSize,
                        final List<SortMeta> multiSortMeta,
                        final Map<String, String> filters) {
        throw new UnsupportedOperationException("Lazy loading is not implemented.");
    }

    public List<T> load(final int first,
                        final int pageSize,
                        final String sortField,
                        final SortOrder sortOrder,
                        final Map<String, String> filters) {
        throw new UnsupportedOperationException("Lazy loading is not implemented.");
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRowCount(final int rowCount) {
        this.rowCount = rowCount;
    }

    @Override
    public void setRowIndex(final int rowIndex) {
        this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
    }

    @Override
    public void setWrappedData(final Object list) {
        this.data = (List) list;
    }
}