package pl.taskyers.taskybase.project.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.roles.constants.Roles;
import pl.taskyers.taskybase.core.roles.slo.RoleSLO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.project.converter.ProjectConverter;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.dto.SettingsEntryResponseData;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectSettingsSLOImpl implements ProjectSettingsSLO {
    
    private final AuthProvider authProvider;
    
    private final RoleSLO roleSLO;
    
    private final ProjectSLO projectSLO;
    
    private final Validator<ProjectEntity> projectValidator;
    
    @Override
    public ResponseEntity updateBasicData(Long id, ProjectDTO projectDTO) {
        Optional<ProjectEntity> projectEntityOptional = projectSLO.getProjectEntityById(id);
        if ( projectEntityOptional.isPresent() ) {
            ProjectEntity projectEntity = projectEntityOptional.get();
            if ( roleSLO.hasPermission(authProvider.getUserEntity(), projectEntity, Roles.SETTINGS_EDIT_PROJECT) ) {
                ProjectEntity projectEntityFromDTO = ProjectConverter.convertFromDTO(projectDTO);
                ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
                if ( !projectEntity.getName().equals(projectDTO.getName()) ) {
                    projectValidator.validate(projectEntityFromDTO, validationMessageContainer, true);
                } else {
                    projectValidator.validate(projectEntityFromDTO, validationMessageContainer, false);
                }
                if ( validationMessageContainer.hasErrors() ) {
                    return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
                }
                ProjectEntity updatedProject = projectSLO.updateProject(projectEntity, projectDTO.getName(), projectDTO.getDescription());
                return ResponseEntity.ok(new ResponseMessage<>(MessageCode.project_updated.getMessage(), MessageType.SUCCESS, updatedProject));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("id", id), MessageType.ERROR));
    }
    
    @Override
    public ResponseEntity deleteProject(Long id) {
        Optional<ProjectEntity> projectEntityOptional = projectSLO.getProjectEntityById(id);
        if ( projectEntityOptional.isPresent() ) {
            if ( roleSLO.hasPermission(authProvider.getUserEntity(), projectEntityOptional.get(), Roles.SETTINGS_DELETE_PROJECT) ) {
                projectSLO.deleteProjectById(id);
                return ResponseEntity.ok(new ResponseMessage<>(MessageCode.project_deleted.getMessage(), MessageType.SUCCESS));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("id", id), MessageType.ERROR));
    }
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName) {
        if ( projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            final ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            final UserEntity userEntity = authProvider.getUserEntity();
            final boolean canEdit = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_EDIT_PROJECT);
            final boolean canDelete = roleSLO.hasPermission(userEntity, projectEntity, Roles.SETTINGS_DELETE_PROJECT);
            
            return (canEdit || canDelete) ? ResponseEntity.ok(createEntryData(projectEntity, canEdit, canDelete)) :
                    ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
    }
    
    private SettingsEntryResponseData createEntryData(ProjectEntity projectEntity, boolean canEdit, boolean canDelete) {
        return new SettingsEntryResponseData(projectEntity.getId(), projectEntity.getName(), projectEntity.getDescription(), canEdit, canDelete);
    }
    
}
