/*
 * Generated, Do Not Modify
 */
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
package org.primefaces.component.inputtextarea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class InputTextarea extends HtmlInputTextarea implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        autoResize,
        maxlength,
        counter,
        counterTemplate,
        completeMethod,
        minQueryLength,
        queryDelay,
        scrollHeight;

        String toString;

        PropertyKeys() {
        }

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    public static final String COMPONENT_TYPE = "org.primefaces.component.InputTextarea";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.InputTextareaRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList("blur", "change", "valueChange", "click", "dblclick", "focus", "keydown", "keypress", "keyup",
                "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "select", "itemSelect"));

    public final static String STYLE_CLASS = "ui-inputfield ui-inputtextarea ui-widget ui-state-default ui-corner-all";

    private List suggestions = null;

    public InputTextarea() {
        setRendererType(InputTextarea.DEFAULT_RENDERER);
    }

    @Override
    public void broadcast(final javax.faces.event.FacesEvent event) throws javax.faces.event.AbortProcessingException {
        super.broadcast(event);

        final FacesContext facesContext = getFacesContext();
        final MethodExpression me = getCompleteMethod();

        if (me != null && event instanceof org.primefaces.event.AutoCompleteEvent) {
            suggestions = (List) me
                .invoke(facesContext.getELContext(), new Object[] { ((org.primefaces.event.AutoCompleteEvent) event)
                    .getQuery() });

            if (suggestions == null) {
                suggestions = new ArrayList();
            }

            facesContext.renderResponse();
        }
    }

    @Override
    public int getCols() {
        final int cols = super.getCols();

        return cols > 0 ? cols : 20;
    }

    public javax.el.MethodExpression getCompleteMethod() {
        return (javax.el.MethodExpression) getStateHelper().eval(PropertyKeys.completeMethod, null);
    }

    public java.lang.String getCounter() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.counter, null);
    }

    public java.lang.String getCounterTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.counterTemplate, null);
    }

    @Override
    public Collection<String> getEventNames() {
        return InputTextarea.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return InputTextarea.COMPONENT_FAMILY;
    }

    public int getMaxlength() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxlength, java.lang.Integer.MAX_VALUE);
    }

    public int getMinQueryLength() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minQueryLength, 3);
    }

    public int getQueryDelay() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.queryDelay, 700);
    }

    @Override
    public int getRows() {
        final int rows = super.getRows();

        return rows > 0 ? rows : 3;
    }

    public int getScrollHeight() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.scrollHeight, java.lang.Integer.MAX_VALUE);
    }

    public List getSuggestions() {
        return suggestions;
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(InputTextarea.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                final ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                } else if (!setAttributes.contains(name)) {
                    setAttributes.add(name);
                }
            }
        }
    }

    public boolean isAutoResize() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.autoResize, true);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("itemSelect")) {
                final String selectedItemValue = params.get(this.getClientId(context) + "_itemSelect");
                final SelectEvent selectEvent = new SelectEvent(this,
                                                                ajaxBehaviorEvent.getBehavior(),
                                                                selectedItemValue);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
            } else {
                // e.g. blur, focus, change
                super.queueEvent(event);
            }
        } else {
            // e.g. valueChange, autoCompleteEvent
            super.queueEvent(event);
        }
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAutoResize(final boolean _autoResize) {
        getStateHelper().put(PropertyKeys.autoResize, _autoResize);
        handleAttribute("autoResize", _autoResize);
    }

    public void setCompleteMethod(final javax.el.MethodExpression _completeMethod) {
        getStateHelper().put(PropertyKeys.completeMethod, _completeMethod);
        handleAttribute("completeMethod", _completeMethod);
    }

    public void setCounter(final java.lang.String _counter) {
        getStateHelper().put(PropertyKeys.counter, _counter);
        handleAttribute("counter", _counter);
    }

    public void setCounterTemplate(final java.lang.String _counterTemplate) {
        getStateHelper().put(PropertyKeys.counterTemplate, _counterTemplate);
        handleAttribute("counterTemplate", _counterTemplate);
    }

    public void setMaxlength(final int _maxlength) {
        getStateHelper().put(PropertyKeys.maxlength, _maxlength);
        handleAttribute("maxlength", _maxlength);
    }

    public void setMinQueryLength(final int _minQueryLength) {
        getStateHelper().put(PropertyKeys.minQueryLength, _minQueryLength);
        handleAttribute("minQueryLength", _minQueryLength);
    }

    public void setQueryDelay(final int _queryDelay) {
        getStateHelper().put(PropertyKeys.queryDelay, _queryDelay);
        handleAttribute("queryDelay", _queryDelay);
    }

    public void setScrollHeight(final int _scrollHeight) {
        getStateHelper().put(PropertyKeys.scrollHeight, _scrollHeight);
        handleAttribute("scrollHeight", _scrollHeight);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}