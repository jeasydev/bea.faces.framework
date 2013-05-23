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
package org.primefaces.component.message;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIMessage;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css") })
public class Message extends UIMessage implements org.primefaces.component.api.UINotification {

    protected enum PropertyKeys {

        display,
        escape,
        severity;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Message";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.MessageRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public Message() {
        setRendererType(Message.DEFAULT_RENDERER);
    }

    public java.lang.String getDisplay() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.display, "both");
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Message.COMPONENT_FAMILY;
    }

    @Override
    public java.lang.String getSeverity() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.severity, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Message.OPTIMIZED_PACKAGE)) {
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

    public boolean isEscape() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.escape, true);
    }

    public void setDisplay(final java.lang.String _display) {
        getStateHelper().put(PropertyKeys.display, _display);
        handleAttribute("display", _display);
    }

    public void setEscape(final boolean _escape) {
        getStateHelper().put(PropertyKeys.escape, _escape);
        handleAttribute("escape", _escape);
    }

    public void setSeverity(final java.lang.String _severity) {
        getStateHelper().put(PropertyKeys.severity, _severity);
        handleAttribute("severity", _severity);
    }
}