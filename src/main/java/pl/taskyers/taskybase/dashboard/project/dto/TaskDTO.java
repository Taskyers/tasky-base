package pl.taskyers.taskybase.dashboard.project.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskDTO {
    
    private String key;
    
    private String name;
    
    private EntryDTO type;
    
    private EntryDTO priority;
    
    private EntryDTO status;
    
}
