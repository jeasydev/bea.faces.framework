/*
 * Copyright 2009,2010 Prime Technology.
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
import java.util.ArrayList;
import java.util.List;

public class DefaultDashboardModel implements DashboardModel, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final List<DashboardColumn> columns;

    public DefaultDashboardModel() {
        columns = new ArrayList<DashboardColumn>();
    }

    @Override
    public void addColumn(final DashboardColumn column) {
        columns.add(column);
    }

    @Override
    public DashboardColumn getColumn(final int index) {
        return columns.get(index);
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public List<DashboardColumn> getColumns() {
        return columns;
    }

    @Override
    public void transferWidget(final DashboardColumn fromColumn,
                               final DashboardColumn toColumn,
                               final String widgetId,
                               final int index) {
        fromColumn.removeWidget(widgetId);
        toColumn.addWidget(index, widgetId);
    }
}
