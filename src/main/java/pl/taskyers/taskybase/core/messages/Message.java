package pl.taskyers.taskybase.core.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Message {
    
    private String message;
    
    private MessageType type;
    
}