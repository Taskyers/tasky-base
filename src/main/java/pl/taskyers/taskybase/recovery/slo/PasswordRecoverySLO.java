package pl.taskyers.taskybase.recovery.slo;

import org.springframework.http.ResponseEntity;

public interface PasswordRecoverySLO {
    
    String PASSWORD_RECOVERY_PREFIX = "/passwordRecovery";
    
    String SEND_EMAIL_WITH_TOKEN = "/requestToken";
    
    String SET_NEW_PASSWORD = "/{token}";
    
    ResponseEntity sendEmailWithToken(String email);
    
    ResponseEntity setNewPassword(String token, String password);
    
}
