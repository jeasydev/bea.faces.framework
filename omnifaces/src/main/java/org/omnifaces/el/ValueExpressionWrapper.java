package org.omnifaces.el;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ValueReference;
import javax.faces.FacesWrapper;

public class ValueExpressionWrapper extends ValueExpression implements FacesWrapper<ValueExpression> {

    private static final long serialVersionUID = -6864367137534689191L;

    private final ValueExpression valueExpression;

    public ValueExpressionWrapper(final ValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    @Override
    public boolean equals(final Object obj) {
        return getWrapped().equals(obj);
    }

    @Override
    public Class<?> getExpectedType() {
        return getWrapped().getExpectedType();
    }

    @Override
    public String getExpressionString() {
        return getWrapped().getExpressionString();
    }

    @Override
    public Class<?> getType(final ELContext context) {
        return getWrapped().getType(context);
    }

    @Override
    public Object getValue(final ELContext context) {
        return getWrapped().getValue(context);
    }

    @Override
    public ValueReference getValueReference(final ELContext context) {
        return getWrapped().getValueReference(context);
    }

    @Override
    public ValueExpression getWrapped() {
        return valueExpression;
    }

    @Override
    public int hashCode() {
        return getWrapped().hashCode();
    }

    @Override
    public boolean isLiteralText() {
        return getWrapped().isLiteralText();
    }

    @Override
    public boolean isReadOnly(final ELContext context) {
        return getWrapped().isReadOnly(context);
    }

    @Override
    public void setValue(final ELContext context, final Object value) {
        getWrapped().setValue(context, value);
    }

}
