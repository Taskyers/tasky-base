package pl.taskyers.taskybase.dashboard.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.dashboard.project.slo.ProjectDashboardSLO;

import static pl.taskyers.taskybase.dashboard.project.slo.ProjectDashboardSLO.GET_BY_PROJECT;
import static pl.taskyers.taskybase.dashboard.project.slo.ProjectDashboardSLO.PROJECT_DASHBOARD_PREFIX;

@RestController
@RequestMapping(value = PROJECT_DASHBOARD_PREFIX)
@AllArgsConstructor
public class ProjectDashboardRestController {
    
    private final ProjectDashboardSLO projectDashboardSLO;
    
    @RequestMapping(value = GET_BY_PROJECT, method = RequestMethod.GET)
    public ResponseEntity getData(@PathVariable String projectName) {
        return projectDashboardSLO.hasProperRoleOnEntry(projectName);
    }
    
}
