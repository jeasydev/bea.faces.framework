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
package org.primefaces.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UniqueIdVendor;
import javax.faces.component.ValueHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.validator.BeanValidator;
import org.primefaces.component.api.RTLAware;
import org.primefaces.component.api.Widget;

public class ComponentUtils {

    public static boolean considerEmptyStringAsNull(final FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        final String value = externalContext.getInitParameter(Constants.INTERPRET_EMPTY_STRING_AS_NULL);

        return (value == null) ? false : Boolean.valueOf(value);
    }

    public static List<SelectItem> createSelectItems(final UIComponent component) {
        final List<SelectItem> items = new ArrayList<SelectItem>();
        final Iterator<UIComponent> children = component.getChildren().iterator();

        while (children.hasNext()) {
            final UIComponent child = children.next();

            if (child instanceof UISelectItem) {
                final UISelectItem selectItem = (UISelectItem) child;

                items.add(new SelectItem(selectItem.getItemValue(), selectItem.getItemLabel()));
            } else if (child instanceof UISelectItems) {
                final Object selectItems = ((UISelectItems) child).getValue();

                if (selectItems instanceof SelectItem[]) {
                    final SelectItem[] itemsArray = (SelectItem[]) selectItems;

                    for (final SelectItem item : itemsArray)
                        items.add(new SelectItem(item.getValue(), item.getLabel()));

                } else if (selectItems instanceof Collection) {
                    final Collection<SelectItem> collection = (Collection<SelectItem>) selectItems;

                    for (final SelectItem item : collection)
                        items.add(new SelectItem(item.getValue(), item.getLabel()));
                }
            }
        }

        return items;
    }

    public static void decorateAttribute(final UIComponent component, final String attribute, final String value) {
        final String attributeValue = (String) component.getAttributes().get(attribute);

        if (attributeValue != null) {
            if (attributeValue.indexOf(value) == -1) {
                final String decoratedValue = attributeValue + ";" + value;

                component.getAttributes().put(attribute, decoratedValue);
            } else {
                component.getAttributes().put(attribute, attributeValue);
            }
        } else {
            component.getAttributes().put(attribute, value);
        }
    }

    public static String escapeJQueryId(final String id) {
        return "#" + id.replaceAll(":", "\\\\\\\\:");
    }

    public static String findClientIds(final FacesContext context, final UIComponent component, final String list) {
        if (list == null) {
            return "@none";
        }

        final String[] ids = list.split("[,\\s]+");
        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < ids.length; i++) {
            if (i != 0) {
                buffer.append(" ");
            }

            final String id = ids[i].trim();

            if (id.equals("@all") || id.equals("@none")) {
                buffer.append(id);
            } else if (id.equals("@this")) {
                buffer.append(component.getClientId(context));
            } else if (id.equals("@form")) {
                final UIComponent form = ComponentUtils.findParentForm(context, component);
                if (form == null) {
                    throw new FacesException("Component " + component.getClientId(context)
                        + " needs to be enclosed in a form");
                }

                buffer.append(form.getClientId(context));
            } else if (id.equals("@parent")) {
                buffer.append(component.getParent().getClientId(context));
            } else if (id.equals("@namingcontainer")) {
                final UIComponent container = ComponentUtils.findParentNamingContainer(component);

                if (container != null) {
                    buffer.append(container.getClientId(context));
                }
            } else {
                final UIComponent comp = component.findComponent(id);
                if (comp != null) {
                    buffer.append(comp.getClientId(context));
                } else {
                    throw new FacesException("Cannot find component with identifier \"" + id + "\" referenced from \""
                        + component.getClientId(context) + "\".");
                }
            }
        }

        return buffer.toString();
    }

    public static UIComponent findComponent(final UIComponent base, final String id) {
        if (id.equals(base.getId())) return base;

        UIComponent kid = null;
        UIComponent result = null;
        final Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            kid = kids.next();
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = ComponentUtils.findComponent(kid, id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public static String findComponentClientId(final String id) {
        UIComponent component = null;

        final FacesContext facesContext = FacesContext.getCurrentInstance();
        component = ComponentUtils.findComponent(facesContext.getViewRoot(), id);

        return component.getClientId(facesContext);
    }

    public static UIComponent findParentForm(final FacesContext context, final UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof UIForm) {
                return parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    public static UIComponent findParentNamingContainer(final UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof NamingContainer) {
                return parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    public static UniqueIdVendor findParentUniqueIdVendor(final UIComponent component) {
        UIComponent parent = component.getParent();

        while (parent != null) {
            if (parent instanceof UniqueIdVendor) {
                return (UniqueIdVendor) parent;
            }

            parent = parent.getParent();
        }

        return null;
    }

    /**
     * Finds appropriate converter for a given value holder
     * 
     * @param context FacesContext instance
     * @param component ValueHolder instance to look converter for
     * @return Converter
     */
    public static Converter getConverter(final FacesContext context, final ValueHolder component) {
        // explicit converter
        Converter converter = component.getConverter();

        // try to find implicit converter
        if (converter == null) {
            final ValueExpression expr = ((UIComponent) component).getValueExpression("value");
            if (expr != null) {
                final Class<?> valueType = expr.getType(context.getELContext());
                if (valueType != null) {
                    converter = context.getApplication().createConverter(valueType);
                }
            }
        }

        return converter;
    }

    /**
     * Algorithm works as follows; - If it's an input component, submitted value
     * is checked first since it'd be the value to be used in case validation
     * errors terminates jsf lifecycle - Finally the value of the component is
     * retrieved from backing bean and if there's a converter, converted value
     * is returned
     * 
     * @param context FacesContext instance
     * @param component UIComponent instance whose value will be returned
     * @return End text
     */
    public static String getValueToRender(final FacesContext context, final UIComponent component) {
        if (component instanceof ValueHolder) {

            if (component instanceof EditableValueHolder) {
                final EditableValueHolder input = (EditableValueHolder) component;
                final Object submittedValue = input.getSubmittedValue();

                if (ComponentUtils.considerEmptyStringAsNull(context) && submittedValue == null
                    && context.isValidationFailed() && !input.isValid()) {
                    return null;
                } else if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            final ValueHolder valueHolder = (ValueHolder) component;
            final Object value = valueHolder.getValue();

            // format the value as string
            if (value != null) {
                final Converter converter = ComponentUtils.getConverter(context, valueHolder);

                if (converter != null)
                    return converter.getAsString(context, component, value);
                else return value.toString(); // Use toString as a fallback if
                                              // there is no explicit or
                                              // implicit converter

            } else {
                // component is a value holder but has no value
                return null;
            }
        }

        // component it not a value holder
        return null;
    }

    public static String getWidgetVar(final String id) {
        final UIComponent component = ComponentUtils.findComponent(FacesContext.getCurrentInstance().getViewRoot(), id);

        if (component == null) {
            throw new FacesException("Cannot find component " + id + " in view.");
        } else if (component instanceof Widget) {
            return ((Widget) component).resolveWidgetVar();
        } else {
            throw new FacesException("Component with id " + id + " is not a Widget");
        }

    }

    public static boolean isBeansValidationAvailable(final FacesContext context) {
        boolean result = false;
        final String beanValidationAvailableKey = "javax.faces.private.BEANS_VALIDATION_AVAILABLE";

        final Map<String, Object> appMap = context.getExternalContext().getApplicationMap();

        if (appMap.containsKey(beanValidationAvailableKey)) {
            result = (Boolean) appMap.get(beanValidationAvailableKey);
        } else {
            try {
                new BeanValidator();
                appMap.put(beanValidationAvailableKey, result = true);
            } catch (final Throwable t) {
                appMap.put(beanValidationAvailableKey, Boolean.FALSE);
            }
        }

        return result;
    }

    public static boolean isLiteralText(final UIComponent component) {
        return component.getFamily().equalsIgnoreCase("facelets.LiteralText");
    }

    public static boolean isPartialSubmitEnabled(final FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        final String value = externalContext.getInitParameter(Constants.SUBMIT_PARAM);

        return (value == null) ? false : value.equalsIgnoreCase("partial");
    }

    public static boolean isRTL(final FacesContext context, final RTLAware component) {
        final ExternalContext externalContext = context.getExternalContext();
        final String value = externalContext.getInitParameter(Constants.DIRECTION_PARAM);
        final boolean globalValue = (value == null) ? false : value.equalsIgnoreCase("rtl");

        return globalValue || component.isRTL();
    }

    public static boolean isValueBlank(final String value) {
        if (value == null) return true;

        return value.trim().equals("");
    }

    /**
     * Implementation from Apache Commons Lang
     */
    public static Locale toLocale(final String str) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch0 = str.charAt(0);
        final char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            final char ch3 = str.charAt(3);
            if (ch3 == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(4));
            }
            final char ch4 = str.charAt(4);
            if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }

    public static boolean validateEmptyFields(final FacesContext context) {
        final ExternalContext externalContext = context.getExternalContext();
        String value = externalContext.getInitParameter(UIInput.VALIDATE_EMPTY_FIELDS_PARAM_NAME);

        if (null == value) {
            value = (String) externalContext.getApplicationMap().get(UIInput.VALIDATE_EMPTY_FIELDS_PARAM_NAME);
        }

        if (value == null || value.equals("auto")) {
            return ComponentUtils.isBeansValidationAvailable(context);
        } else {
            return Boolean.valueOf(value);
        }
    }
}