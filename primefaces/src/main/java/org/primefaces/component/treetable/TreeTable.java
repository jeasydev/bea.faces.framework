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
package org.primefaces.component.treetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import org.primefaces.component.api.UITree;
import org.primefaces.component.column.Column;
import org.primefaces.event.ColumnResizeEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class TreeTable extends UITree implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        style,
        styleClass,
        selection,
        selectionMode,
        scrollable,
        scrollHeight,
        scrollWidth,
        tableStyle,
        tableStyleClass,
        resizableColumns,
        rowStyleClass,
        liveResize,
        propagateSelectionUp,
        propagateSelectionDown;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.TreeTable";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.TreeTableRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String CONTAINER_CLASS = "ui-treetable ui-widget";

    public final static String RESIZABLE_CONTAINER_CLASS = "ui-treetable ui-treetable-resizable ui-widget";

    public final static String HEADER_CLASS = "ui-treetable-header ui-widget-header ui-corner-top";
    public final static String DATA_CLASS = "ui-treetable-data ui-widget-content";

    public final static String FOOTER_CLASS = "ui-treetable-footer ui-widget-header ui-corner-bottom";
    public final static String COLUMN_HEADER_CLASS = "ui-state-default";

    public final static String ROW_CLASS = "ui-widget-content";
    public final static String SELECTED_ROW_CLASS = "ui-widget-content ui-state-highlight ui-selected";

    public final static String COLUMN_CONTENT_WRAPPER = "ui-tt-c";
    public final static String EXPAND_ICON = "ui-treetable-toggler ui-icon ui-icon-triangle-1-e ui-c";

    public final static String COLLAPSE_ICON = "ui-treetable-toggler ui-icon ui-icon-triangle-1-s ui-c";
    public static final String SCROLLABLE_CONTAINER_CLASS = "ui-treetable-scrollable";

    public static final String SCROLLABLE_HEADER_CLASS = "ui-widget-header ui-treetable-scrollable-header";
    public static final String SCROLLABLE_HEADER_BOX_CLASS = "ui-treetable-scrollable-header-box";

    public static final String SCROLLABLE_BODY_CLASS = "ui-treetable-scrollable-body";
    public static final String SCROLLABLE_FOOTER_CLASS = "ui-widget-header ui-treetable-scrollable-footer";

    public static final String SCROLLABLE_FOOTER_BOX_CLASS = "ui-treetable-scrollable-footer-box";
    public static final String SELECTABLE_NODE_CLASS = "ui-treetable-selectable-node";

    public static final String RESIZABLE_COLUMN_CLASS = "ui-resizable-column";
    public static final String INDENT_CLASS = "ui-treetable-indent";

    private static final Collection<String> EVENT_NAMES = Collections
        .unmodifiableCollection(Arrays.asList("select", "unselect", "expand", "collapse", "colResize"));
    private final List<String> selectedRowKeys = new ArrayList<String>();

    private int columnsCount = -1;

    public TreeTable() {
        setRendererType(TreeTable.DEFAULT_RENDERER);
    }

    public Column findColumn(final String clientId) {
        for (final UIComponent child : getChildren()) {
            if (child instanceof Column && child.getClientId().equals(clientId)) {
                return (Column) child;
            }
        }

        return null;
    }

    public int getColumnsCount() {
        if (columnsCount == -1) {
            columnsCount = 0;

            for (final UIComponent kid : getChildren()) {
                if (kid.isRendered() && kid instanceof Column) {
                    columnsCount++;
                }
            }
        }

        return columnsCount;
    }

    @Override
    public Collection<String> getEventNames() {
        return TreeTable.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return TreeTable.COMPONENT_FAMILY;
    }

    public Object getLocalSelectedNodes() {
        return getStateHelper().get(PropertyKeys.selection);
    }

    public java.lang.String getRowStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.rowStyleClass, null);
    }

    public java.lang.String getScrollHeight() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.scrollHeight, null);
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

    public List<String> getSelectedRowKeys() {
        return selectedRowKeys;
    }

    public String getSelectedRowKeysAsString() {
        final StringBuilder builder = new StringBuilder();

        for (final Iterator<String> iter = selectedRowKeys.iterator(); iter.hasNext();) {
            builder.append(iter.next());

            if (iter.hasNext()) {
                builder.append(',');
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

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getTableStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.tableStyle, null);
    }

    public java.lang.String getTableStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.tableStyleClass, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(TreeTable.OPTIMIZED_PACKAGE)) {
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
            if (child instanceof Column && child.isRendered()) {
                final Column column = (Column) child;

                if (column.getFacet("footer") != null || column.getFooterText() != null) return true;
            }
        }

        return false;
    }

    public boolean isLiveResize() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.liveResize, false);
    }

    public boolean isPropagateSelectionDown() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.propagateSelectionDown, true);
    }

    public boolean isPropagateSelectionUp() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.propagateSelectionUp, true);
    }

    private boolean isRequestSource(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isResizableColumns() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resizableColumns, false);
    }

    public boolean isResizeRequest(final FacesContext context) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(context);

        return params.get(clientId + "_colResize") != null;
    }

    public boolean isScrollable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.scrollable, false);
    }

    private boolean isToggleRequest(final FacesContext context) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(context);

        return params.get(clientId + "_expand") != null || params.get(clientId + "_collapse") != null;
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (isToggleRequest(context)) {
            decode(context);
        } else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        super.processUpdates(context);
        final String selectionMode = getSelectionMode();

        if (selectionMode != null) {
            final Object selection = getLocalSelectedNodes();
            final Object previousSelection = getValueExpression("selection").getValue(context.getELContext());

            if (selectionMode.equals("single")) {
                if (previousSelection != null) ((TreeNode) previousSelection).setSelected(false);
                if (selection != null) ((TreeNode) selection).setSelected(true);
            } else {
                final TreeNode[] previousSelections = (TreeNode[]) previousSelection;
                final TreeNode[] selections = (TreeNode[]) selection;

                if (previousSelections != null) {
                    for (final TreeNode node : previousSelections)
                        node.setSelected(false);
                }

                if (selections != null) {
                    for (final TreeNode node : selections)
                        node.setSelected(true);
                }
            }

            getValueExpression("selection").setValue(context.getELContext(), selection);
            setSelection(null);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (isRequestSource(context)) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(context);
            FacesEvent wrapperEvent = null;

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("expand")) {
                final String nodeKey = params.get(clientId + "_expand");
                setRowKey(nodeKey);
                final TreeNode node = getRowNode();

                wrapperEvent = new NodeExpandEvent(this, behaviorEvent.getBehavior(), node);
                wrapperEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else if (eventName.equals("collapse")) {
                final String nodeKey = params.get(clientId + "_collapse");
                setRowKey(nodeKey);
                final TreeNode node = getRowNode();

                wrapperEvent = new NodeCollapseEvent(this, behaviorEvent.getBehavior(), node);
                wrapperEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else if (eventName.equals("select")) {
                final String nodeKey = params.get(clientId + "_instantSelect");
                setRowKey(nodeKey);
                final TreeNode node = getRowNode();

                wrapperEvent = new NodeSelectEvent(this, behaviorEvent.getBehavior(), node);
                wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());
            } else if (eventName.equals("unselect")) {
                final String nodeKey = params.get(clientId + "_instantUnselect");
                setRowKey(nodeKey);
                final TreeNode node = getRowNode();

                wrapperEvent = new NodeUnselectEvent(this, behaviorEvent.getBehavior(), node);
                wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());
            } else if (eventName.equals("colResize")) {
                final String columnId = params.get(clientId + "_columnId");
                final int width = Integer.parseInt(params.get(clientId + "_width"));
                final int height = Integer.parseInt(params.get(clientId + "_height"));

                wrapperEvent = new ColumnResizeEvent(this,
                                                     behaviorEvent.getBehavior(),
                                                     width,
                                                     height,
                                                     findColumn(columnId));
            }

            super.queueEvent(wrapperEvent);
        } else {
            super.queueEvent(event);
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

    public void setLiveResize(final boolean _liveResize) {
        getStateHelper().put(PropertyKeys.liveResize, _liveResize);
        handleAttribute("liveResize", _liveResize);
    }

    public void setPropagateSelectionDown(final boolean _propagateSelectionDown) {
        getStateHelper().put(PropertyKeys.propagateSelectionDown, _propagateSelectionDown);
        handleAttribute("propagateSelectionDown", _propagateSelectionDown);
    }

    public void setPropagateSelectionUp(final boolean _propagateSelectionUp) {
        getStateHelper().put(PropertyKeys.propagateSelectionUp, _propagateSelectionUp);
        handleAttribute("propagateSelectionUp", _propagateSelectionUp);
    }

    public void setResizableColumns(final boolean _resizableColumns) {
        getStateHelper().put(PropertyKeys.resizableColumns, _resizableColumns);
        handleAttribute("resizableColumns", _resizableColumns);
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

    public void setScrollWidth(final java.lang.String _scrollWidth) {
        getStateHelper().put(PropertyKeys.scrollWidth, _scrollWidth);
        handleAttribute("scrollWidth", _scrollWidth);
    }

    public void setSelection(final java.lang.Object _selection) {
        getStateHelper().put(PropertyKeys.selection, _selection);
        handleAttribute("selection", _selection);
    }

    public void setSelectionMode(final java.lang.String _selectionMode) {
        getStateHelper().put(PropertyKeys.selectionMode, _selectionMode);
        handleAttribute("selectionMode", _selectionMode);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
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
}