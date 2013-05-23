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
package org.primefaces.component.outputpanel;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;

public class OutputPanelRenderer extends CoreRenderer {

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final OutputPanel panel = (OutputPanel) component;
        final String clientId = panel.getClientId(context);
        final String tagName = getLayoutTag(context, panel);

        writer.startElement(tagName, panel);
        writer.writeAttribute("id", clientId, "id");

        if (panel.getStyle() != null) {
            writer.writeAttribute("style", panel.getStyle(), "style");
        }
        if (panel.getStyleClass() != null) {
            writer.writeAttribute("class", panel.getStyleClass(), "styleClass");
        }

        renderChildren(context, panel);

        writer.endElement(tagName);
    }

    protected String getLayoutTag(final FacesContext context, final OutputPanel panel) {
        final String layout = panel.getLayout();
        if (layout.equalsIgnoreCase("inline"))
            return "span";
        else if (layout.equalsIgnoreCase("block"))
            return "div";
        else throw new FacesException("Layout type '" + layout + "' is not a valid value for OutputPanel '"
            + panel.getClientId(context) + "'");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
