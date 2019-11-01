package pl.taskyers.taskybase.registration.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.core.users.dto.AccountDTO;

/**
 * Interface for all operations connected with registration
 *
 * @author Jakub Sildatk
 */
public interface RegistrationSLO {
    
    String REGISTRATION_PREFIX = "/register";
    
    String FIND_USER_BY_USERNAME = "/checkByUsername/{username}";
    
    String FIND_USER_BY_EMAIL = "/checkByEmail/{email}";
    
    /**
     * Validating user account, then saving new user to database and sending an email to address provided in registration form
     * Transaction is rollbacked if MailConnectException occurs
     *
     * @param accountDTO User account (username, email, password, name, surname) from form as JSON
     * @return status 201 with header which contains URI to created user and created user excluding password field
     * Status 400 if validation failed, status 500 if email could not be sent
     * @since 0.0.1
     */
    ResponseEntity register(AccountDTO accountDTO);
    
    /**
     * Checking if user already exists by username - used for validation on frontend server
     *
     * @param username username provided as path variable
     * @return true if user with provided username exists in database otherwise false
     * @since 0.0.1
     */
    boolean userExistsByUsername(String username);
    
    /**
     * Checking if user already exists by email - used for validation on frontend server
     *
     * @param email email address provided as path variable
     * @return true if user with provided email exists in database otherwise false
     * @since 0.0.1
     */
    boolean userExistsByEmail(String email);
    
}
