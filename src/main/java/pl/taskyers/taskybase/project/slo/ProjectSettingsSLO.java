package pl.taskyers.taskybase.project.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.project.dto.ProjectDTO;

/**
 * Interface for all operations on project's settings tab
 *
 * @author Jakub Sildatk
 */
public interface ProjectSettingsSLO {
    
    String PROJECT_SETTINGS_PREFIX = "/secure/projects/settings";
    
    String GET_PROJECT_BY_ID = "/{id}";
    
    /**
     * Update basic information about project - name and description
     *
     * @param id         id of the given project as path variable
     * @param projectDTO DTO with project's name and description
     * @return status 200 with updated DTO if data was updated successfully, status 400 if project with name already exists in database,
     * status 403 if user has no permission to do the operation, status 404 if project was not found by id
     * @since 0.0.3
     */
    ResponseEntity updateBasicData(Long id, ProjectDTO projectDTO);
    
    /**
     * Delete project by id
     *
     * @param id id of the given project as path variable
     * @return status 200 if project was deleted, status 404 if project was not found by id
     * @since 0.0.3
     */
    ResponseEntity deleteProject(Long id);
    
}
