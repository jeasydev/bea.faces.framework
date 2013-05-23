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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import org.omnifaces.util.Faces;
import org.omnifaces.util.ResourcePaths;
import org.omnifaces.util.Utils;

/**
 * This class contains the core methods that implement the Faces Views feature.
 * 
 * TODO: break up in logic and config?
 * 
 * @author Arjan Tijms
 * 
 */
public final class FacesViews {

    /**
     * A special dedicated "well-known" directory where facelets implementing
     * views can be placed. This directory is scanned by convention so that no
     * explicit configuration is needed.
     */
    public static final String WEB_INF_VIEWS = "/WEB-INF/faces-views/";

    /**
     * Web context parameter to switch auto-scanning completely off for Servlet
     * 3.0 containers.
     */
    public static final String FACES_VIEWS_ENABLED_PARAM_NAME = "org.omnifaces.FACES_VIEWS_ENABLED";

    /**
     * The name of the init parameter (in web.xml) where the value holds a comma
     * separated list of paths that are to be scanned by faces views.
     */
    public static final String FACES_VIEWS_SCAN_PATHS_PARAM_NAME = "org.omnifaces.FACES_VIEWS_SCAN_PATHS";

    /**
     * The name of the init parameter (in web.xml) via which the user can set
     * scanned views to be always rendered extensionless. Without this setting
     * (or it being set to false), it depends on whether the request URI uses an
     * extension or not. If it doesn't, links are also rendered without one,
     * otherwise are rendered with an extension.
     */
    public static final String FACES_VIEWS_SCANNED_VIEWS_EXTENSIONLESS_PARAM_NAME = "org.omnifaces.FACES_VIEWS_SCANNED_VIEWS_ALWAYS_EXTENSIONLESS";

    /**
     * The name of the init parameter (in web.xml) that determines the action
     * that is performed whenever a resource is requested WITH extension that's
     * also available without an extension. See {@link ExtensionAction}
     */
    public static final String FACES_VIEWS_EXTENSION_ACTION_PARAM_NAME = "org.omnifaces.FACES_VIEWS_EXTENSION_ACTION";

    /**
     * The name of the init parameter (in web.xml) that determines the action
     * that is performed whenever a resource is requested in a public path that
     * has been used for scanning views by faces views. See {@link PathAction}
     */
    public static final String FACES_VIEWS_PATH_ACTION_PARAM_NAME = "org.omnifaces.FACES_VIEWS_PATH_ACTION";

    /**
     * The name of the init parameter (in web.xml) that determines the method
     * used by FacesViews to invoke the FacesServlet. See
     * {@link FacesServletDispatchMethod}.
     */
    public static final String FACES_VIEWS_DISPATCH_METHOD_PARAM_NAME = "org.omnifaces.FACES_VIEWS_DISPATCH_METHOD";

    /**
     * The name of the boolean init parameter (in web.xml) via which the user
     * can set whether the {@link FacesViewsForwardingFilter} should match
     * before declared filters (false) or after declared filters (true);
     */
    public static final String FACES_VIEWS_FILTER_AFTER_DECLARED_FILTERS_PARAM_NAME = "org.omnifaces.FILTER_AFTER_DECLARED_FILTERS";

    /**
     * The name of the application scope context parameter under which a Set
     * version of the paths that are to be scanned by faces views are kept.
     */
    public static final String SCAN_PATHS = "org.omnifaces.facesviews.scanpaths";

    /**
     * The name of the application scope context parameter under which a Set
     * version of the public paths that are to be scanned by faces views are
     * kept. A public path is a path that is also directly accessible, e.g. is
     * world readable. This excludes the special path /, which is by definition
     * world readable but not included in this set.
     */
    public static final String PUBLIC_SCAN_PATHS = "org.omnifaces.facesviews.public.scanpaths";

    /**
     * The name of the application scope context parameter under which a Boolean
     * version of the scanned views always exensionless init parameter is kept.
     */
    public static final String SCANNED_VIEWS_EXTENSIONLESS = "org.omnifaces.facesviews.scannedviewsextensionless";

    public static final String FACES_VIEWS_RESOURCES = "org.omnifaces.facesviews";

    public static final String FACES_VIEWS_REVERSE_RESOURCES = "org.omnifaces.facesviews.reverse.resources";
    public static final String FACES_VIEWS_RESOURCES_EXTENSIONS = "org.omnifaces.facesviews.extensions";
    public static final String FACES_VIEWS_ORIGINAL_SERVLET_PATH = "org.omnifaces.facesviews.original.servlet_path";

    public static boolean canScanDirectory(final String rootPath, final String directory) {

        if (!"/".equals(rootPath)) {
            // If a user has explicitly asked for scanning anything other than
            // /, every sub directory of it can be scanned.
            return true;
        }

        // For the special directory /, don't scan WEB-INF, META-INF and
        // resources
        return !Utils.startsWithOneOf(directory, "/WEB-INF/", "/META-INF/", "/resources/");
    }

    public static boolean canScanResource(final String resource, final String extensionToScan) {

        if (extensionToScan == null) {
            // If no extension has been explicitly defined, we scan all
            // extensions encountered
            return true;
        }

        return resource.endsWith(extensionToScan);
    }

    public static ExtensionAction getExtensionAction(final ServletContext servletContext) {
        final String extensionActionString = servletContext
            .getInitParameter(FacesViews.FACES_VIEWS_EXTENSION_ACTION_PARAM_NAME);
        if (Utils.isEmpty(extensionActionString)) {
            return ExtensionAction.REDIRECT_TO_EXTENSIONLESS;
        }

        try {
            return ExtensionAction.valueOf(extensionActionString.toUpperCase(Locale.US));
        } catch (final Exception e) {
            throw new IllegalStateException(String.format("Value '%s' is not valid for context parameter for '%s'",
                                                          extensionActionString,
                                                          FacesViews.FACES_VIEWS_EXTENSION_ACTION_PARAM_NAME));
        }
    }

    /**
     * Obtains the full request URL from the given request complete with the
     * query string, but with the extension (if any) cut out.
     * <p>
     * E.g. <code>http://localhost/foo/bar.xhtml?kaz=1</code> becomes
     * <code>http://localhost/foo/bar?kaz=1</code>
     * 
     * @param request the request from the URL is obtained.
     * @return request URL with query parameters but without file extension
     */
    public static String getExtensionlessURLWithQuery(final HttpServletRequest request) {
        return FacesViews.getExtensionlessURLWithQuery(request, request.getServletPath());
    }

    /**
     * Obtains the full request URL from the given request and the given
     * resource complete with the query string, but with the extension (if any)
     * cut out.
     * <p>
     * E.g. <code>http://localhost/foo/bar.xhtml?kaz=1</code> becomes
     * <code>http://localhost/foo/bar?kaz=1</code>
     * 
     * @param request the request from which the base URL is obtained.
     * @param resource the resource relative to the base URL
     * @return request URL with query parameters but without file extension
     */
    public static String getExtensionlessURLWithQuery(final HttpServletRequest request, final String resource) {
        final String queryString = !Utils.isEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";

        String baseURL = Faces.getRequestBaseURL(request);
        if (baseURL.endsWith("/")) {
            baseURL = baseURL.substring(0, baseURL.length() - 1);
        }

        return baseURL + ResourcePaths.stripExtension(resource) + queryString;
    }

    public static FacesServletDispatchMethod getFacesServletDispatchMethod(final ServletContext servletContext) {
        final String dispatchMethodString = servletContext
            .getInitParameter(FacesViews.FACES_VIEWS_DISPATCH_METHOD_PARAM_NAME);
        if (Utils.isEmpty(dispatchMethodString)) {
            return FacesServletDispatchMethod.DO_FILTER;
        }

        try {
            return FacesServletDispatchMethod.valueOf(dispatchMethodString.toUpperCase(Locale.US));
        } catch (final Exception e) {
            throw new IllegalStateException(String.format("Value '%s' is not valid for context parameter for '%s'",
                                                          dispatchMethodString,
                                                          FacesViews.FACES_VIEWS_DISPATCH_METHOD_PARAM_NAME));
        }
    }

    public static String getMappedPath(final String path) {
        String facesViewsPath = path;
        final Map<String, String> mappedResources = Faces.getApplicationAttribute(FacesViews.FACES_VIEWS_RESOURCES);
        if (mappedResources != null && mappedResources.containsKey(path)) {
            facesViewsPath = mappedResources.get(path);
        }

        return facesViewsPath;
    }

    public static PathAction getPathAction(final ServletContext servletContext) {
        final String pathActionString = servletContext.getInitParameter(FacesViews.FACES_VIEWS_PATH_ACTION_PARAM_NAME);
        if (Utils.isEmpty(pathActionString)) {
            return PathAction.SEND_404;
        }

        try {
            return PathAction.valueOf(pathActionString.toUpperCase(Locale.US));
        } catch (final Exception e) {
            throw new IllegalStateException(String.format("Value '%s' is not valid for context parameter for '%s'",
                                                          pathActionString,
                                                          FacesViews.FACES_VIEWS_PATH_ACTION_PARAM_NAME));
        }
    }

    public static Set<String> getPublicRootPaths(final ServletContext servletContext) {
        @SuppressWarnings("unchecked")
        Set<String> publicRootPaths = (Set<String>) servletContext.getAttribute(FacesViews.PUBLIC_SCAN_PATHS);

        if (publicRootPaths == null) {
            final Set<String> rootPaths = FacesViews.getRootPaths(servletContext);
            publicRootPaths = new HashSet<String>();
            for (String rootPath : rootPaths) {

                if (rootPath.contains("*")) {
                    final String[] pathAndExtension = rootPath.split(Pattern.quote("*"));
                    rootPath = pathAndExtension[0];
                }

                rootPath = FacesViews.normalizeRootPath(rootPath);

                if (!"/".equals(rootPath) && !Utils.startsWithOneOf(rootPath, "/WEB-INF/", "/META-INF/")) {
                    publicRootPaths.add(rootPath);
                }
            }
            servletContext.setAttribute(FacesViews.PUBLIC_SCAN_PATHS, Collections.unmodifiableSet(publicRootPaths));
        }

        return publicRootPaths;
    }

    public static Set<String> getRootPaths(final ServletContext servletContext) {
        @SuppressWarnings("unchecked")
        Set<String> rootPaths = (Set<String>) servletContext.getAttribute(FacesViews.SCAN_PATHS);

        if (rootPaths == null) {
            rootPaths = new HashSet<String>(Utils.csvToList(servletContext
                .getInitParameter(FacesViews.FACES_VIEWS_SCAN_PATHS_PARAM_NAME)));
            rootPaths.add(FacesViews.WEB_INF_VIEWS);
            servletContext.setAttribute(FacesViews.SCAN_PATHS, Collections.unmodifiableSet(rootPaths));
        }

        return rootPaths;
    }

    public static boolean isFilterAfterDeclaredFilters(final ServletContext servletContext) {
        final String filterAfterDeclaredFilters = servletContext
            .getInitParameter(FacesViews.FACES_VIEWS_FILTER_AFTER_DECLARED_FILTERS_PARAM_NAME);

        if (filterAfterDeclaredFilters == null) {
            return true;
        }

        return Boolean.valueOf(filterAfterDeclaredFilters);
    }

    public static boolean isResourceInPublicPath(final ServletContext servletContext, final String resource) {
        final Set<String> publicPaths = FacesViews.getPublicRootPaths(servletContext);
        for (final String path : publicPaths) {
            if (resource.startsWith(path)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isScannedViewsAlwaysExtensionless(final FacesContext context) {

        final ExternalContext externalContext = context.getExternalContext();
        final Map<String, Object> applicationMap = externalContext.getApplicationMap();

        Boolean scannedViewsExtensionless = (Boolean) applicationMap.get(FacesViews.SCANNED_VIEWS_EXTENSIONLESS);
        if (scannedViewsExtensionless == null) {
            if (externalContext.getInitParameter(FacesViews.FACES_VIEWS_SCANNED_VIEWS_EXTENSIONLESS_PARAM_NAME) == null) {
                scannedViewsExtensionless = true;
            } else {
                scannedViewsExtensionless = Boolean.valueOf(externalContext
                    .getInitParameter(FacesViews.FACES_VIEWS_SCANNED_VIEWS_EXTENSIONLESS_PARAM_NAME));
            }
            applicationMap.put(FacesViews.SCANNED_VIEWS_EXTENSIONLESS, scannedViewsExtensionless);
        }

        return scannedViewsExtensionless;
    }

    /**
     * Map the Facelets Servlet to the given extensions
     * 
     * @param extensions collections of extensions (typically those as
     *            encountered during scanning)
     */
    public static void mapFacesServlet(final ServletContext servletContext, final Set<String> extensions) {

        final ServletRegistration facesServletRegistration = Faces.getFacesServletRegistration(servletContext);
        if (facesServletRegistration != null) {
            final Collection<String> mappings = facesServletRegistration.getMappings();
            for (final String extension : extensions) {
                if (!mappings.contains(extension)) {
                    facesServletRegistration.addMapping(extension);
                }
            }
        }
    }

    public static String normalizeRootPath(final String rootPath) {
        String normalizedPath = rootPath;
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }
        if (!normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath + "/";
        }
        return normalizedPath;
    }

    /**
     * Scans for faces-views resources and stores the result at the designated
     * location "org.omnifaces.facesviews" in the ServletContext.
     * 
     * @param context
     * @return the view found during scanning, or the empty map if no views
     *         encountered
     */
    public static Map<String, String> scanAndStoreViews(final ServletContext context) {
        final Map<String, String> views = FacesViews.scanViews(context);
        if (!views.isEmpty()) {
            context.setAttribute(FacesViews.FACES_VIEWS_RESOURCES, Collections.unmodifiableMap(views));
            context.setAttribute(FacesViews.FACES_VIEWS_REVERSE_RESOURCES, Collections.unmodifiableMap(Utils
                .reverse(views)));
        }
        return views;
    }

    /**
     * Scans resources (views) recursively starting with the given resource
     * paths and returns a flat map containing all resources encountered.
     * 
     * @param servletContext
     * @return views
     */
    public static Map<String, String> scanViews(final ServletContext servletContext) {
        final Map<String, String> collectedViews = new HashMap<String, String>();
        FacesViews.scanViewsFromRootPaths(servletContext, collectedViews, null);
        return collectedViews;
    }

    /**
     * Scans resources (views) recursively starting with the given resource
     * paths for a specific root path, and collects those and all unique
     * extensions encountered in a flat map respectively set.
     * 
     * @param servletContext
     * @param rootPath one of the paths from which views are scanned. By default
     *            this is typically /WEB-INF/faces-view/
     * @param resourcePaths collection of paths to be considered for scanning,
     *            can be either files or directories.
     * @param collectedViews a mapping of all views encountered during scanning.
     *            Mapping will be from the simplified form to the actual
     *            location relatively to the web root. E.g key "foo", value
     *            "/WEB-INF/faces-view/foo.xhtml"
     * @param extensionToScan a specific extension to scan for. Should start
     *            with a ., e.g. ".xhtml". If this is given, only resources with
     *            that extension will be scanned. If null, all resources will be
     *            scanned.
     * @param collectedExtensions set in which all unique extensions will be
     *            collected. May be null, in which case no extensions will be
     *            collected
     */
    public static void scanViews(final ServletContext servletContext,
                                 final String rootPath,
                                 final Set<String> resourcePaths,
                                 final Map<String, String> collectedViews,
                                 final String extensionToScan,
                                 final Set<String> collectedExtensions) {

        if (!Utils.isEmpty(resourcePaths)) {
            for (final String resourcePath : resourcePaths) {
                if (ResourcePaths.isDirectory(resourcePath)) {
                    if (FacesViews.canScanDirectory(rootPath, resourcePath)) {
                        FacesViews.scanViews(servletContext, rootPath, servletContext.getResourcePaths(resourcePath),
                                             collectedViews, extensionToScan, collectedExtensions);
                    }
                } else if (FacesViews.canScanResource(resourcePath, extensionToScan)) {

                    // Strip the root path from the current path. E.g.
                    // /WEB-INF/faces-views/foo.xhtml will become foo.xhtml if
                    // the root path = /WEB-INF/faces-view/
                    final String resource = ResourcePaths.stripPrefixPath(rootPath, resourcePath);

                    // Store the resource with and without an extension, e.g.
                    // store both foo.xhtml and foo
                    collectedViews.put(resource, resourcePath);
                    collectedViews.put(ResourcePaths.stripExtension(resource), resourcePath);

                    // Optionally, collect all unique extensions that we have
                    // encountered
                    if (collectedExtensions != null) {
                        collectedExtensions.add("*" + ResourcePaths.getExtension(resourcePath));
                    }
                }
            }
        }
    }

    public static void scanViewsFromRootPaths(final ServletContext servletContext,
                                              final Map<String, String> collectedViews,
                                              final Set<String> collectedExtensions) {
        for (String rootPath : FacesViews.getRootPaths(servletContext)) {

            String extensionToScan = null;
            if (rootPath.contains("*")) {
                final String[] pathAndExtension = rootPath.split(Pattern.quote("*"));
                rootPath = pathAndExtension[0];
                extensionToScan = pathAndExtension[1];
            }

            rootPath = FacesViews.normalizeRootPath(rootPath);

            FacesViews.scanViews(servletContext, rootPath, servletContext.getResourcePaths(rootPath), collectedViews,
                                 extensionToScan, collectedExtensions);
        }
    }

    /**
     * Strips the special 'faces-views' prefix path from the resource if any.
     * 
     * @param resource
     * @return the resource without the special prefix path, or as-is if it
     *         didn't start with this prefix.
     */
    public static String stripFacesViewsPrefix(final String resource) {
        return ResourcePaths.stripPrefixPath(FacesViews.WEB_INF_VIEWS, resource);
    }

    /**
     * Checks if resources haven't been scanned yet, and if not does scanning
     * and stores the result at the designated location
     * "org.omnifaces.facesviews" in the ServletContext.
     * 
     * @param context
     */
    public static void tryScanAndStoreViews(final ServletContext context) {
        if (Faces.getApplicationAttribute(context, FacesViews.FACES_VIEWS_RESOURCES) == null) {
            FacesViews.scanAndStoreViews(context);
        }
    }

    private FacesViews() {
    }

}