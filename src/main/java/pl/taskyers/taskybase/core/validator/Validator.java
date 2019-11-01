package pl.taskyers.taskybase.core.validator;

import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;

/**
 * Interface for validating objects
 *
 * @param <T> class that objects will be validated from
 * @author Jakub Sildatk
 */
public interface Validator<T> {
    
    /**
     * Validating specified object, after that validation message container will contain all validation messages
     *
     * @param object                     object to be validated
     * @param validationMessageContainer container that will contain all validation messages
     * @since 0.0.1
     */
    void validate(T object, ValidationMessageContainer validationMessageContainer);
    
}
