package pl.taskyers.taskybase.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.task.slo.TaskDetailsSLO;

import static pl.taskyers.taskybase.task.slo.TaskDetailsSLO.GET_TASK_BY_KEY;
import static pl.taskyers.taskybase.task.slo.TaskDetailsSLO.TASK_DETAILS_PREFIX;

@RestController
@RequestMapping(value = TASK_DETAILS_PREFIX)
@AllArgsConstructor
public class TaskDetailsRestController {
    
    private final TaskDetailsSLO taskDetailsSLO;
    
    @RequestMapping(value = GET_TASK_BY_KEY, method = RequestMethod.GET)
    public ResponseEntity getTaskDetails(@PathVariable String key) {
        return taskDetailsSLO.getTaskDetails(key);
    }
    
}
