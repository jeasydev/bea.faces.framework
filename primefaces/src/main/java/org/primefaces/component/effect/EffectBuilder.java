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
package org.primefaces.component.effect;

import org.primefaces.model.JSObjectBuilder;

public class EffectBuilder implements JSObjectBuilder {

    private final StringBuffer buffer;

    private boolean hasOption = false;

    public EffectBuilder(final String type, final String id) {
        buffer = new StringBuffer();
        buffer.append("$(PrimeFaces.escapeClientId('");
        buffer.append(id);
        buffer.append("'))");
        buffer.append(".effect('");
        buffer.append(type);
        buffer.append("',{");
    }

    public EffectBuilder atSpeed(final int speed) {
        buffer.append("},");
        buffer.append(speed);

        return this;
    }

    @Override
    public String build() {
        buffer.append(");");

        return buffer.toString();
    }

    public EffectBuilder withOption(final String name, final String value) {
        if (hasOption)
            buffer.append(",");
        else hasOption = true;

        buffer.append(name);
        buffer.append(":");
        buffer.append(value);

        return this;
    }
}
