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
package org.primefaces.component.poll;

import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Poll extends UIComponentBase implements org.primefaces.component.api.AjaxSource,
    org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        interval,
        update,
        listener,
        immediate,
        onstart,
        oncomplete,
        process,
        onerror,
        onsuccess,
        global,
        async,
        autoStart,
        stop,
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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Poll";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.PollRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public Poll() {
        setRendererType(Poll.DEFAULT_RENDERER);
    }

    @Override
    public void broadcast(final javax.faces.event.FacesEvent event) throws javax.faces.event.AbortProcessingException {
        super.broadcast(event); // backward compatibility

        final FacesContext facesContext = getFacesContext();
        final MethodExpression me = getListener();

        if (me != null) {
            me.invoke(facesContext.getELContext(), new Object[] {});
        }

        final ValueExpression expr = getValueExpression("stop");
        if (expr != null) {
            final RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.addCallbackParam("stop", expr.getValue(facesContext.getELContext()));
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Poll.COMPONENT_FAMILY;
    }

    public int getInterval() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.interval, 2);
    }

    public javax.el.MethodExpression getListener() {
        return (javax.el.MethodExpression) getStateHelper().eval(PropertyKeys.listener, null);
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

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Poll.OPTIMIZED_PACKAGE)) {
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

    public boolean isAutoStart() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoStart, true);
    }

    @Override
    public boolean isGlobal() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public boolean isImmediate() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
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

    public boolean isStop() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.stop, false);
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAsync(final boolean _async) {
        getStateHelper().put(PropertyKeys.async, _async);
        handleAttribute("async", _async);
    }

    public void setAutoStart(final boolean _autoStart) {
        getStateHelper().put(PropertyKeys.autoStart, _autoStart);
        handleAttribute("autoStart", _autoStart);
    }

    public void setGlobal(final boolean _global) {
        getStateHelper().put(PropertyKeys.global, _global);
        handleAttribute("global", _global);
    }

    public void setImmediate(final boolean _immediate) {
        getStateHelper().put(PropertyKeys.immediate, _immediate);
        handleAttribute("immediate", _immediate);
    }

    public void setInterval(final int _interval) {
        getStateHelper().put(PropertyKeys.interval, _interval);
        handleAttribute("interval", _interval);
    }

    public void setListener(final javax.el.MethodExpression _listener) {
        getStateHelper().put(PropertyKeys.listener, _listener);
        handleAttribute("listener", _listener);
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

    public void setStop(final boolean _stop) {
        getStateHelper().put(PropertyKeys.stop, _stop);
        handleAttribute("stop", _stop);
    }

    public void setUpdate(final java.lang.String _update) {
        getStateHelper().put(PropertyKeys.update, _update);
        handleAttribute("update", _update);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}