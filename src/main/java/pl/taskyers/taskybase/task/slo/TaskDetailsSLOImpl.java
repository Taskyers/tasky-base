package pl.taskyers.taskybase.task.slo;

import lombok.AllArgsConstructor;
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
import pl.taskyers.taskybase.task.dto.TaskDetailsDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

@Service
@AllArgsConstructor
public class TaskDetailsSLOImpl implements TaskDetailsSLO {
    
    private final AuthProvider authProvider;
    
    private final TaskDAO taskDAO;
    
    private final ProjectDAO projectDAO;
    
    @Override
    public ResponseEntity getTaskDetails(String key) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTaskAndProject(key, userEntity);
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskByKey(key).get();
            TaskDetailsDTO detailsDTO = TaskConverter.convertToDetailsDTO(taskEntity, UserUtils.getPersonals(userEntity));
            return ResponseEntity.ok(detailsDTO);
        }
        return isTaskFound;
    }
    
    private ResponseEntity checkForTaskAndProject(String key, UserEntity userEntity) {
        if ( !taskDAO.getTaskByKey(key).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.task_not_found.getMessage("key", key), MessageType.WARN));
        } else if ( !projectDAO.getProjectByNameAndUser(taskDAO.getTaskByKey(key).get().getProject().getName(), userEntity)
                .isPresent() ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
}
