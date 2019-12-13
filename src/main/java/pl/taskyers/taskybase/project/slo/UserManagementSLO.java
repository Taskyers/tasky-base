package pl.taskyers.taskybase.project.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.core.roles.EntryEndpoint;
import pl.taskyers.taskybase.project.dto.RolesWrapper;

public interface UserManagementSLO extends EntryEndpoint {
    
    String USER_MANAGEMENT_PREFIX = "/secure/project/settings/users";
    
    String GET_BY_ID = "/{userId}";
    
    String GET_BY_NAME = "/{projectName}";
    
    String GET_BY_ID_AND_NAME = GET_BY_ID + GET_BY_NAME;
    
    /**
     * Update all user's roles that are assigned to given project
     *
     * @param userId      id of user
     * @param projectName project
     * @param roles       list of roles as dto
     * @return status 404 if user or project was not found, status 403 if user has no permission for operation,
     * status 200 if roles were updated
     * @since 0.0.3
     */
    ResponseEntity updateUserRoles(Long userId, String projectName, RolesWrapper roles);
    
    /**
     * Delete user from project by id
     *
     * @param userId      id
     * @param projectName project name
     * @return status 404 if user or project was not found, status 403 if user has no permission for operation,
     * status 200 if user was deleted
     * @since 0.0.3
     */
    ResponseEntity deleteUser(Long userId, String projectName);
    
    /**
     * Get all roles assigned to user with id
     *
     * @param userId      id
     * @param projectName project name
     * @return status 404 if user or project was not found, status 403 if user has no permission for operation,
     * status 200 with list containing all roles
     * @since 0.0.3
     */
    ResponseEntity getUserRoles(Long userId, String projectName);
    
}
