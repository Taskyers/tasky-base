package pl.taskyers.taskybase.task.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.task.converter.TaskConverter;
import pl.taskyers.taskybase.task.dao.TaskDAO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class EditTaskSLOImpl implements EditTaskSLO {
    
    private final AuthProvider authProvider;
    
    private final TaskDAO taskDAO;
    
    private final ProjectDAO projectDAO;
    
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
            return ResponseEntity.ok(
                    new ResponseMessage<>(MessageCode.task_assigned.getMessage(), MessageType.SUCCESS, TaskConverter.convertToDetailsDTO(updated,
                            UserUtils.getPersonals(userEntity))));
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
                    new ResponseMessage<>(MessageCode.task_watcher_added.getMessage(), MessageType.SUCCESS, TaskConverter.convertToDetailsDTO(updated,
                            UserUtils.getPersonals(userEntity))));
        }
        return isTaskFound;
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
    
    private UserEntity watcherExists(Set<UserEntity> watchers, UserEntity userEntity) {
        return watchers.stream().filter(watcher -> watcher.getId().equals(userEntity.getId())).findFirst().orElse(null);
    }
    
}
