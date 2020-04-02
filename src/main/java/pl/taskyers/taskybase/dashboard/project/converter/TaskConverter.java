package pl.taskyers.taskybase.dashboard.project.converter;

import pl.taskyers.taskybase.dashboard.project.dto.TaskDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

public class TaskConverter {
    
    public static TaskDTO convertToDTO(TaskEntity taskEntity) {
        return new TaskDTO(taskEntity.getKey(), taskEntity.getName(), EntryConverter.convertToDTO(taskEntity.getType()),
                EntryConverter.convertToDTO(taskEntity.getPriority()), EntryConverter.convertToDTO(taskEntity.getStatus()));
    }
    
}
