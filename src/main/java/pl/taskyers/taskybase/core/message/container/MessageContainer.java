package pl.taskyers.taskybase.core.message.container;

import pl.taskyers.taskybase.core.message.Message;
import pl.taskyers.taskybase.core.message.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract class MessageContainer<T, K> {
    
    protected List<Message> messages = new ArrayList<>();
    
    public boolean hasErrors() {
        return messages.stream().anyMatch(message -> message.getType().equals(MessageType.ERROR));
    }
    
    public List<Message> getAll() {
        return messages;
    }
    
    public List<Message> getSuccesses() {
        return getAllMessages(MessageType.SUCCESS);
    }
    
    public List<Message> getWarns() {
        return getAllMessages(MessageType.WARN);
    }
    
    public List<Message> getErrors() {
        return getAllMessages(MessageType.ERROR);
    }
    
    private List<Message> getAllMessages(MessageType messageType) {
        return messages.stream().filter(message -> message.getType().equals(messageType)).collect(Collectors.toList());
    }
    
}
