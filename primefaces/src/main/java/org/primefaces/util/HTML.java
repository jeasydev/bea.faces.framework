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

public class HTML {

    public static String[] CLICK_EVENT = { "onclick" };

    public static String[] BLUR_FOCUS_EVENTS = { "onblur", "onfocus" };

    public static String[] CHANGE_SELECT_EVENTS = { "onchange", "onselect" };

    public static String[] COMMON_EVENTS = { "onclick", "ondblclick", "onkeydown", "onkeypress", "onkeyup", "onmousedown", "onmousemove", "onmouseout", "onmouseover", "onmouseup" };

    // StyleClass is omitted
    public static String[] IMG_ATTRS_WITHOUT_EVENTS = { "alt", "width", "height", "title", "dir", "lang", "ismap", "usemap", "style" };

    // StyleClass is omitted
    public static String[] LINK_ATTRS_WITHOUT_EVENTS = { "accesskey", "charset", "coords", "dir", "disabled", "hreflang", "rel", "rev", "shape", "tabindex", "style", "target", "title", "type" };

    // StyleClass is omitted
    public static String[] BUTTON_ATTRS_WITHOUT_EVENTS = { "accesskey", "alt", "dir", "label", "lang", "style", "tabindex", "title", "type" };

    // StyleClass is omitted
    public static String[] MEDIA_ATTRS = { "height", "width", "style" };

    // disabled, readonly, style, styleClass handles by component renderer
    public static String[] INPUT_TEXT_ATTRS_WITHOUT_EVENTS = { "accesskey", "alt", "autocomplete", "dir", "lang", "maxlength", "size", "tabindex", "title" };

    public static String[] SELECT_ATTRS_WITHOUT_EVENTS = { "accesskey", "dir", "disabled", "lang", "readonly", "style", "tabindex", "title" };

    public static String[] TEXTAREA_ATTRS = { "cols", "rows", "accesskey", "alt", "autocomplete", "dir", "lang", "size", "tabindex", "title" };

    // StyleClass is omitted
    public static String[] LABEL_ATTRS_WITHOUT_EVENTS = { "accesskey", "dir", "lang", "style", "tabindex", "title" };

    public static String[] OUTPUT_EVENTS = ArrayUtils.concat(HTML.COMMON_EVENTS, HTML.BLUR_FOCUS_EVENTS);

    public static String[] BUTTON_EVENTS = ArrayUtils.concat(HTML.OUTPUT_EVENTS, HTML.CHANGE_SELECT_EVENTS);

    public static String[] IMG_ATTRS = ArrayUtils.concat(HTML.IMG_ATTRS_WITHOUT_EVENTS, HTML.COMMON_EVENTS);

    public static String[] LINK_ATTRS = ArrayUtils.concat(HTML.LINK_ATTRS_WITHOUT_EVENTS, HTML.OUTPUT_EVENTS);

    public static String[] LABEL_ATTRS = ArrayUtils.concat(HTML.LABEL_ATTRS_WITHOUT_EVENTS, HTML.OUTPUT_EVENTS);

    public static String[] BUTTON_ATTRS = ArrayUtils.concat(HTML.BUTTON_ATTRS_WITHOUT_EVENTS, HTML.BUTTON_EVENTS);

    public static final String[] INPUT_TEXT_ATTRS = ArrayUtils.concat(HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS,
                                                                      HTML.COMMON_EVENTS, HTML.CHANGE_SELECT_EVENTS,
                                                                      HTML.BLUR_FOCUS_EVENTS);

    public static final String[] INPUT_TEXTAREA_ATTRS = ArrayUtils.concat(HTML.TEXTAREA_ATTRS, HTML.COMMON_EVENTS,
                                                                          HTML.CHANGE_SELECT_EVENTS,
                                                                          HTML.BLUR_FOCUS_EVENTS);

    public static final String[] SELECT_ATTRS = ArrayUtils.concat(HTML.SELECT_ATTRS_WITHOUT_EVENTS, HTML.COMMON_EVENTS,
                                                                  HTML.CHANGE_SELECT_EVENTS, HTML.BLUR_FOCUS_EVENTS);

    public final static String BUTTON_TEXT_ONLY_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only";
    public final static String BUTTON_ICON_ONLY_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only";
    public final static String BUTTON_TEXT_ICON_LEFT_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left";
    public final static String BUTTON_TEXT_ICON_RIGHT_BUTTON_CLASS = "ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-right";
    public final static String BUTTON_LEFT_ICON_CLASS = "ui-button-icon-left ui-icon ui-c";
    public final static String BUTTON_RIGHT_ICON_CLASS = "ui-button-icon-right ui-icon ui-c";
    public final static String BUTTON_TEXT_CLASS = "ui-button-text ui-c";
    public final static String BUTTON_TEXT_ONLY_BUTTON_FLAT_CLASS = "ui-button ui-widget ui-state-default ui-button-text-only";

    public final static String CHECKBOX_ALL_CLASS = "ui-chkbox ui-chkbox-all ui-widget";
    public final static String CHECKBOX_CLASS = "ui-chkbox ui-widget";
    public final static String CHECKBOX_BOX_CLASS = "ui-chkbox-box ui-widget ui-corner-all ui-state-default";
    public final static String CHECKBOX_INPUT_WRAPPER_CLASS = "ui-helper-hidden";
    public final static String CHECKBOX_ICON_CLASS = "ui-chkbox-icon ui-c";
    public final static String CHECKBOX_CHECKED_ICON_CLASS = "ui-chkbox-icon ui-icon ui-icon-check ui-c";
    public final static String CHECKBOX_LABEL_CLASS = "ui-chkbox-label";

    public final static String RADIOBUTTON_CLASS = "ui-radiobutton ui-widget";
    public final static String RADIOBUTTON_NATIVE_CLASS = "ui-radiobutton ui-radiobutton-native ui-widget";
    public final static String RADIOBUTTON_BOX_CLASS = "ui-radiobutton-box ui-widget ui-corner-all ui-state-default";
    public final static String RADIOBUTTON_INPUT_WRAPPER_CLASS = "ui-helper-hidden";
    public final static String RADIOBUTTON_ICON_CLASS = "ui-radiobutton-icon";
    public final static String RADIOBUTTON_CHECKED_ICON_CLASS = "ui-icon ui-icon-bullet";
}