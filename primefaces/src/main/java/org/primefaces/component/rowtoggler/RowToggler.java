/*
 * Generated, Do Not Modify
 */
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
package org.primefaces.component.rowtoggler;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class RowToggler extends UIComponentBase {

    protected enum PropertyKeys {
        ;

        String toString;

        PropertyKeys() {
        }

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    public static final String COMPONENT_TYPE = "org.primefaces.component.RowToggler";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.RowTogglerRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public RowToggler() {
        setRendererType(RowToggler.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /*
     * import javax.faces.component.UIComponent; public void
     * processDecodes(FacesContext context) { UIComponent expansion =
     * getTable(context).getFacet("expansion"); if(expansion != null) {
     * expansion.processDecodes(context); } } public void
     * processValidators(FacesContext context) { UIComponent expansion =
     * getTable(context).getFacet("expansion"); if(expansion != null) {
     * expansion.processValidators(context); } } public void
     * processUpdates(FacesContext context) { UIComponent expansion =
     * getTable(context).getFacet("expansion"); if(expansion != null) {
     * expansion.processUpdates(context); } } private DataTable table; private
     * DataTable getTable(FacesContext context) { if(table == null) { table =
     * findParentTable(context); } return table; } private DataTable
     * findParentTable(FacesContext context) { UIComponent parent =
     * this.getParent(); while(parent != null) { if(parent instanceof DataTable)
     * { return (DataTable) parent; } parent = parent.getParent(); } return
     * null; }
     */

    @Override
    public String getFamily() {
        return RowToggler.COMPONENT_FAMILY;
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(RowToggler.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                final ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                } else if (!setAttributes.contains(name)) {
                    setAttributes.add(name);
                }
            }
        }
    }
}