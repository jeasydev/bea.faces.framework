/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.imageareaselect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.Attachable;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.extensions.renderkit.widget.Option;
import org.primefaces.util.Constants;

/**
 * Component class for the <code>ImageAreaSelect</code> component.
 * 
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"), @ResourceDependency(library = "primefaces-extensions", name = "imageareaselect/imageareaselect.css"), @ResourceDependency(library = "primefaces-extensions", name = "imageareaselect/imageareaselect.js") })
public class ImageAreaSelect extends UIComponentBase implements Widget, ClientBehaviorHolder, Attachable {

    /**
     * Properties that are tracked by state saving.
     * 
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        widgetVar,
        forValue("for"),
        @Option
        aspectRatio,
        @Option
        autoHide,
        @Option
        fadeSpeed,
        @Option
        handles,
        @Option
        hide,
        @Option
        imageHeight,
        @Option
        imageWidth,
        @Option
        movable,
        @Option
        persistent,
        @Option
        resizable,
        @Option
        show,
        @Option
        zIndex,
        @Option
        maxHeight,
        @Option
        maxWidth,
        @Option
        minHeight,
        @Option
        minWidth,
        @Option(escapeText = true, useDoubleQuotes = true)
        parentSelector,
        @Option
        keyboardSupport;

        private String toString;

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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageAreaSelect";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageAreaSelectRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";
    public static final String EVENT_SELECT_END = "selectEnd";
    public static final String EVENT_SELECT_START = "selectStart";

    public static final String EVENT_SELECT_CHANGE = "selectChange";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList(ImageAreaSelect.EVENT_SELECT_END, ImageAreaSelect.EVENT_SELECT_START,
                ImageAreaSelect.EVENT_SELECT_CHANGE));

    public ImageAreaSelect() {
        setRendererType(ImageAreaSelect.DEFAULT_RENDERER);
    }

    public String getAspectRatio() {
        return (String) getStateHelper().eval(PropertyKeys.aspectRatio, null);
    }

    @Override
    public String getDefaultEventName() {
        return ImageAreaSelect.EVENT_SELECT_END;
    }

    @Override
    public Collection<String> getEventNames() {
        return ImageAreaSelect.EVENT_NAMES;
    }

    public Integer getFadeSpeed() {
        return (Integer) getStateHelper().eval(PropertyKeys.fadeSpeed, null);
    }

    @Override
    public String getFamily() {
        return ImageAreaSelect.COMPONENT_FAMILY;
    }

    @Override
    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public Integer getImageHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.imageHeight, null);
    }

    public Integer getImageWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.imageWidth, null);
    }

    public Integer getMaxHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxHeight, null);
    }

    public Integer getMaxWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxWidth, null);
    }

    public Integer getMinHeight() {
        return (Integer) getStateHelper().eval(PropertyKeys.minHeight, null);
    }

    public Integer getMinWidth() {
        return (Integer) getStateHelper().eval(PropertyKeys.minWidth, null);
    }

    public String getParentSelector() {
        return (String) getStateHelper().eval(PropertyKeys.parentSelector, null);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public Integer getZIndex() {
        return (Integer) getStateHelper().eval(PropertyKeys.zIndex, null);
    }

    public Boolean isAutoHide() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoHide, null);
    }

    public Boolean isHandles() {
        return (Boolean) getStateHelper().eval(PropertyKeys.handles, null);
    }

    public Boolean isHide() {
        return (Boolean) getStateHelper().eval(PropertyKeys.hide, null);
    }

    public Boolean isKeyboardSupport() {
        return (Boolean) getStateHelper().eval(PropertyKeys.keyboardSupport, null);
    }

    public Boolean isMovable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.movable, null);
    }

    public Boolean isPersistent() {
        return (Boolean) getStateHelper().eval(PropertyKeys.persistent, null);
    }

    private boolean isRequestSource(final String clientId, final Map<String, String> params) {
        return clientId.equals(params.get(Constants.PARTIAL_SOURCE_PARAM));
    }

    public Boolean isResizable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.resizable, null);
    }

    public Boolean isShow() {
        return (Boolean) getStateHelper().eval(PropertyKeys.show, null);
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(context);

        if (isRequestSource(clientId, params)) {
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

            if (eventName.equals(ImageAreaSelect.EVENT_SELECT_END)
                || eventName.equals(ImageAreaSelect.EVENT_SELECT_CHANGE)
                || eventName.equals(ImageAreaSelect.EVENT_SELECT_START)) {

                final BehaviorEvent behaviorEvent = (BehaviorEvent) event;

                final int x1 = Integer.parseInt(params.get(clientId + "_x1"));
                final int x2 = Integer.parseInt(params.get(clientId + "_x2"));
                final int y1 = Integer.parseInt(params.get(clientId + "_y1"));
                final int y2 = Integer.parseInt(params.get(clientId + "_y2"));
                final int height = Integer.parseInt(params.get(clientId + "_height"));
                final int width = Integer.parseInt(params.get(clientId + "_width"));
                final int imgHeight = Integer.parseInt(params.get(clientId + "_imgHeight"));
                final int imgWidth = Integer.parseInt(params.get(clientId + "_imgWidth"));
                final String imgSrc = params.get(clientId + "_imgSrc");

                final ImageAreaSelectEvent selectEvent = new ImageAreaSelectEvent(this,
                                                                                  behaviorEvent.getBehavior(),
                                                                                  height,
                                                                                  width,
                                                                                  x1,
                                                                                  x2,
                                                                                  y1,
                                                                                  y2,
                                                                                  imgHeight,
                                                                                  imgWidth,
                                                                                  imgSrc);

                super.queueEvent(selectEvent);
            }
        } else {
            super.queueEvent(event);
        }
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

        if (userWidgetVar != null) {
            return userWidgetVar;
        }

        return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAspectRatio(final String aspectRatio) {
        setAttribute(PropertyKeys.aspectRatio, aspectRatio);
    }

    @SuppressWarnings("unchecked")
    public void setAttribute(final PropertyKeys property, final Object value) {
        getStateHelper().put(property, value);

        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(ImageAreaSelect.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }

        if (setAttributes != null && value == null) {
            final String attributeName = property.toString();
            final ValueExpression ve = getValueExpression(attributeName);
            if (ve == null) {
                setAttributes.remove(attributeName);
            } else if (!setAttributes.contains(attributeName)) {
                setAttributes.add(attributeName);
            }
        }
    }

    public void setAutoHide(final Boolean autoHide) {
        setAttribute(PropertyKeys.autoHide, autoHide);
    }

    public void setFadeSpeed(final Integer fadeSpeed) {
        setAttribute(PropertyKeys.fadeSpeed, fadeSpeed);
    }

    @Override
    public void setFor(final String forValue) {
        setAttribute(PropertyKeys.forValue, forValue);
    }

    public void setHandles(final Boolean handles) {
        setAttribute(PropertyKeys.handles, handles);
    }

    public void setHide(final Boolean hide) {
        setAttribute(PropertyKeys.hide, hide);
    }

    public void setImageHeight(final Integer imageHeight) {
        setAttribute(PropertyKeys.imageHeight, imageHeight);
    }

    public void setImageWidth(final Integer imageWidth) {
        setAttribute(PropertyKeys.imageWidth, imageWidth);
    }

    public void setKeyboardSupport(final Boolean keyboardSupport) {
        setAttribute(PropertyKeys.keyboardSupport, keyboardSupport);
    }

    public void setMaxHeight(final Integer maxHeight) {
        setAttribute(PropertyKeys.maxHeight, maxHeight);
    }

    public void setMaxWidth(final Integer maxWidth) {
        setAttribute(PropertyKeys.maxWidth, maxWidth);
    }

    public void setMinHeight(final Integer minHeight) {
        setAttribute(PropertyKeys.minHeight, minHeight);
    }

    public void setMinWidth(final Integer minWidth) {
        setAttribute(PropertyKeys.minWidth, minWidth);
    }

    public void setMovable(final Boolean movable) {
        setAttribute(PropertyKeys.movable, movable);
    }

    public void setParentSelector(final String parentSelector) {
        setAttribute(PropertyKeys.parentSelector, parentSelector);
    }

    public void setPersistent(final Boolean persistent) {
        setAttribute(PropertyKeys.persistent, persistent);
    }

    public void setResizable(final Boolean resizable) {
        setAttribute(PropertyKeys.resizable, resizable);
    }

    public void setShow(final Boolean show) {
        setAttribute(PropertyKeys.show, show);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }

    public void setZIndex(final Integer zIndex) {
        setAttribute(PropertyKeys.zIndex, zIndex);
    }
}
