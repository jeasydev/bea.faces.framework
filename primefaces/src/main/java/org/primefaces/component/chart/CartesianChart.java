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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.convert.Converter;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

public class CartesianChart extends UIChart {

    /**
     * Finds the categories using first series
     * 
     * @return List of categories
     */
    public List<String> getCategories() {
        final CartesianChartModel model = (CartesianChartModel) getValue();
        final List<ChartSeries> series = model.getSeries();
        final List<String> categories = new ArrayList<String>();
        final Converter converter = getConverter();

        if (series.size() > 0) {
            final Map<Object, Number> firstSeriesData = series.get(0).getData();
            for (final Object key : firstSeriesData.keySet()) {
                if (key instanceof String) {
                    categories.add(key.toString());
                } else if (key instanceof Date) {
                    final String formattedDate = converter != null ? converter
                        .getAsString(getFacesContext(), this, key) : key.toString();

                    categories.add(formattedDate);
                } else {
                    break;
                }
            }
        }
        return categories;
    }
}
