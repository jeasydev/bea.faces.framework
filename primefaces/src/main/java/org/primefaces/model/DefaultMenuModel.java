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
package org.primefaces.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.submenu.Submenu;

public class DefaultMenuModel implements MenuModel, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final List<UIComponent> contents;

    public DefaultMenuModel() {
        contents = new ArrayList<UIComponent>();
    }

    @Override
    public void addMenuItem(final MenuItem menuItem) {
        contents.add(menuItem);
    }

    @Override
    public void addSeparator(final Separator separator) {
        contents.add(separator);
    }

    @Override
    public void addSubmenu(final Submenu submenu) {
        contents.add(submenu);
    }

    @Override
    public List<UIComponent> getContents() {
        return contents;
    }
}