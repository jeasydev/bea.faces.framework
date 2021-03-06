/*
 * Copyright 2013 OmniFaces.
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
package org.omnifaces.resourcehandler;

import java.io.IOException;
import java.util.Map.Entry;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ResourceWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.http.HttpServletResponse;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Utils;

/**
 * The <strong>UnmappedResourceHandler</strong> allows the developer to map JSF
 * resources on an URL pattern of <code>/javax.faces.resource/*</code> without
 * the need for an additional {@link FacesServlet} prefix or suffic URL pattern
 * in the default produced resource URLs, such as
 * <code>/javax.faces.resource/faces/css/style.css</code> or
 * <code>/javax.faces.resource/css/style.css.xhtml</code>. This resource handler
 * will produce unmapped URLs like
 * <code>/javax.faces.resource/css/style.css</code>. This has the major
 * advantage that the developer don't need the <code>#{resource}</code> EL
 * expression anymore in order to properly reference relative URLs to images in
 * CSS files. Given the following folder structure,
 * 
 * <pre>
 * WebContent
 *  `-- resources
 *       `-- css
 *            |-- images
 *            |    `-- background.png
 *            `-- style.css
 * </pre>
 * <p>
 * you can in <code>css/style.css</code> just use:
 * 
 * <pre>
 * body {
 *     background: url("images/background.png");
 * }
 * </pre>
 * <p>
 * instead of
 * 
 * <pre>
 * body {
 *     background: url("#{resource['css/images/background.png']}");
 * }
 * </pre>
 * 
 * <h3>Configuration</h3>
 * <p>
 * To get it to run, this handler needs be registered as follows in
 * <code>faces-config.xml</code>:
 * 
 * <pre>
 * &lt;application&gt;
 *   &lt;resource-handler&gt;org.omnifaces.resourcehandler.UnmappedResourceHandler&lt;/resource-handler&gt;
 * &lt;/application&gt;
 * </pre>
 * <p>
 * And the {@link FacesServlet} needs to have an additional mapping
 * <code>/javax.faces.resource/*</code> in <code>web.xml</code>. For example,
 * assuming that you've already a mapping on <code>*.xhtml</code>:
 * 
 * <pre>
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;facesServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;*.xhtml&lt;/url-pattern&gt;
 *   &lt;url-pattern&gt;/javax.faces.resource/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </pre>
 * 
 * <h3>CombinedResourceHandler</h3>
 * <p>
 * If you're also using the {@link CombinedResourceHandler} or any other custom
 * resource handler, then you need to ensure that this is in
 * <code>faces-config.xml</code> declared <strong>before</strong> the
 * <code>UnmappedResourceHandler</code>. Thus, like so:
 * 
 * <pre>
 * &lt;application&gt;
 *   &lt;resource-handler&gt;org.omnifaces.resourcehandler.CombinedResourceHandler&lt;/resource-handler&gt;
 *   &lt;resource-handler&gt;org.omnifaces.resourcehandler.UnmappedResourceHandler&lt;/resource-handler&gt;
 * &lt;/application&gt;
 * </pre>
 * <p>
 * Otherwise the combined resource handler will still produce mapped URLs. In
 * essence, the one which is later registered wraps the previously registered
 * one.
 * 
 * @author Bauke Scholtz
 * @since 1.4
 */
public class UnmappedResourceHandler extends ResourceHandlerWrapper {

    // Properties
    // -----------------------------------------------------------------------------------------------------

    private final ResourceHandler wrapped;

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance of this unmapped resource handler which wraps the
     * given resource handler.
     * 
     * @param wrapped The resource handler to be wrapped.
     */
    public UnmappedResourceHandler(final ResourceHandler wrapped) {
        this.wrapped = wrapped;
    }

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance of a resource based on the given resource name and
     * library name. If it is not <code>null</code>, then return a wrapped
     * resource whose {@link Resource#getRequestPath()} returns the unmapped
     * URL.
     */
    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        final Resource resource = super.createResource(resourceName, libraryName);

        if (resource == null) {
            return null;
        }

        return new ResourceWrapper() {

            @Override
            // Necessary because this is missing in ResourceWrapper (will be
            // fixed in JSF 2.2).
            public String getContentType() {
                return resource.getContentType();
            }

            @Override
            // Necessary because this is missing in ResourceWrapper (will be
            // fixed in JSF 2.2).
            public String getLibraryName() {
                return resource.getLibraryName();
            }

            @Override
            public String getRequestPath() {
                final String path = super.getRequestPath();
                final String mapping = Faces.getMapping();

                if (Faces.isPrefixMapping(mapping)) {
                    return path.replaceFirst(mapping, "");
                } else if (path.contains("?")) {
                    return path.replace(mapping + "?", "?");
                } else {
                    return path.substring(0, path.length() - mapping.length());
                }
            }

            @Override
            // Necessary because this is missing in ResourceWrapper (will be
            // fixed in JSF 2.2).
            public String getResourceName() {
                return resource.getResourceName();
            }

            @Override
            public Resource getWrapped() {
                return resource;
            }
        };
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handleResourceRequest(final FacesContext context) throws IOException {
        final ExternalContext externalContext = context.getExternalContext();
        final String resourceName = externalContext.getRequestPathInfo();
        final String libraryName = externalContext.getRequestParameterMap().get("ln");
        final Resource resource = context.getApplication().getResourceHandler().createResource(resourceName,
                                                                                               libraryName);

        if (resource == null) {
            super.handleResourceRequest(context);
            return;
        }

        if (!resource.userAgentNeedsUpdate(context)) {
            externalContext.setResponseStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        externalContext.setResponseContentType(resource.getContentType());

        for (final Entry<String, String> header : resource.getResponseHeaders().entrySet()) {
            externalContext.setResponseHeader(header.getKey(), header.getValue());
        }

        Utils.stream(resource.getInputStream(), externalContext.getResponseOutputStream());
    }

    /**
     * Returns <code>true</code> if
     * {@link ExternalContext#getRequestServletPath()} starts with value of
     * {@link ResourceHandler#RESOURCE_IDENTIFIER}.
     */
    @Override
    public boolean isResourceRequest(final FacesContext context) {
        return ResourceHandler.RESOURCE_IDENTIFIER.equals(context.getExternalContext().getRequestServletPath());
    }

}