package pl.taskyers.taskybase.registration.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.registration.slo.AccountActivationSLO;

import static pl.taskyers.taskybase.registration.slo.AccountActivationSLO.*;

@RestController
@RequestMapping(ACTIVATION_ACCOUNT_PREFIX)
@AllArgsConstructor
public class AccountActivationRestController {

    private final AccountActivationSLO accountActivationSLO;
    
    @RequestMapping(value = ACTIVATE_ACCOUNT_BY_TOKEN, method = RequestMethod.GET)
    public ResponseEntity activateAccount(@PathVariable String token) {
        return accountActivationSLO.activateAccount(token);
    }
    
}
