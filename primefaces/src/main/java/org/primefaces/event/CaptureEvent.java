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
package org.primefaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class CaptureEvent extends FacesEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final byte[] data;

    private final String rawData;

    public CaptureEvent(final UIComponent component, final byte[] data, final String rawData) {
        super(component);
        this.data = data;
        this.rawData = rawData;
    }

    public byte[] getData() {
        return data;
    }

    public String getRawData() {
        return rawData;
    }

    @Override
    public boolean isAppropriateListener(final FacesListener listener) {
        return false;
    }

    @Override
    public void processListener(final FacesListener listener) {
        throw new UnsupportedOperationException();
    }
}
