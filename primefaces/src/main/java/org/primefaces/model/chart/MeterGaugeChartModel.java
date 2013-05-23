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
import java.util.ArrayList;
import java.util.List;

public class MeterGaugeChartModel extends ChartModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Number value;
    private List<Number> intervals;
    private List<Number> ticks;

    public MeterGaugeChartModel() {
        intervals = new ArrayList<Number>();
    }

    public MeterGaugeChartModel(final Number value, final List<Number> intervals) {
        this.value = value;
        this.intervals = intervals;
    }

    public MeterGaugeChartModel(final Number value, final List<Number> intervals, final List<Number> ticks) {
        this.value = value;
        this.intervals = intervals;
        this.ticks = ticks;
    }

    public void addInterval(final Number interval) {
        intervals.add(interval);
    }

    public List<Number> getIntervals() {
        return intervals;
    }

    public List<Number> getTicks() {
        return ticks;
    }

    public Number getValue() {
        return value;
    }

    public void setIntervals(final List<Number> intervals) {
        this.intervals = intervals;
    }

    public void setTicks(final List<Number> ticks) {
        this.ticks = ticks;
    }

    public void setValue(final Number value) {
        this.value = value;
    }

}
