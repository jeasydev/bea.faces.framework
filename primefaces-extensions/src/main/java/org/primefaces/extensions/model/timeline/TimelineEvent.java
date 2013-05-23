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
 * $Id$
 */

package org.primefaces.extensions.model.timeline;

import java.io.Serializable;
import java.util.Date;

/**
 * Model class representing a timeline event.
 * 
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since 0.7 (reimplemented)
 */
public class TimelineEvent implements Serializable {

    private static final long serialVersionUID = 20130316L;

    /** any custom data object (required to show content of the event) */
    private Object data;

    /** event's start date (required) */
    private Date startDate;

    /** event's end date (optional) */
    private Date endDate;

    /**
     * is this event editable? (optional. if null, see the timeline's attribute
     * "editable"
     */
    private Boolean editable;

    /** group this event belongs to (optional) */
    private String group;

    /** any custom style class for this event in UI (optional) */
    private String styleClass;

    public TimelineEvent() {
    }

    public TimelineEvent(final Object data, final Date startDate) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
    }

    public TimelineEvent(final Object data, final Date startDate, final Boolean editable) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
        this.editable = editable;
    }

    public TimelineEvent(final Object data, final Date startDate, final Boolean editable, final String group) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
        this.editable = editable;
        this.group = group;
    }

    public TimelineEvent(final Object data,
                         final Date startDate,
                         final Boolean editable,
                         final String group,
                         final String styleClass) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
        this.editable = editable;
        this.group = group;
        this.styleClass = styleClass;
    }

    public TimelineEvent(final Object data, final Date startDate, final Date endDate) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TimelineEvent(final Object data, final Date startDate, final Date endDate, final Boolean editable) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
        this.editable = editable;
    }

    public TimelineEvent(final Object data,
                         final Date startDate,
                         final Date endDate,
                         final Boolean editable,
                         final String group) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
        this.editable = editable;
        this.group = group;
    }

    public TimelineEvent(final Object data,
                         final Date startDate,
                         final Date endDate,
                         final Boolean editable,
                         final String group,
                         final String styleClass) {
        checkStartDate(startDate);
        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
        this.editable = editable;
        this.group = group;
        this.styleClass = styleClass;
    }

    private void checkStartDate(final Date startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Event start date can not be null!");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TimelineEvent that = (TimelineEvent) o;

        if (data != null ? !data.equals(that.data) : that.data != null) {
            return false;
        }

        return true;
    }

    public Object getData() {
        return data;
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

    public String getStyleClass() {
        return styleClass;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    public Boolean isEditable() {
        return editable;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public void setEditable(final Boolean editable) {
        this.editable = editable;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public void setStyleClass(final String styleClass) {
        this.styleClass = styleClass;
    }

    @Override
    public String toString() {
        return "TimelineEvent{" + "data=" + data + ", startDate=" + startDate + ", endDate=" + endDate + ", editable="
            + editable + ", group='" + group + '\'' + ", styleClass='" + styleClass + '\'' + '}';
    }
}
