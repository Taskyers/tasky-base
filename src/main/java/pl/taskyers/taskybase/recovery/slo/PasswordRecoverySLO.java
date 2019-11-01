package pl.taskyers.taskybase.recovery.slo;

import org.springframework.http.ResponseEntity;

/**
 * Interface for password recovery
 *
 * @author Jakub Sildatk
 */
public interface PasswordRecoverySLO {
    
    String PASSWORD_RECOVERY_PREFIX = "/passwordRecovery";
    
    String SEND_EMAIL_WITH_TOKEN = "/requestToken";
    
    String SET_NEW_PASSWORD = "/{token}";
    
    /**
     * Method for creating password recovery token and then sending email to passed email address with generated token
     *
     * @param email email address which was provided in password recovery form
     * @return status 200 if email was sent, status 500 if email could not be sent and status 404 if provided email was not found in database
     * @since 0.0.1
     */
    ResponseEntity sendEmailWithToken(String email);
    
    /**
     * Method for updating user password based on password recovery token which is assigned to user and removing this token from database
     *
     * @param token    password recovery token which was passed by path variable
     * @param password new password which was provided in password recovery form
     * @return status 200 if password was updated, 404 if provided token was not found in database
     * @since 0.0.1
     */
    ResponseEntity setNewPassword(String token, String password);
    
}
