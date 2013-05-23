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
package org.primefaces.component.outputpanel;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class OutputPanel extends UIPanel implements org.primefaces.component.api.AutoUpdatable {

    protected enum PropertyKeys {

        style,
        styleClass,
        layout,
        autoUpdate;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.OutputPanel";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.OutputPanelRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public OutputPanel() {
        setRendererType(OutputPanel.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return OutputPanel.COMPONENT_FAMILY;
    }

    public java.lang.String getLayout() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.layout, "inline");
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
            if (cname != null && cname.startsWith(OutputPanel.OPTIMIZED_PACKAGE)) {
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

    @Override
    public boolean isAutoUpdate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoUpdate, false);
    }

    public void setAutoUpdate(final boolean _autoUpdate) {
        getStateHelper().put(PropertyKeys.autoUpdate, _autoUpdate);
        handleAttribute("autoUpdate", _autoUpdate);
    }

    public void setLayout(final java.lang.String _layout) {
        getStateHelper().put(PropertyKeys.layout, _layout);
        handleAttribute("layout", _layout);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }
}