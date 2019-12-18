package pl.taskyers.taskybase.task.slo;

import org.springframework.http.ResponseEntity;

/**
 * Interface for returning task details
 *
 * @author Jakub Sildatk
 */
public interface TaskDetailsSLO {
    
    String TASK_DETAILS_PREFIX = "/secure/tasks";
    
    String GET_TASK_BY_KEY = "/{key}";
    
    /**
     * Return task details
     *
     * @param key task's key
     * @return task details as DTO
     * @since 0.0.7
     */
    ResponseEntity getTaskDetails(String key);
    
}
