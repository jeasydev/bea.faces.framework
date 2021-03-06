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
package org.omnifaces.util;

import java.text.MessageFormat;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.ConverterException;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContextListener;

/**
 * Collection of utility methods for the JSF API with respect to working with
 * {@link FacesMessage}. It also offers the possibility to set a custom message
 * resolver so that you can control the way how messages are been resolved. You
 * can for example supply an implementation wherein the message is been treated
 * as for example a resource bundle key.
 * <p>
 * Here's an example:
 * 
 * <pre>
 * Messages.setResolver(new Messages.Resolver() {
 *     private static final String BASE_NAME = &quot;com.example.i18n.messages&quot;;
 * 
 *     public String getMessage(String message, Object... params) {
 *         ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, Faces.getLocale());
 *         if (bundle.containsKey(message)) {
 *             message = bundle.getString(message);
 *         }
 *         return MessageFormat.format(message, params);
 *     }
 * });
 * </pre>
 * <p>
 * There is already a default resolver which just delegates the message and the
 * parameters straight to {@link MessageFormat#format(String, Object...)}. Note
 * that the resolver can be set only once. It's recommend to do it early during
 * webapp's startup, for example with a {@link ServletContextListener}, or a
 * Servlet 3.0 <code>ServletContainerInitializer</code>, or an eagerly
 * initialized {@link ApplicationScoped} {@link ManagedBean}.
 * <p>
 * Note that all of those shortcut methods by design only sets the message
 * summary and ignores the message detail (it is not possible to offer varargs
 * to parameterize <em>both</em> the summary and the detail). The message
 * summary is exactly the information which is by default displayed in the
 * <code>&lt;h:message(s)&gt;</code>, while the detail is by default only
 * displayed when you explicitly set the <code>showDetail="true"</code>
 * attribute.
 * <p>
 * To create a {@link FacesMessage} with a message detail as well, use the
 * {@link Message} builder as you can obtain by
 * {@link Messages#create(String, Object...)}.
 * 
 * @author Bauke Scholtz
 */
public final class Messages {

    // Private constants
    // ----------------------------------------------------------------------------------------------

    /**
     * Faces message builder.
     * 
     * @author Bauke Scholtz
     * @since 1.1
     */
    public static class Message {

        private final FacesMessage message;

        private Message(final FacesMessage message) {
            this.message = message;
        }

        /**
         * Add the current message as a global message.
         * 
         * @see FacesContext#addMessage(String, FacesMessage)
         */
        public void add() {
            Messages.addGlobal(message);
        }

        /**
         * Add the current message for the given client ID.
         * 
         * @param clientId The client ID to add the current message for.
         * @see FacesContext#addMessage(String, FacesMessage)
         */
        public void add(final String clientId) {
            Messages.add(clientId, message);
        }

        /**
         * Set the detail message of the current message.
         * 
         * @param detail The detail message to be set on the current message.
         * @param params The detail message format parameters, if any.
         * @return The current {@link Message} instance for further building.
         * @see FacesMessage#setDetail(String)
         */
        public Message detail(final String detail, final Object... params) {
            message.setDetail(Messages.resolver.getMessage(detail, params));
            return this;
        }

        /**
         * Set the severity of the current message to ERROR. Note: it defaults
         * to INFO already.
         * 
         * @return The current {@link Message} instance for further building.
         * @see FacesMessage#setSeverity(javax.faces.application.FacesMessage.Severity)
         */
        public Message error() {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            return this;
        }

        /**
         * Set the severity of the current message to FATAL. Note: it defaults
         * to INFO already.
         * 
         * @return The current {@link Message} instance for further building.
         * @see FacesMessage#setSeverity(javax.faces.application.FacesMessage.Severity)
         */
        public Message fatal() {
            message.setSeverity(FacesMessage.SEVERITY_FATAL);
            return this;
        }

        /**
         * Make the current message a flash message. Use this when you need to
         * display the message after a redirect.
         * 
         * @return The current {@link Message} instance for further building.
         * @see Flash#setKeepMessages(boolean)
         */
        public Message flash() {
            Faces.getFlash().setKeepMessages(true);
            return this;
        }

        /**
         * Returns the so far built message.
         * 
         * @return The so far built message.
         */
        public FacesMessage get() {
            return message;
        }

        /**
         * Set the severity of the current message to WARN. Note: it defaults to
         * INFO already.
         * 
         * @return The current {@link Message} instance for further building.
         * @see FacesMessage#setSeverity(javax.faces.application.FacesMessage.Severity)
         */
        public Message warn() {
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            return this;
        }

    }

    // Message resolver
    // -----------------------------------------------------------------------------------------------

    /**
     * The message resolver allows the developers to change the way how messages
     * are resolved.
     * 
     * @author Bauke Scholtz
     */
    public static interface Resolver {

        /**
         * Returns the resolved message based on the given message and
         * parameters.
         * 
         * @param message The message which can be treated as for example a
         *            resource bundle key.
         * @param params The message format parameters, if any.
         * @return The resolved message.
         */
        public String getMessage(String message, Object... params);

    }

    private static final String ERROR_RESOLVER_ALREADY_SET = "The resolver can be set only once.";

    /**
     * This is the default message resolver.
     */
    private static final Resolver DEFAULT_RESOLVER = new Resolver() {

        @Override
        public String getMessage(final String message, final Object... params) {
            return MessageFormat.format(message, params);
        }

    };

    /**
     * Initialize with the default resolver.
     */
    private static Resolver resolver = Messages.DEFAULT_RESOLVER;

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Add a faces message of the given severity to the given client ID, with
     * the given message body which is formatted with the given parameters.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param severity The severity of the faces message.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #create(javax.faces.application.FacesMessage.Severity, String,
     *      Object...)
     * @see #add(String, FacesMessage)
     */
    public static void add(final FacesMessage.Severity severity,
                           final String clientId,
                           final String message,
                           final Object... params) {
        Messages.add(clientId, Messages.create(severity, message, params));
    }

    // Builder
    // --------------------------------------------------------------------------------------------------------

    /**
     * Add the given faces message to the given client ID. When the client ID is
     * <code>null</code>, it becomes a global faces message. This can be
     * filtered in a <code>&lt;h:messages globalOnly="true"&gt;</code>.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The faces message.
     * @see FacesContext#addMessage(String, FacesMessage)
     */
    public static void add(final String clientId, final FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(clientId, message);
    }

    /**
     * Add an ERROR faces message to the given client ID, with the given message
     * body which is formatted with the given parameters.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createError(String, Object...)
     * @see #add(String, FacesMessage)
     */
    public static void addError(final String clientId, final String message, final Object... params) {
        Messages.add(clientId, Messages.createError(message, params));
    }

    // Shortcuts - create message
    // -------------------------------------------------------------------------------------

    /**
     * Add a FATAL faces message to the given client ID, with the given message
     * body which is formatted with the given parameters.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createFatal(String, Object...)
     * @see #add(String, FacesMessage)
     */
    public static void addFatal(final String clientId, final String message, final Object... params) {
        Messages.add(clientId, Messages.createFatal(message, params));
    }

    /**
     * Add a flash scoped faces message of the given severity to the given
     * client ID, with the given message body which is formatted with the given
     * parameters. Use this when you need to display the message after a
     * redirect.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param severity The severity of the faces message.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #create(javax.faces.application.FacesMessage.Severity, String,
     *      Object...)
     * @see #addFlash(String, FacesMessage)
     */
    public static void addFlash(final FacesMessage.Severity severity,
                                final String clientId,
                                final String message,
                                final Object... params) {
        Messages.addFlash(clientId, Messages.create(severity, message, params));
    }

    /**
     * Add a flash scoped faces message to the given client ID. Use this when
     * you need to display the message after a redirect.
     * <p>
     * NOTE: the flash scope has in early Mojarra versions however some pretty
     * peculiar problems. In older versions, the messages are remembered too
     * long, or they are only displayed after refresh, or they are not displayed
     * when the next request is on a different path. Only since Mojarra 2.1.14,
     * all known flash scope problems are solved.
     * 
     * @param clientId The client ID to add the flash scoped faces message for.
     * @param message The faces message.
     * @see Flash#setKeepMessages(boolean)
     * @see #add(String, FacesMessage)
     */
    public static void addFlash(final String clientId, final FacesMessage message) {
        Faces.getFlash().setKeepMessages(true);
        Messages.add(clientId, message);
    }

    /**
     * Add a flash scoped ERROR faces message to the given client ID, with the
     * given message body which is formatted with the given parameters. Use this
     * when you need to display the message after a redirect.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createError(String, Object...)
     * @see #addFlash(String, FacesMessage)
     */
    public static void addFlashError(final String clientId, final String message, final Object... params) {
        Messages.addFlash(clientId, Messages.createError(message, params));
    }

    /**
     * Add a flash scoped FATAL faces message to the given client ID, with the
     * given message body which is formatted with the given parameters. Use this
     * when you need to display the message after a redirect.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createFatal(String, Object...)
     * @see #addFlash(String, FacesMessage)
     */
    public static void addFlashFatal(final String clientId, final String message, final Object... params) {
        Messages.addFlash(clientId, Messages.createFatal(message, params));
    }

    // Shortcuts - add message
    // ----------------------------------------------------------------------------------------

    /**
     * Add a flash scoped global faces message. This adds a faces message to a
     * client ID of <code>null</code>. Use this when you need to display the
     * message after a redirect.
     * 
     * @param message The global faces message.
     * @see #addFlash(String, FacesMessage)
     */
    public static void addFlashGlobal(final FacesMessage message) {
        Messages.addFlash(null, message);
    }

    /**
     * Add a flash scoped global ERROR faces message, with the given message
     * body which is formatted with the given parameters. Use this when you need
     * to display the message after a redirect.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createError(String, Object...)
     * @see #addFlashGlobal(FacesMessage)
     */
    public static void addFlashGlobalError(final String message, final Object... params) {
        Messages.addFlashGlobal(Messages.createError(message, params));
    }

    /**
     * Add a flash scoped global FATAL faces message, with the given message
     * body which is formatted with the given parameters. Use this when you need
     * to display the message after a redirect.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createFatal(String, Object...)
     * @see #addFlashGlobal(FacesMessage)
     */
    public static void addFlashGlobalFatal(final String message, final Object... params) {
        Messages.addFlashGlobal(Messages.createFatal(message, params));
    }

    /**
     * Add a flash scoped global INFO faces message, with the given message body
     * which is formatted with the given parameters. Use this when you need to
     * display the message after a redirect.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createInfo(String, Object...)
     * @see #addFlashGlobal(FacesMessage)
     */
    public static void addFlashGlobalInfo(final String message, final Object... params) {
        Messages.addFlashGlobal(Messages.createInfo(message, params));
    }

    /**
     * Add a flash scoped global WARN faces message, with the given message body
     * which is formatted with the given parameters. Use this when you need to
     * display the message after a redirect.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createWarn(String, Object...)
     * @see #addFlashGlobal(FacesMessage)
     */
    public static void addFlashGlobalWarn(final String message, final Object... params) {
        Messages.addFlashGlobal(Messages.createWarn(message, params));
    }

    /**
     * Add a flash scoped INFO faces message to the given client ID, with the
     * given message body which is formatted with the given parameters. Use this
     * when you need to display the message after a redirect.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createInfo(String, Object...)
     * @see #addFlash(String, FacesMessage)
     */
    public static void addFlashInfo(final String clientId, final String message, final Object... params) {
        Messages.addFlash(clientId, Messages.createInfo(message, params));
    }

    // Shortcuts - add global message
    // ---------------------------------------------------------------------------------

    /**
     * Add a flash scoped WARN faces message to the given client ID, with the
     * given message body which is formatted with the given parameters. Use this
     * when you need to display the message after a redirect.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createWarn(String, Object...)
     * @see #addFlash(String, FacesMessage)
     */
    public static void addFlashWarn(final String clientId, final String message, final Object... params) {
        Messages.addFlash(clientId, Messages.createWarn(message, params));
    }

    /**
     * Add a global faces message. This adds a faces message to a client ID of
     * <code>null</code>.
     * 
     * @param message The global faces message.
     * @see #add(String, FacesMessage)
     */
    public static void addGlobal(final FacesMessage message) {
        Messages.add(null, message);
    }

    /**
     * Add a global faces message of the given severity, with the given message
     * body which is formatted with the given parameters.
     * 
     * @param severity The severity of the global faces message.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #create(javax.faces.application.FacesMessage.Severity, String,
     *      Object...)
     * @see #addGlobal(FacesMessage)
     */
    public static void addGlobal(final FacesMessage.Severity severity, final String message, final Object... params) {
        Messages.addGlobal(Messages.create(severity, message, params));
    }

    /**
     * Add a global ERROR faces message, with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createError(String, Object...)
     * @see #addGlobal(FacesMessage)
     */
    public static void addGlobalError(final String message, final Object... params) {
        Messages.addGlobal(Messages.createError(message, params));
    }

    /**
     * Add a global FATAL faces message, with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createFatal(String, Object...)
     * @see #addGlobal(FacesMessage)
     */
    public static void addGlobalFatal(final String message, final Object... params) {
        Messages.addGlobal(Messages.createFatal(message, params));
    }

    /**
     * Add a global INFO faces message, with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createInfo(String, Object...)
     * @see #addGlobal(FacesMessage)
     */
    public static void addGlobalInfo(final String message, final Object... params) {
        Messages.addGlobal(Messages.createInfo(message, params));
    }

    // Shortcuts - add flash message
    // ----------------------------------------------------------------------------------

    /**
     * Add a global WARN faces message, with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createWarn(String, Object...)
     * @see #addGlobal(FacesMessage)
     */
    public static void addGlobalWarn(final String message, final Object... params) {
        Messages.addGlobal(Messages.createWarn(message, params));
    }

    /**
     * Add an INFO faces message to the given client ID, with the given message
     * body which is formatted with the given parameters.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createInfo(String, Object...)
     * @see #add(String, FacesMessage)
     */
    public static void addInfo(final String clientId, final String message, final Object... params) {
        Messages.add(clientId, Messages.createInfo(message, params));
    }

    /**
     * Add a WARN faces message to the given client ID, with the given message
     * body which is formatted with the given parameters.
     * 
     * @param clientId The client ID to add the faces message for.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @see #createWarn(String, Object...)
     * @see #add(String, FacesMessage)
     */
    public static void addWarn(final String clientId, final String message, final Object... params) {
        Messages.add(clientId, Messages.createWarn(message, params));
    }

    /**
     * Create a faces message of the given severity with the given message body
     * which is formatted with the given parameters. Useful when a faces message
     * is needed to construct a {@link ConverterException} or a
     * {@link ValidatorException}.
     * 
     * @param severity The severity of the faces message.
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @return A new faces message of the given severity with the given message
     *         body which is formatted with the given parameters.
     * @see Resolver#getMessage(String, Object...)
     */
    public static FacesMessage create(final FacesMessage.Severity severity,
                                      final String message,
                                      final Object... params) {
        return new FacesMessage(severity, Messages.resolver.getMessage(message, params), null);
    }

    /**
     * Create a faces message with the default INFO severity and the given
     * message body which is formatted with the given parameters as summary
     * message. To set the detail message, use
     * {@link Message#detail(String, Object...)}. To change the default INFO
     * severity, use {@link Message#warn()}, {@link Message#error()}, or
     * {@link Message#fatal()}. To make it a flash message, use
     * {@link Message#flash()}. To finally add it to the faces context, use
     * either {@link Message#add(String)} to add it for a specific client ID, or
     * {@link Message#add()} to add it as a global message.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @return The {@link Message} builder.
     * @see Messages#createInfo(String, Object...)
     * @see Resolver#getMessage(String, Object...)
     * @since 1.1
     */
    public static Message create(final String message, final Object... params) {
        return new Message(Messages.createInfo(message, params));
    }

    /**
     * Create an ERROR faces message with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @return A new ERROR faces message with the given message body which is
     *         formatted with the given parameters.
     * @see #create(javax.faces.application.FacesMessage.Severity, String,
     *      Object...)
     */
    public static FacesMessage createError(final String message, final Object... params) {
        return Messages.create(FacesMessage.SEVERITY_ERROR, message, params);
    }

    // Shortcuts - add global flash message
    // ---------------------------------------------------------------------------

    /**
     * Create a FATAL faces message with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @return A new FATAL faces message with the given message body which is
     *         formatted with the given parameters.
     * @see #create(javax.faces.application.FacesMessage.Severity, String,
     *      Object...)
     */
    public static FacesMessage createFatal(final String message, final Object... params) {
        return Messages.create(FacesMessage.SEVERITY_FATAL, message, params);
    }

    /**
     * Create an INFO faces message with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @return A new INFO faces message with the given message body which is
     *         formatted with the given parameters.
     * @see #create(javax.faces.application.FacesMessage.Severity, String,
     *      Object...)
     */
    public static FacesMessage createInfo(final String message, final Object... params) {
        return Messages.create(FacesMessage.SEVERITY_INFO, message, params);
    }

    /**
     * Create a WARN faces message with the given message body which is
     * formatted with the given parameters.
     * 
     * @param message The message body.
     * @param params The message format parameters, if any.
     * @return A new WARN faces message with the given message body which is
     *         formatted with the given parameters.
     * @see #create(javax.faces.application.FacesMessage.Severity, String,
     *      Object...)
     */
    public static FacesMessage createWarn(final String message, final Object... params) {
        return Messages.create(FacesMessage.SEVERITY_WARN, message, params);
    }

    /**
     * Set the custom message resolver. It can be set only once. It's recommend
     * to do it early during webapp's startup, for example with a
     * {@link ServletContextListener}, or a Servlet 3.0
     * <code>ServletContainerInitializer</code>, or an eagerly initialized
     * {@link ApplicationScoped} {@link ManagedBean}.
     * 
     * @param resolver The custom message resolver.
     * @throws IllegalStateException When the resolver has already been set.
     */
    public static void setResolver(final Resolver resolver) {
        if (Messages.resolver == Messages.DEFAULT_RESOLVER) {
            Messages.resolver = resolver;
        } else {
            throw new IllegalStateException(Messages.ERROR_RESOLVER_ALREADY_SET);
        }
    }

    private Messages() {
        // Hide constructor.
    }

}