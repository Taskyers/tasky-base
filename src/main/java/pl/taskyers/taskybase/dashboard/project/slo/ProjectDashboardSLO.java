package pl.taskyers.taskybase.dashboard.project.slo;

import pl.taskyers.taskybase.core.roles.slo.EntryEndpoint;

/**
 * Interface for project dashboard
 *
 * @author Jakub Sildatk
 */
public interface ProjectDashboardSLO extends EntryEndpoint {
    
    String PROJECT_DASHBOARD_PREFIX = "/secure/dashboard";
    
    String GET_BY_PROJECT = "/{projectName}";
    
}
