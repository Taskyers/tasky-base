package pl.taskyers.taskybase.task.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.task.dto.TaskDTO;

import java.util.List;

/**
 * Interface for all operations related to task creation
 *
 * @author Jakub Sildatk
 */
public interface AddingTaskSLO {
    
    String CREATE_TASK_PREFIX = "/secure/tasks/create";
    
    String CREATE_BY_PROJECT = "/{projectName}";
    
    List<String> getProjects();

    ResponseEntity getEntryData(String projectName);
    
    ResponseEntity createTask(String projectName, TaskDTO taskDTO);
    
}
