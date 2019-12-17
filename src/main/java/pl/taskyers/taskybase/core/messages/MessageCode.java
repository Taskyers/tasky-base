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
    email_subject_project_invitation,
    email_with_token_sent,
    entry_created,
    entry_deleted,
    entry_field_already_exists,
    entry_not_found,
    entry_updated,
    field_empty,
    field_invalid_format,
    field_not_found,
    field_updated,
    password_invalid_format,
    project_created,
    project_deleted,
    project_field_already_exists,
    project_invitation_acceptance,
    project_not_found,
    project_permission_not_granted,
    project_updated,
    registration_successful,
    roles_updated,
    sprint_created,
    sprint_deleted,
    sprint_field_already_exists,
    sprint_not_found,
    sprint_invalid_date,
    sprint_updated,
    server_problem_occured,
    task_created,
    task_field_already_exists,
    user_already_in_project,
    user_field_already_exists,
    user_not_found,
    user_in_project_not_found,
    user_removed_from_project;
    
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