package pl.taskyers.taskybase.project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.project.dto.UserDTO;
import pl.taskyers.taskybase.project.slo.ProjectInvitationSLO;

import java.util.List;

import static pl.taskyers.taskybase.project.slo.ProjectInvitationSLO.*;

@RestController
@RequestMapping(value = PROJECT_INVITATION_PREFIX)
@AllArgsConstructor
public class ProjectInvitationRestController {
    
    private final ProjectInvitationSLO projectInvitationSLO;
    
    @RequestMapping(value = SEND_EMAIL_WITH_INVITATION_TOKEN, method = RequestMethod.POST)
    public ResponseEntity sendEmailWithInvitationToken(@RequestParam String username, @RequestParam String projectName) {
        return projectInvitationSLO.sendEmailWithInvitationToken(username, projectName);
    }
    
    @RequestMapping(value = ACCEPT_INVITATION, method = RequestMethod.PATCH)
    public ResponseEntity acceptInvitation(@PathVariable String token) {
        return projectInvitationSLO.acceptInvitation(token);
    }
    
    @RequestMapping(value = USER_CAN_INVITE, method = RequestMethod.GET)
    public ResponseEntity userCanInvite(@PathVariable String projectName) {
        return projectInvitationSLO.hasProperRoleOnEntry(projectName);
    }
    
    @RequestMapping(value = FIND_USER_BY_USERNAME_LIKE, method = RequestMethod.GET)
    public List<String> findUsersByUsernameLike(@PathVariable String username) {
        return projectInvitationSLO.findUsersByUsernameLike(username);
    }
    
}
