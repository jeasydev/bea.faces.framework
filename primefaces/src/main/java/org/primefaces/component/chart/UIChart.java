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
package org.primefaces.component.chart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.ItemSelectEvent;

public abstract class UIChart extends UIOutput implements ClientBehaviorHolder {

    protected enum PropertyKeys {
        widgetVar,
        styleClass,
        style,
        title,
        legendPosition,
        legendCols,
        legendRows,
        shadow,
        xaxisLabel,
        yaxisLabel,
        xaxisAngle,
        yaxisAngle,
        seriesColors,
        extender;

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

    private final static String DEFAULT_EVENT = "itemSelect";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList(UIChart.DEFAULT_EVENT));

    @Override
    public String getDefaultEventName() {
        return UIChart.DEFAULT_EVENT;
    }

    @Override
    public Collection<String> getEventNames() {
        return UIChart.EVENT_NAMES;
    }

    public java.lang.String getExtender() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.extender, null);
    }

    public Integer getLegendCols() {
        return (Integer) getStateHelper().eval(PropertyKeys.legendCols, 0);
    }

    public String getLegendPosition() {
        return (String) getStateHelper().eval(PropertyKeys.legendPosition, null);
    }

    public Integer getLegendRows() {
        return (Integer) getStateHelper().eval(PropertyKeys.legendRows, 0);
    }

    public java.lang.String getSeriesColors() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.seriesColors, null);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, "");
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public Integer getXaxisAngle() {
        return (Integer) getStateHelper().eval(PropertyKeys.xaxisAngle, 0);
    }

    public String getXaxisLabel() {
        return (String) getStateHelper().eval(PropertyKeys.xaxisLabel, null);
    }

    public Integer getYaxisAngle() {
        return (Integer) getStateHelper().eval(PropertyKeys.yaxisAngle, 0);
    }

    public String getYaxisLabel() {
        return (String) getStateHelper().eval(PropertyKeys.yaxisLabel, null);
    }

    public boolean isShadow() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.shadow, true);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        if (event instanceof AjaxBehaviorEvent) {
            final BehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            final Map<String, String> map = getFacesContext().getExternalContext().getRequestParameterMap();
            final int itemIndex = Integer.parseInt(map.get("itemIndex"));
            final int seriesIndex = Integer.parseInt(map.get("seriesIndex"));

            final ItemSelectEvent itemSelectEvent = new ItemSelectEvent(this,
                                                                        behaviorEvent.getBehavior(),
                                                                        itemIndex,
                                                                        seriesIndex);

            super.queueEvent(itemSelectEvent);
        }
    }

    public void setExtender(final java.lang.String _extender) {
        getStateHelper().put(PropertyKeys.extender, _extender);
    }

    public void setLegendCols(final Integer _legendCols) {
        getStateHelper().put(PropertyKeys.legendCols, _legendCols);
    }

    public void setLegendPosition(final String _legendPosition) {
        getStateHelper().put(PropertyKeys.legendPosition, _legendPosition);
    }

    public void setLegendRows(final Integer _legendRows) {
        getStateHelper().put(PropertyKeys.legendRows, _legendRows);
    }

    public void setSeriesColors(final java.lang.String _seriesColors) {
        getStateHelper().put(PropertyKeys.seriesColors, _seriesColors);
    }

    public void setShadow(final boolean _shadow) {
        getStateHelper().put(PropertyKeys.shadow, _shadow);
    }

    public void setStyle(final String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
    }

    public void setStyleClass(final String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
    }

    public void setTitle(final String _title) {
        getStateHelper().put(PropertyKeys.title, _title);
    }

    public void setWidgetVar(final String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
    }

    public void setXaxisAngle(final Integer _xAngle) {
        getStateHelper().put(PropertyKeys.xaxisAngle, _xAngle);
    }

    public void setXaxisLabel(final String _xLabel) {
        getStateHelper().put(PropertyKeys.xaxisLabel, _xLabel);
    }

    public void setYaxisAngle(final Integer _yAngle) {
        getStateHelper().put(PropertyKeys.yaxisAngle, _yAngle);
    }

    public void setYaxisLabel(final String _yLabel) {
        getStateHelper().put(PropertyKeys.yaxisLabel, _yLabel);
    }
}