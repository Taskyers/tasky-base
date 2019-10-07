package pl.taskyers.taskybase.registration.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.core.entity.UserEntity;

public interface RegistrationSLO {
    
    String REGISTRATION_PREFIX = "/register";
    
    ResponseEntity register(UserEntity userEntity);
    
}
