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
import java.util.LinkedList;
import java.util.List;

public class DefaultDashboardColumn implements DashboardColumn, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final List<String> widgets;

    public DefaultDashboardColumn() {
        widgets = new LinkedList<String>();
    }

    @Override
    public void addWidget(final int index, final String widgetId) {
        widgets.add(index, widgetId);
    }

    @Override
    public void addWidget(final String widgetId) {
        widgets.add(widgetId);
    }

    @Override
    public String getWidget(final int index) {
        return widgets.get(index);
    }

    @Override
    public int getWidgetCount() {
        return widgets.size();
    }

    @Override
    public List<String> getWidgets() {
        return widgets;
    }

    @Override
    public void removeWidget(final String widgetId) {
        widgets.remove(widgetId);
    }

    @Override
    public void reorderWidget(final int index, final String widgetId) {
        widgets.remove(widgetId);
        widgets.add(index, widgetId);
    }
}
