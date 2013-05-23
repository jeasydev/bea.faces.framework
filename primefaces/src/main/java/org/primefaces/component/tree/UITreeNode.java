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
package org.primefaces.component.tree;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIColumn;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class UITreeNode extends UIColumn {

    protected enum PropertyKeys {

        type,
        styleClass,
        icon,
        expandedIcon,
        collapsedIcon;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.UITreeNode";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public UITreeNode() {
        setRendererType(null);
    }

    public java.lang.String getCollapsedIcon() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.collapsedIcon, null);
    }

    public java.lang.String getExpandedIcon() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.expandedIcon, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return UITreeNode.COMPONENT_FAMILY;
    }

    public java.lang.String getIcon() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.icon, null);
    }

    public String getIconToRender(final boolean expanded) {
        final String icon = getIcon();
        if (icon != null) {
            return icon;
        } else {
            final String expandedIcon = getExpandedIcon();
            final String collapsedIcon = getCollapsedIcon();

            if (expandedIcon != null && collapsedIcon != null) {
                return expanded ? expandedIcon : collapsedIcon;
            }
        }

        return null;
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getType() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.type, "default");
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(UITreeNode.OPTIMIZED_PACKAGE)) {
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

    public void setCollapsedIcon(final java.lang.String _collapsedIcon) {
        getStateHelper().put(PropertyKeys.collapsedIcon, _collapsedIcon);
        handleAttribute("collapsedIcon", _collapsedIcon);
    }

    public void setExpandedIcon(final java.lang.String _expandedIcon) {
        getStateHelper().put(PropertyKeys.expandedIcon, _expandedIcon);
        handleAttribute("expandedIcon", _expandedIcon);
    }

    public void setIcon(final java.lang.String _icon) {
        getStateHelper().put(PropertyKeys.icon, _icon);
        handleAttribute("icon", _icon);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setType(final java.lang.String _type) {
        getStateHelper().put(PropertyKeys.type, _type);
        handleAttribute("type", _type);
    }
}