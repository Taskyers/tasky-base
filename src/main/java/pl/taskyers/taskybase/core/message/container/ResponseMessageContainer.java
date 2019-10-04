package pl.taskyers.taskybase.core.message.container;

import pl.taskyers.taskybase.core.message.MessageType;
import pl.taskyers.taskybase.core.message.ResponseMessage;

public class ResponseMessageContainer extends MessageContainer<String, Object> {
    
    public void addSuccess(String message, Object argument) {
        messages.add(new ResponseMessage<>(message, MessageType.SUCCESS, argument));
    }
    
    public void addWarn(String message, Object argument) {
        messages.add(new ResponseMessage<>(message, MessageType.WARN, argument));
    }
    
    public void addError(String message) {
        messages.add(new ResponseMessage<>(message, MessageType.ERROR));
    }
    
}
