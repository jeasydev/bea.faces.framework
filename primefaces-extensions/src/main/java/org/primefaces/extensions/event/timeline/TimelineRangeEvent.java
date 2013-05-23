/*
 * Copyright 2011-2013 PrimeFaces Extensions.
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
 *
 * $Id: $
 */

package org.primefaces.extensions.event.timeline;

import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import org.primefaces.extensions.event.AbstractAjaxBehaviorEvent;

/**
 * Event which is triggered when a visible range of the Timeline is changing or
 * has been changed.
 * 
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since 0.7
 */
public class TimelineRangeEvent extends AbstractAjaxBehaviorEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** new start time of the visible range */
    private final Date startDate;

    /** new end time of the visible range */
    private final Date endDate;

    public TimelineRangeEvent(final UIComponent component,
                              final Behavior behavior,
                              final Date startDate,
                              final Date endDate) {
        super(component, behavior);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }
}
