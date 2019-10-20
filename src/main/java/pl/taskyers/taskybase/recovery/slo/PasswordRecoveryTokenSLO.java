package pl.taskyers.taskybase.recovery.slo;

import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.recovery.entity.PasswordRecoveryTokenEntity;

public interface PasswordRecoveryTokenSLO {
    
    String getPasswordRecoveryToken(UserEntity userEntity);
    
    void createPasswordRecoveryToken(UserEntity userEntity);
    
    PasswordRecoveryTokenEntity getTokenEntity(String token);
    
    void deleteToken(String token);
    
}
