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
package org.primefaces.component.datatable.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.columns.Columns;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;

public class DraggableColumnsFeature implements DataTableFeature {

    @Override
    public void decode(final FacesContext context, final DataTable table) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String[] order = params.get(table.getClientId(context) + "_columnOrder").split(",");
        final List orderedColumns = new ArrayList();
        final String separator = String.valueOf(UINamingContainer.getSeparatorChar(context));

        for (final String columnId : order) {

            for (final UIComponent child : table.getChildren()) {
                if (child instanceof Column && child.getClientId(context).equals(columnId)) {
                    orderedColumns.add(child);
                    break;
                } else if (child instanceof Columns && columnId.startsWith(child.getClientId(context))) {
                    final String[] ids = columnId.split(separator);
                    final int index = Integer.parseInt(ids[ids.length - 1]);

                    orderedColumns.add(new DynamicColumn(index, (Columns) child));
                    break;
                }
            }

        }

        table.setColumns(orderedColumns);
    }

    @Override
    public void encode(final FacesContext context, final DataTableRenderer renderer, final DataTable table)
        throws IOException {
        throw new RuntimeException("DraggableColumns Feature should not encode.");
    }

    @Override
    public boolean shouldDecode(final FacesContext context, final DataTable table) {
        return table.isDraggableColumns();
    }

    @Override
    public boolean shouldEncode(final FacesContext context, final DataTable table) {
        return false;
    }

}