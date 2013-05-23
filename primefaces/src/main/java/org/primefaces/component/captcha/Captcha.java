/*
 * Generated, Do Not Modify
 */
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
package org.primefaces.component.captcha;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.util.MessageFactory;

@ResourceDependencies({

})
public class Captcha extends UIInput {

    protected enum PropertyKeys {

        theme,
        language,
        tabindex,
        label,
        secure;

        String toString;

        PropertyKeys() {
        }

        PropertyKeys(final String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return ((toString != null) ? toString : super.toString());
        }
    }

    public static final String COMPONENT_TYPE = "org.primefaces.component.Captcha";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.CaptchaRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public final static String PUBLIC_KEY = "primefaces.PUBLIC_CAPTCHA_KEY";

    public final static String PRIVATE_KEY = "primefaces.PRIVATE_CAPTCHA_KEY";

    public final static String INVALID_MESSAGE_ID = "primefaces.captcha.INVALID";
    public final static String OLD_PRIVATE_KEY = "org.primefaces.component.captcha.PRIVATE_KEY";

    private static final Logger logger = Logger.getLogger(Captcha.class.getName());

    public Captcha() {
        setRendererType(Captcha.DEFAULT_RENDERER);
    }

    private String createPostParameters(final FacesContext facesContext, final Verification verification)
        throws UnsupportedEncodingException {
        final String challenge = verification.getChallenge();
        final String answer = verification.getAnswer();
        final String remoteAddress = ((HttpServletRequest) facesContext.getExternalContext().getRequest())
            .getRemoteAddr();
        String privateKey = null;
        final String oldPrivateKey = facesContext.getExternalContext().getInitParameter(Captcha.OLD_PRIVATE_KEY);
        final String newPrivateKey = facesContext.getExternalContext().getInitParameter(Captcha.PRIVATE_KEY);

        // Backward compatibility
        if (oldPrivateKey != null) {
            Captcha.logger
                .warning("PrivateKey definition on captcha is deprecated, use primefaces.PRIVATE_CAPTCHA_KEY context-param instead");

            privateKey = oldPrivateKey;
        } else {
            privateKey = newPrivateKey;
        }

        if (privateKey == null) {
            throw new FacesException("Cannot find private key for catpcha, use primefaces.PRIVATE_CAPTCHA_KEY context-param to define one");
        }

        final StringBuilder postParams = new StringBuilder();
        postParams.append("privatekey=").append(URLEncoder.encode(privateKey, "UTF-8"));
        postParams.append("&remoteip=").append(URLEncoder.encode(remoteAddress, "UTF-8"));
        postParams.append("&challenge=").append(URLEncoder.encode(challenge, "UTF-8"));
        postParams.append("&response=").append(URLEncoder.encode(answer, "UTF-8"));

        return postParams.toString();
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return Captcha.COMPONENT_FAMILY;
    }

    public java.lang.String getLabel() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.label, null);
    }

    public java.lang.String getLanguage() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.language, "en");
    }

    public int getTabindex() {
        return (java.lang.Integer) getStateHelper().eval(PropertyKeys.tabindex, 0);
    }

    public java.lang.String getTheme() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.theme, "red");
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(Captcha.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }
        if (setAttributes != null) {
            if (value == null) {
                final ValueExpression ve = getValueExpression(name);
                if (ve == null) {
                    setAttributes.remove(name);
                } else if (!setAttributes.contains(name)) {
                    setAttributes.add(name);
                }
            }
        }
    }

    public boolean isSecure() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.secure, false);
    }

    public void setLabel(final java.lang.String _label) {
        getStateHelper().put(PropertyKeys.label, _label);
        handleAttribute("label", _label);
    }

    public void setLanguage(final java.lang.String _language) {
        getStateHelper().put(PropertyKeys.language, _language);
        handleAttribute("language", _language);
    }

    public void setSecure(final boolean _secure) {
        getStateHelper().put(PropertyKeys.secure, _secure);
        handleAttribute("secure", _secure);
    }

    public void setTabindex(final int _tabindex) {
        getStateHelper().put(PropertyKeys.tabindex, _tabindex);
        handleAttribute("tabindex", _tabindex);
    }

    public void setTheme(final java.lang.String _theme) {
        getStateHelper().put(PropertyKeys.theme, _theme);
        handleAttribute("theme", _theme);
    }

    @Override
    protected void validateValue(final FacesContext context, final Object value) {
        super.validateValue(context, value);

        if (isValid()) {

            String result = null;
            final Verification verification = (Verification) value;

            try {
                final URL url = new URL("http://api-verify.recaptcha.net/verify");
                final URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                final String postBody = createPostParameters(context, verification);

                final OutputStream out = conn.getOutputStream();
                out.write(postBody.getBytes());
                out.flush();
                out.close();

                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = rd.readLine();
                rd.close();
            } catch (final Exception exception) {
                throw new FacesException(exception);
            }

            final boolean isValid = Boolean.valueOf(result);

            if (!isValid) {
                setValid(false);

                final String validatorMessage = getValidatorMessage();
                FacesMessage msg = null;

                if (validatorMessage != null) {
                    msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessage, validatorMessage);
                } else {
                    final Object[] params = new Object[2];
                    params[0] = MessageFactory.getLabel(context, this);
                    params[1] = verification.getAnswer();

                    msg = MessageFactory.getMessage(Captcha.INVALID_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, params);
                }

                context.addMessage(getClientId(context), msg);
            }
        }
    }
}