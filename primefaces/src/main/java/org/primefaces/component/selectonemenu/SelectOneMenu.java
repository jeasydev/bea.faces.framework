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
package org.primefaces.component.selectonemenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.component.column.Column;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.MessageFactory;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class SelectOneMenu extends HtmlSelectOneMenu implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.InputHolder {

    protected enum PropertyKeys {

        widgetVar,
        effect,
        effectSpeed,
        panelStyle,
        panelStyleClass,
        var,
        height,
        editable,
        filter,
        filterMatchMode,
        filterFunction,
        caseSensitive,
        maxlength;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.SelectOneMenu";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.SelectOneMenuRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String STYLE_CLASS = "ui-selectonemenu ui-widget ui-state-default ui-corner-all ui-helper-clearfix";

    public final static String LABEL_CLASS = "ui-selectonemenu-label ui-inputfield ui-corner-all";

    public final static String TRIGGER_CLASS = "ui-selectonemenu-trigger ui-state-default ui-corner-right";
    public final static String PANEL_CLASS = "ui-selectonemenu-panel ui-widget-content ui-corner-all ui-helper-hidden ui-shadow";

    public final static String ITEMS_WRAPPER_CLASS = "ui-selectonemenu-items-wrapper";
    public final static String LIST_CLASS = "ui-selectonemenu-items ui-selectonemenu-list ui-widget-content ui-widget ui-corner-all ui-helper-reset";

    public final static String TABLE_CLASS = "ui-selectonemenu-items ui-selectonemenu-table ui-widget-content ui-widget ui-corner-all ui-helper-reset";
    public final static String ITEM_GROUP_CLASS = "ui-selectonemenu-item-group ui-corner-all";

    public final static String ITEM_CLASS = "ui-selectonemenu-item ui-selectonemenu-list-item ui-corner-all";
    public final static String ROW_CLASS = "ui-selectonemenu-item ui-selectonemenu-row ui-widget-content";

    public final static String FILTER_CONTAINER_CLASS = "ui-selectonemenu-filter-container";
    public final static String FILTER_CLASS = "ui-selectonemenu-filter ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all";

    public final static String FILTER_ICON_CLASS = "ui-icon ui-icon-search";

    public SelectOneMenu() {
        setRendererType(SelectOneMenu.DEFAULT_RENDERER);
    }

    public List<Column> getColums() {
        final List<Column> columns = new ArrayList<Column>();

        for (final UIComponent kid : getChildren()) {
            if (kid instanceof Column) columns.add((Column) kid);
        }

        return columns;
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, null);
    }

    public java.lang.String getEffectSpeed() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effectSpeed, null);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return SelectOneMenu.COMPONENT_FAMILY;
    }

    public java.lang.String getFilterFunction() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterFunction, null);
    }

    public java.lang.String getFilterMatchMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterMatchMode, null);
    }

    public int getHeight() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.height, java.lang.Integer.MAX_VALUE);
    }

    @Override
    public String getInputClientId() {
        return this.getClientId(getFacesContext()) + "_input";
    }

    public int getMaxlength() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxlength, Integer.MAX_VALUE);
    }

    public java.lang.String getPanelStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.panelStyle, null);
    }

    public java.lang.String getPanelStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.panelStyleClass, null);
    }

    public java.lang.String getVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.var, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(SelectOneMenu.OPTIMIZED_PACKAGE)) {
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

    public boolean isCaseSensitive() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.caseSensitive, false);
    }

    public boolean isEditable() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.editable, false);
    }

    public boolean isFilter() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.filter, false);
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

    public void setCaseSensitive(final boolean _caseSensitive) {
        getStateHelper().put(PropertyKeys.caseSensitive, _caseSensitive);
        handleAttribute("caseSensitive", _caseSensitive);
    }

    public void setEditable(final boolean _editable) {
        getStateHelper().put(PropertyKeys.editable, _editable);
        handleAttribute("editable", _editable);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectSpeed(final java.lang.String _effectSpeed) {
        getStateHelper().put(PropertyKeys.effectSpeed, _effectSpeed);
        handleAttribute("effectSpeed", _effectSpeed);
    }

    public void setFilter(final boolean _filter) {
        getStateHelper().put(PropertyKeys.filter, _filter);
        handleAttribute("filter", _filter);
    }

    public void setFilterFunction(final java.lang.String _filterFunction) {
        getStateHelper().put(PropertyKeys.filterFunction, _filterFunction);
        handleAttribute("filterFunction", _filterFunction);
    }

    public void setFilterMatchMode(final java.lang.String _filterMatchMode) {
        getStateHelper().put(PropertyKeys.filterMatchMode, _filterMatchMode);
        handleAttribute("filterMatchMode", _filterMatchMode);
    }

    public void setHeight(final int _height) {
        getStateHelper().put(PropertyKeys.height, _height);
        handleAttribute("height", _height);
    }

    public void setMaxlength(final int _maxlength) {
        getStateHelper().put(PropertyKeys.maxlength, _maxlength);
        handleAttribute("maxlength", _maxlength);
    }

    public void setPanelStyle(final java.lang.String _panelStyle) {
        getStateHelper().put(PropertyKeys.panelStyle, _panelStyle);
        handleAttribute("panelStyle", _panelStyle);
    }

    public void setPanelStyleClass(final java.lang.String _panelStyleClass) {
        getStateHelper().put(PropertyKeys.panelStyleClass, _panelStyleClass);
        handleAttribute("panelStyleClass", _panelStyleClass);
    }

    public void setVar(final java.lang.String _var) {
        getStateHelper().put(PropertyKeys.var, _var);
        handleAttribute("var", _var);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }

    @Override
    protected void validateValue(final FacesContext context, final Object value) {
        if (isEditable()) {

            // required field validation
            if (isValid() && isRequired() && UIInput.isEmpty(value)) {
                final String requiredMessageStr = getRequiredMessage();
                FacesMessage message;
                if (null != requiredMessageStr) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessageStr, requiredMessageStr);
                } else {
                    message = MessageFactory.getMessage(UIInput.REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR,
                                                        new Object[] { MessageFactory.getLabel(context, this) });
                }
                context.addMessage(getClientId(context), message);
                setValid(false);
            }

            // other validators
            if (isValid() && (!UIInput.isEmpty(value) || ComponentUtils.validateEmptyFields(context))) {
                final Validator[] validators = getValidators();

                for (final Validator validator : validators) {
                    try {
                        validator.validate(context, this, value);
                    } catch (final ValidatorException ve) {
                        setValid(false);
                        FacesMessage message;
                        final String validatorMessageString = getValidatorMessage();

                        if (null != validatorMessageString) {
                            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       validatorMessageString,
                                                       validatorMessageString);
                        } else {
                            final Collection<FacesMessage> messages = ve.getFacesMessages();

                            if (null != messages) {
                                message = null;
                                final String cid = getClientId(context);
                                for (final FacesMessage m : messages) {
                                    context.addMessage(cid, m);
                                }
                            } else {
                                message = ve.getFacesMessage();
                            }
                        }

                        if (message != null) {
                            context.addMessage(getClientId(context), message);
                        }
                    }
                }
            }
        } else {
            super.validateValue(context, value);
        }
    }
}