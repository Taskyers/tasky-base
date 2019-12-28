package pl.taskyers.taskybase.project.slo;

import org.springframework.http.ResponseEntity;

/**
 * Interface for project's board
 *
 * @author Jakub Sildatk
 */
public interface BoardSLO {
    
    String BOARD_PREFIX = "/secure/board";
    
    String GET_BY_PROJECT_NAME = "/{projectName}";
    
    /**
     * Get current sprint's board
     *
     * @param projectName project's name
     * @return status 404 if project was not found, 403 if user is not in project otherwise 200
     */
    ResponseEntity getBoard(String projectName);
    
}
