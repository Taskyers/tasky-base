package pl.taskyers.taskybase.dashboard.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDashboardResponseData {
    
    // TODO list of tasks
    
    private boolean manageUsers;
    
    private boolean editProject;
    
    private boolean invite;
    
    private boolean manageStatuses;
    
    private boolean manageTypes;
    
    private boolean managePriorities;
    
    private boolean manageSprints;
    
}
