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
package org.omnifaces.component.output.cache.el;

import java.io.IOException;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import org.omnifaces.component.output.Cache;

/**
 * This handler wraps a value expression that's actually a method expression by
 * another value expression that returns a method expression that gets the value
 * of first value expression, which as "side-effect" executes the original
 * method expression.
 * 
 * <p>
 * This somewhat over-the-top chain of wrapping is done so a method expression
 * can be passed into a Facelet tag as parameter.
 * 
 * @author Arjan Tijms
 * 
 */
public class CacheValue extends TagHandler {

    private final TagAttribute name;
    private final TagAttribute value;

    public CacheValue(final TagConfig config) {
        super(config);
        name = getRequiredAttribute("name");
        value = getRequiredAttribute("value");
    }

    @Override
    public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {

        Cache cacheComponent;
        if (parent instanceof Cache) {
            cacheComponent = (Cache) parent;
        } else {
            throw new IllegalStateException("CacheValue components needs to have a Cache component as direct parent.");
        }

        final String nameStr = name.getValue(ctx);

        // The original value expression we get inside the Facelets tag
        final ValueExpression valueExpression = value.getValueExpression(ctx, Object.class);

        ctx.getVariableMapper().setVariable(nameStr,
                                            new CachingValueExpression(nameStr, valueExpression, cacheComponent));
    }
}