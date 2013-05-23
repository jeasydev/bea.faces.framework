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

package org.primefaces.extensions.model.dynaform;

import java.io.Serializable;

/**
 * Abstract class representing a control (typically input element or label)
 * inside of <code>DynaForm</code>.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public abstract class AbstractDynaFormElement implements Serializable {

    private static final long serialVersionUID = 20120514L;

    private int colspan = 1;
    private int rowspan = 1;
    private int row;
    private int column;
    private boolean extended;

    /**
     * This constructor is required for serialization. Please do not remove.
     */
    public AbstractDynaFormElement() {
    }

    public AbstractDynaFormElement(final int colspan,
                                   final int rowspan,
                                   final int row,
                                   final int column,
                                   final boolean extended) {
        this.colspan = colspan;
        this.rowspan = rowspan;
        this.row = row;
        this.column = column;
        this.extended = extended;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AbstractDynaFormElement that = (AbstractDynaFormElement) o;

        return row == that.row && column == that.column && extended == that.extended;
    }

    public int getColspan() {
        return colspan;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getRowspan() {
        return rowspan;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        result = 31 * result + (extended ? 1 : 0);

        return result;
    }

    public boolean isExtended() {
        return extended;
    }
}
