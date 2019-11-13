package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.slo.ProjectSettingsSLO;

import static pl.taskyers.taskybase.project.slo.ProjectSettingsSLO.GET_PROJECT_BY_ID;
import static pl.taskyers.taskybase.project.slo.ProjectSettingsSLO.PROJECT_SETTINGS_PREFIX;

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
    
}
