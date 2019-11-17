package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.emails.EmailConstants;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.roles.slo.RoleSLO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.slo.TokenSLO;
import pl.taskyers.taskybase.core.users.converters.AccountConverter;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.users.slo.UserSLO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.entity.ProjectInvitationTokenEntity;

import static pl.taskyers.taskybase.core.roles.constants.Roles.PROJECT_INVITE_OTHERS;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectInvitationSLOImpl implements ProjectInvitationSLO {
    
    private final UserSLO userSLO;
    
    private final EmailSLO emailSLO;
    
    private final ProjectSLO projectSLO;
    
    private final RoleSLO roleSLO;
    
    private final TokenSLO projectInvitationTokenSLO;
    
    private final AuthProvider authProvider;
    
    @Override
    public ResponseEntity sendEmailWithInvitationToken(String userName, String projectName) {
        if ( userSLO.getEntityByUsername(userName).isPresent() && projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            UserEntity userEntity = userSLO.getEntityByUsername(userName).get();
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            if ( !roleSLO.hasPermission(authProvider.getUserEntity(), projectEntity, PROJECT_INVITE_OTHERS) ) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseMessage<String>(MessageCode.project_permission_not_granted.getMessage(), MessageType.WARN));
            } else if ( projectSLO.getProjectByNameAndUser(projectName, userEntity).isPresent() ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage<>(MessageCode.user_already_in_project.getMessage(userEntity.getUsername(), projectEntity.getName()),
                                MessageType.ERROR));
            }
            projectInvitationTokenSLO.createToken(userEntity, projectEntity);
            boolean emailWasSent = emailSLO.sendEmailWithTemplateToSingleAddressee(AccountConverter.convertToDTO(userEntity),
                    MessageCode.email_subject_project_invitation.getMessage(),
                    EmailConstants.PROJECT_INVITATION_PATH,
                    new String[]{ "name", "surname", "projectName", "token" },
                    new Object[]{ userEntity.getName(), userEntity.getSurname(), projectEntity.getName(),
                            EmailConstants.PROJECT_INVITATION_URL_TOKEN.replace("{tokenPlaceholder}",
                                    projectInvitationTokenSLO.getToken(userEntity)) });
            
            return emailWasSent ?
                    ResponseEntity.ok(
                            new ResponseMessage<String>(MessageCode.email_with_token_sent.getMessage(userEntity.getEmail()), MessageType.SUCCESS)) :
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseMessage<>(MessageCode.server_problem_occured.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.field_not_found.getMessage("Username", userName), MessageType.WARN));
    }
    
    @Override
    public ResponseEntity acceptInvitation(String token) {
        ProjectInvitationTokenEntity projectInvitationTokenEntity = (ProjectInvitationTokenEntity) projectInvitationTokenSLO.getTokenEntity(token);
        if ( projectInvitationTokenEntity == null ) {
            log.warn("Token " + token + " was not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<String>(MessageCode.field_not_found.getMessage("Token", token), MessageType.WARN));
        }
        ProjectEntity projectEntity = projectInvitationTokenEntity.getProject();
        UserEntity userEntity = projectInvitationTokenEntity.getUser();
        ProjectEntity projectWithSavedUser = projectSLO.addUserToProject(projectEntity, userEntity);
        if ( projectWithSavedUser == null ) {
            log.warn(token + "is not yours");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<String>(MessageCode.field_not_found.getMessage("Token", token), MessageType.WARN));
        }
        projectInvitationTokenSLO.deleteToken(token);
        return ResponseEntity.ok(
                new ResponseMessage<String>(MessageCode.project_invitation_acceptance.getMessage(projectEntity.getName()), MessageType.SUCCESS));
    }
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName) {
        if ( projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            return roleSLO.hasPermission(authProvider.getUserEntity(), projectEntity, PROJECT_INVITE_OTHERS) ? ResponseEntity.ok().build() :
                    ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
    }
    
}
