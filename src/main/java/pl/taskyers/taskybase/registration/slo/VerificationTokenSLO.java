package pl.taskyers.taskybase.registration.slo;

import pl.taskyers.taskybase.core.entity.UserEntity;

public interface VerificationTokenSLO {
    
    String getVerificationToken(UserEntity userEntity);
    
    void createVerificationToken(UserEntity userEntity);
    
}
