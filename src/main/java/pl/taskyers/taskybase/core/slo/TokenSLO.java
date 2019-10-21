package pl.taskyers.taskybase.core.slo;

import pl.taskyers.taskybase.core.users.entity.UserEntity;

public interface TokenSLO<T> {
    
    String generateToken();
    
    String getToken(UserEntity userEntity);
    
    T getTokenEntity(String token);
    
    void createToken(UserEntity userEntity);
    
    void deleteToken(String token);
    
}
