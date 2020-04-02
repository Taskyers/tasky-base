package pl.taskyers.taskybase.core.roles.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    
    private String key;
    
    private String description;
    
    private boolean checked;
    
}
