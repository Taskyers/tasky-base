package pl.taskyers.taskybase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    
    private String name;
    
    private String description;
    
    private String status;
    
    private String priority;
    
    private String type;
    
    private String fixVersion;
    
    private String sprint;
    
}
