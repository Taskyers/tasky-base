package pl.taskyers.taskybase.project.converter;

import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.dashboard.project.converter.EntryConverter;
import pl.taskyers.taskybase.project.dto.BoardTaskDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

public class BoardTaskConverter {
    
    private BoardTaskConverter() {
    }
    
    public static BoardTaskDTO convertToDTO(TaskEntity taskEntity) {
        BoardTaskDTO boardTaskDTO = new BoardTaskDTO();
        boardTaskDTO.setKey(taskEntity.getKey());
        boardTaskDTO.setName(taskEntity.getName());
        boardTaskDTO.setAssignee(UserUtils.getPersonals(taskEntity.getAssignee()));
        boardTaskDTO.setType(EntryConverter.convertToDTO(taskEntity.getType()));
        boardTaskDTO.setPriority(EntryConverter.convertToDTO(taskEntity.getPriority()));
        return boardTaskDTO;
    }
    
}
