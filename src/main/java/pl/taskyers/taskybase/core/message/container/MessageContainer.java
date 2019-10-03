package pl.taskyers.taskybase.core.message.container;

import pl.taskyers.taskybase.core.message.Message;
import pl.taskyers.taskybase.core.message.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract class MessageContainer<T, K> {
    
    List<Message> messages = new ArrayList<>();
    
    protected boolean hasErrors() {
        return messages.stream().anyMatch(message -> message.getType().equals(MessageType.ERROR));
    }
    
    protected List<Message> getAll() {
        return messages;
    }
    
    protected List<Message> getSuccesses() {
        return getAllMessages(MessageType.SUCCESS);
    }
    
    protected List<Message> getWarns() {
        return getAllMessages(MessageType.WARN);
    }
    
    protected List<Message> getErrors() {
        return getAllMessages(MessageType.ERROR);
    }
    
    private List<Message> getAllMessages(MessageType messageType) {
        return messages.stream().filter(message -> message.getType().equals(messageType)).collect(Collectors.toList());
    }
    
}
