/*
 * Copyright 2009-2012 Prime Teknoloji.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.facelets;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;

/**
 * Optional Rule for binding Method[Binding|Expression] properties
 * 
 * @author Mike Kienenberger
 * @author Jacob Hookom
 * 
 *         Implementation copied from Facelets 1.1.14, as it got hidden by JSF
 *         2.0
 */
public class MethodRule extends MetaRule {

    private static class LegacyMethodBinding extends MethodBinding implements Serializable {

        private static final long serialVersionUID = 1L;

        private final MethodExpression m;

        public LegacyMethodBinding(final MethodExpression m) {
            this.m = m;
        }

        /*
         * (non-Javadoc)
         * @see
         * javax.faces.el.MethodBinding#getType(javax.faces.context.FacesContext
         * )
         */

        @Override
        public String getExpressionString() {
            return m.getExpressionString();
        }

        /*
         * (non-Javadoc)
         * @see
         * javax.faces.el.MethodBinding#invoke(javax.faces.context.FacesContext,
         * java.lang.Object[])
         */

        @Override
        public Class getType(final FacesContext context) throws MethodNotFoundException {
            try {
                return m.getMethodInfo(context.getELContext()).getReturnType();
            } catch (final javax.el.MethodNotFoundException e) {
                throw new MethodNotFoundException(e.getMessage(), e.getCause());
            } catch (final ELException e) {
                throw new EvaluationException(e.getMessage(), e.getCause());
            }
        }

        @Override
        public Object invoke(final FacesContext context, final Object[] params) throws EvaluationException,
            MethodNotFoundException {
            try {
                return m.invoke(context.getELContext(), params);
            } catch (final javax.el.MethodNotFoundException e) {
                throw new MethodNotFoundException(e.getMessage(), e.getCause());
            } catch (final ELException e) {
                throw new EvaluationException(e.getMessage(), e.getCause());
            }
        }
    }

    private static class MethodBindingMetadata extends Metadata {
        private final Method _method;

        private final TagAttribute _attribute;

        private final Class[] _paramList;

        private final Class _returnType;

        public MethodBindingMetadata(final Method method,
                                     final TagAttribute attribute,
                                     final Class returnType,
                                     final Class[] paramList) {
            _method = method;
            _attribute = attribute;
            _paramList = paramList;
            _returnType = returnType;
        }

        @Override
        public void applyMetadata(final FaceletContext ctx, final Object instance) {
            final MethodExpression expr = _attribute.getMethodExpression(ctx, _returnType, _paramList);

            try {
                _method.invoke(instance, new Object[] { new LegacyMethodBinding(expr) });
            } catch (final InvocationTargetException e) {
                throw new TagAttributeException(_attribute, e.getCause());
            } catch (final Exception e) {
                throw new TagAttributeException(_attribute, e);
            }
        }
    }

    private static class MethodExpressionMetadata extends Metadata {
        private final Method _method;

        private final TagAttribute _attribute;

        private final Class[] _paramList;

        private final Class _returnType;

        public MethodExpressionMetadata(final Method method,
                                        final TagAttribute attribute,
                                        final Class returnType,
                                        final Class[] paramList) {
            _method = method;
            _attribute = attribute;
            _paramList = paramList;
            _returnType = returnType;
        }

        @Override
        public void applyMetadata(final FaceletContext ctx, final Object instance) {
            final MethodExpression expr = _attribute.getMethodExpression(ctx, _returnType, _paramList);

            try {
                _method.invoke(instance, new Object[] { expr });
            } catch (final InvocationTargetException e) {
                throw new TagAttributeException(_attribute, e.getCause());
            } catch (final Exception e) {
                throw new TagAttributeException(_attribute, e);
            }
        }
    }

    private final String methodName;

    private final Class returnTypeClass;

    private final Class[] params;

    public MethodRule(final String methodName, final Class returnTypeClass, final Class[] params) {
        this.methodName = methodName;
        this.returnTypeClass = returnTypeClass;
        this.params = params;
    }

    @Override
    public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget meta) {
        if (false == name.equals(methodName)) return null;

        if (MethodBinding.class.equals(meta.getPropertyType(name))) {
            final Method method = meta.getWriteMethod(name);
            if (method != null) {
                return new MethodBindingMetadata(method, attribute, returnTypeClass, params);
            }
        } else if (MethodExpression.class.equals(meta.getPropertyType(name))) {
            final Method method = meta.getWriteMethod(name);
            if (method != null) {
                return new MethodExpressionMetadata(method, attribute, returnTypeClass, params);
            }
        }

        return null;
    }
}
