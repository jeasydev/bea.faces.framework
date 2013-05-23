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
package org.primefaces.component.menuitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ResourceDependencies({

})
public class MenuItem extends UICommand implements org.primefaces.component.api.AjaxSource, java.io.Serializable,
    org.primefaces.component.api.UIOutcomeTarget {

    protected enum PropertyKeys {

        url,
        target,
        style,
        styleClass,
        onclick,
        update,
        process,
        onstart,
        disabled,
        oncomplete,
        onerror,
        onsuccess,
        global,
        async,
        ajax,
        icon,
        partialSubmit,
        title,
        outcome,
        includeViewParams,
        fragment;

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

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String COMPONENT_TYPE = "org.primefaces.component.MenuItem";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String STYLE_CLASS = "wijmo-wijmenu-link ui-corner-all";

    public MenuItem() {
        setRendererType(null);
    }

    @Override
    public void decode(final FacesContext facesContext) {
        final Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(facesContext);

        if (params.containsKey(clientId)) {
            queueEvent(new ActionEvent(this));
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return MenuItem.COMPONENT_FAMILY;
    }

    @Override
    public java.lang.String getFragment() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.fragment, null);
    }

    @Override
    public String getHref() {
        return getUrl();
    }

    public java.lang.String getIcon() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.icon, null);
    }

    public java.lang.String getOnclick() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onclick, null);
    }

    @Override
    public java.lang.String getOncomplete() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.oncomplete, null);
    }

    @Override
    public java.lang.String getOnerror() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onerror, null);
    }

    @Override
    public java.lang.String getOnstart() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onstart, null);
    }

    @Override
    public java.lang.String getOnsuccess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.onsuccess, null);
    }

    @Override
    public java.lang.String getOutcome() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.outcome, null);
    }

    @Override
    public java.lang.String getProcess() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.process, null);
    }

    public java.lang.String getStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public java.lang.String getStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public java.lang.String getTarget() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.target, null);
    }

    public java.lang.String getTitle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.title, null);
    }

    @Override
    public java.lang.String getUpdate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.update, null);
    }

    public java.lang.String getUrl() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.url, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(MenuItem.OPTIMIZED_PACKAGE)) {
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

    public boolean isAjax() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.ajax, true);
    }

    @Override
    public boolean isAsync() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.async, false);
    }

    public boolean isDisabled() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
    }

    @Override
    public boolean isGlobal() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.global, true);
    }

    @Override
    public boolean isIncludeViewParams() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.includeViewParams, false);
    }

    @Override
    public boolean isPartialSubmit() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.partialSubmit, false);
    }

    @Override
    public boolean isPartialSubmitSet() {
        return (getStateHelper().get(PropertyKeys.partialSubmit) != null)
            || (getValueExpression("partialSubmit") != null);
    }

    public void setAjax(final boolean _ajax) {
        getStateHelper().put(PropertyKeys.ajax, _ajax);
        handleAttribute("ajax", _ajax);
    }

    public void setAsync(final boolean _async) {
        getStateHelper().put(PropertyKeys.async, _async);
        handleAttribute("async", _async);
    }

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setFragment(final java.lang.String _fragment) {
        getStateHelper().put(PropertyKeys.fragment, _fragment);
        handleAttribute("fragment", _fragment);
    }

    public void setGlobal(final boolean _global) {
        getStateHelper().put(PropertyKeys.global, _global);
        handleAttribute("global", _global);
    }

    public void setIcon(final java.lang.String _icon) {
        getStateHelper().put(PropertyKeys.icon, _icon);
        handleAttribute("icon", _icon);
    }

    public void setIncludeViewParams(final boolean _includeViewParams) {
        getStateHelper().put(PropertyKeys.includeViewParams, _includeViewParams);
        handleAttribute("includeViewParams", _includeViewParams);
    }

    public void setOnclick(final java.lang.String _onclick) {
        getStateHelper().put(PropertyKeys.onclick, _onclick);
        handleAttribute("onclick", _onclick);
    }

    public void setOncomplete(final java.lang.String _oncomplete) {
        getStateHelper().put(PropertyKeys.oncomplete, _oncomplete);
        handleAttribute("oncomplete", _oncomplete);
    }

    public void setOnerror(final java.lang.String _onerror) {
        getStateHelper().put(PropertyKeys.onerror, _onerror);
        handleAttribute("onerror", _onerror);
    }

    public void setOnstart(final java.lang.String _onstart) {
        getStateHelper().put(PropertyKeys.onstart, _onstart);
        handleAttribute("onstart", _onstart);
    }

    public void setOnsuccess(final java.lang.String _onsuccess) {
        getStateHelper().put(PropertyKeys.onsuccess, _onsuccess);
        handleAttribute("onsuccess", _onsuccess);
    }

    public void setOutcome(final java.lang.String _outcome) {
        getStateHelper().put(PropertyKeys.outcome, _outcome);
        handleAttribute("outcome", _outcome);
    }

    public void setPartialSubmit(final boolean _partialSubmit) {
        getStateHelper().put(PropertyKeys.partialSubmit, _partialSubmit);
        handleAttribute("partialSubmit", _partialSubmit);
    }

    public void setProcess(final java.lang.String _process) {
        getStateHelper().put(PropertyKeys.process, _process);
        handleAttribute("process", _process);
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setTarget(final java.lang.String _target) {
        getStateHelper().put(PropertyKeys.target, _target);
        handleAttribute("target", _target);
    }

    public void setTitle(final java.lang.String _title) {
        getStateHelper().put(PropertyKeys.title, _title);
        handleAttribute("title", _title);
    }

    public void setUpdate(final java.lang.String _update) {
        getStateHelper().put(PropertyKeys.update, _update);
        handleAttribute("update", _update);
    }

    public void setUrl(final java.lang.String _url) {
        getStateHelper().put(PropertyKeys.url, _url);
        handleAttribute("url", _url);
    }

    public boolean shouldRenderChildren() {
        if (getChildCount() == 0)
            return false;
        else {
            for (final UIComponent child : getChildren()) {
                if (!(child instanceof UIParameter)) {
                    return true;
                }
            }
        }

        return false;
    }
}