package pl.taskyers.taskybase.dashboard.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.roles.constants.Roles;
import pl.taskyers.taskybase.core.roles.slo.RoleSLO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.dashboard.project.dto.ProjectDashboardResponseData;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.slo.ProjectSLO;

@Service
@AllArgsConstructor
public class ProjectDashboardSLOImpl implements ProjectDashboardSLO {
    
    private final ProjectSLO projectSLO;
    
    private final AuthProvider authProvider;
    
    private final RoleSLO roleSLO;
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName) {
        final UserEntity userEntity = authProvider.getUserEntity();
        if ( !projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.ERROR));
        } else if ( projectSLO.getProjectByNameAndUser(projectName, userEntity).isPresent() ||
                    projectSLO.getProjectByNameAndOwner(projectName, userEntity).isPresent() ) {
            final ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            return ResponseEntity.ok(createResponseData(projectEntity, userEntity));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
    }
    
    private ProjectDashboardResponseData createResponseData(ProjectEntity projectEntity, UserEntity userEntity) {
        final boolean canManageUsers = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_USERS);
        final boolean canEditProject = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_EDIT_PROJECT);
        final boolean canInviteToProject = roleSLO.hasPermission(userEntity, projectEntity, Roles.PROJECT_INVITE_OTHERS);
        final boolean canManageStatuses = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_STATUSES);
        final boolean canManageTypes = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_TYPES);
        final boolean canManagePriorities = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_PRIORITIES);
        final boolean canManageSprints = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_SPRINTS);
        
        return new ProjectDashboardResponseData(canManageUsers, canEditProject, canInviteToProject, canManageStatuses, canManageTypes,
                canManagePriorities, canManageSprints);
    }
    
}
