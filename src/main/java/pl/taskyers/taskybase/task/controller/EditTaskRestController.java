package pl.taskyers.taskybase.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.task.slo.EditTaskSLO;

import static pl.taskyers.taskybase.task.slo.EditTaskSLO.*;

@RestController
@RequestMapping(value = EDIT_TASK_PREFIX)
@AllArgsConstructor
public class EditTaskRestController {
    
    private final EditTaskSLO editTaskSLO;
    
    @RequestMapping(value = ASSIGN_TO_ME, method = RequestMethod.PATCH)
    public ResponseEntity assignToMe(@PathVariable Long id) {
        return editTaskSLO.assignToMe(id);
    }
    
    @RequestMapping(value = WATCH_THIS_TASK, method = RequestMethod.PATCH)
    public ResponseEntity watchTask(@PathVariable Long id) {
        return editTaskSLO.watchThisTask(id);
    }
    
}
