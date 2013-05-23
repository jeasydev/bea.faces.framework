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
package org.primefaces.renderkit;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

public abstract class InputRenderer extends CoreRenderer {

    public static boolean shouldDecode(final UIComponent component) {
        final boolean disabled = Boolean.valueOf(String.valueOf(component.getAttributes().get("disabled")));
        final boolean readonly = Boolean.valueOf(String.valueOf(component.getAttributes().get("readonly")));

        return !disabled && !readonly;
    }

    protected Object coerceToModelType(final FacesContext ctx, final Object value, final Class itemValueType) {
        Object newValue;
        try {
            final ExpressionFactory ef = ctx.getApplication().getExpressionFactory();
            newValue = ef.coerceToType(value, itemValueType);
        } catch (final ELException ele) {
            newValue = value;
        } catch (final IllegalArgumentException iae) {
            newValue = value;
        }

        return newValue;
    }

    protected SelectItem createSelectItem(final FacesContext context,
                                          final UISelectItems uiSelectItems,
                                          final Object object) {
        final String var = (String) uiSelectItems.getAttributes().get("var");

        if (var != null) {
            context.getExternalContext().getRequestMap().put(var, object);

            final Object itemLabelAsObject = uiSelectItems.getAttributes().get("itemLabel");
            Object itemValue = uiSelectItems.getAttributes().get("itemValue");
            final String description = (String) uiSelectItems.getAttributes().get("itemDescription");
            final Object itemDisabled = uiSelectItems.getAttributes().get("itemDisabled");
            final Object itemEscaped = uiSelectItems.getAttributes().get("itemLabelEscaped");
            final Object noSelection = uiSelectItems.getAttributes().get("noSelectionOption");

            if (itemValue == null) {
                itemValue = object;
            }

            final String itemLabel = itemLabelAsObject == null ? String.valueOf(object) : String
                .valueOf(itemLabelAsObject);
            final boolean disabled = itemDisabled == null ? false : Boolean.valueOf(itemDisabled.toString());
            final boolean escaped = itemEscaped == null ? false : Boolean.valueOf(itemEscaped.toString());
            final boolean noSelectionOption = noSelection == null ? false : Boolean.valueOf(noSelection.toString());

            return new SelectItem(itemValue, itemLabel, description, disabled, escaped, noSelectionOption);
        } else {
            return new SelectItem(object, String.valueOf(object));
        }
    }

    protected SelectItem createSelectItem(final FacesContext context,
                                          final UISelectItems uiSelectItems,
                                          final String itemLabel,
                                          final Object itemValue) {
        final String var = (String) uiSelectItems.getAttributes().get("var");

        if (var != null) {
            context.getExternalContext().getRequestMap().put(var, itemValue);

            final String description = (String) uiSelectItems.getAttributes().get("itemDescription");
            final Boolean disabled = Boolean.valueOf(((String) uiSelectItems.getAttributes().get("itemDisabled")));
            final Boolean escaped = Boolean.valueOf(((String) uiSelectItems.getAttributes().get("itemLabelEscaped")));
            final Boolean noSelectionOption = Boolean.valueOf(((String) uiSelectItems.getAttributes()
                .get("noSelectionOption")));

            return new SelectItem(itemValue, itemLabel, description, disabled, escaped, noSelectionOption);
        } else {
            return new SelectItem(itemValue, itemLabel);
        }
    }

    protected Converter findConverter(final FacesContext context, final UIComponent component) {
        if (!(component instanceof ValueHolder)) {
            return null;
        }

        final Converter converter = ((ValueHolder) component).getConverter();

        if (converter != null) {
            return converter;
        } else {
            return findImplicitConverter(context, component);
        }
    }

    protected Converter findImplicitConverter(final FacesContext context, final UIComponent component) {
        final ValueExpression ve = component.getValueExpression("value");

        if (ve != null) {
            final Class<?> valueType = ve.getType(context.getELContext());

            if (valueType != null) return context.getApplication().createConverter(valueType);
        }

        return null;
    }

    @Override
    public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
        throws ConverterException {
        final Converter converter = findConverter(context, component);

        if (converter != null) {
            final String convertableValue = submittedValue == null ? null : submittedValue.toString();
            return converter.getAsObject(context, component, convertableValue);
        } else {
            return submittedValue;
        }
    }

    protected String getOptionAsString(final FacesContext context,
                                       final UIComponent component,
                                       final Converter converter,
                                       final Object value) throws ConverterException {
        if (!(component instanceof ValueHolder)) {
            return value == null ? null : value.toString();
        }

        if (converter == null) {
            if (value == null) {
                return "";
            } else if (value instanceof String) {
                return (String) value;
            } else {
                final Converter implicitConverter = findImplicitConverter(context, component);

                return implicitConverter == null ? value.toString() : implicitConverter.getAsString(context, component,
                                                                                                    value);
            }
        } else {
            return converter.getAsString(context, component, value);
        }
    }

    protected List<SelectItem> getSelectItems(final FacesContext context, final UIInput component) {
        final List<SelectItem> selectItems = new ArrayList<SelectItem>();

        for (final UIComponent child : component.getChildren()) {
            if (child instanceof UISelectItem) {
                final UISelectItem uiSelectItem = (UISelectItem) child;
                final Object selectItemValue = uiSelectItem.getValue();

                if (selectItemValue == null) {
                    selectItems.add(new SelectItem(uiSelectItem.getItemValue(),
                                                   uiSelectItem.getItemLabel(),
                                                   uiSelectItem.getItemDescription(),
                                                   uiSelectItem.isItemDisabled(),
                                                   uiSelectItem.isItemEscaped(),
                                                   uiSelectItem.isNoSelectionOption()));
                } else {
                    selectItems.add((SelectItem) selectItemValue);
                }

            } else if (child instanceof UISelectItems) {
                final UISelectItems uiSelectItems = ((UISelectItems) child);
                final Object value = uiSelectItems.getValue();

                if (value != null) {
                    if (value instanceof SelectItem) {
                        selectItems.add((SelectItem) value);
                    } else if (value.getClass().isArray()) {
                        for (int i = 0; i < Array.getLength(value); i++) {
                            final Object item = Array.get(value, i);

                            if (item instanceof SelectItem)
                                selectItems.add((SelectItem) item);
                            else selectItems.add(createSelectItem(context, uiSelectItems, item));
                        }
                    } else if (value instanceof Map) {
                        final Map map = (Map) value;

                        for (final Iterator it = map.keySet().iterator(); it.hasNext();) {
                            final Object key = it.next();

                            selectItems
                                .add(createSelectItem(context, uiSelectItems, String.valueOf(key), map.get(key)));
                        }
                    } else if (value instanceof Collection) {
                        final Collection collection = (Collection) value;

                        for (final Iterator it = collection.iterator(); it.hasNext();) {
                            final Object item = it.next();
                            if (item instanceof SelectItem)
                                selectItems.add((SelectItem) item);
                            else selectItems.add(createSelectItem(context, uiSelectItems, item));
                        }
                    }
                }
            }
        }

        return selectItems;
    }
}
