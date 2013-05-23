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

import java.util.ArrayList;
import java.util.List;

public class Polyline extends Overlay {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<LatLng> paths;

    private String strokeColor;

    private double strokeOpacity = 1.0;

    private int strokeWeight = 1;

    public Polyline() {
        paths = new ArrayList<LatLng>();
    }

    public Polyline(final List<LatLng> paths) {
        this.paths = paths;
    }

    public Polyline(final List<LatLng> paths, final Object data) {
        super(data);
        this.paths = paths;
    }

    public List<LatLng> getPaths() {
        return paths;
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

    public void setPaths(final List<LatLng> paths) {
        this.paths = paths;
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