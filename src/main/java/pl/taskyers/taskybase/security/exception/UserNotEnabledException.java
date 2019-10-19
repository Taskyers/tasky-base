package pl.taskyers.taskybase.security.exception;

public class UserNotEnabledException extends RuntimeException {
    
    public UserNotEnabledException(String message) {
        super(message);
    }
    
}
