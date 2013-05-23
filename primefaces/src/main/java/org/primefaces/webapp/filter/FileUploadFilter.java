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
package org.primefaces.webapp.filter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.primefaces.webapp.MultipartRequest;

public class FileUploadFilter implements Filter {

    private final static Logger logger = Logger.getLogger(FileUploadFilter.class.getName());

    private final static String THRESHOLD_SIZE_PARAM = "thresholdSize";

    private final static String UPLOAD_DIRECTORY_PARAM = "uploadDirectory";

    private String thresholdSize;

    private String uploadDir;

    @Override
    public void destroy() {
        if (FileUploadFilter.logger.isLoggable(Level.FINE))
            FileUploadFilter.logger.fine("Destroying FileUploadFilter");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
        throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final boolean isMultipart = ServletFileUpload.isMultipartContent(httpServletRequest);

        if (isMultipart) {
            if (FileUploadFilter.logger.isLoggable(Level.FINE))
                FileUploadFilter.logger.fine("Parsing file upload request");

            final DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            if (thresholdSize != null) {
                diskFileItemFactory.setSizeThreshold(Integer.valueOf(thresholdSize));
            }
            if (uploadDir != null) {
                diskFileItemFactory.setRepository(new File(uploadDir));
            }

            final ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
            final MultipartRequest multipartRequest = new MultipartRequest(httpServletRequest, servletFileUpload);

            if (FileUploadFilter.logger.isLoggable(Level.FINE))
                FileUploadFilter.logger
                    .fine("File upload request parsed succesfully, continuing with filter chain with a wrapped multipart request");

            filterChain.doFilter(multipartRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        thresholdSize = filterConfig.getInitParameter(FileUploadFilter.THRESHOLD_SIZE_PARAM);
        uploadDir = filterConfig.getInitParameter(FileUploadFilter.UPLOAD_DIRECTORY_PARAM);

        if (FileUploadFilter.logger.isLoggable(Level.FINE))
            FileUploadFilter.logger.fine("FileUploadFilter initiated successfully");
    }

}