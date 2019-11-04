package pl.taskyers.taskybase.core.messages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@Slf4j
public enum MessageCode {
    account_activated,
    email_subject_registration,
    email_subject_password_recovery,
    email_with_token_sent,
    field_empty,
    field_invalid_format,
    field_not_found,
    field_updated,
    password_invalid_format,
    project_created,
    project_field_already_exists,
    registration_successful,
    server_problem_occured,
    user_field_already_exists;
    
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