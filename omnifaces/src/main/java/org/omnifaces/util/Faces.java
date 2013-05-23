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
package org.omnifaces.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ProjectStage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;
import javax.faces.context.Flash;
import javax.faces.context.PartialViewContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewMetadata;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Collection of utility methods for the JSF API that are mainly shortcuts for
 * obtaining stuff from the thread local {@link FacesContext}. In effect, it
 * 'flattens' the hierarchy of nested objects.
 * <p>
 * Do note that using the hierarchy is actually a better software design
 * practice, but can lead to verbose code.
 * <p>
 * In addition, note that there's normally a minor overhead in obtaining the
 * thread local {@link FacesContext}. In case client code needs to call methods
 * in this class multiple times it's expected that performance will be slightly
 * better if instead the {@link FacesContext} is obtained once and the required
 * methods are called on that, although the difference is practically negligible
 * when used in modern server hardware.
 * 
 * @author Arjan Tijms
 * @author Bauke Scholtz
 */
public final class Faces {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    /**
     * Inner class so that the protected
     * {@link FacesContext#setCurrentInstance(FacesContext)} method can be
     * invoked.
     * 
     * @author Bauke Scholtz
     */
    private static abstract class FacesContextSetter extends FacesContext {
        protected static void setCurrentInstance(final FacesContext context) {
            FacesContext.setCurrentInstance(context);
        }
    }

    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    private static final int DEFAULT_SENDFILE_BUFFER_SIZE = 10240;

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    private static final String ERROR_NO_VIEW = "There is no UIViewRoot.";

    // JSF general
    // ----------------------------------------------------------------------------------------------------

    /**
     * Add a cookie with given name, value, path and maxage to the HTTP
     * response. The cookie value will implicitly be URL-encoded with UTF-8 so
     * that any special characters can be stored in the cookie. The cookie will
     * implicitly be set to secure when the current request is secure (i.e. when
     * the current request is a HTTPS request).
     * 
     * @param name The cookie name.
     * @param value The cookie value.
     * @param path The cookie path. If this is <code>/</code>, then the cookie
     *            is available in all pages of the webapp. If this is
     *            <code>/somespecificpath</code>, then the cookie is only
     *            available in pages under the specified path.
     * @param maxAge The maximum age of the cookie, in seconds. If this is
     *            <code>0</code>, then the cookie will be removed. Note that the
     *            name and path must be exactly the same as it was when the
     *            cookie was created. If this is <code>-1</code> then the cookie
     *            will become a session cookie and thus live as long as the
     *            established HTTP session.
     * @throws UnsupportedOperationException If UTF-8 is not supported on this
     *             machine.
     * @see ExternalContext#addResponseCookie(String, String, Map)
     */
    public static void addResponseCookie(final String name, String value, final String path, final int maxAge) {
        if (value != null) {
            value = Utils.encodeURL(value);
        }

        final ExternalContext externalContext = Faces.getExternalContext();
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("path", path);
        properties.put("maxAge", maxAge);
        properties.put("secure", ((HttpServletRequest) externalContext.getRequest()).isSecure());
        externalContext.addResponseCookie(name, value, properties);
    }

    /**
     * Add a header with given name and value to the HTTP response.
     * 
     * @param name The header name.
     * @param value The header value.
     * @see ExternalContext#addResponseHeader(String, String)
     */
    public static void addResponseHeader(final String name, final String value) {
        Faces.getExternalContext().addResponseHeader(name, value);
    }

    /**
     * Trigger the default container managed authentication mechanism on the
     * current request. It expects the username and password being available as
     * predefinied request parameters on the current request and/or a custom
     * JASPIC implementation.
     * 
     * @return <code>true</code> if the authentication was successful, otherwise
     *         <code>false</code>.
     * @throws ServletException When the authentication has failed. The caller
     *             is responsible for handling it.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     * @see HttpServletRequest#authenticate(HttpServletResponse)
     * @since 1.4
     */
    public static boolean authenticate() throws ServletException, IOException {
        return Faces.getRequest().authenticate(Faces.getResponse());
    }

    /**
     * Helper method to encode the given URL parameters using UTF-8.
     */
    private static Object[] encodeURLParams(final String... params) {
        if (params == null) {
            return new Object[0];
        } else {
            final Object[] encodedParams = new Object[params.length];

            for (int i = 0; i < params.length; i++) {
                encodedParams[i] = Utils.encodeURL(params[i]);
            }

            return encodedParams;
        }
    }

    /**
     * Programmatically evaluate the given EL expression and return the
     * evaluated value.
     * 
     * @param <T> The expected return type.
     * @param expression The EL expression to be evaluated.
     * @return The evaluated value of the given EL expression.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see Application#evaluateExpressionGet(FacesContext, String, Class)
     */
    @SuppressWarnings("unchecked")
    public static <T> T evaluateExpressionGet(final String expression) {
        if (expression == null) {
            return null;
        }

        final FacesContext context = Faces.getContext();
        return (T) context.getApplication().evaluateExpressionGet(context, expression, Object.class);
    }

    /**
     * Programmatically evaluate the given EL expression and set the given
     * value.
     * 
     * @param expression The EL expression to be evaluated.
     * @param value The value to be set in the property behind the EL
     *            expression.
     * @see Application#getExpressionFactory()
     * @see ExpressionFactory#createValueExpression(ELContext, String, Class)
     * @see ValueExpression#setValue(ELContext, Object)
     * @since 1.1
     */
    public static void evaluateExpressionSet(final String expression, final Object value) {
        final FacesContext context = Faces.getContext();
        final ELContext elContext = context.getELContext();
        final ValueExpression valueExpression = context.getApplication().getExpressionFactory()
            .createValueExpression(elContext, expression, Object.class);
        valueExpression.setValue(elContext, value);
    }

    /**
     * Returns the application singleton.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The faces application singleton.
     * @see FacesContext#getApplication()
     */
    public static Application getApplication() {
        return Faces.getContext().getApplication();
    }

    /**
     * Returns the application scope attribute value associated with the given
     * name.
     * 
     * @param name The application scope attribute name.
     * @param context The faces context used for looking up the attribute.
     * 
     * @return The application scope attribute value associated with the given
     *         name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getApplicationMap()
     * @since 1.4
     */
    @SuppressWarnings("unchecked")
    public static <T> T getApplicationAttribute(final FacesContext context, final String name) {
        return (T) context.getExternalContext().getApplicationMap().get(name);
    }

    /**
     * Returns the application scope attribute value associated with the given
     * name.
     * 
     * @param name The application scope attribute name.
     * @param context The servlet context used for looking up the attribute.
     * 
     * @return The application scope attribute value associated with the given
     *         name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ServletContext#getAttribute(String)
     * @since 1.4
     */
    @SuppressWarnings("unchecked")
    public static <T> T getApplicationAttribute(final ServletContext context, final String name) {
        return (T) context.getAttribute(name);
    }

    /**
     * Returns the application scope attribute value associated with the given
     * name.
     * 
     * @param name The application scope attribute name.
     * @return The application scope attribute value associated with the given
     *         name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getApplicationMap()
     */
    @SuppressWarnings("unchecked")
    public static <T> T getApplicationAttribute(final String name) {
        return (T) Faces.getApplicationMap().get(name);
    }

    /**
     * Gets the JSF Application singleton from the FactoryFinder.
     * <p>
     * This method is an alternative for {@link Faces#getApplication()} for
     * those situations where the {@link FacesContext} isn't available.
     * 
     * @return The faces application singleton.
     */
    public static Application getApplicationFromFactory() {
        return ((ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY)).getApplication();
    }

    /**
     * Returns the application scope map.
     * 
     * @return The application scope map.
     * @see ExternalContext#getApplicationMap()
     */
    public static Map<String, Object> getApplicationMap() {
        return Faces.getExternalContext().getApplicationMap();
    }

    /**
     * Returns the current faces context.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The current faces context.
     * @see FacesContext#getCurrentInstance()
     */
    public static FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Returns the faces context that's stored in an ELContext.
     * <p>
     * Note that this only works for an ELContext that is created in the context
     * of JSF.
     * 
     * @param elContext the EL context to obtain the faces context from.
     * @return the faces context that's stored in the given ELContext.
     * @since 1.2
     */
    public static FacesContext getContext(final ELContext elContext) {
        return (FacesContext) elContext.getContext(FacesContext.class);
    }

    /**
     * Returns the Faces context attribute value associated with the given name.
     * 
     * @param name The Faces context attribute name.
     * @return The Faces context attribute value associated with the given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see FacesContext#getAttributes()
     * @since 1.3
     */
    @SuppressWarnings("unchecked")
    public static <T> T getContextAttribute(final String name) {
        return (T) Faces.getContext().getAttributes().get(name);
    }

    /**
     * Returns the current phase ID.
     * 
     * @return The current phase ID.
     * @see FacesContext#getCurrentPhaseId()
     */
    public static PhaseId getCurrentPhaseId() {
        return Faces.getContext().getCurrentPhaseId();
    }

    /**
     * Returns the default locale, or <code>null</code> if there is none.
     * 
     * @return The default locale, or <code>null</code> if there is none.
     * @see Application#getDefaultLocale()
     */
    public static Locale getDefaultLocale() {
        return Faces.getApplication().getDefaultLocale();
    }

    /**
     * Returns the current external context.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The current external context.
     * @see FacesContext#getExternalContext()
     */
    public static ExternalContext getExternalContext() {
        return Faces.getContext().getExternalContext();
    }

    // JSF views
    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the Facelet attribute value associated with the given name. This
     * basically returns the value of the <code>&lt;ui:param&gt;</code> which is
     * been declared inside the Facelet file, or is been passed into the Facelet
     * file by e.g. an <code>&lt;ui:include&gt;</code>.
     * 
     * @param name The Facelet attribute name.
     * @return The Facelet attribute value associated with the given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see FaceletContext#getAttribute(String)
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFaceletAttribute(final String name) {
        return (T) Faces.getFaceletContext().getAttribute(name);
    }

    /**
     * Returns the Facelet context.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The Facelet context.
     * @see FaceletContext
     * @since 1.1
     */
    public static FaceletContext getFaceletContext() {
        return (FaceletContext) Faces.getContext().getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
    }

    /**
     * Gets the ServletRegistration associated with the FacesServlet.
     * 
     * @param servletContext the context to get the ServletRegistration from.
     * @return ServletRegistration for FacesServlet, or null if the FacesServlet
     *         is not installed.
     * @since 1.4
     */
    public static ServletRegistration getFacesServletRegistration(final ServletContext servletContext) {
        ServletRegistration facesServletRegistration = null;
        for (final ServletRegistration registration : servletContext.getServletRegistrations().values()) {
            if (registration.getClassName().equals(FacesServlet.class.getName())) {
                facesServletRegistration = registration;
                break;
            }
        }

        return facesServletRegistration;
    }

    /**
     * Returns the flash scope. Note that <code>Flash</code> implements
     * <code>Map&lt;String, Object&gt;</code>, so you can just treat it like a
     * <code>Map&lt;String, Object&gt;</code>.
     * 
     * @return The flash scope.
     * @see ExternalContext#getFlash()
     */
    public static Flash getFlash() {
        return Faces.getExternalContext().getFlash();
    }

    /**
     * Returns the flash scope attribute value associated with the given name.
     * 
     * @param name The flash scope attribute name.
     * @return The flash scope attribute value associated with the given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getFlash()
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFlashAttribute(final String name) {
        return (T) Faces.getFlash().get(name);
    }

    /**
     * Returns the implementation information of currently loaded JSF
     * implementation. E.g. "Mojarra 2.1.7-FCS".
     * 
     * @return The implementation information of currently loaded JSF
     *         implementation.
     * @see Package#getImplementationTitle()
     * @see Package#getImplementationVersion()
     */
    public static String getImplInfo() {
        final Package jsfPackage = FacesContext.class.getPackage();
        return jsfPackage.getImplementationTitle() + " " + jsfPackage.getImplementationVersion();
    }

    /**
     * Returns the application initialization parameter. This returns the
     * <code>&lt;param-value&gt;</code> of a <code>&lt;context-param&gt;</code>
     * in <code>web.xml</code> associated with the given
     * <code>&lt;param-name&gt;</code>.
     * 
     * @param name The application initialization parameter name.
     * @return The application initialization parameter value associated with
     *         the given name, or <code>null</code> if there is none.
     * @see ExternalContext#getInitParameter(String)
     * @since 1.1
     */
    public static String getInitParameter(final String name) {
        return Faces.getExternalContext().getInitParameter(name);
    }

    /**
     * Returns the application initialization parameter map. This returns the
     * parameter name-value pairs of all <code>&lt;context-param&gt;</code>
     * entries in in <code>web.xml</code>.
     * 
     * @return The application initialization parameter map.
     * @see ExternalContext#getInitParameterMap()
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getInitParameterMap() {
        return Faces.getExternalContext().getInitParameterMap();
    }

    /**
     * Returns the current locale. If the locale set in the JSF view root is not
     * null, then return it. Else if the client preferred locale is not null and
     * is among supported locales, then return it. Else if the JSF default
     * locale is not null, then return it. Else return the system default
     * locale.
     * 
     * @return The current locale.
     * @see UIViewRoot#getLocale()
     * @see ExternalContext#getRequestLocale()
     * @see Application#getDefaultLocale()
     * @see Locale#getDefault()
     */
    public static Locale getLocale() {
        final FacesContext context = Faces.getContext();
        Locale locale = null;
        final UIViewRoot viewRoot = context.getViewRoot();

        // Prefer the locale set in the view.
        if (viewRoot != null) {
            locale = viewRoot.getLocale();
        }

        // Then the client preferred locale.
        if (locale == null) {
            final Locale clientLocale = context.getExternalContext().getRequestLocale();

            if (Faces.getSupportedLocales().contains(clientLocale)) {
                locale = clientLocale;
            }
        }

        // Then the JSF default locale.
        if (locale == null) {
            locale = context.getApplication().getDefaultLocale();
        }

        // Finally the system default locale.
        if (locale == null) {
            locale = Locale.getDefault();
        }

        return locale;
    }

    /**
     * Determines and returns the faces servlet mapping used in the current
     * request. If JSF is prefix mapped (e.g. <code>/faces/*</code>), then this
     * returns the whole path, with a leading slash (e.g. <code>/faces</code>).
     * If JSF is suffix mapped (e.g. <code>*.xhtml</code>), then this returns
     * the whole extension (e.g. <code>.xhtml</code>).
     * 
     * @return The faces servlet mapping (without the wildcard).
     * @see #getRequestPathInfo()
     * @see #getRequestServletPath()
     */
    public static String getMapping() {
        final ExternalContext externalContext = Faces.getExternalContext();

        if (externalContext.getRequestPathInfo() == null) {
            final String path = externalContext.getRequestServletPath();
            return path.substring(path.lastIndexOf('.'));
        } else {
            return externalContext.getRequestServletPath();
        }
    }

    /**
     * Returns the metadata attribute of the current view associated with the
     * given name. Note: this is not the same as the view scope, for that use
     * {@link #getViewAttribute(String)}.
     * 
     * @param name The metadata attribute name.
     * @return The metadata attribute of the current view associated with the
     *         given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see UIViewRoot#getAttributes()
     * @since 1.4
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMetadataAttribute(final String name) {
        return (T) Faces.getViewRoot().getAttributes().get(name);
    }

    /**
     * Returns the metadata attribute of the given view ID associated with the
     * given name. Note: this is not the same as the view scope, for that use
     * {@link #getViewAttribute(String)}.
     * 
     * @param viewId The view ID to return the metadata attribute for.
     * @param name The metadata attribute name.
     * @return The metadata attribute of the given view ID associated with the
     *         given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ViewDeclarationLanguage#getViewMetadata(FacesContext, String)
     * @since 1.4
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMetadataAttribute(final String viewId, final String name) {
        return (T) Faces.getMetadataAttributes(viewId).get(name);
    }

    /**
     * Returns the metadata attribute map of the given view ID, or an empty map
     * if there is no view metadata.
     * 
     * @param viewId The view ID to return the metadata attribute map for.
     * @return The metadata attribute map of the given view ID, or an empty map
     *         if there is no view metadata.
     * @see ViewDeclarationLanguage#getViewMetadata(FacesContext, String)
     * @since 1.4
     */
    public static Map<String, Object> getMetadataAttributes(final String viewId) {
        final FacesContext context = Faces.getContext();
        final ViewHandler viewHandler = context.getApplication().getViewHandler();
        final ViewDeclarationLanguage vdl = viewHandler.getViewDeclarationLanguage(context, viewId);
        final ViewMetadata metadata = vdl.getViewMetadata(context, viewId);

        return (metadata != null) ? metadata.createMetadataView(context).getAttributes() : Collections
            .<String, Object> emptyMap();
    }

    // Facelets
    // -------------------------------------------------------------------------------------------------------

    /**
     * Returns the mime type for the given file name. The mime type is
     * determined based on file extension and configureable by
     * <code>&lt;mime-mapping&gt;</code> entries in <code>web.xml</code>. When
     * the mime type is unknown, then a default of
     * <code>application/octet-stream</code> will be returned.
     * 
     * @param name The file name to return the mime type for.
     * @return The mime type for the given file name.
     * @see ExternalContext#getMimeType(String)
     */
    public static String getMimeType(final String name) {
        String mimeType = Faces.getExternalContext().getMimeType(name);

        if (mimeType == null) {
            mimeType = Faces.DEFAULT_MIME_TYPE;
        }

        return mimeType;
    }

    /**
     * Returns the absolute disk file system path representation of the given
     * web content path. This thus converts the given path of a web content
     * resource (e.g. <code>/index.xhtml</code>) to an absolute disk file system
     * path (e.g. <code>/path/to/server/work/folder/some.war/index.xhtml</code>)
     * which can then be used in {@link File}, {@link FileInputStream}, etc.
     * <p>
     * Note that this will return <code>null</code> when the WAR is not expanded
     * into the disk file system, but instead into memory. If all you want is
     * just an {@link InputStream} of the web content resource, then better use
     * {@link #getResourceAsStream(String)} instead.
     * <p>
     * Also note that it wouldn't make sense to modify or create files in this
     * location, as those changes would get lost anyway when the WAR is
     * redeployed or even when the server is restarted. This is thus absolutely
     * not a good location to store for example uploaded files.
     * 
     * @param webContentPath The web content path to be converted to an absolute
     *            disk file system path.
     * @return The absolute disk file system path representation of the given
     *         web content path.
     * @since 1.2
     */
    public static String getRealPath(final String webContentPath) {
        return Faces.getExternalContext().getRealPath(webContentPath);
    }

    /**
     * Returns the Internet Protocol (IP) address of the client that sent the
     * request. This will first check the <code>X-Forwarded-For</code> request
     * header and if it's present, then return its first IP address, else just
     * return {@link HttpServletRequest#getRemoteAddr()} unmodified.
     * 
     * @return The IP address of the client.
     * @see HttpServletRequest#getRemoteAddr()
     * @since 1.2
     */
    public static String getRemoteAddr() {
        final String forwardedFor = Faces.getRequestHeader("X-Forwarded-For");

        if (!Utils.isEmpty(forwardedFor)) {
            return forwardedFor.split("\\s*,\\s*", 2)[0]; // It's a comma
                                                          // separated string:
                                                          // client,proxy1,proxy2,...
        }

        return Faces.getRequest().getRemoteAddr();
    }

    /**
     * Returns the name of the logged-in user for container managed FORM based
     * authentication, if any.
     * 
     * @return The name of the logged-in user for container managed FORM based
     *         authentication, if any.
     * @see ExternalContext#getRemoteUser()
     */
    public static String getRemoteUser() {
        return Faces.getExternalContext().getRemoteUser();
    }

    // HTTP request
    // ---------------------------------------------------------------------------------------------------

    /**
     * Returns the HTTP servlet request.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The HTTP servlet request.
     * @see ExternalContext#getRequest()
     */
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) Faces.getExternalContext().getRequest();
    }

    /**
     * Returns the request scope attribute value associated with the given name.
     * 
     * @param context The faces context used for looking up the attribute.
     * @param name The request scope attribute name.
     * 
     * @return The request scope attribute value associated with the given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getRequestMap()
     * @since 1.4
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRequestAttribute(final FacesContext context, final String name) {
        return (T) context.getExternalContext().getRequestMap().get(name);
    }

    /**
     * Returns the request scope attribute value associated with the given name.
     * 
     * @param name The request scope attribute name.
     * @return The request scope attribute value associated with the given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getRequestMap()
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRequestAttribute(final String name) {
        return (T) Faces.getRequestMap().get(name);
    }

    /**
     * Returns the HTTP request base URL. This is the URL from the scheme,
     * domain until with context path, including the trailing slash. This is the
     * value you could use in HTML <code>&lt;base&gt;</code> tag.
     * 
     * @return The HTTP request base URL.
     * @see HttpServletRequest#getRequestURL()
     * @see HttpServletRequest#getRequestURI()
     * @see HttpServletRequest#getContextPath()
     */
    public static String getRequestBaseURL() {
        return Faces.getRequestBaseURL(Faces.getRequest());
    }

    /**
     * Returns the HTTP request base URL. This is the URL from the scheme,
     * domain until with context path, including the trailing slash. This is the
     * value you could use in HTML <code>&lt;base&gt;</code> tag.
     * 
     * @param request The request for which the base URL is computed.
     * 
     * @return The HTTP request base URL.
     * @see HttpServletRequest#getRequestURL()
     * @see HttpServletRequest#getRequestURI()
     * @see HttpServletRequest#getContextPath()
     */
    public static String getRequestBaseURL(final HttpServletRequest request) {
        final String url = request.getRequestURL().toString();
        return url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
    }

    /**
     * Returns the HTTP request context path. It's the webapp context name, with
     * a leading slash. If the webapp runs on context root, then it returns an
     * empty string.
     * 
     * @return The HTTP request context path.
     * @see ExternalContext#getRequestContextPath()
     */
    public static String getRequestContextPath() {
        return Faces.getExternalContext().getRequestContextPath();
    }

    /**
     * Returns the value of the HTTP request cookie associated with the given
     * name. The value is implicitly URL-decoded with a charset of UTF-8.
     * 
     * @param name The HTTP request cookie name.
     * @return The value of the HTTP request cookie associated with the given
     *         name.
     * @throws UnsupportedOperationException If UTF-8 is not supported on this
     *             machine.
     * @see ExternalContext#getRequestCookieMap()
     */
    public static String getRequestCookie(final String name) {
        final Cookie cookie = (Cookie) Faces.getExternalContext().getRequestCookieMap().get(name);
        return (cookie != null) ? Utils.decodeURL(cookie.getValue()) : null;
    }

    /**
     * Returns the HTTP request domain URL. This is the URL with the scheme and
     * domain, without any trailing slash.
     * 
     * @return The HTTP request domain URL.
     * @see HttpServletRequest#getRequestURL()
     * @see HttpServletRequest#getRequestURI()
     * @since 1.1
     */
    public static String getRequestDomainURL() {
        final HttpServletRequest request = Faces.getRequest();
        final String url = request.getRequestURL().toString();
        return url.substring(0, url.length() - request.getRequestURI().length());
    }

    /**
     * Returns the HTTP request header value associated with the given name.
     * 
     * @param name The HTTP request header name.
     * @return The HTTP request header value associated with the given name.
     * @see ExternalContext#getRequestHeaderMap()
     */
    public static String getRequestHeader(final String name) {
        return Faces.getRequestHeaderMap().get(name);
    }

    /**
     * Returns the HTTP request header map.
     * 
     * @return The HTTP request header map.
     * @see ExternalContext#getRequestHeaderMap()
     */
    public static Map<String, String> getRequestHeaderMap() {
        return Faces.getExternalContext().getRequestHeaderMap();
    }

    /**
     * Returns the HTTP request header values associated with the given name.
     * 
     * @param name The HTTP request header name.
     * @return The HTTP request header values associated with the given name.
     * @see ExternalContext#getRequestHeaderValuesMap()
     */
    public static String[] getRequestHeaderValues(final String name) {
        return Faces.getRequestHeaderValuesMap().get(name);
    }

    /**
     * Returns the HTTP request header values map.
     * 
     * @return The HTTP request header values map.
     * @see ExternalContext#getRequestHeaderValuesMap()
     */
    public static Map<String, String[]> getRequestHeaderValuesMap() {
        return Faces.getExternalContext().getRequestHeaderValuesMap();
    }

    /**
     * Returns the request scope map.
     * 
     * @return The request scope map.
     * @see ExternalContext#getRequestMap()
     */
    public static Map<String, Object> getRequestMap() {
        return Faces.getExternalContext().getRequestMap();
    }

    /**
     * Returns the HTTP request parameter value associated with the given name.
     * 
     * @param name The HTTP request parameter name.
     * @return The HTTP request parameter value associated with the given name.
     * @see ExternalContext#getRequestParameterMap()
     */
    public static String getRequestParameter(final String name) {
        return Faces.getRequestParameterMap().get(name);
    }

    /**
     * Returns the HTTP request parameter map.
     * 
     * @return The HTTP request parameter map.
     * @see ExternalContext#getRequestParameterMap()
     */
    public static Map<String, String> getRequestParameterMap() {
        return Faces.getExternalContext().getRequestParameterMap();
    }

    /**
     * Returns the HTTP request parameter values associated with the given name.
     * 
     * @param name The HTTP request parameter name.
     * @return The HTTP request parameter values associated with the given name.
     * @see ExternalContext#getRequestParameterValuesMap()
     */
    public static String[] getRequestParameterValues(final String name) {
        return Faces.getRequestParameterValuesMap().get(name);
    }

    /**
     * Returns the HTTP request parameter values map.
     * 
     * @return The HTTP request parameter values map.
     * @see ExternalContext#getRequestParameterValuesMap()
     */
    public static Map<String, String[]> getRequestParameterValuesMap() {
        return Faces.getExternalContext().getRequestParameterValuesMap();
    }

    /**
     * Returns the HTTP request path info. If JSF is prefix mapped (e.g.
     * <code>/faces/*</code>), then this returns the whole part after the prefix
     * mapping, with a leading slash. If JSF is suffix mapped (e.g.
     * <code>*.xhtml</code>), then this returns <code>null</code>.
     * 
     * @return The HTTP request path info.
     * @see ExternalContext#getRequestPathInfo()
     */
    public static String getRequestPathInfo() {
        return Faces.getExternalContext().getRequestPathInfo();
    }

    /**
     * Returns the HTTP request query string. This is the part after the
     * <code>?</code> in the request URL as the enduser sees in browser address
     * bar.
     * 
     * @return The HTTP request query string.
     * @see HttpServletRequest#getQueryString()
     * @since 1.1
     */
    public static String getRequestQueryString() {
        return Faces.getRequest().getQueryString();
    }

    /**
     * Returns the HTTP request servlet path. If JSF is prefix mapped (e.g.
     * <code>/faces/*</code>), then this returns the whole prefix mapping (e.g.
     * <code>/faces</code>). If JSF is suffix mapped (e.g. <code>*.xhtml</code>
     * ), then this returns the whole part after the context path, with a
     * leading slash.
     * 
     * @return The HTTP request servlet path.
     * @see ExternalContext#getRequestServletPath()
     */
    public static String getRequestServletPath() {
        return Faces.getExternalContext().getRequestServletPath();
    }

    /**
     * Returns the HTTP request URI. This is the part after the domain in the
     * request URL, including the leading slash. This does not include the
     * request query string.
     * 
     * @return The HTTP request URI.
     * @see HttpServletRequest#getRequestURI()
     * @since 1.1
     */
    public static String getRequestURI() {
        return Faces.getRequest().getRequestURI();
    }

    /**
     * Returns the HTTP request URL. This is the full request URL as the enduser
     * sees in browser address bar. This does not include the request query
     * string.
     * 
     * @return The HTTP request URL.
     * @see HttpServletRequest#getRequestURL()
     * @since 1.1
     */
    public static String getRequestURL() {
        return Faces.getRequest().getRequestURL().toString();
    }

    // HTTP response
    // --------------------------------------------------------------------------------------------------

    /**
     * Returns a URL for an application resource mapped to the specified path,
     * if it exists; otherwise, return <code>null</code>.
     * 
     * @param path The application resource path to return an input stream for.
     * @return An input stream for an application resource mapped to the
     *         specified path.
     * @throws MalformedURLException
     * @see ExternalContext#getResource(String)
     * @since 1.2
     */
    public static URL getResource(final String path) throws MalformedURLException {
        return Faces.getExternalContext().getResource(path);
    }

    /**
     * Returns an input stream for an application resource mapped to the
     * specified path, if it exists; otherwise, return <code>null</code>.
     * 
     * @param path The application resource path to return an input stream for.
     * @return An input stream for an application resource mapped to the
     *         specified path.
     * @see ExternalContext#getResourceAsStream(String)
     */
    public static InputStream getResourceAsStream(final String path) {
        return Faces.getExternalContext().getResourceAsStream(path);
    }

    /**
     * Returns a set of available application resource paths matching the
     * specified path.
     * 
     * @param path The partial application resource path used to return matching
     *            resource paths.
     * @return A set of available application resource paths matching the
     *         specified path.
     * @see ExternalContext#getResourcePaths(String)
     */
    public static Set<String> getResourcePaths(final String path) {
        return Faces.getExternalContext().getResourcePaths(path);
    }

    /**
     * Returns the HTTP servlet response.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The HTTP servlet response.
     * @see ExternalContext#getResponse()
     */
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) Faces.getExternalContext().getResponse();
    }

    /**
     * Returns the HTTP response buffer size. If Facelets is used and the
     * <code>javax.faces.FACELETS_BUFFER_SIZE</code> context parameter is been
     * set, then it's the context parameter value which will be returned.
     * Otherwise it returns the implementation independent default value, which
     * is 1024 in Mojarra.
     * 
     * @return The HTTP response buffer size.
     * @see ExternalContext#getResponseBufferSize()
     * @since 1.2
     */
    public static int getResponseBufferSize() {
        return Faces.getExternalContext().getResponseBufferSize();
    }

    /**
     * Returns the HTTP response character encoding.
     * 
     * @return The HTTP response character encoding.
     * @see ExternalContext#getResponseCharacterEncoding()
     * @since 1.2
     */
    public static String getResponseCharacterEncoding() {
        return Faces.getExternalContext().getResponseCharacterEncoding();
    }

    /**
     * Returns the server information of currently running application server
     * implementation.
     * 
     * @return The server information of currently running application server
     *         implementation.
     * @see ServletContext#getServerInfo()
     */
    public static String getServerInfo() {
        return Faces.getServletContext().getServerInfo();
    }

    /**
     * Returns the servlet context.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return the servlet context.
     * @see ExternalContext#getContext()
     */
    public static ServletContext getServletContext() {
        return (ServletContext) Faces.getExternalContext().getContext();
    }

    /**
     * Returns the HTTP session and creates one if one doesn't exist.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The HTTP session.
     * @see ExternalContext#getSession(boolean)
     */
    public static HttpSession getSession() {
        return Faces.getSession(true);
    }

    /**
     * Returns the HTTP session and creates one if one doesn't exist and
     * <code>create</code> argument is <code>true</code>, otherwise don't create
     * one and return <code>null</code>.
     * <p>
     * <i>Note that whenever you absolutely need this method to perform a
     * general task, you might want to consider to submit a feature request to
     * OmniFaces in order to add a new utility method which performs exactly
     * this general task.</i>
     * 
     * @return The HTTP session.
     * @see ExternalContext#getSession(boolean)
     */
    public static HttpSession getSession(final boolean create) {
        return (HttpSession) Faces.getExternalContext().getSession(create);
    }

    /**
     * Returns the session scope attribute value associated with the given name.
     * 
     * @param name The session scope attribute name.
     * @return The session scope attribute value associated with the given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getSessionMap()
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(final String name) {
        return (T) Faces.getSessionMap().get(name);
    }

    /**
     * Returns the time when the HTTP session was created, measured in epoch
     * time. This implicitly creates the session if one doesn't exist.
     * 
     * @return The time when the HTTP session was created.
     * @see HttpSession#getCreationTime()
     * @since 1.1
     */
    public static long getSessionCreationTime() {
        return Faces.getSession().getCreationTime();
    }

    /**
     * Returns a string containing the unique identifier assigned to this
     * session. The identifier is assigned by the servlet container and is
     * implementation dependent.
     * 
     * @return The HTTP session ID.
     * @see HttpSession#getId()
     * @since 1.2
     */
    public static String getSessionId() {
        final HttpSession session = Faces.getSession(false);
        return (session != null) ? session.getId() : null;
    }

    /**
     * Returns the time of the previous request associated with the current HTTP
     * session, measured in epoch time. This implicitly creates the session if
     * one doesn't exist.
     * 
     * @return The time of the previous request associated with the current HTTP
     *         session.
     * @see HttpSession#getLastAccessedTime()
     * @since 1.1
     */
    public static long getSessionLastAccessedTime() {
        return Faces.getSession().getLastAccessedTime();
    }

    /**
     * Returns the session scope map.
     * 
     * @return The session scope map.
     * @see ExternalContext#getSessionMap()
     */
    public static Map<String, Object> getSessionMap() {
        return Faces.getExternalContext().getSessionMap();
    }

    // FORM based authentication
    // --------------------------------------------------------------------------------------

    /**
     * Returns the HTTP session timeout in seconds. This implicitly creates the
     * session if one doesn't exist.
     * 
     * @return The HTTP session timeout in seconds.
     * @see HttpSession#getMaxInactiveInterval()
     * @since 1.1
     */
    public static int getSessionMaxInactiveInterval() {
        // Note that JSF 2.1 has this method on ExternalContext. We don't use it
        // in order to be JSF 2.0 compatible.
        return Faces.getSession().getMaxInactiveInterval();
    }

    /**
     * Returns a list of all supported locales on this application, with the
     * default locale as the first item, if any. This will return an empty list
     * if there are no locales definied in <code>faces-config.xml</code>.
     * 
     * @return A list of all supported locales on this application, with the
     *         default locale as the first item, if any.
     * @see Application#getDefaultLocale()
     * @see Application#getSupportedLocales()
     */
    public static List<Locale> getSupportedLocales() {
        final Application application = Faces.getApplication();
        final List<Locale> supportedLocales = new ArrayList<Locale>();
        final Locale defaultLocale = application.getDefaultLocale();

        if (defaultLocale != null) {
            supportedLocales.add(defaultLocale);
        }

        for (final Iterator<Locale> iter = application.getSupportedLocales(); iter.hasNext();) {
            final Locale supportedLocale = iter.next();

            if (!supportedLocale.equals(defaultLocale)) {
                supportedLocales.add(supportedLocale);
            }
        }

        return supportedLocales;
    }

    /**
     * Returns the view scope attribute value associated with the given name.
     * 
     * @param name The view scope attribute name.
     * @return The view scope attribute value associated with the given name.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see UIViewRoot#getViewMap()
     */
    @SuppressWarnings("unchecked")
    public static <T> T getViewAttribute(final String name) {
        return (T) Faces.getViewMap().get(name);
    }

    /**
     * Returns the ID of the current view root, or <code>null</code> if there is
     * no view.
     * 
     * @return The ID of the current view root, or <code>null</code> if there is
     *         no view.
     * @see UIViewRoot#getViewId()
     */
    public static String getViewId() {
        final UIViewRoot viewRoot = Faces.getViewRoot();
        return (viewRoot != null) ? viewRoot.getViewId() : null;
    }

    /**
     * Returns the view scope map.
     * 
     * @return The view scope map.
     * @see UIViewRoot#getViewMap()
     */
    public static Map<String, Object> getViewMap() {
        return Faces.getViewRoot().getViewMap();
    }

    // HTTP cookies
    // ---------------------------------------------------------------------------------------------------

    /**
     * Returns the view parameters of the current view, or an empty collection
     * if there is no view.
     * 
     * @return The view parameters of the current view, or an empty collection
     *         if there is no view.
     * @see ViewMetadata#getViewParameters(UIViewRoot)
     */
    public static Collection<UIViewParameter> getViewParameters() {
        final UIViewRoot viewRoot = Faces.getViewRoot();
        return (viewRoot != null) ? ViewMetadata.getViewParameters(viewRoot) : Collections
            .<UIViewParameter> emptyList();
    }

    /**
     * Returns the current view root.
     * 
     * @return The current view root.
     * @see FacesContext#getViewRoot()
     */
    public static UIViewRoot getViewRoot() {
        return Faces.getContext().getViewRoot();
    }

    /**
     * Returns whether the HTTP session has already been created.
     * 
     * @return <code>true</code> if the HTTP session has already been created,
     *         otherwise <code>false</code>.
     * @see ExternalContext#getSession(boolean)
     * @since 1.1
     */
    public static boolean hasSession() {
        return Faces.getSession(false) != null;
    }

    // HTTP session
    // ---------------------------------------------------------------------------------------------------

    /**
     * Returns whether the HTTP session has been timed out for the current
     * request. This is helpful if you need to distinguish between a first-time
     * request on a fresh session and a first-time request on a timed out
     * session, for example to display
     * "Oops, you have been logged out because your session has been timed out!"
     * .
     * 
     * @return <code>true</code> if the HTTP session has been timed out for the
     *         current request, otherwise <code>false</code>.
     * @see HttpServletRequest#getRequestedSessionId()
     * @see HttpServletRequest#isRequestedSessionIdValid()
     * @since 1.1
     */
    public static boolean hasSessionTimedOut() {
        final HttpServletRequest request = Faces.getRequest();
        return request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();
    }

    /**
     * Include the Facelet file at the given (relative) path as child of the
     * given UI component. This has the same effect as using
     * <code>&lt;ui:include&gt;</code>. The path is relative to the current view
     * ID and absolute to the webcontent root.
     * 
     * @param component The component to include the Facelet file in.
     * @param path The (relative) path to the Facelet file.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     * @see FaceletContext#includeFacelet(UIComponent, String)
     * @since 1.1
     */
    public static void includeFacelet(final UIComponent component, final String path) throws IOException {
        Faces.getFaceletContext().includeFacelet(component, path);
    }

    /**
     * Invalidates the current HTTP session. So, any subsequent HTTP request
     * will get a new one when necessary.
     * 
     * @see ExternalContext#invalidateSession()
     */
    public static void invalidateSession() {
        Faces.getExternalContext().invalidateSession();
    }

    /**
     * Returns whether the current request is an ajax request.
     * 
     * @return <code>true</code> for an ajax request, <code>false</code> for a
     *         non-ajax (synchronous) request.
     * @see PartialViewContext#isAjaxRequest()
     */
    public static boolean isAjaxRequest() {
        return Ajax.getContext().isAjaxRequest();
    }

    /**
     * Returns whether we're in development stage. This will be the case when
     * the <code>javax.faces.PROJECT_STAGE</code> context parameter in
     * <code>web.xml</code> is set to <code>Development</code>.
     * 
     * @return <code>true</code> if we're in development stage, otherwise
     *         <code>false</code>.
     * @see Application#getProjectStage()
     */
    public static boolean isDevelopment() {
        return Faces.getApplication().getProjectStage() == ProjectStage.Development;
    }

    /**
     * Returns whether the current request is a postback.
     * 
     * @return <code>true</code> for a postback, <code>false</code> for a
     *         non-postback (GET) request.
     * @see FacesContext#isPostback()
     */
    public static boolean isPostback() {
        return Faces.getContext().isPostback();
    }

    /**
     * Returns whether the faces servlet mapping used in the current request is
     * a prefix mapping.
     * 
     * @return <code>true</code> if the faces servlet mapping used in the
     *         current request is a prefix mapping, otherwise <code>false</code>
     *         .
     * @see #getMapping()
     * @see #isPrefixMapping(String)
     */
    public static boolean isPrefixMapping() {
        return Faces.isPrefixMapping(Faces.getMapping());
    }

    /**
     * Returns whether the given faces servlet mapping is a prefix mapping. Use
     * this method in preference to {@link #isPrefixMapping()} when you already
     * have obtained the mapping from {@link #getMapping()} so that the mapping
     * won't be calculated twice.
     * 
     * @param mapping The mapping to be tested.
     * @return <code>true</code> if the faces servlet mapping used in the
     *         current request is a prefix mapping, otherwise <code>false</code>
     *         .
     * @throws NullPointerException When mapping is <code>null</code>.
     */
    public static boolean isPrefixMapping(final String mapping) {
        return (mapping.charAt(0) == '/');
    }

    /**
     * Returns <code>true</code> if we're currently in the render response
     * phase. This explicitly checks the current phase ID instead of
     * {@link FacesContext#getRenderResponse()} as the latter may unexpectedly
     * return false during a GET request when <code>&lt;f:viewParam&gt;</code>
     * is been used.
     * 
     * @return <code>true</code> if we're currently in the render response
     *         phase.
     * @see FacesContext#getCurrentPhaseId()
     * @since 1.4
     */
    public static boolean isRenderResponse() {
        return Faces.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE;
    }

    /**
     * Returns whether the response is already committed. That is, when the
     * response headers and a part of the response body has already been sent to
     * the client. This is usually a point of no return and you can't change the
     * response anymore.
     * 
     * @return <code>true</code> if the response is already committed, otherwise
     *         <code>false</code>.
     * @see ExternalContext#isResponseCommitted()
     * @since 1.1
     */
    public static boolean isResponseCommitted() {
        return Faces.getExternalContext().isResponseCommitted();
    }

    /**
     * Returns <code>true</code> if the {@link FacesContext#responseComplete()}
     * has been called.
     * 
     * @return <code>true</code> if the {@link FacesContext#responseComplete()}
     *         has been called.
     * @see FacesContext#responseComplete()
     * @since 1.4
     */
    public static boolean isResponseComplete() {
        return Faces.getContext().getResponseComplete();
    }

    // Servlet context
    // ------------------------------------------------------------------------------------------------

    /**
     * Returns whether the HTTP session has been created for the first time in
     * the current request. This returns also <code>false</code> when there is
     * no means of a HTTP session.
     * 
     * @return <code>true</code> if the HTTP session has been created for the
     *         first time in the current request, otherwise <code>false</code>.
     * @see ExternalContext#getSession(boolean)
     * @see HttpSession#isNew()
     * @since 1.1
     */
    public static boolean isSessionNew() {
        final HttpSession session = Faces.getSession(false);
        return (session != null && session.isNew());
    }

    /**
     * Returns whether the currently logged-in user has the given role.
     * 
     * @param role The role to be checked on the currently logged-in user.
     * @return <code>true</code> if the currently logged-in user has the given
     *         role, otherwise <code>false</code>.
     * @see ExternalContext#isUserInRole(String)
     */
    public static boolean isUserInRole(final String role) {
        return Faces.getExternalContext().isUserInRole(role);
    }

    /**
     * Returns whether the validations phase of the current request has failed.
     * 
     * @return <code>true</code> if the validations phase of the current request
     *         has failed, otherwise <code>false</code>.
     * @see FacesContext#isValidationFailed()
     */
    public static boolean isValidationFailed() {
        return Faces.getContext().isValidationFailed();
    }

    /**
     * Perform programmatic login for container managed FORM based
     * authentication. Note that configuration is container specific and
     * unrelated to JSF. Refer the documentation of the servletcontainer using
     * the keyword "realm".
     * 
     * @param username The login username.
     * @param password The login password.
     * @throws ServletException When the login is invalid, or when container
     *             managed FORM based authentication is not enabled.
     * @see HttpServletRequest#login(String, String)
     */
    public static void login(final String username, final String password) throws ServletException {
        Faces.getRequest().login(username, password);
    }

    /**
     * Perform programmatic logout for container managed FORM based
     * authentication. Note that this basically removes the user principal from
     * the session. It's however better practice to just invalidate the session
     * altogether, which will implicitly also remove the user principal. Just
     * invoke {@link #invalidateSession()} instead. Note that the user principal
     * is still present in the response of the current request, it's therefore
     * recommend to send a redirect after {@link #logout()} or
     * {@link #invalidateSession()}. You can use
     * {@link #redirect(String, String...)} for this.
     * 
     * @throws ServletException When the logout has failed.
     * @see HttpServletRequest#logout()
     */
    public static void logout() throws ServletException {
        Faces.getRequest().logout();
    }

    /**
     * Perform the JSF navigation to the given outcome.
     * 
     * @param outcome The navigation outcome.
     * @see Application#getNavigationHandler()
     * @see NavigationHandler#handleNavigation(FacesContext, String, String)
     */
    public static void navigate(final String outcome) {
        final FacesContext context = Faces.getContext();
        context.getApplication().getNavigationHandler().handleNavigation(context, null, outcome);
    }

    /**
     * Helper method to normalize the given URL for a redirect. If the given URL
     * does not start with <code>http://</code>, <code>https://</code> or
     * <code>/</code>, then the request context path will be prepended,
     * otherwise it will be unmodified.
     */
    private static String normalizeRedirectURL(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("/")) {
            url = Faces.getRequestContextPath() + "/" + url;
        }

        return url;
    }

    /**
     * Normalize the given path as a valid view ID based on the current mapping,
     * if necessary.
     * <ul>
     * <li>If the current mapping is a prefix mapping and the given path starts
     * with it, then remove it.
     * <li>If the current mapping is a suffix mapping and the given path ends
     * with it, then replace it with the default Facelets suffix.
     * </ul>
     * 
     * @param path The path to be normalized as a valid view ID based on the
     *            current mapping.
     * @return The path as a valid view ID.
     * @see #getMapping()
     * @see #isPrefixMapping(String)
     */
    public static String normalizeViewId(final String path) {
        final String mapping = Faces.getMapping();

        if (Faces.isPrefixMapping(mapping)) {
            if (path.startsWith(mapping)) {
                return path.substring(mapping.length());
            }
        } else if (path.endsWith(mapping)) {
            return path.substring(0, path.lastIndexOf('.'))
                + Utils.coalesce(Faces.getInitParameter(ViewHandler.FACELETS_SUFFIX_PARAM_NAME),
                                 ViewHandler.DEFAULT_FACELETS_SUFFIX);
        }

        return path;
    }

    // Request scope
    // --------------------------------------------------------------------------------------------------

    /**
     * Sends a temporary (302) redirect to the given URL. If the given URL does
     * not start with <code>http://</code>, <code>https://</code> or
     * <code>/</code>, then the request context path will be prepended,
     * otherwise it will be the unmodified redirect URL. So, when redirecting to
     * another page in the same web application, always specify the full path
     * from the context root on (which in turn does not need to start with
     * <code>/</code>).
     * <p>
     * You can use {@link String#format(String, Object...)} placeholder
     * <code>%s</code> in the redirect URL to represent placeholders for any
     * request parameter values which needs to be URL-encoded. Here's a concrete
     * example:
     * 
     * <pre>
     * Faces.redirect(&quot;other.xhtml?foo=%s&amp;bar=%s&quot;, foo, bar);
     * </pre>
     * <p>
     * This method implicitly also calls {@link Flash#setRedirect(boolean)} with
     * <code>true</code> so that any flash scoped attributes will survive the
     * redirect.
     * 
     * @param url The URL to redirect the current response to.
     * @param paramValues The request parameter values which you'd like to put
     *            URL-encoded in the given URL.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     * @throws NullPointerException When url is <code>null</code>.
     * @see ExternalContext#redirect(String)
     */
    public static void redirect(final String url, final String... paramValues) throws IOException {
        final ExternalContext externalContext = Faces.getExternalContext();
        externalContext.getFlash().setRedirect(true);
        externalContext.redirect(String.format(Faces.normalizeRedirectURL(url), Faces.encodeURLParams(paramValues)));
    }

    /**
     * Sends a permanent (301) redirect to the given URL. If the given URL does
     * not start with <code>http://</code>, <code>https://</code> or
     * <code>/</code>, then the request context path will be prepended,
     * otherwise it will be the unmodified redirect URL. So, when redirecting to
     * another page in the same web application, always specify the full path
     * from the context root on (which in turn does not need to start with
     * <code>/</code>).
     * <p>
     * You can use {@link String#format(String, Object...)} placeholder
     * <code>%s</code> in the redirect URL to represent placeholders for any
     * request parameter values which needs to be URL-encoded. Here's a concrete
     * example:
     * 
     * <pre>
     * Faces.redirectPermanent(&quot;other.xhtml?foo=%s&amp;bar=%s&quot;, foo, bar);
     * </pre>
     * <p>
     * This method implicitly also calls {@link Flash#setRedirect(boolean)} with
     * <code>true</code> so that any flash scoped attributes will survive the
     * redirect.
     * <p>
     * This method does by design not work on ajax requests. It is not possible
     * to return a "permanent redirect" via JSF ajax XML response.
     * 
     * @param url The URL to redirect the current response to.
     * @param paramValues The request parameter values which you'd like to put
     *            URL-encoded in the given URL.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     * @throws NullPointerException When url is <code>null</code>.
     * @see ExternalContext#setResponseStatus(int)
     * @see ExternalContext#setResponseHeader(String, String)
     */
    public static void redirectPermanent(final String url, final String... paramValues) throws IOException {
        final FacesContext context = Faces.getContext();
        final ExternalContext externalContext = context.getExternalContext();
        externalContext.getFlash().setRedirect(true);
        externalContext.setResponseStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        externalContext.setResponseHeader("Location", String.format(Faces.normalizeRedirectURL(url), Faces
            .encodeURLParams(paramValues)));
        externalContext.setResponseHeader("Connection", "close");
        context.responseComplete();
    }

    /**
     * Removes the application scope attribute value associated with the given
     * name.
     * 
     * @param name The application scope attribute name.
     * @return The application scope attribute value previously associated with
     *         the given name, or <code>null</code> if there is no such
     *         attribute.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getApplicationMap()
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeApplicationAttribute(final String name) {
        return (T) Faces.getApplicationMap().remove(name);
    }

    /**
     * Removes the flash scope attribute value associated with the given name.
     * 
     * @param name The flash scope attribute name.
     * @return The flash scope attribute value previously associated with the
     *         given name, or <code>null</code> if there is no such attribute.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getFlash()
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeFlashAttribute(final String name) {
        return (T) Faces.getFlash().remove(name);
    }

    /**
     * Removes the request scope attribute value associated with the given name.
     * 
     * @param name The request scope attribute name.
     * @return The request scope attribute value previously associated with the
     *         given name, or <code>null</code> if there is no such attribute.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getRequestMap()
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeRequestAttribute(final String name) {
        return (T) Faces.getRequestMap().remove(name);
    }

    // Flash scope
    // ----------------------------------------------------------------------------------------------------

    /**
     * Remove the cookie with given name and path from the HTTP response. Note
     * that the name and path must be exactly the same as it was when the cookie
     * was created.
     * 
     * @param name The cookie name.
     * @param path The cookie path.
     * @see ExternalContext#addResponseCookie(String, String, Map)
     */
    public static void removeResponseCookie(final String name, final String path) {
        Faces.addResponseCookie(name, null, path, 0);
    }

    /**
     * Removes the session scope attribute value associated with the given name.
     * 
     * @param name The session scope attribute name.
     * @return The session scope attribute value previously associated with the
     *         given name, or <code>null</code> if there is no such attribute.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see ExternalContext#getSessionMap()
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeSessionAttribute(final String name) {
        return (T) Faces.getSessionMap().remove(name);
    }

    /**
     * Removes the view scope attribute value associated with the given name.
     * 
     * @param name The view scope attribute name.
     * @return The view scope attribute value previously associated with the
     *         given name, or <code>null</code> if there is no such attribute.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     * @see UIViewRoot#getViewMap()
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T removeViewAttribute(final String name) {
        return (T) Faces.getViewMap().remove(name);
    }

    /**
     * Signals JSF that, as soon as the current phase of the lifecycle has been
     * completed, control should be passed to the Render Response phase,
     * bypassing any phases that have not been executed yet.
     * 
     * @see FacesContext#renderResponse()
     * @since 1.4
     */
    public static void renderResponse() {
        Faces.getContext().renderResponse();
    }

    // View scope
    // -----------------------------------------------------------------------------------------------------

    /**
     * Signals JSF that the response for this request has already been generated
     * (such as providing a file download), and that the lifecycle should be
     * terminated as soon as the current phase is completed.
     * 
     * @see FacesContext#responseComplete()
     * @since 1.4
     */
    public static void responseComplete() {
        Faces.getContext().responseComplete();
    }

    /**
     * Resets the current response. This will clear any headers which are been
     * set and any data which is written to the response buffer which isn't
     * committed yet.
     * 
     * @throws IllegalStateException When the response is already committed.
     * @see ExternalContext#responseReset()
     * @since 1.1
     */
    public static void responseReset() {
        Faces.getExternalContext().responseReset();
    }

    /**
     * Sends a HTTP response error with the given status and message. This will
     * end up in either a custom <code>&lt;error-page&gt;</code> whose
     * <code>&lt;error-code&gt;</code> matches the given status, or in a servlet
     * container specific default error page if there is none. The message will
     * be available in the error page as a request attribute with name
     * <code>javax.servlet.error.message</code>. The
     * {@link FacesContext#responseComplete()} will implicitly be called after
     * sending the error.
     * 
     * @param status The HTTP response status which is supposed to be in the
     *            range 4nn-5nn. You can use the constant field values of
     *            {@link HttpServletResponse} for this.
     * @param message The message which is supposed to be available in the error
     *            page.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     * @see ExternalContext#responseSendError(int, String)
     */
    public static void responseSendError(final int status, final String message) throws IOException {
        final FacesContext context = Faces.getContext();
        context.getExternalContext().responseSendError(status, message);
        context.responseComplete();
    }

    /**
     * Send the given byte array as a file to the response. The content type
     * will be determined based on file name. The content length will be set to
     * the length of the byte array. The {@link FacesContext#responseComplete()}
     * will implicitly be called after successful streaming.
     * 
     * @param content The file content as byte array.
     * @param filename The file name which should appear in content disposition
     *            header.
     * @param attachment Whether the file should be provided as attachment, or
     *            just inline.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     */
    public static void sendFile(final byte[] content, final String filename, final boolean attachment)
        throws IOException {
        Faces.sendFile(new ByteArrayInputStream(content), filename, content.length, attachment);
    }

    // Session scope
    // --------------------------------------------------------------------------------------------------

    /**
     * Send the given file to the response. The content type will be determined
     * based on file name. The content length will be set to the length of the
     * file. The {@link FacesContext#responseComplete()} will implicitly be
     * called after successful streaming.
     * 
     * @param file The file to be sent to the response.
     * @param attachment Whether the file should be provided as attachment, or
     *            just inline.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     */
    public static void sendFile(final File file, final boolean attachment) throws IOException {
        Faces.sendFile(new FileInputStream(file), file.getName(), file.length(), attachment);
    }

    /**
     * Send the given input stream as a file to the response. The content type
     * will be determined based on file name. The content length may not be set
     * because that's not predictable based on input stream. The client may
     * receive a download of an unknown length and thus the download progress
     * may be unknown to the client. Only if the input stream is smaller than
     * the default buffer size, then the content length will be set. The
     * {@link InputStream#close()} will implicitly be called after streaming,
     * regardless of whether an exception is been thrown or not. The
     * {@link FacesContext#responseComplete()} will implicitly be called after
     * successful streaming.
     * 
     * @param content The file content as input stream.
     * @param filename The file name which should appear in content disposition
     *            header.
     * @param attachment Whether the file should be provided as attachment, or
     *            just inline.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     */
    public static void sendFile(final InputStream content, final String filename, final boolean attachment)
        throws IOException {
        Faces.sendFile(content, filename, -1, attachment);
    }

    /**
     * Internal global method to send the given input stream to the response.
     * 
     * @param input The file content as input stream.
     * @param filename The file name which should appear in content disposition
     *            header.
     * @param contentLength The content length, or -1 if it is unknown.
     * @param attachment Whether the file should be provided as attachment, or
     *            just inline.
     * @throws IOException Whenever something fails at I/O level. The caller
     *             should preferably not catch it, but just redeclare it in the
     *             action method. The servletcontainer will handle it.
     */
    private static void sendFile(final InputStream input,
                                 final String filename,
                                 final long contentLength,
                                 final boolean attachment) throws IOException {
        final FacesContext context = Faces.getContext();
        final ExternalContext externalContext = context.getExternalContext();

        // Prepare the response and set the necessary headers.
        externalContext.setResponseBufferSize(Faces.DEFAULT_SENDFILE_BUFFER_SIZE);
        externalContext.setResponseContentType(Faces.getMimeType(filename));
        externalContext.setResponseHeader("Content-Disposition", String.format("%s;filename=\"%s\"",
                                                                               (attachment ? "attachment" : "inline"),
                                                                               Utils.encodeURL(filename)));

        // Not exactly mandatory, but this fixes at least a MSIE quirk:
        // http://support.microsoft.com/kb/316431
        if (((HttpServletRequest) externalContext.getRequest()).isSecure()) {
            externalContext.setResponseHeader("Cache-Control", "public");
            externalContext.setResponseHeader("Pragma", "public");
        }

        // If content length is known, set it. Note that
        // setResponseContentLength() cannot be used as it takes only int.
        if (contentLength != -1) {
            externalContext.setResponseHeader("Content-Length", String.valueOf(contentLength));
        }

        final long size = Utils.stream(input, externalContext.getResponseOutputStream());

        // This may be on time for files smaller than the default buffer size,
        // but is otherwise ignored anyway.
        if (contentLength == -1) {
            externalContext.setResponseHeader("Content-Length", String.valueOf(size));
        }

        context.responseComplete();
    }

    /**
     * Sets the application scope attribute value associated with the given
     * name.
     * 
     * @param name The application scope attribute name.
     * @param value The application scope attribute value.
     * @see ExternalContext#getApplicationMap()
     */
    public static void setApplicationAttribute(final String name, final Object value) {
        Faces.getApplicationMap().put(name, value);
    }

    // Application scope
    // ----------------------------------------------------------------------------------------------

    /**
     * Sets the given faces context as current instance. Use this if you have a
     * custom {@link FacesContextWrapper} which you'd like to (temporarily) use
     * as the current instance of the faces context.
     * 
     * @param context The faces context to be set as the current instance.
     * @since 1.3
     */
    public static void setContext(final FacesContext context) {
        FacesContextSetter.setCurrentInstance(context);
    }

    /**
     * Sets the Faces context attribute value associated with the given name.
     * 
     * @param name The Faces context attribute name.
     * @param value The Faces context attribute value.
     * @see FacesContext#getAttributes()
     * @since 1.3
     */
    public static void setContextAttribute(final String name, final Object value) {
        Faces.getContext().getAttributes().put(name, value);
    }

    /**
     * Sets the Facelet attribute value associated with the given name. This
     * basically does the same as an <code>&lt;ui:param&gt;</code> which is been
     * declared inside the Facelet file, or is been passed into the Facelet file
     * by e.g. an <code>&lt;ui:include&gt;</code>.
     * 
     * @param name The Facelet attribute name.
     * @param value The Facelet attribute value.
     * @see FaceletContext#setAttribute(String, Object)
     * @since 1.1
     */
    public static void setFaceletAttribute(final String name, final Object value) {
        Faces.getFaceletContext().setAttribute(name, value);
    }

    /**
     * Sets the flash scope attribute value associated with the given name.
     * 
     * @param name The flash scope attribute name.
     * @param value The flash scope attribute value.
     * @see ExternalContext#getFlash()
     */
    public static void setFlashAttribute(final String name, final Object value) {
        Faces.getFlash().put(name, value);
    }

    /**
     * Set the locale of the current view, which is to be used in localizing of
     * the response.
     * 
     * @param locale The locale of the current view.
     * @throws IllegalStateException When there is no view (i.e. when it is
     *             <code>null</code>). This can happen if the method is called
     *             at the wrong moment in the JSF lifecycle, e.g. before the
     *             view has been restored/created.
     * @see UIViewRoot#setLocale(Locale)
     * @since 1.2
     */
    public static void setLocale(final Locale locale) {
        final UIViewRoot viewRoot = Faces.getViewRoot();

        if (viewRoot == null) {
            throw new IllegalStateException(Faces.ERROR_NO_VIEW);
        }

        viewRoot.setLocale(locale);
    }

    /**
     * Sets the request scope attribute value associated with the given name.
     * 
     * @param name The request scope attribute name.
     * @param value The request scope attribute value.
     * @see ExternalContext#getRequestMap()
     */
    public static void setRequestAttribute(final String name, final Object value) {
        Faces.getRequestMap().put(name, value);
    }

    // File download
    // --------------------------------------------------------------------------------------------------

    /**
     * Sets the session scope attribute value associated with the given name.
     * 
     * @param name The session scope attribute name.
     * @param value The session scope attribute value.
     * @see ExternalContext#getSessionMap()
     */
    public static void setSessionAttribute(final String name, final Object value) {
        Faces.getSessionMap().put(name, value);
    }

    /**
     * Sets the HTTP session timeout in seconds. A value of 0 or less means that
     * the session should never timeout. This implicitly creates the session if
     * one doesn't exist.
     * 
     * @param seconds The HTTP session timeout in seconds.
     * @see HttpSession#setMaxInactiveInterval(int)
     * @since 1.1
     */
    public static void setSessionMaxInactiveInterval(final int seconds) {
        // Note that JSF 2.1 has this method on ExternalContext. We don't use it
        // in order to be JSF 2.0 compatible.
        Faces.getSession().setMaxInactiveInterval(seconds);
    }

    /**
     * Sets the view scope attribute value associated with the given name.
     * 
     * @param name The view scope attribute name.
     * @param value The view scope attribute value.
     * @see UIViewRoot#getViewMap()
     */
    public static void setViewAttribute(final String name, final Object value) {
        Faces.getViewMap().put(name, value);
    }

    /**
     * Sets the current view root to the given view ID. The view ID must start
     * with a leading slash. If an invalid view ID is given, then the response
     * will simply result in a 404.
     * 
     * @param viewId The ID of the view which needs to be set as the current
     *            view root.
     * @see ViewHandler#createView(FacesContext, String)
     * @see FacesContext#setViewRoot(UIViewRoot)
     * @since 1.1
     */
    public static void setViewRoot(final String viewId) {
        final FacesContext context = Faces.getContext();
        context.setViewRoot(context.getApplication().getViewHandler().createView(context, viewId));
    }

    private Faces() {
        // Hide constructor.
    }

}