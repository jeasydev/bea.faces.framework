//
// [ExceptionHandlerFactory.java]
// Originalmente parte integrante do projeto br.com.bb.framework.
// Criado por brunojensen em Oct 19, 2012.
//
package br.com.bea.framework.exception.handler;

/**
 * ExceptionHandlerFactory. Autoexplicativo, se bem definido.
 * 
 * @author brunojensen
 * @since 2012
 */
public class ExceptionHandlerFactory extends javax.faces.context.ExceptionHandlerFactory {

    private final javax.faces.context.ExceptionHandlerFactory parent;

    public ExceptionHandlerFactory(final javax.faces.context.ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new ExceptionHandler(parent.getExceptionHandler());
    }
}
