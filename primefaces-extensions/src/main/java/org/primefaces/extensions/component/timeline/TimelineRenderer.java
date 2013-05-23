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

package org.primefaces.extensions.component.timeline;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.DateUtils;
import org.primefaces.extensions.util.FastStringWriter;
import org.primefaces.renderkit.CoreRenderer;

/**
 * TimelineRenderer
 * 
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since 0.7 (reimplemented)
 */
public class TimelineRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // do nothing
    }

    // convert from UTC to locale date
    private String encodeDate(final Calendar calendar, final TimeZone localTimeZone, final Date utcDate) {
        return "new Date(" + DateUtils.toLocalDate(calendar, localTimeZone, utcDate) + ")";
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Timeline timeline = (Timeline) component;
        encodeMarkup(context, timeline);
        encodeScript(context, timeline);
    }

    public String encodeEvent(final FacesContext context,
                              final FastStringWriter fsw,
                              final FastStringWriter fswHtml,
                              final Timeline timeline,
                              final Calendar calendar,
                              final TimeZone timeZone,
                              final TimelineEvent event) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        fsw.write("{\"start\":" + encodeDate(calendar, timeZone, event.getStartDate()));

        if (event.getEndDate() != null) {
            fsw.write(",\"end\":" + encodeDate(calendar, timeZone, event.getEndDate()));
        } else {
            fsw.write(",\"end\":null");
        }

        if (event.isEditable() != null) {
            fsw.write(",\"editable\":" + event.isEditable());
        } else {
            fsw.write(",\"editable\":null");
        }

        if (event.getGroup() != null) {
            fsw.write(",\"group\":\"" + event.getGroup() + "\"");
        } else {
            fsw.write(",\"group\":null");
        }

        if (StringUtils.isNotBlank(event.getStyleClass())) {
            fsw.write(",\"className\":\"" + event.getStyleClass() + "\"");
        } else {
            fsw.write(",\"className\":null");
        }

        fsw.write(",\"content\":\"");
        if (timeline.getChildCount() > 0) {
            final Object data = event.getData();
            if (StringUtils.isNotBlank(timeline.getVar()) && data != null) {
                context.getExternalContext().getRequestMap().put(timeline.getVar(), data);
            }

            final ResponseWriter clonedWriter = writer.cloneWithWriter(fswHtml);
            context.setResponseWriter(clonedWriter);

            renderChildren(context, timeline);

            // restore writer
            context.setResponseWriter(writer);
            fsw.write(ComponentUtils.escapeHtmlTextInJson(fswHtml.toString()));
            fswHtml.reset();
        } else if (event.getData() != null) {
            fsw.write(event.getData().toString());
        }

        fsw.write("\"");
        fsw.write("}");

        final String eventJson = fsw.toString();
        fsw.reset();

        return eventJson;
    }

    protected void encodeMarkup(final FacesContext context, final Timeline timeline) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = timeline.getClientId(context);
        writer.startElement("div", timeline);

        writer.writeAttribute("id", clientId, "id");
        if (timeline.getStyle() != null) {
            writer.writeAttribute("style", timeline.getStyle(), "style");
        }

        if (timeline.getStyleClass() != null) {
            writer.writeAttribute("class", timeline.getStyleClass(), "styleClass");
        }

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final Timeline timeline) throws IOException {
        final TimelineModel model = timeline.getValue();
        if (model == null) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = timeline.getClientId(context);
        final TimeZone timeZone = ComponentUtils.resolveTimeZone(timeline.getTimeZone());
        final Calendar calendar = Calendar.getInstance(timeZone);
        final FastStringWriter fsw = new FastStringWriter();
        final FastStringWriter fswHtml = new FastStringWriter();

        startScript(writer, clientId);
        writer.write("$(function(){");
        writer.write("PrimeFacesExt.cw('Timeline','" + timeline.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",data:[");

        // encode events
        final List<TimelineEvent> events = model.getEvents();
        final int size = events != null ? events.size() : 0;
        for (int i = 0; i < size; i++) {
            writer.write(encodeEvent(context, fsw, fswHtml, timeline, calendar, timeZone, events.get(i)));
            if (i + 1 < size) {
                writer.write(",");
            }
        }

        writer.write("],opts:{");

        // encode options
        writer.write("height:'" + timeline.getHeight() + "'");
        writer.write(",minHeight:" + timeline.getMinHeight());
        writer.write(",width:'" + timeline.getWidth() + "'");
        writer.write(",responsive:" + timeline.isResponsive());
        writer.write(",axisOnTop:" + timeline.isAxisOnTop());
        writer.write(",dragAreaWidth:" + timeline.getDragAreaWidth());
        writer.write(",editable:" + timeline.isEditable());
        writer.write(",selectable:" + timeline.isSelectable());
        writer.write(",zoomable:" + timeline.isZoomable());
        writer.write(",moveable:" + timeline.isMoveable());

        if (timeline.getStart() != null) {
            writer.write(",start:" + encodeDate(calendar, timeZone, timeline.getStart()));
        }

        if (timeline.getEnd() != null) {
            writer.write(",end:" + encodeDate(calendar, timeZone, timeline.getEnd()));
        }

        if (timeline.getMin() != null) {
            writer.write(",min:" + encodeDate(calendar, timeZone, timeline.getMin()));
        }

        if (timeline.getMax() != null) {
            writer.write(",max:" + encodeDate(calendar, timeZone, timeline.getMax()));
        }

        writer.write(",zoomMin:" + timeline.getZoomMin());
        writer.write(",zoomMax:" + timeline.getZoomMax());
        writer.write(",eventMargin:" + timeline.getEventMargin());
        writer.write(",eventMarginAxis:" + timeline.getEventMarginAxis());
        writer.write(",style:'" + timeline.getEventStyle() + "'");
        writer.write(",groupsChangeable:" + timeline.isGroupsChangeable());
        writer.write(",groupsOnRight:" + timeline.isGroupsOnRight());

        if (timeline.getGroupsWidth() != null) {
            writer.write(",groupsWidth:'" + timeline.getGroupsWidth() + "'");
        }

        writer.write(",snapEvents:" + timeline.isSnapEvents());
        writer.write(",stackEvents:" + timeline.isStackEvents());
        writer.write(",showCurrentTime:" + timeline.isShowCurrentTime());
        writer.write(",showMajorLabels:" + timeline.isShowMajorLabels());
        writer.write(",showMinorLabels:" + timeline.isShowMinorLabels());
        writer.write(",showButtonNew:" + timeline.isShowButtonNew());
        writer.write(",showNavigation:" + timeline.isShowNavigation());

        if (timeline.getLocale() != null) {
            writer.write(",locale:'" + timeline.getLocale().toString() + "'");
        }

        writer.write("}");
        encodeClientBehaviors(context, timeline);
        writer.write("},true);});");
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
