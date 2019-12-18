package pl.taskyers.taskybase.task.slo;

import org.springframework.http.ResponseEntity;

/**
 * Interface for all operations related to editing tasks: 'assign to me', 'watch this task'
 *
 * @author Jakub Sildatk
 */
public interface EditTaskSLO {
    
    String EDIT_TASK_PREFIX = "/secure/tasks/edit";
    
    String ASSIGN_TO_ME = "/assignToMe/{id}";

    String WATCH_THIS_TASK = "/watch/{id}";
    
    ResponseEntity assignToMe(Long id);
    
    ResponseEntity watchThisTask(Long id);
    
}
