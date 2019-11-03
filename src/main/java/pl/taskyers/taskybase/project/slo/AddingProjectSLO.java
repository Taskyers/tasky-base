package pl.taskyers.taskybase.project.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.project.dto.ProjectDTO;

/**
 * Interface for adding new project
 */
public interface AddingProjectSLO {
    
    String ADDING_PROJECT_PREFIX = "/secure/addNewProject";
    
    String GET_PROJECT_BY_NAME = "/{name}";
    
    /**
     * Create new project from dto
     *
     * @param projectDTO dto containing only project's name and description
     * @return status 400 with validation messages if validation fails
     * Status 201 with saved project entity if validation was passed
     * @since 0.0.3
     */
    ResponseEntity addNewProject(ProjectDTO projectDTO);
    
    /**
     * Check if project already exists by name - used for validation on frontend server
     *
     * @param name name provided as path variable
     * @return true if project with provided name exists in database, otherwise false
     * @since 0.0.3
     */
    boolean projectExistsByName(String name);
    
}
