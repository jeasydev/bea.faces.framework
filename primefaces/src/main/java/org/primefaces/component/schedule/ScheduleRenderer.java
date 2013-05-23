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
package org.primefaces.component.schedule;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class ScheduleRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Schedule schedule = (Schedule) component;

        if (context.getExternalContext().getRequestParameterMap().containsKey(schedule.getClientId(context))) {
            encodeEvents(context, schedule);
        } else {
            encodeMarkup(context, schedule);
            encodeScript(context, schedule);
        }
    }

    protected void encodeEvents(final FacesContext context, final Schedule schedule) throws IOException {
        final String clientId = schedule.getClientId(context);
        final ScheduleModel model = schedule.getValue();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        if (model instanceof LazyScheduleModel) {
            final String startDateParam = params.get(clientId + "_start");
            final String endDateParam = params.get(clientId + "_end");

            final Date startDate = new Date(Long.valueOf(startDateParam));
            final Date endDate = new Date(Long.valueOf(endDateParam));

            final LazyScheduleModel lazyModel = ((LazyScheduleModel) model);
            lazyModel.clear(); // Clear old events
            lazyModel.loadEvents(startDate, endDate); // Lazy load events
        }

        encodeEventsAsJSON(context, schedule, model);
    }

    protected void encodeEventsAsJSON(final FacesContext context, final Schedule schedule, final ScheduleModel model)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(schedule.calculateTimeZone());

        writer.write("{");
        writer.write("\"events\" : [");

        if (model != null) {
            for (final Iterator<ScheduleEvent> iterator = model.getEvents().iterator(); iterator.hasNext();) {
                final ScheduleEvent event = iterator.next();
                calendar.setTime(event.getStartDate());
                final long startDateInMillis = calendar.getTimeInMillis();

                calendar.setTime(event.getEndDate());
                final long endDateInMillis = calendar.getTimeInMillis();

                writer.write("{");
                writer.write("\"id\": \"" + event.getId() + "\"");
                writer.write(",\"title\": \"" + escapeText(event.getTitle()) + "\"");
                writer.write(",\"start\": " + startDateInMillis);
                writer.write(",\"end\": " + endDateInMillis);
                writer.write(",\"allDay\":" + event.isAllDay());
                writer.write(",\"editable\":" + event.isEditable());
                if (event.getStyleClass() != null) writer.write(",\"className\":\"" + event.getStyleClass() + "\"");

                writer.write("}");

                if (iterator.hasNext()) writer.write(",");
            }
        }

        writer.write("]}");
    }

    protected void encodeMarkup(final FacesContext context, final Schedule schedule) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = schedule.getClientId(context);

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, null);
        if (schedule.getStyle() != null) writer.writeAttribute("style", schedule.getStyle(), "style");
        if (schedule.getStyleClass() != null) writer.writeAttribute("class", schedule.getStyleClass(), "style");

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + "_container", null);
        writer.endElement("div");

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final Schedule schedule) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = schedule.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Schedule", schedule.resolveWidgetVar(), clientId, "schedule", true).attr("defaultView",
                                                                                            schedule.getView())
            .attr("locale", schedule.calculateLocale(context).toString()).attr("offset",
                                                                               schedule.calculateTimeZone()
                                                                                   .getRawOffset());

        if (schedule.getInitialDate() != null) {
            final Calendar c = Calendar.getInstance();
            c.setTime((Date) schedule.getInitialDate());

            wb.attr("year", c.get(Calendar.YEAR)).attr("month", c.get(Calendar.MONTH)).attr("date",
                                                                                            c.get(Calendar.DATE));
        }

        if (schedule.isShowHeader()) {
            wb.append(",header:{left:'").append(schedule.getLeftHeaderTemplate()).append("'")
                .attr("center", schedule.getCenterHeaderTemplate()).attr("right", schedule.getRightHeaderTemplate())
                .append("}");
        } else {
            wb.attr("header", false);
        }

        wb.attr("allDaySlot", schedule.isAllDaySlot(), true).attr("slotMinutes", schedule.getSlotMinutes(), 30)
            .attr("firstHour", schedule.getFirstHour(), 6).attr("minTime", schedule.getMinTime(), null)
            .attr("maxTime", schedule.getMaxTime(), null).attr("aspectRatio", schedule.getAspectRatio(),
                                                               Double.MIN_VALUE).attr("weekends",
                                                                                      schedule.isShowWeekends(), true)
            .attr("disableDragging", !schedule.isDraggable(), false).attr("disableResizing", !schedule.isResizable(),
                                                                          false).attr("axisFormat",
                                                                                      schedule.getAxisFormat(), null)
            .attr("timeFormat", schedule.getTimeFormat(), null);

        encodeClientBehaviors(context, schedule, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}