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
package org.primefaces.model.mindmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultMindmapNode implements MindmapNode, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private MindmapNode parent;

    private List<MindmapNode> children;

    private String label;

    private Object data;

    private String fill;

    private boolean selectable;

    public DefaultMindmapNode() {
    }

    public DefaultMindmapNode(final String label) {
        this.label = label;
        children = new ArrayList<MindmapNode>();
        selectable = true;
    }

    public DefaultMindmapNode(final String label, final Object data) {
        this(label);
        this.data = data;
    }

    public DefaultMindmapNode(final String label, final Object data, final String fill) {
        this(label, data);
        this.fill = fill;
    }

    public DefaultMindmapNode(final String label, final Object data, final String fill, final boolean selectable) {
        this(label, data, fill);
        this.selectable = selectable;
    }

    @Override
    public void addNode(final MindmapNode node) {
        node.setParent(this);
    }

    @Override
    public List<MindmapNode> getChildren() {
        return children;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String getFill() {
        return fill;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public MindmapNode getParent() {
        return parent;
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    public void setChildren(final List<MindmapNode> children) {
        this.children = children;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public void setFill(final String fill) {
        this.fill = fill;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public void setParent(final MindmapNode parent) {
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
}
