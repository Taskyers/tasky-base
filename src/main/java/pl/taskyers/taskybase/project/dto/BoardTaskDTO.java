package pl.taskyers.taskybase.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.dashboard.project.dto.EntryDTO;

@Data
@NoArgsConstructor
public class BoardTaskDTO {
    
    private String key;
    
    private String name;
    
    private String assignee;
    
    private EntryDTO type;
    
    private EntryDTO priority;
    
}
