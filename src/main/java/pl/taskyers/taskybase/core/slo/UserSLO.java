package pl.taskyers.taskybase.core.slo;

import pl.taskyers.taskybase.core.entity.UserEntity;

import java.util.Optional;

public interface UserSLO {
    
    void updatePassword(UserEntity userEntity, String password);
    
    void enableUser(UserEntity userEntity);
    
    Optional<UserEntity> getEntityByEmail(String email);
    
    Optional<UserEntity> getEntityByUsername(String username);
    
}
