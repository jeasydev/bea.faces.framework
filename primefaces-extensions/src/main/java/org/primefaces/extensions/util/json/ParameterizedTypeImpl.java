/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.util.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * An immutable implementation of the {@link ParameterizedType} interface.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 */
public final class ParameterizedTypeImpl implements ParameterizedType {

    private final Type rawType;
    private final Type[] actualTypeArguments;
    private final Type owner;

    public ParameterizedTypeImpl(final Type rawType, final Type[] actualTypeArguments, final Type owner) {
        this.rawType = rawType;
        this.actualTypeArguments = actualTypeArguments;
        this.owner = owner;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ParameterizedType)) {
            return false;
        }

        // Check that information is equivalent
        final ParameterizedType that = (ParameterizedType) o;
        if (this == that) {
            return true;
        }

        final Type thatOwner = that.getOwnerType();
        final Type thatRawType = that.getRawType();

        return (owner == null ? thatOwner == null : owner.equals(thatOwner))
            && (rawType == null ? thatRawType == null : rawType.equals(thatRawType))
            && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getOwnerType() {
        return owner;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments) ^ (owner == null ? 0 : owner.hashCode())
            ^ (rawType == null ? 0 : rawType.hashCode());
    }
}
