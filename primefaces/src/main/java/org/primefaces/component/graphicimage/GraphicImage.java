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
package org.primefaces.component.graphicimage;

import java.util.ArrayList;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;

@ResourceDependencies({

})
public class GraphicImage extends HtmlGraphicImage {

    protected enum PropertyKeys {

        cache,
        name,
        library;

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

    public static final String COMPONENT_TYPE = "org.primefaces.component.GraphicImage";
    public static final String COMPONENT_FAMILY = "org.primefaces.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.component.GraphicImageRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.component.";

    public GraphicImage() {
        setRendererType(GraphicImage.DEFAULT_RENDERER);
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    public String getFamily() {
        return GraphicImage.COMPONENT_FAMILY;
    }

    public java.lang.String getLibrary() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.library, null);
    }

    public java.lang.String getName() {
        return (java.lang.String) getStateHelper().eval(PropertyKeys.name, null);
    }

    public void handleAttribute(final String name, final Object value) {
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(GraphicImage.OPTIMIZED_PACKAGE)) {
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

    public boolean isCache() {
        return (java.lang.Boolean) getStateHelper().eval(PropertyKeys.cache, true);
    }

    public void setCache(final boolean _cache) {
        getStateHelper().put(PropertyKeys.cache, _cache);
        handleAttribute("cache", _cache);
    }

    public void setLibrary(final java.lang.String _library) {
        getStateHelper().put(PropertyKeys.library, _library);
        handleAttribute("library", _library);
    }

    public void setName(final java.lang.String _name) {
        getStateHelper().put(PropertyKeys.name, _name);
        handleAttribute("name", _name);
    }
}