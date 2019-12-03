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
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.converter.SprintConverter;
import pl.taskyers.taskybase.sprint.dto.SprintDTO;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.sprint.slo.SprintSLO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SprintManagementSLOImpl implements SprintManagementSLO {
    
    private final AuthProvider authProvider;
    
    private final RoleSLO roleSLO;
    
    private final SprintSLO sprintSLO;
    
    private final ProjectSLO projectSLO;
    
    private final Validator<SprintEntity> sprintValidator;
    
    @Override
    public ResponseEntity getData(Long sprintId) {
        ResponseEntity isSprintFound = checkForId(sprintId);
        return isSprintFound != null ? isSprintFound : ResponseEntity.ok(SprintConverter.convertToDTO(sprintSLO.getById(sprintId).get()));
    }
    
    @Override
    public ResponseEntity createNew(SprintDTO sprintDTO, String projectName) {
        ResponseEntity isProjectFound = checkForProject(projectName);
        if ( isProjectFound == null ) {
            ProjectEntity projectEntity = projectSLO.getProjectEntityByName(projectName).get();
            SprintEntity sprintEntity = SprintConverter.convertFromDTO(sprintDTO);
            sprintEntity.setProject(projectEntity);
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            sprintValidator.validate(sprintEntity, validationMessageContainer);
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            SprintEntity savedSprint = sprintSLO.addNew(sprintEntity);
            return ResponseEntity.created(UriUtils.createURIFromId(savedSprint.getId()))
                    .body(new ResponseMessage<>(MessageCode.sprint_created.getMessage(), MessageType.SUCCESS, savedSprint));
        }
        return isProjectFound;
    }
    
    @Override
    public ResponseEntity update(Long sprintId, SprintDTO sprintDTO) {
        ResponseEntity isSprintFound = checkForId(sprintId);
        if ( isSprintFound == null ) {
            SprintEntity sprintEntity = sprintSLO.getById(sprintId).get();
            sprintEntity.setName(sprintDTO.getName());
            sprintEntity.setStart(DateUtils.parseDate(sprintDTO.getStart()));
            sprintEntity.setEnd(DateUtils.parseDate(sprintDTO.getEnd()));
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            sprintValidator.validate(sprintEntity, validationMessageContainer);
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            SprintEntity updatedSprint = sprintSLO.update(sprintEntity);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.sprint_updated.getMessage(), MessageType.SUCCESS, updatedSprint));
        }
        return isSprintFound;
    }
    
    @Override
    public ResponseEntity delete(Long sprintId) {
        ResponseEntity isSprintFound = checkForId(sprintId);
        if ( isSprintFound == null ) {
            sprintSLO.delete(sprintId);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.sprint_deleted.getMessage(), MessageType.SUCCESS));
        }
        return isSprintFound;
    }
    
    @Override
    public ResponseEntity checkForNameInProject(String name, String projectName) {
        ResponseEntity isProjectFound = checkForProject(projectName);
        return isProjectFound != null ? isProjectFound :
                ResponseEntity.ok(sprintSLO.doesNameExistsInProject(name, projectSLO.getProjectEntityByName(projectName).get()));
    }
    
    @Override
    public ResponseEntity hasProperRoleOnEntry(String projectName) {
        ResponseEntity isProjectFound = checkForProject(projectName);
        return isProjectFound != null ? isProjectFound :
                ResponseEntity.ok(convertToList(projectSLO.getProjectEntityByName(projectName).get().getSprints()));
    }
    
    private ResponseEntity checkForId(Long id) {
        if ( !sprintSLO.getById(id).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.sprint_not_found.getMessage("id", id), MessageType.WARN));
        } else if ( !roleSLO.hasPermission(authProvider.getUserEntity(), sprintSLO.getById(id).get().getProject(), Roles.SETTINGS_MANAGE_SPRINTS) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private ResponseEntity checkForProject(String projectName) {
        if ( !projectSLO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        } else if ( !roleSLO.hasPermission(authProvider.getUserEntity(), projectSLO.getProjectEntityByName(projectName).get(),
                Roles.SETTINGS_MANAGE_SPRINTS) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private List<SprintDTO> convertToList(Set<SprintEntity> entities) {
        List<SprintDTO> result = new ArrayList<>();
        for ( SprintEntity sprintEntity : entities ) {
            result.add(SprintConverter.convertToDTO(sprintEntity));
        }
        return result;
    }
    
}
