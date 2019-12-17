package pl.taskyers.taskybase.task.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.converter.SprintConverter;
import pl.taskyers.taskybase.task.converter.TaskConverter;
import pl.taskyers.taskybase.task.dao.TaskDAO;
import pl.taskyers.taskybase.task.dto.EntryTaskData;
import pl.taskyers.taskybase.task.dto.SprintDTO;
import pl.taskyers.taskybase.task.dto.TaskDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AddingTaskSLOImpl implements AddingTaskSLO {
    
    private final AuthProvider authProvider;
    
    private final ProjectDAO projectDAO;
    
    private final TaskDAO taskDAO;
    
    private final Validator<TaskDTO> taskDTOValidator;
    
    private final Validator<TaskEntity> taskEntityValidator;
    
    @Override
    public List<String> getProjects() {
        return convertProjects(authProvider.getUserEntity().getProjects());
    }
    
    @Override
    public ResponseEntity getEntryData(String projectName) {
        ResponseEntity isProjectFound = checkForProject(authProvider.getUserEntity(), projectName);
        if ( isProjectFound == null ) {
            ProjectEntity projectEntity = projectDAO.getProjectEntityByName(projectName).get();
            final Set<EntryEntity> entryEntities = projectEntity.getEntryEntities();
            final Set<SprintEntity> sprintEntities = projectEntity.getSprints();
            return ResponseEntity.ok(
                    new EntryTaskData(convertEntries(entryEntities, EntryType.TYPE), convertEntries(entryEntities, EntryType.PRIORITY),
                            convertEntries(entryEntities, EntryType.STATUS), convertSprints(sprintEntities)));
        }
        return isProjectFound;
    }
    
    @Override
    public ResponseEntity createTask(String projectName, TaskDTO taskDTO) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isProjectFound = checkForProject(userEntity, projectName);
        if ( isProjectFound == null ) {
            ValidationMessageContainer validationMessageContainerDTO = new ValidationMessageContainer();
            taskDTOValidator.validate(taskDTO, validationMessageContainerDTO, true);
            if ( validationMessageContainerDTO.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainerDTO.getErrors());
            }
            
            ProjectEntity projectEntity = projectDAO.getProjectEntityByName(projectName).get();
            TaskEntity taskEntity = TaskConverter.convertFromDTO(taskDTO, projectEntity);
            ValidationMessageContainer validationMessageContainerEntity = new ValidationMessageContainer();
            taskEntityValidator.validate(taskEntity, validationMessageContainerEntity, true);
            if ( validationMessageContainerEntity.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainerEntity.getErrors());
            }
            
            TaskEntity savedTask =
                    taskDAO.createTask(taskEntity, userEntity, taskDTO.getType(), taskDTO.getStatus(), taskDTO.getPriority(), taskDTO.getSprint());
            return ResponseEntity.created(UriUtils.createURIFromId(savedTask.getId()))
                    .body(new ResponseMessage<>(MessageCode.task_created.getMessage(), MessageType.SUCCESS, savedTask));
        }
        return isProjectFound;
    }
    
    private ResponseEntity checkForProject(UserEntity userEntity, String projectName) {
        if ( !projectDAO.getProjectEntityByName(projectName).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.project_not_found.getMessage("name", projectName), MessageType.WARN));
        } else if ( !projectDAO.getProjectByNameAndUser(projectName, userEntity).isPresent() ||
                    !projectDAO.getProjectByNameAndOwner(projectName, userEntity).isPresent() ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private List<String> convertProjects(Set<ProjectEntity> projects) {
        List<String> projectNames = new ArrayList<>();
        for ( ProjectEntity projectEntity : projects ) {
            projectNames.add(projectEntity.getName());
        }
        return projectNames;
    }
    
    private List<String> convertEntries(Set<EntryEntity> entries, EntryType entryType) {
        List<String> entryNames = new ArrayList<>();
        for ( EntryEntity entryEntity : entries ) {
            if ( entryType.equals(entryEntity.getEntryType()) ) {
                entryNames.add(entryEntity.getValue());
            }
        }
        return entryNames;
    }
    
    private List<SprintDTO> convertSprints(Set<SprintEntity> sprints) {
        List<SprintDTO> sprintDTOS = new ArrayList<>();
        for ( SprintEntity sprintEntity : sprints ) {
            sprintDTOS.add(SprintConverter.convertToDTO(sprintEntity));
        }
        return sprintDTOS;
    }
    
}
