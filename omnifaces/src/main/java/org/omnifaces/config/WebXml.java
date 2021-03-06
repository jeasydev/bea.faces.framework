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
package org.omnifaces.config;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This configuration enum parses the <code>/WEB-INF/web.xml</code> and all
 * <code>/META-INF/web-fragment</code> files found in the classpath and offers
 * methods to obtain information from them which is not available by the
 * standard Servlet API.
 * 
 * @author Bauke Scholtz
 * @since 1.2
 */
public enum WebXml {

    // Enum singleton
    // -------------------------------------------------------------------------------------------------

    /**
     * Returns the lazily loaded enum singleton instance.
     * <p>
     * Note: if this is needed in e.g. a {@link Filter} which is called before
     * the {@link FacesServlet} is invoked, then it won't work if the
     * <code>INSTANCE</code> hasn't been referenced before. Since JSF installs a
     * special "init" {@link FacesContext} during startup, one option for doing
     * this initial referencing is in a {@link ServletContextListener}. The data
     * this enum encapsulates will then be available even where there is no
     * {@link FacesContext} available. If there's no other option, then you need
     * to manually invoke {@link #init(ServletContext)} whereby you pass the
     * desired {@link ServletContext}.
     */
    INSTANCE;

    // Private constants
    // ----------------------------------------------------------------------------------------------

    private static final String WEB_XML = "/WEB-INF/web.xml";
    private static final String WEB_FRAGMENT_XML = "META-INF/web-fragment.xml";

    private static final String XPATH_WELCOME_FILE = "welcome-file-list/welcome-file";
    private static final String XPATH_EXCEPTION_TYPE = "error-page/exception-type";
    private static final String XPATH_LOCATION = "location";
    private static final String XPATH_ERROR_PAGE_500_LOCATION = "error-page[error-code=500]/location";
    private static final String XPATH_ERROR_PAGE_DEFAULT_LOCATION = "error-page[not(error-code) and not(exception-type)]/location";
    private static final String XPATH_FORM_LOGIN_PAGE = "login-config[auth-method='FORM']/form-login-config/form-login-page";
    private static final String XPATH_SECURITY_CONSTRAINT = "security-constraint";
    private static final String XPATH_WEB_RESOURCE_URL_PATTERN = "web-resource-collection/url-pattern";
    private static final String XPATH_AUTH_CONSTRAINT = "auth-constraint";
    private static final String XPATH_AUTH_CONSTRAINT_ROLE_NAME = "auth-constraint/role-name";

    private static final String ERROR_NOT_INITIALIZED = "WebXml is not initialized yet. Please use #init(ServletContext) method to manually initialize it.";
    private static final String ERROR_URL_MUST_START_WITH_SLASH = "URL must start with '/': '%s'";

    // Properties
    // -----------------------------------------------------------------------------------------------------

    /**
     * Returns an instance of {@link DocumentBuilder} which doesn't validate,
     * nor is namespace aware nor expands entity references (to keep it as
     * lenient as possible).
     */
    private static DocumentBuilder createDocumentBuilder() throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setExpandEntityReferences(false);
        return factory.newDocumentBuilder();
    }

    private static NodeList getNodeList(final Node node, final XPath xpath, final String expression) throws Exception {
        return (NodeList) xpath.compile(expression).evaluate(node, XPathConstants.NODESET);
    }

    private static boolean isExactMatch(final String urlPattern, final String url) {
        return url.equals(urlPattern.endsWith("/*") ? urlPattern.substring(0, urlPattern.length() - 2) : urlPattern);
    }

    private static boolean isPrefixMatch(final String urlPattern, final String url) {
        return urlPattern.endsWith("/*") ? url.startsWith(urlPattern.substring(0, urlPattern.length() - 2)) : false;
    }

    private static boolean isRoleMatch(final Set<String> roles, final String role) {
        return roles == null || roles.contains(role) || (role != null && roles.contains("*"));
    }

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    private static boolean isSuffixMatch(final String urlPattern, final String url) {
        return urlPattern.startsWith("*.") ? url.endsWith(urlPattern.substring(1)) : false;
    }

    /**
     * Load, merge and return all <code>web.xml</code> and
     * <code>web-fragment.xml</code> files found in the classpath into a single
     * {@link Document}.
     */
    private static Document loadWebXml(final ServletContext context) throws Exception {
        final DocumentBuilder builder = WebXml.createDocumentBuilder();
        final Document document = builder.newDocument();
        document.appendChild(document.createElement("web"));
        final URL url = context.getResource(WebXml.WEB_XML);

        if (url != null) { // Since Servlet 3.0, web.xml is optional.
            WebXml.parseAndAppendChildren(url, builder, document);
        }

        if (context.getMajorVersion() >= 3) { // web-fragment.xml exist only
                                              // since Servlet 3.0.
            final Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
                .getResources(WebXml.WEB_FRAGMENT_XML);

            while (urls.hasMoreElements()) {
                WebXml.parseAndAppendChildren(urls.nextElement(), builder, document);
            }
        }

        return document;
    }

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Parse the given URL as a document using the given builder and then append
     * all its child nodes to the given document.
     */
    private static void parseAndAppendChildren(final URL url, final DocumentBuilder builder, final Document document)
        throws Exception {
        final URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        InputStream input = null;

        try {
            input = connection.getInputStream();
            final NodeList children = builder.parse(input).getDocumentElement().getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                document.getDocumentElement().appendChild(document.importNode(children.item(i), true));
            }
        } finally {
            Utils.close(input);
        }
    }

    /**
     * Create and return a mapping of all error page locations by exception type
     * found in the given document.
     */
    @SuppressWarnings("unchecked")
    // For the cast on Class<Throwable>.
    private static Map<Class<Throwable>, String> parseErrorPageLocations(final Element webXml, final XPath xpath)
        throws Exception {
        final Map<Class<Throwable>, String> errorPageLocations = new LinkedHashMap<Class<Throwable>, String>();
        final NodeList exceptionTypes = WebXml.getNodeList(webXml, xpath, WebXml.XPATH_EXCEPTION_TYPE);

        for (int i = 0; i < exceptionTypes.getLength(); i++) {
            final Node node = exceptionTypes.item(i);
            final Class<Throwable> exceptionClass = (Class<Throwable>) Class.forName(node.getTextContent().trim());
            final String exceptionLocation = xpath.compile(WebXml.XPATH_LOCATION).evaluate(node.getParentNode()).trim();
            final Class<Throwable> key = (exceptionClass == Throwable.class) ? null : exceptionClass;

            if (!errorPageLocations.containsKey(key)) {
                errorPageLocations.put(key, exceptionLocation);
            }
        }

        if (!errorPageLocations.containsKey(null)) {
            String defaultLocation = xpath.compile(WebXml.XPATH_ERROR_PAGE_500_LOCATION).evaluate(webXml).trim();

            if (Utils.isEmpty(defaultLocation)) {
                defaultLocation = xpath.compile(WebXml.XPATH_ERROR_PAGE_DEFAULT_LOCATION).evaluate(webXml).trim();
            }

            if (!Utils.isEmpty(defaultLocation)) {
                errorPageLocations.put(null, defaultLocation);
            }
        }

        return Collections.unmodifiableMap(errorPageLocations);
    }

    /**
     * Return the location of the FORM authentication login page.
     */
    private static String parseFormLoginPage(final Element webXml, final XPath xpath) throws Exception {
        final String formLoginPage = xpath.compile(WebXml.XPATH_FORM_LOGIN_PAGE).evaluate(webXml).trim();
        return Utils.isEmpty(formLoginPage) ? null : formLoginPage;
    }

    /**
     * Create and return a mapping of all security constraint URL patterns and
     * the associated roles.
     */
    private static Map<String, Set<String>> parseSecurityConstraints(final Element webXml, final XPath xpath)
        throws Exception {
        final Map<String, Set<String>> securityConstraints = new LinkedHashMap<String, Set<String>>();
        final NodeList constraints = WebXml.getNodeList(webXml, xpath, WebXml.XPATH_SECURITY_CONSTRAINT);

        for (int i = 0; i < constraints.getLength(); i++) {
            final Node constraint = constraints.item(i);
            Set<String> roles = null;
            final NodeList auth = WebXml.getNodeList(constraint, xpath, WebXml.XPATH_AUTH_CONSTRAINT);

            if (auth.getLength() > 0) {
                final NodeList authRoles = WebXml
                    .getNodeList(constraint, xpath, WebXml.XPATH_AUTH_CONSTRAINT_ROLE_NAME);
                roles = new HashSet<String>(authRoles.getLength());

                for (int j = 0; j < authRoles.getLength(); j++) {
                    roles.add(authRoles.item(j).getTextContent().trim());
                }

                roles = Collections.unmodifiableSet(roles);
            }

            final NodeList urlPatterns = WebXml.getNodeList(constraint, xpath, WebXml.XPATH_WEB_RESOURCE_URL_PATTERN);

            for (int j = 0; j < urlPatterns.getLength(); j++) {
                final String urlPattern = urlPatterns.item(j).getTextContent().trim();
                securityConstraints.put(urlPattern, roles);
            }
        }

        return Collections.unmodifiableMap(securityConstraints);
    }

    /**
     * Create and return a list of all welcome files.
     */
    private static List<String> parseWelcomeFiles(final Element webXml, final XPath xpath) throws Exception {
        final NodeList welcomeFileList = WebXml.getNodeList(webXml, xpath, WebXml.XPATH_WELCOME_FILE);
        final List<String> welcomeFiles = new ArrayList<String>(welcomeFileList.getLength());

        for (int i = 0; i < welcomeFileList.getLength(); i++) {
            welcomeFiles.add(welcomeFileList.item(i).getTextContent().trim());
        }

        return Collections.unmodifiableList(welcomeFiles);
    }

    private final AtomicBoolean initialized = new AtomicBoolean();

    // Getters
    // --------------------------------------------------------------------------------------------------------

    private List<String> welcomeFiles;

    private Map<Class<Throwable>, String> errorPageLocations;

    private String formLoginPage;

    private Map<String, Set<String>> securityConstraints;

    /**
     * Perform initialization.
     */
    private WebXml() {
        if (Faces.getContext() != null) {
            final ServletContext servletContext = Faces.getServletContext();

            if (servletContext != null) {
                init(servletContext);
            }
        }
    }

    // Helpers
    // --------------------------------------------------------------------------------------------------------

    private void checkInitialized() {
        if (!initialized.get()) {
            throw new IllegalStateException(WebXml.ERROR_NOT_INITIALIZED);
        }
    }

    /**
     * Find for the given exception the right error page location as per Servlet
     * 3.0 specification 10.9.2:
     * <ul>
     * <li>Make a first pass through all specific exception types. If an exact
     * match is found, use its location.
     * <li>Else make a second pass through all specific exception types in the
     * order as they are declared in web.xml. If the current exception is an
     * instance of it, then use its location.
     * <li>Else use the default error page location, which can be either the
     * java.lang.Throwable or HTTP 500 or default one.
     * </ul>
     * 
     * @param exception The exception to find the error page location for.
     * @return The right error page location for the given exception.
     */
    public String findErrorPageLocation(final Throwable exception) {
        final Map<Class<Throwable>, String> errorPageLocations = getErrorPageLocations();

        for (final Entry<Class<Throwable>, String> entry : errorPageLocations.entrySet()) {
            if (entry.getKey() == exception.getClass()) {
                return entry.getValue();
            }
        }

        for (final Entry<Class<Throwable>, String> entry : errorPageLocations.entrySet()) {
            if (entry.getKey() != null && entry.getKey().isInstance(exception)) {
                return entry.getValue();
            }
        }

        return errorPageLocations.get(null);
    }

    /**
     * Returns a mapping of all error page locations by exception type. The
     * default location is identified by <code>null</code> key.
     * 
     * @return A mapping of all error page locations by exception type.
     */
    public Map<Class<Throwable>, String> getErrorPageLocations() {
        checkInitialized();
        return errorPageLocations;
    }

    /**
     * Returns the location of the FORM authentication login page, or
     * <code>null</code> if it is not defined.
     * 
     * @return The location of the FORM authentication login page, or
     *         <code>null</code> if it is not defined.
     */
    public String getFormLoginPage() {
        checkInitialized();
        return formLoginPage;
    }

    /**
     * Returns a mapping of all security constraint URL patterns and the
     * associated roles in the declared order. If the roles is <code>null</code>
     * , then it means that no auth constraint is been set (i.e. the resource is
     * publicly accessible). If the roles is empty, then it means that an empty
     * auth constraint is been set (i.e. the resource is in no way accessible).
     * 
     * @return A mapping of all security constraint URL patterns and the
     *         associated roles in the declared order.
     * @since 1.4
     */
    public Map<String, Set<String>> getSecurityConstraints() {
        checkInitialized();
        return securityConstraints;
    }

    /**
     * Returns a list of all welcome files.
     * 
     * @return A list of all welcome files.
     * @since 1.4
     */
    public List<String> getWelcomeFiles() {
        checkInitialized();
        return welcomeFiles;
    }

    /**
     * Perform manual initialization with the given servlet context, if not
     * already initialized yet.
     * 
     * @param servletContext The servlet context to obtain the web.xml from.
     * @return The current {@link WebXml} instance, initialized and all.
     */
    public WebXml init(final ServletContext servletContext) {
        if (!initialized.getAndSet(true)) {
            try {
                final Element webXml = WebXml.loadWebXml(servletContext).getDocumentElement();
                final XPath xpath = XPathFactory.newInstance().newXPath();
                welcomeFiles = WebXml.parseWelcomeFiles(webXml, xpath);
                errorPageLocations = WebXml.parseErrorPageLocations(webXml, xpath);
                formLoginPage = WebXml.parseFormLoginPage(webXml, xpath);
                securityConstraints = WebXml.parseSecurityConstraints(webXml, xpath);
            } catch (final Exception e) {
                // If this occurs, web.xml is broken anyway and app shouldn't
                // have started/initialized this far at all.
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    // Helpers of helpers (JAXP hell)
    // ---------------------------------------------------------------------------------

    /**
     * Returns <code>true</code> if access to the given URL is allowed for the
     * given role. URL patterns are matched as per Servlet 3.0 specification
     * 12.1:
     * <ul>
     * <li>Make a first pass through all URL patterns. If an exact match is
     * found, then check the role on it.
     * <li>Else make a recursive pass through all prefix URL patterns, stepping
     * down the URL one directory at a time, trying to find the longest path
     * match. If it is found, then check the role on it.
     * <li>Else if the last segment in the URL path contains an extension, then
     * make a last pass through all suffix URL patterns. If a match is found,
     * then check the role on it.
     * <li>Else assume it as unprotected resource and return <code>true</code>.
     * </ul>
     * 
     * @param url URL to be checked for access by the given role. It must start
     *            with '/' and be context-relative.
     * @param role Role to be checked for access to the given URL.
     * @return <code>true</code> if access to the given URL is allowed for the
     *         given role, otherwise <code>false</code>.
     * @throws NullPointerException If given URL is null.
     * @throws IllegalArgumentException If given URL does not start with '/'.
     * @since 1.4
     */
    public boolean isAccessAllowed(String url, final String role) {
        final Map<String, Set<String>> securityConstraints = getSecurityConstraints();

        if (url.charAt(0) != ('/')) {
            throw new IllegalArgumentException(String.format(WebXml.ERROR_URL_MUST_START_WITH_SLASH, url));
        }

        if (url.length() > 1 && url.charAt(url.length() - 1) == '/') {
            url = url.substring(0, url.length() - 1); // Trim trailing slash.
        }

        for (final Entry<String, Set<String>> entry : securityConstraints.entrySet()) {
            if (WebXml.isExactMatch(entry.getKey(), url)) {
                return WebXml.isRoleMatch(entry.getValue(), role);
            }
        }

        for (String path = url, urlMatch = ""; !path.isEmpty(); path = path.substring(0, path.lastIndexOf('/'))) {
            Boolean roleMatch = null;

            for (final Entry<String, Set<String>> entry : securityConstraints.entrySet()) {
                if (urlMatch.length() < entry.getKey().length() && WebXml.isPrefixMatch(entry.getKey(), path)) {
                    urlMatch = entry.getKey();
                    roleMatch = WebXml.isRoleMatch(entry.getValue(), role);
                }
            }

            if (roleMatch != null) {
                return roleMatch;
            }
        }

        if (url.contains(".")) {
            for (final Entry<String, Set<String>> entry : securityConstraints.entrySet()) {
                if (WebXml.isSuffixMatch(url, entry.getKey())) {
                    return WebXml.isRoleMatch(entry.getValue(), role);
                }
            }
        }

        return true;
    }

}