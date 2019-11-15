package pl.taskyers.taskybase.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.taskyers.taskybase.core.roles.dto.RoleDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesWrapper {
    
    private List<RoleDTO> roles;
    
}
