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
package org.primefaces.component.socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "push/push.js") })
public class Socket extends UIComponentBase implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        channel,
        transport,
        fallbackTransport,
        onMessage,
        onError,
        autoConnect;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Socket";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.SocketRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private final static String DEFAULT_EVENT = "message";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("message"));

    private final Map<String, AjaxBehaviorEvent> customEvents = new HashMap<String, AjaxBehaviorEvent>();

    public Socket() {
        setRendererType(Socket.DEFAULT_RENDERER);
    }

    public java.lang.String getChannel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.channel, null);
    }

    @Override
    public String getDefaultEventName() {
        return Socket.DEFAULT_EVENT;
    }

    @Override
    public Collection<String> getEventNames() {
        return Socket.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public java.lang.String getFallbackTransport() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.fallbackTransport, "long-polling");
    }

    @Override
    public String getFamily() {
        return Socket.COMPONENT_FAMILY;
    }

    public java.lang.String getOnError() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onError, null);
    }

    public java.lang.String getOnMessage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onMessage, null);
    }

    public java.lang.String getTransport() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.transport, "websocket");
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Socket.OPTIMIZED_PACKAGE)) {
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

    public boolean isAutoConnect() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoConnect, true);
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

    public void setAutoConnect(final boolean _autoConnect) {
        getStateHelper().put(PropertyKeys.autoConnect, _autoConnect);
        handleAttribute("autoConnect", _autoConnect);
    }

    public void setChannel(final java.lang.String _channel) {
        getStateHelper().put(PropertyKeys.channel, _channel);
        handleAttribute("channel", _channel);
    }

    public void setFallbackTransport(final java.lang.String _fallbackTransport) {
        getStateHelper().put(PropertyKeys.fallbackTransport, _fallbackTransport);
        handleAttribute("fallbackTransport", _fallbackTransport);
    }

    public void setOnError(final java.lang.String _onError) {
        getStateHelper().put(PropertyKeys.onError, _onError);
        handleAttribute("onError", _onError);
    }

    public void setOnMessage(final java.lang.String _onMessage) {
        getStateHelper().put(PropertyKeys.onMessage, _onMessage);
        handleAttribute("onMessage", _onMessage);
    }

    public void setTransport(final java.lang.String _transport) {
        getStateHelper().put(PropertyKeys.transport, _transport);
        handleAttribute("transport", _transport);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}