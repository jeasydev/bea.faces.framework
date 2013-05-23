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
package org.primefaces.component.media;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.media.player.MediaPlayer;
import org.primefaces.component.media.player.MediaPlayerFactory;
import org.primefaces.model.StreamedContent;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.AgentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.HTML;

public class MediaRenderer extends CoreRenderer {

    protected String createUniqueContentId(final FacesContext context) {
        final Map<String, Object> session = context.getExternalContext().getSessionMap();

        String key = generateKey();

        while (session.containsKey(key)) {
            key = generateKey();
        }

        return key;
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Do nothing
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final Media media = (Media) component;
        final MediaPlayer player = resolvePlayer(context, media);
        final ResponseWriter writer = context.getResponseWriter();
        final String src = getMediaSrc(context, media);
        final boolean isIE = AgentUtils.isIE(context);
        final String sourceParam = player.getSourceParam();

        writer.startElement("object", media);
        writer.writeAttribute("type", player.getType(), null);
        writer.writeAttribute("data", src, null);

        if (isIE) {
            encodeIEConfig(writer, player);
        }

        if (media.getStyleClass() != null) {
            writer.writeAttribute("class", media.getStyleClass(), null);
        }

        renderPassThruAttributes(context, media, HTML.MEDIA_ATTRS);

        if (sourceParam != null) {
            encodeParam(writer, player.getSourceParam(), src, false);
        }

        for (final UIComponent child : media.getChildren()) {
            if (child instanceof UIParameter) {
                final UIParameter param = (UIParameter) child;

                encodeParam(writer, param.getName(), param.getValue(), false);
            }
        }

        renderChildren(context, media);

        writer.endElement("object");
    }

    protected void encodeIEConfig(final ResponseWriter writer, final MediaPlayer player) throws IOException {
        writer.writeAttribute("classid", player.getClassId(), null);

        if (player.getCodebase() != null) {
            writer.writeAttribute("codebase", player.getCodebase(), null);
        }
    }

    protected void encodeParam(final ResponseWriter writer,
                               final String name,
                               final Object value,
                               final boolean asAttribute) throws IOException {
        if (value == null) return;

        if (asAttribute) {
            writer.writeAttribute(name, value, null);
        } else {
            writer.startElement("param", null);
            writer.writeAttribute("name", name, null);
            writer.writeAttribute("value", value.toString(), null);
            writer.endElement("param");
        }
    }

    protected String generateKey() {
        final StringBuilder builder = new StringBuilder();

        return builder.append(Constants.DYNAMIC_CONTENT_PARAM).append("_").append(UUID.randomUUID().toString())
            .toString();
    }

    protected String getMediaSrc(final FacesContext context, final Media media) {
        String src;
        final Object value = media.getValue();

        if (value == null) {
            src = null;
        } else {
            if (value instanceof StreamedContent) {
                final StreamedContent streamedContent = (StreamedContent) value;
                final Resource resource = context.getApplication().getResourceHandler()
                    .createResource("dynamiccontent.properties", "primefaces", streamedContent.getContentType());
                final String resourcePath = resource.getRequestPath();
                final String rid = createUniqueContentId(context);
                final StringBuilder builder = new StringBuilder(resourcePath);

                builder.append("&").append(Constants.DYNAMIC_CONTENT_PARAM).append("=").append(rid);

                for (final UIComponent kid : media.getChildren()) {
                    if (kid instanceof UIParameter) {
                        final UIParameter param = (UIParameter) kid;

                        builder.append("&").append(param.getName()).append("=").append(param.getValue());
                    }
                }

                src = builder.toString();

                context.getExternalContext().getSessionMap().put(rid,
                                                                 media.getValueExpression("value")
                                                                     .getExpressionString());
            } else {
                src = getResourceURL(context, (String) value);

                if (src.startsWith("/")) {
                    src = context.getExternalContext().encodeResourceURL(src);
                }
            }
        }

        return src;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    protected MediaPlayer resolvePlayer(final FacesContext context, final Media media) {
        if (media.getPlayer() != null) {
            return MediaPlayerFactory.getPlayer(media.getPlayer());
        } else if (media.getValue() instanceof String) {
            final Map<String, MediaPlayer> players = MediaPlayerFactory.getPlayers();
            final String[] tokens = ((String) media.getValue()).split("\\.");
            final String type = tokens[tokens.length - 1];

            for (final MediaPlayer mp : players.values()) {
                for (final String supportedType : mp.getSupportedTypes()) {
                    if (supportedType.equalsIgnoreCase(type)) {
                        return mp;
                    }
                }
            }
        }

        throw new IllegalArgumentException("Cannot resolve mediaplayer for media component '"
            + media.getClientId(context) + "', cannot play source:" + media.getValue());
    }
}