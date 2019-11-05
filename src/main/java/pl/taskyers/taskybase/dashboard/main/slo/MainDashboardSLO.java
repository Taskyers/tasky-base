package pl.taskyers.taskybase.dashboard.main.slo;

import pl.taskyers.taskybase.dashboard.main.dto.ProjectDTO;

import java.util.List;

/**
 * Interface for operations with main dashboard
 *
 * @author Jakub Sildatk
 */
public interface MainDashboardSLO {
    
    String MAIN_DASHBOARD_PREFIX = "/secure/mainDashboard";
    
    String GET_PROJECTS = "/projects";
    
    int NUMBER_OF_PROJECTS = 8;
    
    /**
     * Get max n projects assigned to the currently logged user
     *
     * @param n number of projects that will be returned
     * @return list containing n project dto with name, description and owner's personals, sorted by creation date
     * @since 0.0.3
     */
    List<ProjectDTO> getProjects(int n);
    
}
