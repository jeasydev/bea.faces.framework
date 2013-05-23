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
package org.primefaces.component.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ResizeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "layout/layout.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "layout/layout.js") })
public class Layout extends UIPanel implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        fullPage,
        style,
        styleClass,
        onResize,
        onClose,
        onToggle,
        resizeTitle,
        collapseTitle,
        expandTitle,
        closeTitle,
        stateful;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Layout";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.LayoutRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String UNIT_CLASS = "ui-layout-unit ui-widget ui-widget-content ui-corner-all";

    public final static String UNIT_HEADER_CLASS = "ui-layout-unit-header ui-widget-header ui-corner-all";

    public final static String UNIT_CONTENT_CLASS = "ui-layout-unit-content ui-widget-content";
    public final static String UNIT_FOOTER_CLASS = "ui-layout-unit-footer ui-widget-header ui-corner-all";

    public final static String UNIT_HEADER_TITLE_CLASS = "ui-layout-unit-header-title";
    public final static String UNIT_FOOTER_TITLE_CLASS = "ui-layout-unit-footer-title";

    public final static String UNIT_HEADER_ICON_CLASS = "ui-layout-unit-header-icon ui-state-default ui-corner-all";
    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("toggle",
                                                                                                           "close",
                                                                                                           "resize"));

    public Layout() {
        setRendererType(Layout.DEFAULT_RENDERER);
    }

    public java.lang.String getCloseTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.closeTitle, "Close");
    }

    public java.lang.String getCollapseTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.collapseTitle, "Collapse");
    }

    @Override
    public Collection<String> getEventNames() {
        return Layout.EVENT_NAMES;
    }

    public java.lang.String getExpandTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.expandTitle, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Layout.COMPONENT_FAMILY;
    }

    protected LayoutUnit getLayoutUnitByPosition(final String name) {
        for (final UIComponent child : getChildren()) {
            if (child instanceof LayoutUnit) {
                final LayoutUnit layoutUnit = (LayoutUnit) child;

                if (layoutUnit.getPosition().equalsIgnoreCase(name)) return layoutUnit;
            }
        }

        return null;
    }

    public java.lang.String getOnClose() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onClose, null);
    }

    public java.lang.String getOnResize() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onResize, null);
    }

    public java.lang.String getOnToggle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onToggle, null);
    }

    public java.lang.String getResizeTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.resizeTitle, null);
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
            if (cname != null && cname.startsWith(Layout.OPTIMIZED_PACKAGE)) {
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

    public boolean isElementLayout() {
        return !isNested() && !isFullPage();
    }

    public boolean isFullPage() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.fullPage, false);
    }

    public boolean isNested() {
        return getParent() instanceof LayoutUnit;
    }

    private boolean isSelfRequest(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isStateful() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.stateful, false);
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (isSelfRequest(context)) {
            decode(context);
        } else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isSelfRequest(context)) {
            super.processUpdates(context);
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isSelfRequest(context)) {
            super.processValidators(context);
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
        final String clientId = this.getClientId(context);

        if (isSelfRequest(context)) {

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            FacesEvent wrapperEvent = null;

            if (eventName.equals("toggle")) {
                final boolean collapsed = Boolean.valueOf(params.get(clientId + "_collapsed"));
                final LayoutUnit unit = getLayoutUnitByPosition(params.get(clientId + "_unit"));
                final Visibility visibility = collapsed ? Visibility.HIDDEN : Visibility.VISIBLE;
                unit.setCollapsed(collapsed);

                wrapperEvent = new ToggleEvent(unit, behaviorEvent.getBehavior(), visibility);
            } else if (eventName.equals("close")) {
                final LayoutUnit unit = getLayoutUnitByPosition(params.get(clientId + "_unit"));
                unit.setVisible(false);

                wrapperEvent = new CloseEvent(unit, behaviorEvent.getBehavior());
            } else if (eventName.equals("resize")) {
                final LayoutUnit unit = getLayoutUnitByPosition(params.get(clientId + "_unit"));
                final String position = unit.getPosition();
                final int width = Integer.valueOf(params.get(clientId + "_width"));
                final int height = Integer.valueOf(params.get(clientId + "_height"));

                if (position.equals("west") || position.equals("east")) {
                    unit.setSize(String.valueOf(width));
                } else if (position.equals("north") || position.equals("south")) {
                    unit.setSize(String.valueOf(height));
                }

                wrapperEvent = new ResizeEvent(unit, behaviorEvent.getBehavior(), width, height);
            }

            wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());

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

    public void setCloseTitle(final java.lang.String _closeTitle) {
        getStateHelper().put(PropertyKeys.closeTitle, _closeTitle);
        handleAttribute("closeTitle", _closeTitle);
    }

    public void setCollapseTitle(final java.lang.String _collapseTitle) {
        getStateHelper().put(PropertyKeys.collapseTitle, _collapseTitle);
        handleAttribute("collapseTitle", _collapseTitle);
    }

    public void setExpandTitle(final java.lang.String _expandTitle) {
        getStateHelper().put(PropertyKeys.expandTitle, _expandTitle);
        handleAttribute("expandTitle", _expandTitle);
    }

    public void setFullPage(final boolean _fullPage) {
        getStateHelper().put(PropertyKeys.fullPage, _fullPage);
        handleAttribute("fullPage", _fullPage);
    }

    public void setOnClose(final java.lang.String _onClose) {
        getStateHelper().put(PropertyKeys.onClose, _onClose);
        handleAttribute("onClose", _onClose);
    }

    public void setOnResize(final java.lang.String _onResize) {
        getStateHelper().put(PropertyKeys.onResize, _onResize);
        handleAttribute("onResize", _onResize);
    }

    public void setOnToggle(final java.lang.String _onToggle) {
        getStateHelper().put(PropertyKeys.onToggle, _onToggle);
        handleAttribute("onToggle", _onToggle);
    }

    public void setResizeTitle(final java.lang.String _resizeTitle) {
        getStateHelper().put(PropertyKeys.resizeTitle, _resizeTitle);
        handleAttribute("resizeTitle", _resizeTitle);
    }

    public void setStateful(final boolean _stateful) {
        getStateHelper().put(PropertyKeys.stateful, _stateful);
        handleAttribute("stateful", _stateful);
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