package pl.taskyers.taskybase.sprint.converter;

import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.sprint.dto.SprintDTO;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;

public class SprintConverter {
    
    public static SprintEntity convertFromDTO(SprintDTO sprintDTO) {
        SprintEntity sprintEntity = new SprintEntity();
        sprintEntity.setName(sprintDTO.getName());
        sprintEntity.setStart(DateUtils.parseDate(sprintDTO.getStart()));
        sprintEntity.setEnd(DateUtils.parseDate(sprintDTO.getEnd()));
        return sprintEntity;
    }
    
    public static SprintDTO convertToDTO(SprintEntity sprintEntity) {
        return new SprintDTO(sprintEntity.getName(), DateUtils.parseString(sprintEntity.getStart()), DateUtils.parseString(sprintEntity.getEnd()));
    }
    
}
