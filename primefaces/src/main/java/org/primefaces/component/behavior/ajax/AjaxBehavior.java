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

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.AjaxSource;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class AjaxBehavior extends ClientBehaviorBase implements AjaxSource {

    private String update;
    private String process;
    private Boolean global;
    private Boolean async;
    private String oncomplete;
    private String onerror;
    private String onsuccess;
    private String onstart;
    private MethodExpression listener;
    private Boolean immediate;
    private Boolean disabled;
    private Boolean partialSubmit;
    private boolean partialSubmitSet = false;

    public final static String BEHAVIOR_ID = "org.primefaces.component.AjaxBehavior";

    private static final Set<ClientBehaviorHint> HINTS = Collections.unmodifiableSet(EnumSet
        .of(ClientBehaviorHint.SUBMITTING));

    private Map<String, ValueExpression> bindings;

    public void addAjaxBehaviorListener(final AjaxBehaviorListenerImpl listener) {
        addBehaviorListener(listener);
    }

    protected Object eval(final String propertyName, final Object value) {
        if (value != null) {
            return value;
        }

        final ValueExpression expression = getValueExpression(propertyName);
        if (expression != null) {
            return expression.getValue(FacesContext.getCurrentInstance().getELContext());
        }

        return null;
    }

    @Override
    public Set<ClientBehaviorHint> getHints() {
        return AjaxBehavior.HINTS;
    }

    public MethodExpression getListener() {
        return listener;
    }

    @Override
    public String getOncomplete() {
        return (String) eval("oncomplete", oncomplete);
    }

    @Override
    public String getOnerror() {
        return (String) eval("onerror", onerror);
    }

    @Override
    public String getOnstart() {
        return (String) eval("onstart", onstart);
    }

    @Override
    public String getOnsuccess() {
        return (String) eval("onsuccess", onsuccess);
    }

    @Override
    public String getProcess() {
        return (String) eval("process", process);
    }

    @Override
    public String getRendererType() {
        return "org.primefaces.component.AjaxBehaviorRenderer";
    }

    @Override
    public String getUpdate() {
        return (String) eval("update", update);
    }

    public ValueExpression getValueExpression(final String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }

        return ((bindings == null) ? null : bindings.get(name));
    }

    @Override
    public boolean isAsync() {
        final Boolean result = (Boolean) eval("async", async);

        return ((result != null) ? result : false);
    }

    public boolean isDisabled() {
        final Boolean result = (Boolean) eval("disabled", disabled);

        return ((result != null) ? result : false);
    }

    @Override
    public boolean isGlobal() {
        final Boolean result = (Boolean) eval("global", global);

        return ((result != null) ? result : true);
    }

    public boolean isImmediate() {
        final Boolean result = (Boolean) eval("immediate", immediate);

        return ((result != null) ? result : false);
    }

    public boolean isImmediateSet() {
        return ((immediate != null) || (getValueExpression("immediate") != null));
    }

    @Override
    public boolean isPartialSubmit() {
        final Boolean result = (Boolean) eval("partialSubmit", partialSubmit);

        return ((result != null) ? result : false);
    }

    @Override
    public boolean isPartialSubmitSet() {
        return partialSubmitSet || (getValueExpression("partialSubmit") != null);
    }

    public void removeAjaxBehaviorListener(final AjaxBehaviorListenerImpl listener) {
        removeBehaviorListener(listener);
    }

    private Map<String, ValueExpression> restoreBindings(final FacesContext context, final Object state) {
        if (state == null) {
            return null;
        }

        final Object values[] = (Object[]) state;
        final String names[] = (String[]) values[0];
        final Object states[] = (Object[]) values[1];
        final Map<String, ValueExpression> bindings = new HashMap<String, ValueExpression>(names.length);

        for (int i = 0; i < names.length; i++) {
            bindings.put(names[i], (ValueExpression) UIComponentBase.restoreAttachedState(context, states[i]));
        }
        return bindings;
    }

    @Override
    public void restoreState(final FacesContext context, final Object state) {
        if (state != null) {
            final Object[] values = (Object[]) state;
            super.restoreState(context, values[0]);

            if (values.length != 1) {
                onstart = (String) values[1];
                onerror = (String) values[2];
                onsuccess = (String) values[3];
                oncomplete = (String) values[4];
                disabled = (Boolean) values[5];
                immediate = (Boolean) values[6];
                process = (String) values[7];
                update = (String) values[8];
                async = (Boolean) values[9];
                global = (Boolean) values[10];
                partialSubmit = (Boolean) values[11];
                listener = (MethodExpression) values[12];
                bindings = restoreBindings(context, values[13]);

                clearInitialState();
            }
        }
    }

    private Object saveBindings(final FacesContext context, final Map<String, ValueExpression> bindings) {
        if (bindings == null) {
            return null;
        }

        final Object values[] = new Object[2];
        values[0] = bindings.keySet().toArray(new String[bindings.size()]);

        final Object[] bindingValues = bindings.values().toArray();
        for (int i = 0; i < bindingValues.length; i++) {
            bindingValues[i] = UIComponentBase.saveAttachedState(context, bindingValues[i]);
        }

        values[1] = bindingValues;

        return values;
    }

    @Override
    public Object saveState(final FacesContext context) {
        Object[] values;

        final Object superState = super.saveState(context);

        if (initialStateMarked()) {
            if (superState == null)
                values = null;
            else values = new Object[] { superState };
        } else {
            values = new Object[14];

            values[0] = superState;
            values[1] = onstart;
            values[2] = onerror;
            values[3] = onsuccess;
            values[4] = oncomplete;
            values[5] = disabled;
            values[6] = immediate;
            values[7] = process;
            values[8] = update;
            values[9] = async;
            values[10] = global;
            values[11] = partialSubmit;
            values[12] = listener;
            values[13] = saveBindings(context, bindings);
        }

        return values;
    }

    public void setAsync(final boolean async) {
        this.async = async;

        clearInitialState();
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;

        clearInitialState();
    }

    public void setGlobal(final boolean global) {
        this.global = global;

        clearInitialState();
    }

    public void setImmediate(final Boolean immediate) {
        this.immediate = immediate;

        clearInitialState();
    }

    public void setListener(final MethodExpression listener) {
        this.listener = listener;
    }

    private void setLiteralValue(final String propertyName, final ValueExpression expression) {
        Object value;
        final ELContext context = FacesContext.getCurrentInstance().getELContext();

        try {
            value = expression.getValue(context);
        } catch (final ELException eLException) {
            throw new FacesException(eLException);
        }

        if ("update".equals(propertyName)) {
            update = (String) value;
        } else if ("process".equals(propertyName)) {
            process = (String) value;
        } else if ("oncomplete".equals(propertyName)) {
            oncomplete = (String) value;
        } else if ("onerror".equals(propertyName)) {
            onerror = (String) value;
        } else if ("onsuccess".equals(propertyName)) {
            onsuccess = (String) value;
        } else if ("onstart".equals(propertyName)) {
            onstart = (String) value;
        } else if ("immediate".equals(propertyName)) {
            immediate = (Boolean) value;
        } else if ("disabled".equals(propertyName)) {
            disabled = (Boolean) value;
        } else if ("async".equals(propertyName)) {
            async = (Boolean) value;
        } else if ("global".equals(propertyName)) {
            global = (Boolean) value;
        } else if ("partialSubmit".equals(propertyName)) {
            partialSubmit = (Boolean) value;
            partialSubmitSet = true;
        }
    }

    public void setOncomplete(final String oncomplete) {
        this.oncomplete = oncomplete;

        clearInitialState();
    }

    public void setOnerror(final String onerror) {
        this.onerror = onerror;

        clearInitialState();
    }

    public void setOnstart(final String onstart) {
        this.onstart = onstart;

        clearInitialState();
    }

    public void setOnsuccess(final String onsuccess) {
        this.onsuccess = onsuccess;

        clearInitialState();
    }

    public void setPartialSubmit(final boolean partialSubmit) {
        this.partialSubmit = partialSubmit;
        partialSubmitSet = true;

        clearInitialState();
    }

    public void setProcess(final String process) {
        this.process = process;

        clearInitialState();
    }

    public void setUpdate(final String update) {
        this.update = update;

        clearInitialState();
    }

    public void setValueExpression(final String name, final ValueExpression expr) {
        if (name == null) {
            throw new IllegalArgumentException();
        }

        if (expr != null) {
            if (expr.isLiteralText()) {
                setLiteralValue(name, expr);
            } else {
                if (bindings == null) {
                    bindings = new HashMap<String, ValueExpression>(6, 1.0f);
                }

                bindings.put(name, expr);
            }
        } else {
            if (bindings != null) {
                bindings.remove(name);
                if (bindings.isEmpty()) {
                    bindings = null;
                }
            }
        }

        clearInitialState();
    }
}