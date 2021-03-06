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
package org.omnifaces.facesviews;

import static javax.faces.application.ProjectStage.Development;
import java.io.IOException;
import java.util.Map;
import javax.faces.application.Application;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.omnifaces.filter.HttpFilter;
import org.omnifaces.util.Faces;
import org.omnifaces.util.ResourcePaths;

/**
 * This filter forwards request to a FacesServlet using an extension on which
 * this Servlet is mapped.
 * <p>
 * A filter like this is needed for extensionless requests, since the
 * FacesServlet in at least JSF 2.1 and before does not take into account any
 * other mapping than prefix- and extension (suffix) mapping.
 * <p>
 * For a guide on FacesViews, please see the <a
 * href="package-summary.html">package summary</a>.
 * 
 * @author Arjan Tijms
 * 
 */
public class FacesViewsForwardingFilter extends HttpFilter {

    private static ExtensionAction extensionAction;
    private static PathAction pathAction;
    private static FacesServletDispatchMethod dispatchMethod;

    private static void redirectPermanent(final HttpServletResponse response, final String url) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", url);
        response.setHeader("Connection", "close");
    }

    @Override
    public void doFilter(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final HttpSession session,
                         final FilterChain chain) throws ServletException, IOException {

        final ServletContext context = getServletContext();
        Map<String, String> resources = Faces.getApplicationAttribute(context, FacesViews.FACES_VIEWS_RESOURCES);
        final String resource = request.getServletPath();

        if (ResourcePaths.isExtensionless(resource)) {

            if (Faces.getApplicationFromFactory().getProjectStage() == Development && !resources.containsKey(resource)) {
                // Check if the resource was dynamically added by scanning the
                // faces-views location(s) again.
                resources = FacesViews.scanAndStoreViews(context);
            }

            if (resources.containsKey(resource)) {

                final String extension = ResourcePaths.getExtension(resources.get(resource));

                switch (FacesViewsForwardingFilter.dispatchMethod) {
                    case DO_FILTER:

                        // Continue the chain, but make the request appear to be
                        // to the resource with an extension.
                        // This assumes that the FacesServlet has been mapped to
                        // something that includes the extensionless
                        // request.
                        try {
                            request
                                .setAttribute(FacesViews.FACES_VIEWS_ORIGINAL_SERVLET_PATH, request.getServletPath());
                            chain.doFilter(new UriExtensionRequestWrapper(request, extension), response);
                        } finally {
                            request.removeAttribute(FacesViews.FACES_VIEWS_ORIGINAL_SERVLET_PATH);
                        }
                        return;
                    case FORWARD:

                        // Forward the resource (view) using its original
                        // extension, on which the Facelets Servlet
                        // is mapped. Technically it matters most that the
                        // Facelets Servlet picks up the
                        // request, and the exact extension or even prefix is
                        // perhaps less relevant.
                        final RequestDispatcher requestDispatcher = context.getRequestDispatcher(resource + extension);
                        if (requestDispatcher != null) {
                            // Forward the request to FacesServlet
                            requestDispatcher.forward(request, response);
                            return;
                        }
                }
            }
        } else if (resources.containsKey(resource)) {

            // A mapped resource request with extension is encountered, user
            // setting
            // determines how we handle this.

            switch (FacesViewsForwardingFilter.extensionAction) {
                case REDIRECT_TO_EXTENSIONLESS:
                    FacesViewsForwardingFilter.redirectPermanent(response, FacesViews
                        .getExtensionlessURLWithQuery(request));
                    return;
                case SEND_404:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                case PROCEED:
                    break;
                default:
                    break;
            }
        } else if (FacesViews.isResourceInPublicPath(context, resource)) {

            final Map<String, String> reverseResources = Faces
                .getApplicationAttribute(context, FacesViews.FACES_VIEWS_REVERSE_RESOURCES);

            if (reverseResources.containsKey(resource)) {

                // A direct request to one of the public paths (excluding /)
                // from where we scanned resources
                // was done. Again, the user setting determined how we handle
                // this.

                switch (FacesViewsForwardingFilter.pathAction) {
                    case REDIRECT_TO_SCANNED_EXTENSIONLESS:
                        FacesViewsForwardingFilter.redirectPermanent(response, FacesViews
                            .getExtensionlessURLWithQuery(request, reverseResources.get(resource)));
                        return;
                    case SEND_404:
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    case PROCEED:
                        break;
                    default:
                        break;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

        super.init(filterConfig);

        final ServletContext servletContext = filterConfig.getServletContext();

        // Mostly for pre-Servlet 3.0: scan the views if the auto-configure
        // listener hasn't done this yet.
        FacesViews.tryScanAndStoreViews(servletContext);

        // Register a view handler that transforms a view id with extension back
        // to an extensionless one.

        // Note that Filter#init is used here, since it loads after the
        // ServletContextListener that initializes JSF itself,
        // and thus guarantees the {@link Application} instance needed for
        // installing the FacesViewHandler is available.

        final Application application = Faces.getApplicationFromFactory();
        application.setViewHandler(new FacesViewsViewHandler(application.getViewHandler()));

        // In development mode additionally map this Filter to "*", so we can
        // catch requests to extensionless resources that
        // have been dynamically added. Note that resources with mapped
        // extensions are already handled by the FacesViewsResolver.
        // Adding resources with new extensions still requires a restart.
        if (application.getProjectStage() == Development && servletContext.getMajorVersion() > 2) {
            filterConfig.getServletContext().getFilterRegistration(FacesViewsForwardingFilter.class.getName())
                .addMappingForUrlPatterns(null, false, "*");
        }

        FacesViewsForwardingFilter.extensionAction = FacesViews.getExtensionAction(servletContext);
        FacesViewsForwardingFilter.pathAction = FacesViews.getPathAction(servletContext);
        FacesViewsForwardingFilter.dispatchMethod = FacesViews.getFacesServletDispatchMethod(servletContext);
    }

}