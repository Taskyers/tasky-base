package pl.taskyers.taskybase.project.converter;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.dto.UserDTO;

public class UserConverter {
    
    public static UserDTO convertFromDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getName() + " " + userEntity.getSurname(), userEntity.getUsername(), userEntity.getEmail());
    }
    
}
