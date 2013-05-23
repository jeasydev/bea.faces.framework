package br.com.bea.framework.exception;

import java.util.List;
import javax.ejb.ApplicationException;

@ApplicationException
public class DAOException extends RuntimeException {

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
    public DAOException(final List<String> messages) {
        super(DAOException.build(messages));
    }

    /**
     * Cria uma nova inst&acirc;ncia de ServicoException.
     * 
     * @param message
     */
    public DAOException(final String message) {
        super(message);
    }

    /**
     * Cria uma nova inst&acirc;ncia de ServicoException.
     * 
     * @param message
     * @param cause
     */
    public DAOException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Cria uma nova inst&acirc;ncia de ServicoException.
     * 
     * @param cause
     */
    public DAOException(final Throwable cause) {
        this(cause.getLocalizedMessage(), cause);
    }
}
