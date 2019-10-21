package pl.taskyers.taskybase.core.users.slo;

import pl.taskyers.taskybase.core.users.entity.UserEntity;

import java.util.Optional;

public interface UserSLO {
    
    void updatePassword(UserEntity userEntity, String password);
    
    void enableUser(UserEntity userEntity);
    
    Optional<UserEntity> getEntityByEmail(String email);
    
    Optional<UserEntity> getEntityByUsername(String username);
    
}
