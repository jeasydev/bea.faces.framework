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

public class OhlcChartSeries implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Object value;
    private double open;
    private double high;
    private double low;
    private double close;

    public OhlcChartSeries() {
    }

    public OhlcChartSeries(final Object value,
                           final double open,
                           final double high,
                           final double low,
                           final double close) {
        this.value = value;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getOpen() {
        return open;
    }

    public Object getValue() {
        return value;
    }

    public void setClose(final double close) {
        this.close = close;
    }

    public void setHigh(final double high) {
        this.high = high;
    }

    public void setLow(final double low) {
        this.low = low;
    }

    public void setOpen(final double open) {
        this.open = open;
    }

    public void setValue(final Object value) {
        this.value = value;
    }
}
