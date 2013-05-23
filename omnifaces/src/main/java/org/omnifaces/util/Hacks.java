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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import org.omnifaces.resourcehandler.ResourceIdentifier;

/**
 * Collection of JSF implementation and/or JSF component library specific hacks.
 * 
 * @author Bauke Scholtz
 * @author Arjan Tijms
 * @since 1.3
 */
public final class Hacks {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    private static final boolean RICHFACES_INSTALLED = Hacks.initRichFacesInstalled();
    private static final boolean RICHFACES_RESOURCE_OPTIMIZATION_ENABLED = Hacks.RICHFACES_INSTALLED
        && Boolean.valueOf(Faces.getInitParameter("org.richfaces.resourceOptimization.enabled"));
    private static final String RICHFACES_PVC_CLASS_NAME = "org.richfaces.context.ExtendedPartialViewContextImpl";
    private static final String RICHFACES_RLR_RENDERER_TYPE = "org.richfaces.renderkit.ResourceLibraryRenderer";
    private static final String RICHFACES_RLF_CLASS_NAME = "org.richfaces.resource.ResourceLibraryFactoryImpl";

    private static final boolean JUEL_SUPPORTS_METHOD_EXPRESSION = Hacks.initJUELSupportsMethodExpression();
    private static final String JUEL_EF_CLASS_NAME = "de.odysseus.el.ExpressionFactoryImpl";
    private static final String JUEL_MINIMUM_METHOD_EXPRESSION_VERSION = "2.2.6";

    private static final Set<String> MOJARRA_MYFACES_RESOURCE_DEPENDENCY_KEYS = Utils
        .unmodifiableSet("com.sun.faces.PROCESSED_RESOURCE_DEPENDENCIES",
                         "org.apache.myfaces.RENDERED_SCRIPT_RESOURCES_SET",
                         "org.apache.myfaces.RENDERED_STYLESHEET_RESOURCES_SET");

    private static final String ERROR_CREATE_INSTANCE = "Cannot create instance of class '%s'.";
    private static final String ERROR_ACCESS_FIELD = "Cannot access field '%s' of class '%s'.";
    private static final String ERROR_INVOKE_METHOD = "Cannot invoke method '%s' of class '%s' with arguments %s.";

    private static final Object[] EMPTY_PARAMETERS = new Object[0];

    // Constructors/init
    // ----------------------------------------------------------------------------------------------

    /**
     * Access a field of the given instance on the given field name and return
     * the field value.
     */
    @SuppressWarnings("unchecked")
    private static <T> T accessField(final Object instance, final String fieldName) {
        try {
            final Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (final Exception e) {
            throw new RuntimeException(String.format(Hacks.ERROR_ACCESS_FIELD, fieldName, instance.getClass()), e);
        }
    }

    /**
     * Create an instance of the given class using the default constructor and
     * return the instance.
     */
    private static Object createInstance(final String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(String.format(Hacks.ERROR_CREATE_INSTANCE, className), e);
        }
    }

    /**
     * RichFaces PartialViewContext implementation does not extend from
     * PartialViewContextWrapper. So a hack wherin the exact fully qualified
     * class name needs to be known has to be used to properly extract it from
     * the {@link FacesContext#getPartialViewContext()}.
     * 
     * @return The RichFaces PartialViewContext implementation.
     */
    public static PartialViewContext getRichFacesPartialViewContext() {
        PartialViewContext context = Ajax.getContext();

        while (!context.getClass().getName().equals(Hacks.RICHFACES_PVC_CLASS_NAME)
            && context instanceof PartialViewContextWrapper) {
            context = ((PartialViewContextWrapper) context).getWrapped();
        }

        if (context.getClass().getName().equals(Hacks.RICHFACES_PVC_CLASS_NAME)) {
            return context;
        } else {
            return null;
        }
    }

    // Helpers
    // --------------------------------------------------------------------------------------------------------

    /**
     * RichFaces PartialViewContext implementation does not have the
     * getRenderIds() method properly implemented. So a hack wherin the exact
     * name of the private field needs to be known has to be used to properly
     * extract it from the RichFaces PartialViewContext implementation.
     * 
     * @return The render IDs from the RichFaces PartialViewContext
     *         implementation.
     */
    public static Collection<String> getRichFacesRenderIds() {
        final PartialViewContext richFacesContext = Hacks.getRichFacesPartialViewContext();

        if (richFacesContext != null) {
            final Collection<String> renderIds = Hacks.accessField(richFacesContext, "componentRenderIds");

            if (renderIds != null) {
                return renderIds;
            }
        }

        return Collections.emptyList();
    }

    /**
     * Returns an ordered set of all JSF resource identifiers for the given
     * RichFaces resource library resources.
     * 
     * @param id The resource identifier of the RichFaces resource library (e.g.
     *            org.richfaces:ajax.reslib).
     * @return An ordered set of all JSF resource identifiers for the given
     *         RichFaces resource library resources.
     */
    @SuppressWarnings("rawtypes")
    public static Set<ResourceIdentifier> getRichFacesResourceLibraryResources(final ResourceIdentifier id) {
        final Object resourceFactory = Hacks.createInstance(Hacks.RICHFACES_RLF_CLASS_NAME);
        final String name = id.getName().split("\\.")[0];
        final Object resourceLibrary = Hacks.invokeMethod(resourceFactory, "getResourceLibrary", name, id.getLibrary());
        final Iterable resources = Hacks.invokeMethod(resourceLibrary, "getResources");
        final Set<ResourceIdentifier> resourceIdentifiers = new LinkedHashSet<ResourceIdentifier>();

        for (final Object resource : resources) {
            final String libraryName = Hacks.invokeMethod(resource, "getLibraryName");
            final String resourceName = Hacks.invokeMethod(resource, "getResourceName");
            resourceIdentifiers.add(new ResourceIdentifier(libraryName, resourceName));
        }

        return resourceIdentifiers;
    }

    /**
     * RichFaces PartialViewContext implementation does not have any
     * getWrapped() method to return the wrapped PartialViewContext. So a
     * reflection hack is necessary to return it from the private field.
     * 
     * @return The wrapped PartialViewContext from the RichFaces
     *         PartialViewContext implementation.
     */
    public static PartialViewContext getRichFacesWrappedPartialViewContext() {
        final PartialViewContext richFacesContext = Hacks.getRichFacesPartialViewContext();

        if (richFacesContext != null) {
            return Hacks.accessField(richFacesContext, "wrappedViewContext");
        }

        return null;
    }

    private static int getVersionElement(final List<Integer> versionElements, final int index) {
        if (index < versionElements.size()) {
            return versionElements.get(index);
        }

        return 0;
    }

    private static boolean initJUELSupportsMethodExpression() {
        final Package juelPackage = Package.getPackage("de.odysseus.el");
        if (juelPackage == null) {
            return false;
        }

        final String juelVersion = juelPackage.getImplementationVersion();
        if (juelVersion == null) {
            return false;
        }

        return Hacks.isSameOrHigherVersion(juelVersion, Hacks.JUEL_MINIMUM_METHOD_EXPRESSION_VERSION);
    }

    private static boolean initRichFacesInstalled() {
        try {
            Class.forName(Hacks.RICHFACES_PVC_CLASS_NAME);
            return true;
        } catch (final ClassNotFoundException ignore) {
            return false;
        }
    }

    /**
     * Invoke a method of the given instance on the given method name with the
     * given parameters and return the result. Note: the current implementation
     * assumes for simplicity that no one of the given parameters is null. If
     * one of them is still null, a NullPointerException will be thrown. The
     * implementation should be changed accordingly to take that into account
     * (but then varargs cannot be used anymore and you end up creating ugly
     * arrays).
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <T> T invokeMethod(final Object instance, final String methodName, final Object... parameters) {
        final Class[] parameterTypes = new Class[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }

        try {
            final Method method = instance.getClass().getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return (T) method.invoke(instance, parameters);
        } catch (final Exception e) {
            throw new RuntimeException(String.format(Hacks.ERROR_INVOKE_METHOD, methodName, instance.getClass(), Arrays
                .toString(parameters)), e);
        }
    }

    public static boolean isJUELSupportingMethodExpression() {
        return Hacks.JUEL_SUPPORTS_METHOD_EXPRESSION;
    }

    public static boolean isJUELUsed() {
        return Hacks.isJUELUsed(Faces.getApplication().getExpressionFactory());
    }

    public static boolean isJUELUsed(final ExpressionFactory factory) {
        return factory.getClass().getName().equals(Hacks.JUEL_EF_CLASS_NAME);
    }

    /**
     * Returns true if RichFaces is installed. That is, when the RichFaces
     * specific ExtendedPartialViewContextImpl is present in the runtime
     * classpath -which is present in RF 4.x. As this is usually
     * auto-registered, we may safely assume that RichFaces is installed.
     * 
     * @return Whether RichFaces is installed.
     */
    public static boolean isRichFacesInstalled() {
        return Hacks.RICHFACES_INSTALLED;
    }

    /**
     * Returns true if the given renderer type is recognizeable as RichFaces
     * resource library renderer.
     * 
     * @param rendererType The renderer type to be checked.
     * @return Whether the given renderer type is recognizeable as RichFaces
     *         resource library renderer.
     */
    public static boolean isRichFacesResourceLibraryRenderer(final String rendererType) {
        return Hacks.RICHFACES_RLR_RENDERER_TYPE.equals(rendererType);
    }

    /**
     * RichFaces "resource optimization" do not support
     * {@link Resource#getURL()} and {@link Resource#getInputStream()}. The
     * combined resource handler has to manually create the URL based on
     * {@link Resource#getRequestPath()} and the current request domain URL
     * whenever RichFaces "resource optimization" is enabled.
     * 
     * @return Whether RichFaces resource optimization is enabled.
     */
    public static boolean isRichFacesResourceOptimizationEnabled() {
        return Hacks.RICHFACES_RESOURCE_OPTIMIZATION_ENABLED;
    }

    /**
     * Checks if the given version1 is the same or a higher version than
     * version2.
     * 
     * @param version1 the first version in the comparison
     * @param version2 the second version in the comparison
     * @return true if version1 is the same or a higher version than version2,
     *         false otherwise
     */
    public static boolean isSameOrHigherVersion(final String version1, final String version2) {

        final List<Integer> version1Elements = Hacks.toVersionElements(version1);
        final List<Integer> version2Elements = Hacks.toVersionElements(version2);

        final int maxLength = Math.max(version1Elements.size(), version2Elements.size());

        for (int i = 0; i < maxLength; i++) {
            final int version1Element = Hacks.getVersionElement(version1Elements, i);
            final int version2Element = Hacks.getVersionElement(version2Elements, i);

            if (version1Element > version2Element) {
                return true;
            }
            if (version1Element < version2Element) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method wraps a <code>MethodExpression</code> in a
     * <code>Method</code> which can be statically invoked.
     * <p>
     * Since Method is a final class with only a non-public constructor, various
     * reflective tricks have been used to create an instance of this class and
     * make sure it calls the given method expression. It has been tested on the
     * Sun/Oracle JDK versions 6 and 7, and it should work on OpenJDK 6 and 7 as
     * well. Other JDKs might not work.
     * 
     * @param context the context used for evaluation of the method expression
     *            when it's invoked later. NOTE, this reference is retained by
     *            the returned method.
     * 
     * @param methodExpression the method expression to be wrapped
     * @return a Method instance that when invoked causes the wrapped method
     *         expression to be invoked.
     */
    public static Method methodExpressionToStaticMethod(final ELContext context, final MethodExpression methodExpression) {

        final MethodInfo methodInfo = methodExpression.getMethodInfo(FacesContext.getCurrentInstance().getELContext());

        try {
            // Create a Method instance with the signature (return type, name,
            // parameter types) corresponding
            // to the method the MethodExpression references.
            final Constructor<Method> methodConstructor = Method.class.getDeclaredConstructor(Class.class,
                                                                                              String.class,
                                                                                              Class[].class,
                                                                                              Class.class,
                                                                                              Class[].class, int.class,
                                                                                              int.class, String.class,
                                                                                              byte[].class,
                                                                                              byte[].class,
                                                                                              byte[].class);
            methodConstructor.setAccessible(true);
            final Method staticMethod = methodConstructor.newInstance(null, methodInfo.getName(), methodInfo
                .getParamTypes(), methodInfo.getReturnType(), null, 0, 0, null, null, null, null);

            // The Sun/Oracle/OpenJDK Method makes use of a private delegator
            // called MethodAccessor.
            // Though specific to those JDKs, this is what we can use to let our
            // Method instance execute something
            // we want. (simply overriding the invoke method would be much
            // better, but unfortunately Method is final)
            final Class<?> methodAccessorClass = Class.forName("sun.reflect.MethodAccessor");

            // Create a proxy for our MethodAccessor, so we don't have to
            // reference the actual type at compile-time.
            final Object MethodAccessor = Proxy.newProxyInstance(Method.class.getClassLoader(),
                                                                 new Class[] { methodAccessorClass },
                                                                 new InvocationHandler() {

                                                                     @Override
                                                                     public Object invoke(final Object proxy,
                                                                                          final Method method,
                                                                                          final Object[] args)
                                                                         throws Throwable {

                                                                         Object[] params = null;
                                                                         if (args != null && args.length > 1) {
                                                                             // args[0]
                                                                             // should
                                                                             // be
                                                                             // the
                                                                             // Object
                                                                             // on
                                                                             // which
                                                                             // the
                                                                             // method
                                                                             // is
                                                                             // to
                                                                             // be
                                                                             // invoked
                                                                             // (null
                                                                             // in
                                                                             // case
                                                                             // of
                                                                             // static
                                                                             // call)
                                                                             // args[1]
                                                                             // should
                                                                             // be
                                                                             // the
                                                                             // parameters
                                                                             // for
                                                                             // the
                                                                             // method
                                                                             // invocation
                                                                             // (possibly
                                                                             // empty)
                                                                             params = (Object[]) args[1];
                                                                         } else {
                                                                             params = Hacks.EMPTY_PARAMETERS;
                                                                         }

                                                                         return methodExpression
                                                                             .invoke(context, params);
                                                                     }
                                                                 });

            final Method setMethodAccessor = Method.class.getDeclaredMethod("setMethodAccessor", methodAccessorClass);
            setMethodAccessor.setAccessible(true);
            setMethodAccessor.invoke(staticMethod, MethodAccessor);

            // Another private implementation detail of the Sun/Oracle/OpenJDK
            // Method - unless override is set
            // to true, a couple of nasty language checks are done before
            // invoking the MethodAccessor
            final Field override = AccessibleObject.class.getDeclaredField("override");
            override.setAccessible(true);
            override.set(staticMethod, true);

            return staticMethod;
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // Some reflection helpers
    // ----------------------------------------------------------------------------------------

    /**
     * Remove the resource dependency processing related attributes from the
     * given faces context.
     * 
     * @param context The involved faces context.
     */
    public static void removeResourceDependencyState(final FacesContext context) {
        // Mojarra and MyFaces remembers processed resource dependencies in a
        // map.
        context.getAttributes().keySet().removeAll(Hacks.MOJARRA_MYFACES_RESOURCE_DEPENDENCY_KEYS);

        // PrimeFaces puts "namelibrary=true" for every processed resource
        // dependency.
        // TODO: This may possibly conflict with other keys with value=true. So
        // far tested, this is harmless.
        context.getAttributes().values().removeAll(Collections.singleton(true));
    }

    private static List<Integer> toVersionElements(final String version) {

        final List<Integer> versionElements = new ArrayList<Integer>();
        for (final String string : version.split("\\.")) {
            versionElements.add(Integer.valueOf(string));
        }

        return versionElements;
    }

    private Hacks() {
        //
    }

}