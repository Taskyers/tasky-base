package pl.taskyers.taskybase.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SettingsEntryResponseData {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private boolean canEditProject;
    
    private boolean canDeleteProject;
    
}
