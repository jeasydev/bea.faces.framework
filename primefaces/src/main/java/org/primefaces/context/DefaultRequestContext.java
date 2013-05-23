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
package org.primefaces.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.context.FacesContext;
import org.primefaces.util.WidgetBuilder;
import org.primefaces.visit.ResetInputVisitCallback;

public class DefaultRequestContext extends RequestContext {

    private final static String CALLBACK_PARAMS_KEY = "CALLBACK_PARAMS";
    private final static String EXECUTE_SCRIPT_KEY = "EXECUTE_SCRIPT";
    private final Map<String, Object> attributes;
    private final WidgetBuilder widgetBuilder;

    public DefaultRequestContext() {
        attributes = new HashMap<String, Object>();
        widgetBuilder = new WidgetBuilder();
    }

    @Override
    public void addCallbackParam(final String name, final Object value) {
        getCallbackParams().put(name, value);
    }

    @Override
    public void execute(final String script) {
        getScriptsToExecute().add(script);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCallbackParams() {
        if (attributes.get(DefaultRequestContext.CALLBACK_PARAMS_KEY) == null) {
            attributes.put(DefaultRequestContext.CALLBACK_PARAMS_KEY, new HashMap<String, Object>());
        }
        return (Map<String, Object>) attributes.get(DefaultRequestContext.CALLBACK_PARAMS_KEY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getScriptsToExecute() {
        if (attributes.get(DefaultRequestContext.EXECUTE_SCRIPT_KEY) == null) {
            attributes.put(DefaultRequestContext.EXECUTE_SCRIPT_KEY, new ArrayList());
        }
        return (List<String>) attributes.get(DefaultRequestContext.EXECUTE_SCRIPT_KEY);
    }

    @Override
    public WidgetBuilder getWidgetBuilder() {
        return widgetBuilder;
    }

    @Override
    public boolean isAjaxRequest() {
        return FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
    }

    @Override
    public void reset(final Collection<String> ids) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED);
        final VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);
        final VisitCallback visitCallback = new ResetInputVisitCallback();
        final UIViewRoot root = context.getViewRoot();

        for (final String id : ids) {
            final UIComponent targetComponent = root.findComponent(id);
            if (targetComponent == null) {
                throw new FacesException("Cannot find component with identifier \"" + id
                    + "\" referenced from viewroot.");
            }

            targetComponent.visitTree(visitContext, visitCallback);
        }
    }

    @Override
    public void reset(final String id) {
        final Collection<String> list = new ArrayList<String>();
        list.add(id);

        reset(list);
    }

    @Override
    public void scrollTo(final String clientId) {
        execute("PrimeFaces.scrollTo('" + clientId + "');");
    }

    @Override
    public void update(final Collection<String> collection) {
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().addAll(collection);
    }

    @Override
    public void update(final String clientId) {
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add(clientId);
    }
}