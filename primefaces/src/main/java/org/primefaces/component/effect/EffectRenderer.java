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

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class EffectRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Effect effect = (Effect) component;
        final String clientId = effect.getClientId(context);
        String target = null;
        final String source = component.getParent().getClientId(context);
        final String event = effect.getEvent();
        final int delay = effect.getDelay();

        if (effect.getFor() != null) {
            final UIComponent _for = effect.findComponent(effect.getFor());
            if (_for != null)
                target = _for.getClientId(context);
            else throw new FacesException("Cannot find component \"" + effect.getFor() + "\" in view.");
        } else {
            target = source;
        }

        final String animation = getEffectBuilder(effect, target).build();

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Effect", effect.resolveWidgetVar(), clientId, true).attr("source", source).attr("event", event)
            .attr("delay", delay).callback("fn", "function()", animation);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    private EffectBuilder getEffectBuilder(final Effect effect, final String effectedComponentClientId) {
        final EffectBuilder effectBuilder = new EffectBuilder(effect.getType(), effectedComponentClientId);

        for (final UIComponent child : effect.getChildren()) {
            if (child instanceof UIParameter) {
                final UIParameter param = (UIParameter) child;

                effectBuilder.withOption(param.getName(), (String) param.getValue()); // TODO:
                                                                                      // Use
                                                                                      // converter
            }
        }

        effectBuilder.atSpeed(effect.getSpeed());

        return effectBuilder;
    }
}