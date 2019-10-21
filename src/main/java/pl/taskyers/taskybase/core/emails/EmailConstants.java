package pl.taskyers.taskybase.core.emails;

public interface EmailConstants {
    
    String SENDER_ADDRESS = "noreply@taskyers.com";
    
    String SENDER_PERSONAL = "Taskyers";
    
    String ENCODING = "UTF-8";
    
    String REGISTER_PATH = "/emails/register.ftl";
    
    String REGISTER_URL_TOKEN = "/activateAccount/{tokenPlaceholder}";
    
    String PASSWORD_RECOVERY_PATH = "/emails/recoveryPassword.ftl";
    
    String PASSWORD_RECOVERY_URL_TOKEN = "/passwordRecovery/{tokenPlaceholder}";
    
}
