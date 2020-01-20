package pl.taskyers.taskybase.settings.user.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.settings.user.dto.PasswordDTO;
import pl.taskyers.taskybase.settings.user.dto.UserDTO;

public interface UserSettingsSLO {
    
    String USER_SETTINGS_PREFIX = "/secure/settings/user";
    
    String UPDATE_PASSWORD = "/password";
    
    String UPDATE_EMAIL = "/email";
    
    String ACCEPT_NEW_EMAIL = UPDATE_EMAIL + "/{token}";
    
    /**
     * Get user details as DTO
     *
     * @return status 200 with user details as DTO if user is logged in, status 403 if user has no permission to do the operation
     * @since 0.0.7
     */
    UserDTO getUserDetails();
    
    /**
     * Update user details - name and description
     *
     * @param userDTO DTO
     * @return status 200 with updated DTO if data was updated successfully, status 400 if user with email already exists in database,
     * status 403 if user has no permission to do the operation
     * @since 0.0.7
     */
    ResponseEntity updateUser(UserDTO userDTO);
    
    /**
     * Update user's password with saving to database
     *
     * @param passwordDTO DTO
     * @return status 200 if data was updated successfully, status 400 if new password or current password was invalid,
     * status 403 if user has no permission to do the operation
     * @since 0.0.7
     */
    ResponseEntity updatePassword(PasswordDTO passwordDTO);
    
    /**
     * Method for creating email update token and then sending email to passed email address with generated token
     *
     * @param email email address which was provided in email update form
     * @return status 200 if email was sent, status 500 if email could not be sent
     * @since 0.0.7
     */
    ResponseEntity sendTokenToNewEmail(String email);
    
    /**
     * Method for updating user email based on email update token which is assigned to user and removing this token from database
     *
     * @param token email update token which was passed by path variable
     * @return status 200 if email was updated, 404 if provided token was not found in database
     * @since 0.0.7
     */
    ResponseEntity acceptNewEmail(String token);
    
}
