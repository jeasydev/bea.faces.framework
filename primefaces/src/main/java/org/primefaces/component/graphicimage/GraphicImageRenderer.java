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

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.model.StreamedContent;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.Constants;
import org.primefaces.util.HTML;

public class GraphicImageRenderer extends CoreRenderer {

    private final static Logger logger = Logger.getLogger(GraphicImageRenderer.class.getName());

    protected String createUniqueContentId(final FacesContext context) {
        final Map<String, Object> session = context.getExternalContext().getSessionMap();

        String key = generateKey();

        while (session.containsKey(key)) {
            key = generateKey();
        }

        return key;
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final GraphicImage image = (GraphicImage) component;
        final String clientId = image.getClientId(context);
        final String imageSrc = getImageSrc(context, image);

        writer.startElement("img", image);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("src", imageSrc, null);

        if (image.getAlt() == null) writer.writeAttribute("alt", "", null);
        if (image.getStyleClass() != null) writer.writeAttribute("class", image.getStyleClass(), "styleClass");

        renderPassThruAttributes(context, image, HTML.IMG_ATTRS);

        writer.endElement("img");
    }

    protected String generateKey() {
        final StringBuilder builder = new StringBuilder();

        return builder.append(Constants.DYNAMIC_CONTENT_PARAM).append("_").append(UUID.randomUUID().toString())
            .toString();
    }

    protected String getImageSrc(final FacesContext context, final GraphicImage image) {
        String src = null;
        final String name = image.getName();

        if (name != null) {
            final String libName = image.getLibrary();
            final ResourceHandler handler = context.getApplication().getResourceHandler();
            final Resource res = handler.createResource(name, libName);

            if (res == null) {
                return "RES_NOT_FOUND";
            } else {
                final String requestPath = res.getRequestPath();

                return context.getExternalContext().encodeResourceURL(requestPath);
            }
        } else {
            final Object value = image.getValue();

            if (value == null) {
                return "";
            } else if (value instanceof String) {
                src = getResourceURL(context, (String) value);
            } else if (value instanceof StreamedContent) {
                final StreamedContent streamedContent = (StreamedContent) value;
                final Resource resource = context.getApplication().getResourceHandler()
                    .createResource("dynamiccontent.properties", "primefaces", streamedContent.getContentType());
                final String resourcePath = resource.getRequestPath();
                final String rid = createUniqueContentId(context);
                final StringBuilder builder = new StringBuilder(resourcePath);

                builder.append("&").append(Constants.DYNAMIC_CONTENT_PARAM).append("=").append(rid);

                for (final UIComponent kid : image.getChildren()) {
                    if (kid instanceof UIParameter) {
                        final UIParameter param = (UIParameter) kid;

                        builder.append("&").append(param.getName()).append("=").append(param.getValue());
                    }
                }

                src = builder.toString();

                context.getExternalContext().getSessionMap().put(rid,
                                                                 image.getValueExpression("value")
                                                                     .getExpressionString());
            }

            if (!image.isCache()) {
                src += src.contains("?") ? "&" : "?";
                src += "primefaces_image=" + UUID.randomUUID().toString();
            }

            src = context.getExternalContext().encodeResourceURL(src);
        }

        return src;
    }
}