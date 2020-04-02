package pl.taskyers.taskybase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EntryTaskData {
    
    private List<String> types;
    
    private List<String> priorities;
    
    private List<String> statuses;
    
    private List<SprintDTO> sprints;
    
}
