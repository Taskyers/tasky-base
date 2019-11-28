package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.project.slo.SprintManagementSLO;
import pl.taskyers.taskybase.sprint.dto.SprintDTO;

import static pl.taskyers.taskybase.project.slo.SprintManagementSLO.*;

@RestController
@RequestMapping(value = SPRINTS_MANAGEMENT_PREFIX)
@AllArgsConstructor
public class SprintManagementRestController {
    
    private final SprintManagementSLO sprintManagementSLO;
    
    @RequestMapping(value = GET_BY_PROJECT_NAME, method = RequestMethod.GET)
    public ResponseEntity hasProperRoleOnEntry(@PathVariable String projectName) {
        return sprintManagementSLO.hasProperRoleOnEntry(projectName);
    }
    
    @RequestMapping(value = GET_DATA_BY_SPRINT_ID, method = RequestMethod.GET)
    public ResponseEntity getData(@PathVariable Long sprintId) {
        return sprintManagementSLO.getData(sprintId);
    }
    
    @RequestMapping(value = GET_BY_PROJECT_NAME, method = RequestMethod.POST)
    public ResponseEntity createNewSprint(@RequestBody SprintDTO sprintDTO, @PathVariable String projectName) {
        return sprintManagementSLO.createNew(sprintDTO, projectName);
    }
    
    @RequestMapping(value = GET_BY_SPRINT_ID, method = RequestMethod.PUT)
    public ResponseEntity updateSprint(@RequestBody SprintDTO sprintDTO, @PathVariable Long sprintId) {
        return sprintManagementSLO.update(sprintId, sprintDTO);
    }
    
    @RequestMapping(value = GET_BY_SPRINT_ID, method = RequestMethod.DELETE)
    public ResponseEntity deleteSprint(@PathVariable Long sprintId) {
        return sprintManagementSLO.delete(sprintId);
    }
    
    @RequestMapping(value = GET_BY_SPRINT_NAME_AND_PROJECT_NAME, method = RequestMethod.GET)
    public ResponseEntity doesNameExistsInProject(@PathVariable String name, @PathVariable String projectName) {
        return sprintManagementSLO.checkForNameInProject(name, projectName);
    }
    
}
