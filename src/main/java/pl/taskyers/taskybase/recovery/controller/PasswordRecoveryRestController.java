package pl.taskyers.taskybase.recovery.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.recovery.slo.PasswordRecoverySLO;

import static pl.taskyers.taskybase.recovery.slo.PasswordRecoverySLO.*;

@RestController
@RequestMapping(value = PASSWORD_RECOVERY_PREFIX)
@AllArgsConstructor
public class PasswordRecoveryRestController {
    
    private final PasswordRecoverySLO passwordRecoverySLO;
    
    @RequestMapping(value = SEND_EMAIL_WITH_TOKEN, method = RequestMethod.POST)
    public ResponseEntity sendEmailWithToken(@RequestParam String email) {
        return passwordRecoverySLO.sendEmailWithToken(email);
    }
    
    @RequestMapping(value = SET_NEW_PASSWORD, method = RequestMethod.POST)
    public ResponseEntity setNewPassword(@PathVariable String token, @RequestParam String password) {
        return passwordRecoverySLO.setNewPassword(token, password);
    }
    
}
