package pl.taskyers.taskybase.project.converter;

import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.project.dto.SprintResponseData;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;

import java.util.Date;

public class SprintConverter {
    
    public static SprintResponseData convertToDTO(SprintEntity sprintEntity) {
        Date current = DateUtils.getCurrentDate();
        boolean isCurrentSprint = false;
        final Date start = sprintEntity.getStart();
        final Date end = sprintEntity.getEnd();
        if ( DateUtils.checkIfDateBetweenTwoDates(current, start, end) ) {
            isCurrentSprint = true;
        }
        return new SprintResponseData(sprintEntity.getId(), isCurrentSprint, sprintEntity.getName(), DateUtils.parseString(sprintEntity.getStart()),
                DateUtils.parseString(sprintEntity.getEnd()));
    }
    
}
