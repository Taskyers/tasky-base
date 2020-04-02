package pl.taskyers.taskybase.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardStatusDTO {
    
    private String status;
    
    private List<BoardTaskDTO> tasks;
    
}
