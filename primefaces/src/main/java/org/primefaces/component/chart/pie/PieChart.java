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
package org.primefaces.component.chart.pie;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.chart.UIChart;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "charts/charts.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "charts/charts.js") })
public class PieChart extends UIChart implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        diameter,
        sliceMargin,
        fill,
        showDataLabels,
        dataFormat;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.chart.PieChart";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.chart.PieChartRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public PieChart() {
        setRendererType(PieChart.DEFAULT_RENDERER);
    }

    public java.lang.String getDataFormat() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dataFormat, null);
    }

    public int getDiameter() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.diameter, java.lang.Integer.MIN_VALUE);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return PieChart.COMPONENT_FAMILY;
    }

    public int getSliceMargin() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.sliceMargin, 0);
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
            if (cname != null && cname.startsWith(PieChart.OPTIMIZED_PACKAGE)) {
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

    public boolean isFill() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.fill, true);
    }

    public boolean isShowDataLabels() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showDataLabels, false);
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

    public void setDataFormat(final java.lang.String _dataFormat) {
        getStateHelper().put(PropertyKeys.dataFormat, _dataFormat);
        handleAttribute("dataFormat", _dataFormat);
    }

    public void setDiameter(final int _diameter) {
        getStateHelper().put(PropertyKeys.diameter, _diameter);
        handleAttribute("diameter", _diameter);
    }

    public void setFill(final boolean _fill) {
        getStateHelper().put(PropertyKeys.fill, _fill);
        handleAttribute("fill", _fill);
    }

    public void setShowDataLabels(final boolean _showDataLabels) {
        getStateHelper().put(PropertyKeys.showDataLabels, _showDataLabels);
        handleAttribute("showDataLabels", _showDataLabels);
    }

    public void setSliceMargin(final int _sliceMargin) {
        getStateHelper().put(PropertyKeys.sliceMargin, _sliceMargin);
        handleAttribute("sliceMargin", _sliceMargin);
    }

    @Override
    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}