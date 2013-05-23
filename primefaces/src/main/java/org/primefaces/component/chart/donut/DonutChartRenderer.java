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
package org.primefaces.component.chart.donut;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.chart.BaseChartRenderer;
import org.primefaces.component.chart.UIChart;

public class DonutChartRenderer extends BaseChartRenderer {

    protected void encodeData(final FacesContext context, final DonutChart chart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final List data = chart.getValue().getData();

        writer.write(",data:[");

        for (int i = 0; i < data.size(); i++) {
            if (i != 0) {
                writer.write(",");
            }

            writer.write("[");
            final Map<String, Number> map = (Map) data.get(i);

            for (final Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
                final String key = it.next();
                final Number value = map.get(key);

                writer.write("['" + key + "'," + value + "]");

                if (it.hasNext()) {
                    writer.write(",");
                }
            }
            writer.write("]");
        }

        writer.write("]");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final DonutChart chart = (DonutChart) component;

        encodeMarkup(context, chart);
        encodeScript(context, chart);
    }

    protected void encodeOptions(final FacesContext context, final DonutChart chart) throws IOException {
        super.encodeOptions(context, chart);

        final ResponseWriter writer = context.getResponseWriter();

        if (chart.getSliceMargin() != 0) writer.write(",sliceMargin:" + chart.getSliceMargin());

        if (chart.isFill() == false) writer.write(",fill:false");

        if (chart.isShowDataLabels()) writer.write(",showDataLabels:true");

        if (chart.getDataFormat() != null) writer.write(",dataFormat:'" + chart.getDataFormat() + "'");
    }

    protected void encodeScript(final FacesContext context, final UIChart uichart) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final DonutChart chart = (DonutChart) uichart;
        final String clientId = chart.getClientId(context);

        startScript(writer, clientId);

        writer.write("$(function(){");

        writer.write("PrimeFaces.cw('DonutChart','" + chart.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");

        encodeData(context, chart);

        encodeOptions(context, chart);

        encodeClientBehaviors(context, chart);

        writer.write("},'charts');});");

        endScript(writer);
    }
}