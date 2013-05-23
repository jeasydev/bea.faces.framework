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
package org.primefaces.model.chart;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChartSeries implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String label;

    private Map<Object, Number> data = new LinkedHashMap<Object, Number>();

    public ChartSeries() {
    }

    public ChartSeries(final String label) {
        this.label = label;
    }

    public Map<Object, Number> getData() {
        return data;
    }

    public String getLabel() {
        return label;
    }

    public void set(final Object x, final Number y) {
        data.put(x, y);
    }

    public void setData(final Map<Object, Number> data) {
        this.data = data;
    }

    public void setLabel(final String label) {
        this.label = label;
    }
}
