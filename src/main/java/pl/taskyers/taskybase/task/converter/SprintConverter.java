package pl.taskyers.taskybase.task.converter;

import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.dto.SprintDTO;

public class SprintConverter {
    
    public static SprintDTO convertToDTO(SprintEntity sprintEntity) {
        return new SprintDTO(sprintEntity.getName(),
                DateUtils.checkIfDateBetweenTwoDates(DateUtils.getCurrentDate(), sprintEntity.getStart(), sprintEntity.getEnd()));
    }
    
}
