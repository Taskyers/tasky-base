package pl.taskyers.taskybase.settings.user.converter;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.settings.user.dto.UserDTO;

public class UserConverter {
    
    public static UserEntity convertFromDTO(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setSurname(userDTO.getSurname());
        return userEntity;
    }
    
    public static UserDTO convertToDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getUsername(), userEntity.getName(), userEntity.getSurname(), userEntity.getEmail());
    }
    
}
