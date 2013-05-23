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
package org.primefaces.component.slider;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.WidgetBuilder;

public class SliderRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Slider slider = (Slider) component;

        encodeMarkup(context, slider);
        encodeScript(context, slider);
    }

    protected void encodeMarkup(final FacesContext context, final Slider slider) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = slider.getClientId(context);

        writer.startElement("div", slider);
        writer.writeAttribute("id", clientId, "id");
        if (slider.getStyle() != null) writer.writeAttribute("style", slider.getStyle(), null);
        if (slider.getStyleClass() != null) writer.writeAttribute("class", slider.getStyleClass(), null);

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final Slider slider) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = slider.getClientId(context);
        final boolean range = slider.isRange();
        final UIComponent output = getTarget(context, slider, slider.getDisplay());

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Slider", slider.resolveWidgetVar(), clientId, true);

        if (range) {
            final String[] inputIds = slider.getFor().split(",");
            final UIComponent inputMin = getTarget(context, slider, inputIds[0].trim());
            final UIComponent inputMax = getTarget(context, slider, inputIds[1].trim());
            final String inputMinValue = ComponentUtils.getValueToRender(context, inputMin);
            final String inputMaxValue = ComponentUtils.getValueToRender(context, inputMax);

            wb.attr("input", inputMin.getClientId(context) + "," + inputMax.getClientId(context)).append(",values:[")
                .append(inputMinValue).append(",").append(inputMaxValue).append("]");
        } else {
            final UIComponent input = getTarget(context, slider, slider.getFor());

            wb.attr("value", ComponentUtils.getValueToRender(context, input)).attr("input", input.getClientId(context));
        }

        wb.attr("min", slider.getMinValue()).attr("max", slider.getMaxValue()).attr("animate", slider.isAnimate())
            .attr("step", slider.getStep()).attr("orientation", slider.getType()).attr("disabled", slider.isDisabled(),
                                                                                       false).attr("range", range)
            .attr("displayTemplate", slider.getDisplayTemplate(), null).callback("onSlideStart", "function(event,ui)",
                                                                                 slider.getOnSlideStart())
            .callback("onSlide", "function(event,ui)", slider.getOnSlide()).callback("onSlideEnd",
                                                                                     "function(event,ui)",
                                                                                     slider.getOnSlideEnd());

        if (output != null) {
            wb.attr("display", output.getClientId(context));
        }

        encodeClientBehaviors(context, slider, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected UIComponent getTarget(final FacesContext context, final Slider slider, final String target) {
        if (target == null) {
            return null;
        } else {
            final UIComponent targetComponent = slider.findComponent(target);
            if (targetComponent == null) {
                throw new FacesException("Cannot find component with identifier \"" + target + "\" referenced from \""
                    + slider.getClientId(context) + "\".");
            }

            return targetComponent;
        }
    }
}