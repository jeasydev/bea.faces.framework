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
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

public class ItemSelectEvent extends AjaxBehaviorEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final int itemIndex;

    private final int seriesIndex;

    public ItemSelectEvent(final UIComponent source, final Behavior behavior, final int itemIndex, final int seriesIndex) {
        super(source, behavior);
        this.itemIndex = itemIndex;
        this.seriesIndex = seriesIndex;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public int getSeriesIndex() {
        return seriesIndex;
    }

    @Override
    public boolean isAppropriateListener(final FacesListener faceslistener) {
        return (faceslistener instanceof AjaxBehaviorListener);
    }

    @Override
    public void processListener(final FacesListener faceslistener) {
        ((AjaxBehaviorListener) faceslistener).processAjaxBehavior(this);
    }
}