package pl.taskyers.taskybase.task.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.entry.EntryType;

/**
 * Interface for returning task details
 *
 * @author Jakub Sildatk
 */
public interface TaskDetailsSLO {
    
    String TASK_DETAILS_PREFIX = "/secure/tasks";
    
    String GET_TASK_BY_KEY = "/{key}";
    
    String GET_STATUSES = GET_TASK_BY_KEY + "/statuses";
    
    String GET_TYPES = GET_TASK_BY_KEY + "/types";
    
    String GET_PRIORITIES = GET_TASK_BY_KEY + "/priorities";
    
    /**
     * Return task details
     *
     * @param key task's key
     * @return task details as DTO
     * @since 0.0.7
     */
    ResponseEntity getTaskDetails(String key);
    
    /**
     * Get entries of given type
     *
     * @param key       task's key
     * @param entryType entry type
     * @return list of strings as entries values
     * @since 0.0.7
     */
    ResponseEntity getEntries(String key, EntryType entryType);
    
}
