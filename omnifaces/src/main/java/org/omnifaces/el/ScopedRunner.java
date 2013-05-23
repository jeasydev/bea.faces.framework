/*
 * Copyright 2012 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.el;

import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.omnifaces.util.Callback;

/**
 * This class helps in letting code run within its own scope. Such scope is
 * defined by specific variables being available to EL within it. The request
 * scope is used to store the variables.
 * 
 * @author Arjan Tijms
 * 
 */
public class ScopedRunner {

    FacesContext context;
    Map<String, Object> scopedVariables;
    Map<String, Object> previousVariables = new HashMap<String, Object>();

    public ScopedRunner(final FacesContext context) {
        this(context, new HashMap<String, Object>());
    }

    public ScopedRunner(final FacesContext context, final Map<String, Object> scopedVariables) {
        this.context = context;
        this.scopedVariables = scopedVariables;
    }

    /**
     * Invokes the callback within the scope of the variables being given in the
     * constructor.
     * 
     * @param callback
     */
    public void invoke(final Callback.Void callback) {
        try {
            setNewScope();
            callback.invoke();
        } finally {
            restorePreviousScope();
        }
    }

    private void restorePreviousScope() {
        try {
            final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            for (final Map.Entry<String, Object> entry : scopedVariables.entrySet()) {
                final Object previousVariable = previousVariables.get(entry.getKey());
                if (previousVariable != null) {
                    requestMap.put(entry.getKey(), previousVariable);
                } else {
                    requestMap.remove(entry.getKey());
                }
            }
        } finally {
            previousVariables.clear();
        }
    }

    private void setNewScope() {
        previousVariables.clear();

        final Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        for (final Map.Entry<String, Object> entry : scopedVariables.entrySet()) {
            final Object previousVariable = requestMap.put(entry.getKey(), entry.getValue());
            if (previousVariable != null) {
                previousVariables.put(entry.getKey(), previousVariable);
            }
        }
    }

    /**
     * Adds the given variable to this instance. Can be used in a
     * builder-pattern.
     * 
     * @param key the key name of the variable
     * @param value the value of the variable
     * @return this ScopedRunner, so adding variables and finally calling invoke
     *         can be chained.
     */
    public ScopedRunner with(final String key, final Object value) {
        scopedVariables.put(key, value);
        return this;
    }

}
