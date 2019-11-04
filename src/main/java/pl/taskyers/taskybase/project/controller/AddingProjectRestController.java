package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.slo.AddingProjectSLO;

import static pl.taskyers.taskybase.project.slo.AddingProjectSLO.ADDING_PROJECT_PREFIX;
import static pl.taskyers.taskybase.project.slo.AddingProjectSLO.GET_PROJECT_BY_NAME;

@RestController
@RequestMapping(value = ADDING_PROJECT_PREFIX)
@AllArgsConstructor
public class AddingProjectRestController {
    
    private final AddingProjectSLO addingProjectSLO;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveNewProject(@RequestBody ProjectDTO projectDTO) {
        return addingProjectSLO.addNewProject(projectDTO);
    }
    
    @RequestMapping(value = GET_PROJECT_BY_NAME, method = RequestMethod.GET)
    public boolean projectExistsByName(@PathVariable String name) {
        return addingProjectSLO.projectExistsByName(name);
    }
    
}
