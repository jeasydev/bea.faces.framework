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
package org.primefaces.component.chart.line;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.chart.CartesianChart;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "charts/charts.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "charts/charts.js") })
public class LineChart extends CartesianChart implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        minY,
        maxY,
        minX,
        maxX,
        breakOnNull,
        fill,
        stacked,
        showMarkers,
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

    public static final String COMPONENT_TYPE = "org.primefaces.component.chart.LineChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.chart.LineChartRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public LineChart() {
        setRendererType(LineChart.DEFAULT_RENDERER);
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
        return LineChart.COMPONENT_FAMILY;
    }

    public double getMaxX() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.maxX, java.lang.Double.MAX_VALUE);
    }

    public double getMaxY() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.maxY, java.lang.Double.MAX_VALUE);
    }

    public double getMinX() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.minX, java.lang.Double.MIN_VALUE);
    }

    public double getMinY() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.minY, java.lang.Double.MIN_VALUE);
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
            if (cname != null && cname.startsWith(LineChart.OPTIMIZED_PACKAGE)) {
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

    public boolean isFill() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.fill, false);
    }

    public boolean isShowDatatip() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showDatatip, true);
    }

    public boolean isShowMarkers() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showMarkers, true);
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

    public void setBreakOnNull(final boolean _breakOnNull) {
        getStateHelper().put(PropertyKeys.breakOnNull, _breakOnNull);
        handleAttribute("breakOnNull", _breakOnNull);
    }

    public void setDatatipFormat(final java.lang.String _datatipFormat) {
        getStateHelper().put(PropertyKeys.datatipFormat, _datatipFormat);
        handleAttribute("datatipFormat", _datatipFormat);
    }

    public void setFill(final boolean _fill) {
        getStateHelper().put(PropertyKeys.fill, _fill);
        handleAttribute("fill", _fill);
    }

    public void setMaxX(final double _maxX) {
        getStateHelper().put(PropertyKeys.maxX, _maxX);
        handleAttribute("maxX", _maxX);
    }

    public void setMaxY(final double _maxY) {
        getStateHelper().put(PropertyKeys.maxY, _maxY);
        handleAttribute("maxY", _maxY);
    }

    public void setMinX(final double _minX) {
        getStateHelper().put(PropertyKeys.minX, _minX);
        handleAttribute("minX", _minX);
    }

    public void setMinY(final double _minY) {
        getStateHelper().put(PropertyKeys.minY, _minY);
        handleAttribute("minY", _minY);
    }

    public void setShowDatatip(final boolean _showDatatip) {
        getStateHelper().put(PropertyKeys.showDatatip, _showDatatip);
        handleAttribute("showDatatip", _showDatatip);
    }

    public void setShowMarkers(final boolean _showMarkers) {
        getStateHelper().put(PropertyKeys.showMarkers, _showMarkers);
        handleAttribute("showMarkers", _showMarkers);
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