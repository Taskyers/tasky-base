package pl.taskyers.taskybase.dashboard.main.slo;

import org.springframework.http.ResponseEntity;

/**
 * Interface for operations with main dashboard
 *
 * @author Jakub Sildatk
 */
public interface MainDashboardSLO {
    
    String MAIN_DASHBOARD_PREFIX = "/secure/mainDashboard";
    
    String GET_PROJECTS = "/projects";
    
    int NUMBER_OF_PROJECTS = 5;
    
    /**
     * Get max n projects assigned to the currently logged user
     *
     * @param n number of projects that will be returned
     * @return status 200 with n project dto with name, description, owner's personals and owner's username
     * @since 0.0.3
     */
    ResponseEntity getProjects(int n);
    
}
