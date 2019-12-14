package pl.taskyers.taskybase.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.taskyers.taskybase.core.slo.AuthProvider;

@RestController
@RequestMapping(value = "/isLoggedIn")
@AllArgsConstructor
public class SecurityRestController {
    
    private final AuthProvider authProvider;
    
    @RequestMapping(method = RequestMethod.GET)
    public boolean isLoggedIn() {
        return authProvider.isLoggedIn();
    }
    
}
