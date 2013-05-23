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
package org.primefaces.component.picklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.util.Constants;
import org.primefaces.util.MessageFactory;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class PickList extends UIInput implements org.primefaces.component.api.Widget,
    javax.faces.component.behavior.ClientBehaviorHolder {

    protected enum PropertyKeys {

        widgetVar,
        var,
        itemLabel,
        itemValue,
        style,
        styleClass,
        disabled,
        effect,
        effectSpeed,
        addLabel,
        addAllLabel,
        removeLabel,
        removeAllLabel,
        moveUpLabel,
        moveTopLabel,
        moveDownLabel,
        moveBottomLabel,
        showSourceControls,
        showTargetControls,
        onTransfer,
        label,
        itemDisabled,
        showSourceFilter,
        showTargetFilter,
        filterMatchMode,
        filterFunction,
        showCheckbox;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.PickList";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.PickListRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public static final String CONTAINER_CLASS = "ui-picklist ui-widget";

    public static final String LIST_CLASS = "ui-widget-content ui-picklist-list";

    public static final String SOURCE_CLASS = PickList.LIST_CLASS + " ui-picklist-source";
    public static final String TARGET_CLASS = PickList.LIST_CLASS + " ui-picklist-target";

    public static final String SOURCE_CONTROLS = "ui-picklist-source-controls";
    public static final String TARGET_CONTROLS = "ui-picklist-target-controls";

    public static final String ITEM_CLASS = "ui-picklist-item ui-corner-all";
    public static final String ITEM_DISABLED_CLASS = "ui-state-disabled";

    public static final String CAPTION_CLASS = "ui-picklist-caption ui-widget-header ui-corner-tl ui-corner-tr";
    public static final String ADD_BUTTON_CLASS = "ui-picklist-button-add";

    public static final String ADD_ALL_BUTTON_CLASS = "ui-picklist-button-add-all";
    public static final String REMOVE_BUTTON_CLASS = "ui-picklist-button-remove";

    public static final String REMOVE_ALL_BUTTON_CLASS = "ui-picklist-button-remove-all";
    public static final String ADD_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-e";

    public static final String ADD_ALL_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-e";
    public static final String REMOVE_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-w";

    public static final String REMOVE_ALL_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-w";
    public static final String MOVE_UP_BUTTON_CLASS = "ui-picklist-button-move-up";

    public static final String MOVE_DOWN_BUTTON_CLASS = "ui-picklist-button-move-down";
    public static final String MOVE_TOP_BUTTON_CLASS = "ui-picklist-button-move-top";

    public static final String MOVE_BOTTOM_BUTTON_CLASS = "ui-picklist-button-move-bottom";
    public static final String MOVE_UP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-n";

    public static final String MOVE_DOWN_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrow-1-s";
    public static final String MOVE_TOP_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-n";

    public static final String MOVE_BOTTOM_BUTTON_ICON_CLASS = "ui-icon ui-icon-arrowstop-1-s";
    public static final String FILTER_CLASS = "ui-picklist-filter ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all";

    public static final String FILTER_CONTAINER = "ui-picklist-filter-container";
    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("transfer"));

    public PickList() {
        setRendererType(PickList.DEFAULT_RENDERER);
    }

    public java.lang.String getAddAllLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.addAllLabel, "Add All");
    }

    public java.lang.String getAddLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.addLabel, "Add");
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, "fade");
    }

    public java.lang.String getEffectSpeed() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effectSpeed, "fast");
    }

    @Override
    public Collection<String> getEventNames() {
        return PickList.EVENT_NAMES;
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return PickList.COMPONENT_FAMILY;
    }

    public java.lang.String getFilterFunction() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterFunction, null);
    }

    public java.lang.String getFilterMatchMode() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.filterMatchMode, null);
    }

    public java.lang.String getItemLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemLabel, null);
    }

    public java.lang.Object getItemValue() {
        return getStateHelper().eval(PropertyKeys.itemValue, null);
    }

    public java.lang.String getLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.label, null);
    }

    public java.lang.String getMoveBottomLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveBottomLabel, "Move Bottom");
    }

    public java.lang.String getMoveDownLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveDownLabel, "Move Down");
    }

    public java.lang.String getMoveTopLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveTopLabel, "Move Top");
    }

    public java.lang.String getMoveUpLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.moveUpLabel, "Move Up");
    }

    public java.lang.String getOnTransfer() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onTransfer, null);
    }

    public java.lang.String getRemoveAllLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.removeAllLabel, "Remove All");
    }

    public java.lang.String getRemoveLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.removeLabel, "Remove");
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
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
            if (cname != null && cname.startsWith(PickList.OPTIMIZED_PACKAGE)) {
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

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    public boolean isItemDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.itemDisabled, false);
    }

    private boolean isRequestSource(final FacesContext context) {
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap()
                                                    .get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public boolean isShowCheckbox() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showCheckbox, false);
    }

    public boolean isShowSourceControls() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showSourceControls, false);
    }

    public boolean isShowSourceFilter() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showSourceFilter, false);
    }

    public boolean isShowTargetControls() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showTargetControls, false);
    }

    public boolean isShowTargetFilter() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.showTargetFilter, false);
    }

    @SuppressWarnings("unchecked")
    public void populateModel(final FacesContext context, final String[] values, final List model) {
        final Converter converter = getConverter();

        for (final String item : values) {
            if (item == null || item.trim().equals("")) continue;

            final Object convertedValue = converter != null ? converter.getAsObject(context, this, item) : item;

            if (convertedValue != null) {
                model.add(convertedValue);
            }
        }
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = getFacesContext();

        if (isRequestSource(context) && event instanceof AjaxBehaviorEvent) {
            final Map<String, String[]> paramValues = context.getExternalContext().getRequestParameterValuesMap();
            final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
            final String clientId = this.getClientId(context);

            final AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if (eventName.equals("transfer")) {
                final String[] items = paramValues.get(clientId + "_transferred");
                final boolean isAdd = Boolean.valueOf(params.get(clientId + "_add"));
                final List transferredItems = new ArrayList();
                populateModel(context, items, transferredItems);
                final TransferEvent transferEvent = new TransferEvent(this,
                                                                      behaviorEvent.getBehavior(),
                                                                      transferredItems,
                                                                      isAdd);
                transferEvent.setPhaseId(transferEvent.getPhaseId());

                super.queueEvent(transferEvent);
            }
        } else {
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

    public void setAddAllLabel(final java.lang.String _addAllLabel) {
        getStateHelper().put(PropertyKeys.addAllLabel, _addAllLabel);
        handleAttribute("addAllLabel", _addAllLabel);
    }

    public void setAddLabel(final java.lang.String _addLabel) {
        getStateHelper().put(PropertyKeys.addLabel, _addLabel);
        handleAttribute("addLabel", _addLabel);
    }

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectSpeed(final java.lang.String _effectSpeed) {
        getStateHelper().put(PropertyKeys.effectSpeed, _effectSpeed);
        handleAttribute("effectSpeed", _effectSpeed);
    }

    public void setFilterFunction(final java.lang.String _filterFunction) {
        getStateHelper().put(PropertyKeys.filterFunction, _filterFunction);
        handleAttribute("filterFunction", _filterFunction);
    }

    public void setFilterMatchMode(final java.lang.String _filterMatchMode) {
        getStateHelper().put(PropertyKeys.filterMatchMode, _filterMatchMode);
        handleAttribute("filterMatchMode", _filterMatchMode);
    }

    public void setItemDisabled(final boolean _itemDisabled) {
        getStateHelper().put(PropertyKeys.itemDisabled, _itemDisabled);
        handleAttribute("itemDisabled", _itemDisabled);
    }

    public void setItemLabel(final java.lang.String _itemLabel) {
        getStateHelper().put(PropertyKeys.itemLabel, _itemLabel);
        handleAttribute("itemLabel", _itemLabel);
    }

    public void setItemValue(final java.lang.Object _itemValue) {
        getStateHelper().put(PropertyKeys.itemValue, _itemValue);
        handleAttribute("itemValue", _itemValue);
    }

    public void setLabel(final java.lang.String _label) {
        getStateHelper().put(PropertyKeys.label, _label);
        handleAttribute("label", _label);
    }

    public void setMoveBottomLabel(final java.lang.String _moveBottomLabel) {
        getStateHelper().put(PropertyKeys.moveBottomLabel, _moveBottomLabel);
        handleAttribute("moveBottomLabel", _moveBottomLabel);
    }

    public void setMoveDownLabel(final java.lang.String _moveDownLabel) {
        getStateHelper().put(PropertyKeys.moveDownLabel, _moveDownLabel);
        handleAttribute("moveDownLabel", _moveDownLabel);
    }

    public void setMoveTopLabel(final java.lang.String _moveTopLabel) {
        getStateHelper().put(PropertyKeys.moveTopLabel, _moveTopLabel);
        handleAttribute("moveTopLabel", _moveTopLabel);
    }

    public void setMoveUpLabel(final java.lang.String _moveUpLabel) {
        getStateHelper().put(PropertyKeys.moveUpLabel, _moveUpLabel);
        handleAttribute("moveUpLabel", _moveUpLabel);
    }

    public void setOnTransfer(final java.lang.String _onTransfer) {
        getStateHelper().put(PropertyKeys.onTransfer, _onTransfer);
        handleAttribute("onTransfer", _onTransfer);
    }

    public void setRemoveAllLabel(final java.lang.String _removeAllLabel) {
        getStateHelper().put(PropertyKeys.removeAllLabel, _removeAllLabel);
        handleAttribute("removeAllLabel", _removeAllLabel);
    }

    public void setRemoveLabel(final java.lang.String _removeLabel) {
        getStateHelper().put(PropertyKeys.removeLabel, _removeLabel);
        handleAttribute("removeLabel", _removeLabel);
    }

    public void setShowCheckbox(final boolean _showCheckbox) {
        getStateHelper().put(PropertyKeys.showCheckbox, _showCheckbox);
        handleAttribute("showCheckbox", _showCheckbox);
    }

    public void setShowSourceControls(final boolean _showSourceControls) {
        getStateHelper().put(PropertyKeys.showSourceControls, _showSourceControls);
        handleAttribute("showSourceControls", _showSourceControls);
    }

    public void setShowSourceFilter(final boolean _showSourceFilter) {
        getStateHelper().put(PropertyKeys.showSourceFilter, _showSourceFilter);
        handleAttribute("showSourceFilter", _showSourceFilter);
    }

    public void setShowTargetControls(final boolean _showTargetControls) {
        getStateHelper().put(PropertyKeys.showTargetControls, _showTargetControls);
        handleAttribute("showTargetControls", _showTargetControls);
    }

    public void setShowTargetFilter(final boolean _showTargetFilter) {
        getStateHelper().put(PropertyKeys.showTargetFilter, _showTargetFilter);
        handleAttribute("showTargetFilter", _showTargetFilter);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
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
    protected void validateValue(final FacesContext facesContext, final Object newValue) {
        super.validateValue(facesContext, newValue);

        final DualListModel model = (DualListModel) newValue;
        if (isRequired() && model.getTarget().isEmpty()) {
            final String requiredMessage = getRequiredMessage();
            FacesMessage message = null;

            if (requiredMessage != null) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, requiredMessage, requiredMessage);
            } else {
                String label = getLabel();
                if (label == null) {
                    label = this.getClientId(facesContext);
                }

                message = MessageFactory.getMessage(UIInput.REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR,
                                                    new Object[] { label });

            }

            facesContext.addMessage(getClientId(facesContext), message);
            setValid(false);
        }
    }
}