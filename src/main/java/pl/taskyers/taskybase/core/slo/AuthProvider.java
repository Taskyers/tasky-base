package pl.taskyers.taskybase.core.slo;

import pl.taskyers.taskybase.core.users.entity.UserEntity;

/**
 * Interface for providing data about currently logged user
 *
 * @author Jakub Sildatk
 */

public interface AuthProvider {
    
    /**
     * Getting logged user's username
     *
     * @return user's username
     * @since 0.0.1
     */
    String getUserLogin();
    
    /**
     * Getting logged user entity using getUserLogin()
     *
     * @return user's entity
     * @since 0.0.1
     */
    UserEntity getUserEntity();
    
    /**
     * Getting logged user's email using getUserEntity()
     *
     * @return user's email
     * @since 0.0.1
     */
    String getUserEmail();
    
    /**
     * Getting logged user's name using getUserEntity()
     *
     * @return user's name
     * @since 0.0.1
     */
    String getUserName();
    
    /**
     * Getting logged user's surname using getUserEntity()
     *
     * @return user's surname
     * @since 0.0.1
     */
    String getUserSurname();
    
    /**
     * Getting logged user's personals - name and surname using getUserName() and getUserSurname()
     *
     * @return user's personals
     * @since 0.0.1
     */
    String getUserPersonal();
    
}
