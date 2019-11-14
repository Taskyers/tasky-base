package pl.taskyers.taskybase.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsEntryResponseData {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private boolean canEditProject;
    
    private boolean canDeleteProject;
    
}
