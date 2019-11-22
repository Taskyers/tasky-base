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
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.entry.converter.CustomizableEntryConverter;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.entry.entity.StatusEntryEntity;
import pl.taskyers.taskybase.entry.slo.CustomizableEntrySLO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EntrySettingsSLOImpl implements EntrySettingsSLO {
    
    private final CustomizableEntrySLO<StatusEntryEntity> statusEntrySLO;
    
    private final Validator<StatusEntryEntity> statusEntryValidator;
    
    private final ProjectSLO projectSLO;
    
    private final RoleSLO roleSLO;
    
    private final AuthProvider authProvider;
    
    @Override
    public ResponseEntity createNewEntry(String projectName, CustomizableEntryDTO customizableEntryDTO) {
        ResponseEntity isProjectFound = checkForProjectAndRole(projectName);
        if ( isProjectFound == null ) {
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            StatusEntryEntity statusEntryEntity = CustomizableEntryConverter.convertEntryStatusFromDTO(customizableEntryDTO);
            statusEntryEntity.setProject(projectEntity);
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            statusEntryValidator.validate(statusEntryEntity, validationMessageContainer);
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            StatusEntryEntity savedEntry = statusEntrySLO.addNewEntry(statusEntryEntity);
            return ResponseEntity.created(UriUtils.createURIFromId(savedEntry.getId()))
                    .body(new ResponseMessage<>(MessageCode.entry_created.getMessage("Status"), MessageType.SUCCESS, savedEntry));
        }
        return isProjectFound;
    }
    
    @Override
    public ResponseEntity updateEntry(Long id, CustomizableEntryDTO customizableEntryDTO) {
        ResponseEntity isEntryFound = checkForEntryAndRole(id);
        if ( isEntryFound == null ) {
            StatusEntryEntity statusEntryEntity = CustomizableEntryConverter.convertEntryStatusFromDTO(customizableEntryDTO);
            statusEntryEntity.setProject(statusEntrySLO.getEntryById(id).get().getProject());
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            statusEntryValidator.validate(statusEntryEntity, validationMessageContainer);
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            StatusEntryEntity updatedEntry = statusEntrySLO.updateEntry(id, statusEntryEntity);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.entry_updated.getMessage("id", id), MessageType.SUCCESS, updatedEntry));
        }
        return isEntryFound;
    }
    
    @Override
    public ResponseEntity deleteEntry(Long id) {
        ResponseEntity isEntryFound = checkForEntryAndRole(id);
        if ( isEntryFound == null ) {
            statusEntrySLO.deleteEntry(id);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.entry_deleted.getMessage("Status"), MessageType.SUCCESS));
        }
        return isEntryFound;
    }
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName) {
        if ( projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            return roleSLO.hasPermission(authProvider.getUserEntity(), projectEntity, Roles.SETTINGS_MANAGE_STATUSES) ?
                    ResponseEntity.ok(convertToDTOList(projectEntity)) : ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
    }
    
    @Override
    public ResponseEntity doesValueExistInProject(String value, String projectName) {
        final Optional<ProjectEntity> projectEntity = projectSLO.getProjectEntityByName(projectName);
        if ( !projectEntity.isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        }
        return ResponseEntity.ok(statusEntrySLO.getEntryByValueAndProject(value, projectEntity.get()).isPresent());
    }
    
    private ResponseEntity checkForProjectAndRole(String projectName) {
        if ( !projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        } else if ( !roleSLO.hasPermission(authProvider.getUserEntity(), projectSLO.getProjectEntityByName(projectName).get(),
                Roles.SETTINGS_MANAGE_STATUSES) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private ResponseEntity checkForEntryAndRole(Long id) {
        if ( !statusEntrySLO.getEntryById(id).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage("status", "id", id), MessageType.WARN));
        } else if ( !roleSLO.hasPermission(authProvider.getUserEntity(), statusEntrySLO.getEntryById(id).get().getProject(),
                Roles.SETTINGS_MANAGE_STATUSES) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private List<CustomizableEntryDTO> convertToDTOList(ProjectEntity projectEntity) {
        List<CustomizableEntryDTO> result = new ArrayList<>();
        for ( StatusEntryEntity statusEntryEntity : statusEntrySLO.getAllByProject(projectEntity) ) {
            result.add(CustomizableEntryConverter.convertEntryStatusToDTO(statusEntryEntity));
        }
        return result;
    }
    
}
