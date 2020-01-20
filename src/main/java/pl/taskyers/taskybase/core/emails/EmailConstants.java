package pl.taskyers.taskybase.core.emails;

public interface EmailConstants {
    
    String SENDER_ADDRESS = "noreply@taskyers.com";
    
    String SENDER_PERSONAL = "Taskyers";
    
    String ENCODING = "UTF-8";
    
    String REGISTER_PATH = "/emails/register.ftl";
    
    String REGISTER_URL_TOKEN = "/activateAccount/{tokenPlaceholder}";
    
    String PASSWORD_RECOVERY_PATH = "/emails/recoveryPassword.ftl";
    
    String PASSWORD_RECOVERY_URL_TOKEN = "/passwordRecovery/{tokenPlaceholder}";
    
    String PROJECT_INVITATION_PATH = "/emails/projectInvitation.ftl";
    
    String PROJECT_INVITATION_URL_TOKEN = "/secure/projectInvitation/{tokenPlaceholder}";
    
    String TASK_UPDATED_PATH = "/emails/taskUpdated.ftl";
    
    String EMAIL_UPDATE_PATH = "/emails/emailUpdate.ftl";
    
    String EMAIL_UPDATE_URL_TOKEN = "/secure/settings/user/email/{tokenPlaceholder}";
    
}
