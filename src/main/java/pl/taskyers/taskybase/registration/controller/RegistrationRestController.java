package pl.taskyers.taskybase.registration.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.registration.slo.RegistrationSLO;

import static pl.taskyers.taskybase.registration.slo.RegistrationSLO.REGISTRATION_PREFIX;

@RestController
@RequestMapping(value = REGISTRATION_PREFIX)
@AllArgsConstructor
public class RegistrationRestController implements RegistrationSLO {
    
    private final RegistrationSLO registrationSLO;
    
    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody UserEntity userEntity) {
        return registrationSLO.register(userEntity);
    }
    
}
