package pl.taskyers.taskybase.project.converter;

import pl.taskyers.taskybase.core.roles.dto.RoleDTO;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;

public class RoleConverter {
    
    private RoleConverter() {
    }
    
    public static RoleDTO convertFromRoleLinker(RoleLinkerEntity roleLinkerEntity) {
        return new RoleDTO(roleLinkerEntity.getRole()
                .getKey(), roleLinkerEntity.getRole()
                .getDescription(), roleLinkerEntity.isChecked());
    }
    
}
