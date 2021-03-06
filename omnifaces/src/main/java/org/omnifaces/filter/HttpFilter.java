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
package org.omnifaces.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * An abstract filter for HTTP requests. It provides a convenient abstract
 * <code>doFilter()</code> method providing the HTTP servlet request, response
 * and session so that there's no need to cast them everytime. Also, default
 * implementations of {@link #init(FilterConfig)} and {@link #destroy()} are
 * provided, so that there's no need to implement them every time even when not
 * really needed.
 * 
 * @author Arjan Tijms, Bauke Scholtz
 */
public abstract class HttpFilter implements Filter {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    private static final String ERROR_NO_FILTERCONFIG = "FilterConfig is not available."
        + " When overriding HttpFilter#init(FilterConfig), please don't forget to call super.init(config).";

    // Properties
    // -----------------------------------------------------------------------------------------------------

    private FilterConfig filterConfig;

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Check if the filter config is been set and thus the enduser has properly
     * called super.init(config) when overriding the init(config).
     * 
     * @throws IllegalStateException When this is not the case.
     */
    private void checkFilterConfig() {
        if (filterConfig == null) {
            throw new IllegalStateException(HttpFilter.ERROR_NO_FILTERCONFIG);
        }
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }

    /**
     * Filter the HTTP request. The session argument is <code>null</code> if
     * there is no session.
     * 
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public abstract void doFilter(HttpServletRequest request,
                                  HttpServletResponse response,
                                  HttpSession session,
                                  FilterChain chain) throws ServletException, IOException;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws ServletException, IOException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final HttpSession session = httpRequest.getSession(false);
        doFilter(httpRequest, httpResponse, session, chain);
    }

    /**
     * Returns the filter config.
     * 
     * @return The filter config.
     */
    protected FilterConfig getFilterConfig() {
        checkFilterConfig();
        return filterConfig;
    }

    // Getters
    // --------------------------------------------------------------------------------------------------------

    /**
     * Returns the value of the filter init parameter associated with the given
     * name.
     * 
     * @param name The filter init parameter name to return the associated value
     *            for.
     * @return The value of the filter init parameter associated with the given
     *         name.
     */
    protected String getInitParameter(final String name) {
        checkFilterConfig();
        return filterConfig.getInitParameter(name);
    }

    /**
     * Returns the servlet context.
     * 
     * @return The servlet context.
     */
    protected ServletContext getServletContext() {
        checkFilterConfig();
        return filterConfig.getServletContext();
    }

    /**
     * Convenience init() method without FilterConfig parameter which will be
     * called by init(FilterConfig).
     * 
     * @throws ServletException When filter's initialization failed.
     */
    public void init() throws ServletException {
        //
    }

    // Helpers
    // --------------------------------------------------------------------------------------------------------

    /**
     * Called by the servlet container when the filter is about to be placed
     * into service. This implementation stores the {@link FilterConfig} object
     * for later use by the getter methods. When overriding this method, call
     * <code>super.init(config)</code>, otherwise the getter methods will throw
     * an illegal state exception.
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        init();
    }

}