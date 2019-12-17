package pl.taskyers.taskybase.task.converter;

import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.task.dto.TaskDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

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
    
}
