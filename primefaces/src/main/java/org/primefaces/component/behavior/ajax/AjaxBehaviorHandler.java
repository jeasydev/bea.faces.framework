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
package org.primefaces.component.behavior.ajax;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectHandler;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

public class AjaxBehaviorHandler extends TagHandler implements BehaviorHolderAttachedObjectHandler {

    private final TagAttribute event;
    private final TagAttribute process;
    private final TagAttribute update;
    private final TagAttribute onstart;
    private final TagAttribute onerror;
    private final TagAttribute onsuccess;
    private final TagAttribute oncomplete;
    private final TagAttribute disabled;
    private final TagAttribute immediate;
    private final TagAttribute listener;
    private final TagAttribute global;
    private final TagAttribute async;
    private final TagAttribute partialSubmit;

    public AjaxBehaviorHandler(final BehaviorConfig config) {
        super(config);
        event = getAttribute("event");
        process = getAttribute("process");
        update = getAttribute("update");
        onstart = getAttribute("onstart");
        onerror = getAttribute("onerror");
        onsuccess = getAttribute("onsuccess");
        oncomplete = getAttribute("oncomplete");
        disabled = getAttribute("disabled");
        immediate = getAttribute("immediate");
        listener = getAttribute("listener");
        global = getAttribute("global");
        async = getAttribute("async");
        partialSubmit = getAttribute("partialSubmit");
    }

    @Override
    public void apply(final FaceletContext ctx, final UIComponent parent) throws IOException {
        if (!ComponentHandler.isNew(parent)) {
            return;
        }

        final String eventName = getEventName();

        if (UIComponent.isCompositeComponent(parent)) {
            boolean tagApplied = false;
            if (parent instanceof ClientBehaviorHolder) {
                applyAttachedObject(ctx, parent, eventName);
                tagApplied = true;
            }

            final BeanInfo componentBeanInfo = (BeanInfo) parent.getAttributes().get(UIComponent.BEANINFO_KEY);
            if (null == componentBeanInfo) {
                throw new TagException(tag, "Composite component does not have BeanInfo attribute");
            }

            final BeanDescriptor componentDescriptor = componentBeanInfo.getBeanDescriptor();
            if (null == componentDescriptor) {
                throw new TagException(tag, "Composite component BeanInfo does not have BeanDescriptor");
            }

            final List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>) componentDescriptor
                .getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
            if (null == targetList && !tagApplied) {
                throw new TagException(tag, "Composite component does not support behavior events");
            }

            boolean supportedEvent = false;
            for (final AttachedObjectTarget target : targetList) {
                if (target instanceof BehaviorHolderAttachedObjectTarget) {
                    final BehaviorHolderAttachedObjectTarget behaviorTarget = (BehaviorHolderAttachedObjectTarget) target;
                    if ((null != eventName && eventName.equals(behaviorTarget.getName()))
                        || (null == eventName && behaviorTarget.isDefaultEvent())) {
                        supportedEvent = true;
                        break;
                    }
                }
            }

            if (supportedEvent) {
                getAttachedObjectHandlers(parent).add(this);
            } else {
                if (!tagApplied) {
                    throw new TagException(tag, "Composite component does not support event " + eventName);
                }
            }
        } else if (parent instanceof ClientBehaviorHolder) {
            applyAttachedObject(ctx, parent, eventName);
        } else {
            throw new TagException(tag, "Unable to attach <p:ajax> to non-ClientBehaviorHolder parent");
        }
    }

    public void applyAttachedObject(final FaceletContext context, final UIComponent component, String eventName) {
        final ClientBehaviorHolder holder = (ClientBehaviorHolder) component;

        if (null == eventName) {
            eventName = holder.getDefaultEventName();
            if (null == eventName) {
                throw new TagException(tag, "Event attribute could not be determined: " + eventName);
            }
        } else {
            final Collection<String> eventNames = holder.getEventNames();
            if (!eventNames.contains(eventName)) {
                throw new TagException(tag, "Event:" + eventName + " is not supported.");
            }
        }

        final AjaxBehavior ajaxBehavior = createAjaxBehavior(context, eventName);
        holder.addClientBehavior(eventName, ajaxBehavior);
    }

    @Override
    public void applyAttachedObject(final FacesContext context, final UIComponent parent) {
        final FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);

        applyAttachedObject(ctx, parent, getEventName());
    }

    // Construct our AjaxBehavior from tag parameters.
    private AjaxBehavior createAjaxBehavior(final FaceletContext ctx, final String eventName) {
        final Application application = ctx.getFacesContext().getApplication();
        final AjaxBehavior behavior = (AjaxBehavior) application.createBehavior(AjaxBehavior.BEHAVIOR_ID);

        setBehaviorAttribute(ctx, behavior, process, String.class);
        setBehaviorAttribute(ctx, behavior, update, String.class);
        setBehaviorAttribute(ctx, behavior, onstart, String.class);
        setBehaviorAttribute(ctx, behavior, onerror, String.class);
        setBehaviorAttribute(ctx, behavior, onsuccess, String.class);
        setBehaviorAttribute(ctx, behavior, oncomplete, String.class);
        setBehaviorAttribute(ctx, behavior, disabled, Boolean.class);
        setBehaviorAttribute(ctx, behavior, immediate, Boolean.class);
        setBehaviorAttribute(ctx, behavior, global, Boolean.class);
        setBehaviorAttribute(ctx, behavior, async, Boolean.class);
        setBehaviorAttribute(ctx, behavior, partialSubmit, Boolean.class);
        setBehaviorAttribute(ctx, behavior, listener, MethodExpression.class);

        if (listener != null) {
            behavior
                .addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(listener.getMethodExpression(ctx, Object.class,
                                                                                                   new Class[] {}),
                                                                      listener
                                                                          .getMethodExpression(ctx,
                                                                                               Object.class,
                                                                                               new Class[] { AjaxBehaviorEvent.class })));
        }

        return behavior;
    }

    public List<AttachedObjectHandler> getAttachedObjectHandlers(final UIComponent component) {
        return getAttachedObjectHandlers(component, true);
    }

    public List<AttachedObjectHandler> getAttachedObjectHandlers(final UIComponent component, final boolean create) {
        final Map<String, Object> attrs = component.getAttributes();
        List<AttachedObjectHandler> result = (List<AttachedObjectHandler>) attrs
            .get("javax.faces.RetargetableHandlers");

        if (result == null) {
            if (create) {
                result = new ArrayList<AttachedObjectHandler>();
                attrs.put("javax.faces.RetargetableHandlers", result);
            } else {
                result = Collections.EMPTY_LIST;
            }
        }
        return result;
    }

    @Override
    public String getEventName() {
        return (event != null) ? event.getValue() : null;
    }

    @Override
    public String getFor() {
        return null;
    }

    private void setBehaviorAttribute(final FaceletContext ctx,
                                      final AjaxBehavior behavior,
                                      final TagAttribute attr,
                                      final Class type) {
        if (attr != null) {
            behavior.setValueExpression(attr.getLocalName(), attr.getValueExpression(ctx, type));
        }
    }
}