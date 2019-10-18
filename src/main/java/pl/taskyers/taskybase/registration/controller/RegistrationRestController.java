package pl.taskyers.taskybase.registration.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.core.dto.AccountDTO;
import pl.taskyers.taskybase.registration.slo.RegistrationSLO;

import static pl.taskyers.taskybase.registration.slo.RegistrationSLO.*;

@RestController
@RequestMapping(value = REGISTRATION_PREFIX)
@AllArgsConstructor
public class RegistrationRestController {
    
    private final RegistrationSLO registrationSLO;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody AccountDTO accountDTO) {
        return registrationSLO.register(accountDTO);
    }
    
    @RequestMapping(value = FIND_USER_BY_USERNAME, method = RequestMethod.GET)
    public boolean userExistsByUsername(@PathVariable String username) {
        return registrationSLO.userExistsByUsername(username);
    }
    
    @RequestMapping(value = FIND_USER_BY_EMAIL, method = RequestMethod.GET)
    public boolean userExistsByEmail(@PathVariable String email) {
        return registrationSLO.userExistsByEmail(email);
    }
    
}
