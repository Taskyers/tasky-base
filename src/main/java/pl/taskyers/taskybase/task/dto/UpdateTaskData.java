package pl.taskyers.taskybase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskData {
    
    private String name;
    
    private String description;
    
    private String fixVersion;
    
    private String sprint;
    
    private String resolution;
    
}
