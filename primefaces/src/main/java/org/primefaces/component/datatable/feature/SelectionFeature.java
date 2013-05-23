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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;
import org.primefaces.util.ComponentUtils;

public class SelectionFeature implements DataTableFeature {

    @Override
    public void decode(final FacesContext context, final DataTable table) {
        final String clientId = table.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        final String selection = params.get(clientId + "_selection");

        if (table.isSingleSelectionMode())
            decodeSingleSelection(table, selection);
        else decodeMultipleSelection(context, table, selection);
    }

    void decodeMultipleSelection(final FacesContext context, final DataTable table, final String selection) {
        final Class<?> clazz = table.getValueExpression("selection").getType(context.getELContext());
        final boolean isArray = clazz.isArray();

        if (!isArray && !clazz.isAssignableFrom(List.class)) {
            throw new FacesException("Multiple selection reference must be an Array or a List for datatable "
                + table.getClientId());
        }

        if (ComponentUtils.isValueBlank(selection)) {
            if (isArray) {
                table.setSelection(Array.newInstance(clazz.getComponentType(), 0));
            } else {
                table.setSelection(new ArrayList<Object>());
            }
        } else {
            final String[] rowKeys = selection.split(",");
            final List selectionList = new ArrayList();

            for (final String rowKey : rowKeys) {
                final Object rowData = table.getRowData(rowKey);

                if (rowData != null) selectionList.add(rowData);
            }

            if (isArray) {
                final Object selectionArray = Array.newInstance(clazz.getComponentType(), selectionList.size());
                table.setSelection(selectionList.toArray((Object[]) selectionArray));
            } else {
                table.setSelection(selectionList);
            }
        }
    }

    void decodeSingleSelection(final DataTable table, final String selection) {
        if (ComponentUtils.isValueBlank(selection))
            table.setSelection(null);
        else table.setSelection(table.getRowData(selection));
    }

    @Override
    public void encode(final FacesContext context, final DataTableRenderer renderer, final DataTable table)
        throws IOException {
        throw new RuntimeException("SelectFeature should not encode.");
    }

    @Override
    public boolean shouldDecode(final FacesContext context, final DataTable table) {
        return table.isSelectionEnabled();
    }

    @Override
    public boolean shouldEncode(final FacesContext context, final DataTable table) {
        return false;
    }

}
