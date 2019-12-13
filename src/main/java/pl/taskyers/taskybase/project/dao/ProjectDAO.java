package pl.taskyers.taskybase.project.dao;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface for operations on project entity
 *
 * @author Jakub Sildatk
 */
public interface ProjectDAO {
    
    String PROJECTS_PREFIX = "/secure/projects";
    
    /**
     * Get n currently logged in user's projects as DTO converted from ProjectEntity
     *
     * @param n number of projects to be returned
     * @return n projects as DTO
     * @since 0.0.3
     */
    List<ProjectDTO> getProjects(int n);
    
    /**
     * Get all projects for currently logged in user
     *
     * @return all projects as DTO
     * @since 0.0.3
     */
    List<ProjectDTO> getAllProjects();
    
    /**
     * Save new project to database - currently logged user will be the owner
     * Users collection will only contain owner
     *
     * @param projectEntity project entity to be saved
     * @return saved project entity
     * @since 0.0.3
     */
    ProjectEntity addNewProject(ProjectEntity projectEntity);
    
    /**
     * Get project entity by project's name
     *
     * @param name project's name
     * @return project entity as optional
     * @since 0.0.3
     */
    Optional<ProjectEntity> getProjectEntityByName(String name);
    
    /**
     * Get project entity by project entity and user entity
     *
     * @param projectEntity project entity
     * @param userEntity    user entity
     * @return project entity
     * @since 0.0.3
     */
    ProjectEntity addUserToProject(ProjectEntity projectEntity, UserEntity userEntity);
    
    /**
     * Get project entity by project's id
     *
     * @param id project's id
     * @return project entity as optional
     * @since 0.0.3
     */
    Optional<ProjectEntity> getProjectEntityById(Long id);
    
    /**
     * Update project basic data - name and description
     *
     * @param projectEntity existing project as entity
     * @param name          new project's name
     * @param description   new project's description
     * @return updated project entity
     * @since 0.0.3
     */
    ProjectEntity updateProject(ProjectEntity projectEntity, String name, String description);
    
    /**
     * Delete project by id
     *
     * @param id id of project
     * @since 0.0.3
     */
    void deleteProjectById(Long id);
    
    /**
     * Get project user by name
     *
     * @return project
     * @since 0.0.3
     */
    Optional<ProjectEntity> getProjectByNameAndUser(String projectName, UserEntity userEntity);
    
    /**
     * Remove user from project
     *
     * @param userId      id
     * @param projectName project's name
     * @since 0.0.3
     */
    void deleteUserInProject(Long userId, String projectName);
    
    /**
     * Get project by name and owner
     *
     * @param projectName name
     * @param owner       project's creator
     * @return project entity as optional
     * @since 0.0.3
     */
    Optional<ProjectEntity> getProjectByNameAndOwner(String projectName, UserEntity owner);
    
}
