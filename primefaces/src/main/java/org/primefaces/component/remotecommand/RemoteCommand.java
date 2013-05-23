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
package org.primefaces.component.remotecommand;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class RemoteCommand extends UICommand implements org.primefaces.component.api.AjaxSource {

    protected enum PropertyKeys {

        name,
        update,
        process,
        onstart,
        oncomplete,
        onerror,
        onsuccess,
        global,
        async,
        autoRun,
        partialSubmit;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.RemoteCommand";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.RemoteCommandRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public RemoteCommand() {
        setRendererType(RemoteCommand.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return RemoteCommand.COMPONENT_FAMILY;
    }

    public java.lang.String getName() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.name, null);
    }

    @Override
    public java.lang.String getOncomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    @Override
    public java.lang.String getOnerror() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    @Override
    public java.lang.String getOnstart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    @Override
    public java.lang.String getOnsuccess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    @Override
    public java.lang.String getProcess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
    }

    @Override
    public java.lang.String getUpdate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.update, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(RemoteCommand.OPTIMIZED_PACKAGE)) {
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

    @Override
    public boolean isAsync() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    public boolean isAutoRun() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoRun, false);
    }

    @Override
    public boolean isGlobal() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    @Override
    public boolean isPartialSubmit() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
    }

    @Override
    public boolean isPartialSubmitSet() {
        return (getStateHelper().get(PropertyKeys.partialSubmit) != null)
            || (getValueExpression("partialSubmit") != null);
    }

    public void setAsync(final boolean _async) {
        getStateHelper().put(PropertyKeys.async, _async);
        handleAttribute("async", _async);
    }

    public void setAutoRun(final boolean _autoRun) {
        getStateHelper().put(PropertyKeys.autoRun, _autoRun);
        handleAttribute("autoRun", _autoRun);
    }

    public void setGlobal(final boolean _global) {
        getStateHelper().put(PropertyKeys.global, _global);
        handleAttribute("global", _global);
    }

    public void setName(final java.lang.String _name) {
        getStateHelper().put(PropertyKeys.name, _name);
        handleAttribute("name", _name);
    }

    public void setOncomplete(final java.lang.String _oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
        handleAttribute("oncomplete", _oncomplete);
    }

    public void setOnerror(final java.lang.String _onerror) {
        getStateHelper().put(PropertyKeys.onerror, _onerror);
        handleAttribute("onerror", _onerror);
    }

    public void setOnstart(final java.lang.String _onstart) {
        getStateHelper().put(PropertyKeys.onstart, _onstart);
        handleAttribute("onstart", _onstart);
    }

    public void setOnsuccess(final java.lang.String _onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, _onsuccess);
        handleAttribute("onsuccess", _onsuccess);
    }

    public void setPartialSubmit(final boolean _partialSubmit) {
        getStateHelper().put(PropertyKeys.partialSubmit, _partialSubmit);
        handleAttribute("partialSubmit", _partialSubmit);
    }

    public void setProcess(final java.lang.String _process) {
        getStateHelper().put(PropertyKeys.process, _process);
        handleAttribute("process", _process);
    }

    public void setUpdate(final java.lang.String _update) {
        getStateHelper().put(PropertyKeys.update, _update);
        handleAttribute("update", _update);
    }
}