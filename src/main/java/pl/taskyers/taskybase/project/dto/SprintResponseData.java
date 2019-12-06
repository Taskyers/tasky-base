package pl.taskyers.taskybase.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintResponseData {
    
    private Long id;
    
    private boolean current;
    
    private String name;
    
    private String start;
    
    private String end;
    
}
