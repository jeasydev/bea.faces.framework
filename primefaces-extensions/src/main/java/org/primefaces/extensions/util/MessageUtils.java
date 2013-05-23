/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Message utils.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
public class MessageUtils {

    public static final String FACES_MESSAGES = "javax.faces.Messages";

    public static FacesMessage getMessage(final FacesMessage.Severity severity,
                                          final String key,
                                          final Object... params) {
        // let current locale to be calculated
        return MessageUtils.getMessage(null, severity, key, params);
    }

    public static FacesMessage getMessage(final Locale locale,
                                          final FacesMessage.Severity severity,
                                          final String key,
                                          final Object... params) {
        String summary = null;
        String detail = null;
        ResourceBundle bundle;
        String bundleName;

        final FacesContext context = FacesContext.getCurrentInstance();
        final Application application = context.getApplication();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = application.getClass().getClassLoader();
        }

        // try to find message in user provided bundle
        bundleName = application.getMessageBundle();
        if (bundleName != null) {
            bundle = ResourceBundle.getBundle(bundleName,
                                              (locale != null ? locale : context.getViewRoot().getLocale()), loader);
            if (bundle != null) {
                try {
                    summary = bundle.getString(key);
                    detail = bundle.getString(key + "_detail");
                } catch (final MissingResourceException e) {
                    // ignore
                }
            }
        }

        if (summary == null) {
            // try to find message in JSF standard provided bundle
            bundle = ResourceBundle.getBundle(MessageUtils.FACES_MESSAGES, (locale != null ? locale : context
                .getViewRoot().getLocale()), loader);
            if (bundle == null) {
                throw new NullPointerException();
            }

            try {
                summary = bundle.getString(key);
                detail = bundle.getString(key + "_detail");
            } catch (final MissingResourceException e) {
                // ignore
            }
        }

        if (summary != null && params != null) {
            summary = MessageFormat.format(summary, params);
        }

        if (detail != null && params != null) {
            detail = MessageFormat.format(detail, params);
        }

        if (summary != null) {
            return new FacesMessage(severity, summary, (detail != null ? detail : StringUtils.EMPTY));
        }

        return new FacesMessage(severity, "???" + key + "???", (detail != null ? detail : StringUtils.EMPTY));
    }

    public static FacesMessage getMessage(final Locale locale, final String key, final Object... params) {
        // set severity to error
        return MessageUtils.getMessage(locale, FacesMessage.SEVERITY_ERROR, key, params);
    }

    public static FacesMessage getMessage(final String key, final Object... params) {
        // let current locale to be calculated
        // set severity to error
        return MessageUtils.getMessage(null, FacesMessage.SEVERITY_ERROR, key, params);
    }
}
