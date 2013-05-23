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
package org.primefaces.component.growl;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIMessages;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Growl extends UIMessages implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.AutoUpdatable, org.primefaces.component.api.UINotification {

    protected enum PropertyKeys {

        widgetVar,
        sticky,
        life,
        autoUpdate,
        escape,
        severity;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Growl";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.GrowlRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public Growl() {
        setRendererType(Growl.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Growl.COMPONENT_FAMILY;
    }

    public int getLife() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.life, 6000);
    }

    @Override
    public java.lang.String getSeverity() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.severity, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Growl.OPTIMIZED_PACKAGE)) {
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

    public boolean isEscape() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.escape, true);
    }

    public boolean isSticky() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.sticky, false);
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

    public void setAutoUpdate(final boolean _autoUpdate) {
        getStateHelper().put(PropertyKeys.autoUpdate, _autoUpdate);
        handleAttribute("autoUpdate", _autoUpdate);
    }

    public void setEscape(final boolean _escape) {
        getStateHelper().put(PropertyKeys.escape, _escape);
        handleAttribute("escape", _escape);
    }

    public void setLife(final int _life) {
        getStateHelper().put(PropertyKeys.life, _life);
        handleAttribute("life", _life);
    }

    public void setSeverity(final java.lang.String _severity) {
        getStateHelper().put(PropertyKeys.severity, _severity);
        handleAttribute("severity", _severity);
    }

    public void setSticky(final boolean _sticky) {
        getStateHelper().put(PropertyKeys.sticky, _sticky);
        handleAttribute("sticky", _sticky);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}