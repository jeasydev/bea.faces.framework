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

package org.primefaces.extensions.component.inputnumber;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.Widget;

/**
 * InputNumber
 * 
 * @author Mauricio Fenoglio / last modified by $Author$
 * @version $Revision$
 * @since 0.3
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "primefaces.css"), @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js"), @ResourceDependency(library = "primefaces-extensions", name = "inputnumber/inputnumber.js") })
public class InputNumber extends HtmlInputText implements Widget {

    /**
     * PropertyKeys
     * 
     * @author Mauricio Fenoglio / last modified by $Author: fenoloco@gmail.com
     *         $
     * @version $Revision$
     * @since 0.3
     */
    protected enum PropertyKeys {

        widgetVar,
        decimalSeparator,
        thousandSeparator,
        symbol,
        symbolPosition,
        minValue,
        maxValue,
        roundMethod,
        decimalPlaces,
        emptyValue;
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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.InputNumber";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.InputNumberRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";
    public final static String INPUTNUMBER_CLASS = "ui-inputNum ui-widget";
    private String decimalSeparator;

    private String thousandSeparator;

    public InputNumber() {
        setRendererType(InputNumber.DEFAULT_RENDERER);
        decimalSeparator = null;
        thousandSeparator = null;
    }

    private String getCalculatedDecimalSepartor() {
        if (decimalSeparator == null) {
            final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);
            decimalSeparator = Character.toString(decimalFormatSymbols.getDecimalSeparator());
        }
        return decimalSeparator;
    }

    private String getCalculatedThousandSeparator() {
        if (thousandSeparator == null) {
            final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);
            thousandSeparator = Character.toString(decimalFormatSymbols.getGroupingSeparator());
        }
        return thousandSeparator;
    }

    public String getDecimalPlaces() {
        return (String) getStateHelper().eval(PropertyKeys.decimalPlaces, "");
    }

    public String getDecimalSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.decimalSeparator, getCalculatedDecimalSepartor());
    }

    public String getEmptyValue() {
        return (String) getStateHelper().eval(PropertyKeys.emptyValue, "empty");
    }

    @Override
    public String getFamily() {
        return InputNumber.COMPONENT_FAMILY;
    }

    public String getMaxValue() {
        return (String) getStateHelper().eval(PropertyKeys.maxValue, "");
    }

    public String getMinValue() {
        return (String) getStateHelper().eval(PropertyKeys.minValue, "");
    }

    public String getRoundMethod() {
        return (String) getStateHelper().eval(PropertyKeys.roundMethod, "");
    }

    public String getSymbol() {
        return (String) getStateHelper().eval(PropertyKeys.symbol, "");
    }

    public String getSymbolPosition() {
        return (String) getStateHelper().eval(PropertyKeys.symbolPosition, "");
    }

    public String getThousandSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.thousandSeparator, getCalculatedThousandSeparator());
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    @Override
    public String resolveWidgetVar() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String userWidgetVar = (String) getAttributes()
            .get(org.primefaces.extensions.component.inputnumber.InputNumber.PropertyKeys.widgetVar.toString());

        if (userWidgetVar != null) {
            return userWidgetVar;
        }

        return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
    }

    public void setAttribute(final org.primefaces.extensions.component.inputnumber.InputNumber.PropertyKeys property,
                             final Object value) {
        getStateHelper().put(property, value);

        @SuppressWarnings("unchecked")
        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(InputNumber.OPTIMIZED_PACKAGE)) {
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

    public void setDecimalPlaces(final String decimalPlaces) {
        setAttribute(PropertyKeys.decimalPlaces, decimalPlaces);
    }

    public void setDecimalSeparator(final String decimalSeparator) {
        setAttribute(PropertyKeys.decimalSeparator, decimalSeparator);
    }

    public void setEmptyValue(final String emptyValue) {
        setAttribute(PropertyKeys.emptyValue, emptyValue);
    }

    public void setMaxValue(final String maxValue) {
        setAttribute(PropertyKeys.maxValue, maxValue);
    }

    public void setMinValue(final String minValue) {
        setAttribute(PropertyKeys.minValue, minValue);
    }

    public void setRoundMethod(final String roundMethod) {
        setAttribute(PropertyKeys.roundMethod, roundMethod);
    }

    public void setSymbol(final String symbol) {
        setAttribute(PropertyKeys.symbol, symbol);
    }

    public void setSymbolPosition(final String symbolPosition) {
        setAttribute(PropertyKeys.symbolPosition, symbolPosition);
    }

    public void setThousandSeparator(final String thousandSeparator) {
        setAttribute(PropertyKeys.thousandSeparator, thousandSeparator);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }
}
