package pl.taskyers.taskybase.dashboard.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.roles.constants.Roles;
import pl.taskyers.taskybase.core.roles.dao.RoleDAO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.dashboard.project.dto.ProjectDashboardResponseData;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.dao.ProjectDAO;

@Service
@AllArgsConstructor
public class ProjectDashboardSLOImpl implements ProjectDashboardSLO {
    
    private final ProjectDAO projectDAO;
    
    private final AuthProvider authProvider;
    
    private final RoleDAO roleDAO;
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName) {
        final UserEntity userEntity = authProvider.getUserEntity();
        if ( !projectDAO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.ERROR));
        } else if ( projectDAO.getProjectByNameAndUser(projectName, userEntity).isPresent() ||
                    projectDAO.getProjectByNameAndOwner(projectName, userEntity).isPresent() ) {
            final ProjectEntity projectEntity = projectDAO.getProjectEntityByName(projectName).get();
            return ResponseEntity.ok(createResponseData(projectEntity, userEntity));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
    }
    
    private ProjectDashboardResponseData createResponseData(ProjectEntity projectEntity, UserEntity userEntity) {
        final boolean canManageUsers = roleDAO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_USERS);
        final boolean canEditProject = roleDAO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_EDIT_PROJECT);
        final boolean canInviteToProject = roleDAO.hasPermission(userEntity, projectEntity, Roles.PROJECT_INVITE_OTHERS);
        final boolean canManageStatuses = roleDAO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_STATUSES);
        final boolean canManageTypes = roleDAO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_TYPES);
        final boolean canManagePriorities = roleDAO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_PRIORITIES);
        final boolean canManageSprints = roleDAO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_MANAGE_SPRINTS);
        
        return new ProjectDashboardResponseData(canManageUsers, canEditProject, canInviteToProject, canManageStatuses, canManageTypes,
                canManagePriorities, canManageSprints);
    }
    
}
