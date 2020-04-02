package pl.taskyers.taskybase.project.converter;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.project.dto.UserDTO;

public class UserConverter {
    
    private UserConverter() {
    }
    
    public static UserDTO convertFromDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), UserUtils.getPersonals(userEntity), userEntity.getUsername(), userEntity.getEmail());
    }
    
}
