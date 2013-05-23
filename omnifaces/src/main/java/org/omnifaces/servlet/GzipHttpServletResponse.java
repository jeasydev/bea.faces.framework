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
package org.omnifaces.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This HTTP servlet response wrapper will GZIP the response when the given
 * threshold has exceeded and the response content type matches one of the given
 * mimetypes.
 * 
 * @author Bauke Scholtz
 * @since 1.1
 */
public class GzipHttpServletResponse extends HttpServletResponseOutputWrapper {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    /**
     * This output stream will switch to GZIP compression when the given
     * threshold is exceeded.
     * <p>
     * This is an inner class because it needs to be able to manipulate the
     * response headers once the decision whether to GZIP or not has been made.
     * 
     * @author Bauke Scholtz
     */
    private class GzipThresholdOutputStream extends OutputStream {

        // Constants
        // --------------------------------------------------------------------------------------------------

        private static final String ERROR_CLOSED = "Stream is already closed.";

        // Properties
        // -------------------------------------------------------------------------------------------------

        private final byte[] thresholdBuffer;
        private int thresholdLength;
        private OutputStream output;
        private boolean closed;

        // Constructors
        // -----------------------------------------------------------------------------------------------

        public GzipThresholdOutputStream(final int threshold) {
            thresholdBuffer = new byte[threshold];
        }

        // Actions
        // ----------------------------------------------------------------------------------------------------

        /**
         * Check if the current stream is closed and if so, then throw IO
         * exception.
         * 
         * @throws IOException When the current stream is closed.
         */
        private void checkClosed() throws IOException {
            if (closed) {
                throw new IOException(GzipThresholdOutputStream.ERROR_CLOSED);
            }
        }

        @Override
        public void close() throws IOException {
            if (closed) {
                return;
            }

            if (output == null) {
                // Threshold buffer hasn't exceeded. Use normal output stream.
                setContentLength(thresholdLength);
                output = createGzipOutputStreamIfNecessary(false);
                output.write(thresholdBuffer, 0, thresholdLength);
            }

            output.close();
            closed = true;
        }

        /**
         * Create GZIP output stream if necessary. That is, when the given
         * <code>gzip</code> argument is <code>true</code>, the current response
         * does not have the <code>Cache-Control: no-transform</code> or
         * <code>Content-Range</code> headers, the current response is not
         * committed, the content type is not <code>null</code> and the content
         * type matches one of the mimetypes.
         */
        private OutputStream createGzipOutputStreamIfNecessary(final boolean gzip) throws IOException {
            final ServletResponse originalResponse = getResponse();

            if (gzip && !noGzip && (closing || !isCommitted())) {
                final String contentType = getContentType();

                if (contentType != null && mimetypes.contains(contentType.split(";", 2)[0])) {
                    addHeader("Content-Encoding", "gzip");
                    setHeader("Vary", ((vary != null && !vary.equals("*")) ? (vary + ",") : "") + "Accept-Encoding");
                    return new GZIPOutputStream(originalResponse.getOutputStream());
                }
            }

            if (contentLength > 0) {
                originalResponse.setContentLength(contentLength);
            }

            return originalResponse.getOutputStream();
        }

        @Override
        public void flush() throws IOException {
            checkClosed();

            if (output != null) {
                output.flush();
            }
        }

        public void reset() {
            thresholdLength = 0;
            output = null;
        }

        @Override
        public void write(final byte[] bytes) throws IOException {
            write(bytes, 0, bytes.length);
        }

        // Helpers
        // ----------------------------------------------------------------------------------------------------

        @Override
        public void write(final byte[] bytes, final int offset, final int length) throws IOException {
            checkClosed();

            if (length == 0) {
                return;
            }

            if (output == null) {
                if ((length - offset) <= (thresholdBuffer.length - thresholdLength)) {
                    System.arraycopy(bytes, offset, thresholdBuffer, thresholdLength, length);
                    thresholdLength += length;
                    return;
                } else {
                    // Threshold buffer has exceeded. Now use GZIP if possible.
                    output = createGzipOutputStreamIfNecessary(true);
                    output.write(thresholdBuffer, 0, thresholdLength);
                }
            }

            output.write(bytes, offset, length);
        }

        @Override
        public void write(final int b) throws IOException {
            write(new byte[] { (byte) b }, 0, 1);
        }

    }

    // Properties
    // -----------------------------------------------------------------------------------------------------

    private static final Pattern NO_TRANSFORM = Pattern.compile("((.*)[\\s,])?no-transform([\\s,](.*))?",
                                                                Pattern.CASE_INSENSITIVE);
    private final int threshold;
    private final Set<String> mimetypes;
    private int contentLength;
    private String vary;
    private boolean noGzip;
    private boolean closing;

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    private GzipThresholdOutputStream output;

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Construct a new GZIP HTTP servlet response based on the given wrapped
     * response, threshold and mimetypes.
     * 
     * @param wrapped The wrapped response.
     * @param threshold The GZIP buffer threshold.
     * @param mimetypes The mimetypes which needs to be compressed with GZIP.
     */
    public GzipHttpServletResponse(final HttpServletResponse wrapped, final int threshold, final Set<String> mimetypes) {
        super(wrapped);
        this.threshold = threshold;
        this.mimetypes = mimetypes;
    }

    @Override
    public void addHeader(String name, final String value) {
        super.addHeader(name, value);

        if (name != null && value != null) {
            name = name.toLowerCase();

            if ("vary".equals(name)) {
                vary = ((vary != null) ? (vary + ",") : "") + value;
            } else if ("content-range".equals(name)) {
                noGzip = true;
            } else if ("cache-control".equals(name)) {
                noGzip = (noGzip || GzipHttpServletResponse.NO_TRANSFORM.matcher(value).matches());
            }
        }
    }

    @Override
    public void close() throws IOException {
        closing = true;
        super.close();
        closing = false;
    }

    @Override
    protected OutputStream createOutputStream() {
        return output = new GzipThresholdOutputStream(threshold);
    }

    @Override
    public void reset() {
        super.reset();

        if (!isCommitted()) {
            contentLength = 0;
            vary = null;
            noGzip = false;

            if (output != null) {
                output.reset();
            }
        }
    }

    @Override
    public void setContentLength(final int contentLength) {
        // Get hold of content length locally to avoid it from being set on
        // responses which will actually be gzipped.
        this.contentLength = contentLength;
    }

    // Inner classes
    // --------------------------------------------------------------------------------------------------

    @Override
    public void setHeader(String name, final String value) {
        super.setHeader(name, value);

        if (name != null) {
            name = name.toLowerCase();

            if ("vary".equals(name)) {
                vary = value;
            } else if ("content-range".equals(name)) {
                noGzip = (value != null);
            } else if ("cache-control".equals(name)) {
                noGzip = (value != null && GzipHttpServletResponse.NO_TRANSFORM.matcher(value).matches());
            }
        }
    }

}