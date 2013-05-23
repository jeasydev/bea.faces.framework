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
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;

public class RowEditFeature implements DataTableFeature {

    @Override
    public void decode(final FacesContext context, final DataTable table) {
        throw new RuntimeException("RowEditFeature should not encode.");
    }

    @Override
    public void encode(final FacesContext context, final DataTableRenderer renderer, final DataTable table)
        throws IOException {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        table.getClientId(context);
        final int editedRowId = Integer.parseInt(params.get(table.getClientId(context) + "_rowEditIndex"));
        final String action = params.get(table.getClientId(context) + "_rowEditAction");
        table.setRowIndex(editedRowId);

        if (action.equals("cancel")) {
            for (final UIColumn column : table.getColumns()) {
                for (final UIComponent grandkid : column.getChildren()) {
                    if (grandkid instanceof CellEditor) {
                        final UIComponent inputFacet = grandkid.getFacet("input");

                        if (inputFacet instanceof EditableValueHolder) {
                            ((EditableValueHolder) inputFacet).resetValue();
                        }
                    }
                }
            }
        }

        if (table.isRowAvailable()) {
            renderer.encodeRow(context, table, table.getClientId(context), editedRowId, table.getRowIndexVar());
        }
    }

    @Override
    public boolean shouldDecode(final FacesContext context, final DataTable table) {
        return false;
    }

    @Override
    public boolean shouldEncode(final FacesContext context, final DataTable table) {
        return context.getExternalContext().getRequestParameterMap().containsKey(table.getClientId(context)
                                                                                     + "_rowEditAction");
    }
}
