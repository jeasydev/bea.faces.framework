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

import javax.el.MethodExpression;
import org.primefaces.component.api.UIColumn;

public class SortMeta {

    private UIColumn column;

    private String sortField;

    private SortOrder sortOrder;

    private MethodExpression sortFunction;

    public SortMeta() {
    }

    public SortMeta(final UIColumn column,
                    final String sortField,
                    final SortOrder sortOrder,
                    final MethodExpression sortFunction) {
        this.column = column;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public UIColumn getColumn() {
        return column;
    }

    public String getSortField() {
        return sortField;
    }

    public MethodExpression getSortFunction() {
        return sortFunction;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortBy(final UIColumn column) {
        this.column = column;
    }

    public void setSortField(final String sortField) {
        this.sortField = sortField;
    }

    public void setSortFunction(final MethodExpression sortFunction) {
        this.sortFunction = sortFunction;
    }

    public void setSortOrder(final SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}