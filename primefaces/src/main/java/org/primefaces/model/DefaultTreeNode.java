/*
 * Copyright 2009,2010 Prime Technology.
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
package org.primefaces.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultTreeNode implements TreeNode, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_TYPE = "default";

    private String type;

    private Object data;

    private List<TreeNode> children;

    private TreeNode parent;

    private boolean expanded;

    private boolean selected;

    private boolean selectable = true;

    public DefaultTreeNode() {
        type = DefaultTreeNode.DEFAULT_TYPE;
        children = new ArrayList<TreeNode>();
    }

    public DefaultTreeNode(final Object data, final TreeNode parent) {
        type = DefaultTreeNode.DEFAULT_TYPE;
        this.data = data;
        children = new ArrayList<TreeNode>();
        this.parent = parent;
        if (this.parent != null) this.parent.getChildren().add(this);
    }

    public DefaultTreeNode(final String type, final Object data, final TreeNode parent) {
        this.type = type;
        this.data = data;
        children = new ArrayList<TreeNode>();
        this.parent = parent;
        if (this.parent != null) this.parent.getChildren().add(this);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final DefaultTreeNode other = (DefaultTreeNode) obj;
        if (data == null) {
            if (other.data != null) return false;
        } else if (!data.equals(other.data)) return false;

        return true;
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public boolean isLeaf() {
        if (children == null) return true;

        return children.isEmpty();
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public void setChildren(final List<TreeNode> children) {
        this.children = children;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    @Override
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void setParent(final TreeNode parent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;

        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
    }

    @Override
    public void setSelectable(final boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public void setSelected(final boolean value) {
        selected = value;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (data != null)
            return data.toString();
        else return super.toString();
    }
}