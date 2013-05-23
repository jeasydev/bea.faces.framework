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
package org.primefaces.model;

import java.io.Serializable;
import java.util.Date;

public class DefaultScheduleEvent implements ScheduleEvent, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    private Date startDate;

    private Date endDate;

    private boolean allDay = false;

    private String styleClass;

    private Object data;

    private boolean editable = true;

    public DefaultScheduleEvent() {
    }

    public DefaultScheduleEvent(final String title, final Date start, final Date end) {
        this.title = title;
        startDate = start;
        endDate = end;
    }

    public DefaultScheduleEvent(final String title, final Date start, final Date end, final boolean allDay) {
        this.title = title;
        startDate = start;
        endDate = end;
        this.allDay = allDay;
    }

    public DefaultScheduleEvent(final String title, final Date start, final Date end, final Object data) {
        this.title = title;
        startDate = start;
        endDate = end;
        this.data = data;
    }

    public DefaultScheduleEvent(final String title, final Date start, final Date end, final String styleClass) {
        this.title = title;
        startDate = start;
        endDate = end;
        this.styleClass = styleClass;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DefaultScheduleEvent other = (DefaultScheduleEvent) obj;
        if ((title == null) ? (other.title != null) : !title.equals(other.title)) {
            return false;
        }
        if (startDate != other.startDate && (startDate == null || !startDate.equals(other.startDate))) {
            return false;
        }
        if (endDate != other.endDate && (endDate == null || !endDate.equals(other.endDate))) {
            return false;
        }
        return true;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public String getStyleClass() {
        return styleClass;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (title != null ? title.hashCode() : 0);
        hash = 61 * hash + (startDate != null ? startDate.hashCode() : 0);
        hash = 61 * hash + (endDate != null ? endDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean isAllDay() {
        return allDay;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    public void setAllDay(final boolean allDay) {
        this.allDay = allDay;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public void setEditable(final boolean editable) {
        this.editable = editable;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public void setStyleClass(final String styleClass) {
        this.styleClass = styleClass;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "DefaultScheduleEvent{title=" + title + ",startDate=" + startDate + ",endDate=" + endDate + "}";
    }
}