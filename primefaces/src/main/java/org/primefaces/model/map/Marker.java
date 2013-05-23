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

public class Marker extends Overlay {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean clickable = true;

    private String cursor;

    private boolean draggable = false;

    private boolean flat = false;

    private String icon;

    private LatLng latlng;

    private String shadow;

    private String title;

    private boolean visible = true;

    public Marker(final LatLng latlng) {
        this.latlng = latlng;
    }

    public Marker(final LatLng latlng, final String title) {
        this.latlng = latlng;
        this.title = title;
    }

    public Marker(final LatLng latlng, final String title, final Object data) {
        super(data);
        this.latlng = latlng;
        this.title = title;
    }

    public Marker(final LatLng latlng, final String title, final Object data, final String icon) {
        super(data);
        this.latlng = latlng;
        this.title = title;
        this.icon = icon;
    }

    public Marker(final LatLng latlng, final String title, final Object data, final String icon, final String shadow) {
        super(data);
        this.latlng = latlng;
        this.title = title;
        this.icon = icon;
        this.shadow = shadow;
    }

    public String getCursor() {
        return cursor;
    }

    public String getIcon() {
        return icon;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public String getShadow() {
        return shadow;
    }

    public String getTitle() {
        return title;
    }

    public boolean isClickable() {
        return clickable;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public boolean isFlat() {
        return flat;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setClickable(final boolean clickable) {
        this.clickable = clickable;
    }

    public void setCursor(final String cursor) {
        this.cursor = cursor;
    }

    public void setDraggable(final boolean draggable) {
        this.draggable = draggable;
    }

    public void setFlat(final boolean flat) {
        this.flat = flat;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public void setLatlng(final LatLng latlng) {
        this.latlng = latlng;
    }

    public void setShadow(final String shadow) {
        this.shadow = shadow;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}