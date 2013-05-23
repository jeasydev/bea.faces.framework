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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Class representing a label inside of <code>DynaForm</code>.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class DynaFormLabel extends AbstractDynaFormElement {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String value;
    private boolean escape = true;
    private DynaFormControl forControl;
    private String targetClientId;
    private boolean targetRequired = false;
    private boolean targetValid = true;

    public DynaFormLabel(final String value,
                         final boolean escape,
                         final int colspan,
                         final int rowspan,
                         final int row,
                         final int column,
                         final boolean extended) {
        super(colspan, rowspan, row, column, extended);

        this.value = value;
        this.escape = escape;
    }

    public DynaFormControl getForControl() {
        return forControl;
    }

    public String getTargetClientId() {
        return targetClientId;
    }

    public String getValue() {
        return value;
    }

    public boolean isEscape() {
        return escape;
    }

    public boolean isTargetRequired() {
        return targetRequired;
    }

    public boolean isTargetValid() {
        return targetValid;
    }

    public void setForControl(final DynaFormControl forControl) {
        this.forControl = forControl;
    }

    public void setTargetClientId(final String targetClientId) {
        this.targetClientId = targetClientId;
    }

    public void setTargetRequired(final boolean targetRequired) {
        this.targetRequired = targetRequired;
    }

    public void setTargetValid(final boolean targetValid) {
        this.targetValid = targetValid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("value", value).append("escape",
                                                                                                         escape)
            .append("forControl", (forControl != null ? forControl.getKey() : "null")).append("colspan", getColspan())
            .append("rowspan", getRowspan()).append("row", getRow()).append("column", getColumn()).append("extended",
                                                                                                          isExtended())
            .toString();
    }
}
