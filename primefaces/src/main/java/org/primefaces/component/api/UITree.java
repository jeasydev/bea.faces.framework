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

import java.util.Collection;
import java.util.Map;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import org.primefaces.model.TreeNode;

public abstract class UITree extends UIComponentBase implements NamingContainer {

    protected enum PropertyKeys {
        var,
        saved,
        value;

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

    public final static String SEPARATOR = "_";

    private String rowKey;

    private TreeNode rowNode;

    private boolean rtl;

    @Override
    public void broadcast(final FacesEvent event) throws AbortProcessingException {
        if (!(event instanceof WrapperEvent)) {
            super.broadcast(event);
            return;
        }

        final WrapperEvent wrapperEvent = (WrapperEvent) event;
        final FacesEvent originalEvent = wrapperEvent.getFacesEvent();
        final UIComponent originalSource = (UIComponent) originalEvent.getSource();
        setRowKey(wrapperEvent.getRowKey());

        originalSource.broadcast(originalEvent);
    }

    protected boolean doVisitChildren(final VisitContext context) {
        final Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);

        return (!idsToVisit.isEmpty());
    }

    protected TreeNode findTreeNode(TreeNode searchRoot, final String rowKey) {
        if (rowKey == null) {
            return null;
        }

        if (rowKey.equals("root")) {
            return getValue();
        }

        final String[] paths = rowKey.split(UITree.SEPARATOR);

        if (paths.length == 0) return null;

        final int childIndex = Integer.parseInt(paths[0]);
        searchRoot = searchRoot.getChildren().get(childIndex);

        if (paths.length == 1) {
            return searchRoot;
        } else {
            final String relativeRowKey = rowKey.substring(rowKey.indexOf(UITree.SEPARATOR) + 1);

            return findTreeNode(searchRoot, relativeRowKey);
        }
    }

    @Override
    public String getContainerClientId(final FacesContext context) {
        final String clientId = super.getContainerClientId(context);
        final String _rowKey = getRowKey();

        if (_rowKey == null) {
            return clientId;
        } else {
            final StringBuilder builder = new StringBuilder();

            return builder.append(clientId).append(UINamingContainer.getSeparatorChar(context)).append(rowKey)
                .toString();
        }
    }

    public String getRowKey() {
        return rowKey;
    }

    public TreeNode getRowNode() {
        return rowNode;
    }

    public TreeNode getValue() {
        return (TreeNode) getStateHelper().eval(PropertyKeys.value, null);
    }

    public java.lang.String getVar() {
        return (String) getStateHelper().eval(PropertyKeys.var, null);
    }

    public boolean isRTLRendering() {
        return rtl;
    }

    protected void processColumnChildren(final FacesContext context, final PhaseId phaseId, final String nodeKey) {
        setRowKey(nodeKey);

        if (nodeKey == null) return;

        for (final UIComponent child : getChildren()) {
            if (child instanceof UIColumn && child.isRendered()) {
                for (final UIComponent grandkid : child.getChildren()) {
                    if (!grandkid.isRendered()) continue;

                    if (phaseId == PhaseId.APPLY_REQUEST_VALUES)
                        grandkid.processDecodes(context);
                    else if (phaseId == PhaseId.PROCESS_VALIDATIONS)
                        grandkid.processValidators(context);
                    else if (phaseId == PhaseId.UPDATE_MODEL_VALUES)
                        grandkid.processUpdates(context);
                    else throw new IllegalArgumentException();
                }
            }
        }
    }

    protected void processColumnFacets(final FacesContext context, final PhaseId phaseId) {
        setRowKey(null);

        for (final UIComponent child : getChildren()) {
            if (child instanceof UIColumn && child.isRendered()) {
                final UIColumn column = (UIColumn) child;

                if (column.getFacetCount() > 0) {
                    for (final UIComponent columnFacet : column.getFacets().values()) {
                        if (phaseId == PhaseId.APPLY_REQUEST_VALUES)
                            columnFacet.processDecodes(context);
                        else if (phaseId == PhaseId.PROCESS_VALIDATIONS)
                            columnFacet.processValidators(context);
                        else if (phaseId == PhaseId.UPDATE_MODEL_VALUES)
                            columnFacet.processUpdates(context);
                        else throw new IllegalArgumentException();
                    }
                }
            }
        }
    }

    @Override
    public void processDecodes(final FacesContext context) {
        pushComponentToEL(context, this);

        final Map<String, SavedState> saved = (Map<String, SavedState>) getStateHelper().get(PropertyKeys.saved);
        if (saved == null) {
            getStateHelper().remove(PropertyKeys.saved);
        }

        processNodes(context, PhaseId.APPLY_REQUEST_VALUES);

        try {
            decode(context);
        } catch (final RuntimeException e) {
            context.renderResponse();
            throw e;
        }

        popComponentFromEL(context);
    }

    protected void processFacets(final FacesContext context, final PhaseId phaseId) {
        setRowKey(null);

        if (getFacetCount() > 0) {
            for (final UIComponent facet : getFacets().values()) {
                if (phaseId == PhaseId.APPLY_REQUEST_VALUES)
                    facet.processDecodes(context);
                else if (phaseId == PhaseId.PROCESS_VALIDATIONS)
                    facet.processValidators(context);
                else if (phaseId == PhaseId.UPDATE_MODEL_VALUES)
                    facet.processUpdates(context);
                else throw new IllegalArgumentException();
            }
        }
    }

    protected void processNode(final FacesContext context,
                               final PhaseId phaseId,
                               final TreeNode treeNode,
                               final String rowKey) {
        processColumnChildren(context, phaseId, rowKey);

        // process child nodes if node is expanded or node itself is the root
        if (treeNode.isExpanded() || treeNode.getParent() == null) {
            int childIndex = 0;
            for (final TreeNode treeNode2 : treeNode.getChildren()) {
                final String childRowKey = rowKey == null ? String.valueOf(childIndex) : rowKey + UITree.SEPARATOR
                    + childIndex;

                processNode(context, phaseId, treeNode2, childRowKey);

                childIndex++;
            }
        }
    }

    protected void processNodes(final FacesContext context, final PhaseId phaseId) {

        processFacets(context, phaseId);
        processColumnFacets(context, phaseId);

        final TreeNode root = getValue();
        if (root != null) {
            processNode(context, phaseId, root, null);
        }

        setRowKey(null);
    }

    @Override
    public void processUpdates(final FacesContext context) {
        pushComponentToEL(context, this);

        processNodes(context, PhaseId.UPDATE_MODEL_VALUES);

        popComponentFromEL(context);
    }

    @Override
    public void processValidators(final FacesContext context) {
        pushComponentToEL(context, this);

        processNodes(context, PhaseId.PROCESS_VALIDATIONS);

        popComponentFromEL(context);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        super.queueEvent(new WrapperEvent(this, event, getRowKey()));
    }

    private void restoreDescendantState() {
        final FacesContext context = getFacesContext();

        for (final UIComponent child : getChildren()) {
            restoreDescendantState(child, context);
        }
    }

    private void restoreDescendantState(final UIComponent component, final FacesContext context) {
        // force id reset
        final String id = component.getId();
        component.setId(id);

        final Map<String, SavedState> saved = (Map<String, SavedState>) getStateHelper().get(PropertyKeys.saved);

        if (component instanceof EditableValueHolder) {
            final EditableValueHolder input = (EditableValueHolder) component;
            final String clientId = component.getClientId(context);

            SavedState state = saved.get(clientId);
            if (state == null) {
                state = new SavedState();
            }

            input.setValue(state.getValue());
            input.setValid(state.isValid());
            input.setSubmittedValue(state.getSubmittedValue());
            input.setLocalValueSet(state.isLocalValueSet());
        }
        for (final UIComponent kid : component.getChildren()) {
            restoreDescendantState(kid, context);
        }

        if (component.getFacetCount() > 0) {
            for (final UIComponent facet : component.getFacets().values()) {
                restoreDescendantState(facet, context);
            }
        }
    }

    private void saveDescendantState() {
        final FacesContext context = getFacesContext();

        for (final UIComponent child : getChildren()) {
            saveDescendantState(child, context);
        }
    }

    private void saveDescendantState(final UIComponent component, final FacesContext context) {
        final Map<String, SavedState> saved = (Map<String, SavedState>) getStateHelper().get(PropertyKeys.saved);

        if (component instanceof EditableValueHolder) {
            final EditableValueHolder input = (EditableValueHolder) component;
            SavedState state = null;
            final String clientId = component.getClientId(context);

            if (saved == null) {
                state = new SavedState();
                getStateHelper().put(PropertyKeys.saved, clientId, state);
            }

            if (state == null) {
                state = saved.get(clientId);

                if (state == null) {
                    state = new SavedState();
                    getStateHelper().put(PropertyKeys.saved, clientId, state);
                }
            }

            state.setValue(input.getLocalValue());
            state.setValid(input.isValid());
            state.setSubmittedValue(input.getSubmittedValue());
            state.setLocalValueSet(input.isLocalValueSet());
        }

        for (final UIComponent uiComponent : component.getChildren()) {
            saveDescendantState(uiComponent, context);
        }

        if (component.getFacetCount() > 0) {
            for (final UIComponent facet : component.getFacets().values()) {
                saveDescendantState(facet, context);
            }
        }
    }

    public void setRowKey(final String rowKey) {
        final Map<String, Object> requestMap = getFacesContext().getExternalContext().getRequestMap();
        saveDescendantState();

        this.rowKey = rowKey;

        if (rowKey == null) {
            requestMap.remove(getVar());
        } else {
            final TreeNode root = getValue();
            rowNode = findTreeNode(root, rowKey);

            requestMap.put(getVar(), rowNode.getData());
        }

        restoreDescendantState();
    }

    public void setRTLRendering(final boolean rtl) {
        this.rtl = rtl;
    }

    public void setValue(final TreeNode _value) {
        getStateHelper().put(PropertyKeys.value, _value);
    }

    public void setVar(final java.lang.String _var) {
        getStateHelper().put(PropertyKeys.var, _var);
    }

    private boolean visitColumns(final VisitContext context, final VisitCallback callback, final String rowKey) {
        setRowKey(rowKey);

        if (rowKey == null) return false;

        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIColumn) {
                    if (child.visitTree(context, callback)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean visitFacets(final VisitContext context, final VisitCallback callback) {
        if (getFacetCount() > 0) {
            for (final UIComponent facet : getFacets().values()) {
                if (facet.visitTree(context, callback)) return true;
            }
        }

        return false;
    }

    protected boolean visitNode(final VisitContext context,
                                final VisitCallback callback,
                                final TreeNode treeNode,
                                final String rowKey) {
        if (visitColumns(context, callback, rowKey)) {
            return true;
        }

        // visit child nodes if node is expanded or node itself is the root
        if ((treeNode.isExpanded() || treeNode.getParent() == null)) {
            int childIndex = 0;
            for (final TreeNode treeNode2 : treeNode.getChildren()) {
                final String childRowKey = rowKey == null ? String.valueOf(childIndex) : rowKey + UITree.SEPARATOR
                    + childIndex;

                if (visitNode(context, callback, treeNode2, childRowKey)) {
                    return true;
                }

                childIndex++;
            }
        }

        return false;
    }

    protected boolean visitNodes(final VisitContext context, final VisitCallback callback) {
        final TreeNode root = getValue();
        if (root != null) {
            if (visitNode(context, callback, root, null)) {
                return true;
            }
        }

        setRowKey(null);

        return false;
    }

    @Override
    public boolean visitTree(final VisitContext context, final VisitCallback callback) {
        if (!isVisitable(context)) return false;

        final FacesContext facesContext = context.getFacesContext();

        final String oldRowKey = getRowKey();
        setRowKey(null);

        pushComponentToEL(facesContext, null);

        try {
            final VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) return true;

            if ((result == VisitResult.ACCEPT) && doVisitChildren(context)) {
                if (visitFacets(context, callback)) return true;

                if (visitNodes(context, callback)) return true;
            }
        } finally {
            popComponentFromEL(facesContext);
            setRowKey(oldRowKey);
        }

        return false;
    }
}