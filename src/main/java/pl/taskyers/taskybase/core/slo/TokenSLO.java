package pl.taskyers.taskybase.core.slo;

import pl.taskyers.taskybase.core.entity.UserEntity;

public interface TokenSLO<T> {
    
    String generateToken();
    
    String getToken(UserEntity userEntity);
    
    T getTokenEntity(String token);
    
    void createToken(UserEntity userEntity);
    
    void deleteToken(String token);
    
}
