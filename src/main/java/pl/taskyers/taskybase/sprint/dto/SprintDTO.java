package pl.taskyers.taskybase.sprint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintDTO {
    
    private String name;
    
    private String start;
    
    private String end;
    
}
