package pl.taskyers.taskybase.core.validator;

import pl.taskyers.taskybase.core.message.container.ValidationMessageContainer;

public interface Validator<T> {
    
    void validate(T object, ValidationMessageContainer validationMessageContainer);
    
}
