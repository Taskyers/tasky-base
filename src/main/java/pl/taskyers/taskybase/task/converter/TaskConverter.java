package pl.taskyers.taskybase.task.converter;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.dashboard.project.converter.EntryConverter;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.task.dto.CommentDTO;
import pl.taskyers.taskybase.task.dto.TaskDTO;
import pl.taskyers.taskybase.task.dto.TaskDetailsDTO;
import pl.taskyers.taskybase.task.entity.CommentEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TaskConverter {
    
    public static TaskEntity convertFromDTO(TaskDTO taskDTO, ProjectEntity projectEntity) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setName(taskDTO.getName());
        taskEntity.setDescription(taskDTO.getDescription());
        taskEntity.setFixVersion(taskDTO.getFixVersion());
        taskEntity.setProject(projectEntity);
        return taskEntity;
    }
    
    public static TaskDTO convertToDTO(TaskEntity taskEntity) {
        return new TaskDTO(taskEntity.getName(), taskEntity.getDescription(), taskEntity.getStatus().getValue(), taskEntity.getPriority().getValue(),
                taskEntity.getType().getValue(), taskEntity.getFixVersion(), taskEntity.getSprint().getName());
    }
    
    public static TaskDetailsDTO convertToDetailsDTO(TaskEntity taskEntity, UserEntity userEntity) {
        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();
        taskDetailsDTO.setId(taskEntity.getId());
        taskDetailsDTO.setKey(taskEntity.getKey());
        taskDetailsDTO.setName(taskEntity.getName());
        taskDetailsDTO.setDescription(taskEntity.getDescription());
        taskDetailsDTO.setAssignee(UserUtils.getPersonals(taskEntity.getAssignee()));
        taskDetailsDTO.setCreator(UserUtils.getPersonals(taskEntity.getCreator()));
        taskDetailsDTO.setWatchers(convertWatchers(taskEntity.getWatchers()));
        taskDetailsDTO.setComments(convertComments(taskEntity.getComments(), UserUtils.getPersonals(userEntity)));
        taskDetailsDTO.setStatus(EntryConverter.convertToDTO(taskEntity.getStatus()));
        taskDetailsDTO.setType(EntryConverter.convertToDTO(taskEntity.getType()));
        taskDetailsDTO.setPriority(EntryConverter.convertToDTO(taskEntity.getPriority()));
        taskDetailsDTO.setFixVersion(taskEntity.getFixVersion());
        taskDetailsDTO.setSprint(SprintConverter.convertToDTO(taskEntity.getSprint()));
        taskDetailsDTO.setCreationDate(DateUtils.parseStringDatetime(taskEntity.getCreationDate()));
        taskDetailsDTO.setUpdateDate(DateUtils.parseStringDatetime(taskEntity.getUpdateDate()));
        taskDetailsDTO.setResolution(taskEntity.getResolution());
        taskDetailsDTO.setAssignedToMe(taskEntity.getAssignee() != null && taskEntity.getAssignee().getId().equals(userEntity.getId()));
        taskDetailsDTO.setWatching(watcherExists(taskEntity.getWatchers(), userEntity) != null);
        return taskDetailsDTO;
    }
    
    private static UserEntity watcherExists(Set<UserEntity> watchers, UserEntity userEntity) {
        return watchers.stream().filter(watcher -> watcher.getId().equals(userEntity.getId())).findFirst().orElse(null);
    }
    
    private static List<String> convertWatchers(Set<UserEntity> users) {
        List<String> watchers = new ArrayList<>();
        for ( UserEntity userEntity : users ) {
            watchers.add(UserUtils.getPersonals(userEntity));
        }
        return watchers;
    }
    
    private static List<CommentDTO> convertComments(Set<CommentEntity> commentEntities, String personals) {
        List<CommentEntity> entities = new ArrayList<>(commentEntities);
        entities.sort(Comparator.comparing(CommentEntity::getCreationDate));
        List<CommentDTO> comments = new ArrayList<>();
        for ( CommentEntity commentEntity : entities ) {
            comments.add(CommentConverter.convertToDTO(commentEntity, personals));
        }
        return comments;
    }
    
}
