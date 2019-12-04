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
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.converter.CustomizableEntryConverter;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.entry.slo.EntrySLO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EntrySettingsSLOImpl implements EntrySettingsSLO {
    
    private final EntrySLO entityEntrySLO;
    
    private final Validator<EntryEntity> entryEntityValidator;
    
    private final ProjectSLO projectSLO;
    
    private final RoleSLO roleSLO;
    
    private final AuthProvider authProvider;
    
    @Override
    public ResponseEntity createNewEntry(String projectName, CustomizableEntryDTO customizableEntryDTO) {
        ResponseEntity isProjectFound = checkForProjectAndRole(projectName, customizableEntryDTO.getEntryType());
        if ( isProjectFound == null ) {
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            EntryEntity entryEntity = CustomizableEntryConverter.convertEntryStatusFromDTO(customizableEntryDTO);
            entryEntity.setProject(projectEntity);
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            entryEntityValidator.validate(entryEntity, validationMessageContainer, true);
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            EntryEntity savedEntry = entityEntrySLO.addNewEntry(entryEntity);
            return ResponseEntity.created(UriUtils.createURIFromId(savedEntry.getId()))
                    .body(new ResponseMessage<>(MessageCode.entry_created.getMessage(entryEntity.getEntryType()), MessageType.SUCCESS, savedEntry));
        }
        return isProjectFound;
    }
    
    @Override
    public ResponseEntity updateEntry(Long id, CustomizableEntryDTO customizableEntryDTO) {
        ResponseEntity isEntryFound = checkForEntryAndRole(id);
        if ( isEntryFound == null ) {
            EntryEntity entryEntity = CustomizableEntryConverter.convertEntryStatusFromDTO(customizableEntryDTO);
            entryEntity.setProject(entityEntrySLO.getEntryById(id).get().getProject());
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            if ( entityEntrySLO.getEntryById(id).get().getValue().equals(customizableEntryDTO.getValue()) ) {
                entryEntityValidator.validate(entryEntity, validationMessageContainer, false);
            } else {
                entryEntityValidator.validate(entryEntity, validationMessageContainer, true);
            }
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            EntryEntity updatedEntry = entityEntrySLO.updateEntry(id, entryEntity);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.entry_updated.getMessage("id", id), MessageType.SUCCESS, updatedEntry));
        }
        return isEntryFound;
    }
    
    @Override
    public ResponseEntity deleteEntry(Long id) {
        ResponseEntity isEntryFound = checkForEntryAndRole(id);
        if ( isEntryFound == null ) {
            String entryType = entityEntrySLO.getEntryById(id).get().getEntryType().name();
            entityEntrySLO.deleteEntry(id);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.entry_deleted.getMessage(entryType), MessageType.SUCCESS));
        }
        return isEntryFound;
    }
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName, String type) {
        EntryType entryType = CustomizableEntryConverter.checkEntryType(type);
        if ( entryType == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage("of type " + type), MessageType.WARN));
        }
        
        if ( projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            return roleSLO.hasPermission(authProvider.getUserEntity(), projectEntity, getRoleByEntryType(entryType)) ?
                    ResponseEntity.ok(convertToDTOList(projectEntity, entryType)) : ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
    }
    
    @Override
    public ResponseEntity doesValueExistInProject(String value, String projectName, String type) {
        EntryType entryType = CustomizableEntryConverter.checkEntryType(type);
        if ( entryType == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage("of type " + type), MessageType.WARN));
        }
        final Optional<ProjectEntity> projectEntity = projectSLO.getProjectEntityByName(projectName);
        
        if ( !projectEntity.isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        }
        return ResponseEntity.ok(entityEntrySLO.getEntryByEntryTypeAndValueAndProject(entryType, value, projectEntity.get()).isPresent());
    }
    
    private ResponseEntity checkForProjectAndRole(String projectName, String type) {
        EntryType entryType = CustomizableEntryConverter.checkEntryType(type);
        if ( entryType == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage("of type " + type), MessageType.WARN));
        }
        
        if ( !projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        } else if ( !roleSLO.hasPermission(authProvider.getUserEntity(), projectSLO.getProjectEntityByName(projectName).get(),
                getRoleByEntryType(entryType)) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        
        return null;
    }
    
    private ResponseEntity checkForEntryAndRole(Long id) {
        if ( !entityEntrySLO.getEntryById(id).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage("with id " + id), MessageType.WARN));
        } else if ( !roleSLO.hasPermission(authProvider.getUserEntity(), entityEntrySLO.getEntryById(id).get().getProject(),
                getRoleByEntryType(entityEntrySLO.getEntryById(id).get().getEntryType())) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private List<CustomizableEntryDTO> convertToDTOList(ProjectEntity projectEntity, EntryType entryType) {
        List<CustomizableEntryDTO> result = new ArrayList<>();
        for ( EntryEntity entryEntity : entityEntrySLO.getAllByProjectAndEntryType(projectEntity, entryType) ) {
            result.add(CustomizableEntryConverter.convertEntryStatusToDTO(entryEntity));
        }
        return result;
    }
    
    private String getRoleByEntryType(EntryType type) {
        if ( type == EntryType.TYPE ) {
            return Roles.SETTINGS_MANAGE_TYPES;
        } else if ( type == EntryType.STATUS ) {
            return Roles.SETTINGS_MANAGE_STATUSES;
        } else {
            return Roles.SETTINGS_MANAGE_PRIORITIES;
        }
    }
    
}
