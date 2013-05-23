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
package org.primefaces.component.media;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class Media extends UIComponentBase {

    protected enum PropertyKeys {

        value,
        player,
        width,
        height,
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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Media";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.MediaRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public Media() {
        setRendererType(Media.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Media.COMPONENT_FAMILY;
    }

    public java.lang.String getHeight() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.height, null);
    }

    public java.lang.String getPlayer() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.player, null);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.Object getValue() {
        return getStateHelper().eval(PropertyKeys.value, null);
    }

    public java.lang.String getWidth() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.width, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Media.OPTIMIZED_PACKAGE)) {
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

    public void setHeight(final java.lang.String _height) {
        getStateHelper().put(PropertyKeys.height, _height);
        handleAttribute("height", _height);
    }

    public void setPlayer(final java.lang.String _player) {
        getStateHelper().put(PropertyKeys.player, _player);
        handleAttribute("player", _player);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setValue(final java.lang.Object _value) {
        getStateHelper().put(PropertyKeys.value, _value);
        handleAttribute("value", _value);
    }

    public void setWidth(final java.lang.String _width) {
        getStateHelper().put(PropertyKeys.width, _width);
        handleAttribute("width", _width);
    }
}