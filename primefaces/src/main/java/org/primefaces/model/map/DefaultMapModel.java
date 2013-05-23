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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DefaultMapModel implements MapModel, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final List<Marker> markers;

    private final List<Polyline> polylines;

    private final List<Polygon> polygons;

    private final List<Circle> circles;

    private final List<Rectangle> rectangles;

    private final static String MARKER_ID_PREFIX = "marker";

    private final static String POLYLINE_ID_PREFIX = "polyline_";

    private final static String POLYGON_ID_PREFIX = "polygon_";

    private final static String CIRCLE_ID_PREFIX = "circle_";

    private final static String RECTANGLE_ID_PREFIX = "rectangle_";

    public DefaultMapModel() {
        markers = new ArrayList<Marker>();
        polylines = new ArrayList<Polyline>();
        polygons = new ArrayList<Polygon>();
        circles = new ArrayList<Circle>();
        rectangles = new ArrayList<Rectangle>();
    }

    @Override
    public void addOverlay(final Overlay overlay) {
        if (overlay instanceof Marker) {
            overlay.setId(DefaultMapModel.MARKER_ID_PREFIX + UUID.randomUUID().toString());
            markers.add((Marker) overlay);
        } else if (overlay instanceof Polyline) {
            overlay.setId(DefaultMapModel.POLYLINE_ID_PREFIX + UUID.randomUUID().toString());
            polylines.add((Polyline) overlay);
        } else if (overlay instanceof Polygon) {
            overlay.setId(DefaultMapModel.POLYGON_ID_PREFIX + UUID.randomUUID().toString());
            polygons.add((Polygon) overlay);
        } else if (overlay instanceof Circle) {
            overlay.setId(DefaultMapModel.CIRCLE_ID_PREFIX + UUID.randomUUID().toString());
            circles.add((Circle) overlay);
        } else if (overlay instanceof Rectangle) {
            overlay.setId(DefaultMapModel.RECTANGLE_ID_PREFIX + UUID.randomUUID().toString());
            rectangles.add((Rectangle) overlay);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Overlay findOverlay(final String id) {
        List list = null;

        if (id.startsWith(DefaultMapModel.MARKER_ID_PREFIX))
            list = markers;
        else if (id.startsWith(DefaultMapModel.POLYLINE_ID_PREFIX))
            list = polylines;
        else if (id.startsWith(DefaultMapModel.POLYGON_ID_PREFIX))
            list = polygons;
        else if (id.startsWith(DefaultMapModel.CIRCLE_ID_PREFIX))
            list = circles;
        else if (id.startsWith(DefaultMapModel.RECTANGLE_ID_PREFIX)) list = rectangles;

        for (final Iterator iterator = list.iterator(); iterator.hasNext();) {
            final Overlay overlay = (Overlay) iterator.next();

            if (overlay.getId().equals(id)) return overlay;
        }

        return null;
    }

    @Override
    public List<Circle> getCircles() {
        return circles;
    }

    @Override
    public List<Marker> getMarkers() {
        return markers;
    }

    @Override
    public List<Polygon> getPolygons() {
        return polygons;
    }

    @Override
    public List<Polyline> getPolylines() {
        return polylines;
    }

    @Override
    public List<Rectangle> getRectangles() {
        return rectangles;
    }
}