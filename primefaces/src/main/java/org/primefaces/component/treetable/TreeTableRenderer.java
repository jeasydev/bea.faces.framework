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
package org.primefaces.component.treetable;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.api.UITree;
import org.primefaces.component.column.Column;
import org.primefaces.model.TreeNode;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.renderkit.RendererUtils;
import org.primefaces.util.WidgetBuilder;

public class TreeTableRenderer extends CoreRenderer {

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final TreeTable tt = (TreeTable) component;

        if (tt.getSelectionMode() != null) {
            decodeSelection(context, component);
        }

        decodeBehaviors(context, component);
    }

    protected void decodeSelection(final FacesContext context, final UIComponent component) {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final TreeTable tt = (TreeTable) component;
        final String selectionMode = tt.getSelectionMode();

        // decode selection
        if (selectionMode != null) {
            final String selectionValue = params.get(tt.getClientId(context) + "_selection");

            if (!isValueBlank(selectionValue)) {
                if (selectionMode.equals("single")) {
                    tt.setRowKey(selectionValue);

                    tt.setSelection(tt.getRowNode());
                } else {
                    final String[] rowKeys = selectionValue.split(",");
                    final TreeNode[] selection = new TreeNode[rowKeys.length];

                    for (int i = 0; i < rowKeys.length; i++) {
                        tt.setRowKey(rowKeys[i]);

                        selection[i] = tt.getRowNode();
                    }

                    tt.setSelection(selection);
                }

                tt.setRowKey(null); // cleanup
            }
        }
    }

    @Override
    public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
        // Do nothing
    }

    public void encodeColGroup(final FacesContext context, final TreeTable tt) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("colgroup", null);
        for (final UIComponent kid : tt.getChildren()) {
            if (kid.isRendered() && kid instanceof Column) {
                writer.startElement("col", null);
                writer.endElement("col");
            }
        }
        writer.endElement("colgroup");
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final TreeTable tt = (TreeTable) component;
        final String clientId = tt.getClientId(context);
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();

        final String nodeKey = params.get(clientId + "_expand");
        if (nodeKey != null) {
            tt.setRowKey(nodeKey);
            final TreeNode node = tt.getRowNode();
            node.setExpanded(true);

            encodeNodeChildren(context, tt, node, nodeKey);
        } else {
            encodeMarkup(context, tt);
            encodeScript(context, tt);
        }
    }

    protected void encodeFacet(final FacesContext context,
                               final TreeTable tt,
                               final UIComponent facet,
                               final String styleClass) throws IOException {
        if (facet == null) return;

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", styleClass, null);

        facet.encodeAll(context);

        writer.endElement("div");
    }

    protected void encodeMarkup(final FacesContext context, final TreeTable tt) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = tt.getClientId(context);
        final boolean scrollable = tt.isScrollable();

        String containerClass = tt.isResizableColumns()
                                                       ? TreeTable.RESIZABLE_CONTAINER_CLASS
                                                       : TreeTable.CONTAINER_CLASS;
        containerClass = scrollable ? containerClass + " " + TreeTable.SCROLLABLE_CONTAINER_CLASS : containerClass;
        containerClass = tt.getStyleClass() == null ? containerClass : containerClass + " " + tt.getStyleClass();

        writer.startElement("div", null);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", containerClass, null);
        if (tt.getStyle() != null) writer.writeAttribute("style", tt.getStyle(), null);

        if (scrollable)
            encodeScrollableMarkup(context, tt);
        else encodeRegularMarkup(context, tt);

        if (tt.getSelectionMode() != null)
            encodeStateHolder(context, tt, clientId + "_selection", tt.getSelectedRowKeysAsString());

        if (scrollable) encodeStateHolder(context, tt, clientId + "_scrollState", tt.getScrollState());

        writer.endElement("div");
    }

    protected void encodeNode(final FacesContext context,
                              final TreeTable tt,
                              final TreeNode treeNode,
                              final String rowKey,
                              final String parentRowKey) throws IOException {
        if (rowKey != null) {
            final ResponseWriter writer = context.getResponseWriter();
            tt.setRowKey(rowKey);
            final String icon = treeNode.isExpanded() ? TreeTable.COLLAPSE_ICON : TreeTable.EXPAND_ICON;
            final int depth = rowKey.split(UITree.SEPARATOR).length - 1;
            final String selectionMode = tt.getSelectionMode();
            final boolean selectionEnabled = selectionMode != null;
            final boolean selectable = treeNode.isSelectable() && selectionEnabled;
            final boolean checkboxSelection = selectionEnabled && selectionMode.equals("checkbox");

            if (checkboxSelection && treeNode.getParent().isSelected()) {
                treeNode.setSelected(true);
            }

            final boolean selected = treeNode.isSelected();

            String rowStyleClass = selected ? TreeTable.SELECTED_ROW_CLASS : TreeTable.ROW_CLASS;
            rowStyleClass = selectable ? rowStyleClass + " " + TreeTable.SELECTABLE_NODE_CLASS : rowStyleClass;
            rowStyleClass = rowStyleClass + " " + treeNode.getType();

            final String userRowStyleClass = tt.getRowStyleClass();
            if (userRowStyleClass != null) {
                rowStyleClass = rowStyleClass + " " + userRowStyleClass;
            }

            if (selected) {
                tt.getSelectedRowKeys().add(rowKey);
            }

            writer.startElement("tr", null);
            writer.writeAttribute("id", tt.getClientId(context) + "_node_" + rowKey, null);
            writer.writeAttribute("class", rowStyleClass, null);
            writer.writeAttribute("role", "row", null);
            writer.writeAttribute("aria-expanded", String.valueOf(treeNode.isExpanded()), null);
            writer.writeAttribute("data-rk", rowKey, null);

            if (selectionEnabled) {
                writer.writeAttribute("aria-selected", String.valueOf(selected), null);
            }

            if (parentRowKey != null) {
                writer.writeAttribute("data-prk", parentRowKey, null);
            }

            for (int i = 0; i < tt.getChildren().size(); i++) {
                final UIComponent kid = tt.getChildren().get(i);

                if (kid instanceof Column && kid.isRendered()) {
                    final Column column = (Column) kid;
                    final String columnStyleClass = column.getStyleClass();
                    final String columnStyle = column.getStyle();

                    writer.startElement("td", null);
                    writer.writeAttribute("role", "gridcell", null);
                    if (columnStyle != null) writer.writeAttribute("style", columnStyle, null);
                    if (columnStyleClass != null) writer.writeAttribute("class", columnStyleClass, null);

                    if (i == 0) {
                        for (int j = 0; j < depth; j++) {
                            writer.startElement("span", null);
                            writer.writeAttribute("class", TreeTable.INDENT_CLASS, null);
                            writer.endElement("span");
                        }

                        writer.startElement("span", null);
                        writer.writeAttribute("class", icon, null);
                        if (treeNode.getChildCount() == 0) {
                            writer.writeAttribute("style", "visibility:hidden", null);
                        }
                        writer.endElement("span");

                        if (selectable && checkboxSelection) {
                            RendererUtils.encodeCheckbox(context, selected);
                        }
                    }

                    column.encodeAll(context);

                    writer.endElement("td");
                }
            }

            writer.endElement("tr");
        }

        // render child nodes if node is expanded or node itself is the root
        if (treeNode.isExpanded() || treeNode.getParent() == null) {
            int childIndex = 0;
            for (final TreeNode treeNode2 : treeNode.getChildren()) {
                final String childRowKey = rowKey == null ? String.valueOf(childIndex) : rowKey + UITree.SEPARATOR
                    + childIndex;

                encodeNode(context, tt, treeNode2, childRowKey, rowKey);

                childIndex++;
            }
        }
    }

    protected void encodeNodeChildren(final FacesContext context,
                                      final TreeTable tt,
                                      final TreeNode treeNode,
                                      final String rowKey) throws IOException {
        int childIndex = 0;
        for (final TreeNode treeNode2 : treeNode.getChildren()) {
            final String childRowKey = rowKey == null ? String.valueOf(childIndex) : rowKey + UITree.SEPARATOR
                + childIndex;

            encodeNode(context, tt, treeNode2, childRowKey, rowKey);

            childIndex++;
        }
    }

    protected void encodeRegularMarkup(final FacesContext context, final TreeTable tt) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        encodeFacet(context, tt, tt.getFacet("header"), TreeTable.HEADER_CLASS);

        writer.startElement("table", tt);
        writer.writeAttribute("role", "treegrid", null);
        if (tt.getTableStyle() != null) writer.writeAttribute("style", tt.getTableStyle(), null);
        if (tt.getTableStyleClass() != null) writer.writeAttribute("class", tt.getTableStyleClass(), null);

        encodeThead(context, tt);
        encodeTfoot(context, tt);
        encodeTbody(context, tt);

        writer.endElement("table");

        encodeFacet(context, tt, tt.getFacet("footer"), TreeTable.FOOTER_CLASS);
    }

    protected void encodeScript(final FacesContext context, final TreeTable tt) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = tt.getClientId(context);
        final String selectionMode = tt.getSelectionMode();
        final WidgetBuilder wb = getWidgetBuilder(context);
        wb.widget("TreeTable", tt.resolveWidgetVar(), clientId, true).attr("selectionMode", selectionMode, null)
            .attr("resizableColumns", tt.isResizableColumns(), false).attr("liveResize", tt.isLiveResize(), false)
            .attr("scrollable", tt.isScrollable(), false).attr("scrollHeight", tt.getScrollHeight(), null)
            .attr("scrollWidth", tt.getScrollWidth(), null).attr("propagateUp", tt.isPropagateSelectionUp(), true)
            .attr("propagateDown", tt.isPropagateSelectionDown(), true);

        encodeClientBehaviors(context, tt, wb);

        startScript(writer, clientId);
        writer.write(wb.build());
        endScript(writer);
    }

    protected void encodeScrollableMarkup(final FacesContext context, final TreeTable tt) throws IOException {
        final String tableStyle = tt.getStyle();
        final String tableStyleClass = tt.getStyleClass();

        encodeScrollAreaStart(context, tt, TreeTable.SCROLLABLE_HEADER_CLASS, TreeTable.SCROLLABLE_HEADER_BOX_CLASS,
                              tableStyle, tableStyleClass, "header", TreeTable.HEADER_CLASS);
        encodeThead(context, tt);
        encodeScrollAreaEnd(context);

        encodeScrollBody(context, tt, tableStyle, tableStyleClass);

        encodeScrollAreaStart(context, tt, TreeTable.SCROLLABLE_FOOTER_CLASS, TreeTable.SCROLLABLE_FOOTER_BOX_CLASS,
                              tableStyle, tableStyleClass, "footer", TreeTable.FOOTER_CLASS);
        encodeTfoot(context, tt);
        encodeScrollAreaEnd(context);
    }

    protected void encodeScrollAreaEnd(final FacesContext context) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement("table");
        writer.endElement("div");
        writer.endElement("div");
    }

    protected void encodeScrollAreaStart(final FacesContext context,
                                         final TreeTable tt,
                                         final String containerClass,
                                         final String containerBoxClass,
                                         final String tableStyle,
                                         final String tableStyleClass,
                                         final String facet,
                                         final String facetClass) throws IOException {

        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", containerClass, null);

        encodeFacet(context, tt, tt.getFacet(facet), facetClass);

        writer.startElement("div", null);
        writer.writeAttribute("class", containerBoxClass, null);

        writer.startElement("table", null);
        writer.writeAttribute("role", "grid", null);
        if (tableStyle != null) writer.writeAttribute("style", tableStyle, null);
        if (tableStyleClass != null) writer.writeAttribute("class", tableStyleClass, null);
    }

    protected void encodeScrollBody(final FacesContext context,
                                    final TreeTable tt,
                                    final String tableStyle,
                                    final String tableStyleClass) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String scrollHeight = tt.getScrollHeight();

        writer.startElement("div", null);
        writer.writeAttribute("class", TreeTable.SCROLLABLE_BODY_CLASS, null);
        if (scrollHeight != null && scrollHeight.indexOf("%") == -1) {
            writer.writeAttribute("style", "height:" + scrollHeight + "px", null);
        }
        writer.startElement("table", null);
        writer.writeAttribute("role", "grid", null);

        if (tableStyle != null) writer.writeAttribute("style", tableStyle, null);
        if (tableStyleClass != null) writer.writeAttribute("class", tableStyleClass, null);

        encodeColGroup(context, tt);
        encodeTbody(context, tt);

        writer.endElement("table");
        writer.endElement("div");
    }

    private void encodeStateHolder(final FacesContext context, final TreeTable tt, final String name, final String value)
        throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("input", null);
        writer.writeAttribute("id", name, null);
        writer.writeAttribute("name", name, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", value, null);
        writer.endElement("input");
    }

    protected void encodeTbody(final FacesContext context, final TreeTable tt) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final TreeNode root = tt.getValue();
        final String clientId = tt.getClientId(context);

        writer.startElement("tbody", null);
        writer.writeAttribute("id", clientId + "_data", null);
        writer.writeAttribute("class", TreeTable.DATA_CLASS, null);

        if (root != null) {
            encodeNode(context, tt, root, null, null);
        }

        tt.setRowKey(null);

        writer.endElement("tbody");
    }

    protected void encodeTfoot(final FacesContext context, final TreeTable tt) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement("tfoot", null);

        if (tt.hasFooterColumn()) {
            writer.startElement("tr", null);

            for (int i = 0; i < tt.getChildCount(); i++) {
                final UIComponent kid = tt.getChildren().get(i);

                if (kid instanceof Column && kid.isRendered()) {
                    final Column column = (Column) kid;
                    final UIComponent footer = column.getFacet("footer");
                    final String footerText = column.getHeaderText();

                    final String columnStyleClass = column.getStyleClass() == null
                                                                                  ? TreeTable.COLUMN_HEADER_CLASS
                                                                                  : TreeTable.COLUMN_HEADER_CLASS + " "
                                                                                      + column.getStyleClass();
                    final String style = column.getStyle();

                    writer.startElement("td", null);
                    writer.writeAttribute("class", columnStyleClass, null);
                    if (style != null) {
                        writer.writeAttribute("style", style, null);
                    }

                    if (footer != null)
                        footer.encodeAll(context);
                    else if (footerText != null) writer.write(footerText);

                    writer.endElement("td");
                }
            }

            writer.endElement("tr");
        }

        writer.endElement("tfoot");
    }

    protected void encodeThead(final FacesContext context, final TreeTable tt) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = tt.getClientId(context);

        writer.startElement("thead", null);
        writer.writeAttribute("id", clientId + "_head", null);

        writer.startElement("tr", null);
        writer.writeAttribute("role", "row", null);

        for (int i = 0; i < tt.getChildCount(); i++) {
            final UIComponent kid = tt.getChildren().get(i);

            if (kid instanceof Column && kid.isRendered()) {
                final Column column = (Column) kid;
                final UIComponent header = column.getFacet("header");
                final String headerText = column.getHeaderText();
                final String style = column.getStyle();
                String styleClass = column.getStyleClass();
                styleClass = styleClass == null ? TreeTable.COLUMN_HEADER_CLASS : TreeTable.COLUMN_HEADER_CLASS + " "
                    + styleClass;

                if (column.isResizable()) {
                    styleClass = styleClass + " " + TreeTable.RESIZABLE_COLUMN_CLASS;
                }

                writer.startElement("th", null);
                writer.writeAttribute("id", column.getClientId(context), null);
                writer.writeAttribute("class", styleClass, null);
                writer.writeAttribute("role", "columnheader", null);
                if (style != null) {
                    writer.writeAttribute("style", style, null);
                }

                if (header != null)
                    header.encodeAll(context);
                else if (headerText != null) writer.write(headerText);

                writer.endElement("th");
            }
        }

        writer.endElement("tr");
        writer.endElement("thead");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}