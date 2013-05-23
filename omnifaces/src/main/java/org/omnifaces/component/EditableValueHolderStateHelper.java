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
package org.omnifaces.component;

import java.util.Iterator;
import java.util.Map;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * Helper class to save and restore state of an {@link EditableValueHolder}.
 * 
 * @author Bauke Scholtz.
 */
public final class EditableValueHolderStateHelper {

    // UIData and UIRepeat have a similar nested class. Too bad that they aren't
    // publicly reusable.

    // Variables
    // ------------------------------------------------------------------------------------------------------

    /**
     * Returns the state helper of the {@link EditableValueHolder} child
     * associated with the given client ID.
     * 
     * @param stateHelper The state helper of the parent component.
     * @param clientId The client ID of the {@link EditableValueHolder} child to
     *            return the state helper for.
     * @return The state helper of the {@link EditableValueHolder} child
     *         associated with the given client ID.
     */
    @SuppressWarnings("unchecked")
    public static EditableValueHolderStateHelper get(final StateHelper stateHelper, final String clientId) {
        final Map<String, EditableValueHolderStateHelper> childState = (Map<String, EditableValueHolderStateHelper>) stateHelper
            .get(EditableValueHolderStateHelper.class);
        EditableValueHolderStateHelper state = null;

        if (childState != null) {
            state = childState.get(clientId);
        }

        if (state == null) {
            state = new EditableValueHolderStateHelper();
            stateHelper.put(EditableValueHolderStateHelper.class, clientId, state);
        }

        return state;
    }

    /**
     * Restore state of any {@link EditableValueHolder} children.
     * 
     * @param context The faces context to work with.
     * @param stateHelper The state helper of the parent component.
     * @param children An iterator with all child facets and components of the
     *            parent component as obtained by
     *            {@link UIComponentBase#getFacetsAndChildren()}.
     */
    public static void restore(final FacesContext context,
                               final StateHelper stateHelper,
                               final Iterator<UIComponent> children) {
        while (children.hasNext()) {
            final UIComponent child = children.next();
            child.setId(child.getId()); // This implicitly resets the cached
                                        // client ID. See JSF spec 3.1.6.

            if (child instanceof EditableValueHolder && !child.isTransient()) {
                EditableValueHolderStateHelper.get(stateHelper, child.getClientId(context))
                    .restore((EditableValueHolder) child);
            }

            if (child.getFacetCount() > 0 || child.getChildCount() > 0) {
                EditableValueHolderStateHelper.restore(context, stateHelper, child.getFacetsAndChildren());
            }
        }
    }

    /**
     * Save state of any {@link EditableValueHolder} children.
     * 
     * @param context The faces context to work with.
     * @param stateHelper The state helper of the parent component.
     * @param children An iterator with all child facets and components of the
     *            parent component as obtained by
     *            {@link UIComponentBase#getFacetsAndChildren()}.
     */
    public static void save(final FacesContext context,
                            final StateHelper stateHelper,
                            final Iterator<UIComponent> children) {
        while (children.hasNext()) {
            final UIComponent child = children.next();

            if (child instanceof EditableValueHolder && !child.isTransient()) {
                EditableValueHolderStateHelper.get(stateHelper, child.getClientId(context))
                    .save((EditableValueHolder) child);
            }

            if (child.getFacetCount() > 0 || child.getChildCount() > 0) {
                EditableValueHolderStateHelper.save(context, stateHelper, child.getFacetsAndChildren());
            }
        }
    }

    private Object submittedValue;

    // Actions
    // --------------------------------------------------------------------------------------------------------

    private boolean valid = true;

    private Object localValue;

    // Utility
    // --------------------------------------------------------------------------------------------------------

    private boolean localValueSet;

    /**
     * Restore the state of the given {@link EditableValueHolder}.
     * 
     * @param holder The {@link EditableValueHolder} to restore the state for.
     */
    public void restore(final EditableValueHolder holder) {
        holder.setSubmittedValue(submittedValue);
        holder.setValid(valid);
        holder.setValue(localValue);
        holder.setLocalValueSet(localValueSet);
    }

    /**
     * Save the state of the given {@link EditableValueHolder}.
     * 
     * @param holder The {@link EditableValueHolder} to save the state for.
     */
    public void save(final EditableValueHolder holder) {
        submittedValue = holder.getSubmittedValue();
        valid = holder.isValid();
        localValue = holder.getLocalValue();
        localValueSet = holder.isLocalValueSet();
    }

}