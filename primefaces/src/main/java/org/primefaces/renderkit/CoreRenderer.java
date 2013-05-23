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
package org.primefaces.renderkit;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import org.primefaces.component.api.AjaxSource;
import org.primefaces.context.RequestContext;
import org.primefaces.util.AjaxRequestBuilder;
import org.primefaces.util.Constants;
import org.primefaces.util.WidgetBuilder;

public abstract class CoreRenderer extends Renderer {

    protected String buildAjaxRequest(final FacesContext context, final AjaxSource source, final UIComponent form) {
        final UIComponent component = (UIComponent) source;
        final String clientId = component.getClientId(context);

        final AjaxRequestBuilder builder = new AjaxRequestBuilder();

        builder.source(clientId).process(context, component, source.getProcess()).update(context, component,
                                                                                         source.getUpdate())
            .async(source.isAsync()).global(source.isGlobal()).partialSubmit(source.isPartialSubmit(),
                                                                             source.isPartialSubmitSet())
            .onstart(source.getOnstart()).onerror(source.getOnerror()).onsuccess(source.getOnsuccess())
            .oncomplete(source.getOncomplete()).params(component);

        if (form != null) {
            builder.form(form.getClientId(context));
        }

        builder.preventDefault();

        return builder.build();
    }

    protected String buildNonAjaxRequest(final FacesContext context,
                                         final UIComponent component,
                                         final UIComponent form,
                                         final String decodeParam,
                                         final boolean submit) {
        final StringBuilder request = new StringBuilder();
        final String formId = form.getClientId(context);
        final Map<String, String> params = new HashMap<String, String>();

        if (decodeParam != null) {
            params.put(decodeParam, decodeParam);
        }

        for (final UIComponent child : component.getChildren()) {
            if (child instanceof UIParameter) {
                final UIParameter param = (UIParameter) child;

                params.put(param.getName(), String.valueOf(param.getValue()));
            }
        }

        // append params
        if (!params.isEmpty()) {
            request.append("PrimeFaces.addSubmitParam('").append(formId).append("',{");

            for (final Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
                final String key = it.next();
                final String value = params.get(key);

                request.append("'").append(key).append("':'").append(value).append("'");

                if (it.hasNext()) request.append(",");
            }

            request.append("})");
        }

        if (submit) {
            request.append(".submit('").append(formId).append("');");
        }

        return request.toString();
    }

    protected void decodeBehaviors(final FacesContext context, final UIComponent component) {

        if (!(component instanceof ClientBehaviorHolder)) {
            return;
        }

        final Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
        if (behaviors.isEmpty()) {
            return;
        }

        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (null != behaviorEvent) {
            final List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if (behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
                final String behaviorSource = params.get("javax.faces.source");
                final String clientId = component.getClientId();

                if (behaviorSource != null && clientId.startsWith(behaviorSource)) {
                    for (final ClientBehavior behavior : behaviorsForEvent) {
                        behavior.decode(context, component);
                    }
                }
            }
        }
    }

    /**
     * Non-obstrusive way to apply client behaviors. Behaviors are rendered as
     * options to the client side widget and applied by widget to necessary dom
     * element
     */
    protected void encodeClientBehaviors(final FacesContext context, final ClientBehaviorHolder component)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        // ClientBehaviors
        final Map<String, List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();

        if (!behaviorEvents.isEmpty()) {
            final String clientId = ((UIComponent) component).getClientId(context);
            final List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

            writer.write(",behaviors:{");

            for (final Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
                final String event = eventIterator.next();
                String domEvent = event;

                if (event.equalsIgnoreCase("valueChange")) // editable value
                                                           // holders
                    domEvent = "change";
                else if (event.equalsIgnoreCase("action")) // commands
                    domEvent = "click";

                writer.write(domEvent + ":");

                writer.write("function(event) {");
                for (final ClientBehavior behavior : behaviorEvents.get(event)) {
                    final ClientBehaviorContext cbc = ClientBehaviorContext
                        .createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    final String script = behavior.getScript(cbc); // could be
                                                                   // null if
                                                                   // disabled

                    if (script != null) {
                        writer.write(script);
                    }
                }
                writer.write("}");

                if (eventIterator.hasNext()) {
                    writer.write(",");
                }
            }

            writer.write("}");
        }
    }

    /**
     * Non-obstrusive way to apply client behaviors. Behaviors are rendered as
     * options to the client side widget and applied by widget to necessary dom
     * element
     */
    protected void encodeClientBehaviors(final FacesContext context,
                                         final ClientBehaviorHolder component,
                                         final WidgetBuilder wb) throws IOException {
        // ClientBehaviors
        final Map<String, List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();

        if (!behaviorEvents.isEmpty()) {
            final String clientId = ((UIComponent) component).getClientId(context);
            final List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

            wb.append(",behaviors:{");
            for (final Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
                final String event = eventIterator.next();
                String domEvent = event;

                if (event.equalsIgnoreCase("valueChange")) // editable value
                                                           // holders
                    domEvent = "change";
                else if (event.equalsIgnoreCase("action")) // commands
                    domEvent = "click";

                wb.append(domEvent).append(":");

                wb.append("function(event) {");
                for (final ClientBehavior behavior : behaviorEvents.get(event)) {
                    final ClientBehaviorContext cbc = ClientBehaviorContext
                        .createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    final String script = behavior.getScript(cbc); // could be
                                                                   // null if
                                                                   // disabled

                    if (script != null) {
                        wb.append(script);
                    }
                }
                wb.append("}");

                if (eventIterator.hasNext()) {
                    wb.append(",");
                }
            }

            wb.append("}");
        }
    }

    protected void endScript(final ResponseWriter writer) throws IOException {
        writer.endElement("script");
    }

    /**
     * Duplicate code from json-simple project under apache license
     * http://code.google
     * .com/p/json-simple/source/browse/trunk/src/org/json/simple/JSONValue.java
     */
    protected String escapeText(final String text) {
        if (text == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            final char ch = text.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    // Reference: http://www.unicode.org/versions/Unicode5.1.0/
                    if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F')
                        || (ch >= '\u2000' && ch <= '\u20FF')) {
                        final String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }

        return sb.toString();
    }

    protected String getActionURL(final FacesContext context) {
        final String actionURL = context.getApplication().getViewHandler().getActionURL(context,
                                                                                        context.getViewRoot()
                                                                                            .getViewId());

        return context.getExternalContext().encodeActionURL(actionURL);
    }

    public String getEscapedClientId(final String clientId) {
        return "#" + clientId.replaceAll(":", "\\\\\\\\:");
    }

    protected String getOnclickBehaviors(final FacesContext context, final ClientBehaviorHolder cbh) {
        final List<ClientBehavior> behaviors = cbh.getClientBehaviors().get("action");
        final StringBuilder sb = new StringBuilder();

        if (behaviors != null && !behaviors.isEmpty()) {
            final UIComponent component = (UIComponent) cbh;
            final String clientId = component.getClientId(context);
            final List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

            for (final ClientBehavior behavior : behaviors) {
                final ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, component,
                                                                                                    "action", clientId,
                                                                                                    params);
                final String script = behavior.getScript(cbc);

                if (script != null) sb.append(script).append(";");
            }
        }

        return sb.length() == 0 ? null : sb.toString();
    }

    protected String getResourceRequestPath(final FacesContext context, final String resourceName) {
        final Resource resource = context.getApplication().getResourceHandler().createResource(resourceName,
                                                                                               "primefaces");

        return resource.getRequestPath();
    }

    protected String getResourceURL(final FacesContext context, final String value) {
        if (value.contains(ResourceHandler.RESOURCE_IDENTIFIER)) {
            return value;
        } else {
            final String url = context.getApplication().getViewHandler().getResourceURL(context, value);

            return context.getExternalContext().encodeResourceURL(url);
        }
    }

    protected WidgetBuilder getWidgetBuilder(final FacesContext context) {
        return ((RequestContext) context.getAttributes().get(Constants.REQUEST_CONTEXT_ATTR)).getWidgetBuilder();
    }

    public boolean isAjaxRequest(final FacesContext context) {
        return context.getPartialViewContext().isAjaxRequest();
    }

    private boolean isIgnoredAttribute(final String attribute, final String[] ignoredAttrs) {
        for (final String ignoredAttribute : ignoredAttrs) {
            if (attribute.equals(ignoredAttribute)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPostback(final FacesContext context) {
        return context.getRenderKit().getResponseStateManager().isPostback(context);
    }

    protected boolean isPostBack() {
        final FacesContext context = FacesContext.getCurrentInstance();

        return context.getRenderKit().getResponseStateManager().isPostback(context);
    }

    public boolean isValueBlank(final String value) {
        if (value == null) return true;

        return value.trim().equals("");
    }

    public boolean isValueEmpty(final String value) {
        if (value == null || "".equals(value)) return true;

        return false;
    }

    protected void renderChild(final FacesContext context, final UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }

        child.encodeBegin(context);

        if (child.getRendersChildren()) {
            child.encodeChildren(context);
        } else {
            renderChildren(context, child);
        }
        child.encodeEnd(context);
    }

    protected void renderChildren(final FacesContext context, final UIComponent component) throws IOException {
        for (final UIComponent uiComponent : component.getChildren()) {
            final UIComponent child = uiComponent;
            renderChild(context, child);
        }
    }

    protected void renderPassThruAttributes(final FacesContext context,
                                            final UIComponent component,
                                            final String var,
                                            final String[] attrs) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        for (final String event : attrs) {
            final String eventHandler = (String) component.getAttributes().get(event);

            if (eventHandler != null)
                writer.write(var + ".addListener(\"" + event.substring(2, event.length()) + "\", function(e){"
                    + eventHandler + ";});\n");
        }
    }

    protected void renderPassThruAttributes(final FacesContext context,
                                            final UIComponent component,
                                            final String[] attrs) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        for (final String attribute : attrs) {
            final Object value = component.getAttributes().get(attribute);

            if (shouldRenderAttribute(value)) writer.writeAttribute(attribute, value.toString(), attribute);
        }
    }

    protected void renderPassThruAttributes(final FacesContext context,
                                            final UIComponent component,
                                            final String[] attrs,
                                            final String[] ignoredAttrs) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        for (final String attribute : attrs) {
            if (isIgnoredAttribute(attribute, ignoredAttrs)) {
                continue;
            }

            final Object value = component.getAttributes().get(attribute);

            if (shouldRenderAttribute(value)) writer.writeAttribute(attribute, value.toString(), attribute);
        }
    }

    protected boolean shouldRenderAttribute(final Object value) {
        if (value == null) return false;

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof Number) {
            final Number number = (Number) value;

            if (value instanceof Integer)
                return number.intValue() != Integer.MIN_VALUE;
            else if (value instanceof Double)
                return number.doubleValue() != Double.MIN_VALUE;
            else if (value instanceof Long)
                return number.longValue() != Long.MIN_VALUE;
            else if (value instanceof Byte)
                return number.byteValue() != Byte.MIN_VALUE;
            else if (value instanceof Float)
                return number.floatValue() != Float.MIN_VALUE;
            else if (value instanceof Short) return number.shortValue() != Short.MIN_VALUE;
        }

        return true;
    }

    protected boolean shouldWriteId(final UIComponent component) {
        final String id = component.getId();

        return (null != id)
            && (!id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX) || ((component instanceof ClientBehaviorHolder) && !((ClientBehaviorHolder) component)
                .getClientBehaviors().isEmpty()));
    }

    protected void startScript(final ResponseWriter writer, final String clientId) throws IOException {
        writer.startElement("script", null);
        writer.writeAttribute("id", clientId + "_s", null);
        writer.writeAttribute("type", "text/javascript", null);
    }
}