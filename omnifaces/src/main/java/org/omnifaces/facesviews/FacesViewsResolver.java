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

import java.net.URL;
import javax.faces.view.facelets.ResourceResolver;
import org.omnifaces.util.Faces;

/**
 * Facelets resource resolver that resolves mapped resources (views) to the
 * special auto-scanned faces-views folder.
 * <p>
 * For a guide on FacesViews, please see the <a
 * href="package-summary.html">package summary</a>.
 * 
 * @author Arjan Tijms
 * 
 */
public class FacesViewsResolver extends ResourceResolver {

    private final ResourceResolver resourceResolver;

    public FacesViewsResolver(final ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    @Override
    public URL resolveUrl(final String path) {

        URL resource = resourceResolver.resolveUrl(FacesViews.getMappedPath(path));

        if (resource == null && Faces.isDevelopment()) {
            // If "resource" is null it means it wasn't found. Check if the
            // resource was dynamically added by
            // scanning the faces-views location(s) again.
            FacesViews.scanAndStoreViews(Faces.getServletContext());
            resource = resourceResolver.resolveUrl(FacesViews.getMappedPath(path));
        }

        return resource;
    }

}