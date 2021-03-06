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
import pl.taskyers.taskybase.dashboard.project.dto.TaskDTO;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.dao.EntryDAO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.ResolutionType;
import pl.taskyers.taskybase.task.converter.SprintConverter;
import pl.taskyers.taskybase.task.converter.TaskConverter;
import pl.taskyers.taskybase.task.dao.TaskDAO;
import pl.taskyers.taskybase.task.dto.SprintDTO;
import pl.taskyers.taskybase.task.dto.TaskDetailsDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TaskDetailsSLOImpl implements TaskDetailsSLO {
    
    private final AuthProvider authProvider;
    
    private final TaskDAO taskDAO;
    
    private final ProjectDAO projectDAO;
    
    private final EntryDAO entryDAO;
    
    @Override
    public ResponseEntity getTaskDetails(String key) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTaskAndProject(key, userEntity);
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskByKey(key).get();
            TaskDetailsDTO detailsDTO = TaskConverter.convertToDetailsDTO(taskEntity, userEntity);
            return ResponseEntity.ok(detailsDTO);
        }
        return isTaskFound;
    }
    
    @Override
    public ResponseEntity getEntries(String key, EntryType entryType) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTaskAndProject(key, userEntity);
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskByKey(key).get();
            final ProjectEntity projectEntity = taskEntity.getProject();
            return ResponseEntity.ok(convertEntries(entryDAO.getAllByProjectAndEntryType(projectEntity, entryType)));
        }
        return isTaskFound;
    }
    
    @Override
    public ResponseEntity getResolutionTypes(String key) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTaskAndProject(key, userEntity);
        return isTaskFound == null ? ResponseEntity.ok(convertResolutionTypes()) : isTaskFound;
    }
    
    @Override
    public List<pl.taskyers.taskybase.dashboard.project.dto.TaskDTO> getUserTasksByName(String name) {
        List<TaskEntity> taskEntities = taskDAO.getUserTasksByNameLike(projectDAO.getAllProjectsEntities(), name);
        return convertTasksToDTO(taskEntities);
    }
    
    @Override
    public ResponseEntity getSprintsByTask(String key) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTaskAndProject(key, userEntity);
        if ( isTaskFound == null ) {
            final ProjectEntity projectEntity = taskDAO.getTaskByKey(key).get().getProject();
            return ResponseEntity.ok(convertSprints(projectEntity.getSprints()));
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
    
    private List<String> convertEntries(List<EntryEntity> entryEntities) {
        List<String> entries = new ArrayList<>();
        for ( EntryEntity entryEntity : entryEntities ) {
            entries.add(entryEntity.getValue());
        }
        return entries;
    }
    
    private List<String> convertResolutionTypes() {
        List<String> types = new ArrayList<>();
        for ( ResolutionType resolutionType : ResolutionType.values() ) {
            types.add(resolutionType.getValue());
        }
        return types;
    }
    
    private List<pl.taskyers.taskybase.dashboard.project.dto.TaskDTO> convertTasksToDTO(List<TaskEntity> entities) {
        List<TaskDTO> tasks = new ArrayList<>();
        for ( TaskEntity taskEntity : entities ) {
            tasks.add(pl.taskyers.taskybase.dashboard.project.converter.TaskConverter.convertToDTO(taskEntity));
        }
        return tasks;
    }
    
    private List<SprintDTO> convertSprints(Set<SprintEntity> sprintEntities) {
        List<SprintDTO> sprints = new ArrayList<>();
        for ( SprintEntity sprintEntity : sprintEntities ) {
            sprints.add(SprintConverter.convertToDTO(sprintEntity));
        }
        return sprints;
    }
    
}
