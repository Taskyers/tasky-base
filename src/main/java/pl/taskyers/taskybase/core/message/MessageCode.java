package pl.taskyers.taskybase.core.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@Slf4j
public enum MessageCode {
    
    /**
     * Only for test purposes - do not change it
     **/
    test,
    test_1,
    test_1_2,
    test_1_2_3,
    test_1_2_3_4;
    
    private static MessageSource messageSource;
    
    public static void setMessageSource(MessageSource messageSource) {
        MessageCode.messageSource = messageSource;
    }
    
    public String getMessage(Object... args) {
        String message = replaceUnderscores(this.toString());
        try {
            return messageSource.getMessage(message, args, Locale.US);
        } catch ( NoSuchMessageException e ) {
            log.warn("Message with code: " + this.toString() + " (" + message + ") was not found");
            return null;
        }
    }
    
    private String replaceUnderscores(String message) {
        return message.replace("_", ".");
    }
    
}