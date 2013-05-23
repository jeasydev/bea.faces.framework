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
package org.primefaces.component.wizard;

import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.FlowEvent;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Wizard extends UIComponentBase implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        step,
        style,
        styleClass,
        flowListener,
        showNavBar,
        showStepStatus,
        onback,
        onnext,
        nextLabel,
        backLabel;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Wizard";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.WizardRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String STEP_STATUS_CLASS = "ui-wizard-step-titles ui-helper-reset ui-helper-clearfix";

    public final static String STEP_CLASS = "ui-wizard-step-title ui-state-default ui-corner-all";

    public final static String ACTIVE_STEP_CLASS = "ui-wizard-step-title ui-state-default ui-state-highlight ui-corner-all";
    public final static String BACK_BUTTON_CLASS = "ui-wizard-nav-back";

    public final static String NEXT_BUTTON_CLASS = "ui-wizard-nav-next";
    private Tab current;

    public Wizard() {
        setRendererType(Wizard.DEFAULT_RENDERER);
    }

    @Override
    public void broadcast(final FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event instanceof FlowEvent) {
            final FlowEvent flowEvent = (FlowEvent) event;
            final FacesContext context = getFacesContext();
            context.getExternalContext().getRequestParameterMap();
            this.getClientId(context);
            final MethodExpression me = getFlowListener();

            if (me != null) {
                final String step = (String) me.invoke(context.getELContext(), new Object[] { event });

                setStep(step);
            } else {
                setStep(flowEvent.getNewStep());
            }
        }
    }

    public java.lang.String getBackLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.backLabel, "Back");
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Wizard.COMPONENT_FAMILY;
    }

    public javax.el.MethodExpression getFlowListener() {
        return (javax.el.MethodExpression) getStateHelper().eval(PropertyKeys.flowListener, null);
    }

    public java.lang.String getNextLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.nextLabel, "Next");
    }

    public java.lang.String getOnback() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onback, null);
    }

    public java.lang.String getOnnext() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onnext, null);
    }

    public java.lang.String getStep() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.step, null);
    }

    public Tab getStepToProcess() {
        if (current == null) {
            final String currentStepId = getStep();

            for (final UIComponent child : getChildren()) {
                if (child.getId().equals(currentStepId)) {
                    current = (Tab) child;

                    break;
                }
            }
        }

        return current;
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Wizard.OPTIMIZED_PACKAGE)) {
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

    public boolean isBackRequest(final FacesContext context) {
        return isWizardRequest(context)
            && context.getExternalContext().getRequestParameterMap().containsKey(getClientId(context) + "_backRequest");
    }

    public boolean isShowNavBar() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showNavBar, true);
    }

    public boolean isShowStepStatus() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showStepStatus, true);
    }

    public boolean isWizardRequest(final FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(getClientId(context)
                                                                                     + "_wizardRequest");
    }

    @Override
    public void processDecodes(final FacesContext context) {
        decode(context);

        if (!isBackRequest(context)) {
            getStepToProcess().processDecodes(context);
        }
    }

    @Override
    public void processUpdates(final FacesContext context) {
        if (!isBackRequest(context)) {
            current.processUpdates(context);
        }
    }

    @Override
    public void processValidators(final FacesContext context) {
        if (!isBackRequest(context)) {
            current.processValidators(context);
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

    public void setBackLabel(final java.lang.String _backLabel) {
        getStateHelper().put(PropertyKeys.backLabel, _backLabel);
        handleAttribute("backLabel", _backLabel);
    }

    public void setFlowListener(final javax.el.MethodExpression _flowListener) {
        getStateHelper().put(PropertyKeys.flowListener, _flowListener);
        handleAttribute("flowListener", _flowListener);
    }

    public void setNextLabel(final java.lang.String _nextLabel) {
        getStateHelper().put(PropertyKeys.nextLabel, _nextLabel);
        handleAttribute("nextLabel", _nextLabel);
    }

    public void setOnback(final java.lang.String _onback) {
        getStateHelper().put(PropertyKeys.onback, _onback);
        handleAttribute("onback", _onback);
    }

    public void setOnnext(final java.lang.String _onnext) {
        getStateHelper().put(PropertyKeys.onnext, _onnext);
        handleAttribute("onnext", _onnext);
    }

    public void setShowNavBar(final boolean _showNavBar) {
        getStateHelper().put(PropertyKeys.showNavBar, _showNavBar);
        handleAttribute("showNavBar", _showNavBar);
    }

    public void setShowStepStatus(final boolean _showStepStatus) {
        getStateHelper().put(PropertyKeys.showStepStatus, _showStepStatus);
        handleAttribute("showStepStatus", _showStepStatus);
    }

    public void setStep(final java.lang.String _step) {
        getStateHelper().put(PropertyKeys.step, _step);
        handleAttribute("step", _step);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}