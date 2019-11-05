package pl.taskyers.taskybase.dashboard.main.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;
import pl.taskyers.taskybase.dashboard.main.slo.MainDashboardSLO;

import java.util.List;

import static pl.taskyers.taskybase.dashboard.main.slo.MainDashboardSLO.*;

@RestController
@RequestMapping(value = MAIN_DASHBOARD_PREFIX)
@AllArgsConstructor
public class MainDashboardRestController {
    
    private final MainDashboardSLO mainDashboardSLO;
    
    @RequestMapping(value = GET_PROJECTS, method = RequestMethod.GET)
    public List<ProjectDTO> getProjects() {
        return mainDashboardSLO.getProjects(NUMBER_OF_PROJECTS);
    }
    
}
