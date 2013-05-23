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
package org.primefaces.model.tagcloud;

import java.io.Serializable;

public class DefaultTagCloudItem implements TagCloudItem, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String label;

    private String url;

    private int strength = 1;

    public DefaultTagCloudItem() {

    }

    public DefaultTagCloudItem(final String label, final int strength) {
        this.label = label;
        this.strength = strength;
    }

    public DefaultTagCloudItem(final String label, final String url, final int strength) {
        this(label, strength);
        this.url = url;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TagCloudItem other = (TagCloudItem) obj;
        if ((label == null) ? (other.getLabel() != null) : !label.equals(other.getLabel())) {
            return false;
        }
        if ((url == null) ? (other.getUrl() != null) : !url.equals(other.getUrl())) {
            return false;
        }
        if (strength != other.getStrength()) {
            return false;
        }
        return true;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (label != null ? label.hashCode() : 0);
        hash = 73 * hash + (url != null ? url.hashCode() : 0);
        hash = 73 * hash + strength;
        return hash;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setStrength(final int strength) {
        this.strength = strength;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
