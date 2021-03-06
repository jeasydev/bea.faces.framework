/*
 * Copyright 2012 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.omnifaces.component.validator;

import java.util.HashSet;
import java.util.List;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * <strong>ValidateUnique</strong> validates if ALL of the given
 * <code>UIInput</code> components have an unique value. The default message is
 * <blockquote>{0}: Please fill out an unique value for all of those
 * fields</blockquote>
 * <p>
 * For general usage instructions, refer {@link ValidateMultipleFields}
 * documentation.
 * 
 * @author Bauke Scholtz
 */
@FacesComponent(ValidateUnique.COMPONENT_TYPE)
public class ValidateUnique extends ValidateMultipleFields {

    // Public constants
    // -----------------------------------------------------------------------------------------------

    /** The standard component type. */
    public static final String COMPONENT_TYPE = "org.omnifaces.component.validator.ValidateUnique";

    // Private constants
    // ----------------------------------------------------------------------------------------------

    private static final String DEFAULT_MESSAGE = "{0}: Please fill out an unique value for all of those fields";

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * The default constructor sets the default message.
     */
    public ValidateUnique() {
        super(ValidateUnique.DEFAULT_MESSAGE);
    }

    // Actions
    // --------------------------------------------------------------------------------------------------------

    /**
     * Validate if all values are unique.
     */
    @Override
    protected boolean validateValues(final FacesContext context, final List<UIInput> inputs, final List<Object> values) {
        return (new HashSet<Object>(values).size() == inputs.size());
    }

}