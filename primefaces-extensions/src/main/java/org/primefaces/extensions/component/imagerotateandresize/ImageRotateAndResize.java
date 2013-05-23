/*
 * Copyright 2011 PrimeFaces Extensions.
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

package org.primefaces.extensions.component.imagerotateandresize;

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
import org.primefaces.extensions.event.ResizeEvent;
import org.primefaces.extensions.event.RotateEvent;
import org.primefaces.util.Constants;

/**
 * Component class for the <code>ImageRotateAndResize</code> component.
 * 
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.1
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js") })
public class ImageRotateAndResize extends UIComponentBase implements Widget, ClientBehaviorHolder, Attachable {

    /**
     * Properties that are tracked by state saving.
     * 
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        widgetVar,
        forValue("for");

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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.ImageRotateAndResize";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.ImageRotateAndResizeRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";
    public static final String EVENT_ROTATE = "rotate";

    public static final String EVENT_RESIZE = "resize";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList(ImageRotateAndResize.EVENT_ROTATE, ImageRotateAndResize.EVENT_RESIZE));

    public ImageRotateAndResize() {
        setRendererType(ImageRotateAndResize.DEFAULT_RENDERER);
    }

    @Override
    public Collection<String> getEventNames() {
        return ImageRotateAndResize.EVENT_NAMES;
    }

    @Override
    public String getFamily() {
        return ImageRotateAndResize.COMPONENT_FAMILY;
    }

    @Override
    public String getFor() {
        return (String) getStateHelper().eval(PropertyKeys.forValue, null);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    private boolean isRequestSource(final String clientId, final Map<String, String> params) {
        return clientId.equals(params.get(Constants.PARTIAL_SOURCE_PARAM));
    }

    @Override
    public void queueEvent(final FacesEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String clientId = getClientId(context);

        if (isRequestSource(clientId, params)) {
            final String eventName = params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);

            final BehaviorEvent behaviorEvent = (BehaviorEvent) event;

            if (eventName.equals(ImageRotateAndResize.EVENT_RESIZE)) {
                final double width = Double.parseDouble(params.get(clientId + "_width"));
                final double height = Double.parseDouble(params.get(clientId + "_height"));

                final ResizeEvent resizeEvent = new ResizeEvent(this, behaviorEvent.getBehavior(), width, height);

                super.queueEvent(resizeEvent);
            } else if (eventName.equals(ImageRotateAndResize.EVENT_ROTATE)) {
                final int degree = Integer.parseInt(params.get(clientId + "_degree"));

                final RotateEvent rotateEvent = new RotateEvent(this, behaviorEvent.getBehavior(), degree);

                super.queueEvent(rotateEvent);
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

    @SuppressWarnings("unchecked")
    public void setAttribute(final PropertyKeys property, final Object value) {
        getStateHelper().put(property, value);

        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(ImageRotateAndResize.OPTIMIZED_PACKAGE)) {
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

    @Override
    public void setFor(final String forValue) {
        setAttribute(PropertyKeys.forValue, forValue);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }
}
