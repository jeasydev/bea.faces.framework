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
package org.primefaces.component.spinner;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Spinner extends HtmlInputText implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.InputHolder {

    protected enum PropertyKeys {

        widgetVar,
        stepFactor,
        min,
        max,
        prefix,
        suffix;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Spinner";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.SpinnerRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String CONTAINER_CLASS = "ui-spinner ui-widget ui-corner-all";

    public final static String INPUT_CLASS = "ui-spinner-input ui-inputfield ui-state-default ui-corner-all";

    public final static String UP_BUTTON_CLASS = "ui-spinner-button ui-spinner-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only";
    public final static String DOWN_BUTTON_CLASS = "ui-spinner-button ui-spinner-down ui-corner-br ui-button ui-widget ui-state-default ui-button-text-only";

    public final static String UP_ICON_CLASS = "ui-icon ui-icon-triangle-1-n";
    public final static String DOWN_ICON_CLASS = "ui-icon ui-icon-triangle-1-s";

    public Spinner() {
        setRendererType(Spinner.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Spinner.COMPONENT_FAMILY;
    }

    @Override
    public String getInputClientId() {
        return this.getClientId(getFacesContext()) + "_input";
    }

    public double getMax() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.max, java.lang.Double.MAX_VALUE);
    }

    public double getMin() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.min, java.lang.Double.MIN_VALUE);
    }

    public java.lang.String getPrefix() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.prefix, null);
    }

    public double getStepFactor() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.stepFactor, 1.0);
    }

    public java.lang.String getSuffix() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.suffix, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Spinner.OPTIMIZED_PACKAGE)) {
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
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setMax(final double _max) {
        getStateHelper().put(PropertyKeys.max, _max);
        handleAttribute("max", _max);
    }

    public void setMin(final double _min) {
        getStateHelper().put(PropertyKeys.min, _min);
        handleAttribute("min", _min);
    }

    public void setPrefix(final java.lang.String _prefix) {
        getStateHelper().put(PropertyKeys.prefix, _prefix);
        handleAttribute("prefix", _prefix);
    }

    public void setStepFactor(final double _stepFactor) {
        getStateHelper().put(PropertyKeys.stepFactor, _stepFactor);
        handleAttribute("stepFactor", _stepFactor);
    }

    public void setSuffix(final java.lang.String _suffix) {
        getStateHelper().put(PropertyKeys.suffix, _suffix);
        handleAttribute("suffix", _suffix);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}