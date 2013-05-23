package org.omnifaces.el;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import org.omnifaces.util.Hacks;

/**
 * Special purpose value expression that wraps a method expression for which a
 * Method instance is created whenever the getValue method is called.
 * 
 * @author Arjan Tijms
 * @since 1.4
 */
public final class ValueExpressionMethodWrapper extends ValueExpression {

    private static final long serialVersionUID = 891954866066788234L;

    private final MethodExpression methodExpression;

    public ValueExpressionMethodWrapper(final MethodExpression methodExpression) {
        this.methodExpression = methodExpression;
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof ValueExpressionMethodWrapper && equals((ValueExpressionMethodWrapper) obj));
    }

    public boolean equals(final ValueExpressionMethodWrapper other) {
        return other != null
            && (methodExpression != null && other.methodExpression != null && (methodExpression == other.methodExpression || methodExpression
                .equals(other.methodExpression)));
    }

    @Override
    public Class<?> getExpectedType() {
        return methodExpression.getClass();
    }

    @Override
    public String getExpressionString() {
        return methodExpression.toString();
    }

    @Override
    public Class<?> getType(final ELContext context) {
        return methodExpression.getClass();
    }

    @Override
    public Object getValue(final ELContext context) {
        return Hacks.methodExpressionToStaticMethod(context, methodExpression);
    }

    @Override
    public int hashCode() {
        return methodExpression.hashCode();
    }

    @Override
    public boolean isLiteralText() {
        return true;
    }

    @Override
    public boolean isReadOnly(final ELContext context) {
        return true;
    }

    @Override
    public void setValue(final ELContext context, final Object value) {
        throw new PropertyNotWritableException();
    }

}
