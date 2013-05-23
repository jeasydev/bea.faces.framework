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
package org.primefaces.model;

import java.io.InputStream;
import java.io.Serializable;

/**
 * Default implementation of a StreamedContent
 */
public class DefaultStreamedContent implements StreamedContent, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private InputStream stream;

    private String contentType;

    private String name;

    private String contentEncoding;

    public DefaultStreamedContent() {
    }

    public DefaultStreamedContent(final InputStream stream) {
        this.stream = stream;
    }

    public DefaultStreamedContent(final InputStream stream, final String contentType) {
        this.contentType = contentType;
        this.stream = stream;
    }

    public DefaultStreamedContent(final InputStream stream, final String contentType, final String name) {
        this.contentType = contentType;
        this.stream = stream;
        this.name = name;
    }

    public DefaultStreamedContent(final InputStream stream,
                                  final String contentType,
                                  final String name,
                                  final String contentEncoding) {
        this.contentType = contentType;
        this.stream = stream;
        this.name = name;
        this.contentEncoding = contentEncoding;
    }

    @Override
    public String getContentEncoding() {
        return contentEncoding;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InputStream getStream() {
        return stream;
    }

    public void setContentEncoding(final String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setStream(final InputStream stream) {
        this.stream = stream;
    }
}