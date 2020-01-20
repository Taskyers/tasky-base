package pl.taskyers.taskybase.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.dashboard.project.dto.EntryDTO;
import pl.taskyers.taskybase.task.ResolutionType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDetailsDTO {
    
    private Long id;
    
    private String key;
    
    private String name;
    
    private String description;
    
    private String assignee;
    
    private boolean assignedToMe;
    
    private String creator;
    
    private List<String> watchers;
    
    private boolean watching;
    
    private List<CommentDTO> comments;
    
    private EntryDTO status;
    
    private EntryDTO type;
    
    private EntryDTO priority;
    
    private String fixVersion;
    
    private SprintDTO sprint;
    
    private String creationDate;
    
    private String updateDate;
    
    private ResolutionType resolution;

}
