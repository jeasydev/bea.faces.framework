/*
 * Copyright 2012 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.component.output;

import static org.omnifaces.component.output.Cache.PropertyKeys.key;
import static org.omnifaces.component.output.Cache.PropertyKeys.scope;
import static org.omnifaces.component.output.Cache.PropertyKeys.time;
import static org.omnifaces.component.output.Cache.PropertyKeys.useBuffer;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.faces.component.FacesComponent;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.event.PreRenderViewEvent;
import javax.faces.event.SystemEvent;
import org.omnifaces.component.output.cache.CacheFactory;
import org.omnifaces.component.output.cache.CacheInitializerListener;
import org.omnifaces.filter.OnDemandResponseBufferFilter;
import org.omnifaces.servlet.BufferedHttpServletResponse;
import org.omnifaces.util.Callback;
import org.omnifaces.util.Events;
import org.omnifaces.util.Faces;
import org.omnifaces.util.State;

/**
 * <strong>Cache</strong> is a component that captures the mark-up rendered by
 * its children and caches this for future requests.
 * 
 * @since 1.1
 * @author Arjan Tijms
 */
@FacesComponent(Cache.COMPONENT_TYPE)
public class Cache extends OutputFamily {

    enum PropertyKeys {
        key,
        scope,
        time,
        useBuffer
    }

    public static final String COMPONENT_TYPE = "org.omnifaces.component.output.Cache";
    public static final String VALUE_SET = "org.omnifaces.cache.VALUE_SET";
    public static final String DEFAULT_SCOPE = "session";
    public static final String START_CONTENT_MARKER = "<!-- START CACHE FOR %s -->";

    public static final String END_CONTENT_MARKER = "<!-- END CACHE FOR %s -->";
    private static final String ERROR_NO_BUFFERED_RESPONSE = String
        .format("No buffered response found in request, but 'useBuffer' set to true. Check setting the '%s' context parameter or installing the '%s' filter manually.",
                CacheInitializerListener.CACHE_INSTALL_BUFFER_FILTER, OnDemandResponseBufferFilter.class);

    private static Class<? extends SystemEvent> PRE_RENDER = PreRenderViewEvent.class;

    private final State state = new State(getStateHelper());

    public Cache() {

        final FacesContext context = FacesContext.getCurrentInstance();

        // Execute the following code in PreRenderView, since at construction
        // time the "useBuffer" and "key" attributes
        // have not been set, and there is no @PostContruct for UIComponents.
        Events.subscribeToViewEvent(Cache.PRE_RENDER, new Callback.Void() {

            @Override
            public void invoke() {

                if (isUseBuffer() && !hasCachedValue(context)) {

                    final BufferedHttpServletResponse bufferedHttpServletResponse = Faces
                        .getRequestAttribute(OnDemandResponseBufferFilter.BUFFERED_RESPONSE);

                    if (bufferedHttpServletResponse == null) {
                        throw new IllegalStateException(Cache.ERROR_NO_BUFFERED_RESPONSE);
                    }

                    // Start buffering the response from now on
                    bufferedHttpServletResponse.setPassThrough(false);

                    // After the RENDER_RESPONSE phase, copy the area we need to
                    // cache from the response buffer
                    // and insert it into our cache
                    Events.setCallbackAfterPhaseListener(PhaseId.RENDER_RESPONSE, new Callback.Void() {

                        @Override
                        public void invoke() {
                            String content = null;

                            try {
                                content = getContentFromBuffer(bufferedHttpServletResponse.getBufferAsString());
                            } catch (final IOException e) {
                                throw new IllegalStateException(e);
                            }

                            if (content != null) {
                                cacheContent(context, content);
                            }
                        }

                    });
                }
            }
        });
    }

    private void cacheContent(final FacesContext context,
                              final org.omnifaces.component.output.cache.Cache scopedCache,
                              final String key,
                              final String content) {
        final int time = getTime();
        if (time > 0) {
            scopedCache.put(key, content, time);
        } else {
            scopedCache.put(key, content);
        }

        // Marker to register we added a value to the cache during this request
        context.getExternalContext().getRequestMap().put(Cache.VALUE_SET, Boolean.TRUE);
    }

    private void cacheContent(final FacesContext context, final String content) {
        cacheContent(context, CacheFactory.getCache(context, getScope()), getKeyWithDefault(context), content);
    }

    @Override
    public void encodeChildren(final FacesContext context) throws IOException {

        final String key = getKeyWithDefault(context);

        final ResponseWriter responseWriter = context.getResponseWriter();
        final org.omnifaces.component.output.cache.Cache scopedCache = getCacheImpl(context);

        String childRendering = scopedCache.get(key);

        if (childRendering == null) {
            final Writer bufferWriter = new StringWriter();

            final ResponseWriter bufferedResponseWriter = responseWriter.cloneWithWriter(bufferWriter);

            context.setResponseWriter(bufferedResponseWriter);

            try {
                if (isUseBuffer()) {
                    bufferedResponseWriter.write(getStartContentMarker());
                }

                super.encodeChildren(context);

                if (isUseBuffer()) {
                    bufferedResponseWriter.write(getEndContentMarker());
                }
            } finally {
                context.setResponseWriter(responseWriter);
            }

            childRendering = bufferWriter.toString();

            cacheContent(context, scopedCache, key, childRendering);
        }

        responseWriter.write(childRendering);
    }

    /**
     * Gets a named attribute associated with the main cache entry this
     * component is using to store the rendering of its child components.
     * 
     * @param context the current FacesContext
     * @param name name of the attribute to retrieve a value for
     * @return value associated with the named attribute
     * @since 1.2
     */
    public Object getCacheAttribute(final FacesContext context, final String name) {
        return getCacheImpl(context).getAttribute(getKeyWithDefault(context), name);
    }

    private org.omnifaces.component.output.cache.Cache getCacheImpl(final FacesContext context) {
        return CacheFactory.getCache(context, getScope());
    }

    private String getContentFromBuffer(final String buffer) {
        final String startMarker = getStartContentMarker();
        final int startIndex = buffer.indexOf(startMarker);

        if (startIndex != -1) {

            final String endMarker = getEndContentMarker();
            final int endIndex = buffer.indexOf(endMarker);

            if (endIndex != -1) {

                return buffer.substring(startIndex + startMarker.length(), endIndex);
            }
        }

        return null;
    }

    private String getEndContentMarker() {
        return String.format(Cache.END_CONTENT_MARKER, getClientId());
    }

    public String getKey() {
        return state.get(key);
    }

    private String getKeyWithDefault(final FacesContext context) {
        String key = getKey();
        if (key == null) {
            key = context.getViewRoot().getViewId() + "_" + this.getClientId(context);
        }

        return key;
    }

    public String getScope() {
        return state.get(scope, Cache.DEFAULT_SCOPE);
    }

    private String getStartContentMarker() {
        return String.format(Cache.START_CONTENT_MARKER, getClientId());
    }

    public Integer getTime() {
        return state.get(time, -1);
    }

    /**
     * 
     * @param context the FacesContext
     * @return true if there is a value in the cache corresponding to this
     *         component, false otherwise
     */
    private boolean hasCachedValue(final FacesContext context) {
        return CacheFactory.getCache(context, getScope()).get(getKeyWithDefault(context)) != null;
    }

    /**
     * 
     * @param context the FacesContext
     * @return true if a value was inserted in the cache during this request,
     *         false otherwise
     */
    private boolean isCachedValueJustSet(final FacesContext context) {
        return Boolean.TRUE.equals(context.getExternalContext().getRequestMap().get(Cache.VALUE_SET));
    }

    public Boolean isUseBuffer() {
        return state.get(useBuffer, Boolean.FALSE);
    }

    @Override
    protected boolean isVisitable(final VisitContext visitContext) {

        final FacesContext context = visitContext.getFacesContext();

        // Visit us and our children if a value for the cache was set in this
        // request, or
        // if no value was cached yet.
        return isCachedValueJustSet(context) || !hasCachedValue(context);
    }

    /**
     * Sets a named attribute associated with the main cache entry this
     * component is using to store the rendering of its child components.
     * 
     * @param context the current FacesContext
     * @param name name of the attribute under which the value is stored
     * @param value the value that is to be stored
     * @since 1.2
     */
    public void setCacheAttribute(final FacesContext context, final String name, final Object value) {
        getCacheImpl(context).putAttribute(getKeyWithDefault(context), name, value, getTime());
    }

    public void setKey(final String keyValue) {
        state.put(key, keyValue);
    }

    public void setScope(final String scopeValue) {
        state.put(scope, scopeValue);
    }

    public void setTime(final Integer timeValue) {
        state.put(time, timeValue);
    }

    public void setUseBuffer(final Boolean useBufferValue) {
        state.put(useBuffer, useBufferValue);
    }

}
