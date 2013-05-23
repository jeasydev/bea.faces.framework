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
 */

package org.primefaces.extensions.util;

import java.io.Serializable;
import java.util.Comparator;
import org.primefaces.extensions.model.timeline.TimelineEvent;

/**
 * Comparator for {@link TimelineEvent} to order their according start / end
 * dates.
 * 
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 */
public class TimelineEventComparator implements Comparator<TimelineEvent>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final TimelineEvent a, final TimelineEvent b) {
        if (a.getStartDate().equals(b.getStartDate())) {
            if (a.getEndDate() == null && b.getEndDate() == null) {
                return 0;
            } else if (a.getEndDate() == null) {
                return -1;
            } else if (b.getEndDate() == null) {
                return 1;
            } else {
                return (a.getEndDate().before(b.getEndDate()) ? -1 : 1);
            }
        } else {
            return (a.getStartDate().before(b.getStartDate()) ? -1 : 1);
        }
    }
}
