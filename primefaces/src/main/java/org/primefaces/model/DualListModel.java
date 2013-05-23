package org.primefaces.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DualListModel<T> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<T> source = new ArrayList<T>();
    private List<T> target = new ArrayList<T>();

    public DualListModel() {
    }

    public DualListModel(final List<T> source, final List<T> target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        final DualListModel<T> other = (DualListModel<T>) obj;

        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) return false;
        if (this.target != other.target && (this.target == null || !this.target.equals(other.target))) return false;

        return true;
    }

    public List<T> getSource() {
        return source;
    }

    public List<T> getTarget() {
        return target;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 29 * hash + (this.target != null ? this.target.hashCode() : 0);
        return hash;
    }

    public void setSource(final List<T> source) {
        this.source = source;
    }

    public void setTarget(final List<T> target) {
        this.target = target;
    }
}
