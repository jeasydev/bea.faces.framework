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
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class Tab extends UIPanel {

    protected enum PropertyKeys {

        title,
        titleStyle,
        titleStyleClass,
        disabled,
        closable,
        titletip;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Tab";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public Tab() {
        setRendererType(null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Tab.COMPONENT_FAMILY;
    }

    public java.lang.String getTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public java.lang.String getTitleStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.titleStyle, null);
    }

    public java.lang.String getTitleStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.titleStyleClass, null);
    }

    public java.lang.String getTitletip() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.titletip, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Tab.OPTIMIZED_PACKAGE)) {
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

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public boolean isLoaded() {
        final Object value = getStateHelper().get("loaded");

        return (value == null) ? false : (Boolean) value;
    }

    public void setClosable(final boolean _closable) {
        getStateHelper().put(PropertyKeys.closable, _closable);
        handleAttribute("closable", _closable);
    }

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setLoaded(final boolean value) {
        getStateHelper().put("loaded", value);
    }

    public void setTitle(final java.lang.String _title) {
        getStateHelper().put(PropertyKeys.title, _title);
        handleAttribute("title", _title);
    }

    public void setTitleStyle(final java.lang.String _titleStyle) {
        getStateHelper().put(PropertyKeys.titleStyle, _titleStyle);
        handleAttribute("titleStyle", _titleStyle);
    }

    public void setTitleStyleClass(final java.lang.String _titleStyleClass) {
        getStateHelper().put(PropertyKeys.titleStyleClass, _titleStyleClass);
        handleAttribute("titleStyleClass", _titleStyleClass);
    }

    public void setTitletip(final java.lang.String _titletip) {
        getStateHelper().put(PropertyKeys.titletip, _titletip);
        handleAttribute("titletip", _titletip);
    }
}