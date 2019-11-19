package pl.taskyers.taskybase.core.roles.slo;

import pl.taskyers.taskybase.core.roles.entity.RoleEntity;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface for operations on role entity and role linker entity
 *
 * @author Jakub Sildatk
 */
public interface RoleSLO {
    
    /**
     * Create all roles that are in the database at the moment of project creation with checked set to false.
     * Roles, user and project will be connected by RoleLinkerEntity
     *
     * @param userEntity    user that will be connected to the all roles and single project
     * @param projectEntity project that will be connected to the all roles and single user
     * @see pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity
     * @since 0.0.3
     */
    void createAllRolesForUser(UserEntity userEntity, ProjectEntity projectEntity);
    
    /**
     * Create all roles that are in the database at the moment of project creation with checked set to true
     * Roles, owner and project will be connected by RoleLinkerEntity
     *
     * @param userEntity    owner that will be connected to the all roles and single project
     * @param projectEntity project that will be connected to the all roles and owner of project
     * @see pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity
     * @since 0.0.3
     */
    void createAllRolesForOwner(UserEntity userEntity, ProjectEntity projectEntity);
    
    /**
     * Set checked field in role connected with project and user
     *
     * @param userEntity    user that will have changed checked property with project and role
     * @param projectEntity project that is connected to the role and user
     * @param roleEntity    role that will be updated in user and project
     * @param value         new checked value
     * @since 0.0.3
     */
    void setRole(UserEntity userEntity, ProjectEntity projectEntity, RoleEntity roleEntity, boolean value);
    
    /**
     * Check if user has permission for given role as string and project as entity
     *
     * @param userEntity    user
     * @param projectEntity project
     * @param role          role key
     * @return true if checked is set to true in role linker, if role linker is not present or checked is set to false
     * @since 0.0.3
     */
    boolean hasPermission(UserEntity userEntity, ProjectEntity projectEntity, String role);
    
    /**
     * Get role entity by role key
     *
     * @param key key
     * @return entity as optional
     */
    Optional<RoleEntity> getEntityByKey(String key);
    
    /**
     * Get all linkers assigned to user and project
     *
     * @param userEntity
     * @param projectEntity
     * @return all linkers as list
     * @since 0.0.3
     */
    List<RoleLinkerEntity> getRoleLinkersByUserAndProject(UserEntity userEntity, ProjectEntity projectEntity);
    
}
