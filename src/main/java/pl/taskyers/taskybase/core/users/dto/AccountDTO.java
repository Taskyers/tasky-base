package pl.taskyers.taskybase.core.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    
    private String username;
    
    private String email;
    
    private String password;
    
    private String name;
    
    private String surname;
    
}
