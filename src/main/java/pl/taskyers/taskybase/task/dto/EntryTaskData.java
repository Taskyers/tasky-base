package pl.taskyers.taskybase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryTaskData {
    
    private List<String> types;
    
    private List<String> priorities;
    
    private List<String> statuses;
    
    private List<SprintDTO> sprints;
    
}
