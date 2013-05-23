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
package org.primefaces.webapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MultipartRequest extends HttpServletRequestWrapper {

    private static final Logger logger = Logger.getLogger(MultipartRequest.class.getName());

    private final Map<String, List<String>> formParams;

    private final Map<String, List<FileItem>> fileParams;

    private Map<String, String[]> parameterMap;

    public MultipartRequest(final HttpServletRequest request, final ServletFileUpload servletFileUpload) throws IOException {
        super(request);
        formParams = new LinkedHashMap<String, List<String>>();
        fileParams = new LinkedHashMap<String, List<FileItem>>();

        parseRequest(request, servletFileUpload);
    }

    private void addFileParam(final FileItem item) {
        if (fileParams.containsKey(item.getFieldName())) {
            fileParams.get(item.getFieldName()).add(item);
        } else {
            final List<FileItem> items = new ArrayList<FileItem>();
            items.add(item);
            fileParams.put(item.getFieldName(), items);
        }
    }

    private void addFormParam(final FileItem item) {
        if (formParams.containsKey(item.getFieldName())) {
            formParams.get(item.getFieldName()).add(getItemString(item));
        } else {
            final List<String> items = new ArrayList<String>();
            items.add(getItemString(item));
            formParams.put(item.getFieldName(), items);
        }
    }

    public FileItem getFileItem(final String name) {
        if (fileParams.containsKey(name)) {
            final List<FileItem> items = fileParams.get(name);

            return items.get(0);
        } else {
            return null;
        }
    }

    private String getItemString(final FileItem item) {
        try {
            final String characterEncoding = getRequest().getCharacterEncoding();
            return (characterEncoding == null) ? item.getString() : item.getString(characterEncoding);
        } catch (final UnsupportedEncodingException e) {
            MultipartRequest.logger.severe("Unsupported character encoding " + getRequest().getCharacterEncoding());
            return item.getString();
        }
    }

    @Override
    public String getParameter(final String name) {
        if (formParams.containsKey(name)) {
            final List<String> values = formParams.get(name);
            if (values.isEmpty())
                return "";
            else return values.get(0);
        } else {
            return super.getParameter(name);
        }
    }

    @Override
    public Map getParameterMap() {
        if (parameterMap == null) {
            final Map<String, String[]> map = new LinkedHashMap<String, String[]>();

            for (final String formParam : formParams.keySet()) {
                map.put(formParam, formParams.get(formParam).toArray(new String[0]));
            }

            map.putAll(super.getParameterMap());

            parameterMap = Collections.unmodifiableMap(map);
        }

        return parameterMap;
    }

    @Override
    public Enumeration getParameterNames() {
        final Set<String> paramNames = new LinkedHashSet<String>();
        paramNames.addAll(formParams.keySet());

        final Enumeration<String> original = super.getParameterNames();
        while (original.hasMoreElements()) {
            paramNames.add(original.nextElement());
        }

        return Collections.enumeration(paramNames);
    }

    @Override
    public String[] getParameterValues(final String name) {
        if (formParams.containsKey(name)) {
            final List<String> values = formParams.get(name);
            if (values.isEmpty())
                return new String[0];
            else return values.toArray(new String[values.size()]);
        } else {
            return super.getParameterValues(name);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseRequest(final HttpServletRequest request, final ServletFileUpload servletFileUpload)
        throws IOException {
        try {
            final List<FileItem> fileItems = servletFileUpload.parseRequest(request);

            for (final FileItem item : fileItems) {
                if (item.isFormField())
                    addFormParam(item);
                else addFileParam(item);
            }

        } catch (final FileUploadException e) {
            MultipartRequest.logger.severe("Error in parsing fileupload request");

            throw new IOException(e.getMessage());
        }
    }
}
