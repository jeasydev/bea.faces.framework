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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;
import org.primefaces.model.BeanPropertyComparator;
import org.primefaces.model.ChainedBeanPropertyComparator;
import org.primefaces.model.DynamicChainedPropertyComparator;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

public class SortFeature implements DataTableFeature {

    @Override
    public void decode(final FacesContext context, final DataTable table) {
        table.setRowIndex(-1);
        final String clientId = table.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String sortKey = params.get(clientId + "_sortKey");
        final String sortDir = params.get(clientId + "_sortDir");

        if (table.isMultiSort()) {
            final List<SortMeta> multiSortMeta = new ArrayList<SortMeta>();
            final String[] sortKeys = sortKey.split(",");
            final String[] sortOrders = sortDir.split(",");

            for (int i = 0; i < sortKeys.length; i++) {
                final UIColumn sortColumn = table.findColumn(sortKeys[i]);
                final ValueExpression sortByVE = sortColumn.getValueExpression("sortBy");
                String sortField = null;

                if (sortColumn.isDynamic()) {
                    ((DynamicColumn) sortColumn).applyStatelessModel();
                    sortField = table.resolveDynamicField(sortByVE);
                } else {
                    sortField = table.resolveStaticField(sortByVE);
                }

                multiSortMeta.add(new SortMeta(sortColumn, sortField, SortOrder.valueOf(sortOrders[i]), sortColumn
                    .getSortFunction()));
            }

            table.setMultiSortMeta(multiSortMeta);
        } else {
            final UIColumn sortColumn = table.findColumn(sortKey);
            final ValueExpression sortByVE = sortColumn.getValueExpression("sortBy");
            table.setValueExpression("sortBy", sortByVE);
            table.setSortColumn(sortColumn);
            table.setSortOrder(sortDir);
        }
    }

    @Override
    public void encode(final FacesContext context, final DataTableRenderer renderer, final DataTable table)
        throws IOException {
        table.setFirst(0);

        if (table.isLazy()) {
            table.loadLazyData();
        } else {
            if (table.isMultiSort()) {
                multiSort(context, table);
            } else {
                sortColumn(context, table);
            }
        }

        renderer.encodeTbody(context, table, true);
    }

    private boolean isSortRequest(final FacesContext context, final DataTable table) {
        return context.getExternalContext().getRequestParameterMap().containsKey(table.getClientId(context)
                                                                                     + "_sorting");
    }

    public void multiSort(final FacesContext context, final DataTable table) {
        final Object value = table.getValue();
        final List<SortMeta> sortMeta = table.getMultiSortMeta();
        List list = null;

        if (value == null) {
            return;
        }

        if (value instanceof List) {
            list = (List) value;
        } else if (value instanceof ListDataModel) {
            list = (List) ((ListDataModel) value).getWrappedData();
        } else {
            throw new FacesException("Data type should be java.util.List or javax.faces.model.ListDataModel instance to be sortable.");
        }

        final ChainedBeanPropertyComparator chainedComparator = new ChainedBeanPropertyComparator();
        for (final SortMeta meta : sortMeta) {
            BeanPropertyComparator comparator = null;
            final UIColumn sortColumn = meta.getColumn();

            if (sortColumn.isDynamic()) {
                comparator = new DynamicChainedPropertyComparator((DynamicColumn) sortColumn, sortColumn
                    .getValueExpression("sortBy"), table.getVar(), meta.getSortOrder(), sortColumn.getSortFunction());
            } else {
                comparator = new BeanPropertyComparator(sortColumn.getValueExpression("sortBy"), table.getVar(), meta
                    .getSortOrder(), sortColumn.getSortFunction());
            }

            chainedComparator.addComparator(comparator);
        }

        Collections.sort(list, chainedComparator);
    }

    @Override
    public boolean shouldDecode(final FacesContext context, final DataTable table) {
        return isSortRequest(context, table);
    }

    @Override
    public boolean shouldEncode(final FacesContext context, final DataTable table) {
        return isSortRequest(context, table);
    }

    public void sort(final FacesContext context,
                     final DataTable table,
                     final ValueExpression sortBy,
                     final SortOrder sortOrder,
                     final MethodExpression sortFunction) {
        final Object value = table.getValue();
        List list = null;

        if (value == null) {
            return;
        }

        if (value instanceof List) {
            list = (List) value;
        } else if (value instanceof ListDataModel) {
            list = (List) ((ListDataModel) value).getWrappedData();
        } else {
            throw new FacesException("Data type should be java.util.List or javax.faces.model.ListDataModel instance to be sortable.");
        }

        Collections.sort(list, new BeanPropertyComparator(sortBy, table.getVar(), sortOrder, sortFunction));
    }

    private void sortColumn(final FacesContext context, final DataTable table) {
        final UIColumn sortColumn = table.getSortColumn();
        final SortOrder sortOrder = SortOrder.valueOf(table.getSortOrder().toUpperCase(Locale.ENGLISH));

        if (sortColumn.isDynamic()) {
            ((DynamicColumn) sortColumn).applyStatelessModel();
        }

        sort(context, table, sortColumn.getValueExpression("sortBy"), sortOrder, sortColumn.getSortFunction());
    }
}