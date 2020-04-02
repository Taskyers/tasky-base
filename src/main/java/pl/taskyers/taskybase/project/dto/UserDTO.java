package pl.taskyers.taskybase.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    
    private String personals;
    
    private String username;
    
    private String email;
    
}
