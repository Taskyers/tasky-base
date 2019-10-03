package pl.taskyers.taskybase.core.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Message {
    
    private String message;
    
    private MessageType type;
    
}