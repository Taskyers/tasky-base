package pl.taskyers.taskybase.core.dao;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

/**
 * Interface for operations on token entities
 *
 * @param <T> token entity
 * @author Jakub Sildatk
 */
public interface TokenDAO<T> {
    
    /**
     * Generating token based on UUID until it is unique
     *
     * @return generated token as string
     * @since 0.0.1
     */
    String generateToken();
    
    /**
     * Get token as string by user entity
     *
     * @param userEntity user entity that has token assigned to it
     * @return token as string assigned to user entity
     * @since 0.0.1
     */
    String getToken(UserEntity userEntity);
    
    /**
     * Get token entity by token as string
     *
     * @param token token as string
     * @return token entity that contains provided token
     * @since 0.0.1
     */
    T getTokenEntity(String token);
    
    /**
     * Create and save to database token which will be assigned to passed user
     *
     * @param userEntity user that token will be created for
     * @since 0.0.1
     */
    void createToken(UserEntity userEntity);
    
    /**
     * Create and save to database token which will be assigned to passed user
     *
     * @param userEntity    user that token will be created for
     * @param projectEntity project that token will be created for
     * @since 0.0.3
     */
    void createToken(UserEntity userEntity, ProjectEntity projectEntity);
    
    /**
     * Create and save to database token which will be assigned to passed user
     *
     * @param userEntity user that token will be created for
     * @param email      email
     * @since 0.0.7
     */
    void createToken(UserEntity userEntity, String email);
    
    /**
     * Delete from database token entity by token as string
     *
     * @param token token that will be deleted
     * @since 0.0.1
     */
    void deleteToken(String token);
    
}
