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
package org.omnifaces.eventlistener;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UICommand;
import javax.faces.component.UIInput;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.Validator;

/**
 * Overrides {@link BeanValidator#setValidationGroups(String)} for all
 * components in the current view. This allows to temporarily use different
 * validationGroups or disabling validation if a specific {@link UICommand} or
 * {@link UIInput} has invoked the form submit.
 * 
 * @author Adrian Gygax
 * @author Bauke Scholtz
 * @since 1.3
 */
public class BeanValidationEventListener implements SystemEventListener {

    // Constants
    // ------------------------------------------------------------------------------------------------------

    /**
     * Dummy validation group to disable any validation.
     */
    private static interface NoValidationGroup {
        //
    }

    private static final String ATTRIBUTE_ORIGINAL_VALIDATION_GROUPS = "BeanValidationEventListener.originalValidationGroups";
    private static final Logger LOGGER = Logger.getLogger(BeanValidationEventListener.class.getName());

    // Variables
    // ------------------------------------------------------------------------------------------------------

    private static final String LOG_VALIDATION_GROUPS_OVERRIDDEN = "Validation groups for component with id '%s' overriden from '%s' to '%s'";

    /**
     * Obtain the bean validator instance of the given editable value holder
     * component.
     */
    private static BeanValidator getBeanValidator(final EditableValueHolder component) {
        final Validator[] validators = component.getValidators();

        for (final Validator validator : validators) {
            if (validator instanceof BeanValidator) {
                return (BeanValidator) validator;
            }
        }

        return null;
    }

    // Constructors
    // ---------------------------------------------------------------------------------------------------

    private final String validationGroups;

    // Actions
    // --------------------------------------------------------------------------------------------------------

    private final boolean disabled;

    /**
     * Construct an instance of bean validation event listener based on the
     * given validation groups and disabled state.
     */
    public BeanValidationEventListener(final String validationGroups, final boolean disabled) {
        this.validationGroups = validationGroups;
        this.disabled = disabled;
    }

    /**
     * Restores the original value of
     * {@link BeanValidator#getValidationGroups()}.
     */
    private void handlePostValidate(final UIInput component) {
        final BeanValidator beanValidator = BeanValidationEventListener.getBeanValidator(component);
        final String originalValidationGroups = (String) component.getAttributes()
            .remove(BeanValidationEventListener.ATTRIBUTE_ORIGINAL_VALIDATION_GROUPS);
        beanValidator.setValidationGroups(originalValidationGroups);
    }

    /**
     * Replaces the original value of
     * {@link BeanValidator#getValidationGroups()} with the value from the tag
     * attribute.
     */
    private void handlePreValidate(final UIInput component) {
        final BeanValidator beanValidator = BeanValidationEventListener.getBeanValidator(component);
        final String validationGroups = disabled ? NoValidationGroup.class.getName() : this.validationGroups;
        final String originalValidationGroups = beanValidator.getValidationGroups();

        if (originalValidationGroups != null) {
            component.getAttributes().put(BeanValidationEventListener.ATTRIBUTE_ORIGINAL_VALIDATION_GROUPS,
                                          originalValidationGroups);
        }

        beanValidator.setValidationGroups(validationGroups);

        if (BeanValidationEventListener.LOGGER.isLoggable(Level.FINER)) {
            BeanValidationEventListener.LOGGER.finer(String
                .format(BeanValidationEventListener.LOG_VALIDATION_GROUPS_OVERRIDDEN, component.getClientId(),
                        originalValidationGroups, validationGroups));
        }
    }

    // Helpers
    // --------------------------------------------------------------------------------------------------------

    /**
     * Only listens to {@link UIInput} components which have a
     * {@link javax.faces.validator.BeanValidator} assigned.
     */
    @Override
    public boolean isListenerForSource(final Object source) {
        return source instanceof UIInput
            && BeanValidationEventListener.getBeanValidator((EditableValueHolder) source) != null;
    }

    /**
     * Handle the {@link PreValidateEvent} and {@link PostValidateEvent}.
     */
    @Override
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        if (event instanceof PreValidateEvent) {
            handlePreValidate((UIInput) ((ComponentSystemEvent) event).getComponent());
        } else if (event instanceof PostValidateEvent) {
            handlePostValidate((UIInput) ((ComponentSystemEvent) event).getComponent());
        }
    }

}