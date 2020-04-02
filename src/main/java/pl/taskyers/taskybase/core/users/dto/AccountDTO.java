package pl.taskyers.taskybase.core.users.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    
    private String username;
    
    private String email;
    
    private String password;
    
    private String name;
    
    private String surname;
    
}
