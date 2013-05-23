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
package org.primefaces.component.terminal;

import java.io.IOException;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class TerminalRenderer extends CoreRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Terminal terminal = (Terminal) component;

        if (context.getExternalContext().getRequestParameterMap().containsKey(terminal.getClientId(context))) {
            handleCommand(context, component);
        } else {
            encodeMarkup(context, terminal);
            encodeScript(context, terminal);
        }
    }

    protected void encodeMarkup(final FacesContext context, final Terminal terminal) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = terminal.getClientId(context);

        writer.startElement("div", terminal);
        writer.writeAttribute("id", clientId, "id");
        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final Terminal terminal) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = terminal.getClientId(context);
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("Terminal", terminal.resolveWidgetVar(), clientId, "terminal", true)
            .attr("PS1", terminal.getPrompt()).attr("WELCOME_MESSAGE", terminal.getWelcomeMessage(), null)
            .attr("WIDTH", terminal.getWidth()).attr("HEIGHT", terminal.getHeight());

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void handleCommand(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final Terminal terminal = (Terminal) component;
        final String clientId = terminal.getClientId(context);
        final String argsParam = context.getExternalContext().getRequestParameterMap().get(clientId + "_args");
        final String tokens[] = argsParam.split(",");
        final String command = tokens[0];
        String[] args;
        if (tokens.length > 1) {
            args = new String[tokens.length - 1];
            for (int t = 1; t < tokens.length; t++) {
                args[t - 1] = tokens[t];
            }
        } else {
            args = new String[0];
        }

        final MethodExpression commandHandler = terminal.getCommandHandler();
        final String result = (String) commandHandler.invoke(context.getELContext(), new Object[] { command, args });

        writer.write(result);
    }
}
