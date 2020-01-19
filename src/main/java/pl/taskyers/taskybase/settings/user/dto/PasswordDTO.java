package pl.taskyers.taskybase.settings.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {
    
    private String currentPassword;
    
    private String newPassword;
    
}
