/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.primefaces.component.api;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

public class WrapperEvent extends FacesEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private FacesEvent event = null;

    private String rowKey = null;

    public WrapperEvent(final UIComponent component, final FacesEvent event, final String rowKey) {
        super(component);
        this.event = event;
        this.rowKey = rowKey;
    }

    public FacesEvent getFacesEvent() {
        return (event);
    }

    @Override
    public PhaseId getPhaseId() {
        return (event.getPhaseId());
    }

    public String getRowKey() {
        return rowKey;
    }

    @Override
    public boolean isAppropriateListener(final FacesListener listener) {
        return (false);
    }

    @Override
    public void processListener(final FacesListener listener) {
        throw new IllegalStateException();
    }

    @Override
    public void setPhaseId(final PhaseId phaseId) {
        event.setPhaseId(phaseId);
    }
}
