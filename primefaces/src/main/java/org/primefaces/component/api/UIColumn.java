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
package org.primefaces.component.api;

import java.io.IOException;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.component.celleditor.CellEditor;

public interface UIColumn {

    public void encodeAll(FacesContext context) throws IOException;

    public CellEditor getCellEditor();

    public List<UIComponent> getChildren();

    public String getClientId();

    public String getClientId(FacesContext context);

    public int getColspan();

    public String getColumnKey();

    public String getContainerClientId(FacesContext context);

    public UIComponent getFacet(String facet);

    public String getFilterMatchMode();

    public int getFilterMaxLength();

    public Object getFilterOptions();

    public String getFilterPosition();

    public String getFilterStyle();

    public String getFilterStyleClass();

    public String getFooterText();

    public String getHeaderText();

    public int getRowspan();

    public String getSelectionMode();

    public MethodExpression getSortFunction();

    public String getStyle();

    public String getStyleClass();

    public ValueExpression getValueExpression(String property);

    public String getWidth();

    public boolean isDisabledSelection();

    public boolean isDynamic();

    public boolean isExportable();

    public boolean isRendered();

    public boolean isResizable();
}
