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
package org.omnifaces.component.tree;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.omnifaces.model.tree.TreeModel;
import org.omnifaces.util.Components;
import org.omnifaces.util.State;

/**
 * <strong>TreeNode</strong> is an {@link UIComponent} that represents a single
 * tree node within a parent {@link Tree} component. Within this component, the
 * <code>var</code> attribute of the parent {@link Tree} component will expose
 * the tree node. Each of its children is processed by {@link TreeNodeItem}.
 * <p>
 * The <code>level</code> attribute can be used to specify for which tree node
 * level as obtained by {@link TreeModel#getLevel()} this component should
 * render the children by {@link TreeNodeItem}. The root tree node has level 0.
 * 
 * @author Bauke Scholtz
 * @see Tree
 * @see TreeNodeItem
 */
@FacesComponent(TreeNode.COMPONENT_TYPE)
public class TreeNode extends TreeFamily {

    // Public constants
    // -----------------------------------------------------------------------------------------------

    private enum PropertyKeys {
        // Cannot be uppercased. They have to exactly match the attribute names.
        level;
    }

    // Private constants
    // ----------------------------------------------------------------------------------------------

    /** The standard component type. */
    public static final String COMPONENT_TYPE = "org.omnifaces.component.tree.TreeNode";

    // Variables
    // ------------------------------------------------------------------------------------------------------

    private final State state = new State(getStateHelper());

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Returns the level for which this node should render the items.
     * 
     * @return The level for which this node should render the items.
     */
    public Integer getLevel() {
        return state.get(PropertyKeys.level);
    }

    /**
     * This method is by design only called by
     * {@link Tree#processTreeNode(FacesContext, PhaseId)} as it maintains all
     * the nodes.
     * 
     * @see Tree#processTreeNode(FacesContext, PhaseId)
     */
    @Override
    protected void process(final FacesContext context, final PhaseId phaseId) {
        if (!isRendered()) {
            return;
        }

        processSuper(context, phaseId);
    }

    // Getters/setters
    // ------------------------------------------------------------------------------------------------

    /**
     * Sets the level for which this node should render the items.
     * 
     * @param level The level for which this node should render the items.
     */
    public void setLevel(final Integer level) {
        state.put(PropertyKeys.level, level);
    }

    /**
     * Validate the component hierarchy.
     * 
     * @throws IllegalArgumentException When the direct parent component isn't
     *             of type {@link Tree}.
     */
    @Override
    protected void validateHierarchy() {
        Components.validateHasDirectParent(this, Tree.class);
    }

}