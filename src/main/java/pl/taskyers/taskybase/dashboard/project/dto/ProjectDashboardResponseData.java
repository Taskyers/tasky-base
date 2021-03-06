package pl.taskyers.taskybase.dashboard.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectDashboardResponseData {
    
    List<TaskDTO> tasks;
    
    private boolean manageUsers;
    
    private boolean editProject;
    
    private boolean invite;
    
    private boolean manageStatuses;
    
    private boolean manageTypes;
    
    private boolean managePriorities;
    
    private boolean manageSprints;
    
}
