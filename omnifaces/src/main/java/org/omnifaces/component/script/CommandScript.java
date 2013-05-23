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
package org.omnifaces.component.script;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import org.omnifaces.util.Components;
import org.omnifaces.util.Json;
import org.omnifaces.util.State;
import org.omnifaces.util.Utils;

/**
 * <p>
 * <strong>CommandScript</strong> is an extension to
 * <code>&lt;h:commandXxx&gt;</code> which generates a JavaScript function in
 * the global JavaScript scope which allows the end-user to execute a JSF ajax
 * request by just a function call <code>functionName()</code> in the JavaScript
 * context.
 * <p>
 * The <code>&lt;o:commandScript&gt;</code> component is required to be enclosed
 * in a {@link UIForm} component. The <code>name</code> attribute is required
 * and it represents the JavaScript function name. The <code>execute</code> and
 * <code>render</code> attributes work exactly the same as in
 * <code>&lt;f:ajax&gt;</code>. The <code>onbegin</code> and
 * <code>oncomplete</code> attributes must represent (valid!) JavaScript code
 * which will be executed before sending the ajax request and after processing
 * the ajax response respectively. The <code>action</code>,
 * <code>actionListener</code> and <code>immediate</code> attributes work
 * exactly the same as in <code>&lt;h:commandXxx&gt;</code>.
 * <p>
 * Basic usage example of <code>&lt;o:commandScript&gt;</code> which submits the
 * entire form on click of a plain HTML button:
 * 
 * <pre>
 * &lt;h:form&gt;
 *     &lt;h:inputText value="#{bean.input1}" ... /&gt;
 *     &lt;h:inputText value="#{bean.input2}" ... /&gt;
 *     &lt;h:inputText value="#{bean.input3}" ... /&gt;
 *     &lt;o:commandScript name="submitForm" action="#{bean.submit}" render="@form" /&gt;
 * &lt;/h:form&gt;
 * &lt;input type="button" value="submit" onclick="submitForm()" /&gt;
 * </pre>
 * <p>
 * Usage example which uses the <code>&lt;o:commandScript&gt;</code> as a poll
 * function which updates every 3 seconds:
 * 
 * <pre>
 * &lt;h:form&gt;
 *     &lt;h:dataTable id="data" value="#{bean.data}" ...&gt;...&lt;/h:dataTable&gt;
 *     &lt;o:commandScript name="updateData" action="#{bean.reloadData}" render="data" /&gt;
 * &lt;/h:form&gt;
 * &lt;h:outputScript target="body"&gt;setInterval(updateData, 3000);&lt;/h:outputScript&gt;
 * </pre>
 * <p>
 * The component also supports nesting of <code>&lt;f:param&gt;</code>,
 * <code>&lt;f:actionListener&gt;</code> and
 * <code>&lt;f:setPropertyActionListener&gt;</code>, exactly like as in
 * <code>&lt;h:commandXxx&gt;</code>. The function also supports a JS object as
 * argument which will then end up in the HTTP request parameter map:
 * 
 * <pre>
 * functionName({ name1: "value1", name2: "value2" });
 * </pre>
 * <p>
 * With the above example, the parameters are in the action method available as
 * follows:
 * 
 * <pre>
 * String name1 = Faces.getRequestParameter(&quot;name1&quot;); // value1
 * String name2 = Faces.getRequestParameter(&quot;name2&quot;); // value2
 * </pre>
 * 
 * @author Bauke Scholtz
 * @since 1.3
 */
@FacesComponent(CommandScript.COMPONENT_TYPE)
@ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head")
public class CommandScript extends UICommand {

    // Public constants
    // -----------------------------------------------------------------------------------------------

    private enum PropertyKeys {
        // Cannot be uppercased. They have to exactly match the attribute names.
        name,
        execute,
        render,
        onbegin,
        oncomplete;
    }

    // Private constants
    // ----------------------------------------------------------------------------------------------

    public static final String COMPONENT_TYPE = "org.omnifaces.component.script.CommandScript";

    private static final Pattern PATTERN_NAME = Pattern.compile("[$a-z_][$\\w]*", Pattern.CASE_INSENSITIVE);
    private static final String ERROR_MISSING_NAME = "o:commandScript 'name' attribute must be specified.";
    private static final String ERROR_ILLEGAL_NAME = "o:commandScript 'name' attribute '%s' does not represent a valid script function name.";

    private static final String ERROR_UNKNOWN_CLIENTID = "o:commandScript execute/render client ID '%s' cannot be found relative to parent NamingContainer component"
        + " with client ID '%s'.";

    // Variables
    // ------------------------------------------------------------------------------------------------------

    private final State state = new State(getStateHelper());

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * If this command script was invoked, queue the {@link ActionEvent}
     * accordingly.
     */
    @Override
    public void decode(final FacesContext context) {
        final String source = context.getExternalContext().getRequestParameterMap().get("javax.faces.source");

        if (getClientId(context).equals(source)) {
            final ActionEvent event = new ActionEvent(this);
            event.setPhaseId(isImmediate() ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION);
            queueEvent(event);
        }
    }

    /**
     * Write a <code>&lt;span&gt;&lt;script&gt;</code> with therein the script
     * function which allows the end-user to execute a JSF ajax request by a
     * just script function call <code>functionName()</code> in the JavaScript
     * context.
     * 
     * @throws IllegalArgumentException When there is no parent form, or when
     *             the <code>name</code> attribute is missing, or when the
     *             <code>name</code> attribute does not represent a valid script
     *             function name.
     */
    @Override
    public void encodeChildren(final FacesContext context) throws IOException {
        Components.validateHasParent(this, UIForm.class);
        final String name = getName();

        if (name == null) {
            throw new IllegalArgumentException(CommandScript.ERROR_MISSING_NAME);
        }

        if (!CommandScript.PATTERN_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException(String.format(CommandScript.ERROR_ILLEGAL_NAME, name));
        }

        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", this);
        writer.writeAttribute("id", getClientId(context), "id");
        writer.startElement("script", this);
        writer.writeAttribute("type", "text/javascript", "type");
        encodeFunction(context, name);
        writer.endElement("script");
        writer.endElement("span");
    }

    /**
     * Encode the script function. It has the following syntax:
     * 
     * <pre>
     * var name = function() {
     *     jsf.ajax.request(clientId, null, options);
     * });
     * </pre>
     * 
     * The first argument <code>clientId</code> is the client ID of the current
     * component which will ultimately be sent as
     * <code>javax.faces.source</code> request parameter, so that the
     * {@link #decode(FacesContext)} can properly intercept on it. The second
     * argument is the event type, which is irrelevant here. The third argument
     * is a JS object which holds the jsf.ajax.request options, such as
     * additional request parameters from <code>&lt;f:param&gt;</code>, the
     * values of <code>execute</code> and <code>render</code> attributes and the
     * <code>onevent</code> function which contains the <code>onbegin</code> and
     * <code>oncomplete</code> scripts.
     * 
     * @param context The faces context to work with.
     * @param name The script function name.
     * @throws IOException When something fails at I/O level.
     */
    protected void encodeFunction(final FacesContext context, final String name) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.append("var ").append(name).append('=').append("function(o){var o=(typeof o==='object')&&o?o:{};");
        encodeOptions(context);
        writer.append("jsf.ajax.request('").append(getClientId(context)).append("',null,o)}");
    }

    /**
     * Create an option for the <code>onevent</code> function which contains the
     * <code>onbegin</code> and <code>oncomplete</code> scripts. This will
     * return <code>null</code> when no scripts are been definied.
     * 
     * @param context The faces context to work with.
     * @param onbegin The onbegin script.
     * @param oncomplete The oncomplete script.
     * @throws IOException When something fails at I/O level.
     */
    protected void encodeOneventOption(final FacesContext context, final String onbegin, final String oncomplete)
        throws IOException {
        if (onbegin == null && oncomplete == null) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        writer.write("o.onevent=function(data){");

        if (onbegin != null) {
            writer.append("if(data.status=='begin'){").append(onbegin).append('}');
        }

        if (oncomplete != null) {
            writer.append("if(data.status=='success'){").append(oncomplete).append('}');
        }

        writer.write("};");
    }

    /**
     * Encode the JS object which holds the jsf.ajax.request options, such as
     * additional request parameters from <code>&lt;f:param&gt;</code>, the
     * values of <code>execute</code> and <code>render</code> attributes and the
     * <code>onevent</code> function which contains the <code>onbegin</code> and
     * <code>oncomplete</code> scripts.
     * 
     * @param context The faces context to work with.
     * @throws IOException When something fails at I/O level.
     */
    protected void encodeOptions(final FacesContext context) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIParameter) {
                    final UIParameter param = (UIParameter) child;
                    final String name = param.getName();

                    if (!Utils.isEmpty(name)) {
                        writer.append("o[").append(Json.encode(name)).append("]=")
                            .append(Json.encode(param.getValue())).append(";");
                    }
                }
            }
        }

        writer.append("o['javax.faces.behavior.event']='action';");
        writer.append("o.execute='").append(resolveClientIds(context, getExecute())).append("';");
        writer.append("o.render='").append(resolveClientIds(context, getRender())).append("';");
        encodeOneventOption(context, getOnbegin(), getOncomplete());
    }

    /**
     * Returns a space separated string of client IDs to process on ajax
     * request.
     * 
     * @return A space separated string of client IDs to process on ajax
     *         request.
     */
    public String getExecute() {
        return state.get(PropertyKeys.execute, "@this");
    }

    /**
     * Returns {@link ScriptFamily#COMPONENT_FAMILY}.
     */
    @Override
    public String getFamily() {
        return ScriptFamily.COMPONENT_FAMILY;
    }

    /**
     * Returns the script function name.
     * 
     * @return The script function name.
     */
    public String getName() {
        return state.get(PropertyKeys.name);
    }

    // Attribute getters/setters
    // --------------------------------------------------------------------------------------

    /**
     * Returns a script to execute before ajax request is fired.
     * 
     * @return A script to execute before ajax request is fired.
     */
    public String getOnbegin() {
        return state.get(PropertyKeys.onbegin);
    }

    /**
     * Returns a script to execute after ajax response is processed.
     * 
     * @return A script to execute after ajax response is processed.
     */
    public String getOncomplete() {
        return state.get(PropertyKeys.oncomplete);
    }

    /**
     * Returns a space separated string of client IDs to update on ajax
     * response.
     * 
     * @return A space separated string of client IDs to update on ajax
     *         response.
     */
    public String getRender() {
        return state.get(PropertyKeys.render, "@none");
    }

    /**
     * Returns <code>true</code>.
     */
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * Resolve the given space separated collection of relative client IDs to
     * absolute client IDs.
     * 
     * @param context The faces context to work with.
     * @param relativeClientIds The space separated collection of relative
     *            client IDs to be resolved.
     * @return A space separated collection of absolute client IDs, or
     *         <code>null</code> if the given relative client IDs is empty.
     */
    protected String resolveClientIds(final FacesContext context, final String relativeClientIds) {
        if (Utils.isEmpty(relativeClientIds)) {
            return null;
        }

        final StringBuilder absoluteClientIds = new StringBuilder();

        for (final String relativeClientId : relativeClientIds.split("\\s+")) {
            if (absoluteClientIds.length() > 0) {
                absoluteClientIds.append(' ');
            }

            if (relativeClientId.charAt(0) == '@') {
                absoluteClientIds.append(relativeClientId);
            } else {
                final UIComponent found = findComponent(relativeClientId);

                if (found == null) {
                    throw new IllegalArgumentException(String.format(CommandScript.ERROR_UNKNOWN_CLIENTID,
                                                                     relativeClientId, getNamingContainer()
                                                                         .getClientId(context)));
                }

                absoluteClientIds.append(found.getClientId(context));
            }
        }

        return absoluteClientIds.toString();
    }

    /**
     * Sets a space separated string of client IDs to process on ajax request.
     * 
     * @param execute A space separated string of client IDs to process on ajax
     *            request.
     */
    public void setExecute(final String execute) {
        state.put(PropertyKeys.execute, execute);
    }

    /**
     * Sets the script function name.
     * 
     * @param name The script function name.
     */
    public void setName(final String name) {
        state.put(PropertyKeys.name, name);
    }

    /**
     * Sets a script to execute before ajax request is fired.
     * 
     * @param onbegin A script to execute before ajax request is fired.
     */
    public void setOnbegin(final String onbegin) {
        state.put(PropertyKeys.onbegin, onbegin);
    }

    /**
     * Sets a script to execute after ajax response is processed.
     * 
     * @param oncomplete A script to execute after ajax response is processed.
     */
    public void setOncomplete(final String oncomplete) {
        state.put(PropertyKeys.oncomplete, oncomplete);
    }

    /**
     * Sets a space separated string of client IDs to update on ajax response.
     * 
     * @param render A space separated string of client IDs to update on ajax
     *            response.
     */
    public void setRender(final String render) {
        state.put(PropertyKeys.render, render);
    }

}