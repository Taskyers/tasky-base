package pl.taskyers.taskybase.task.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.emails.AddressConverter;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.dao.EntryDAO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.dao.SprintDAO;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.ResolutionType;
import pl.taskyers.taskybase.task.converter.TaskConverter;
import pl.taskyers.taskybase.task.dao.TaskDAO;
import pl.taskyers.taskybase.task.dto.UpdateTaskData;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class EditTaskSLOImpl implements EditTaskSLO {
    
    private final AuthProvider authProvider;
    
    private final TaskDAO taskDAO;
    
    private final ProjectDAO projectDAO;
    
    private final EntryDAO entryDAO;
    
    private final SprintDAO sprintDAO;
    
    private final Validator<UpdateTaskData> updateTaskDataValidator;
    
    private final EmailSLO emailSLO;
    
    @Override
    public ResponseEntity assignToMe(Long id) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTask(id, userEntity);
        
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskById(id).get();
            if ( taskEntity.getAssignee().equals(userEntity) ) {
                log.warn("User {} has tried to assign yourself again to task {}", UserUtils.getPersonals(userEntity), taskEntity.getKey());
                return ResponseEntity.badRequest().body(new ResponseMessage<>(MessageCode.task_same_assignee.getMessage(), MessageType.ERROR));
            }
            final TaskEntity updated = taskDAO.setAssignee(taskEntity, userEntity);
            sendEmails(updated);
            return ResponseEntity.ok(
                    new ResponseMessage<>(MessageCode.task_assigned.getMessage(), MessageType.SUCCESS,
                            TaskConverter.convertToDetailsDTO(updated, userEntity)));
        }
        return isTaskFound;
    }
    
    @Override
    public ResponseEntity watchThisTask(Long id) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTask(id, userEntity);
        
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskById(id).get();
            if ( watcherExists(taskEntity.getWatchers(), userEntity) == null ) {
                log.warn("User {} has tried to watch task {} again", UserUtils.getPersonals(userEntity), taskEntity.getKey());
                return ResponseEntity.badRequest().body(new ResponseMessage<>(MessageCode.task_same_watcher.getMessage(), MessageType.ERROR));
            }
            final TaskEntity updated = taskDAO.addWatcher(taskEntity, userEntity);
            return ResponseEntity.ok(
                    new ResponseMessage<>(MessageCode.task_watcher_added.getMessage(), MessageType.SUCCESS,
                            TaskConverter.convertToDetailsDTO(updated, userEntity)));
        }
        return isTaskFound;
    }
    
    @Override
    public ResponseEntity updateEntry(Long id, String value, EntryType entryType) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isEntryFound = checkForEntry(id, userEntity, value, entryType);
        if ( isEntryFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskById(id).get();
            final ProjectEntity projectEntity = taskEntity.getProject();
            final EntryEntity entryEntity = entryDAO.getEntryByEntryTypeAndValueAndProject(entryType, value, projectEntity).get();
            final TaskEntity updated = taskDAO.updateEntry(taskEntity, entryEntity);
            sendEmails(updated);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.task_entry_updated.getMessage(entryType), MessageType.SUCCESS,
                    TaskConverter.convertToDetailsDTO(updated, userEntity)));
        }
        return isEntryFound;
    }
    
    @Override
    public ResponseEntity updateResolution(Long id, String value) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isResolutionFound = checkForResolution(id, userEntity, value);
        if ( isResolutionFound == null ) {
            final ResolutionType resolutionType = ResolutionType.getByValue(value);
            final TaskEntity taskEntity = taskDAO.getTaskById(id).get();
            final TaskEntity updated = taskDAO.updateResolution(taskEntity, resolutionType);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.resolution_updated.getMessage(), MessageType.SUCCESS,
                    TaskConverter.convertToDetailsDTO(updated, userEntity)));
        }
        return isResolutionFound;
    }
    
    @Override
    public ResponseEntity updateData(Long id, UpdateTaskData taskData) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForSprint(id, userEntity, taskData.getSprint());
        if ( isTaskFound == null ) {
            ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
            updateTaskDataValidator.validate(taskData, validationMessageContainer, false);
            if ( validationMessageContainer.hasErrors() ) {
                return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
            }
            final TaskEntity toUpdate = taskDAO.getTaskById(id).get();
            final ProjectEntity projectEntity = toUpdate.getProject();
            final SprintEntity sprintEntity = sprintDAO.getByNameAndProject(taskData.getSprint(), projectEntity).get();
            final TaskEntity updated =
                    taskDAO.updateTask(toUpdate, taskData.getName(), taskData.getDescription(), taskData.getFixVersion(), sprintEntity);
            sendEmails(updated);
            
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.task_updated.getMessage(), MessageType.SUCCESS,
                    TaskConverter.convertToDetailsDTO(updated, userEntity)));
        }
        return isTaskFound;
    }
    
    @Override
    public ResponseEntity stopWatchingTask(Long id) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTask(id, userEntity);
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskById(id).get();
            if ( watcherExists(taskEntity.getWatchers(), userEntity) == null ) {
                log.warn("Attempt to remove user that is not in watchers");
                return ResponseEntity.badRequest().body(new ResponseMessage<>(MessageCode.task_watcher_not_in.getMessage(), MessageType.WARN));
            }
            final TaskEntity updated = taskDAO.removeFromWatchers(taskEntity, userEntity);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.task_watcher_removed.getMessage(), MessageType.SUCCESS,
                    TaskConverter.convertToDetailsDTO(updated, userEntity)));
        }
        return isTaskFound;
    }
    
    private void sendEmails(TaskEntity taskEntity) {
        final int size = taskEntity.getWatchers().size();
        if ( size > 0 ) {
            emailSLO.sendEmailToWatchers(AddressConverter.convertToDTOList(taskEntity.getWatchers()), authProvider.getUserPersonal(), taskEntity);
            log.debug("Sending emails to {} watchers", size);
        } else {
            log.debug("Watchers size is 0 - not sending emails");
        }
    }
    
    private ResponseEntity checkForTask(Long id, UserEntity userEntity) {
        if ( !taskDAO.getTaskById(id).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.task_not_found.getMessage("id", id), MessageType.WARN));
        } else if ( !projectDAO.getProjectByNameAndUser(taskDAO.getTaskById(id).get().getProject().getName(), userEntity)
                .isPresent() ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private ResponseEntity checkForResolution(Long id, UserEntity userEntity, String value) {
        ResponseEntity isTaskFound = checkForTask(id, userEntity);
        if ( isTaskFound == null ) {
            return ResolutionType.getByValue(value) != null ? null : ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.resolution_not_found.getMessage(value), MessageType.WARN));
        }
        return isTaskFound;
    }
    
    private ResponseEntity checkForEntry(Long id, UserEntity userEntity, String value, EntryType entryType) {
        ResponseEntity isTaskFound = checkForTask(id, userEntity);
        if ( isTaskFound == null ) {
            final ProjectEntity projectEntity = taskDAO.getTaskById(id).get().getProject();
            if ( !entryDAO.getEntryByEntryTypeAndValueAndProject(entryType, value, projectEntity).isPresent() ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage<>(MessageCode.entry_not_found.getMessage(value), MessageType.WARN));
            }
            return null;
        }
        return isTaskFound;
    }
    
    private ResponseEntity checkForSprint(Long id, UserEntity userEntity, String sprint) {
        ResponseEntity isTaskFound = checkForTask(id, userEntity);
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskById(id).get();
            if ( !sprintDAO.getByNameAndProject(sprint, taskEntity.getProject()).isPresent() ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage<>(MessageCode.sprint_not_found.getMessage("name", sprint), MessageType.WARN));
            }
            return null;
        }
        return isTaskFound;
    }
    
    private UserEntity watcherExists(Set<UserEntity> watchers, UserEntity userEntity) {
        return watchers.isEmpty() ? null : watchers.stream().filter(watcher -> watcher.getId().equals(userEntity.getId())).findFirst().orElse(null);
    }
    
}
