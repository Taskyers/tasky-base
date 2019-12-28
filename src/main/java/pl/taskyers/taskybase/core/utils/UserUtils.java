package pl.taskyers.taskybase.core.utils;

import pl.taskyers.taskybase.core.users.entity.UserEntity;

public class UserUtils {
    
    public static String getPersonals(UserEntity userEntity) {
        return userEntity == null ? "" : userEntity.getName() + " " + userEntity.getSurname();
    }
    
}
