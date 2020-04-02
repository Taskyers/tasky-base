package pl.taskyers.taskybase.dashboard.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectDTO {
    
    private String name;
    
    private String description;
    
    private String owner;
    
}
