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
package org.primefaces.component.tabview;

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
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.api.UIData;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class TabView extends UIData implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.RTLAware, javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        activeIndex,
        effect,
        effectDuration,
        dynamic,
        cache,
        onTabChange,
        onTabShow,
        style,
        styleClass,
        orientation,
        onTabClose,
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

    public static final String COMPONENT_TYPE = "org.primefaces.component.TabView";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.TabViewRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String CONTAINER_CLASS = "ui-tabs ui-widget ui-widget-content ui-corner-all ui-hidden-container";

    public static final String NAVIGATOR_CLASS = "ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all";

    public static final String INACTIVE_TAB_HEADER_CLASS = "ui-state-default";
    public static final String ACTIVE_TAB_HEADER_CLASS = "ui-state-default ui-tabs-selected ui-state-active";

    public static final String PANELS_CLASS = "ui-tabs-panels";
    public static final String ACTIVE_TAB_CONTENT_CLASS = "ui-tabs-panel ui-widget-content ui-corner-bottom";

    public static final String INACTIVE_TAB_CONTENT_CLASS = "ui-tabs-panel ui-widget-content ui-corner-bottom ui-helper-hidden";
    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("tabChange",
                                                                                                           "tabClose"));

    List<Tab> loadedTabs;

    public TabView() {
        setRendererType(TabView.DEFAULT_RENDERER);
    }

    public Tab findTab(final String tabClientId) {
        for (final UIComponent component : getChildren()) {
            if (component.getClientId().equals(tabClientId)) return (Tab) component;
        }

        return null;
    }

    public int getActiveIndex() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.activeIndex, 0);
    }

    public java.lang.String getDir() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dir, "ltr");
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, null);
    }

    public java.lang.String getEffectDuration() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effectDuration, "normal");
    }

    @Override
    public Collection<String> getEventNames() {
        return TabView.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return TabView.COMPONENT_FAMILY;
    }

    public List<Tab> getLoadedTabs() {
        if (loadedTabs == null) {
            loadedTabs = new ArrayList<Tab>();

            for (final UIComponent component : getChildren()) {
                if (component instanceof Tab) {
                    final Tab tab = (Tab) component;

                    if (tab.isLoaded()) loadedTabs.add(tab);
                }
            }
        }

        return loadedTabs;
    }

    public java.lang.String getOnTabChange() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onTabChange, null);
    }

    public java.lang.String getOnTabClose() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onTabClose, null);
    }

    public java.lang.String getOnTabShow() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onTabShow, null);
    }

    public java.lang.String getOrientation() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.orientation, "top");
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
            if (cname != null && cname.startsWith(TabView.OPTIMIZED_PACKAGE)) {
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

    public boolean isCache() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.cache, true);
    }

    public boolean isContentLoadRequest(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(this.getClientId(context)
                                                                                     + "_contentLoad");
    }

    public boolean isDynamic() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.dynamic, false);
    }

    private boolean isRequestSource(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public boolean isRTL() {
        return getDir().equalsIgnoreCase("rtl");
    }

    @Override
    public void processDecodes(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        // only process loaded tabs on dynamic case without tab model
        if (isDynamic() && getVar() == null) {
            for (final Tab tab : getLoadedTabs()) {
                tab.processDecodes(context);
            }
            decode(context);
        } else {
            if (getVar() == null) {
                final Iterator kids = getFacetsAndChildren();
                while (kids.hasNext()) {
                    final UIComponent kid = (UIComponent) kids.next();
                    kid.processDecodes(context);
                }

                decode(context);
            } else {
                super.processDecodes(context);
            }
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        final ValueExpression expr = getValueExpression("activeIndex");
        if (expr != null) {
            expr.setValue(getFacesContext().getELContext(), getActiveIndex());
            resetActiveIndex();
        }

        // only process loaded tabs on dynamic case without tab model
        if (isDynamic() && getVar() == null) {
            for (final Tab tab : getLoadedTabs()) {
                tab.processUpdates(context);
            }
        } else {
            if (getVar() == null) {
                final Iterator kids = getFacetsAndChildren();
                while (kids.hasNext()) {
                    final UIComponent kid = (UIComponent) kids.next();
                    kid.processUpdates(context);
                }
            } else {
                super.processUpdates(context);
            }
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isRendered()) {
            return;
        }

        // only process loaded tabs on dynamic case without tab model
        if (isDynamic() && getVar() == null) {
            for (final Tab tab : getLoadedTabs()) {
                tab.processValidators(context);
            }
        } else {
            if (getVar() == null) {
                final Iterator kids = getFacetsAndChildren();
                while (kids.hasNext()) {
                    final UIComponent kid = (UIComponent) kids.next();
                    kid.processValidators(context);
                }
            } else {
                super.processValidators(context);
            }
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (isRequestSource(context) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(context);

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("tabChange")) {
                final String tabClientId = params.get(clientId + "_newTab");
                final TabChangeEvent changeEvent = new TabChangeEvent(this,
                                                                      behaviorEvent.getBehavior(),
                                                                      findTab(tabClientId));

                if (getVar() != null) {
                    final int tabindex = Integer.parseInt(params.get(clientId + "_tabindex"));
                    setRowIndex(tabindex);
                    changeEvent.setData(getRowData());
                    changeEvent.setTab((Tab) getChildren().get(0));
                    setRowIndex(-1);
                }

                changeEvent.setPhaseId(behaviorEvent.getPhaseId());

                super.queueEvent(changeEvent);
            } else if (eventName.equals("tabClose")) {
                final String tabClientId = params.get(clientId + "_closeTab");
                final TabCloseEvent closeEvent = new TabCloseEvent(this,
                                                                   behaviorEvent.getBehavior(),
                                                                   findTab(tabClientId));

                if (getVar() != null) {
                    final int tabindex = Integer.parseInt(params.get(clientId + "_tabindex"));
                    setRowIndex(tabindex);
                    closeEvent.setData(getRowData());
                    closeEvent.setTab((Tab) getChildren().get(0));
                    setRowIndex(-1);
                }

                closeEvent.setPhaseId(behaviorEvent.getPhaseId());

                super.queueEvent(closeEvent);
            }
        } else {
            super.queueEvent(event);
        }
    }

    protected void resetActiveIndex() {
        getStateHelper().remove(PropertyKeys.activeIndex);
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

    public void setActiveIndex(final int _activeIndex) {
        getStateHelper().put(PropertyKeys.activeIndex, _activeIndex);
        handleAttribute("activeIndex", _activeIndex);
    }

    public void setCache(final boolean _cache) {
        getStateHelper().put(PropertyKeys.cache, _cache);
        handleAttribute("cache", _cache);
    }

    public void setDir(final java.lang.String _dir) {
        getStateHelper().put(PropertyKeys.dir, _dir);
        handleAttribute("dir", _dir);
    }

    public void setDynamic(final boolean _dynamic) {
        getStateHelper().put(PropertyKeys.dynamic, _dynamic);
        handleAttribute("dynamic", _dynamic);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectDuration(final java.lang.String _effectDuration) {
        getStateHelper().put(PropertyKeys.effectDuration, _effectDuration);
        handleAttribute("effectDuration", _effectDuration);
    }

    public void setOnTabChange(final java.lang.String _onTabChange) {
        getStateHelper().put(PropertyKeys.onTabChange, _onTabChange);
        handleAttribute("onTabChange", _onTabChange);
    }

    public void setOnTabClose(final java.lang.String _onTabClose) {
        getStateHelper().put(PropertyKeys.onTabClose, _onTabClose);
        handleAttribute("onTabClose", _onTabClose);
    }

    public void setOnTabShow(final java.lang.String _onTabShow) {
        getStateHelper().put(PropertyKeys.onTabShow, _onTabShow);
        handleAttribute("onTabShow", _onTabShow);
    }

    public void setOrientation(final java.lang.String _orientation) {
        getStateHelper().put(PropertyKeys.orientation, _orientation);
        handleAttribute("orientation", _orientation);
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

    @Override
    public boolean visitTree(final VisitContext context, final VisitCallback callback) {

        if (getVar() == null) {
            if (!isVisitable(context)) return false;

            final FacesContext facesContext = context.getFacesContext();
            pushComponentToEL(facesContext, null);

            try {
                final VisitResult result = context.invokeVisitCallback(this, callback);

                if (result == VisitResult.COMPLETE) return true;

                if (result == VisitResult.ACCEPT) {
                    final Iterator<UIComponent> kids = getFacetsAndChildren();

                    while (kids.hasNext()) {
                        final boolean done = kids.next().visitTree(context, callback);

                        if (done) return true;
                    }
                }
            } finally {
                popComponentFromEL(facesContext);
            }

            return false;
        } else {
            return super.visitTree(context, callback);
        }
    }
}