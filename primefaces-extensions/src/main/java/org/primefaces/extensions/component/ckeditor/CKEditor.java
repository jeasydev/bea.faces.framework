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

package org.primefaces.extensions.component.ckeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.Widget;
import org.primefaces.extensions.renderkit.widget.Option;

/**
 * Component class for the <code>CKEditor</code> component.
 * 
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"), @ResourceDependency(library = "primefaces", name = "primefaces.js"), @ResourceDependency(library = "primefaces-extensions", name = "primefaces-extensions.js") })
public class CKEditor extends HtmlInputTextarea implements ClientBehaviorHolder, Widget {

    /**
     * Properties that are tracked by state saving.
     * 
     * @author Thomas Andraschko / last modified by $Author$
     * @version $Revision$
     */
    protected enum PropertyKeys {

        widgetVar,
        @Option
        height,
        @Option
        width,
        @Option
        theme,
        @Option
        skin,
        @Option(useDoubleQuotes = true)
        toolbar,
        @Option
        readOnly,
        @Option
        interfaceColor,
        @Option
        language,
        @Option
        defaultLanguage,
        @Option
        contentsCss,
        @Option
        checkDirtyInterval,
        @Option
        customConfig,
        @Option
        tabindex,
        escape;

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

    public static final String COMPONENT_TYPE = "org.primefaces.extensions.component.CKEditor";
    public static final String COMPONENT_FAMILY = "org.primefaces.extensions.component";
    private static final String DEFAULT_RENDERER = "org.primefaces.extensions.component.CKEditorRenderer";

    private static final String OPTIMIZED_PACKAGE = "org.primefaces.extensions.component.";
    public static final String EVENT_SAVE = "save";
    public static final String EVENT_INITIALIZE = "initialize";
    public static final String EVENT_BLUR = "blur";
    public static final String EVENT_FOCUS = "focus";
    public static final String EVENT_WYSIWYG_MODE = "wysiwygMode";

    public static final String EVENT_SOURCE_MODE = "sourceMode";

    /**
     * Event, which will be fired after the content has been changed (without
     * blur before).
     */
    public static final String EVENT_DIRTY = "dirty";

    /**
     * Event, which will be fired after blur, when the content has been changed.
     */
    public static final String EVENT_CHANGE = "change";

    private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays
        .asList(CKEditor.EVENT_SAVE, CKEditor.EVENT_INITIALIZE, CKEditor.EVENT_BLUR, CKEditor.EVENT_FOCUS,
                CKEditor.EVENT_CHANGE, CKEditor.EVENT_DIRTY, CKEditor.EVENT_WYSIWYG_MODE, CKEditor.EVENT_SOURCE_MODE));

    public CKEditor() {
        setRendererType(CKEditor.DEFAULT_RENDERER);
    }

    public int getCheckDirtyInterval() {
        return (Integer) getStateHelper().eval(PropertyKeys.checkDirtyInterval, 1000);
    }

    public String getContentsCss() {
        return (String) getStateHelper().eval(PropertyKeys.contentsCss, null);
    }

    public String getCustomConfig() {
        return (String) getStateHelper().eval(PropertyKeys.customConfig, null);
    }

    @Override
    public String getDefaultEventName() {
        return CKEditor.EVENT_SAVE;
    }

    public String getDefaultLanguage() {
        return (String) getStateHelper().eval(PropertyKeys.defaultLanguage, null);
    }

    @Override
    public Collection<String> getEventNames() {
        return CKEditor.EVENT_NAMES;
    }

    @Override
    public String getFamily() {
        return CKEditor.COMPONENT_FAMILY;
    }

    public String getHeight() {
        return (String) getStateHelper().eval(PropertyKeys.height, "200px");
    }

    public String getInterfaceColor() {
        return (String) getStateHelper().eval(PropertyKeys.interfaceColor, null);
    }

    public String getLanguage() {
        return (String) getStateHelper().eval(PropertyKeys.language, null);
    }

    public String getSkin() {
        return (String) getStateHelper().eval(PropertyKeys.skin, null);
    }

    @Override
    public String getTabindex() {
        return (String) getStateHelper().eval(PropertyKeys.tabindex, null);
    }

    public String getTheme() {
        return (String) getStateHelper().eval(PropertyKeys.theme, null);
    }

    public String getToolbar() {
        return (String) getStateHelper().eval(PropertyKeys.toolbar, null);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public String getWidth() {
        return (String) getStateHelper().eval(PropertyKeys.width, "600px");
    }

    public boolean isEscape() {
        return (Boolean) getStateHelper().eval(PropertyKeys.escape, true);
    }

    public boolean isReadOnly() {
        return (Boolean) getStateHelper().eval(PropertyKeys.readOnly, false);
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

    @SuppressWarnings("unchecked")
    public void setAttribute(final PropertyKeys property, final Object value) {
        getStateHelper().put(property, value);

        List<String> setAttributes = (List<String>) getAttributes()
            .get("javax.faces.component.UIComponentBase.attributesThatAreSet");
        if (setAttributes == null) {
            final String cname = this.getClass().getName();
            if (cname != null && cname.startsWith(CKEditor.OPTIMIZED_PACKAGE)) {
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

    public void setCheckDirtyInterval(final int checkDirtyInterval) {
        setAttribute(PropertyKeys.checkDirtyInterval, checkDirtyInterval);
    }

    public void setContentsCss(final String contentsCss) {
        setAttribute(PropertyKeys.contentsCss, contentsCss);
    }

    public void setCustomConfig(final String customConfig) {
        setAttribute(PropertyKeys.customConfig, customConfig);
    }

    public void setDefaultLanguage(final String defaultLanguage) {
        setAttribute(PropertyKeys.defaultLanguage, defaultLanguage);
    }

    public void setEscape(final boolean escape) {
        setAttribute(PropertyKeys.escape, escape);
    }

    public void setHeight(final String height) {
        setAttribute(PropertyKeys.height, height);
    }

    public void setInterfaceColor(final String interfaceColor) {
        setAttribute(PropertyKeys.interfaceColor, interfaceColor);
    }

    public void setLanguage(final String language) {
        setAttribute(PropertyKeys.language, language);
    }

    public void setReadOnly(final boolean readOnly) {
        setAttribute(PropertyKeys.readOnly, readOnly);
    }

    public void setSkin(final String skin) {
        setAttribute(PropertyKeys.skin, skin);
    }

    @Override
    public void setTabindex(final String tabindex) {
        setAttribute(PropertyKeys.tabindex, tabindex);
    }

    public void setTheme(final String theme) {
        setAttribute(PropertyKeys.theme, theme);
    }

    public void setToolbar(final String toolbar) {
        setAttribute(PropertyKeys.toolbar, toolbar);
    }

    public void setWidgetVar(final String widgetVar) {
        setAttribute(PropertyKeys.widgetVar, widgetVar);
    }

    public void setWidth(final String width) {
        setAttribute(PropertyKeys.width, width);
    }
}
