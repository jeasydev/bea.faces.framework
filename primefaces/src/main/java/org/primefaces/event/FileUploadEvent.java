package org.primefaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import org.primefaces.model.UploadedFile;

public class FileUploadEvent extends FacesEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final UploadedFile file;

    public FileUploadEvent(final UIComponent component, final UploadedFile file) {
        super(component);
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    @Override
    public boolean isAppropriateListener(final FacesListener listener) {
        return false;
    }

    @Override
    public void processListener(final FacesListener listener) {
        throw new UnsupportedOperationException();
    }
}
