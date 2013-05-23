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

import java.util.Map;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import org.omnifaces.util.Faces;
import org.omnifaces.util.ResourcePaths;

/**
 * View handler that renders action URL extensionless if the current request is
 * extensionless and the requested resource is a mapped one, otherwise as-is.
 * <p>
 * For a guide on FacesViews, please see the <a
 * href="package-summary.html">package summary</a>.
 * 
 * @author Arjan Tijms
 * 
 */
public class FacesViewsViewHandler extends ViewHandlerWrapper {

    private final ViewHandler wrapped;

    public FacesViewsViewHandler(final ViewHandler viewHandler) {
        wrapped = viewHandler;
    }

    @Override
    public String getActionURL(final FacesContext context, final String viewId) {

        final Map<String, String> mappedResources = Faces.getApplicationAttribute(context,
                                                                                  FacesViews.FACES_VIEWS_RESOURCES);
        if (mappedResources.containsKey(viewId)) {

            if (FacesViews.isScannedViewsAlwaysExtensionless(context) || isOriginalViewExtensionless(context)) {
                // User has requested to always render extensionless, or the
                // requested viewId was mapped and the current
                // request is extensionless, render the action URL extensionless
                // as well.
                return context.getExternalContext().getRequestContextPath() + ResourcePaths.stripExtension(viewId);
            }
        }

        // Not a resource we mapped or not a forwarded one, let the original
        // view handler take care of it.
        return super.getActionURL(context, viewId);
    }

    @Override
    public ViewHandler getWrapped() {
        return wrapped;
    }

    private boolean isOriginalViewExtensionless(final FacesContext context) {
        String originalViewId = Faces.getRequestAttribute(context, "javax.servlet.forward.servlet_path");
        if (originalViewId == null) {
            originalViewId = Faces.getRequestAttribute(context, FacesViews.FACES_VIEWS_ORIGINAL_SERVLET_PATH);
        }

        return ResourcePaths.isExtensionless(originalViewId);
    }

}