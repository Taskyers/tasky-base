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
import pl.taskyers.taskybase.core.roles.dao.RoleDAO;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.converter.CustomizableEntryConverter;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.entry.dao.EntryDAO;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EntrySettingsSLOImpl implements EntrySettingsSLO {
    
    private final EntryDAO entityEntryDAO;
    
    private final Validator<EntryEntity> entryEntityValidator;
    
    private final ProjectDAO projectDAO;
    
    private final RoleDAO roleDAO;
    
    private final AuthProvider authProvider;
    
    @Override
    public ResponseEntity createNewEntry(String projectName, CustomizableEntryDTO customizableEntryDTO) {
        ResponseEntity isProjectFound = checkForProjectAndRole(projectName, customizableEntryDTO.getEntryType());
        if ( isProjectFound == null ) {
            ProjectEntity projectEntity = projectDAO.getProjectEntityByName(projectName).get();
            EntryEntity entryEntity = CustomizableEntryConverter.convertEntryStatusFromDTO(customizableEntryDTO);
            entryEntity.setProject(projectEntity);
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            entryEntityValidator.validate(entryEntity, validationMessageContainer, true);
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            EntryEntity savedEntry = entityEntryDAO.addNewEntry(entryEntity);
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
            entryEntity.setProject(entityEntryDAO.getEntryById(id).get().getProject());
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            if ( entityEntryDAO.getEntryById(id).get().getValue().equals(customizableEntryDTO.getValue()) ) {
                entryEntityValidator.validate(entryEntity, validationMessageContainer, false);
            } else {
                entryEntityValidator.validate(entryEntity, validationMessageContainer, true);
            }
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            EntryEntity updatedEntry = entityEntryDAO.updateEntry(id, entryEntity);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.entry_updated.getMessage("id", id), MessageType.SUCCESS, updatedEntry));
        }
        return isEntryFound;
    }
    
    @Override
    public ResponseEntity deleteEntry(Long id) {
        ResponseEntity isEntryFound = checkForEntryAndRole(id);
        if ( isEntryFound == null ) {
            String entryType = entityEntryDAO.getEntryById(id).get().getEntryType().name();
            entityEntryDAO.deleteEntry(id);
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
        
        if ( projectDAO.getProjectEntityByName(projectName).isPresent() ) {
            ProjectEntity projectEntity = projectDAO.getProjectEntityByName(projectName).get();
            return roleDAO.hasPermission(authProvider.getUserEntity(), projectEntity, getRoleByEntryType(entryType)) ?
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
        final Optional<ProjectEntity> projectEntity = projectDAO.getProjectEntityByName(projectName);
        
        if ( !projectEntity.isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        }
        return ResponseEntity.ok(entityEntryDAO.getEntryByEntryTypeAndValueAndProject(entryType, value, projectEntity.get()).isPresent());
    }
    
    private ResponseEntity checkForProjectAndRole(String projectName, String type) {
        EntryType entryType = CustomizableEntryConverter.checkEntryType(type);
        if ( entryType == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage("of type " + type), MessageType.WARN));
        }
        
        if ( !projectDAO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        } else if ( !roleDAO.hasPermission(authProvider.getUserEntity(), projectDAO.getProjectEntityByName(projectName).get(),
                getRoleByEntryType(entryType)) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        
        return null;
    }
    
    private ResponseEntity checkForEntryAndRole(Long id) {
        if ( !entityEntryDAO.getEntryById(id).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage("with id " + id), MessageType.WARN));
        } else if ( !roleDAO.hasPermission(authProvider.getUserEntity(), entityEntryDAO.getEntryById(id).get().getProject(),
                getRoleByEntryType(entityEntryDAO.getEntryById(id).get().getEntryType())) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private List<CustomizableEntryDTO> convertToDTOList(ProjectEntity projectEntity, EntryType entryType) {
        List<CustomizableEntryDTO> result = new ArrayList<>();
        for ( EntryEntity entryEntity : entityEntryDAO.getAllByProjectAndEntryType(projectEntity, entryType) ) {
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
