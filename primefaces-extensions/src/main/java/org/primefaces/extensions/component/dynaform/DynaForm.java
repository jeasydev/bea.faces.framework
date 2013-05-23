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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.component.base.AbstractDynamicData;
import org.primefaces.extensions.model.common.KeyData;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormModel;

/**
 * <code>DynaForm</code> component.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.css"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js") })
public class DynaForm extends AbstractDynamicData implements Widget {

    /**
     * Properties that are tracked by state saving.
     * 
     * @author Oleg Varaksin / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        widgetVar,
        autoSubmit,
        openExtended,
        buttonBarPosition, // top, bottom, both
        style,
        styleClass;

        private String toString;

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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.DynaForm";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";

    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.DynaFormRenderer";

    private Map<String, UIDynaFormControl> cells;

    public DynaForm() {
        setRendererType(DynaForm.DEFAULT_RENDERER);
    }

    @Override
    protected KeyData findData(final String key) {
        final Object value = getValue();
        if (value == null) {
            return null;
        }

        if (!(value instanceof DynaFormModel)) {
            throw new FacesException("Value in DynaForm must be of type DynaFormModel");
        }

        final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
        for (final DynaFormControl dynaFormControl : dynaFormControls) {
            if (key.equals(dynaFormControl.getKey())) {
                return dynaFormControl;
            }
        }

        return null;
    }

    public String getButtonBarPosition() {
        return (String) getStateHelper().eval(PropertyKeys.buttonBarPosition, "bottom");
    }

    public UIDynaFormControl getControlCell(final String type) {
        final UIDynaFormControl cell = getControlCells().get(type);

        if (cell == null) {
            throw new FacesException("UIDynaFormControl to type " + type + " was not found");
        } else {
            return cell;
        }
    }

    protected Map<String, UIDynaFormControl> getControlCells() {
        if (cells == null) {
            cells = new HashMap<String, UIDynaFormControl>();
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIDynaFormControl) {
                    final UIDynaFormControl dynaFormCell = (UIDynaFormControl) child;
                    cells.put(dynaFormCell.getType(), dynaFormCell);
                }
            }
        }

        return cells;
    }

    @Override
    public String getFamily() {
        return DynaForm.COMPONENT_FAMILY;
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    @Override
    protected boolean invokeOnChildren(final FacesContext context, final String clientId, final ContextCallback callback) {
        final Object value = getValue();
        if (value == null) {
            return false;
        }

        if (!(value instanceof DynaFormModel)) {
            throw new FacesException("Value in DynaForm must be of type DynaFormModel");
        }

        final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
        for (final DynaFormControl dynaFormControl : dynaFormControls) {
            setData(dynaFormControl);

            if (super.invokeOnComponent(context, clientId, callback)) {
                return true;
            }
        }

        resetData();

        return false;
    }

    public boolean isAutoSubmit() {
        return (Boolean) getStateHelper().eval(PropertyKeys.autoSubmit, false);
    }

    public boolean isOpenExtended() {
        return (Boolean) getStateHelper().eval(PropertyKeys.openExtended, false);
    }

    @Override
    protected void processChildren(final FacesContext context, final PhaseId phaseId) {
        final Object value = getValue();
        if (value != null) {
            if (!(value instanceof DynaFormModel)) {
                throw new FacesException("Value in DynaForm must be of type DynaFormModel");
            }

            final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
            for (final DynaFormControl dynaFormControl : dynaFormControls) {
                processDynaFormCells(context, phaseId, dynaFormControl);
            }
        }

        resetData();
    }

    private void processDynaFormCells(final FacesContext context,
                                      final PhaseId phaseId,
                                      final DynaFormControl dynaFormControl) {
        for (final UIComponent kid : getChildren()) {
            if (!(kid instanceof UIDynaFormControl) || !kid.isRendered()
                || !((UIDynaFormControl) kid).getType().equals(dynaFormControl.getType())) {
                continue;
            }

            for (final UIComponent grandkid : kid.getChildren()) {
                if (!grandkid.isRendered()) {
                    continue;
                }

                setData(dynaFormControl);
                if (getData() == null) {
                    return;
                }

                if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                    grandkid.processDecodes(context);
                } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                    grandkid.processValidators(context);
                } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                    grandkid.processUpdates(context);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes().get(PropertyKeys.widgetVar.toString());

        if (userWidgetVar != null) {
            return userWidgetVar;
        }

        return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAttribute(final PropertyKeys property, final Object value) {
        getStateHelper().put(property, value);

        @SuppressWarnings("unchecked")
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(AbstractDynamicData.OPTIMIZED_PACKAGE)) {
                setAttributes = new ArrayList<String>(6);
                getAttributes().put("javax.faces.component.UIComponentBase.attributesThatAreSet", setAttributes);
            }
        }

        if (setAttributes != null && value == null) {
            final String attributeName = property.toString();
            final ValueExpression ve = getValueExpression(attributeName);
            if (ve == null) {
                setAttributes.remove(attributeName);
            } else if (!setAttributes.contains(attributeName)) {
                setAttributes.add(attributeName);
            }
        }
    }

    public void setAutoSubmit(final boolean autoSubmit) {
        setAttribute(PropertyKeys.autoSubmit, autoSubmit);
    }

    public void setButtonBarPosition(final String buttonBarPosition) {
        setAttribute(PropertyKeys.buttonBarPosition, buttonBarPosition);
    }

    public void setOpenExtended(final boolean openExtended) {
        setAttribute(PropertyKeys.openExtended, openExtended);
    }

    public void setStyle(final String style) {
        setAttribute(PropertyKeys.style, style);
    }

    public void setStyleClass(final String styleClass) {
        setAttribute(PropertyKeys.styleClass, styleClass);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }

    @Override
    protected boolean visitChildren(final VisitContext context, final VisitCallback callback) {
        final Object value = getValue();
        if (value == null) {
            return false;
        }

        if (!(value instanceof DynaFormModel)) {
            throw new FacesException("Value in DynaForm must be of type DynaFormModel");
        }

        final List<DynaFormControl> dynaFormControls = ((DynaFormModel) value).getControls();
        for (final DynaFormControl dynaFormControl : dynaFormControls) {
            if (visitDynaFormCells(context, callback, dynaFormControl)) {
                return true;
            }
        }

        resetData();

        return false;
    }

    private boolean visitDynaFormCells(final VisitContext context,
                                       final VisitCallback callback,
                                       final DynaFormControl dynaFormControl) {
        if (getChildCount() > 0) {
            for (final UIComponent child : getChildren()) {
                if (child instanceof UIDynaFormControl
                    && ((UIDynaFormControl) child).getType().equals(dynaFormControl.getType())) {
                    setData(dynaFormControl);
                    if (getData() == null) {
                        return false;
                    }

                    if (child.visitTree(context, callback)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
