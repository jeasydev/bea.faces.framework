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
package org.primefaces.component.imagecropper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.primefaces.model.CroppedImage;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class ImageCropperRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final ImageCropper cropper = (ImageCropper) component;
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final String coordsParam = cropper.getClientId(context) + "_coords";

        if (params.containsKey(coordsParam)) {
            cropper.setSubmittedValue(params.get(coordsParam));
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ImageCropper cropper = (ImageCropper) component;

        encodeMarkup(context, cropper);
        encodeScript(context, cropper);
    }

    protected void encodeMarkup(final FacesContext context, final ImageCropper cropper) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = cropper.getClientId(context);
        final String coordsHolderId = clientId + "_coords";

        writer.startElement("div", cropper);
        writer.writeAttribute("id", clientId, null);

        renderImage(context, cropper, clientId);

        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", coordsHolderId, null);
        writer.writeAttribute("name", coordsHolderId, null);
        writer.endElement("input");

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext context, final ImageCropper cropper) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String widgetVar = cropper.resolveWidgetVar();
        final String clientId = cropper.getClientId(context);
        final String image = clientId + "_image";
        String select = null;

        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("ImageCropper", widgetVar, clientId, "imagecropper", false).attr("image", image);

        if (cropper.getMinSize() != null) wb.append(",minSize:[").append(cropper.getMinSize()).append("]");

        if (cropper.getMaxSize() != null) wb.append(",maxSize:[").append(cropper.getMaxSize()).append("]");

        wb.attr("bgColor", cropper.getBackgroundColor(), null).attr("bgOpacity", cropper.getBackgroundOpacity(), 0.6)
            .attr("aspectRatio", cropper.getAspectRatio(), Double.MIN_VALUE);

        if (cropper.getValue() != null) {
            final CroppedImage croppedImage = (CroppedImage) cropper.getValue();

            final int x = croppedImage.getLeft();
            final int y = croppedImage.getTop();
            final int x2 = x + croppedImage.getWidth();
            final int y2 = y + croppedImage.getHeight();

            select = "[" + x + "," + y + "," + x2 + "," + y2 + "]";
        } else if (cropper.getInitialCoords() != null) {
            select = "[" + cropper.getInitialCoords() + "]";
        }

        wb.append(",setSelect:").append(select);

        startScript(writer, clientId);
        writer.write("$(PrimeFaces.escapeClientId('" + clientId + "_image')).load(function(){");
        writer.write(wb.build());
        writer.write("});");
        endScript(writer);
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        final String coords = (String) submittedValue;
        if (isValueBlank(coords)) {
            return null;
        }

        final ImageCropper cropper = (ImageCropper) component;

        // remove query string
        String imagePath = cropper.getImage();
        final int queryStringIndex = imagePath.indexOf("?");
        if (queryStringIndex != -1) {
            imagePath = imagePath.substring(0, queryStringIndex);
        }

        final String[] cropCoords = coords.split("_");
        final String format = getFormat(imagePath);

        final int x = Integer.parseInt(cropCoords[0]);
        final int y = Integer.parseInt(cropCoords[1]);
        final int w = Integer.parseInt(cropCoords[2]);
        final int h = Integer.parseInt(cropCoords[3]);

        try {
            final BufferedImage outputImage = getSourceImage(context, imagePath);
            final BufferedImage cropped = outputImage.getSubimage(x, y, w, h);

            final ByteArrayOutputStream croppedOutImage = new ByteArrayOutputStream();
            ImageIO.write(cropped, format, croppedOutImage);

            return new CroppedImage(cropper.getImage(), croppedOutImage.toByteArray(), x, y, w, h);

        } catch (final IOException e) {
            throw new ConverterException(e);
        }
    }

    protected String getFormat(final String path) {
        final String[] pathTokens = path.split("\\.");

        return pathTokens[pathTokens.length - 1];
    }

    private BufferedImage getSourceImage(final FacesContext context, final String imagePath) throws IOException {
        BufferedImage outputImage = null;
        final boolean isExternal = imagePath.startsWith("http");

        if (isExternal) {
            final URL url = new URL(imagePath);

            outputImage = ImageIO.read(url);
        } else {
            final ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();

            outputImage = ImageIO.read(new File(servletContext.getRealPath("") + imagePath));
        }

        return outputImage;
    }

    protected boolean isExternalImage(final ImageCropper cropper) {
        return cropper.getImage().startsWith("http");
    }

    private void renderImage(final FacesContext context, final ImageCropper cropper, final String clientId)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String alt = cropper.getAlt() == null ? "" : cropper.getAlt();

        writer.startElement("img", null);
        writer.writeAttribute("id", clientId + "_image", null);
        writer.writeAttribute("alt", alt, null);
        writer.writeAttribute("src", getResourceURL(context, cropper.getImage()), null);
        writer.endElement("img");
    }
}