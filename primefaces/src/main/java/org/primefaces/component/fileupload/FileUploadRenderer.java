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
package org.primefaces.component.fileupload;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletRequestWrapper;
import org.apache.commons.fileupload.FileItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;
import org.primefaces.util.WidgetBuilder;
import org.primefaces.webapp.MultipartRequest;

public class FileUploadRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final FileUpload fileUpload = (FileUpload) component;
        final String clientId = fileUpload.getClientId(context);
        final MultipartRequest multipartRequest = getMultiPartRequestInChain(context);

        if (multipartRequest != null) {
            final FileItem file = multipartRequest.getFileItem(clientId);

            if (fileUpload.getMode().equals("simple")) {
                decodeSimple(context, fileUpload, file);
            } else {
                decodeAdvanced(context, fileUpload, file);
            }
        }
    }

    public void decodeAdvanced(final FacesContext context, final FileUpload fileUpload, final FileItem file) {
        if (file != null) {
            fileUpload.queueEvent(new FileUploadEvent(fileUpload, new DefaultUploadedFile(file)));
        }
    }

    public void decodeSimple(final FacesContext context, final FileUpload fileUpload, final FileItem file) {
        if (file.getName().equals(""))
            fileUpload.setSubmittedValue("");
        else fileUpload.setSubmittedValue(new DefaultUploadedFile(file));
    }

    protected void encodeAdvancedMarkup(final FacesContext context, final FileUpload fileUpload) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = fileUpload.getClientId(context);
        String styleClass = fileUpload.getStyleClass();
        styleClass = styleClass == null ? FileUpload.CONTAINER_CLASS : FileUpload.CONTAINER_CLASS + " " + styleClass;

        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "id");
        if (fileUpload.getStyle() != null) writer.writeAttribute("style", fileUpload.getStyle(), "style");

        // buttonbar
        writer.startElement("div", fileUpload);
        writer.writeAttribute("class", FileUpload.BUTTON_BAR_CLASS, "styleClass");

        // choose button
        encodeChooseButton(context, fileUpload);

        if (fileUpload.isShowButtons() && !fileUpload.isAuto()) {
            encodeButton(context, fileUpload.getUploadLabel(), FileUpload.UPLOAD_BUTTON_CLASS,
                         "ui-icon-arrowreturnthick-1-n");
            encodeButton(context, fileUpload.getCancelLabel(), FileUpload.CANCEL_BUTTON_CLASS, "ui-icon-cancel");
        }

        writer.endElement("div");

        // content
        writer.startElement("div", null);
        writer.writeAttribute("class", FileUpload.CONTENT_CLASS, null);

        writer.startElement("table", null);
        writer.writeAttribute("class", FileUpload.FILES_CLASS, null);
        writer.endElement("table");

        writer.endElement("div");

        writer.endElement("div");
    }

    protected void encodeButton(final FacesContext context,
                                final String label,
                                final String styleClass,
                                final String icon) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("button", null);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS + " " + styleClass, null);

        // button icon
        final String iconClass = HTML.BUTTON_LEFT_ICON_CLASS;
        writer.startElement("span", null);
        writer.writeAttribute("class", iconClass + " " + icon, null);
        writer.endElement("span");

        // text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.writeText(label, "value");
        writer.endElement("span");

        writer.endElement("button");
    }

    protected void encodeChooseButton(final FacesContext context, final FileUpload fileUpload) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = fileUpload.getClientId(context);

        writer.startElement("label", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS + " " + FileUpload.CHOOSE_BUTTON_CLASS,
                              null);

        // button icon
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_LEFT_ICON_CLASS + " ui-icon-plusthick", null);
        writer.endElement("span");

        // text
        writer.startElement("span", null);
        writer.writeAttribute("class", HTML.BUTTON_TEXT_CLASS, null);
        writer.writeText(fileUpload.getLabel(), "value");
        writer.endElement("span");

        encodeInputField(context, fileUpload, clientId + "_input");

        writer.endElement("label");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final FileUpload fileUpload = (FileUpload) component;

        encodeMarkup(context, fileUpload);
        encodeScript(context, fileUpload);
    }

    protected void encodeInputField(final FacesContext context, final FileUpload fileUpload, final String clientId)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("input", null);
        writer.writeAttribute("type", "file", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);

        if (fileUpload.isMultiple()) writer.writeAttribute("multiple", "multiple", null);
        if (fileUpload.getStyle() != null) writer.writeAttribute("style", fileUpload.getStyle(), "style");
        if (fileUpload.getStyleClass() != null)
            writer.writeAttribute("class", fileUpload.getStyleClass(), "styleClass");
        if (fileUpload.isDisabled()) writer.writeAttribute("disabled", "disabled", "disabled");

        writer.endElement("input");
    }

    protected void encodeMarkup(final FacesContext context, final FileUpload fileUpload) throws IOException {
        if (fileUpload.getMode().equals("simple"))
            encodeSimpleMarkup(context, fileUpload);
        else encodeAdvancedMarkup(context, fileUpload);
    }

    protected void encodeScript(final FacesContext context, final FileUpload fileUpload) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = fileUpload.getClientId(context);
        final String mode = fileUpload.getMode();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("FileUpload", fileUpload.resolveWidgetVar(), clientId, "fileupload", true).attr("mode", mode);

        if (!mode.equals("simple")) {
            final String update = fileUpload.getUpdate();
            final String process = fileUpload.getProcess();

            wb.attr("autoUpload", fileUpload.isAuto()).attr("dnd", fileUpload.isDragDropSupport())
                .attr("update", ComponentUtils.findClientIds(context, fileUpload, update), null)
                .attr("process", ComponentUtils.findClientIds(context, fileUpload, process), null)
                .attr("maxFileSize", fileUpload.getSizeLimit(), Integer.MAX_VALUE).attr("invalidFileMessage",
                                                                                        fileUpload
                                                                                            .getInvalidFileMessage(),
                                                                                        null)
                .attr("invalidSizeMessage", fileUpload.getInvalidSizeMessage(), null).callback("onstart", "function()",
                                                                                               fileUpload.getOnstart())
                .callback("oncomplete", "function()", fileUpload.getOncomplete());

            if (fileUpload.getAllowTypes() != null) {
                wb.append(",acceptFileTypes:").append(fileUpload.getAllowTypes());
            }
        }

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeSimpleMarkup(final FacesContext context, final FileUpload fileUpload) throws IOException {
        encodeInputField(context, fileUpload, fileUpload.getClientId(context));
    }

    /**
     * Return null if no file is submitted in simple mode
     * 
     * @param context
     * @param component
     * @param submittedValue
     * @return
     * @throws ConverterException
     */
    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        final FileUpload fileUpload = (FileUpload) component;

        if (fileUpload.getMode().equals("simple") && submittedValue != null && submittedValue.equals("")) {
            return null;
        } else {
            return submittedValue;
        }
    }

    /**
     * Finds our MultipartRequestServletWrapper in case application contains
     * other RequestWrappers
     */
    private MultipartRequest getMultiPartRequestInChain(final FacesContext facesContext) {
        Object request = facesContext.getExternalContext().getRequest();

        while (request instanceof ServletRequestWrapper) {
            if (request instanceof MultipartRequest) {
                return (MultipartRequest) request;
            } else {
                request = ((ServletRequestWrapper) request).getRequest();
            }
        }

        return null;
    }
}