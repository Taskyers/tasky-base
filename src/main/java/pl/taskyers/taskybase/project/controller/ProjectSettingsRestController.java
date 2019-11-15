package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.slo.ProjectSettingsSLO;

import static pl.taskyers.taskybase.project.slo.ProjectSettingsSLO.*;

@RestController
@RequestMapping(value = PROJECT_SETTINGS_PREFIX)
@AllArgsConstructor
public class ProjectSettingsRestController {
    
    private final ProjectSettingsSLO projectSettingsSLO;
    
    @RequestMapping(value = GET_PROJECT_BY_ID, method = RequestMethod.PUT)
    public ResponseEntity updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        return projectSettingsSLO.updateBasicData(id, projectDTO);
    }
    
    @RequestMapping(value = GET_PROJECT_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity deleteProject(@PathVariable Long id) {
        return projectSettingsSLO.deleteProject(id);
    }
    
    @RequestMapping(value = GET_PROJECT_BY_NAME, method = RequestMethod.GET)
    public ResponseEntity userCanChangeSettings(@PathVariable String projectName) {
        return projectSettingsSLO.hasProperRoleOnEntry(projectName);
    }
    
}
