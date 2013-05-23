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

public class Overlay implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private Object data;

    private int zindex = Integer.MIN_VALUE;

    public Overlay() {

    }

    public Overlay(final Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Overlay other = (Overlay) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;

        return true;
    }

    public Object getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public int getZindex() {
        return zindex;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setZindex(final int zindex) {
        this.zindex = zindex;
    }
}