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
package org.omnifaces.el;

import java.lang.reflect.Method;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

/**
 * This MethodExpression wraps a ValueExpression.
 * <p>
 * With this wrapper a value expression can be used where a method expression is
 * expected. The return value of the method execution will be the value
 * represented by the value expression.
 * 
 * @author Arjan Tijms
 * 
 */
public class MethodExpressionValueExpressionAdapter extends MethodExpression {

    /**
     * Custom ELContext implementation that wraps a given ELContext to be able
     * to provide a custom ElResolver.
     * 
     */
    static class ValueToInvokeElContext extends ELContextWrapper {

        // The parameters provided by the client that calls the EL method
        // expression, as opposed to those
        // parameters that are bound to the expression when it's created in EL
        // (like #{bean.myMethod(param1, param2)}).
        private final Object[] callerProvidedParameters;

        public ValueToInvokeElContext(final ELContext elContext, final Object[] callerProvidedParameters) {
            super(elContext);
            this.callerProvidedParameters = callerProvidedParameters;
        }

        @Override
        public ELResolver getELResolver() {
            return new ValueToInvokeElResolver(super.getELResolver(), callerProvidedParameters);
        }
    }

    /**
     * Custom EL Resolver that turns calls for value expression calls (getValue)
     * into method expression calls (invoke).
     * 
     */
    static class ValueToInvokeElResolver extends ELResolverWrapper {

        // Null should theoretically be accepted, but some EL implementations
        // want an empty array.
        private static final Object[] EMPTY_PARAMETERS = new Object[0];
        private final Object[] callerProvidedParameters;

        public ValueToInvokeElResolver(final ELResolver elResolver, final Object[] callerProvidedParameters) {
            super(elResolver);
            this.callerProvidedParameters = callerProvidedParameters;
        }

        @Override
        public Object getValue(final ELContext context, final Object base, final Object property) {

            // If base is null, we're resolving it. Base should always be
            // resolved as a value expression.
            if (base == null) {
                return super.getValue(context, base, property);
            }

            // Turn getValue calls into invoke.

            // Note 1: We can not directly delegate to invoke() here, since
            // otherwise chained expressions
            // "like base.value.value.expression" will not resolve correctly.
            //
            // Note 2: Some EL implementations call getValue when invoke should
            // be called already. This typically happens when the
            // method expression takes no parameters, but is specified with
            // parentheses, e.g. "#{mybean.doAction()}".
            try {
                return super.getValue(context, base, property);
            } catch (final PropertyNotFoundException pnfe) {
                try {
                    return super.invoke(context, base, property, null,
                                        callerProvidedParameters != null
                                                                        ? callerProvidedParameters
                                                                        : ValueToInvokeElResolver.EMPTY_PARAMETERS);
                } catch (final MethodNotFoundException e) {

                    // Wrap into new ELException since down the call chain,
                    // ElExceptions might be caught, unwrapped one level and
                    // then wrapped in
                    // a new ELException. E.g. Mojarra 2.1's TagValueExpression
                    // does the following:
                    //
                    // catch (ELException e) {
                    // throw new ELException(this.attr + ": " + e.getMessage(),
                    // e.getCause());
                    // }
                    //
                    // Without wrapping here, we'll then loose this exception.

                    throw new ELException(e.getMessage(), e);
                }
            }
        }
    }

    private static final long serialVersionUID = 1L;

    private final ValueExpression valueExpression;

    public MethodExpressionValueExpressionAdapter(final ValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof MethodExpressionValueExpressionAdapter) {
            return ((MethodExpressionValueExpressionAdapter) obj).getValueExpression().equals(valueExpression);
        }

        return false;
    }

    @Override
    public String getExpressionString() {
        return valueExpression.getExpressionString();
    }

    @Override
    public MethodInfo getMethodInfo(final ELContext context) {

        final Method method = ExpressionInspector.getMethodReference(context, valueExpression).getMethod();

        return new MethodInfo(method.getName(), method.getReturnType(), method.getParameterTypes());
    }

    public ValueExpression getValueExpression() {
        return valueExpression;
    }

    @Override
    public int hashCode() {
        return valueExpression.hashCode();
    }

    @Override
    public Object invoke(final ELContext context, final Object[] params) {
        try {
            return valueExpression.getValue(new ValueToInvokeElContext(context, params));
        } catch (final ELException e) {

            // If the method is not found, an ELException will be thrown here
            // which wraps a MethodNotFoundException. However, normally
            // a MethodExpression#invoke will directly throw the
            // MethodNotFoundExpression and classes like
            // MethodExpressionActionListener
            // depend on this to try an invocation with and without the Event
            // parameter.

            // Try to find wrapped MethodNotFoundExpression and throw that if
            // found.
            Throwable throwable = e.getCause();
            while (throwable != null) {
                if (throwable instanceof MethodNotFoundException) {
                    throw (MethodNotFoundException) throwable;
                }
                throwable = throwable.getCause();
            }

            // Not found, just re-throw original
            throw e;
        }
    }

    @Override
    public boolean isLiteralText() {
        return false;
    }
}
