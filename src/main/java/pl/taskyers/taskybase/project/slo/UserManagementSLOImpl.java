package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.roles.constants.Roles;
import pl.taskyers.taskybase.core.roles.dto.RoleDTO;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.core.roles.slo.RoleSLO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.users.slo.UserSLO;
import pl.taskyers.taskybase.project.converter.RoleConverter;
import pl.taskyers.taskybase.project.converter.UserConverter;
import pl.taskyers.taskybase.project.dto.RolesWrapper;
import pl.taskyers.taskybase.project.dto.UserDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserManagementSLOImpl implements UserManagementSLO {
    
    private final ProjectSLO projectSLO;
    
    private final RoleSLO roleSLO;
    
    private final UserSLO userSLO;
    
    private final AuthProvider authProvider;
    
    @Override
    public ResponseEntity updateUserRoles(Long userId, String projectName, RolesWrapper roles) {
        ResponseEntity areUserAndProjectFound = checkForUserAndProject(userId, projectName);
        if ( areUserAndProjectFound == null ) {
            UserEntity userEntity = userSLO.getEntityById(userId).get();
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            for ( RoleDTO roleDTO : roles.getRoles() ) {
                if ( roleSLO.getEntityByKey(roleDTO.getKey()).isPresent() ) {
                    roleSLO.setRole(userEntity, projectEntity, roleSLO.getEntityByKey(roleDTO.getKey()).get(), roleDTO.isChecked());
                }
            }
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.roles_updated.getMessage(), MessageType.SUCCESS, roles));
        }
        return areUserAndProjectFound;
    }
    
    @Override
    public ResponseEntity deleteUser(Long userId, String projectName) {
        ResponseEntity areUserAndProjectFound = checkForUserAndProject(userId, projectName);
        if ( areUserAndProjectFound == null ) {
            projectSLO.deleteUserInProject(userId, projectName);
            return ResponseEntity.ok(
                    new ResponseMessage<>(MessageCode.user_removed_from_project.getMessage("id", userId, projectName), MessageType.SUCCESS));
        }
        return areUserAndProjectFound;
    }
    
    @Override
    public ResponseEntity getUserRoles(Long userId, String projectName) {
        ResponseEntity areUserAndProjectFound = checkForUserAndProject(userId, projectName);
        return areUserAndProjectFound != null ? areUserAndProjectFound : ResponseEntity.ok(convertRolesToList(
                roleSLO.getRoleLinkersByUserAndProject(userSLO.getEntityById(userId).get(), projectSLO.getProjectEntityByName(projectName).get())));
    }
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName) {
        if ( projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            return roleSLO.hasPermission(authProvider.getUserEntity(), projectEntity, Roles.SETTINGS_MANAGE_USERS) ?
                    ResponseEntity.ok(convertUsersToList(projectEntity)) : ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
    }
    
    private ResponseEntity checkForUserAndProject(Long userId, String projectName) {
        if ( !projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        } else if ( !userSLO.getEntityById(userId).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.user_not_found.getMessage("id", userId), MessageType.WARN));
        } else if ( !roleSLO.hasPermission(authProvider.getUserEntity(), projectSLO.getProjectEntityByName(projectName).get(),
                Roles.SETTINGS_MANAGE_USERS) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        } else if ( !projectSLO.getProjectByNameAndUser(projectName, userSLO.getEntityById(userId).get()).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.user_in_project_not_found.getMessage("id", userId, projectName), MessageType.WARN));
        }
        return null;
    }
    
    private List<UserDTO> convertUsersToList(ProjectEntity projectEntity) {
        List<UserDTO> result = new ArrayList<>();
        for ( UserEntity userEntity : projectEntity.getUsers() ) {
            result.add(UserConverter.convertFromDTO(userEntity));
        }
        return result;
    }
    
    private List<RoleDTO> convertRolesToList(List<RoleLinkerEntity> linkers) {
        List<RoleDTO> result = new ArrayList<>();
        for ( RoleLinkerEntity roleLinkerEntity : linkers ) {
            result.add(RoleConverter.convertFromRoleLinker(roleLinkerEntity));
        }
        return result;
    }
    
}
