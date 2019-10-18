package pl.taskyers.taskybase.registration.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.core.dto.AccountDTO;

import java.io.UnsupportedEncodingException;

public interface RegistrationSLO {
    
    String REGISTRATION_PREFIX = "/register";
    
    String FIND_USER_BY_USERNAME = "/checkByUsername/{username}";
    
    String FIND_USER_BY_EMAIL = "/checkByEmail/{email}";
    
    ResponseEntity register(AccountDTO accountDTO) throws UnsupportedEncodingException;
    
    boolean userExistsByUsername(String username);
    
    boolean userExistsByEmail(String email);
    
}
