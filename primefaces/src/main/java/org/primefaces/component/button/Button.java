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
package org.primefaces.component.button;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlOutcomeTargetButton;
import javax.faces.context.FacesContext;
import org.primefaces.util.HTML;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Button extends HtmlOutcomeTargetButton implements org.primefaces.component.api.Widget,
    org.primefaces.component.api.UIOutcomeTarget {

    protected enum PropertyKeys {

        widgetVar,
        fragment,
        disabled,
        icon,
        iconPos,
        href,
        escape;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Button";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.ButtonRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    private final static Logger logger = Logger.getLogger(Button.class.getName());

    public Button() {
        setRendererType(Button.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Button.COMPONENT_FAMILY;
    }

    @Override
    public java.lang.String getFragment() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.fragment, null);
    }

    @Override
    public java.lang.String getHref() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.href, null);
    }

    public java.lang.String getIcon() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.icon, null);
    }

    public java.lang.String getIconPos() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.iconPos, "left");
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Button.OPTIMIZED_PACKAGE)) {
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

    public boolean isEscape() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.escape, true);
    }

    public String resolveIcon() {
        String icon = getIcon();

        if (icon == null) {
            icon = getImage();

            if (icon != null)
                Button.logger.info("image attribute is deprecated to define an icon, use icon attribute instead.");
        }

        return icon;
    }

    public String resolveStyleClass() {
        final String icon = resolveIcon();
        final Object value = getValue();
        String styleClass = "";

        if (value != null && icon == null) {
            styleClass = HTML.BUTTON_TEXT_ONLY_BUTTON_CLASS;
        } else if (value != null && icon != null) {
            styleClass = getIconPos().equals("left")
                                                    ? HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS
                                                    : HTML.BUTTON_TEXT_ICON_RIGHT_BUTTON_CLASS;
        } else if (value == null && icon != null) {
            styleClass = HTML.BUTTON_ICON_ONLY_BUTTON_CLASS;
        }

        if (isDisabled()) {
            styleClass = styleClass + " ui-state-disabled";
        }

        final String userStyleClass = getStyleClass();
        if (userStyleClass != null) {
            styleClass = styleClass + " " + userStyleClass;
        }

        return styleClass;
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

    public void setDisabled(final boolean _disabled) {
        getStateHelper().put(PropertyKeys.disabled, _disabled);
        handleAttribute("disabled", _disabled);
    }

    public void setEscape(final boolean _escape) {
        getStateHelper().put(PropertyKeys.escape, _escape);
        handleAttribute("escape", _escape);
    }

    public void setFragment(final java.lang.String _fragment) {
        getStateHelper().put(PropertyKeys.fragment, _fragment);
        handleAttribute("fragment", _fragment);
    }

    public void setHref(final java.lang.String _href) {
        getStateHelper().put(PropertyKeys.href, _href);
        handleAttribute("href", _href);
    }

    public void setIcon(final java.lang.String _icon) {
        getStateHelper().put(PropertyKeys.icon, _icon);
        handleAttribute("icon", _icon);
    }

    public void setIconPos(final java.lang.String _iconPos) {
        getStateHelper().put(PropertyKeys.iconPos, _iconPos);
        handleAttribute("iconPos", _iconPos);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}