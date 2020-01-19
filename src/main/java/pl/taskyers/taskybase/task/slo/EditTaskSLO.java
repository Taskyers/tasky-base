package pl.taskyers.taskybase.task.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.task.dto.UpdateTaskData;

/**
 * Interface for all operations related to editing tasks:
 * 'assign to me', 'watch this task', 'update status', 'update type', 'update priority', 'update data'
 *
 * @author Jakub Sildatk
 */
public interface EditTaskSLO {
    
    String EDIT_TASK_PREFIX = "/secure/tasks/edit";
    
    String ASSIGN_TO_ME = "/assignToMe/{id}";
    
    String WATCH_THIS_TASK = "/watch/{id}";
    
    String UPDATE_STATUS = "/status/{id}";
    
    String UPDATE_TYPE = "/type/{id}";
    
    String UPDATE_PRIORITY = "/priority/{id}";
    
    String UPDATE_RESOLUTION = "/resolution/{id}";
    
    String UPDATE_DATA = "/{id}";
    
    /**
     * Assign task to currently logged in user
     *
     * @param id task's id
     * @return status 404 if task was not found, 403 if user is not in project, 400 if user is already assigned to this task otherwise 200
     * @since 0.0.7
     */
    ResponseEntity assignToMe(Long id);
    
    /**
     * Add currently logged in user to task's watchers collection
     *
     * @param id task's id
     * @return status 404 if task was not found, 403 if user is not in project, 400 if user is already in collection otherwise 200
     * @since 0.0.7
     */
    ResponseEntity watchThisTask(Long id);
    
    /**
     * Update task status
     *
     * @param id        task's id
     * @param value     new entry value
     * @param entryType entry type
     * @return status 404 if task was not found, 404 if value was not found, 403 if user is not in project otherwise 200
     * @since 0.0.7
     */
    ResponseEntity updateEntry(Long id, String value, EntryType entryType);
    
    /**
     * Update task resolution type
     *
     * @param id    task's id
     * @param value resolution value
     * @return status 404 if task was not found, 404 if value was not found, 403 if user is not in project otherwise 200
     * @since 0.0.7
     */
    ResponseEntity updateResolution(Long id, String value);
    
    /**
     * Update rest of task data
     *
     * @param id       task's id
     * @param taskData task data
     * @return status 404 if task or sprint was not found, 403 if user is not in project otherwise 200
     * @since 0.0.7
     */
    ResponseEntity updateData(Long id, UpdateTaskData taskData);
    
}
