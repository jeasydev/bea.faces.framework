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
package org.primefaces.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class MessageFactory {

    private static String DEFAULT_BUNDLE_BASENAME = "javax.faces.Messages";
    private static String PRIMEFACES_BUNDLE_BASENAME = "org.primefaces.Messages";
    private static String DEFAULT_DETAIL_SUFFIX = "_detail";

    protected static ClassLoader getCurrentClassLoader(final Object clazz) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null) {
            loader = clazz.getClass().getClassLoader();
        }
        return loader;
    }

    public static String getFormattedText(final Locale locale, final String message, final Object params[]) {
        MessageFormat messageFormat = null;

        if (params == null || message == null) return message;

        if (locale != null)
            messageFormat = new MessageFormat(message, locale);
        else messageFormat = new MessageFormat(message);

        return messageFormat.format(params);
    }

    public static Object getLabel(final FacesContext facesContext, final UIComponent component) {
        String label = (String) component.getAttributes().get("label");

        if (label == null) {
            label = component.getClientId(facesContext);
        }

        return label;
    }

    protected static Locale getLocale() {
        Locale locale = null;
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext != null && facesContext.getViewRoot() != null) {
            locale = facesContext.getViewRoot().getLocale();

            if (locale == null) locale = Locale.getDefault();
        } else {
            locale = Locale.getDefault();
        }

        return locale;
    }

    public static FacesMessage getMessage(final Locale locale, final String messageId, final Object params[]) {
        String summary = null;
        String detail = null;
        final String userBundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
        ResourceBundle bundle = null;

        // try user defined bundle first
        if (userBundleName != null) {
            try {
                bundle = ResourceBundle.getBundle(userBundleName, locale, MessageFactory
                    .getCurrentClassLoader(userBundleName));
                summary = bundle.getString(messageId);
            } catch (final MissingResourceException e) {
                // No Op
            }
        }

        // try primefaces bundle
        if (summary == null) {
            try {
                bundle = ResourceBundle.getBundle(MessageFactory.PRIMEFACES_BUNDLE_BASENAME, locale, MessageFactory
                    .getCurrentClassLoader(MessageFactory.PRIMEFACES_BUNDLE_BASENAME));
                if (bundle == null) {
                    throw new NullPointerException();
                }
                summary = bundle.getString(messageId);
            } catch (final MissingResourceException e) {
                // No Op
            }
        }

        // fallback to default jsf bundle
        if (summary == null) {
            try {
                bundle = ResourceBundle.getBundle(MessageFactory.DEFAULT_BUNDLE_BASENAME, locale, MessageFactory
                    .getCurrentClassLoader(MessageFactory.DEFAULT_BUNDLE_BASENAME));
                if (bundle == null) {
                    throw new NullPointerException();
                }
                summary = bundle.getString(messageId);
            } catch (final MissingResourceException e) {
                // No Op
            }
        }

        summary = MessageFactory.getFormattedText(locale, summary, params);

        try {
            detail = MessageFactory.getFormattedText(locale, bundle.getString(messageId
                + MessageFactory.DEFAULT_DETAIL_SUFFIX), params);
        } catch (final MissingResourceException e) {
            // NoOp
        }

        return new FacesMessage(summary, detail);
    }

    public static FacesMessage getMessage(final String messageId,
                                          final FacesMessage.Severity severity,
                                          final Object[] params) {
        final FacesMessage facesMessage = MessageFactory.getMessage(MessageFactory.getLocale(), messageId, params);
        facesMessage.setSeverity(severity);

        return facesMessage;
    }

    private MessageFactory() {
    }
}
