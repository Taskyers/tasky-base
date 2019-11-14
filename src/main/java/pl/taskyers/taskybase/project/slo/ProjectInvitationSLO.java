package pl.taskyers.taskybase.project.slo;

import org.springframework.http.ResponseEntity;
import pl.taskyers.taskybase.core.roles.slo.EntryEndpoint;

/**
 * Interface for project invitation
 *
 * @author Marcin Ruszkiewicz
 */
public interface ProjectInvitationSLO extends EntryEndpoint {
    
    String PROJECT_INVITATION_PREFIX = "/secure/projectInvitation";
    
    String SEND_EMAIL_WITH_INVITATION_TOKEN = "/invitationToken";
    
    String ACCEPT_INVITATION = "/{token}";
    
    String USER_CAN_INVITE = "/{projectName}";
    
    /**
     * Method for creating project invitation token and then sending email to passed user with generated token
     *
     * @param username    user to which token will be send
     * @param projectName project to which token will be assign
     * @return status 200 if email was sent, status 500 if email could not be sent and status 404 if provided email was not found in database
     * @since 0.0.3
     */
    ResponseEntity sendEmailWithInvitationToken(String username, String projectName);
    
    /**
     * Method for accepting invitation and removing this token from database
     *
     * @param token invitation token which was passed by path variable
     * @return status 200 if user was added, 404 if provided token was not found in database
     * @since 0.0.3
     */
    ResponseEntity acceptInvitation(String token);
    
}
