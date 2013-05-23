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

public class BubbleChartSeries implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int radius;
    private String label;

    public BubbleChartSeries() {
    }

    public BubbleChartSeries(final String label) {
        this.label = label;
    }

    public BubbleChartSeries(final String label, final int x, final int y, final int radius) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public String getLabel() {
        return label;
    }

    public int getRadius() {
        return radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setRadius(final int radius) {
        this.radius = radius;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public void setY(final int y) {
        this.y = y;
    }
}
