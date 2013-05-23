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
package org.primefaces.component.chart.metergauge;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.chart.BaseChartRenderer;
import org.primefaces.component.chart.UIChart;
import org.primefaces.model.chart.MeterGaugeChartModel;

public class MeterGaugeChartRenderer extends BaseChartRenderer {

    protected void encodeData(final FacesContext context, final MeterGaugeChart chart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final MeterGaugeChartModel model = chart.getValue();

        writer.write(",data:[[" + model.getValue() + "]]");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final MeterGaugeChart chart = (MeterGaugeChart) component;

        encodeMarkup(context, chart);
        encodeScript(context, chart);
    }

    protected void encodeNumberList(final FacesContext context, final String name, final List<Number> values)
        throws IOException {
        if (values != null) {
            final ResponseWriter writer = context.getResponseWriter();

            writer.write("," + name + ":[");
            for (final Iterator<Number> it = values.iterator(); it.hasNext();) {
                final Number number = it.next();
                writer.write(number.toString());

                if (it.hasNext()) {
                    writer.write(",");
                }
            }
            writer.write("]");
        }
    }

    protected void encodeOptions(final FacesContext context, final MeterGaugeChart chart) throws IOException {
        super.encodeOptions(context, chart);

        final ResponseWriter writer = context.getResponseWriter();
        final MeterGaugeChartModel model = chart.getValue();
        final String label = chart.getLabel();

        encodeNumberList(context, "intervals", model.getIntervals());
        encodeNumberList(context, "ticks", model.getTicks());

        if (label != null) {
            writer.write(",label:'" + label + "'");
        }

        writer.write(",showTickLabels:" + chart.isShowTickLabels());
        writer.write(",labelHeightAdjust:" + chart.getLabelHeightAdjust());
        writer.write(",intervalOuterRadius:" + chart.getIntervalOuterRadius());

        if (chart.getMin() != Double.MIN_VALUE) writer.write(",min:" + chart.getMin());
        if (chart.getMax() != Double.MAX_VALUE) writer.write(",max:" + chart.getMax());
    }

    protected void encodeScript(final FacesContext context, final UIChart uichart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final MeterGaugeChart chart = (MeterGaugeChart) uichart;
        final String clientId = chart.getClientId(context);

        startScript(writer, clientId);

        writer.write("$(function(){");

        writer.write("PrimeFaces.cw('MeterGaugeChart','" + chart.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");

        encodeData(context, chart);

        encodeOptions(context, chart);

        encodeClientBehaviors(context, chart);

        writer.write("},'charts');});");

        endScript(writer);
    }
}