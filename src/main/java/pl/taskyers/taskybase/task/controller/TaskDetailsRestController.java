package pl.taskyers.taskybase.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.dashboard.project.dto.TaskDTO;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.task.slo.TaskDetailsSLO;

import java.util.List;

import static pl.taskyers.taskybase.task.slo.TaskDetailsSLO.*;

@RestController
@RequestMapping(value = TASK_DETAILS_PREFIX)
@AllArgsConstructor
public class TaskDetailsRestController {
    
    private final TaskDetailsSLO taskDetailsSLO;
    
    @RequestMapping(value = GET_TASK_BY_KEY, method = RequestMethod.GET)
    public ResponseEntity getTaskDetails(@PathVariable String key) {
        return taskDetailsSLO.getTaskDetails(key);
    }
    
    @RequestMapping(value = GET_STATUSES, method = RequestMethod.GET)
    public ResponseEntity getStatuses(@PathVariable String key) {
        return taskDetailsSLO.getEntries(key, EntryType.STATUS);
    }
    
    @RequestMapping(value = GET_TYPES, method = RequestMethod.GET)
    public ResponseEntity getTypes(@PathVariable String key) {
        return taskDetailsSLO.getEntries(key, EntryType.TYPE);
    }
    
    @RequestMapping(value = GET_PRIORITIES, method = RequestMethod.GET)
    public ResponseEntity getPriorities(@PathVariable String key) {
        return taskDetailsSLO.getEntries(key, EntryType.PRIORITY);
    }
    
    @RequestMapping(value = GET_RESOLUTION_TYPES, method = RequestMethod.GET)
    public ResponseEntity getResolutionTypes(@PathVariable String key) {
        return taskDetailsSLO.getResolutionTypes(key);
    }
    
    @RequestMapping(value = GET_TASKS_BY_NAME, method = RequestMethod.GET)
    public List<TaskDTO> getUserTasks(@PathVariable String name) {
        return taskDetailsSLO.getUserTasksByName(name);
    }
    
}
