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

package org.primefaces.extensions.util;

import java.io.Serializable;

/**
 * Keeps state of a component implementing
 * {@link javax.faces.component.EditableValueHolder}. This class is used in
 * {@link org.primefaces.extensions.component.base.AbstractDynamicData}.
 * 
 * @author Oleg Varaksin / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class SavedEditableValueState implements Serializable {

    private static final long serialVersionUID = 20120425L;

    private Object submittedValue;

    private boolean valid = true;

    private Object value;

    private boolean localValueSet = false;

    private Object labelValue;

    public Object getLabelValue() {
        return labelValue;
    }

    public Object getSubmittedValue() {
        return submittedValue;
    }

    public Object getValue() {
        return value;
    }

    public boolean isLocalValueSet() {
        return localValueSet;
    }

    public boolean isValid() {
        return valid;
    }

    public void reset() {
        submittedValue = null;
        valid = true;
        value = null;
        localValueSet = false;
    }

    public void setLabelValue(final Object labelValue) {
        this.labelValue = labelValue;
    }

    public void setLocalValueSet(final boolean localValueSet) {
        this.localValueSet = localValueSet;
    }

    public void setSubmittedValue(final Object submittedValue) {
        this.submittedValue = submittedValue;
    }

    public void setValid(final boolean valid) {
        this.valid = valid;
    }

    public void setValue(final Object value) {
        this.value = value;
    }
}
