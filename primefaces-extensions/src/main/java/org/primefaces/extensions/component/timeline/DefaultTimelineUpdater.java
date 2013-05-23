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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.extensions.util.FastStringWriter;

/**
 * Default implementation of the {@link TimelineUpdater}.
 * 
 * @author Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since 0.7
 */
public class DefaultTimelineUpdater extends TimelineUpdater implements PhaseListener {

    /**
     * CrudOperation
     * 
     * @author Oleg Varaksin / last modified by $Author: $
     * @version $Revision: 1.0 $
     */
    enum CrudOperation {

        ADD,
        UPDATE,
        DELETE,
        CLEAR
    }

    class CrudOperationData implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private final CrudOperation crudOperation;
        private TimelineEvent event;
        private int index;

        CrudOperationData(final CrudOperation crudOperation) {
            this.crudOperation = crudOperation;
        }

        CrudOperationData(final CrudOperation crudOperation, final int index) {
            this.crudOperation = crudOperation;
            this.index = index;
        }

        CrudOperationData(final CrudOperation crudOperation, final TimelineEvent event) {
            this.crudOperation = crudOperation;
            this.event = event;
        }

        CrudOperationData(final CrudOperation crudOperation, final TimelineEvent event, final int index) {
            this.crudOperation = crudOperation;
            this.event = event;
            this.index = index;
        }

        public CrudOperation getCrudOperation() {
            return crudOperation;
        }

        public TimelineEvent getEvent() {
            return event;
        }

        public int getIndex() {
            return index;
        }
    }

    private static final long serialVersionUID = 20130317L;
    private static final Logger LOG = Logger.getLogger(DefaultTimelineUpdater.class.getName());

    private String widgetVar;

    private List<CrudOperationData> crudOperationDatas;

    @Override
    public void add(final TimelineEvent event) {
        if (event == null) {
            return;
        }

        checkCrudOperationDataList();
        crudOperationDatas.add(new CrudOperationData(CrudOperation.ADD, event));
    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        // NOOP.
    }

    @Override
    public void beforePhase(final PhaseEvent event) {
        if (crudOperationDatas == null) {
            return;
        }

        final FacesContext fc = event.getFacesContext();
        final StringBuilder sb = new StringBuilder();
        final FastStringWriter fsw = new FastStringWriter();
        final FastStringWriter fswHtml = new FastStringWriter();

        final Timeline timeline = (Timeline) fc.getViewRoot().findComponent(id);
        final TimelineRenderer timelineRenderer = (TimelineRenderer) fc.getRenderKit()
            .getRenderer(Timeline.COMPONENT_FAMILY, Timeline.DEFAULT_RENDERER);

        final TimeZone timeZone = ComponentUtils.resolveTimeZone(timeline.getTimeZone());
        final Calendar calendar = Calendar.getInstance(timeZone);

        try {
            for (final CrudOperationData crudOperationData : crudOperationDatas) {
                switch (crudOperationData.getCrudOperation()) {
                    case ADD:

                        sb.append(";");
                        sb.append(widgetVar);
                        sb.append(".addEvent(");
                        sb.append(timelineRenderer.encodeEvent(fc, fsw, fswHtml, timeline, calendar, timeZone,
                                                               crudOperationData.getEvent()));
                        sb.append(")");
                        break;

                    case UPDATE:

                        sb.append(";");
                        sb.append(widgetVar);
                        sb.append(".changeEvent(");
                        sb.append(crudOperationData.getIndex());
                        sb.append(",");
                        sb.append(timelineRenderer.encodeEvent(fc, fsw, fswHtml, timeline, calendar, timeZone,
                                                               crudOperationData.getEvent()));
                        sb.append(")");
                        break;

                    case DELETE:

                        sb.append(";");
                        sb.append(widgetVar);
                        sb.append(".deleteEvent(");
                        sb.append(crudOperationData.getIndex());
                        sb.append(")");
                        break;

                    case CLEAR:

                        sb.append(";");
                        sb.append(widgetVar);
                        sb.append(".deleteAllEvents()");
                        break;
                }
            }

            // execute JS script
            RequestContext.getCurrentInstance().execute(sb.toString());
        } catch (final IOException e) {
            DefaultTimelineUpdater.LOG.log(Level.WARNING, "Timeline with id " + id
                + " could not be updated, at least one CRUD operation failed", e);
        }
    }

    private void checkCrudOperationDataList() {
        if (crudOperationDatas == null) {
            crudOperationDatas = new ArrayList<CrudOperationData>();
        }
    }

    @Override
    public void clear() {
        checkCrudOperationDataList();
        crudOperationDatas.add(new CrudOperationData(CrudOperation.CLEAR));
    }

    @Override
    public void delete(final int index) {
        checkCrudOperationDataList();
        crudOperationDatas.add(new CrudOperationData(CrudOperation.DELETE, index));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DefaultTimelineUpdater that = (DefaultTimelineUpdater) o;

        if (widgetVar != null ? !widgetVar.equals(that.widgetVar) : that.widgetVar != null) {
            return false;
        }

        return true;
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    @Override
    public int hashCode() {
        return widgetVar != null ? widgetVar.hashCode() : 0;
    }

    public void setWidgetVar(final String widgetVar) {
        this.widgetVar = widgetVar;
    }

    @Override
    public void update(final TimelineEvent event, final int index) {
        if (event == null) {
            return;
        }

        checkCrudOperationDataList();
        crudOperationDatas.add(new CrudOperationData(CrudOperation.UPDATE, event, index));
    }
}
