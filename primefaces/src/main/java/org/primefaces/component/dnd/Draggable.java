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
package org.primefaces.component.dnd;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Draggable extends UIComponentBase implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        proxy,
        dragOnly,
        forValue("for"),
        disabled,
        axis,
        containment,
        helper,
        revert,
        snap,
        snapMode,
        snapTolerance,
        zindex,
        handle,
        opacity,
        stack,
        grid,
        scope,
        cursor,
        dashboard;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Draggable";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.DraggableRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public Draggable() {
        setRendererType(Draggable.DEFAULT_RENDERER);
    }

    public java.lang.String getAxis() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.axis, null);
    }

    public java.lang.String getContainment() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.containment, null);
    }

    public java.lang.String getCursor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.cursor, "crosshair");
    }

    public java.lang.String getDashboard() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dashboard, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Draggable.COMPONENT_FAMILY;
    }

    public java.lang.String getFor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public java.lang.String getGrid() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.grid, null);
    }

    public java.lang.String getHandle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.handle, null);
    }

    public java.lang.String getHelper() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.helper, null);
    }

    public double getOpacity() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.opacity, 1.0);
    }

    public java.lang.String getScope() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.scope, null);
    }

    public java.lang.String getSnapMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.snapMode, null);
    }

    public int getSnapTolerance() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.snapTolerance, 20);
    }

    public java.lang.String getStack() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.stack, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public int getZindex() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.zindex, -1);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Draggable.OPTIMIZED_PACKAGE)) {
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

    public boolean isDragOnly() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.dragOnly, false);
    }

    public boolean isProxy() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.proxy, false);
    }

    public boolean isRevert() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.revert, false);
    }

    public boolean isSnap() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.snap, false);
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

    public void setAxis(final java.lang.String _axis) {
        getStateHelper().put(PropertyKeys.axis, _axis);
        handleAttribute("axis", _axis);
    }

    public void setContainment(final java.lang.String _containment) {
        getStateHelper().put(PropertyKeys.containment, _containment);
        handleAttribute("containment", _containment);
    }

    public void setCursor(final java.lang.String _cursor) {
        getStateHelper().put(PropertyKeys.cursor, _cursor);
        handleAttribute("cursor", _cursor);
    }

    public void setDashboard(final java.lang.String _dashboard) {
        getStateHelper().put(PropertyKeys.dashboard, _dashboard);
        handleAttribute("dashboard", _dashboard);
    }

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setDragOnly(final boolean _dragOnly) {
        getStateHelper().put(PropertyKeys.dragOnly, _dragOnly);
        handleAttribute("dragOnly", _dragOnly);
    }

    public void setFor(final java.lang.String _for) {
        getStateHelper().put(PropertyKeys.forValue, _for);
        handleAttribute("forValue", _for);
    }

    public void setGrid(final java.lang.String _grid) {
        getStateHelper().put(PropertyKeys.grid, _grid);
        handleAttribute("grid", _grid);
    }

    public void setHandle(final java.lang.String _handle) {
        getStateHelper().put(PropertyKeys.handle, _handle);
        handleAttribute("handle", _handle);
    }

    public void setHelper(final java.lang.String _helper) {
        getStateHelper().put(PropertyKeys.helper, _helper);
        handleAttribute("helper", _helper);
    }

    public void setOpacity(final double _opacity) {
        getStateHelper().put(PropertyKeys.opacity, _opacity);
        handleAttribute("opacity", _opacity);
    }

    public void setProxy(final boolean _proxy) {
        getStateHelper().put(PropertyKeys.proxy, _proxy);
        handleAttribute("proxy", _proxy);
    }

    public void setRevert(final boolean _revert) {
        getStateHelper().put(PropertyKeys.revert, _revert);
        handleAttribute("revert", _revert);
    }

    public void setScope(final java.lang.String _scope) {
        getStateHelper().put(PropertyKeys.scope, _scope);
        handleAttribute("scope", _scope);
    }

    public void setSnap(final boolean _snap) {
        getStateHelper().put(PropertyKeys.snap, _snap);
        handleAttribute("snap", _snap);
    }

    public void setSnapMode(final java.lang.String _snapMode) {
        getStateHelper().put(PropertyKeys.snapMode, _snapMode);
        handleAttribute("snapMode", _snapMode);
    }

    public void setSnapTolerance(final int _snapTolerance) {
        getStateHelper().put(PropertyKeys.snapTolerance, _snapTolerance);
        handleAttribute("snapTolerance", _snapTolerance);
    }

    public void setStack(final java.lang.String _stack) {
        getStateHelper().put(PropertyKeys.stack, _stack);
        handleAttribute("stack", _stack);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }

    public void setZindex(final int _zindex) {
        getStateHelper().put(PropertyKeys.zindex, _zindex);
        handleAttribute("zindex", _zindex);
    }
}