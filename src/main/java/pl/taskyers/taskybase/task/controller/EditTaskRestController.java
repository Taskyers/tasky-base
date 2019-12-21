package pl.taskyers.taskybase.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.task.dto.UpdateTaskData;
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
    
    @RequestMapping(value = UPDATE_STATUS, method = RequestMethod.PATCH)
    public ResponseEntity updateStatus(@PathVariable Long id, @RequestParam String value) {
        return editTaskSLO.updateEntry(id, value, EntryType.STATUS);
    }
    
    @RequestMapping(value = UPDATE_TYPE, method = RequestMethod.PATCH)
    public ResponseEntity updateType(@PathVariable Long id, @RequestParam String value) {
        return editTaskSLO.updateEntry(id, value, EntryType.TYPE);
    }
    
    @RequestMapping(value = UPDATE_PRIORITY, method = RequestMethod.PATCH)
    public ResponseEntity updatePriority(@PathVariable Long id, @RequestParam String value) {
        return editTaskSLO.updateEntry(id, value, EntryType.PRIORITY);
    }
    
    @RequestMapping(value = UPDATE_DATA, method = RequestMethod.PUT)
    public ResponseEntity updateData(@PathVariable Long id, @RequestBody UpdateTaskData taskData) {
        return editTaskSLO.updateData(id, taskData);
    }
    
}
