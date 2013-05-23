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

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.DelegatingMetaTagHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagHandlerDelegate;
import javax.faces.view.facelets.ValidatorHandler;

/**
 * Helper class for OmniFaces {@link Converter} and {@link Validator}. It can't
 * be an abstract class as they have to extend from {@link ConverterHandler} and
 * {@link ValidatorHandler}.
 * 
 * @author Bauke Scholtz
 */
final class RenderTimeTagHandlerHelper {

    // Private constants
    // ----------------------------------------------------------------------------------------------

    /**
     * Convenience class which holds all render time attribute setters and value
     * expressions.
     * 
     * @author Bauke Scholtz
     */
    static class RenderTimeAttributes {

        private final Map<Method, ValueExpression> attributes;

        private RenderTimeAttributes() {
            attributes = new HashMap<Method, ValueExpression>();
        }

        private void add(final Method setter, final ValueExpression valueExpression) {
            attributes.put(setter, valueExpression);
        }

        public void invokeSetters(final ELContext elContext, final Object object) {
            for (final Entry<Method, ValueExpression> entry : attributes.entrySet()) {
                try {
                    entry.getKey().invoke(object, entry.getValue().getValue(elContext));
                } catch (final Exception e) {
                    throw new FacesException(e);
                }
            }
        }
    }

    /**
     * So that we can extract tag attributes from both {@link ConverterHandler}
     * and {@link ValidatorHandler} and create concrete {@link Converter} and
     * {@link Validator} instances.
     * 
     * @author Bauke Scholtz
     */
    static interface RenderTimeTagHandler {

        /**
         * Create the concrete {@link Converter} or {@link Validator}.
         */
        public <T> T create(Application application, String id);

        /**
         * Just return TagHandler#getAttribute() via a public method (it's by
         * default protected and otherwise thus unavailable inside
         * collectRenderTimeAttributes().
         */
        public TagAttribute getTagAttribute(String name);

    }

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Convenience tag handler delegate which delegates
     * {@link #applyAttachedObject(FacesContext, UIComponent)} to the owning tag
     * itself. This all is used when composite components come into picture.
     * 
     * @author Bauke Scholtz
     */
    static class RenderTimeTagHandlerDelegate extends TagHandlerDelegate implements AttachedObjectHandler {

        private final DelegatingMetaTagHandler tag;
        private final TagHandlerDelegate delegate;

        public RenderTimeTagHandlerDelegate(final DelegatingMetaTagHandler tag, final TagHandlerDelegate delegate) {
            this.tag = tag;
            this.delegate = delegate;
        }

        @Override
        public void apply(final FaceletContext context, final UIComponent component) throws IOException {
            delegate.apply(context, component);
        }

        @Override
        public void applyAttachedObject(final FacesContext context, final UIComponent parent) {
            try {
                tag.apply((FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY), parent);
            } catch (final IOException e) {
                throw new FacesException(e);
            }
        }

        @Override
        public MetaRuleset createMetaRuleset(@SuppressWarnings("rawtypes") final Class type) {
            return delegate.createMetaRuleset(type);
        }

        @Override
        public String getFor() {
            return ((AttachedObjectHandler) delegate).getFor();
        }

    }

    // Actions
    // --------------------------------------------------------------------------------------------------------

    private static final String ERROR_MISSING_ID = "%s '%s' or 'binding' attribute must be specified.";

    private static final String ERROR_INVALID_ID = "%s '%s' attribute must refer an valid %1$s ID. The %1$s ID '%s' cannot be found.";

    /**
     * Collect the EL properties of the given object. If the property is a
     * literal text (i.e. no EL expression), then it will just be set directly
     * on the given object, else it will be collected and returned.
     * 
     * @param context The involved facelet context.
     * @param instance The instance to collect EL properties for.
     * @return The EL properties of the given object.
     */
    static <T> RenderTimeAttributes collectRenderTimeAttributes(final FaceletContext context,
                                                                final RenderTimeTagHandler tag,
                                                                final T instance) {
        final RenderTimeAttributes attributes = new RenderTimeAttributes();

        try {
            for (final PropertyDescriptor property : Introspector.getBeanInfo(instance.getClass())
                .getPropertyDescriptors()) {
                final Method setter = property.getWriteMethod();

                if (setter == null) {
                    continue;
                }

                final ValueExpression ve = RenderTimeTagHandlerHelper.getValueExpression(context, tag, property
                    .getName(), property.getPropertyType());

                if (ve == null) {
                    continue;
                }

                if (ve.isLiteralText()) {
                    setter.invoke(instance, ve.getValue(context));
                } else {
                    attributes.add(setter, ve);
                }
            }
        } catch (final Exception e) {
            throw new FacesException(e);
        }

        return attributes;
    }

    // Nested classes
    // -------------------------------------------------------------------------------------------------

    /**
     * Create the tag instance based on the <code>binding</code> and/or
     * <code>instanceId</code> attribute.
     * 
     * @param context The involved facelet context.
     * @param tag The involved tag handler.
     * @param instanceId The attribute name representing the instance ID.
     * @return The created instance.
     * @throws IllegalArgumentException If the <code>validatorId</code>
     *             attribute is invalid or missing while the
     *             <code>binding</code> attribute is also missing.
     * @throws ClassCastException When <code>T</code> is of wrong type.
     */
    @SuppressWarnings("unchecked")
    static <T> T createInstance(final FaceletContext context, final RenderTimeTagHandler tag, final String instanceId) {
        final ValueExpression binding = RenderTimeTagHandlerHelper.getValueExpression(context, tag, "binding",
                                                                                      Object.class);
        final ValueExpression id = RenderTimeTagHandlerHelper
            .getValueExpression(context, tag, instanceId, String.class);
        T instance = null;

        if (binding != null) {
            instance = (T) binding.getValue(context);
        }

        if (id != null) {
            try {
                instance = tag.create(context.getFacesContext().getApplication(), (String) id.getValue(context));
            } catch (final FacesException e) {
                throw new IllegalArgumentException(String.format(RenderTimeTagHandlerHelper.ERROR_INVALID_ID, tag
                    .getClass().getSimpleName(), instanceId, id));
            }

            if (binding != null) {
                binding.setValue(context, instance);
            }
        } else if (instance == null) {
            throw new IllegalArgumentException(String.format(RenderTimeTagHandlerHelper.ERROR_MISSING_ID, tag
                .getClass().getSimpleName(), instanceId));
        }

        return instance;
    }

    /**
     * Convenience method to get the given attribute as a
     * {@link ValueExpression}, or <code>null</code> if there is no such
     * attribute.
     * 
     * @param context The involved facelet context.
     * @param name The attribute name to return the value expression for.
     * @param type The type of the value expression.
     * @return The given attribute as a {@link ValueExpression}.
     */
    static <T> ValueExpression getValueExpression(final FaceletContext context,
                                                  final RenderTimeTagHandler tag,
                                                  final String name,
                                                  final Class<T> type) {
        final TagAttribute attribute = tag.getTagAttribute(name);
        return (attribute != null) ? attribute.getValueExpression(context, type) : null;
    }

    /**
     * Hide constructor.
     */
    private RenderTimeTagHandlerHelper() {
        //
    }

}