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
package org.primefaces.component.columngroup;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class ColumnGroup extends UIComponentBase {

    protected enum PropertyKeys {

        type;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.ColumnGroup";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public ColumnGroup() {
        setRendererType(null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return ColumnGroup.COMPONENT_FAMILY;
    }

    public java.lang.String getType() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.type, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(ColumnGroup.OPTIMIZED_PACKAGE)) {
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

    public void setType(final java.lang.String _type) {
        getStateHelper().put(PropertyKeys.type, _type);
        handleAttribute("type", _type);
    }
}