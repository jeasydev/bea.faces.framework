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
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class LayoutUnit extends UIComponentBase {

    protected enum PropertyKeys {

        position,
        size,
        resizable,
        closable,
        collapsible,
        header,
        footer,
        minSize,
        maxSize,
        gutter,
        visible,
        collapsed,
        collapseSize,
        style,
        styleClass,
        effect,
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

    public static final String COMPONENT_TYPE = "org.primefaces.component.LayoutUnit";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.LayoutUnitRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public LayoutUnit() {
        setRendererType(LayoutUnit.DEFAULT_RENDERER);
    }

    public String getCollapseIcon() {
        return "ui-icon-triangle-1-" + getPosition().substring(0, 1);
    }

    public int getCollapseSize() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.collapseSize, 25);
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, null);
    }

    public java.lang.String getEffectSpeed() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effectSpeed, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return LayoutUnit.COMPONENT_FAMILY;
    }

    public java.lang.String getFooter() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.footer, null);
    }

    public int getGutter() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.gutter, 6);
    }

    public java.lang.String getHeader() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.header, null);
    }

    public int getMaxSize() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxSize, 0);
    }

    public int getMinSize() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minSize, 50);
    }

    public java.lang.String getPosition() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.position, null);
    }

    public java.lang.String getSize() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.size, "auto");
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(LayoutUnit.OPTIMIZED_PACKAGE)) {
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

    public boolean isClosable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.closable, false);
    }

    public boolean isCollapsed() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.collapsed, false);
    }

    public boolean isCollapsible() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.collapsible, false);
    }

    public boolean isNesting() {
        for (final UIComponent child : getChildren()) {
            if (child instanceof Layout) return true;
        }

        return false;
    }

    public boolean isResizable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.resizable, false);
    }

    public boolean isVisible() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.visible, true);
    }

    public void setClosable(final boolean _closable) {
        getStateHelper().put(PropertyKeys.closable, _closable);
        handleAttribute("closable", _closable);
    }

    public void setCollapsed(final boolean _collapsed) {
        getStateHelper().put(PropertyKeys.collapsed, _collapsed);
        handleAttribute("collapsed", _collapsed);
    }

    public void setCollapseSize(final int _collapseSize) {
        getStateHelper().put(PropertyKeys.collapseSize, _collapseSize);
        handleAttribute("collapseSize", _collapseSize);
    }

    public void setCollapsible(final boolean _collapsible) {
        getStateHelper().put(PropertyKeys.collapsible, _collapsible);
        handleAttribute("collapsible", _collapsible);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectSpeed(final java.lang.String _effectSpeed) {
        getStateHelper().put(PropertyKeys.effectSpeed, _effectSpeed);
        handleAttribute("effectSpeed", _effectSpeed);
    }

    public void setFooter(final java.lang.String _footer) {
        getStateHelper().put(PropertyKeys.footer, _footer);
        handleAttribute("footer", _footer);
    }

    public void setGutter(final int _gutter) {
        getStateHelper().put(PropertyKeys.gutter, _gutter);
        handleAttribute("gutter", _gutter);
    }

    public void setHeader(final java.lang.String _header) {
        getStateHelper().put(PropertyKeys.header, _header);
        handleAttribute("header", _header);
    }

    public void setMaxSize(final int _maxSize) {
        getStateHelper().put(PropertyKeys.maxSize, _maxSize);
        handleAttribute("maxSize", _maxSize);
    }

    public void setMinSize(final int _minSize) {
        getStateHelper().put(PropertyKeys.minSize, _minSize);
        handleAttribute("minSize", _minSize);
    }

    public void setPosition(final java.lang.String _position) {
        getStateHelper().put(PropertyKeys.position, _position);
        handleAttribute("position", _position);
    }

    public void setResizable(final boolean _resizable) {
        getStateHelper().put(PropertyKeys.resizable, _resizable);
        handleAttribute("resizable", _resizable);
    }

    public void setSize(final java.lang.String _size) {
        getStateHelper().put(PropertyKeys.size, _size);
        handleAttribute("size", _size);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setVisible(final boolean _visible) {
        getStateHelper().put(PropertyKeys.visible, _visible);
        handleAttribute("visible", _visible);
    }
}