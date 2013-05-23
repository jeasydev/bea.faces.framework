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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.chart.BaseChartRenderer;
import org.primefaces.component.chart.UIChart;
import org.primefaces.model.chart.OhlcChartModel;
import org.primefaces.model.chart.OhlcChartSeries;

public class OhlcChartRenderer extends BaseChartRenderer {

    protected void encodeData(final FacesContext context, final OhlcChart chart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final OhlcChartModel model = chart.getValue();
        final List<OhlcChartSeries> data = model.getData();
        final StringBuilder builder = new StringBuilder();

        writer.write(",data:[[");
        for (final Iterator<OhlcChartSeries> it = data.iterator(); it.hasNext();) {
            final OhlcChartSeries s = it.next();
            builder.append("[").append(s.getValue()).append(",").append(s.getOpen()).append(",").append(s.getHigh())
                .append(",").append(s.getLow()).append(",").append(s.getClose()).append("]");

            writer.write(builder.toString());
            builder.setLength(0);

            if (it.hasNext()) {
                writer.write(",");
            }
        }

        writer.write("]]");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final OhlcChart chart = (OhlcChart) component;

        encodeMarkup(context, chart);
        encodeScript(context, chart);
    }

    protected void encodeOptions(final FacesContext context, final OhlcChart chart) throws IOException {
        super.encodeOptions(context, chart);

        final ResponseWriter writer = context.getResponseWriter();

        // axes
        writer.write(",axes:{");
        encodeAxis(context, "xaxis", chart.getXaxisLabel(), chart.getXaxisAngle(), Double.MIN_VALUE, Double.MAX_VALUE);
        encodeAxis(context, ",yaxis", chart.getYaxisLabel(), chart.getYaxisAngle(), Double.MIN_VALUE, Double.MAX_VALUE);
        writer.write("}");

        if (chart.isCandleStick()) writer.write(",candleStick:true");

        if (chart.isZoom()) writer.write(",zoom:true");

        if (chart.isAnimate()) writer.write(",animate:true");

        if (chart.isShowDatatip()) {
            writer.write(",datatip:true");
            if (chart.getDatatipFormat() != null) writer.write(",datatipFormat:'" + chart.getDatatipFormat() + "'");
        }
    }

    protected void encodeScript(final FacesContext context, final UIChart uichart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final OhlcChart chart = (OhlcChart) uichart;
        final String clientId = chart.getClientId(context);

        startScript(writer, clientId);

        writer.write("$(function(){");

        writer.write("PrimeFaces.cw('OhlcChart','" + chart.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");

        encodeData(context, chart);

        encodeOptions(context, chart);

        encodeClientBehaviors(context, chart);

        writer.write("},'charts');});");

        endScript(writer);
    }
}