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
package org.primefaces.context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.event.AbortProcessingException;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class PrimePartialResponseWriter extends PartialResponseWriter {

    private final PartialResponseWriter wrapped;

    public PrimePartialResponseWriter(final PartialResponseWriter writer) {
        super(writer);
        wrapped = writer;
    }

    @Override
    public void delete(final String targetId) throws IOException {
        wrapped.delete(targetId);
    }

    private void encodeCallbackParams(final RequestContext requestContext) throws IOException, JSONException {
        // callback params
        final Map<String, Object> params = requestContext.getCallbackParams();

        final boolean validationFailed = FacesContext.getCurrentInstance().isValidationFailed();
        if (validationFailed) {
            requestContext.addCallbackParam("validationFailed", true);
        }

        if (!params.isEmpty()) {
            final StringBuilder jsonBuilder = new StringBuilder();
            final Map<String, String> callbackParamExtension = new HashMap<String, String>();
            callbackParamExtension.put("ln", "primefaces");
            callbackParamExtension.put("type", "args");

            startExtension(callbackParamExtension);

            jsonBuilder.append("{");

            for (final Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
                final String paramName = it.next();
                final Object paramValue = params.get(paramName);

                if (isBean(paramValue)) {
                    jsonBuilder.append("\"").append(paramName).append("\":").append(new JSONObject(paramValue)
                                                                                        .toString());
                } else {
                    final String json = new JSONObject().put(paramName, paramValue).toString();
                    jsonBuilder.append(json.substring(1, json.length() - 1));
                }

                if (it.hasNext()) {
                    jsonBuilder.append(",");
                }
            }

            jsonBuilder.append("}");

            write(jsonBuilder.toString());
            jsonBuilder.setLength(0);

            endExtension();
        }
    }

    private void encodeScripts(final RequestContext requestContext) throws IOException {
        final List<String> scripts = requestContext.getScriptsToExecute();
        if (!scripts.isEmpty()) {
            startEval();

            for (final String script : scripts) {
                write(script + ";");
            }

            endEval();
        }
    }

    @Override
    public void endDocument() throws IOException {
        final RequestContext requestContext = RequestContext.getCurrentInstance();

        if (requestContext != null) {
            try {
                encodeCallbackParams(requestContext);
                encodeScripts(requestContext);
            } catch (final Exception exception) {
                throw new AbortProcessingException(exception);
            }
        }

        wrapped.endDocument();
    }

    @Override
    public void endError() throws IOException {
        wrapped.endError();
    }

    @Override
    public void endEval() throws IOException {
        wrapped.endEval();
    }

    @Override
    public void endExtension() throws IOException {
        wrapped.endExtension();
    }

    @Override
    public void endInsert() throws IOException {
        wrapped.endInsert();
    }

    @Override
    public void endUpdate() throws IOException {
        wrapped.endUpdate();
    }

    private boolean isBean(final Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean || value instanceof String || value instanceof Number) {
            return false;
        }

        return true;
    }

    @Override
    public void redirect(final String url) throws IOException {
        wrapped.redirect(url);
    }

    @Override
    public void startDocument() throws IOException {
        wrapped.startDocument();
    }

    @Override
    public void startError(final String errorName) throws IOException {
        wrapped.startError(errorName);
    }

    @Override
    public void startEval() throws IOException {
        wrapped.startEval();
    }

    @Override
    public void startExtension(final Map<String, String> attributes) throws IOException {
        wrapped.startExtension(attributes);
    }

    @Override
    public void startInsertAfter(final String targetId) throws IOException {
        wrapped.startInsertAfter(targetId);
    }

    @Override
    public void startInsertBefore(final String targetId) throws IOException {
        wrapped.startInsertBefore(targetId);
    }

    @Override
    public void startUpdate(final String targetId) throws IOException {
        wrapped.startUpdate(targetId);
    }

    @Override
    public void updateAttributes(final String targetId, final Map<String, String> attributes) throws IOException {
        wrapped.updateAttributes(targetId, attributes);
    }
}
