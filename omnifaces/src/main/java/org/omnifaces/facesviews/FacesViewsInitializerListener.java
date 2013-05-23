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
package org.omnifaces.facesviews;

import static org.omnifaces.facesviews.FacesServletDispatchMethod.DO_FILTER;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.omnifaces.config.WebXml;
import org.omnifaces.eventlistener.DefaultServletContextListener;
import org.omnifaces.util.Faces;
import org.omnifaces.util.ResourcePaths;
import org.omnifaces.util.Utils;

/**
 * Convenience class for Servlet 3.0 users, which will map the FacesServlet to
 * extensions found during scanning in {@link FacesViewsInitializer}. This part
 * of the initialization is executed in a separate ServletContextListener,
 * because the FacesServlet has to be available. This is not guaranteed to be
 * the case in an ServletContainerInitializer.
 * <p>
 * For a guide on FacesViews, please see the <a
 * href="package-summary.html">package summary</a>.
 * 
 * @author Arjan Tijms
 * 
 */
@WebListener
public class FacesViewsInitializerListener extends DefaultServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent context) {

        final ServletContext servletContext = context.getServletContext();

        if (!"false".equals(servletContext.getInitParameter(FacesViews.FACES_VIEWS_ENABLED_PARAM_NAME))) {

            final Set<String> extensions = Faces.getApplicationAttribute(servletContext,
                                                                         FacesViews.FACES_VIEWS_RESOURCES_EXTENSIONS);

            if (!Utils.isEmpty(extensions)) {

                final Set<String> mappings = new HashSet<String>(extensions);
                for (String welcomeFile : WebXml.INSTANCE.init(servletContext).getWelcomeFiles()) {
                    if (ResourcePaths.isExtensionless(welcomeFile)) {
                        if (!welcomeFile.startsWith("/")) {
                            welcomeFile = "/" + welcomeFile;
                        }
                        mappings.add(welcomeFile);
                    }
                }

                if (FacesViews.getFacesServletDispatchMethod(servletContext) == DO_FILTER) {
                    // In order for the DO_FILTER method to work the
                    // FacesServlet, in addition the forward filter, has
                    // to be mapped on all extensionless resources.
                    final Map<String, String> collectedViews = Faces
                        .getApplicationAttribute(servletContext, FacesViews.FACES_VIEWS_RESOURCES);
                    mappings.addAll(ResourcePaths.filterExtension(collectedViews.keySet()));
                }

                FacesViews.mapFacesServlet(servletContext, mappings);
            }
        }
    }

}