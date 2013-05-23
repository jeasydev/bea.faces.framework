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
package org.omnifaces.resourcehandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Utils;

/**
 * This {@link Resource} implementation holds all the necessary information
 * about combined resources in order to properly serve combined resources on a
 * single HTTP request.
 * 
 * @author Bauke Scholtz
 */
final class CombinedResource extends Resource {

    // Properties
    // -----------------------------------------------------------------------------------------------------

    /**
     * Determines and returns the resource name of the current resource request.
     * 
     * @param context The involved faces context.
     * @return The resource name of the current resource request (without any
     *         faces servlet mapping).
     */
    private static String getResourceName(final FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        String path = externalContext.getRequestPathInfo();

        if (path == null) {
            path = externalContext.getRequestServletPath();
            return path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
        } else {
            return path.substring(path.lastIndexOf('/') + 1);
        }
    }

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    private final CombinedResourceInfo info;

    /**
     * Constructs a new combined resource based on the current request. The
     * resource name will be extracted from the request information and be
     * passed to the other constructor {@link #CombinedResource(String)}. This
     * constructor is only used by
     * {@link CombinedResourceHandler#handleResourceRequest(FacesContext)}.
     * 
     * @param context The faces context involved in the current request.
     * @throws IllegalArgumentException If the resource name does not represent
     *             a valid combined resource.
     */
    public CombinedResource(final FacesContext context) {
        this(CombinedResource.getResourceName(context));
    }

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Constructs a new combined resource based on the given resource name. This
     * constructor is only used by
     * {@link CombinedResourceHandler#createResource(String, String)}.
     * 
     * @param name The resource name of the combined resource.
     * @throws IllegalArgumentException If the resource name does not represent
     *             a valid combined resource.
     */
    public CombinedResource(final String name) {
        final String[] resourcePathParts = name.split("\\.", 2)[0].split("/");
        final String resourceId = resourcePathParts[resourcePathParts.length - 1];
        info = CombinedResourceInfo.get(resourceId);

        if (info == null || info.getResources().isEmpty()) {
            throw new IllegalArgumentException();
        }

        setResourceName(name);
        setLibraryName(CombinedResourceHandler.LIBRARY_NAME);
        setContentType(Faces.getMimeType(name));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!info.getResources().isEmpty()) {
            return new CombinedResourceInputStream(info.getResources());
        } else {
            return null;
        }
    }

    @Override
    public String getRequestPath() {
        final String mapping = Faces.getMapping();
        final String path = ResourceHandler.RESOURCE_IDENTIFIER + "/" + getResourceName();
        return Faces.getRequestContextPath() + (Faces.isPrefixMapping(mapping) ? (mapping + path) : (path + mapping))
            + "?ln=" + CombinedResourceHandler.LIBRARY_NAME + "&v=" + (info.getLastModified() / 60000); // To
                                                                                                        // force
                                                                                                        // browser
                                                                                                        // refresh
                                                                                                        // whenever
                                                                                                        // a
                                                                                                        // resource
                                                                                                        // changes.
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        final Map<String, String> responseHeaders = new HashMap<String, String>(3);
        final long lastModified = info.getLastModified();
        responseHeaders.put("Last-Modified", Utils.formatRFC1123(new Date(lastModified)));
        responseHeaders.put("Expires", Utils.formatRFC1123(new Date(System.currentTimeMillis() + info.getMaxAge())));
        responseHeaders.put("Etag", String.format("W/\"%d-%d\"", info.getContentLength(), lastModified));
        return responseHeaders;
    }

    @Override
    public URL getURL() {
        try {
            // Yes, this returns a HTTP URL, not a classpath URL. There's no
            // other way anyway.
            return new URL(Faces.getRequestDomainURL() + getRequestPath());
        } catch (final MalformedURLException e) {
            // This exception should never occur.
            throw new RuntimeException(e);
        }
    }

    // Helpers
    // --------------------------------------------------------------------------------------------------------

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context) {
        final String ifModifiedSince = context.getExternalContext().getRequestHeaderMap().get("If-Modified-Since");

        if (ifModifiedSince != null) {
            try {
                return info.getLastModified() > Utils.parseRFC1123(ifModifiedSince).getTime();
            } catch (final ParseException ignore) {
                return true;
            }
        }

        return true;
    }

}