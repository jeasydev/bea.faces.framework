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
package org.primefaces.model.map;

public class Rectangle extends Overlay {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private LatLngBounds bounds;

    private String strokeColor;

    private double strokeOpacity = 1.0;

    private int strokeWeight = 1;

    private String fillColor;

    private double fillOpacity = 1.0;

    public Rectangle(final LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public String getFillColor() {
        return fillColor;
    }

    public double getFillOpacity() {
        return fillOpacity;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public double getStrokeOpacity() {
        return strokeOpacity;
    }

    public int getStrokeWeight() {
        return strokeWeight;
    }

    public void setBounds(final LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public void setFillColor(final String fillColor) {
        this.fillColor = fillColor;
    }

    public void setFillOpacity(final double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public void setStrokeColor(final String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeOpacity(final double strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    public void setStrokeWeight(final int strokeWeight) {
        this.strokeWeight = strokeWeight;
    }
}