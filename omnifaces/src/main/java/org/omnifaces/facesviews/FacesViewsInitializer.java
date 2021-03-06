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
package org.omnifaces.facesviews;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.faces.view.facelets.ResourceResolver;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.omnifaces.util.Utils;

/**
 * Convenience class for Servlet 3.0 users, which will auto-register most
 * artifacts required for auto-mapping and extensionless view to work.
 * <p>
 * For a guide on FacesViews, please see the <a
 * href="package-summary.html">package summary</a>.
 * 
 * @author Arjan Tijms
 * @since 1.4
 */
public class FacesViewsInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(final Set<Class<?>> c, final ServletContext servletContext) throws ServletException {

        if (!"false".equals(servletContext.getInitParameter(FacesViews.FACES_VIEWS_ENABLED_PARAM_NAME))) {

            // Scan our dedicated directory for Faces resources that need to be
            // mapped
            final Map<String, String> collectedViews = new HashMap<String, String>();
            final Set<String> collectedExtensions = new HashSet<String>();
            FacesViews.scanViewsFromRootPaths(servletContext, collectedViews, collectedExtensions);

            if (!collectedViews.isEmpty()) {

                // Store the resources and extensions that were found in
                // application scope, where others can find it.
                servletContext.setAttribute(FacesViews.FACES_VIEWS_RESOURCES, Collections
                    .unmodifiableMap(collectedViews));
                servletContext.setAttribute(FacesViews.FACES_VIEWS_REVERSE_RESOURCES, Collections.unmodifiableMap(Utils
                    .reverse(collectedViews)));
                servletContext.setAttribute(FacesViews.FACES_VIEWS_RESOURCES_EXTENSIONS, Collections
                    .unmodifiableSet(collectedExtensions));

                // Register 3 artifacts with the Servlet container and JSF that
                // help implement this feature:

                // 1. A Filter that forwards extensionless requests to an
                // extension mapped request, e.g. /index to
                // /index.xhtml
                // (The FacesServlet doesn't work well with the exact mapping
                // that we use for extensionless URLs).
                final FilterRegistration facesViewsRegistration = servletContext
                    .addFilter(FacesViewsForwardingFilter.class.getName(), FacesViewsForwardingFilter.class);

                // 2. A Facelets resource resolver that resolves requests like
                // /index.xhtml to
                // /WEB-INF/faces-views/index.xhtml
                servletContext.setInitParameter(ResourceResolver.FACELETS_RESOURCE_RESOLVER_PARAM_NAME,
                                                FacesViewsResolver.class.getName());

                // 3. A ViewHandler that transforms the forwarded extension
                // based URL back to an extensionless one, e.g.
                // /index.xhtml to /index
                // See FacesViewsForwardingFilter#init

                // Map the forwarding filter to all the resources we found.
                for (final String resource : collectedViews.keySet()) {
                    facesViewsRegistration.addMappingForUrlPatterns(null, FacesViews
                        .isFilterAfterDeclaredFilters(servletContext), resource);
                }

                // Additionally map the filter to all paths that were scanned
                // and which are also directly
                // accessible. This is to give the filter an opportunity to
                // block these.
                for (final String path : FacesViews.getPublicRootPaths(servletContext)) {
                    facesViewsRegistration.addMappingForUrlPatterns(null, false, path + "*");
                }

                // We now need to map the Faces Servlet to the extensions we
                // found, but at this point in time
                // this Faces Servlet might not be created yet, so we do this
                // part in FacesViewInitializedListener.
            }
        }
    }

}