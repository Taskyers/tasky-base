package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.project.dto.RolesWrapper;
import pl.taskyers.taskybase.project.slo.UserManagementSLO;

import static pl.taskyers.taskybase.project.slo.UserManagementSLO.*;

@RestController
@RequestMapping(value = USER_MANAGEMENT_PREFIX)
@AllArgsConstructor
public class UserManagementRestController {
    
    private UserManagementSLO userManagementSLO;
    
    @RequestMapping(value = GET_BY_NAME, method = RequestMethod.GET)
    public ResponseEntity entryPoint(@PathVariable String projectName) {
        return userManagementSLO.hasProperRoleOnEntry(projectName);
    }
    
    @RequestMapping(value = GET_BY_ID_AND_NAME, method = RequestMethod.GET)
    public ResponseEntity getUserRoles(@PathVariable Long userId, @PathVariable String projectName) {
        return userManagementSLO.getUserRoles(userId, projectName);
    }
    
    @RequestMapping(value = GET_BY_ID_AND_NAME, method = RequestMethod.DELETE)
    public ResponseEntity deleteUserInProject(@PathVariable Long userId, @PathVariable String projectName) {
        return userManagementSLO.deleteUser(userId, projectName);
    }
    
    @RequestMapping(value = GET_BY_ID_AND_NAME, method = RequestMethod.PUT)
    public ResponseEntity updateUserRoles(@RequestBody RolesWrapper roles, @PathVariable Long userId, @PathVariable String projectName) {
        return userManagementSLO.updateUserRoles(userId, projectName, roles);
    }
    
}
