package pl.taskyers.taskybase.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardResponseData {
    
    private String sprintName;
    
    private List<BoardStatusDTO> statuses = new ArrayList<>();
    
}
