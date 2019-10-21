package pl.taskyers.taskybase.core.messages.container;

import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ValidationMessage;

public class ValidationMessageContainer extends MessageContainer<String, String> {
    
    public void addWarn(String message, String field) {
        messages.add(new ValidationMessage(message, MessageType.WARN, field));
    }
    
    public void addError(String message, String field) {
        messages.add(new ValidationMessage(message, MessageType.ERROR, field));
    }
    
}
