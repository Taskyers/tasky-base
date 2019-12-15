package pl.taskyers.taskybase.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.task.dto.TaskDTO;
import pl.taskyers.taskybase.task.slo.AddingTaskSLO;

import java.util.List;

import static pl.taskyers.taskybase.task.slo.AddingTaskSLO.CREATE_BY_PROJECT;
import static pl.taskyers.taskybase.task.slo.AddingTaskSLO.CREATE_TASK_PREFIX;

@RestController
@RequestMapping(value = CREATE_TASK_PREFIX)
@AllArgsConstructor
public class AddingTaskRestController {
    
    private final AddingTaskSLO addingTaskSLO;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<String> getProjects() {
        return addingTaskSLO.getProjects();
    }
    
    @RequestMapping(value = CREATE_BY_PROJECT, method = RequestMethod.GET)
    public ResponseEntity getEntryData(@PathVariable String projectName) {
        return addingTaskSLO.getEntryData(projectName);
    }
    
    @RequestMapping(value = CREATE_BY_PROJECT, method = RequestMethod.POST)
    public ResponseEntity createTask(@PathVariable String projectName, @RequestBody TaskDTO taskDTO) {
        return addingTaskSLO.createTask(projectName, taskDTO);
    }
    
}
