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
package org.primefaces.component.chart.ohlc;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.chart.UIChart;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "charts/charts.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "charts/charts.js") })
public class OhlcChart extends UIChart implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        value,
        candleStick,
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

    public static final String COMPONENT_TYPE = "org.primefaces.component.chart.OhlcChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.chart.OhlcChartRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public OhlcChart() {
        setRendererType(OhlcChart.DEFAULT_RENDERER);
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
        return OhlcChart.COMPONENT_FAMILY;
    }

    @Override
    public org.primefaces.model.chart.OhlcChartModel getValue() {
        return (org.primefaces.model.chart.OhlcChartModel) getStateHelper().eval(PropertyKeys.value, null);
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
            if (cname != null && cname.startsWith(OhlcChart.OPTIMIZED_PACKAGE)) {
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

    public boolean isCandleStick() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.candleStick, false);
    }

    public boolean isShowDatatip() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showDatatip, true);
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

    public void setCandleStick(final boolean _candleStick) {
        getStateHelper().put(PropertyKeys.candleStick, _candleStick);
        handleAttribute("candleStick", _candleStick);
    }

    public void setDatatipFormat(final java.lang.String _datatipFormat) {
        getStateHelper().put(PropertyKeys.datatipFormat, _datatipFormat);
        handleAttribute("datatipFormat", _datatipFormat);
    }

    public void setShowDatatip(final boolean _showDatatip) {
        getStateHelper().put(PropertyKeys.showDatatip, _showDatatip);
        handleAttribute("showDatatip", _showDatatip);
    }

    public void setValue(final org.primefaces.model.chart.OhlcChartModel _value) {
        getStateHelper().put(PropertyKeys.value, _value);
        handleAttribute("value", _value);
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