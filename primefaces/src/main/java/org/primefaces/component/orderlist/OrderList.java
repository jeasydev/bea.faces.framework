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
package org.primefaces.component.orderlist;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class OrderList extends UIInput implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        var,
        itemLabel,
        itemValue,
        style,
        styleClass,
        disabled,
        effect,
        moveUpLabel,
        moveTopLabel,
        moveDownLabel,
        moveBottomLabel,
        controlsLocation;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.OrderList";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.OrderListRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String CONTAINER_CLASS = "ui-orderlist ui-widget";

    public static final String LIST_CLASS = "ui-widget-content ui-orderlist-list";

    public static final String CONTROLS_CLASS = "ui-orderlist-controls";
    public static final String CAPTION_CLASS = "ui-orderlist-caption ui-widget-header ui-corner-top";

    public static final String ITEM_CLASS = "ui-orderlist-item ui-corner-all";
    public static final String MOVE_UP_BUTTON_CLASS = "ui-orderlist-button-move-up";

    public static final String MOVE_DOWN_BUTTON_CLASS = "ui-orderlist-button-move-down";
    public static final String MOVE_TOP_BUTTON_CLASS = "ui-orderlist-button-move-top";

    public static final String MOVE_BOTTOM_BUTTON_CLASS = "ui-orderlist-button-move-bottom";
    public static final String MOVE_UP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-n";

    public static final String MOVE_DOWN_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-s";
    public static final String MOVE_TOP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-n";

    public static final String MOVE_BOTTOM_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-s";

    public OrderList() {
        setRendererType(OrderList.DEFAULT_RENDERER);
    }

    public java.lang.String getControlsLocation() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.controlsLocation, "left");
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return OrderList.COMPONENT_FAMILY;
    }

    public java.lang.String getItemLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemLabel, null);
    }

    public java.lang.Object getItemValue() {
        return getStateHelper().eval(PropertyKeys.itemValue, null);
    }

    public java.lang.String getMoveBottomLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveBottomLabel, "Move Bottom");
    }

    public java.lang.String getMoveDownLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveDownLabel, "Move Down");
    }

    public java.lang.String getMoveTopLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveTopLabel, "Move Top");
    }

    public java.lang.String getMoveUpLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveUpLabel, "Move Up");
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.var, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(OrderList.OPTIMIZED_PACKAGE)) {
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

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setControlsLocation(final java.lang.String _controlsLocation) {
        getStateHelper().put(PropertyKeys.controlsLocation, _controlsLocation);
        handleAttribute("controlsLocation", _controlsLocation);
    }

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setItemLabel(final java.lang.String _itemLabel) {
        getStateHelper().put(PropertyKeys.itemLabel, _itemLabel);
        handleAttribute("itemLabel", _itemLabel);
    }

    public void setItemValue(final java.lang.Object _itemValue) {
        getStateHelper().put(PropertyKeys.itemValue, _itemValue);
        handleAttribute("itemValue", _itemValue);
    }

    public void setMoveBottomLabel(final java.lang.String _moveBottomLabel) {
        getStateHelper().put(PropertyKeys.moveBottomLabel, _moveBottomLabel);
        handleAttribute("moveBottomLabel", _moveBottomLabel);
    }

    public void setMoveDownLabel(final java.lang.String _moveDownLabel) {
        getStateHelper().put(PropertyKeys.moveDownLabel, _moveDownLabel);
        handleAttribute("moveDownLabel", _moveDownLabel);
    }

    public void setMoveTopLabel(final java.lang.String _moveTopLabel) {
        getStateHelper().put(PropertyKeys.moveTopLabel, _moveTopLabel);
        handleAttribute("moveTopLabel", _moveTopLabel);
    }

    public void setMoveUpLabel(final java.lang.String _moveUpLabel) {
        getStateHelper().put(PropertyKeys.moveUpLabel, _moveUpLabel);
        handleAttribute("moveUpLabel", _moveUpLabel);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setVar(final java.lang.String _var) {
        getStateHelper().put(PropertyKeys.var, _var);
        handleAttribute("var", _var);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}