/*
 * Generated, Do Not Modify
 */
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
package org.primefaces.component.datatable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.model.DataModel;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.api.UIData;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.feature.DataTableFeature;
import org.primefaces.component.datatable.feature.DataTableFeatureKey;
import org.primefaces.component.datatable.feature.DraggableColumnsFeature;
import org.primefaces.component.datatable.feature.FilterFeature;
import org.primefaces.component.datatable.feature.PageFeature;
import org.primefaces.component.datatable.feature.ResizableColumnsFeature;
import org.primefaces.component.datatable.feature.RowEditFeature;
import org.primefaces.component.datatable.feature.RowExpandFeature;
import org.primefaces.component.datatable.feature.ScrollFeature;
import org.primefaces.component.datatable.feature.SelectionFeature;
import org.primefaces.component.datatable.feature.SortFeature;
import org.primefaces.component.rowexpansion.RowExpansion;
import org.primefaces.component.subtable.SubTable;
import org.primefaces.component.summaryrow.SummaryRow;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.ColumnResizeEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.event.data.SortEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SelectableDataModelWrapper;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.Visibility;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class DataTable extends UIData implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.RTLAware, javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        scrollable,
        scrollHeight,
        scrollWidth,
        selectionMode,
        selection,
        emptyMessage,
        style,
        styleClass,
        liveScroll,
        rowStyleClass,
        onExpandStart,
        resizableColumns,
        sortBy,
        sortOrder,
        sortFunction,
        scrollRows,
        rowKey,
        filterEvent,
        filterDelay,
        tableStyle,
        tableStyleClass,
        draggableColumns,
        editable,
        filteredValue,
        sortMode,
        editMode,
        editingRow,
        cellSeparator,
        summary,
        frozenRows,
        dir,
        liveResize;

        String toString;

        PropertyKeys() {
        }

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    public static final String COMPONENT_TYPE = "org.primefaces.component.DataTable";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.DataTableRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private final static Logger logger = Logger.getLogger(DataTable.class.getName());

    public static final String CONTAINER_CLASS = "ui-datatable ui-widget";

    public static final String TABLE_WRAPPER_CLASS = "ui-datatable-tablewrapper";
    public static final String RTL_CLASS = "ui-datatable-rtl";

    public static final String COLUMN_HEADER_CLASS = "ui-state-default";
    public static final String DYNAMIC_COLUMN_HEADER_CLASS = "ui-dynamic-column";

    public static final String COLUMN_HEADER_CONTAINER_CLASS = "ui-header-column";
    public static final String COLUMN_FOOTER_CLASS = "ui-state-default";

    public static final String COLUMN_FOOTER_CONTAINER_CLASS = "ui-footer-column";
    public static final String DATA_CLASS = "ui-datatable-data ui-widget-content";

    public static final String ROW_CLASS = "ui-widget-content";
    public static final String EMPTY_MESSAGE_ROW_CLASS = "ui-widget-content ui-datatable-empty-message";

    public static final String HEADER_CLASS = "ui-datatable-header ui-widget-header ui-corner-top";
    public static final String FOOTER_CLASS = "ui-datatable-footer ui-widget-header ui-corner-bottom";

    public static final String SORTABLE_COLUMN_CLASS = "ui-sortable-column";
    public static final String SORTABLE_COLUMN_ICON_CLASS = "ui-sortable-column-icon ui-icon ui-icon-carat-2-n-s";

    public static final String SORTABLE_COLUMN_ASCENDING_ICON_CLASS = "ui-sortable-column-icon ui-icon ui-icon ui-icon-carat-2-n-s ui-icon-triangle-1-n";
    public static final String SORTABLE_COLUMN_DESCENDING_ICON_CLASS = "ui-sortable-column-icon ui-icon ui-icon ui-icon-carat-2-n-s ui-icon-triangle-1-s";

    public static final String FILTER_COLUMN_CLASS = "ui-filter-column";
    public static final String COLUMN_FILTER_CLASS = "ui-column-filter ui-widget ui-state-default ui-corner-left";

    public static final String COLUMN_INPUT_FILTER_CLASS = "ui-column-filter ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all";
    public static final String RESIZABLE_COLUMN_CLASS = "ui-resizable-column";

    public static final String EXPANDED_ROW_CLASS = "ui-expanded-row";
    public static final String EXPANDED_ROW_CONTENT_CLASS = "ui-expanded-row-content";

    public static final String ROW_TOGGLER_CLASS = "ui-row-toggler";
    public static final String EDITABLE_COLUMN_CLASS = "ui-editable-column";

    public static final String CELL_EDITOR_CLASS = "ui-cell-editor";
    public static final String CELL_EDITOR_INPUT_CLASS = "ui-cell-editor-input";

    public static final String CELL_EDITOR_OUTPUT_CLASS = "ui-cell-editor-output";
    public static final String ROW_EDITOR_COLUMN_CLASS = "ui-row-editor-column";

    public static final String ROW_EDITOR_CLASS = "ui-row-editor";
    public static final String SELECTION_COLUMN_CLASS = "ui-selection-column";

    public static final String EVEN_ROW_CLASS = "ui-datatable-even";
    public static final String ODD_ROW_CLASS = "ui-datatable-odd";

    public static final String SCROLLABLE_CONTAINER_CLASS = "ui-datatable-scrollable";
    public static final String SCROLLABLE_HEADER_CLASS = "ui-widget-header ui-datatable-scrollable-header";

    public static final String SCROLLABLE_HEADER_BOX_CLASS = "ui-datatable-scrollable-header-box";
    public static final String SCROLLABLE_BODY_CLASS = "ui-datatable-scrollable-body";

    public static final String SCROLLABLE_FOOTER_CLASS = "ui-widget-header ui-datatable-scrollable-footer";
    public static final String SCROLLABLE_FOOTER_BOX_CLASS = "ui-datatable-scrollable-footer-box";

    public static final String COLUMN_RESIZER_CLASS = "ui-column-resizer";
    public static final String RESIZABLE_CONTAINER_CLASS = "ui-datatable-resizable";

    public static final String SUBTABLE_HEADER = "ui-datatable-subtable-header";
    public static final String SUBTABLE_FOOTER = "ui-datatable-subtable-footer";

    public static final String SUMMARY_ROW_CLASS = "ui-datatable-summaryrow ui-widget-header";
    public static final String EDITING_ROW_CLASS = "ui-row-editing";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList("page", "sort", "filter", "rowSelect", "rowUnselect", "rowEdit", "rowEditInit", "rowEditCancel",
                "colResize", "toggleSelect", "colReorder", "contextMenu", "rowSelectRadio", "rowSelectCheckbox",
                "rowUnselectCheckbox", "rowDblselect", "rowToggle", "cellEdit"));
    static Map<DataTableFeatureKey, DataTableFeature> FEATURES;

    static {
        DataTable.FEATURES = new HashMap<DataTableFeatureKey, DataTableFeature>();
        DataTable.FEATURES.put(DataTableFeatureKey.DRAGGABLE_COLUMNS, new DraggableColumnsFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.FILTER, new FilterFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.PAGE, new PageFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.SORT, new SortFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.RESIZABLE_COLUMNS, new ResizableColumnsFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.SELECT, new SelectionFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.ROW_EDIT, new RowEditFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.ROW_EXPAND, new RowExpandFeature());
        DataTable.FEATURES.put(DataTableFeatureKey.SCROLL, new ScrollFeature());
    }
    private boolean reset = false;

    private SelectableDataModelWrapper selectableDataModelWrapper = null;
    private List<Object> selectedRowKeys = new ArrayList<Object>();

    private int columnsCount = -1;
    private List<UIColumn> columns;

    private UIColumn sortColumn;
    private List<SortMeta> multiSortMeta;

    public DataTable() {
        setRendererType(DataTable.DEFAULT_RENDERER);
    }

    void addToSelectedRowKeys(final Object object,
                              final Map<String, Object> map,
                              final String var,
                              final boolean hasRowKey) {
        if (hasRowKey) {
            map.put(var, object);
            selectedRowKeys.add(getRowKey());
        } else {
            selectedRowKeys.add(getRowKeyFromModel(object));
        }
    }

    public void clearLazyCache() {
        final LazyDataModel model = (LazyDataModel) getDataModel();
        model.setWrappedData(null);
    }

    protected SortOrder convertSortOrder() {
        final String sortOrder = getSortOrder();

        if (sortOrder == null)
            return SortOrder.UNSORTED;
        else return SortOrder.valueOf(sortOrder.toUpperCase(Locale.ENGLISH));
    }

    public void enableFiltering() {
        getStateHelper().put("filtering", true);
    }

    public UIColumn findColumn(final String clientId) {
        for (final UIColumn column : getColumns()) {
            if (column.getColumnKey().equals(clientId)) {
                return column;
            }
        }

        final FacesContext context = getFacesContext();
        final ColumnGroup headerGroup = getColumnGroup("header");
        for (final UIComponent row : headerGroup.getChildren()) {
            for (final UIComponent col : row.getChildren()) {
                if (col.getClientId(context).equals(clientId)) {
                    return (UIColumn) col;
                }
            }
        }

        throw new FacesException("Cannot find column with key: " + clientId);
    }

    void findSelectedRowKeys() {
        final Object selection = getSelection();
        selectedRowKeys = new ArrayList<Object>();
        final boolean hasRowKeyVe = getValueExpression("rowKey") != null;
        final String var = getVar();
        final Map<String, Object> requestMap = getFacesContext().getExternalContext().getRequestMap();

        if (isSelectionEnabled() && selection != null) {
            if (isSingleSelectionMode()) {
                addToSelectedRowKeys(selection, requestMap, var, hasRowKeyVe);
            } else {
                if (selection.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(selection); i++) {
                        addToSelectedRowKeys(Array.get(selection, i), requestMap, var, hasRowKeyVe);
                    }
                } else {
                    final List<?> list = (List<?>) selection;

                    for (final Object object : list) {
                        addToSelectedRowKeys(object, requestMap, var, hasRowKeyVe);
                    }
                }

            }
        }
    }

    public java.lang.String getCellSeparator() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.cellSeparator, null);
    }

    public ColumnGroup getColumnGroup(final String target) {
        for (final UIComponent child : getChildren()) {
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

    public List<UIColumn> getColumns() {
        if (columns == null) {
            columns = new ArrayList<UIColumn>();
            final FacesContext context = getFacesContext();
            final char separator = UINamingContainer.getSeparatorChar(context);

            for (final UIComponent child : getChildren()) {
                if (child instanceof Column) {
                    columns.add((UIColumn) child);
                } else if (child instanceof Columns) {
                    final Columns uiColumns = (Columns) child;
                    final String uiColumnsClientId = uiColumns.getClientId(context);

                    for (int i = 0; i < uiColumns.getRowCount(); i++) {
                        final DynamicColumn dynaColumn = new DynamicColumn(i, uiColumns);
                        dynaColumn.setColumnKey(uiColumnsClientId + separator + i);
                        columns.add(dynaColumn);
                    }
                }
            }
        }

        return columns;
    }

    public int getColumnsCount() {
        if (columnsCount == -1) {
            columnsCount = 0;

            for (final UIComponent kid : getChildren()) {
                if (kid.isRendered()) {
                    if (kid instanceof Columns) {
                        final Columns uicolumns = (Columns) kid;
                        final Collection collection = (Collection) uicolumns.getValue();
                        if (collection != null) {
                            columnsCount += collection.size();
                        }
                    } else if (kid instanceof Column) {
                        columnsCount++;
                    } else if (kid instanceof SubTable) {
                        final SubTable subTable = (SubTable) kid;
                        for (final UIComponent subTableKid : subTable.getChildren()) {
                            if (subTableKid.isRendered() && subTableKid instanceof Column) {
                                columnsCount++;
                            }
                        }
                    }
                }
            }
        }

        return columnsCount;
    }

    public String getColumnSelectionMode() {
        for (final UIComponent child : getChildren()) {
            if (child.isRendered() && (child instanceof Column)) {
                final String selectionMode = ((Column) child).getSelectionMode();

                if (selectionMode != null) {
                    return selectionMode;
                }
            }
        }

        return null;
    }

    public java.lang.String getDir() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dir, "ltr");
    }

    public java.lang.String getEditMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.editMode, "row");
    }

    public java.lang.String getEmptyMessage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.emptyMessage, "No records found.");
    }

    @Override
    public Collection<String> getEventNames() {
        return DataTable.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return DataTable.COMPONENT_FAMILY;
    }

    public int getFilterDelay() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.filterDelay, java.lang.Integer.MAX_VALUE);
    }

    public java.util.List getFilteredValue() {
        return (java.util.List) getStateHelper().eval(PropertyKeys.filteredValue, null);
    }

    public java.lang.String getFilterEvent() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterEvent, null);
    }

    public Map<String, String> getFilters() {
        return (Map<String, String>) getStateHelper().eval("filters", new HashMap<String, String>());
    }

    public java.util.Collection getFrozenRows() {
        return (java.util.Collection) getStateHelper().eval(PropertyKeys.frozenRows, null);
    }

    public Object getLocalSelection() {
        return getStateHelper().get(PropertyKeys.selection);
    }

    public List<SortMeta> getMultiSortMeta() {
        if (multiSortMeta == null) {
            final ValueExpression ve = getValueExpression("sortBy");
            if (ve != null) {
                multiSortMeta = (List<SortMeta>) ve.getValue(getFacesContext().getELContext());
            }
        }

        return multiSortMeta;
    }

    public java.lang.String getOnExpandStart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onExpandStart, null);
    }

    public Object getRowData(final String rowKey) {
        final boolean hasRowKeyVe = getValueExpression("rowKey") != null;

        if (hasRowKeyVe) {
            final Map<String, Object> requestMap = getFacesContext().getExternalContext().getRequestMap();
            final String var = getVar();
            final Collection data = (Collection) getDataModel().getWrappedData();

            for (final Iterator it = data.iterator(); it.hasNext();) {
                final Object object = it.next();
                requestMap.put(var, object);

                if (String.valueOf(getRowKey()).equals(rowKey)) {
                    return object;
                }
            }

            return null;
        } else {
            final DataModel model = getDataModel();
            if (!(model instanceof SelectableDataModel)) {
                throw new FacesException("DataModel must implement org.primefaces.model.SelectableDataModel when selection is enabled or you need to define rowKey attribute");
            }

            return ((SelectableDataModel) getDataModel()).getRowData(rowKey);
        }
    }

    public RowExpansion getRowExpansion() {
        for (final UIComponent kid : getChildren()) {
            if (kid instanceof RowExpansion) return (RowExpansion) kid;
        }

        return null;
    }

    public java.lang.Object getRowKey() {
        return getStateHelper().eval(PropertyKeys.rowKey, null);
    }

    public Object getRowKeyFromModel(final Object object) {
        final DataModel model = getDataModel();
        if (!(model instanceof SelectableDataModel)) {
            throw new FacesException("DataModel must implement org.primefaces.model.SelectableDataModel when selection is enabled.");
        }

        return ((SelectableDataModel) getDataModel()).getRowKey(object);
    }

    public java.lang.String getRowStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.rowStyleClass, null);
    }

    public java.lang.String getScrollHeight() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.scrollHeight, null);
    }

    public int getScrollRows() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.scrollRows, 0);
    }

    public String getScrollState() {
        final Map<String, String> params = getFacesContext().getExternalContext().getRequestParameterMap();
        final String name = this.getClientId() + "_scrollState";
        final String value = params.get(name);

        return value == null ? "0,0" : value;
    }

    public java.lang.String getScrollWidth() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.scrollWidth, null);
    }

    List<Object> getSelectedRowKeys() {
        return selectedRowKeys;
    }

    String getSelectedRowKeysAsString() {
        final StringBuilder builder = new StringBuilder();
        for (final Iterator<Object> iter = getSelectedRowKeys().iterator(); iter.hasNext();) {
            builder.append(iter.next());

            if (iter.hasNext()) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public java.lang.Object getSelection() {
        return getStateHelper().eval(PropertyKeys.selection, null);
    }

    public java.lang.String getSelectionMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.selectionMode, null);
    }

    public java.lang.Object getSortBy() {
        return getStateHelper().eval(PropertyKeys.sortBy, null);
    }

    public UIColumn getSortColumn() {
        return sortColumn;
    }

    public javax.el.MethodExpression getSortFunction() {
        return (javax.el.MethodExpression) getStateHelper().eval(PropertyKeys.sortFunction, null);
    }

    public java.lang.String getSortMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.sortMode, "single");
    }

    public java.lang.String getSortOrder() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.sortOrder, "ascending");
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public SubTable getSubTable() {
        for (final UIComponent kid : getChildren()) {
            if (kid instanceof SubTable) return (SubTable) kid;
        }

        return null;
    }

    public java.lang.String getSummary() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.summary, null);
    }

    public SummaryRow getSummaryRow() {
        for (final UIComponent kid : getChildren()) {
            if (kid.isRendered() && kid instanceof SummaryRow) {
                return (SummaryRow) kid;
            }
        }

        return null;
    }

    public java.lang.String getTableStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.tableStyle, null);
    }

    public java.lang.String getTableStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.tableStyleClass, null);
    }

    /**
     * Override to support filtering, we return the filtered subset in getValue
     * instead of actual data. In case selectableDataModel is bound, we wrap it
     * with filtered data.
     * 
     */
    @Override
    public Object getValue() {
        final Object value = super.getValue();
        final List<?> filteredValue = getFilteredValue();

        if (filteredValue != null) {
            if (value instanceof SelectableDataModel) {
                return selectableDataModelWrapper == null
                                                         ? (selectableDataModelWrapper = new SelectableDataModelWrapper((SelectableDataModel) value,
                                                                                                                        filteredValue))
                                                         : selectableDataModelWrapper;
            } else {
                return filteredValue;
            }
        } else {
            return value;
        }
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(DataTable.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                final ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                } else if (!setAttributes.contains(name)) {
                    setAttributes.add(name);
                }
            }
        }
    }

    public boolean hasFooterColumn() {
        for (final UIComponent child : getChildren()) {
            if (child.isRendered() && (child instanceof UIColumn)) {
                final UIColumn column = (UIColumn) child;

                if (column.getFacet("footer") != null || column.getFooterText() != null) return true;
            }

        }

        return false;
    }

    public boolean isBodyUpdate(final FacesContext context) {
        final String clientId = this.getClientId(context);

        return context.getExternalContext().getRequestParameterMap().containsKey(clientId + "_updateBody");
    }

    public boolean isColumnSelectionEnabled() {
        return getColumnSelectionMode() != null;
    }

    public boolean isDefaultSorted() {
        final Object value = getStateHelper().get("defaultSorted");

        return value == null ? false : true;
    }

    public boolean isDraggableColumns() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.draggableColumns, false);
    }

    public boolean isEditable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.editable, false);
    }

    public boolean isEditingRow() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.editingRow, false);
    }

    public boolean isFilteringEnabled() {
        final Object value = getStateHelper().get("filtering");

        return value == null ? false : true;
    }

    public boolean isLiveResize() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.liveResize, false);
    }

    public boolean isLiveScroll() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.liveScroll, false);
    }

    public boolean isMultiSort() {
        final String sortMode = getSortMode();

        return (sortMode != null && sortMode.equals("multiple"));
    }

    public boolean isRequestSource(final FacesContext context) {
        final String partialSource = context.getExternalContext().getRequestParameterMap()
            .get(Constants.PARTIAL_SOURCE_PARAM);

        return partialSource != null && this.getClientId(context).equals(partialSource);
    }

    public boolean isReset() {
        return reset;
    }

    public boolean isResizableColumns() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resizableColumns, false);
    }

    public boolean isRowEditCancelRequest(final FacesContext context) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String value = params.get(this.getClientId(context) + "_rowEditAction");

        return value != null && value.equals("cancel");
    }

    public boolean isRowEditRequest(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context)
                                                                                     + "_rowEditAction");
    }

    public boolean isRowSelectionEnabled() {
        return getSelectionMode() != null;
    }

    @Override
    public boolean isRTL() {
        return getDir().equalsIgnoreCase("rtl");
    }

    public boolean isScrollable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.scrollable, false);
    }

    public boolean isSelectionEnabled() {
        return isRowSelectionEnabled() || isColumnSelectionEnabled();
    }

    public boolean isSingleSelectionMode() {
        final String selectionMode = getSelectionMode();
        final String columnSelectionMode = getColumnSelectionMode();

        if (selectionMode != null)
            return selectionMode.equalsIgnoreCase("single");
        else if (columnSelectionMode != null)
            return columnSelectionMode.equalsIgnoreCase("single");
        else return false;
    }

    public void loadLazyData() {
        final DataModel model = getDataModel();

        if (model != null && model instanceof LazyDataModel) {
            final LazyDataModel lazyModel = (LazyDataModel) model;

            List<?> data = null;

            if (isMultiSort()) {
                data = lazyModel.load(getFirst(), getRows(), getMultiSortMeta(), getFilters());
            } else {
                data = lazyModel.load(getFirst(), getRows(), resolveSortField(), convertSortOrder(), getFilters());
            }

            lazyModel.setPageSize(getRows());
            lazyModel.setWrappedData(data);

            // Update paginator for callback
            if (isPaginator()) {
                final RequestContext requestContext = RequestContext.getCurrentInstance();

                if (requestContext != null) {
                    requestContext.addCallbackParam("totalRecords", lazyModel.getRowCount());
                }
            }
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        super.processUpdates(context);

        final ValueExpression selectionVE = getValueExpression("selection");

        if (selectionVE != null) {
            selectionVE.setValue(context.getELContext(), getLocalSelection());

            setSelection(null);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (isRequestSource(context) && event instanceof AjaxBehaviorEvent) {
            setRowIndex(-1);
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(context);
            FacesEvent wrapperEvent = null;

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("rowSelect") || eventName.equals("rowSelectRadio") || eventName.equals("contextMenu")
                || eventName.equals("rowSelectCheckbox") || eventName.equals("rowDblselect")) {
                final String rowKey = params.get(clientId + "_instantSelectedRowKey");
                wrapperEvent = new SelectEvent(this, behaviorEvent.getBehavior(), this.getRowData(rowKey));
            } else if (eventName.equals("rowUnselect") || eventName.equals("rowUnselectCheckbox")) {
                final String rowKey = params.get(clientId + "_instantUnselectedRowKey");
                wrapperEvent = new UnselectEvent(this, behaviorEvent.getBehavior(), this.getRowData(rowKey));
            } else if (eventName.equals("page")) {
                final int rows = getRowsToRender();
                final int first = Integer.parseInt(params.get(clientId + "_first"));
                final int page = rows > 0 ? (int) (first / rows) : 0;

                wrapperEvent = new PageEvent(this, behaviorEvent.getBehavior(), page);
            } else if (eventName.equals("sort")) {
                SortOrder order;
                UIColumn sortColumn;

                if (isMultiSort()) {
                    final String[] sortDirs = params.get(clientId + "_sortDir").split(",");
                    final String[] sortKeys = params.get(clientId + "_sortKey").split(",");

                    order = SortOrder.valueOf(sortDirs[sortDirs.length - 1]);
                    sortColumn = findColumn(sortKeys[sortKeys.length - 1]);
                } else {
                    order = SortOrder.valueOf(params.get(clientId + "_sortDir"));
                    sortColumn = findColumn(params.get(clientId + "_sortKey"));
                }

                wrapperEvent = new SortEvent(this, behaviorEvent.getBehavior(), sortColumn, order);
            } else if (eventName.equals("filter")) {
                wrapperEvent = new FilterEvent(this, behaviorEvent.getBehavior(), getFilteredValue(), getFilters());
            } else if (eventName.equals("rowEdit") || eventName.equals("rowEditCancel")
                || eventName.equals("rowEditInit")) {
                final int rowIndex = Integer.parseInt(params.get(clientId + "_rowEditIndex"));
                setRowIndex(rowIndex);
                wrapperEvent = new RowEditEvent(this, behaviorEvent.getBehavior(), this.getRowData());
            } else if (eventName.equals("colResize")) {
                final String columnId = params.get(clientId + "_columnId");
                final int width = Integer.parseInt(params.get(clientId + "_width"));
                final int height = Integer.parseInt(params.get(clientId + "_height"));

                wrapperEvent = new ColumnResizeEvent(this,
                                                     behaviorEvent.getBehavior(),
                                                     width,
                                                     height,
                                                     findColumn(columnId));
            } else if (eventName.equals("toggleSelect")) {
                final boolean checked = Boolean.valueOf(params.get(clientId + "_checked"));

                wrapperEvent = new ToggleSelectEvent(this, behaviorEvent.getBehavior(), checked);
            } else if (eventName.equals("colReorder")) {
                wrapperEvent = behaviorEvent;
            } else if (eventName.equals("rowToggle")) {
                final boolean expansion = params.containsKey(clientId + "_rowExpansion");
                final Visibility visibility = expansion ? Visibility.VISIBLE : Visibility.HIDDEN;
                final String rowIndex = expansion ? params.get(clientId + "_expandedRowIndex") : params.get(clientId
                    + "_collapsedRowIndex");
                setRowIndex(Integer.parseInt(rowIndex));

                wrapperEvent = new ToggleEvent(this, behaviorEvent.getBehavior(), visibility, getRowData());
            } else if (eventName.equals("cellEdit")) {
                final String[] cellInfo = params.get(clientId + "_cellInfo").split(",");
                final int rowIndex = Integer.parseInt(cellInfo[0]);
                final int cellIndex = Integer.parseInt(cellInfo[1]);
                int i = -1;
                UIColumn column = null;

                for (final UIColumn col : getColumns()) {
                    if (col.isRendered()) {
                        i++;

                        if (i == cellIndex) {
                            column = col;
                            break;
                        }
                    }
                }

                wrapperEvent = new CellEditEvent(this, behaviorEvent.getBehavior(), rowIndex, column);
            }

            wrapperEvent.setPhaseId(event.getPhaseId());

            super.queueEvent(wrapperEvent);
        } else {
            super.queueEvent(event);
        }
    }

    @Override
    protected boolean requiresColumns() {
        return true;
    }

    public void reset() {
        resetValue();
        setFirst(0);
        reset = true;
    }

    public void resetValue() {
        setValue(null);
        setFilteredValue(null);
        setFilters(null);
    }

    public String resolveDynamicField(final ValueExpression expression) {
        if (expression != null) {
            String expressionString = expression.getExpressionString();
            expressionString = expressionString.substring(expressionString.indexOf("[") + 1, expressionString
                .indexOf("]"));
            expressionString = "#{" + expressionString + "}";

            final FacesContext context = getFacesContext();
            final ELContext eLContext = context.getELContext();
            final ValueExpression dynaVE = context.getApplication().getExpressionFactory()
                .createValueExpression(eLContext, expressionString, String.class);

            return (String) dynaVE.getValue(eLContext);
        } else {
            return null;
        }
    }

    public String resolveSelectionMode() {
        final String tableSelectionMode = getSelectionMode();
        final String columnSelectionMode = getColumnSelectionMode();
        String selectionMode = null;

        if (tableSelectionMode != null)
            selectionMode = tableSelectionMode;
        else if (columnSelectionMode != null)
            selectionMode = columnSelectionMode.equals("single") ? "radio" : "checkbox";

        return selectionMode;
    }

    protected String resolveSortField() {
        final UIColumn column = getSortColumn();
        String sortField = null;
        final ValueExpression sortVE = getValueExpression("sortBy");

        if (column == null) {
            sortField = resolveStaticField(sortVE);
        } else {
            if (column.isDynamic()) {
                ((DynamicColumn) column).applyStatelessModel();
                sortField = resolveDynamicField(sortVE);
            } else {
                sortField = resolveStaticField(sortVE);
            }

        }

        return sortField;
    }

    public String resolveStaticField(final ValueExpression expression) {
        if (expression != null) {
            String expressionString = expression.getExpressionString();
            expressionString = expressionString.substring(2, expressionString.length() - 1); // Remove
                                                                                             // #{}

            return expressionString.substring(expressionString.indexOf(".") + 1); // Remove
                                                                                  // var
        } else {
            return null;
        }
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setCellSeparator(final java.lang.String _cellSeparator) {
        getStateHelper().put(PropertyKeys.cellSeparator, _cellSeparator);
        handleAttribute("cellSeparator", _cellSeparator);
    }

    public void setColumns(final List<UIColumn> columns) {
        this.columns = columns;
    }

    public void setDefaultSorted() {
        getStateHelper().put("defaultSorted", true);
    }

    public void setDir(final java.lang.String _dir) {
        getStateHelper().put(PropertyKeys.dir, _dir);
        handleAttribute("dir", _dir);
    }

    public void setDraggableColumns(final boolean _draggableColumns) {
        getStateHelper().put(PropertyKeys.draggableColumns, _draggableColumns);
        handleAttribute("draggableColumns", _draggableColumns);
    }

    public void setEditable(final boolean _editable) {
        getStateHelper().put(PropertyKeys.editable, _editable);
        handleAttribute("editable", _editable);
    }

    public void setEditingRow(final boolean _editingRow) {
        getStateHelper().put(PropertyKeys.editingRow, _editingRow);
        handleAttribute("editingRow", _editingRow);
    }

    public void setEditMode(final java.lang.String _editMode) {
        getStateHelper().put(PropertyKeys.editMode, _editMode);
        handleAttribute("editMode", _editMode);
    }

    public void setEmptyMessage(final java.lang.String _emptyMessage) {
        getStateHelper().put(PropertyKeys.emptyMessage, _emptyMessage);
        handleAttribute("emptyMessage", _emptyMessage);
    }

    public void setFilterDelay(final int _filterDelay) {
        getStateHelper().put(PropertyKeys.filterDelay, _filterDelay);
        handleAttribute("filterDelay", _filterDelay);
    }

    public void setFilteredValue(final java.util.List _filteredValue) {
        getStateHelper().put(PropertyKeys.filteredValue, _filteredValue);
        handleAttribute("filteredValue", _filteredValue);
    }

    public void setFilterEvent(final java.lang.String _filterEvent) {
        getStateHelper().put(PropertyKeys.filterEvent, _filterEvent);
        handleAttribute("filterEvent", _filterEvent);
    }

    public void setFilters(final Map<String, String> filters) {
        getStateHelper().put("filters", filters);
    }

    public void setFrozenRows(final java.util.Collection _frozenRows) {
        getStateHelper().put(PropertyKeys.frozenRows, _frozenRows);
        handleAttribute("frozenRows", _frozenRows);
    }

    public void setLiveResize(final boolean _liveResize) {
        getStateHelper().put(PropertyKeys.liveResize, _liveResize);
        handleAttribute("liveResize", _liveResize);
    }

    public void setLiveScroll(final boolean _liveScroll) {
        getStateHelper().put(PropertyKeys.liveScroll, _liveScroll);
        handleAttribute("liveScroll", _liveScroll);
    }

    public void setMultiSortMeta(final List<SortMeta> value) {
        multiSortMeta = value;
    }

    public void setOnExpandStart(final java.lang.String _onExpandStart) {
        getStateHelper().put(PropertyKeys.onExpandStart, _onExpandStart);
        handleAttribute("onExpandStart", _onExpandStart);
    }

    public void setResizableColumns(final boolean _resizableColumns) {
        getStateHelper().put(PropertyKeys.resizableColumns, _resizableColumns);
        handleAttribute("resizableColumns", _resizableColumns);
    }

    public void setRowKey(final java.lang.Object _rowKey) {
        getStateHelper().put(PropertyKeys.rowKey, _rowKey);
        handleAttribute("rowKey", _rowKey);
    }

    public void setRowStyleClass(final java.lang.String _rowStyleClass) {
        getStateHelper().put(PropertyKeys.rowStyleClass, _rowStyleClass);
        handleAttribute("rowStyleClass", _rowStyleClass);
    }

    public void setScrollable(final boolean _scrollable) {
        getStateHelper().put(PropertyKeys.scrollable, _scrollable);
        handleAttribute("scrollable", _scrollable);
    }

    public void setScrollHeight(final java.lang.String _scrollHeight) {
        getStateHelper().put(PropertyKeys.scrollHeight, _scrollHeight);
        handleAttribute("scrollHeight", _scrollHeight);
    }

    public void setScrollRows(final int _scrollRows) {
        getStateHelper().put(PropertyKeys.scrollRows, _scrollRows);
        handleAttribute("scrollRows", _scrollRows);
    }

    public void setScrollWidth(final java.lang.String _scrollWidth) {
        getStateHelper().put(PropertyKeys.scrollWidth, _scrollWidth);
        handleAttribute("scrollWidth", _scrollWidth);
    }

    public void setSelectableDataModelWrapper(final SelectableDataModelWrapper wrapper) {
        selectableDataModelWrapper = wrapper;
    }

    public void setSelection(final java.lang.Object _selection) {
        getStateHelper().put(PropertyKeys.selection, _selection);
        handleAttribute("selection", _selection);
    }

    public void setSelectionMode(final java.lang.String _selectionMode) {
        getStateHelper().put(PropertyKeys.selectionMode, _selectionMode);
        handleAttribute("selectionMode", _selectionMode);
    }

    public void setSortBy(final java.lang.Object _sortBy) {
        getStateHelper().put(PropertyKeys.sortBy, _sortBy);
        handleAttribute("sortBy", _sortBy);
    }

    public void setSortColumn(final UIColumn column) {
        sortColumn = column;
    }

    public void setSortFunction(final javax.el.MethodExpression _sortFunction) {
        getStateHelper().put(PropertyKeys.sortFunction, _sortFunction);
        handleAttribute("sortFunction", _sortFunction);
    }

    public void setSortMode(final java.lang.String _sortMode) {
        getStateHelper().put(PropertyKeys.sortMode, _sortMode);
        handleAttribute("sortMode", _sortMode);
    }

    public void setSortOrder(final java.lang.String _sortOrder) {
        getStateHelper().put(PropertyKeys.sortOrder, _sortOrder);
        handleAttribute("sortOrder", _sortOrder);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setSummary(final java.lang.String _summary) {
        getStateHelper().put(PropertyKeys.summary, _summary);
        handleAttribute("summary", _summary);
    }

    public void setTableStyle(final java.lang.String _tableStyle) {
        getStateHelper().put(PropertyKeys.tableStyle, _tableStyle);
        handleAttribute("tableStyle", _tableStyle);
    }

    public void setTableStyleClass(final java.lang.String _tableStyleClass) {
        getStateHelper().put(PropertyKeys.tableStyleClass, _tableStyleClass);
        handleAttribute("tableStyleClass", _tableStyleClass);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }

    public boolean shouldEncodeFeature(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context)
                                                                                     + "_encodeFeature");
    }

    @Override
    protected boolean shouldProcessChildren(final FacesContext context) {
        return !context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context)
                                                                                      + "_skipChildren");
    }
}