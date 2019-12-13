package pl.taskyers.taskybase.core.users.dao;

import pl.taskyers.taskybase.core.users.entity.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface for operations on user entity
 *
 * @author Jakub Sildatk
 */
public interface UserDAO {
    
    /**
     * Add new user to database with encoded password
     *
     * @param userEntity user entity to be saved
     * @return saved user entity
     * @since 0.0.1
     */
    UserEntity registerUser(UserEntity userEntity);
    
    /**
     * Update user's password with saving to database
     *
     * @param userEntity user entity which password will be updated
     * @param password   new password
     * @since 0.0.1
     */
    void updatePassword(UserEntity userEntity, String password);
    
    /**
     * Enable user account with saving to database
     *
     * @param userEntity user entity which will be activated
     * @since 0.0.1
     */
    void enableUser(UserEntity userEntity);
    
    /**
     * Get user entity by email
     *
     * @param email
     * @return optional user entity
     * @since 0.0.1
     */
    Optional<UserEntity> getEntityByEmail(String email);
    
    /**
     * Get user entity by username
     *
     * @param username
     * @return optional user entity
     * @since 0.0.1
     */
    Optional<UserEntity> getEntityByUsername(String username);
    
    /**
     * Get user entity by id
     *
     * @param id
     * @return optional user entity
     * @since 0.0.3
     */
    Optional<UserEntity> getEntityById(Long id);
    
    /**
     * Flush user repository
     *
     * @since 0.0.3
     */
    void flushRepository();
    
    /**
     * Get user entities by username
     *
     * @param username
     * @return UserEntity list
     * @since 0.0.3
     */
    List<UserEntity> findUsersByUsernameLike(String username);
    
}
