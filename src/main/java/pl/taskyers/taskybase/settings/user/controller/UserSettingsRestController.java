package pl.taskyers.taskybase.settings.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.settings.user.dto.PasswordDTO;
import pl.taskyers.taskybase.settings.user.dto.UserDTO;
import pl.taskyers.taskybase.settings.user.slo.UserSettingsSLO;

import static pl.taskyers.taskybase.settings.user.slo.UserSettingsSLO.UPDATE_EMAIL;
import static pl.taskyers.taskybase.settings.user.slo.UserSettingsSLO.UPDATE_PASSWORD;
import static pl.taskyers.taskybase.settings.user.slo.UserSettingsSLO.ACCEPT_NEW_EMAIL;
import static pl.taskyers.taskybase.settings.user.slo.UserSettingsSLO.USER_SETTINGS_PREFIX;

@RestController
@RequestMapping(value = USER_SETTINGS_PREFIX)
@AllArgsConstructor
public class UserSettingsRestController {
    
    private final UserSettingsSLO userSettingsSLO;
    
    @RequestMapping(method = RequestMethod.GET)
    public UserDTO getUserDetails() {
        return userSettingsSLO.getUserDetails();
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateUser(@RequestBody UserDTO userDTO) {
        return userSettingsSLO.updateUser(userDTO);
    }
    
    @RequestMapping(value = UPDATE_PASSWORD, method = RequestMethod.PATCH)
    public ResponseEntity updatePassword(@RequestBody PasswordDTO passwordDTO) {
        return userSettingsSLO.updatePassword(passwordDTO);
    }
    
    @RequestMapping(value = UPDATE_EMAIL, method = RequestMethod.POST)
    public ResponseEntity sendEmailWithToken(@RequestParam String email) {
        return userSettingsSLO.sendTokenToNewEmail(email);
    }
    
    @RequestMapping(value = ACCEPT_NEW_EMAIL, method = RequestMethod.PATCH)
    public ResponseEntity setNewPassword(@PathVariable String token) {
        return userSettingsSLO.acceptNewEmail(token);
    }
    
}
