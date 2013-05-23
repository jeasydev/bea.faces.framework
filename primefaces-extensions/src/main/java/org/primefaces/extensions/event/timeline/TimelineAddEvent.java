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
 * Event which is triggered when a new timeline event is added.
 * 
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since 0.7
 */
public class TimelineAddEvent extends AbstractAjaxBehaviorEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final Date startDate;
    private final Date endDate;
    private final String group;

    public TimelineAddEvent(final UIComponent component,
                            final Behavior behavior,
                            final Date startDate,
                            final Date endDate,
                            final String group) {
        super(component, behavior);
        this.startDate = startDate;
        this.endDate = endDate;
        this.group = group;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getGroup() {
        return group;
    }

    public Date getStartDate() {
        return startDate;
    }
}
