package pl.taskyers.taskybase.dashboard.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    
    private String name;
    
    private String description;
    
    private String ownerPersonals;
    
    private String ownerUsername;
    
}
