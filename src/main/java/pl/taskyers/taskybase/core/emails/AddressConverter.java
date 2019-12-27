package pl.taskyers.taskybase.core.emails;

import pl.taskyers.taskybase.core.users.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddressConverter {
    
    public static List<AddressDTO> convertToDTOList(Set<UserEntity> users) {
        List<AddressDTO> result = new ArrayList<>();
        for ( UserEntity userEntity : users ) {
            result.add(new AddressDTO(userEntity.getEmail(), userEntity.getName(), userEntity.getSurname()));
        }
        return result;
    }
    
}
