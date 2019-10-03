package pl.taskyers.taskybase.core.message.container;

import pl.taskyers.taskybase.core.message.MessageType;
import pl.taskyers.taskybase.core.message.ResponseMessage;

public class ResponseMessageContainer extends MessageContainer<String, Object> {
    
    protected void addSuccess(String message, Object argument) {
        messages.add(new ResponseMessage<>(message, MessageType.SUCCESS, argument));
    }
    
    protected void addWarn(String message, Object argument) {
        messages.add(new ResponseMessage<>(message, MessageType.WARN, argument));
    }
    
    protected void addError(String message) {
        messages.add(new ResponseMessage<>(message, MessageType.ERROR));
    }
    
}
