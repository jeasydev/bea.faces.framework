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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.chart.BaseChartRenderer;
import org.primefaces.component.chart.UIChart;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

public class BarChartRenderer extends BaseChartRenderer {

    protected void encodeData(final FacesContext context, final BarChart chart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final CartesianChartModel model = (CartesianChartModel) chart.getValue();
        final boolean horizontal = chart.getOrientation().equals("horizontal");
        final List<String> categories = chart.getCategories();

        // data
        writer.write(",data:[");
        for (final Iterator<ChartSeries> it = model.getSeries().iterator(); it.hasNext();) {
            final ChartSeries series = it.next();
            int i = 1;

            writer.write("[");
            for (final Iterator<Object> x = series.getData().keySet().iterator(); x.hasNext();) {
                final Number value = series.getData().get(x.next());
                final String valueToRender = value != null ? value.toString() : "null";

                if (horizontal) {
                    writer.write("[");
                    writer.write(valueToRender + "," + i);
                    writer.write("]");

                    i++;
                } else {
                    writer.write(valueToRender);
                }

                if (x.hasNext()) {
                    writer.write(",");
                }
            }
            writer.write("]");

            if (it.hasNext()) {
                writer.write(",");
            }
        }
        writer.write("]");

        // categories
        writer.write(",categories:[");
        for (final Iterator<String> it = categories.iterator(); it.hasNext();) {
            writer.write("'" + it.next() + "'");

            if (it.hasNext()) {
                writer.write(",");
            }
        }
        writer.write("]");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final BarChart chart = (BarChart) component;

        encodeMarkup(context, chart);
        encodeScript(context, chart);
    }

    protected void encodeOptions(final FacesContext context, final BarChart chart) throws IOException {
        super.encodeOptions(context, chart);

        final ResponseWriter writer = context.getResponseWriter();
        final CartesianChartModel model = (CartesianChartModel) chart.getValue();

        // axes
        writer.write(",axes:{");
        encodeAxis(context, "xaxis", chart.getXaxisLabel(), chart.getXaxisAngle(), Double.MIN_VALUE, Double.MAX_VALUE);
        encodeAxis(context, ",yaxis", chart.getYaxisLabel(), chart.getYaxisAngle(), Double.MIN_VALUE, Double.MAX_VALUE);
        writer.write("}");

        // series
        writer.write(",series:[");
        for (final Iterator<ChartSeries> it = model.getSeries().iterator(); it.hasNext();) {
            final ChartSeries series = it.next();

            writer.write("{");
            writer.write("label:'" + series.getLabel() + "'");
            writer.write("}");

            if (it.hasNext()) {
                writer.write(",");
            }
        }
        writer.write("]");

        // config
        writer.write(",orientation:'" + chart.getOrientation() + "'");
        writer.write(",barPadding:" + chart.getBarPadding());
        writer.write(",barMargin:" + chart.getBarMargin());

        if (chart.isStacked()) writer.write(",stackSeries:true");

        if (chart.isBreakOnNull()) writer.write(",breakOnNull:true");

        if (chart.isZoom()) writer.write(",zoom:true");

        if (chart.isAnimate()) writer.write(",animate:true");

        if (chart.isShowDatatip()) {
            writer.write(",datatip:true");
            if (chart.getDatatipFormat() != null) writer.write(",datatipFormat:'" + chart.getDatatipFormat() + "'");
        }

        // boundaries
        if (chart.getMin() != Double.MIN_VALUE) writer.write(",min:" + chart.getMin());
        if (chart.getMax() != Double.MAX_VALUE) writer.write(",max:" + chart.getMax());
    }

    protected void encodeScript(final FacesContext context, final UIChart uichart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final BarChart chart = (BarChart) uichart;
        final String clientId = chart.getClientId(context);

        startScript(writer, clientId);

        writer.write("$(function(){");

        writer.write("PrimeFaces.cw('BarChart','" + chart.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");

        encodeData(context, chart);

        encodeOptions(context, chart);

        encodeClientBehaviors(context, chart);

        writer.write("},'charts');});");

        endScript(writer);
    }
}