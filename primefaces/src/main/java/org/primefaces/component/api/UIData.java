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

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UniqueIdVendor;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.faces.render.Renderer;
import org.primefaces.util.ComponentUtils;

public class UIData extends javax.faces.component.UIData {

    protected enum PropertyKeys {
        paginator,
        paginatorTemplate,
        rowsPerPageTemplate,
        currentPageReportTemplate,
        pageLinks,
        paginatorPosition,
        paginatorAlwaysVisible,
        rowIndex,
        rowIndexVar,
        saved,
        lazy;

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

    public static final String PAGINATOR_TOP_CONTAINER_CLASS = "ui-paginator ui-paginator-top ui-widget-header";
    public static final String PAGINATOR_BOTTOM_CONTAINER_CLASS = "ui-paginator ui-paginator-bottom ui-widget-header";
    public static final String PAGINATOR_PAGES_CLASS = "ui-paginator-pages";
    public static final String PAGINATOR_PAGE_CLASS = "ui-paginator-page ui-state-default ui-corner-all";
    public static final String PAGINATOR_ACTIVE_PAGE_CLASS = "ui-paginator-page ui-state-default ui-state-active ui-corner-all";
    public static final String PAGINATOR_CURRENT_CLASS = "ui-paginator-current";
    public static final String PAGINATOR_RPP_OPTIONS_CLASS = "ui-paginator-rpp-options ui-widget ui-state-default ui-corner-left";
    public static final String PAGINATOR_JTP_CLASS = "ui-paginator-jtp-select ui-widget ui-state-default ui-corner-left";
    public static final String PAGINATOR_FIRST_PAGE_LINK_CLASS = "ui-paginator-first ui-state-default ui-corner-all";
    public static final String PAGINATOR_FIRST_PAGE_ICON_CLASS = "ui-icon ui-icon-seek-first";
    public static final String PAGINATOR_PREV_PAGE_LINK_CLASS = "ui-paginator-prev ui-state-default ui-corner-all";
    public static final String PAGINATOR_PREV_PAGE_ICON_CLASS = "ui-icon ui-icon-seek-prev";
    public static final String PAGINATOR_NEXT_PAGE_LINK_CLASS = "ui-paginator-next ui-state-default ui-corner-all";
    public static final String PAGINATOR_NEXT_PAGE_ICON_CLASS = "ui-icon ui-icon-seek-next";
    public static final String PAGINATOR_LAST_PAGE_LINK_CLASS = "ui-paginator-last ui-state-default ui-corner-all";

    public static final String PAGINATOR_LAST_PAGE_ICON_CLASS = "ui-icon ui-icon-seek-end";
    private String clientId = null;
    private final StringBuilder idBuilder = new StringBuilder();

    private DataModel model = null;

    public void calculateFirst() {
        final int rows = getRows();

        if (rows > 0) {
            final int first = getFirst();
            final int rowCount = getRowCount();

            if (rowCount > 0 && first >= rowCount) {
                final int numberOfPages = (int) Math.ceil(rowCount * 1d / rows);

                setFirst((numberOfPages - 1) * rows);
            }
        }
    }

    @Override
    public String getClientId(final FacesContext context) {
        if (clientId != null) {
            return clientId;
        }

        String id = getId();
        if (id == null) {
            final UniqueIdVendor parentUniqueIdVendor = ComponentUtils.findParentUniqueIdVendor(this);

            if (parentUniqueIdVendor == null) {
                final UIViewRoot viewRoot = context.getViewRoot();

                if (viewRoot != null) {
                    id = viewRoot.createUniqueId();
                } else {
                    throw new FacesException("Cannot create clientId for " + this.getClass().getCanonicalName());
                }
            } else {
                id = parentUniqueIdVendor.createUniqueId(context, null);
            }

            setId(id);
        }

        final UIComponent namingContainer = ComponentUtils.findParentNamingContainer(this);
        if (namingContainer != null) {
            final String containerClientId = namingContainer.getContainerClientId(context);

            if (containerClientId != null) {
                clientId = idBuilder.append(containerClientId).append(UINamingContainer.getSeparatorChar(context))
                    .append(id).toString();
                idBuilder.setLength(0);
            } else {
                clientId = id;
            }
        } else {
            clientId = id;
        }

        final Renderer renderer = getRenderer(context);
        if (renderer != null) {
            clientId = renderer.convertClientId(context, clientId);
        }

        return clientId;
    }

    @Override
    public String getContainerClientId(final FacesContext context) {
        // clientId is without rowIndex
        final String componentClientId = this.getClientId(context);

        final int rowIndex = getRowIndex();
        if (rowIndex == -1) {
            return componentClientId;
        }

        final String containerClientId = idBuilder.append(componentClientId).append(UINamingContainer
                                                                                        .getSeparatorChar(context))
            .append(rowIndex).toString();
        idBuilder.setLength(0);

        return containerClientId;
    }

    public java.lang.String getCurrentPageReportTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.currentPageReportTemplate,
                                                        "({currentPage} of {totalPages})");
    }

    @Override
    protected DataModel getDataModel() {
        if (model != null) {
            return (model);
        }

        final Object current = getValue();
        if (current == null) {
            setDataModel(new ListDataModel(Collections.EMPTY_LIST));
        } else if (current instanceof DataModel) {
            setDataModel((DataModel) current);
        } else if (current instanceof List) {
            setDataModel(new ListDataModel((List) current));
        } else if (Object[].class.isAssignableFrom(current.getClass())) {
            setDataModel(new ArrayDataModel((Object[]) current));
        } else if (current instanceof ResultSet) {
            setDataModel(new ResultSetDataModel((ResultSet) current));
        } else {
            setDataModel(new ScalarDataModel(current));
        }

        return model;
    }

    public int getPage() {
        if (getRowCount() > 0) {
            final int rows = getRowsToRender();

            if (rows > 0) {
                final int first = getFirst();

                return first / rows;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getPageCount() {
        return (int) Math.ceil(getRowCount() * 1d / getRowsToRender());
    }

    public int getPageLinks() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.pageLinks, 10);
    }

    public java.lang.String getPaginatorPosition() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.paginatorPosition, "both");
    }

    public java.lang.String getPaginatorTemplate() {
        return (java.lang.String) getStateHelper()
            .eval(PropertyKeys.paginatorTemplate,
                  "{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink} {RowsPerPageDropdown}");
    }

    @Override
    public int getRowIndex() {
        return (Integer) getStateHelper().eval(PropertyKeys.rowIndex, -1);
    }

    public java.lang.String getRowIndexVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.rowIndexVar, null);
    }

    public java.lang.String getRowsPerPageTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.rowsPerPageTemplate, null);
    }

    public int getRowsToRender() {
        final int rows = getRows();

        return rows == 0 ? getRowCount() : rows;
    }

    public boolean isLazy() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.lazy, false);
    }

    public boolean isPaginationRequest(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(getClientId(context) + "_pagination");
    }

    public boolean isPaginator() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.paginator, false);
    }

    public boolean isPaginatorAlwaysVisible() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.paginatorAlwaysVisible, true);
    }

    protected void process(final FacesContext context, final UIComponent component, final PhaseId phaseId) {
        if (!shouldProcessChildren(context)) {
            return;
        }

        if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
            component.processDecodes(context);
        } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
            component.processValidators(context);
        } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
            component.processUpdates(context);
        }

    }

    protected void processChildren(final FacesContext context, final PhaseId phaseId) {
        final int first = getFirst();
        final int rows = getRows();
        final int last = rows == 0 ? getRowCount() : (first + rows);

        for (int rowIndex = first; rowIndex < last; rowIndex++) {
            setRowIndex(rowIndex);

            if (!isRowAvailable()) {
                break;
            }

            for (final UIComponent child : getChildren()) {
                if (child.isRendered()) {
                    process(context, child, phaseId);
                }
            }
        }
    }

    protected void processChildrenFacets(final FacesContext context, final PhaseId phaseId) {
        for (final UIComponent child : getChildren()) {
            if (child.isRendered() && (child.getFacetCount() > 0)) {
                for (final UIComponent facet : child.getFacets().values()) {
                    process(context, facet, phaseId);
                }
            }
        }
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, this);
        processPhase(context, PhaseId.APPLY_REQUEST_VALUES);
        decode(context);
        popComponentFromEL(context);
    }

    protected void processFacets(final FacesContext context, final PhaseId phaseId) {
        if (getFacetCount() > 0) {
            for (final UIComponent facet : getFacets().values()) {
                process(context, facet, phaseId);
            }
        }
    }

    protected void processPhase(final FacesContext context, final PhaseId phaseId) {
        setRowIndex(-1);
        processFacets(context, phaseId);
        processChildrenFacets(context, phaseId);
        processChildren(context, phaseId);
        setRowIndex(-1);
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, this);
        processPhase(context, PhaseId.UPDATE_MODEL_VALUES);
        popComponentFromEL(context);
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, this);
        final Application app = context.getApplication();
        app.publishEvent(context, PreValidateEvent.class, this);
        processPhase(context, PhaseId.PROCESS_VALIDATIONS);
        app.publishEvent(context, PostValidateEvent.class, this);
        popComponentFromEL(context);
    }

    protected boolean requiresColumns() {
        return false;
    }

    protected void restoreDescendantState() {
        final FacesContext context = getFacesContext();

        if (getChildCount() > 0) {
            for (final UIComponent kid : getChildren()) {
                restoreDescendantState(kid, context);
            }
        }
    }

    protected void restoreDescendantState(final UIComponent component, final FacesContext context) {
        final String id = component.getId();
        component.setId(id); // reset the client id
        final Map<String, SavedState> saved = (Map<String, SavedState>) getStateHelper().get(PropertyKeys.saved);

        if (component instanceof EditableValueHolder) {
            final EditableValueHolder input = (EditableValueHolder) component;
            final String componentClientId = component.getClientId(context);

            SavedState state = saved.get(componentClientId);
            if (state == null) {
                state = new SavedState();
            }

            input.setValue(state.getValue());
            input.setValid(state.isValid());
            input.setSubmittedValue(state.getSubmittedValue());
            input.setLocalValueSet(state.isLocalValueSet());
        } else if (component instanceof UIForm) {
            final UIForm form = (UIForm) component;
            final String componentClientId = component.getClientId(context);
            SavedState state = saved.get(componentClientId);
            if (state == null) {
                state = new SavedState();
            }

            form.setSubmitted(state.getSubmitted());
            state.setSubmitted(form.isSubmitted());
        }

        // restore state of children
        if (component.getChildCount() > 0) {
            for (final UIComponent kid : component.getChildren()) {
                restoreDescendantState(kid, context);
            }
        }

        // restore state of facets
        if (component.getFacetCount() > 0) {
            for (final UIComponent facet : component.getFacets().values()) {
                restoreDescendantState(facet, context);
            }
        }

    }

    protected void saveDescendantState() {
        final FacesContext context = getFacesContext();

        if (getChildCount() > 0) {
            for (final UIComponent kid : getChildren()) {
                saveDescendantState(kid, context);
            }
        }
    }

    protected void saveDescendantState(final UIComponent component, final FacesContext context) {
        final Map<String, SavedState> saved = (Map<String, SavedState>) getStateHelper().get(PropertyKeys.saved);

        if (component instanceof EditableValueHolder) {
            final EditableValueHolder input = (EditableValueHolder) component;
            SavedState state = null;
            final String componentClientId = component.getClientId(context);

            if (saved == null) {
                state = new SavedState();
                getStateHelper().put(PropertyKeys.saved, componentClientId, state);
            }

            if (state == null) {
                state = saved.get(componentClientId);

                if (state == null) {
                    state = new SavedState();
                    getStateHelper().put(PropertyKeys.saved, componentClientId, state);
                }
            }

            state.setValue(input.getLocalValue());
            state.setValid(input.isValid());
            state.setSubmittedValue(input.getSubmittedValue());
            state.setLocalValueSet(input.isLocalValueSet());
        } else if (component instanceof UIForm) {
            final UIForm form = (UIForm) component;
            final String componentClientId = component.getClientId(context);
            SavedState state = null;
            if (saved == null) {
                state = new SavedState();
                getStateHelper().put(PropertyKeys.saved, componentClientId, state);
            }

            if (state == null) {
                state = saved.get(componentClientId);
                if (state == null) {
                    state = new SavedState();
                    // saved.put(clientId, state);
                    getStateHelper().put(PropertyKeys.saved, componentClientId, state);
                }
            }
            state.setSubmitted(form.isSubmitted());
        }

        // save state for children
        if (component.getChildCount() > 0) {
            for (final UIComponent kid : component.getChildren()) {
                saveDescendantState(kid, context);
            }
        }

        // save state for facets
        if (component.getFacetCount() > 0) {
            for (final UIComponent facet : component.getFacets().values()) {
                saveDescendantState(facet, context);
            }
        }

    }

    public void setCurrentPageReportTemplate(final java.lang.String _currentPageReportTemplate) {
        getStateHelper().put(PropertyKeys.currentPageReportTemplate, _currentPageReportTemplate);
    }

    @Override
    protected void setDataModel(final DataModel dataModel) {
        model = dataModel;
    }

    @Override
    public void setId(final String id) {
        super.setId(id);

        // clear
        clientId = null;
    }

    public void setLazy(final boolean _lazy) {
        getStateHelper().put(PropertyKeys.lazy, _lazy);
    }

    public void setPageLinks(final int _pageLinks) {
        getStateHelper().put(PropertyKeys.pageLinks, _pageLinks);
    }

    public void setPaginator(final boolean _paginator) {
        getStateHelper().put(PropertyKeys.paginator, _paginator);
    }

    public void setPaginatorAlwaysVisible(final boolean _paginatorAlwaysVisible) {
        getStateHelper().put(PropertyKeys.paginatorAlwaysVisible, _paginatorAlwaysVisible);
    }

    public void setPaginatorPosition(final java.lang.String _paginatorPosition) {
        getStateHelper().put(PropertyKeys.paginatorPosition, _paginatorPosition);
    }

    public void setPaginatorTemplate(final java.lang.String _paginatorTemplate) {
        getStateHelper().put(PropertyKeys.paginatorTemplate, _paginatorTemplate);
    }

    @Override
    public void setRowIndex(final int rowIndex) {
        saveDescendantState();

        setRowModel(rowIndex);

        restoreDescendantState();
    }

    public void setRowIndexVar(final java.lang.String _rowIndexVar) {
        getStateHelper().put(PropertyKeys.rowIndexVar, _rowIndexVar);
    }

    public void setRowModel(final int rowIndex) {
        // update rowIndex
        getStateHelper().put(PropertyKeys.rowIndex, rowIndex);
        getDataModel().setRowIndex(rowIndex);

        // clear datamodel
        if (rowIndex == -1) {
            setDataModel(null);
        }

        // update var
        final String var = getVar();
        final String rowIndexVar = getRowIndexVar();
        if (var != null) {
            final Map<String, Object> requestMap = getFacesContext().getExternalContext().getRequestMap();

            if (isRowAvailable()) {
                requestMap.put(var, getRowData());

                if (rowIndexVar != null) requestMap.put(rowIndexVar, rowIndex);
            } else {
                requestMap.remove(var);

                if (rowIndexVar != null) requestMap.remove(rowIndexVar);
            }
        }
    }

    public void setRowsPerPageTemplate(final java.lang.String _rowsPerPageTemplate) {
        getStateHelper().put(PropertyKeys.rowsPerPageTemplate, _rowsPerPageTemplate);
    }

    protected boolean shouldProcessChildren(final FacesContext context) {
        return true;
    }

    protected boolean shouldVisitChildren(final VisitContext context, final boolean visitRows) {
        if (visitRows) {
            setRowIndex(-1);
        }

        final Collection<String> idsToVisit = context.getSubtreeIdsToVisit(this);

        return (!idsToVisit.isEmpty());
    }

    protected boolean shouldVisitRows(final FacesContext context, final VisitContext visitContext) {
        try {
            // JSF 2.1
            final VisitHint skipHint = VisitHint.valueOf("SKIP_ITERATION");
            return !visitContext.getHints().contains(skipHint);
        } catch (final IllegalArgumentException e) {
            // JSF 2.0
            final Object skipHint = context.getAttributes().get("javax.faces.visit.SKIP_ITERATION");
            return !Boolean.TRUE.equals(skipHint);
        }
    }

    public void updatePaginationData(final FacesContext context, final UIData data) {
        data.setRowIndex(-1);
        final String componentClientId = data.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final ELContext elContext = context.getELContext();

        final String firstParam = params.get(componentClientId + "_first");
        final String rowsParam = params.get(componentClientId + "_rows");

        data.setFirst(Integer.valueOf(firstParam));
        data.setRows(Integer.valueOf(rowsParam));

        final ValueExpression firstVe = data.getValueExpression("first");
        final ValueExpression rowsVe = data.getValueExpression("rows");

        if (firstVe != null && !firstVe.isReadOnly(elContext))
            firstVe.setValue(context.getELContext(), data.getFirst());
        if (rowsVe != null && !rowsVe.isReadOnly(elContext)) rowsVe.setValue(context.getELContext(), data.getRows());
    }

    protected boolean visitColumnsAndColumnFacets(final VisitContext context,
                                                  final VisitCallback callback,
                                                  final boolean visitRows) {
        if (visitRows) {
            setRowIndex(-1);
        }

        if (getChildCount() > 0) {
            for (final UIComponent column : getChildren()) {
                final VisitResult result = context.invokeVisitCallback(column, callback); // visit
                                                                                          // the
                                                                                          // column
                                                                                          // directly
                if (result == VisitResult.COMPLETE) {
                    return true;
                }

                if (column.getFacetCount() > 0) {
                    for (final UIComponent columnFacet : column.getFacets().values()) {
                        if (columnFacet.visitTree(context, callback)) {
                            return true;
                        }
                    }
                }

            }
        }

        return false;
    }

    protected boolean visitFacets(final VisitContext context, final VisitCallback callback, final boolean visitRows) {
        if (visitRows) {
            setRowIndex(-1);
        }

        if (getFacetCount() > 0) {
            for (final UIComponent facet : getFacets().values()) {
                if (facet.visitTree(context, callback)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean visitRows(final VisitContext context, final VisitCallback callback, final boolean visitRows) {
        final boolean requiresColumns = requiresColumns();
        int processed = 0;
        int rowIndex = 0;
        int rows = 0;
        if (visitRows) {
            rowIndex = getFirst() - 1;
            rows = getRows();
        }

        while (true) {
            if (visitRows) {
                if ((rows > 0) && (++processed > rows)) {
                    break;
                }

                setRowIndex(++rowIndex);
                if (!isRowAvailable()) {
                    break;
                }
            }

            if (getChildCount() > 0) {
                for (final UIComponent kid : getChildren()) {

                    if (requiresColumns) {
                        if (kid.getChildCount() > 0) {
                            for (final UIComponent grandkid : kid.getChildren()) {
                                if (grandkid.visitTree(context, callback)) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        if (kid.visitTree(context, callback)) {
                            return true;
                        }
                    }
                }
            }

            if (!visitRows) {
                break;
            }

        }

        return false;
    }

    @Override
    public boolean visitTree(final VisitContext context, final VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        final FacesContext facesContext = context.getFacesContext();
        final boolean visitRows = shouldVisitRows(facesContext, context);

        int rowIndex = -1;
        if (visitRows) {
            rowIndex = getRowIndex();
            setRowIndex(-1);
        }

        pushComponentToEL(facesContext, null);

        try {
            final VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) {
                return true;
            }

            if ((result == VisitResult.ACCEPT) && shouldVisitChildren(context, visitRows)) {
                if (visitFacets(context, callback, visitRows)) {
                    return true;
                }

                if (visitColumnsAndColumnFacets(context, callback, visitRows)) {
                    return true;
                }

                if (visitRows(context, callback, visitRows)) {
                    return true;
                }

            }
        } finally {
            popComponentFromEL(facesContext);

            if (visitRows) {
                setRowIndex(rowIndex);
            }
        }

        return false;
    }
}
