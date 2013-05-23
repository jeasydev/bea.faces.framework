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
package org.primefaces.component.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Dashboard extends UIPanel implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        model,
        disabled,
        style,
        styleClass;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Dashboard";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.DashboardRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String CONTAINER_CLASS = "ui-dashboard";

    public static final String COLUMN_CLASS = "ui-dashboard-column";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("reorder"));

    public Dashboard() {
        setRendererType(Dashboard.DEFAULT_RENDERER);
    }

    @Override
    public Collection<String> getEventNames() {
        return Dashboard.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Dashboard.COMPONENT_FAMILY;
    }

    public org.primefaces.model.DashboardModel getModel() {
        return (org.primefaces.model.DashboardModel) getStateHelper().eval(PropertyKeys.model, null);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Dashboard.OPTIMIZED_PACKAGE)) {
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

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    private boolean isRequestSource(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (isRequestSource(context)) {
            decode(context);
        } else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isRequestSource(context)) {
            super.processUpdates(context);
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isRequestSource(context)) {
            super.processValidators(context);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (isRequestSource(context)) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(context);
            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("reorder")) {
                final String widgetClientId = params.get(clientId + "_widgetId");
                final Integer itemIndex = Integer.valueOf(params.get(clientId + "_itemIndex"));
                final Integer receiverColumnIndex = Integer.valueOf(params.get(clientId + "_receiverColumnIndex"));
                final String senderIndexParam = clientId + "_senderColumnIndex";
                Integer senderColumnIndex = null;

                if (params.containsKey(senderIndexParam)) {
                    senderColumnIndex = Integer.valueOf(params.get(senderIndexParam));
                }

                final String[] idTokens = widgetClientId.split(":");
                final String widgetId = idTokens.length == 1 ? idTokens[0] : idTokens[idTokens.length - 1];

                final DashboardReorderEvent reorderEvent = new DashboardReorderEvent(this,
                                                                                     behaviorEvent.getBehavior(),
                                                                                     widgetId,
                                                                                     itemIndex,
                                                                                     receiverColumnIndex,
                                                                                     senderColumnIndex);
                reorderEvent.setPhaseId(behaviorEvent.getPhaseId());

                updateDashboardModel(getModel(), widgetId, itemIndex, receiverColumnIndex, senderColumnIndex);

                super.queueEvent(reorderEvent);
            }
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

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setModel(final org.primefaces.model.DashboardModel _model) {
        getStateHelper().put(PropertyKeys.model, _model);
        handleAttribute("model", _model);
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

    protected void updateDashboardModel(final DashboardModel model,
                                        final String widgetId,
                                        final Integer itemIndex,
                                        final Integer receiverColumnIndex,
                                        final Integer senderColumnIndex) {
        if (senderColumnIndex == null) {
            // Reorder widget in same column
            final DashboardColumn column = model.getColumn(receiverColumnIndex);
            column.reorderWidget(itemIndex, widgetId);
        } else {
            // Transfer widget
            final DashboardColumn oldColumn = model.getColumn(senderColumnIndex);
            final DashboardColumn newColumn = model.getColumn(receiverColumnIndex);

            model.transferWidget(oldColumn, newColumn, widgetId, itemIndex);
        }
    }
}