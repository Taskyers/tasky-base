package pl.taskyers.taskybase.registration.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.core.entity.UserEntity;

public interface RegistrationSLO {
    
    String REGISTRATION_PREFIX = "/register";
    
    String FIND_USER_BY_USERNAME = "/checkByUsername/{username}";
    
    String FIND_USER_BY_EMAIL = "/checkByEmail/{email}";
    
    ResponseEntity register(UserEntity userEntity);
    
    boolean userExistsByUsername(String username);
    
    boolean userExistsByEmail(String email);
    
}
