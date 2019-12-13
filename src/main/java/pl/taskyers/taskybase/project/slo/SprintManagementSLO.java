package pl.taskyers.taskybase.project.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.core.roles.EntryEndpoint;
import pl.taskyers.taskybase.sprint.dto.SprintDTO;

/**
 * Interface for managing sprints
 *
 * @author Jakub Sildatk
 */
public interface SprintManagementSLO extends EntryEndpoint {
    
    String SPRINTS_MANAGEMENT_PREFIX = "/secure/project/settings/sprints";
    
    String GET_BY_PROJECT_NAME = "/{projectName}";
    
    String GET_BY_SPRINT_ID = "/{sprintId}";
    
    String GET_DATA_BY_SPRINT_ID = "/data/{sprintId}";
    
    String GET_BY_SPRINT_NAME_AND_PROJECT_NAME = "/{name}" + GET_BY_PROJECT_NAME;
    
    /**
     * Get sprint data for one sprint by id
     *
     * @param sprintId sprint id
     * @return status 404 if sprint was not found, 403 if user has no access, otherwise 200 with single data
     * @since 0.0.3
     */
    ResponseEntity getData(Long sprintId);
    
    /**
     * Create new sprint from dto and project name
     *
     * @param sprintDTO   dto
     * @param projectName name
     * @return status 404 if project was not found, 403 if user has no access, 400 if validation did not pass, 201 if sprint was created
     * @since 0.0.3
     */
    ResponseEntity createNew(SprintDTO sprintDTO, String projectName);
    
    /**
     * Update existing sprint from DTO
     *
     * @param sprintId  sprint id
     * @param sprintDTO sprint DTO
     * @return status 404 if sprint was not found, 403 if user has no access, 400 if validation did not pass, 200 if sprint was updated
     * @since 0.0.3
     */
    ResponseEntity update(Long sprintId, SprintDTO sprintDTO);
    
    /**
     * Remove sprint from project
     *
     * @param sprintId sprint id
     * @return status 404 if sprint was not found, 403 if user has no access, 200 if sprint was deleted
     * @since 0.0.3
     */
    ResponseEntity delete(Long sprintId);
    
    /**
     * Check if sprint with name already exists in project
     *
     * @param name        sprint name
     * @param projectName project name
     * @return status 404 if project was not found, 403 if user has no access, 200 with boolean if project with name exists
     * @since 0.0.3
     */
    ResponseEntity checkForNameInProject(String name, String projectName);
    
}
