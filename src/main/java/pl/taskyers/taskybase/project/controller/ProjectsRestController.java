package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.slo.AddingProjectSLO;
import pl.taskyers.taskybase.project.dao.ProjectDAO;

import java.util.List;

import static pl.taskyers.taskybase.project.slo.AddingProjectSLO.GET_PROJECT_BY_NAME;
import static pl.taskyers.taskybase.project.dao.ProjectDAO.PROJECTS_PREFIX;

@RestController
@RequestMapping(value = PROJECTS_PREFIX)
@AllArgsConstructor
public class ProjectsRestController {
    
    private final AddingProjectSLO addingProjectSLO;
    
    private final ProjectDAO projectDAO;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO> getAllProjects() {
        return projectDAO.getAllProjects();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveNewProject(@RequestBody ProjectDTO projectDTO) {
        return addingProjectSLO.addNewProject(projectDTO);
    }
    
    @RequestMapping(value = GET_PROJECT_BY_NAME, method = RequestMethod.GET)
    public boolean projectExistsByName(@PathVariable String name) {
        return addingProjectSLO.projectExistsByName(name);
    }
    
}
