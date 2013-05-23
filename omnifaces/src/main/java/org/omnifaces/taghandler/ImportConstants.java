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
package org.omnifaces.taghandler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import org.omnifaces.util.MapWrapper;

/**
 * <p>
 * The <code>&lt;o:importConstants&gt;</code> allows the developer to have a
 * mapping of all constant field values of the given fully qualified name of a
 * type in the EL scope. The constant field values are those public static final
 * fields. This works for classes, interfaces and enums. For example:
 * 
 * <pre>
 * public class Foo {
 *     public static final String FOO1 = &quot;foo1&quot;;
 *     public static final String FOO2 = &quot;foo2&quot;;
 * }
 * 
 * public interface Bar {
 *     public String BAR1 = &quot;bar1&quot;;
 *     public String BAR2 = &quot;bar2&quot;;
 * }
 * 
 * public enum Baz {
 *     BAZ1,
 *     BAZ2;
 * }
 * </pre>
 * <p>
 * The constant field values of the above types can be mapped into the EL scope
 * as follows:
 * 
 * <pre>
 * &lt;o:importConstants type="com.example.Foo" /&gt;
 * &lt;o:importConstants type="com.example.Bar" /&gt;
 * &lt;o:importConstants type="com.example.Baz" var="Bazzz" /&gt;
 * ...
 * #{Foo.FOO1}, #{Foo.FOO2}, #{Bar.BAR1}, #{Bar.BAR2}, #{Bazzz.BAZ1}, #{Bazzz.BAZ2}
 * </pre>
 * <p>
 * The map is by default stored in the EL scope by the simple name of the type
 * as variable name. You can override this by explicitly specifying the
 * <code>var</code> attribute, as demonstrated for <code>com.example.Baz</code>
 * in the above example.
 * <p>
 * The resolved constants are by reference stored in the cache to improve
 * retrieving performance.
 * 
 * @author Bauke Scholtz
 */
public class ImportConstants extends TagHandler {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    /**
     * Specific map implementation which wraps the given map in
     * {@link Collections#unmodifiableMap(Map)} and throws an
     * {@link IllegalArgumentException} in {@link ConstantsMap#get(Object)}
     * method when the key doesn't exist at all.
     * 
     * @author Bauke Scholtz
     */
    private static class ConstantsMap extends MapWrapper<String, Object> {

        private final String type;

        public ConstantsMap(final Map<String, Object> map, final String type) {
            super(Collections.unmodifiableMap(map));
            this.type = type;
        }

        @Override
        public Object get(final Object key) {
            if (!containsKey(key)) {
                throw new IllegalArgumentException(String.format(ImportConstants.ERROR_INVALID_CONSTANT, type, key));
            }

            return super.get(key);
        }

    }

    private static Map<String, Map<String, Object>> CONSTANTS_CACHE = new HashMap<String, Map<String, Object>>();
    private static final String ERROR_INVALID_VAR = "The 'var' attribute may not be an EL expression.";
    private static final String ERROR_MISSING_CLASS = "Cannot find type '%s' in classpath.";
    private static final String ERROR_FIELD_ACCESS = "Cannot access constant field '%s' of type '%s'.";

    // Variables
    // ------------------------------------------------------------------------------------------------------

    private static final String ERROR_INVALID_CONSTANT = "Type '%s' does not have the constant '%s'.";

    /**
     * Collect constants of the given type. That are, all public static final
     * fields of the given type.
     * 
     * @param type The fully qualified name of the type to collect constants
     *            for.
     * @return Constants of the given type.
     */
    private static Map<String, Object> collectConstants(final String type) {
        final Map<String, Object> constants = new HashMap<String, Object>();

        for (final Field field : ImportConstants.toClass(type).getFields()) {
            if (ImportConstants.isPublicStaticFinal(field)) {
                try {
                    constants.put(field.getName(), field.get(null));
                } catch (final Exception e) {
                    throw new IllegalArgumentException(String.format(ImportConstants.ERROR_FIELD_ACCESS, type, field
                        .getName()), e);
                }
            }
        }

        return new ConstantsMap(constants, type);
    }

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Returns whether the given field is a constant field, that is when it is
     * public, static and final.
     * 
     * @param field The field to be checked.
     * @return <code>true</code> if the given field is a constant field,
     *         otherwise <code>false</code>.
     */
    private static boolean isPublicStaticFinal(final Field field) {
        final int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Convert the given type, which should represent a fully qualified name, to
     * a concrete {@link Class} instance.
     * 
     * @param type The fully qualified name of the class.
     * @return The concrete {@link Class} instance.
     * @throws IllegalArgumentException When it is missing in the classpath.
     */
    private static Class<?> toClass(final String type) {
        try {
            return Class.forName(type);
        } catch (final ClassNotFoundException e) {
            // Perhaps it's an inner enum which is specified as
            // com.example.SomeClass.SomeEnum.
            // Let's be lenient on that although the proper type notation should
            // be com.example.SomeClass$SomeEnum.
            final int i = type.lastIndexOf('.');

            if (i > 0) {
                try {
                    return Class.forName(new StringBuilder(type.substring(0, i)).append('$')
                        .append(type.substring(i + 1)).toString());
                } catch (final ClassNotFoundException ignore) {
                    // Just continue to the IllegalArgumentException on the
                    // original ClassNotFoundException.
                }
            }

            throw new IllegalArgumentException(String.format(ImportConstants.ERROR_MISSING_CLASS, type), e);
        }
    }

    // Helpers
    // --------------------------------------------------------------------------------------------------------

    private String var;

    private final TagAttribute type;

    /**
     * The tag constructor.
     * 
     * @param config The tag config.
     */
    public ImportConstants(final TagConfig config) {
        super(config);
        final TagAttribute var = getAttribute("var");

        if (var != null) {
            if (var.isLiteral()) {
                this.var = var.getValue();
            } else {
                throw new IllegalArgumentException(ImportConstants.ERROR_INVALID_VAR);
            }
        }

        type = getRequiredAttribute("type");
    }

    // Nested classes
    // -------------------------------------------------------------------------------------------------

    /**
     * First obtain the constants of the class by its fully qualified name as
     * specified in the <code>type<code>
     * attribute from the cache. If it hasn't been collected yet and is thus not present in the cache, then collect
     * them and store in cache. Finally set the constants in the EL scope by the simple name of the type, or by the
     * name as specified in the <code>var</code> attribute, if any.
     */
    @Override
    public void apply(final FaceletContext context, final UIComponent parent) throws IOException {
        final String type = this.type.getValue(context);
        Map<String, Object> constants = ImportConstants.CONSTANTS_CACHE.get(type);

        if (constants == null) {
            constants = ImportConstants.collectConstants(type);
            ImportConstants.CONSTANTS_CACHE.put(type, constants);
        }

        context.setAttribute(var != null ? var : type.substring(type.lastIndexOf('.') + 1), constants);
    }

}