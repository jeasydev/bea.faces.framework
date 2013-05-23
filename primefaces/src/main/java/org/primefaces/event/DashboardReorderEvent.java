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

public class DashboardReorderEvent extends AjaxBehaviorEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String widgetId;

    private final Integer itemIndex;

    private final Integer columnIndex;

    private final Integer senderColumnIndex;

    public DashboardReorderEvent(final UIComponent component,
                                 final Behavior behavior,
                                 final String widgetId,
                                 final Integer itemIndex,
                                 final Integer columnIndex,
                                 final Integer senderColumnIndex) {
        super(component, behavior);
        this.widgetId = widgetId;
        this.itemIndex = itemIndex;
        this.columnIndex = columnIndex;
        this.senderColumnIndex = senderColumnIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public Integer getItemIndex() {
        return itemIndex;
    }

    public Integer getSenderColumnIndex() {
        return senderColumnIndex;
    }

    public String getWidgetId() {
        return widgetId;
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