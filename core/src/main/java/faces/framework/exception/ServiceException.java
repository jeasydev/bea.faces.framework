package faces.framework.exception;

import java.util.List;
import javax.ejb.ApplicationException;

@ApplicationException
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static String build(final List<String> messages) {
        final StringBuilder builder = new StringBuilder();
        for (final String message : messages)
            builder.append(message).append("\n");
        return builder.toString();
    }

    /**
     * Cria uma nova inst&acirc;ncia de ServicoException.
     * 
     * @param message
     */
    public ServiceException(final List<String> messages) {
        super(ServiceException.build(messages));
    }

    /**
     * Cria uma nova inst&acirc;ncia de ServicoException.
     * 
     * @param message
     */
    public ServiceException(final String message) {
        super(message);
    }

    /**
     * Cria uma nova inst&acirc;ncia de ServicoException.
     * 
     * @param message
     * @param cause
     */
    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Cria uma nova inst&acirc;ncia de ServicoException.
     * 
     * @param cause
     */
    public ServiceException(final Throwable cause) {
        this(cause.getLocalizedMessage(), cause);
    }
}
