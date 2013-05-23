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
package org.primefaces.component.datalist;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.model.DataModel;
import org.primefaces.component.api.UIData;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class DataList extends UIData implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        type,
        itemType,
        style,
        styleClass,
        varStatus;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.DataList";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.DataListRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String DATALIST_CLASS = "ui-datalist ui-widget";

    public static final String CONTENT_CLASS = "ui-datalist-content ui-widget-content";

    public static final String LIST_CLASS = "ui-datalist-data";
    public static final String LIST_ITEM_CLASS = "ui-datalist-item";

    public static final String HEADER_CLASS = "ui-datalist-header ui-widget-header ui-corner-top";
    public static final String FOOTER_CLASS = "ui-datalist-footer ui-widget-header ui-corner-top";

    public DataList() {
        setRendererType(DataList.DEFAULT_RENDERER);
    }

    @Override
    public void broadcast(final FacesEvent event) throws AbortProcessingException {
        if (getVar() != null) {
            super.broadcast(event);
        } else {

            if (event == null) {
                throw new NullPointerException();
            }
            if (event instanceof BehaviorEvent) {
                final BehaviorEvent behaviorEvent = (BehaviorEvent) event;
                final Behavior behavior = behaviorEvent.getBehavior();
                behavior.broadcast(behaviorEvent);
            }
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return DataList.COMPONENT_FAMILY;
    }

    public java.lang.String getItemType() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemType, null);
    }

    public String getListTag() {
        final String type = getType();

        if (type.equalsIgnoreCase("unordered"))
            return "ul";
        else if (type.equalsIgnoreCase("ordered"))
            return "ol";
        else if (type.equalsIgnoreCase("definition"))
            return "dl";
        else if (type.equalsIgnoreCase("none"))
            return null;
        else throw new FacesException("DataList '" + this.getClientId() + "' has invalid list type:'" + type + "'");
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getType() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.type, "unordered");
    }

    public java.lang.String getVarStatus() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.varStatus, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(DataList.OPTIMIZED_PACKAGE)) {
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

    public boolean isDefinition() {
        return getType().equalsIgnoreCase("definition");
    }

    protected void iterateChildren(final FacesContext context, final PhaseId phaseId) {
        setRowIndex(-1);
        if (getChildCount() > 0) {
            for (final UIComponent kid : getChildren()) {
                if (phaseId == PhaseId.APPLY_REQUEST_VALUES)
                    kid.processDecodes(context);
                else if (phaseId == PhaseId.PROCESS_VALIDATIONS)
                    kid.processValidators(context);
                else if (phaseId == PhaseId.UPDATE_MODEL_VALUES)
                    kid.processUpdates(context);
                else throw new IllegalArgumentException();
            }
        }
    }

    public void loadLazyData() {
        final DataModel model = getDataModel();

        if (model != null && model instanceof LazyDataModel) {
            final LazyDataModel lazyModel = (LazyDataModel) model;

            final List<?> data = lazyModel.load(getFirst(), getRows(), null, null, null);

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
    public void processDecodes(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        if (getVar() == null) {
            pushComponentToEL(context, this);
            iterateChildren(context, PhaseId.APPLY_REQUEST_VALUES);
            decode(context);
            popComponentFromEL(context);
        } else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        if (getVar() == null) {
            pushComponentToEL(context, this);
            iterateChildren(context, PhaseId.UPDATE_MODEL_VALUES);
            popComponentFromEL(context);
        } else {
            super.processUpdates(context);
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        if (getVar() == null) {
            pushComponentToEL(context, this);
            iterateChildren(context, PhaseId.PROCESS_VALIDATIONS);
            popComponentFromEL(context);
        } else {
            super.processValidators(context);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (getVar() != null) {
            super.queueEvent(event);
        } else {
            if (event == null) {
                throw new NullPointerException();
            }

            final UIComponent parent = getParent();
            if (parent == null)
                throw new IllegalStateException();
            else parent.queueEvent(event);
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

    public void setItemType(final java.lang.String _itemType) {
        getStateHelper().put(PropertyKeys.itemType, _itemType);
        handleAttribute("itemType", _itemType);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setType(final java.lang.String _type) {
        getStateHelper().put(PropertyKeys.type, _type);
        handleAttribute("type", _type);
    }

    public void setVarStatus(final java.lang.String _varStatus) {
        getStateHelper().put(PropertyKeys.varStatus, _varStatus);
        handleAttribute("varStatus", _varStatus);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}