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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;
import org.primefaces.component.row.Row;
import org.primefaces.context.RequestContext;
import org.primefaces.model.filter.ContainsFilterConstraint;
import org.primefaces.model.filter.EndsWithFilterConstraint;
import org.primefaces.model.filter.ExactFilterConstraint;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.model.filter.StartsWithFilterConstraint;
import org.primefaces.util.ComponentUtils;

public class FilterFeature implements DataTableFeature {

    private final static Logger logger = Logger.getLogger(DataTable.class.getName());

    private final static String STARTS_WITH_MATCH_MODE = "startsWith";
    private final static String ENDS_WITH_MATCH_MODE = "endsWith";
    private final static String CONTAINS_MATCH_MODE = "contains";
    private final static String EXACT_MATCH_MODE = "exact";

    @Override
    public void decode(final FacesContext context, final DataTable table) {
        // reset state
        updateFilteredValue(context, table, null);
        table.setFirst(0);
        table.setRowIndex(-1);

        final String globalFilterParam = table.getClientId(context) + UINamingContainer.getSeparatorChar(context)
            + "globalFilter";
        final Map<String, UIColumn> columnFilterMap = populateColumnFilterMap(context, table);
        final Map<String, String> filterParameterMap = populateFilterParameterMap(context, table, columnFilterMap,
                                                                                  globalFilterParam);

        table.setFilters(filterParameterMap);

        if (!table.isLazy()) {
            filter(context, table, columnFilterMap, globalFilterParam);
        }
    }

    @Override
    public void encode(final FacesContext context, final DataTableRenderer renderer, final DataTable table)
        throws IOException {
        if (table.isLazy()) {
            table.loadLazyData();
        }

        renderer.encodeTbody(context, table, true);
    }

    protected void filter(final FacesContext context,
                          final DataTable table,
                          final Map<String, UIColumn> filterMap,
                          final String globalFilterParam) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final List filteredData = new ArrayList();
        final boolean hasGlobalFilter = params.containsKey(globalFilterParam);
        final String globalFilter = hasGlobalFilter ? params.get(globalFilterParam).toLowerCase() : null;

        for (int i = 0; i < table.getRowCount(); i++) {
            table.setRowIndex(i);
            boolean localMatch = true;
            boolean globalMatch = false;

            for (final String filterParamName : filterMap.keySet()) {
                final UIColumn column = filterMap.get(filterParamName);
                final String filterParamValue = params.containsKey(filterParamName) ? params.get(filterParamName)
                    .toLowerCase() : null;

                if (column instanceof DynamicColumn) {
                    ((DynamicColumn) column).applyModel();
                }

                final String columnValue = String.valueOf(column.getValueExpression("filterBy")
                    .getValue(context.getELContext()));
                final FilterConstraint filterConstraint = getFilterConstraint(column);

                if (hasGlobalFilter && !globalMatch) {
                    if (columnValue != null && columnValue.toLowerCase().contains(globalFilter)) globalMatch = true;
                }

                if (ComponentUtils.isValueBlank(filterParamValue)) {
                    localMatch = true;
                } else if (columnValue == null
                    || !filterConstraint.applies(columnValue.toLowerCase(), filterParamValue)) {
                    localMatch = false;
                    break;
                }
            }

            boolean matches = localMatch;
            if (hasGlobalFilter) {
                matches = localMatch && globalMatch;
            }

            if (matches) {
                filteredData.add(table.getRowData());
            }
        }

        // Metadata for callback
        if (table.isPaginator()) {
            final RequestContext requestContext = RequestContext.getCurrentInstance();

            if (requestContext != null) {
                requestContext.addCallbackParam("totalRecords", filteredData.size());
            }
        }

        // save filtered data
        updateFilteredValue(context, table, filteredData);

        table.setRowIndex(-1); // reset datamodel
    }

    private ColumnGroup getColumnGroup(final DataTable table, final String target) {
        for (final UIComponent child : table.getChildren()) {
            if (child instanceof ColumnGroup) {
                final ColumnGroup colGroup = (ColumnGroup) child;
                final String type = colGroup.getType();

                if (type != null && type.equals(target)) {
                    return colGroup;
                }

            }
        }

        return null;
    }

    public FilterConstraint getFilterConstraint(final UIColumn column) {
        final String filterMatchMode = column.getFilterMatchMode();
        FilterConstraint filterConstraint = null;

        if (filterMatchMode.equals(FilterFeature.STARTS_WITH_MATCH_MODE)) {
            filterConstraint = new StartsWithFilterConstraint();
        } else if (filterMatchMode.equals(FilterFeature.ENDS_WITH_MATCH_MODE)) {
            filterConstraint = new EndsWithFilterConstraint();
        } else if (filterMatchMode.equals(FilterFeature.CONTAINS_MATCH_MODE)) {
            filterConstraint = new ContainsFilterConstraint();
        } else if (filterMatchMode.equals(FilterFeature.EXACT_MATCH_MODE)) {
            filterConstraint = new ExactFilterConstraint();
        } else {
            throw new FacesException("Illegal filter match mode:" + filterMatchMode);
        }

        return filterConstraint;
    }

    private boolean isFilterRequest(final FacesContext context, final DataTable table) {
        return context.getExternalContext().getRequestParameterMap().containsKey(table.getClientId(context)
                                                                                     + "_filtering");
    }

    public Map<String, UIColumn> populateColumnFilterMap(final FacesContext context, final DataTable table) {
        final Map filterMap = new HashMap<String, UIColumn>();
        final String separator = String.valueOf(UINamingContainer.getSeparatorChar(context));

        final ColumnGroup group = getColumnGroup(table, "header");
        if (group != null) {
            for (final UIComponent child : group.getChildren()) {
                final Row headerRow = (Row) child;

                if (headerRow.isRendered()) {
                    for (final UIComponent headerRowChild : headerRow.getChildren()) {
                        final Column column = (Column) headerRowChild;

                        if (column.isRendered() && column.getValueExpression("filterBy") != null) {
                            final String filterId = column.getClientId(context) + separator + "filter";
                            filterMap.put(filterId, column);
                        }
                    }
                }
            }
        } else {

            for (final UIColumn column : table.getColumns()) {

                if (column.getValueExpression("filterBy") != null) {
                    if (column instanceof Column) {
                        final String filterId = column.getClientId(context) + separator + "filter";
                        filterMap.put(filterId, column);
                    } else if (column instanceof DynamicColumn) {
                        final DynamicColumn dynamicColumn = (DynamicColumn) column;
                        dynamicColumn.applyModel();

                        final String filterId = dynamicColumn.getContainerClientId(context) + separator + "filter";
                        filterMap.put(filterId, dynamicColumn);
                    }
                }
            }
        }

        return filterMap;
    }

    public Map<String, String> populateFilterParameterMap(final FacesContext context,
                                                          final DataTable table,
                                                          final Map<String, UIColumn> filterColumnMap,
                                                          final String globalFilterParam) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final Map<String, String> filterParameterMap = new HashMap<String, String>();

        for (final String filterName : filterColumnMap.keySet()) {
            final UIColumn column = filterColumnMap.get(filterName);
            final String filterValue = params.get(filterName);

            if (!ComponentUtils.isValueBlank(filterValue)) {
                String filterField = null;

                if (column instanceof DynamicColumn) {
                    ((DynamicColumn) column).applyModel();

                    filterField = table.resolveDynamicField(column.getValueExpression("filterBy"));
                } else {
                    filterField = table.resolveStaticField(column.getValueExpression("filterBy"));
                }

                filterParameterMap.put(filterField, filterValue);
            }
        }

        if (params.containsKey(globalFilterParam)) {
            filterParameterMap.put("globalFilter", params.get(globalFilterParam));
        }

        return filterParameterMap;
    }

    @Override
    public boolean shouldDecode(final FacesContext context, final DataTable table) {
        return isFilterRequest(context, table);
    }

    @Override
    public boolean shouldEncode(final FacesContext context, final DataTable table) {
        return isFilterRequest(context, table);
    }

    public void updateFilteredValue(final FacesContext context, final DataTable table, final List<?> value) {
        table.setSelectableDataModelWrapper(null);
        final ValueExpression ve = table.getValueExpression("filteredValue");

        if (ve != null) {
            ve.setValue(context.getELContext(), value);
        } else {
            if (value != null) {
                FilterFeature.logger
                    .log(Level.WARNING,
                         "DataTable {0} has filtering enabled but no filteredValue model reference is defined"
                             + ", for backward compatibility falling back to page viewstate method to keep filteredValue."
                             + " It is highly suggested to use filtering with a filteredValue model reference as viewstate method is deprecated and will be removed in future.",
                         new Object[] { table.getClientId(context) });

            }

            table.setFilteredValue(value);
        }
    }
}