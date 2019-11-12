package pl.taskyers.taskybase.project.slo;

import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface for operations on project entity
 *
 * @author Jakub Sildatk
 */
public interface ProjectSLO {
    
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
    
}
