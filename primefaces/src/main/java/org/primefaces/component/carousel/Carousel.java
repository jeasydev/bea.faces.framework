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
package org.primefaces.component.carousel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.UIData;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class Carousel extends UIData implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        firstVisible,
        numVisible,
        circular,
        vertical,
        autoPlayInterval,
        pageLinks,
        effect,
        easing,
        effectDuration,
        dropdownTemplate,
        style,
        styleClass,
        itemStyle,
        itemStyleClass,
        headerText,
        footerText;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.Carousel";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.CarouselRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String CONTAINER_CLASS = "ui-carousel ui-widget ui-widget-content ui-corner-all";

    public final static String ITEM_CLASS = "ui-carousel-item ui-widget-content ui-corner-all";

    public final static String HEADER_CLASS = "ui-carousel-header ui-widget-header ui-corner-all";
    public final static String HEADER_TITLE_CLASS = "ui-carousel-header-title";

    public final static String FOOTER_CLASS = "ui-carousel-footer ui-widget-header ui-corner-all";
    public final static String HORIZONTAL_NEXT_BUTTON = "ui-carousel-button ui-carousel-next-button ui-icon ui-icon-circle-triangle-e";

    public final static String HORIZONTAL_PREV_BUTTON = "ui-carousel-button ui-carousel-prev-button ui-icon ui-icon-circle-triangle-w";
    public final static String VERTICAL_NEXT_BUTTON = "ui-carousel-button ui-carousel-next-button ui-icon ui-icon-circle-triangle-s";

    public final static String VERTICAL_PREV_BUTTON = "ui-carousel-button ui-carousel-prev-button ui-icon ui-icon-circle-triangle-n";
    public final static String VIEWPORT_CLASS = "ui-carousel-viewport";

    public final static String VERTICAL_VIEWPORT_CLASS = "ui-carousel-viewport ui-carousel-vertical-viewport";
    public final static String PAGE_LINKS_CONTAINER_CLASS = "ui-carousel-page-links";

    public final static String PAGE_LINK_CLASS = "ui-icon ui-carousel-page-link ui-icon-radio-off";
    public final static String DROPDOWN_CLASS = "ui-carousel-dropdown ui-widget ui-state-default ui-corner-left";

    private final static Logger logger = Logger.getLogger(Carousel.class.getName());

    public Carousel() {
        setRendererType(Carousel.DEFAULT_RENDERER);
    }

    public int getAutoPlayInterval() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.autoPlayInterval, 0);
    }

    public java.lang.String getDropdownTemplate() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.dropdownTemplate, "{page}");
    }

    public java.lang.String getEasing() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.easing, null);
    }

    public java.lang.String getEffect() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.effect, null);
    }

    public int getEffectDuration() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.effectDuration, java.lang.Integer.MIN_VALUE);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Carousel.COMPONENT_FAMILY;
    }

    public int getFirstVisible() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.firstVisible, 0);
    }

    public java.lang.String getFooterText() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.footerText, null);
    }

    public java.lang.String getHeaderText() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.headerText, null);
    }

    public java.lang.String getItemStyle() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemStyle, null);
    }

    public java.lang.String getItemStyleClass() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.itemStyleClass, null);
    }

    public int getNumVisible() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.numVisible, 3);
    }

    @Override
    public int getPageLinks() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.pageLinks, 3);
    }

    public int getRenderedChildCount() {
        int i = 0;

        for (final UIComponent child : getChildren()) {
            if (child.isRendered()) {
                i++;
            }
        }

        return i;
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
            if (cname != null && cname.startsWith(Carousel.OPTIMIZED_PACKAGE)) {
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

    public boolean isCircular() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.circular, false);
    }

    public boolean isVertical() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.vertical, false);
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

    public void setAutoPlayInterval(final int _autoPlayInterval) {
        getStateHelper().put(PropertyKeys.autoPlayInterval, _autoPlayInterval);
        handleAttribute("autoPlayInterval", _autoPlayInterval);
    }

    public void setCircular(final boolean _circular) {
        getStateHelper().put(PropertyKeys.circular, _circular);
        handleAttribute("circular", _circular);
    }

    public void setDropdownTemplate(final java.lang.String _dropdownTemplate) {
        getStateHelper().put(PropertyKeys.dropdownTemplate, _dropdownTemplate);
        handleAttribute("dropdownTemplate", _dropdownTemplate);
    }

    public void setEasing(final java.lang.String _easing) {
        getStateHelper().put(PropertyKeys.easing, _easing);
        handleAttribute("easing", _easing);
    }

    public void setEffect(final java.lang.String _effect) {
        getStateHelper().put(PropertyKeys.effect, _effect);
        handleAttribute("effect", _effect);
    }

    public void setEffectDuration(final int _effectDuration) {
        getStateHelper().put(PropertyKeys.effectDuration, _effectDuration);
        handleAttribute("effectDuration", _effectDuration);
    }

    public void setFirstVisible(final int _firstVisible) {
        getStateHelper().put(PropertyKeys.firstVisible, _firstVisible);
        handleAttribute("firstVisible", _firstVisible);
    }

    public void setFooterText(final java.lang.String _footerText) {
        getStateHelper().put(PropertyKeys.footerText, _footerText);
        handleAttribute("footerText", _footerText);
    }

    public void setHeaderText(final java.lang.String _headerText) {
        getStateHelper().put(PropertyKeys.headerText, _headerText);
        handleAttribute("headerText", _headerText);
    }

    public void setItemStyle(final java.lang.String _itemStyle) {
        getStateHelper().put(PropertyKeys.itemStyle, _itemStyle);
        handleAttribute("itemStyle", _itemStyle);
    }

    public void setItemStyleClass(final java.lang.String _itemStyleClass) {
        getStateHelper().put(PropertyKeys.itemStyleClass, _itemStyleClass);
        handleAttribute("itemStyleClass", _itemStyleClass);
    }

    public void setNumVisible(final int _numVisible) {
        getStateHelper().put(PropertyKeys.numVisible, _numVisible);
        handleAttribute("numVisible", _numVisible);
    }

    @Override
    public void setPageLinks(final int _pageLinks) {
        getStateHelper().put(PropertyKeys.pageLinks, _pageLinks);
        handleAttribute("pageLinks", _pageLinks);
    }

    @Override
    public void setRows(final int rows) {
        super.setRows(rows);
        setNumVisible(rows);

        Carousel.logger.log(Level.WARNING, "rows is deprecated, use numVisible instead.");
    }

    public void setStyle(final java.lang.String _style) {
        getStateHelper().put(PropertyKeys.style, _style);
        handleAttribute("style", _style);
    }

    public void setStyleClass(final java.lang.String _styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, _styleClass);
        handleAttribute("styleClass", _styleClass);
    }

    public void setVertical(final boolean _vertical) {
        getStateHelper().put(PropertyKeys.vertical, _vertical);
        handleAttribute("vertical", _vertical);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}