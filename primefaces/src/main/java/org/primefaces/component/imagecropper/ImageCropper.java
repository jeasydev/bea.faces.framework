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
package org.primefaces.component.imagecropper;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "imagecropper/imagecropper.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces", name = "imagecropper/imagecropper.js") })
public class ImageCropper extends UIInput implements org.primefaces.component.api.Widget {

    protected enum PropertyKeys {

        widgetVar,
        image,
        alt,
        aspectRatio,
        minSize,
        maxSize,
        backgroundColor,
        backgroundOpacity,
        initialCoords;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.ImageCropper";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.ImageCropperRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public ImageCropper() {
        setRendererType(ImageCropper.DEFAULT_RENDERER);
    }

    public java.lang.String getAlt() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.alt, null);
    }

    public double getAspectRatio() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.aspectRatio, java.lang.Double.MIN_VALUE);
    }

    public java.lang.String getBackgroundColor() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.backgroundColor, null);
    }

    public double getBackgroundOpacity() {
        return (java.lang.Double) getStateHelper().eval(PropertyKeys.backgroundOpacity, 0.6);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return ImageCropper.COMPONENT_FAMILY;
    }

    public java.lang.String getImage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.image, null);
    }

    public java.lang.String getInitialCoords() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.initialCoords, null);
    }

    public java.lang.String getMaxSize() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.maxSize, null);
    }

    public java.lang.String getMinSize() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.minSize, null);
    }

    public java.lang.String getWidgetVar() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(ImageCropper.OPTIMIZED_PACKAGE)) {
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

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get("widgetVar");

        if (userWidgetVar != null)
            return userWidgetVar;
        else return "widget_"
            + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAlt(final java.lang.String _alt) {
        getStateHelper().put(PropertyKeys.alt, _alt);
        handleAttribute("alt", _alt);
    }

    public void setAspectRatio(final double _aspectRatio) {
        getStateHelper().put(PropertyKeys.aspectRatio, _aspectRatio);
        handleAttribute("aspectRatio", _aspectRatio);
    }

    public void setBackgroundColor(final java.lang.String _backgroundColor) {
        getStateHelper().put(PropertyKeys.backgroundColor, _backgroundColor);
        handleAttribute("backgroundColor", _backgroundColor);
    }

    public void setBackgroundOpacity(final double _backgroundOpacity) {
        getStateHelper().put(PropertyKeys.backgroundOpacity, _backgroundOpacity);
        handleAttribute("backgroundOpacity", _backgroundOpacity);
    }

    public void setImage(final java.lang.String _image) {
        getStateHelper().put(PropertyKeys.image, _image);
        handleAttribute("image", _image);
    }

    public void setInitialCoords(final java.lang.String _initialCoords) {
        getStateHelper().put(PropertyKeys.initialCoords, _initialCoords);
        handleAttribute("initialCoords", _initialCoords);
    }

    public void setMaxSize(final java.lang.String _maxSize) {
        getStateHelper().put(PropertyKeys.maxSize, _maxSize);
        handleAttribute("maxSize", _maxSize);
    }

    public void setMinSize(final java.lang.String _minSize) {
        getStateHelper().put(PropertyKeys.minSize, _minSize);
        handleAttribute("minSize", _minSize);
    }

    public void setWidgetVar(final java.lang.String _widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
        handleAttribute("widgetVar", _widgetVar);
    }
}