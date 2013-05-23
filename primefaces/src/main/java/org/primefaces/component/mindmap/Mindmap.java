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
package org.primefaces.component.mindmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.MindmapNode;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "raphael/raphael.js"), @ResourceDependency(library = "primefaces", name = "mindmap/mindmap.js") })
public class Mindmap extends UIComponentBase implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        value,
        style,
        styleClass,
        effectSpeed;

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

    public static final String COMPONENT_TYPE = "org.primefaces.Mindmap";
    public static final String COMPONENT_FAMILY = "org.primefaces";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.MindmapRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String STYLE_CLASS = "ui-mindmap ui-widget ui-widget-content ui-corner-all";

    private static final Collection<String> EVENT_NAMES = Collections
        .unmodifiableCollection(Arrays.asList("select", "dblselect"));

    private MindmapNode selectedNode = null;

    public Mindmap() {
        setRendererType(Mindmap.DEFAULT_RENDERER);
    }

    protected MindmapNode findNode(MindmapNode searchRoot, final String rowKey) {
        final String[] paths = rowKey.split("_");

        if (paths.length == 0) return null;

        final int childIndex = Integer.parseInt(paths[0]);
        searchRoot = searchRoot.getChildren().get(childIndex);

        if (paths.length == 1) {
            return searchRoot;
        } else {
            final String relativeRowKey = rowKey.substring(rowKey.indexOf("_") + 1);

            return findNode(searchRoot, relativeRowKey);
        }
    }

    public int getEffectSpeed() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.effectSpeed, 300);
    }

    @Override
    public Collection<String> getEventNames() {
        return Mindmap.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Mindmap.COMPONENT_FAMILY;
    }

    public MindmapNode getSelectedNode() {
        return selectedNode;
    }

    public String getSelectedNodeKey(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().get(this.getClientId(context) + "_nodeKey");
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public org.primefaces.model.mindmap.MindmapNode getValue() {
        return (org.primefaces.model.mindmap.MindmapNode) getStateHelper().eval(PropertyKeys.value, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Mindmap.OPTIMIZED_PACKAGE)) {
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

    public boolean isNodeSelectRequest(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap()
            .containsKey(this.getClientId(context) + "_nodeKey");
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = this.getClientId(context);
        final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName.equals("select") || eventName.equals("dblselect")) {
            final String nodeKey = params.get(clientId + "_nodeKey");
            final MindmapNode node = nodeKey.equals("root") ? getValue() : findNode(getValue(), nodeKey);
            selectedNode = node;

            super.queueEvent(new SelectEvent(this, behaviorEvent.getBehavior(), node));
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

    public void setEffectSpeed(final int _effectSpeed) {
        getStateHelper().put(PropertyKeys.effectSpeed, _effectSpeed);
        handleAttribute("effectSpeed", _effectSpeed);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setValue(final org.primefaces.model.mindmap.MindmapNode _value) {
        getStateHelper().put(PropertyKeys.value, _value);
        handleAttribute("value", _value);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}