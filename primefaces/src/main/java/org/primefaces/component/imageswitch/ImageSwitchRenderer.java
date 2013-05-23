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
package org.primefaces.component.imageswitch;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class ImageSwitchRenderer extends CoreRenderer {

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        final ImageSwitch imageSwitch = (ImageSwitch) component;
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = imageSwitch.getClientId(context);

        writer.startElement("div", imageSwitch);
        writer.writeAttribute("id", clientId, "id");

        if (imageSwitch.getStyle() != null) writer.writeAttribute("style", imageSwitch.getStyle(), null);
        if (imageSwitch.getStyleClass() != null) writer.writeAttribute("class", imageSwitch.getStyleClass(), null);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ImageSwitch imageSwitch = (ImageSwitch) component;
        final String clientId = imageSwitch.getClientId(context);
        final ResponseWriter writer = context.getResponseWriter();
        final int slideshowSpeed = imageSwitch.isSlideshowAuto() ? imageSwitch.getSlideshowSpeed() : 0;

        writer.endElement("div");

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("ImageSwitch", imageSwitch.resolveWidgetVar(), clientId, "imageswitch", true).attr("fx",
                                                                                                     imageSwitch
                                                                                                         .getEffect())
            .attr("speed", imageSwitch.getSpeed()).attr("timeout", slideshowSpeed);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

}