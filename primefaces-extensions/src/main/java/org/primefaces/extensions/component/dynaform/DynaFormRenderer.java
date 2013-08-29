/*
 * Copyright 2011-2012 PrimeFaces Extensions.
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
 *
 * $Id$
 */

package org.primefaces.extensions.component.dynaform;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.InputHolder;
import org.primefaces.extensions.model.dynaform.AbstractDynaFormElement;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.renderkit.CoreRenderer;

/**
 * Renderer for {@link DynaForm} component.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormRenderer extends CoreRenderer {

    private static final Logger LOGGER = Logger.getLogger(DynaFormRenderer.class.getName());

    private static final String FACET_HEADER_REGULAR = "headerRegular";
    private static final String FACET_FOOTER_REGULAR = "footerRegular";
    private static final String FACET_HEADER_EXTENDED = "headerExtended";
    private static final String FACET_FOOTER_EXTENDED = "footerExtended";
    private static final String FACET_BUTTON_BAR = "buttonBar";

    private static final String GRID_CLASS = "pe-dynaform-grid";
    private static final String CELL_CLASS = "pe-dynaform-cell";
    private static final String CELL_FIRST_CLASS = "pe-dynaform-cell-first";
    private static final String CELL_LAST_CLASS = "pe-dynaform-cell-last";
    private static final String LABEL_CLASS = "pe-dynaform-label";
    private static final String LABEL_INVALID_CLASS = "ui-state-error ui-corner-all";
    private static final String LABEL_INDICATOR_CLASS = "pe-dynaform-label-rfi";

    private static final String FACET_BUTTON_BAR_TOP_CLASS = "pe-dynaform-buttonbar-top";
    private static final String FACET_BUTTON_BAR_BOTTOM_CLASS = "pe-dynaform-buttonbar-bottom";
    private static final String FACET_HEADER_CLASS = "pe-dynaform-headerfacet";
    private static final String FACET_FOOTER_CLASS = "pe-dynaform-footerfacet";
    private static final String EXTENDED_ROW_CLASS = "pe-dynaform-extendedrow";

    private static final String BUTTON_BAR_ROLE = "toolbar";
    private static final String GRID_CELL_ROLE = "gridcell";

    protected void encodeBody(final FacesContext fc,
                              final DynaForm dynaForm,
                              final List<DynaFormRow> dynaFormRows,
                              final boolean extended,
                              final boolean visible) throws IOException {
        if (dynaFormRows == null || dynaFormRows.isEmpty()) {
            return;
        }

        final ResponseWriter writer = fc.getResponseWriter();

        for (final DynaFormRow dynaFormRow : dynaFormRows) {
            writer.startElement("div", null);
            if (extended) {
                writer.writeAttribute("class", DynaFormRenderer.EXTENDED_ROW_CLASS, null);
            }

            if (!visible) {
                writer.writeAttribute("style", "display:none;", null);
            }

            final List<AbstractDynaFormElement> elements = dynaFormRow.getElements();
            final int size = elements.size();

            for (int i = 0; i < size; i++) {
                final AbstractDynaFormElement element = elements.get(i);

                if (element.getColspan() > 1) {
                    writer.writeAttribute("colspan", element.getColspan(), null);
                }

                if (element.getRowspan() > 1) {
                    writer.writeAttribute("rowspan", element.getRowspan(), null);
                }

                String styleClass = DynaFormRenderer.CELL_CLASS;
                if (i == 0 && element.getColspan() == 1) {
                    styleClass = styleClass + " " + DynaFormRenderer.CELL_FIRST_CLASS;
                }

                if (i == size - 1 && element.getColspan() == 1) {
                    styleClass = styleClass + " " + DynaFormRenderer.CELL_LAST_CLASS;
                }

                if (element instanceof DynaFormLabel) {
                    // render label
                    final DynaFormLabel label = (DynaFormLabel) element;

                    writer.writeAttribute("class", styleClass + " " + DynaFormRenderer.LABEL_CLASS, null);
                    writer.writeAttribute("role", DynaFormRenderer.GRID_CELL_ROLE, null);

                    writer.startElement("label", null);
                    if (!label.isTargetValid()) {
                        writer.writeAttribute("class", DynaFormRenderer.LABEL_INVALID_CLASS, null);
                    }

                    writer.writeAttribute("for", label.getTargetClientId(), null);

                    if (label.getValue() != null) {
                        if (label.isEscape()) {
                            writer.writeText(label.getValue(), "value");
                        } else {
                            writer.write(label.getValue());
                        }
                    }

                    if (label.isTargetRequired()) {
                        writer.startElement("span", null);
                        writer.writeAttribute("class", DynaFormRenderer.LABEL_INDICATOR_CLASS, null);
                        writer.write("*");
                        writer.endElement("span");
                    }

                    writer.endElement("label");
                } else {
                    // render control
                    final DynaFormControl control = (DynaFormControl) element;
                    dynaForm.setData(control);

                    // find control's cell by type
                    final UIDynaFormControl cell = dynaForm.getControlCell(control.getType());

                    if (cell.getStyleClass() != null) {
                        styleClass = styleClass + " " + cell.getStyleClass();
                    }

                    writer.writeAttribute("class", styleClass, null);
                    writer.writeAttribute("role", DynaFormRenderer.GRID_CELL_ROLE, null);

                    cell.encodeAll(fc);
                }

            }

            writer.endElement("div");
        }

        dynaForm.resetData();
    }

    @Override
    public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
        // Rendering happens on encodeEnd
    }

    @Override
    public void encodeEnd(final FacesContext fc, final UIComponent component) throws IOException {
        final DynaForm dynaForm = (DynaForm) component;

        // get model
        final DynaFormModel dynaFormModel = (DynaFormModel) dynaForm.getValue();
        encodeMarkup(fc, dynaForm, dynaFormModel);
        encodeScript(fc, dynaForm, dynaFormModel);
    }

    protected void encodeFacet(final FacesContext fc,
                               final DynaForm dynaForm,
                               final String name,
                               final int totalColspan,
                               final String styleClass,
                               final String role,
                               final boolean extended,
                               final boolean visible) throws IOException {
        final UIComponent facet = dynaForm.getFacet(name);
        if (facet != null && facet.isRendered()) {
            final ResponseWriter writer = fc.getResponseWriter();
            writer.startElement("div", null);
            if (extended) {
                writer.writeAttribute("class", DynaFormRenderer.EXTENDED_ROW_CLASS, null);
            }

            if (!visible) {
                writer.writeAttribute("style", "display:none;", null);
            }

            if (totalColspan > 1) {
                writer.writeAttribute("colspan", totalColspan, null);
            }


            facet.encodeAll(fc);

            writer.endElement("div");
        }
    }

    protected void encodeMarkup(final FacesContext fc, final DynaForm dynaForm, final DynaFormModel dynaFormModel)
        throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = dynaForm.getClientId(fc);

        final String styleClass = (dynaForm.getStyleClass() == null
                                                                   ? DynaFormRenderer.GRID_CLASS
                                                                   : DynaFormRenderer.GRID_CLASS + " "
                                                                       + dynaForm.getStyleClass());

        writer.startElement("div", dynaForm);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", styleClass, "styleClass");
        if (dynaForm.getStyle() != null) {
            writer.writeAttribute("style", dynaForm.getStyle(), "style");
        }

        // prepare labels with informations about corresponding control
        // components
        preRenderLabel(fc, dynaForm, dynaFormModel);

        final int totalColspan = getTotalColspan(dynaFormModel);
        final String bbPosition = dynaForm.getButtonBarPosition();

        if ("top".equals(bbPosition) || "both".equals(bbPosition)) {
            encodeFacet(fc, dynaForm, DynaFormRenderer.FACET_BUTTON_BAR, totalColspan,
                        DynaFormRenderer.FACET_BUTTON_BAR_TOP_CLASS, DynaFormRenderer.BUTTON_BAR_ROLE, false, true);
        }

        encodeFacet(fc, dynaForm, DynaFormRenderer.FACET_HEADER_REGULAR, totalColspan,
                    DynaFormRenderer.FACET_HEADER_CLASS, DynaFormRenderer.GRID_CELL_ROLE, false, true);

        // encode regular grid
        encodeBody(fc, dynaForm, dynaFormModel.getRegularRows(), false, true);

        encodeFacet(fc, dynaForm, DynaFormRenderer.FACET_FOOTER_REGULAR, totalColspan,
                    DynaFormRenderer.FACET_FOOTER_CLASS, DynaFormRenderer.GRID_CELL_ROLE, false, true);
        encodeFacet(fc, dynaForm, DynaFormRenderer.FACET_HEADER_EXTENDED, totalColspan,
                    DynaFormRenderer.FACET_HEADER_CLASS, DynaFormRenderer.GRID_CELL_ROLE, true, dynaForm
                        .isOpenExtended());

        // encode extended grid
        encodeBody(fc, dynaForm, dynaFormModel.getExtendedRows(), true, dynaForm.isOpenExtended());

        encodeFacet(fc, dynaForm, DynaFormRenderer.FACET_FOOTER_EXTENDED, totalColspan,
                    DynaFormRenderer.FACET_FOOTER_CLASS, DynaFormRenderer.GRID_CELL_ROLE, true, dynaForm
                        .isOpenExtended());

        if ("bottom".equals(bbPosition) || "both".equals(bbPosition)) {
            encodeFacet(fc, dynaForm, DynaFormRenderer.FACET_BUTTON_BAR, totalColspan,
                        DynaFormRenderer.FACET_BUTTON_BAR_BOTTOM_CLASS, DynaFormRenderer.BUTTON_BAR_ROLE, false, true);
        }

        writer.endElement("div");
    }

    protected void encodeScript(final FacesContext fc, final DynaForm dynaForm, final DynaFormModel dynaFormModel)
        throws IOException {
        final ResponseWriter writer = fc.getResponseWriter();
        final String clientId = dynaForm.getClientId(fc);
        final String widgetVar = dynaForm.resolveWidgetVar();

        startScript(writer, clientId);
        writer.write("$(function() {");
        writer.write("PrimeFacesExt.cw('DynaForm','" + widgetVar + "',{");
        writer.write("id:'" + clientId + "'");
        writer.write(",widgetVar:'" + widgetVar + "'");
        writer.write(",uuid:'" + dynaFormModel.getUuid() + "'");
        writer.write(",autoSubmit:" + dynaForm.isAutoSubmit());
        writer.write(",isPostback:" + fc.isPostback());
        writer.write("});});");
        endScript(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    protected int getTotalColspan(final DynaFormModel dynaFormModel) {
        // the main aim of this method is layout check
        int totalColspan = -1;
        for (final DynaFormRow dynaFormRow : dynaFormModel.getRegularRows()) {
            if (dynaFormRow.getTotalColspan() > totalColspan) {
                totalColspan = dynaFormRow.getTotalColspan();
            }
        }

        if (dynaFormModel.getExtendedRows() != null) {
            for (final DynaFormRow dynaFormRow : dynaFormModel.getExtendedRows()) {
                if (dynaFormRow.getTotalColspan() > totalColspan) {
                    totalColspan = dynaFormRow.getTotalColspan();
                }
            }
        }

        if (totalColspan < 1) {
            totalColspan = 1;
        }

        return totalColspan;
    }

    protected void preRenderLabel(final FacesContext fc, final DynaForm dynaForm, final DynaFormModel dynaFormModel) {
        for (final DynaFormLabel dynaFormLabel : dynaFormModel.getLabels()) {
            // get corresponding control if any
            final DynaFormControl control = dynaFormLabel.getForControl();
            if (control != null) {
                // find control's cell by type
                final UIDynaFormControl cell = dynaForm.getControlCell(control.getType());

                if (cell.getFor() != null) {
                    dynaForm.setData(control);

                    final UIComponent target = cell.findComponent(cell.getFor());
                    if (target == null) {
                        DynaFormRenderer.LOGGER.warning("Cannot find component with identifier " + cell.getFor()
                            + " inside UIDynaFormControl");

                        continue;
                    }

                    final String targetClientId = (target instanceof InputHolder) ? ((InputHolder) target)
                        .getInputClientId() : target.getClientId(fc);

                    dynaFormLabel.setTargetClientId(targetClientId);
                    dynaFormLabel.setTargetValid(((EditableValueHolder) target).isValid());
                    dynaFormLabel.setTargetRequired(((EditableValueHolder) target).isRequired());

                    if (dynaFormLabel.getValue() != null) {
                        target.getAttributes().put("label", dynaFormLabel.getValue());
                    }
                }
            }
        }

        dynaForm.resetData();
    }
}
