package pl.taskyers.taskybase.registration.slo;

import org.springframework.http.ResponseEntity;

public interface AccountActivationSLO {
    
    String ACTIVATION_ACCOUNT_PREFIX = "/activateAccount";
    
    String ACTIVATE_ACCOUNT_BY_TOKEN = "/{token}";
    
    ResponseEntity activateAccount(String token);
    
}
