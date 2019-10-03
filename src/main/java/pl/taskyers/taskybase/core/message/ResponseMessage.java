package pl.taskyers.taskybase.core.message;

import lombok.Getter;

@Getter
public class ResponseMessage<T> extends Message {
    
    private T object;
    
    public ResponseMessage(String message, MessageType messageType, T object) {
        super(message, messageType);
        this.object = object;
    }
    
    public ResponseMessage(String message, MessageType messageType) {
        super(message, messageType);
    }
    
}
