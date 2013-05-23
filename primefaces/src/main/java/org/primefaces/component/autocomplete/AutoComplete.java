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
package org.primefaces.component.autocomplete;

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
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.column.Column;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.util.Constants;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class AutoComplete extends HtmlInputText implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.InputHolder {

    protected enum PropertyKeys {

        widgetVar,
        completeMethod,
        var,
        itemLabel,
        itemValue,
        maxResults,
        minQueryLength,
        queryDelay,
        forceSelection,
        onstart,
        oncomplete,
        global,
        scrollHeight,
        effect,
        effectDuration,
        dropdown,
        panelStyle,
        panelStyleClass,
        multiple,
        process,
        itemtipMyPosition,
        itemtipAtPosition;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.AutoComplete";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.AutoCompleteRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList("blur", "change", "valueChange", "click", "dblclick", "focus", "keydown", "keypress", "keyup",
                "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "select", "itemSelect", "itemUnselect"));

    public final static String STYLE_CLASS = "ui-autocomplete";

    public final static String MULTIPLE_STYLE_CLASS = "ui-autocomplete-multiple";
    public final static String INPUT_CLASS = "ui-autocomplete-input ui-inputfield ui-widget ui-state-default ui-corner-all";

    public final static String INPUT_WITH_DROPDOWN_CLASS = "ui-autocomplete-input ui-inputfield ui-widget ui-state-default ui-corner-left";
    public final static String DROPDOWN_ICON_CLASS = "ui-autocomplete-dropdown ui-state-default ui-corner-right";

    public final static String PANEL_CLASS = "ui-autocomplete-panel ui-widget-content ui-corner-all ui-helper-hidden ui-shadow";
    public final static String LIST_CLASS = "ui-autocomplete-items ui-autocomplete-list ui-widget-content ui-widget ui-corner-all ui-helper-reset";

    public final static String TABLE_CLASS = "ui-autocomplete-items ui-autocomplete-table ui-widget-content ui-widget ui-corner-all ui-helper-reset";
    public final static String ITEM_CLASS = "ui-autocomplete-item ui-autocomplete-list-item ui-corner-all";

    public final static String ROW_CLASS = "ui-autocomplete-item ui-autocomplete-row ui-corner-all";
    public final static String TOKEN_DISPLAY_CLASS = "ui-autocomplete-token ui-state-active ui-corner-all";

    public final static String TOKEN_LABEL_CLASS = "ui-autocomplete-token-label";
    public final static String TOKEN_ICON_CLASS = "ui-autocomplete-token-icon ui-icon ui-icon-close";

    public final static String TOKEN_INPUT_CLASS = "ui-autocomplete-input-token";
    public final static String MULTIPLE_CONTAINER_CLASS = "ui-autocomplete-multiple-container ui-widget ui-inputfield ui-state-default ui-corner-all";

    public final static String ITEMTIP_CONTENT_CLASS = "ui-autocomplete-itemtip-content";
    private List suggestions = null;

    public AutoComplete() {
        setRendererType(AutoComplete.DEFAULT_RENDERER);
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

    private Object convertValue(final FacesContext context, final String submittedItemValue) {
        final Converter converter = getConverter();

        if (converter == null)
            return submittedItemValue;
        else return converter.getAsObject(context, this, submittedItemValue);
    }

    public List<Column> getColums() {
        final List<Column> columns = new ArrayList<Column>();

        for (final UIComponent kid : getChildren()) {
            if (kid instanceof Column) columns.add((Column) kid);
        }

        return columns;
    }

    public javax.el.MethodExpression getCompleteMethod() {
        return (javax.el.MethodExpression) getStateHelper().eval(PropertyKeys.completeMethod, null);
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, null);
    }

    public int getEffectDuration() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.effectDuration, 400);
    }

    @Override
    public Collection<String> getEventNames() {
        return AutoComplete.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return AutoComplete.COMPONENT_FAMILY;
    }

    @Override
    public String getInputClientId() {
        return this.getClientId(getFacesContext()) + "_input";
    }

    public java.lang.String getItemLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemLabel, null);
    }

    public java.lang.String getItemtipAtPosition() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemtipAtPosition, null);
    }

    public java.lang.String getItemtipMyPosition() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemtipMyPosition, null);
    }

    public java.lang.Object getItemValue() {
        return getStateHelper().eval(PropertyKeys.itemValue, null);
    }

    public int getMaxResults() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.maxResults, java.lang.Integer.MAX_VALUE);
    }

    public int getMinQueryLength() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.minQueryLength, 1);
    }

    public java.lang.String getOncomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    public java.lang.String getOnstart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    public java.lang.String getPanelStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.panelStyle, null);
    }

    public java.lang.String getPanelStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.panelStyleClass, null);
    }

    public java.lang.String getProcess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
    }

    public int getQueryDelay() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.queryDelay, 300);
    }

    public int getScrollHeight() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.scrollHeight, java.lang.Integer.MAX_VALUE);
    }

    public List getSuggestions() {
        return suggestions;
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
            if (cname != null && cname.startsWith(AutoComplete.OPTIMIZED_PACKAGE)) {
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

    public boolean isDropdown() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.dropdown, false);
    }

    public boolean isForceSelection() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.forceSelection, false);
    }

    public boolean isGlobal() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    public boolean isMultiple() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.multiple, false);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

        if (eventName != null && event instanceof AjaxBehaviorEvent) {
            final AjaxBehaviorEvent ajaxBehaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("itemSelect")) {
                final Object selectedItemValue = convertValue(context, params.get(this.getClientId(context)
                    + "_itemSelect"));
                final SelectEvent selectEvent = new SelectEvent(this,
                                                                ajaxBehaviorEvent.getBehavior(),
                                                                selectedItemValue);
                selectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(selectEvent);
            } else if (eventName.equals("itemUnselect")) {
                final Object unselectedItemValue = convertValue(context, params.get(this.getClientId(context)
                    + "_itemUnselect"));
                final UnselectEvent unselectEvent = new UnselectEvent(this,
                                                                      ajaxBehaviorEvent.getBehavior(),
                                                                      unselectedItemValue);
                unselectEvent.setPhaseId(ajaxBehaviorEvent.getPhaseId());
                super.queueEvent(unselectEvent);
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

    public void setCompleteMethod(final javax.el.MethodExpression _completeMethod) {
        getStateHelper().put(PropertyKeys.completeMethod, _completeMethod);
        handleAttribute("completeMethod", _completeMethod);
    }

    public void setDropdown(final boolean _dropdown) {
        getStateHelper().put(PropertyKeys.dropdown, _dropdown);
        handleAttribute("dropdown", _dropdown);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectDuration(final int _effectDuration) {
        getStateHelper().put(PropertyKeys.effectDuration, _effectDuration);
        handleAttribute("effectDuration", _effectDuration);
    }

    public void setForceSelection(final boolean _forceSelection) {
        getStateHelper().put(PropertyKeys.forceSelection, _forceSelection);
        handleAttribute("forceSelection", _forceSelection);
    }

    public void setGlobal(final boolean _global) {
        getStateHelper().put(PropertyKeys.global, _global);
        handleAttribute("global", _global);
    }

    public void setItemLabel(final java.lang.String _itemLabel) {
        getStateHelper().put(PropertyKeys.itemLabel, _itemLabel);
        handleAttribute("itemLabel", _itemLabel);
    }

    public void setItemtipAtPosition(final java.lang.String _itemtipAtPosition) {
        getStateHelper().put(PropertyKeys.itemtipAtPosition, _itemtipAtPosition);
        handleAttribute("itemtipAtPosition", _itemtipAtPosition);
    }

    public void setItemtipMyPosition(final java.lang.String _itemtipMyPosition) {
        getStateHelper().put(PropertyKeys.itemtipMyPosition, _itemtipMyPosition);
        handleAttribute("itemtipMyPosition", _itemtipMyPosition);
    }

    public void setItemValue(final java.lang.Object _itemValue) {
        getStateHelper().put(PropertyKeys.itemValue, _itemValue);
        handleAttribute("itemValue", _itemValue);
    }

    public void setMaxResults(final int _maxResults) {
        getStateHelper().put(PropertyKeys.maxResults, _maxResults);
        handleAttribute("maxResults", _maxResults);
    }

    public void setMinQueryLength(final int _minQueryLength) {
        getStateHelper().put(PropertyKeys.minQueryLength, _minQueryLength);
        handleAttribute("minQueryLength", _minQueryLength);
    }

    public void setMultiple(final boolean _multiple) {
        getStateHelper().put(PropertyKeys.multiple, _multiple);
        handleAttribute("multiple", _multiple);
    }

    public void setOncomplete(final java.lang.String _oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
        handleAttribute("oncomplete", _oncomplete);
    }

    public void setOnstart(final java.lang.String _onstart) {
        getStateHelper().put(PropertyKeys.onstart, _onstart);
        handleAttribute("onstart", _onstart);
    }

    public void setPanelStyle(final java.lang.String _panelStyle) {
        getStateHelper().put(PropertyKeys.panelStyle, _panelStyle);
        handleAttribute("panelStyle", _panelStyle);
    }

    public void setPanelStyleClass(final java.lang.String _panelStyleClass) {
        getStateHelper().put(PropertyKeys.panelStyleClass, _panelStyleClass);
        handleAttribute("panelStyleClass", _panelStyleClass);
    }

    public void setProcess(final java.lang.String _process) {
        getStateHelper().put(PropertyKeys.process, _process);
        handleAttribute("process", _process);
    }

    public void setQueryDelay(final int _queryDelay) {
        getStateHelper().put(PropertyKeys.queryDelay, _queryDelay);
        handleAttribute("queryDelay", _queryDelay);
    }

    public void setScrollHeight(final int _scrollHeight) {
        getStateHelper().put(PropertyKeys.scrollHeight, _scrollHeight);
        handleAttribute("scrollHeight", _scrollHeight);
    }

    public void setVar(final java.lang.String _var) {
        getStateHelper().put(PropertyKeys.var, _var);
        handleAttribute("var", _var);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}