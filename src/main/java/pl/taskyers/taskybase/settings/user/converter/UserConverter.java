package pl.taskyers.taskybase.settings.user.converter;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.settings.user.dto.UserDTO;

public class UserConverter {
    
    private UserConverter() {
    }
    
    public static UserDTO convertToDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getUsername(), userEntity.getName(), userEntity.getSurname(), userEntity.getEmail());
    }
    
}
