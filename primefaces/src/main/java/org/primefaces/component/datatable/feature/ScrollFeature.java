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
import java.util.Map;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;

public class ScrollFeature implements DataTableFeature {

    @Override
    public void decode(final FacesContext context, final DataTable table) {
        throw new RuntimeException("RowScrollFeature should not encode.");
    }

    @Override
    public void encode(final FacesContext context, final DataTableRenderer renderer, final DataTable table)
        throws IOException {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final int scrollOffset = Integer.parseInt(params.get(table.getClientId(context) + "_scrollOffset"));
        final String clientId = table.getClientId(context);
        final String rowIndexVar = table.getRowIndexVar();

        for (int i = scrollOffset; i < (scrollOffset + table.getScrollRows()); i++) {
            table.setRowIndex(i);

            if (table.isRowAvailable()) {
                renderer.encodeRow(context, table, clientId, i, rowIndexVar);
            }
        }
    }

    @Override
    public boolean shouldDecode(final FacesContext context, final DataTable table) {
        return false;
    }

    @Override
    public boolean shouldEncode(final FacesContext context, final DataTable table) {
        return context.getExternalContext().getRequestParameterMap().containsKey(table.getClientId(context)
                                                                                     + "_scrolling");
    }

}
