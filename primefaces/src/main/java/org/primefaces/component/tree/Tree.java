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
package org.primefaces.component.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Tree extends UITree implements org.primefaces.component.api.Widget, org.primefaces.component.api.RTLAware,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        dynamic,
        cache,
        onNodeClick,
        selection,
        style,
        styleClass,
        selectionMode,
        highlight,
        datakey,
        animate,
        orientation,
        propagateSelectionUp,
        propagateSelectionDown,
        dir;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Tree";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.TreeRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("select",
                                                                                                           "unselect",
                                                                                                           "expand",
                                                                                                           "collapse"));

    private final List<String> selectedRowKeys = new ArrayList<String>();

    private Map<String, UITreeNode> nodes;
    public static String CONTAINER_CLASS = "ui-tree ui-widget ui-widget-content ui-corner-all";

    public static String CONTAINER_RTL_CLASS = "ui-tree ui-tree-rtl ui-widget ui-widget-content ui-corner-all";
    public static String HORIZONTAL_CONTAINER_CLASS = "ui-tree ui-tree-horizontal ui-widget ui-widget-content ui-corner-all";

    public static String ROOT_NODES_CLASS = "ui-tree-container";
    public static String PARENT_NODE_CLASS = "ui-treenode ui-treenode-parent";

    public static String LEAF_NODE_CLASS = "ui-treenode ui-treenode-leaf";
    public static String CHILDREN_NODES_CLASS = "ui-treenode-children";

    public static String NODE_CONTENT_CLASS_V = "ui-treenode-content";
    public static String SELECTABLE_NODE_CONTENT_CLASS_V = "ui-treenode-content ui-tree-selectable";

    public static String NODE_CONTENT_CLASS_H = "ui-treenode-content ui-state-default ui-corner-all";
    public static String SELECTABLE_NODE_CONTENT_CLASS_H = "ui-treenode-content ui-tree-selectable ui-state-default ui-corner-all";

    public static String EXPANDED_ICON_CLASS_V = "ui-tree-toggler ui-icon ui-icon-triangle-1-s";
    public static String COLLAPSED_ICON_CLASS_V = "ui-tree-toggler ui-icon ui-icon-triangle-1-e";

    public static String COLLAPSED_ICON_RTL_CLASS_V = "ui-tree-toggler ui-icon ui-icon-triangle-1-w";
    public static String EXPANDED_ICON_CLASS_H = "ui-tree-toggler ui-icon ui-icon-minus";

    public static String COLLAPSED_ICON_CLASS_H = "ui-tree-toggler ui-icon ui-icon-plus";
    public static String LEAF_ICON_CLASS = "ui-treenode-leaf-icon";

    public static String NODE_ICON_CLASS = "ui-treenode-icon ui-icon";
    public static String NODE_LABEL_CLASS = "ui-treenode-label ui-corner-all";

    public Tree() {
        setRendererType(Tree.DEFAULT_RENDERER);
    }

    public java.lang.Object getDatakey() {
        return getStateHelper().eval(PropertyKeys.datakey, null);
    }

    public java.lang.String getDir() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dir, "ltr");
    }

    @Override
    public Collection<String> getEventNames() {
        return Tree.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Tree.COMPONENT_FAMILY;
    }

    public Object getLocalSelectedNodes() {
        return getStateHelper().get(PropertyKeys.selection);
    }

    public java.lang.String getOnNodeClick() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onNodeClick, null);
    }

    public java.lang.String getOrientation() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.orientation, "vertical");
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
    };;

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

    public Map<String, UITreeNode> getTreeNodes() {
        if (nodes == null) {
            nodes = new HashMap<String, UITreeNode>();
            for (final UIComponent child : getChildren()) {
                final UITreeNode node = (UITreeNode) child;
                nodes.put(node.getType(), node);
            }
        }

        return nodes;
    }

    public UITreeNode getUITreeNodeByType(final String type) {
        final UITreeNode node = getTreeNodes().get(type);

        if (node == null)
            throw new javax.faces.FacesException("Unsupported tree node type:" + type);
        else return node;
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Tree.OPTIMIZED_PACKAGE)) {
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

    public boolean isAnimate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.animate, false);
    }

    public boolean isCache() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.cache, true);
    }

    public boolean isCheckboxSelection() {
        final String selectionMode = getSelectionMode();

        return selectionMode != null && selectionMode.equals("checkbox");
    }

    public boolean isDynamic() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.dynamic, false);
    }

    public boolean isHighlight() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.highlight, true);
    }

    public boolean isNodeExpandRequest(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context)
                                                                                     + "_expandNode");
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

    @Override
    public boolean isRTL() {
        return getDir().equalsIgnoreCase("rtl");
    }

    private boolean isToggleRequest(final FacesContext context) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(context);

        return params.get(clientId + "_expandNode") != null || params.get(clientId + "_collapseNode") != null;
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (isToggleRequest(context)) {
            decode(context);
            context.renderResponse();
        } else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        super.processUpdates(context);

        final String selectionMode = getSelectionMode();
        final ValueExpression selectionVE = getValueExpression("selection");

        if (selectionMode != null && selectionVE != null) {

            final Object selection = getLocalSelectedNodes();
            final Object previousSelection = selectionVE.getValue(context.getELContext());

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

            selectionVE.setValue(context.getELContext(), selection);
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
                setRowKey(params.get(clientId + "_expandNode"));

                wrapperEvent = new NodeExpandEvent(this, behaviorEvent.getBehavior(), getRowNode());
                wrapperEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else if (eventName.equals("collapse")) {
                setRowKey(params.get(clientId + "_collapseNode"));
                final TreeNode collapsedNode = getRowNode();
                collapsedNode.setExpanded(false);

                wrapperEvent = new NodeCollapseEvent(this, behaviorEvent.getBehavior(), collapsedNode);
                wrapperEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else if (eventName.equals("select")) {
                setRowKey(params.get(clientId + "_instantSelection"));

                wrapperEvent = new NodeSelectEvent(this, behaviorEvent.getBehavior(), getRowNode());
                wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());
            } else if (eventName.equals("unselect")) {
                setRowKey(params.get(clientId + "_instantUnselection"));

                wrapperEvent = new NodeUnselectEvent(this, behaviorEvent.getBehavior(), getRowNode());
                wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());
            }

            super.queueEvent(wrapperEvent);

            setRowKey(null);
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

    public void setAnimate(final boolean _animate) {
        getStateHelper().put(PropertyKeys.animate, _animate);
        handleAttribute("animate", _animate);
    }

    public void setCache(final boolean _cache) {
        getStateHelper().put(PropertyKeys.cache, _cache);
        handleAttribute("cache", _cache);
    }

    public void setDatakey(final java.lang.Object _datakey) {
        getStateHelper().put(PropertyKeys.datakey, _datakey);
        handleAttribute("datakey", _datakey);
    }

    public void setDir(final java.lang.String _dir) {
        getStateHelper().put(PropertyKeys.dir, _dir);
        handleAttribute("dir", _dir);
    }

    public void setDynamic(final boolean _dynamic) {
        getStateHelper().put(PropertyKeys.dynamic, _dynamic);
        handleAttribute("dynamic", _dynamic);
    }

    public void setHighlight(final boolean _highlight) {
        getStateHelper().put(PropertyKeys.highlight, _highlight);
        handleAttribute("highlight", _highlight);
    }

    public void setOnNodeClick(final java.lang.String _onNodeClick) {
        getStateHelper().put(PropertyKeys.onNodeClick, _onNodeClick);
        handleAttribute("onNodeClick", _onNodeClick);
    }

    public void setOrientation(final java.lang.String _orientation) {
        getStateHelper().put(PropertyKeys.orientation, _orientation);
        handleAttribute("orientation", _orientation);
    }

    public void setPropagateSelectionDown(final boolean _propagateSelectionDown) {
        getStateHelper().put(PropertyKeys.propagateSelectionDown, _propagateSelectionDown);
        handleAttribute("propagateSelectionDown", _propagateSelectionDown);
    }

    public void setPropagateSelectionUp(final boolean _propagateSelectionUp) {
        getStateHelper().put(PropertyKeys.propagateSelectionUp, _propagateSelectionUp);
        handleAttribute("propagateSelectionUp", _propagateSelectionUp);
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

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}