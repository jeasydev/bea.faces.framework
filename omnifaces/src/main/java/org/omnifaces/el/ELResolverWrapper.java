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

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.FacesWrapper;

/**
 * <p>
 * Provides a simple implementation of {@link ELResolver} that can be
 * sub-classed by developers wishing to provide specialized behavior to an
 * existing {@link ELResolver} instance. The default implementation of all
 * methods is to call through to the wrapped {@link ELResolver}.
 * </p>
 * 
 * <p>
 * Usage: extend this class and override {@link #getWrapped} to return the
 * instance we are wrapping, or provide this instance to the overloaded
 * constructor.
 * </p>
 * 
 * @author Arjan Tijms
 */
public class ELResolverWrapper extends ELResolver implements FacesWrapper<ELResolver> {

    private ELResolver elResolver;

    public ELResolverWrapper() {
    }

    public ELResolverWrapper(final ELResolver elResolver) {
        this.elResolver = elResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getCommonPropertyType(final ELContext context, final Object base) {
        return getWrapped().getCommonPropertyType(context, base);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(final ELContext context, final Object base) {
        return getWrapped().getFeatureDescriptors(context, base);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getType(final ELContext context, final Object base, final Object property) {
        return getWrapped().getType(context, base, property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(final ELContext context, final Object base, final Object property) {
        return getWrapped().getValue(context, base, property);
    }

    @Override
    public ELResolver getWrapped() {
        return elResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(final ELContext context,
                         final Object base,
                         final Object method,
                         final Class<?>[] paramTypes,
                         final Object[] params) {
        return getWrapped().invoke(context, base, method, paramTypes, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReadOnly(final ELContext context, final Object base, final Object property) {
        return getWrapped().isReadOnly(context, base, property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final ELContext context, final Object base, final Object property, final Object value) {
        getWrapped().setValue(context, base, property, value);
    }

}
