//
// [ExceptionHandler.java]
// Originalmente parte integrante do projeto br.com.bb.framework.
// Criado por brunojensen em Oct 19, 2012.
//
package faces.framework.exception.handler;

import java.util.Iterator;
import java.util.StringTokenizer;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import faces.framework.exception.ServiceException;

/**
 * ExceptionHandler. Autoexplicativo, se bem definido.
 * 
 * @author brunojensen
 * @since 2012
 */
public class ExceptionHandler extends ExceptionHandlerWrapper {

    private final javax.faces.context.ExceptionHandler wrapped;

    public ExceptionHandler(final javax.faces.context.ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    private void emiteMensagem(final String clientId, final Severity severity, final String mensagem) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final StringTokenizer token = new StringTokenizer(mensagem, "\n");
        while (token.hasMoreElements())
            context.addMessage(clientId, new FacesMessage(severity, (String) token.nextElement(), null));
    }

    private Throwable getException(final Throwable throwable) {
        if (throwable instanceof ViewExpiredException || throwable instanceof ServiceException)
            return throwable;
        else {
            Throwable t = throwable;
            for (int i = 0; i < 5; i++) {
                if (t instanceof ServiceException) return t;
                if (null == t) break;
                t = t.getCause();
            }
        }
        return throwable;
    }

    @Override
    public javax.faces.context.ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        final StringBuilder messages = new StringBuilder();
        if (i.hasNext()) {
            final Throwable throwable = getException(i.next().getContext().getException());
            throwable.printStackTrace();
            final String message = throwable.getLocalizedMessage();
            if (null != message && !message.isEmpty()) messages.append(message).append("\n");
            i.remove();
        }
        if (messages.length() > 0) emiteMensagem(null, FacesMessage.SEVERITY_ERROR, messages.toString());
        getWrapped().handle();
    }
}
