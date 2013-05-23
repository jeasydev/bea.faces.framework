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
package org.primefaces.component.chart.bar;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.chart.CartesianChart;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "charts/charts.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "charts/charts.js") })
public class BarChart extends CartesianChart implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        barPadding,
        barMargin,
        orientation,
        stacked,
        min,
        max,
        breakOnNull,
        zoom,
        animate,
        showDatatip,
        datatipFormat;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.chart.BarChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.chart.BarChartRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public BarChart() {
        setRendererType(BarChart.DEFAULT_RENDERER);
    }

    public int getBarMargin() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.barMargin, 10);
    }

    public int getBarPadding() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.barPadding, 8);
    }

    public java.lang.String getDatatipFormat() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.datatipFormat, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return BarChart.COMPONENT_FAMILY;
    }

    public double getMax() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.max, java.lang.Double.MAX_VALUE);
    }

    public double getMin() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.min, java.lang.Double.MIN_VALUE);
    }

    public java.lang.String getOrientation() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.orientation, "vertical");
    }

    @Override
    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(BarChart.OPTIMIZED_PACKAGE)) {
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

    public boolean isAnimate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.animate, false);
    }

    public boolean isBreakOnNull() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.breakOnNull, false);
    }

    public boolean isShowDatatip() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showDatatip, true);
    }

    public boolean isStacked() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.stacked, false);
    }

    public boolean isZoom() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.zoom, false);
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

    public void setAnimate(final boolean _animate) {
        getStateHelper().put(PropertyKeys.animate, _animate);
        handleAttribute("animate", _animate);
    }

    public void setBarMargin(final int _barMargin) {
        getStateHelper().put(PropertyKeys.barMargin, _barMargin);
        handleAttribute("barMargin", _barMargin);
    }

    public void setBarPadding(final int _barPadding) {
        getStateHelper().put(PropertyKeys.barPadding, _barPadding);
        handleAttribute("barPadding", _barPadding);
    }

    public void setBreakOnNull(final boolean _breakOnNull) {
        getStateHelper().put(PropertyKeys.breakOnNull, _breakOnNull);
        handleAttribute("breakOnNull", _breakOnNull);
    }

    public void setDatatipFormat(final java.lang.String _datatipFormat) {
        getStateHelper().put(PropertyKeys.datatipFormat, _datatipFormat);
        handleAttribute("datatipFormat", _datatipFormat);
    }

    public void setMax(final double _max) {
        getStateHelper().put(PropertyKeys.max, _max);
        handleAttribute("max", _max);
    }

    public void setMin(final double _min) {
        getStateHelper().put(PropertyKeys.min, _min);
        handleAttribute("min", _min);
    }

    public void setOrientation(final java.lang.String _orientation) {
        getStateHelper().put(PropertyKeys.orientation, _orientation);
        handleAttribute("orientation", _orientation);
    }

    public void setShowDatatip(final boolean _showDatatip) {
        getStateHelper().put(PropertyKeys.showDatatip, _showDatatip);
        handleAttribute("showDatatip", _showDatatip);
    }

    public void setStacked(final boolean _stacked) {
        getStateHelper().put(PropertyKeys.stacked, _stacked);
        handleAttribute("stacked", _stacked);
    }

    @Override
    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }

    public void setZoom(final boolean _zoom) {
        getStateHelper().put(PropertyKeys.zoom, _zoom);
        handleAttribute("zoom", _zoom);
    }
}