package pl.taskyers.taskybase.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDTO {

    private Long id;
    
    private String author;
    
    private String content;
    
    private String creationDate;
    
    private boolean edited;
    
    private boolean yours;

}
