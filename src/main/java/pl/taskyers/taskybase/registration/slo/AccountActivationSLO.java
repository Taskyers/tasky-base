package pl.taskyers.taskybase.registration.slo;

import org.springframework.http.ResponseEntity;

/**
 * Interface for activating new user's account
 *
 * @author Jakub Sildatk
 */
public interface AccountActivationSLO {
    
    String ACTIVATION_ACCOUNT_PREFIX = "/activateAccount";
    
    String ACTIVATE_ACCOUNT_BY_TOKEN = "/{token}";
    
    /**
     * Activating user's account based on verification token and deleting token after activation
     *
     * @param token verification token which was provided by path variable
     * @return status 200 if account was activated, status 404 if token was not found
     * @since 0.0.1
     */
    ResponseEntity activateAccount(String token);
    
}
