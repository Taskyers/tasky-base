package pl.taskyers.taskybase.sprint.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.sprint.slo.SprintSLO;

@Component("sprintValidator")
@AllArgsConstructor
@Slf4j
public class SprintValidator implements Validator<SprintEntity> {
    
    private final SprintSLO sprintSLO;
    
    @Override
    public void validate(SprintEntity object, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(object.getName()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Name"), "name");
        } else if ( sprintSLO.doesNameExistsInProject(object.getName(), object.getProject()) ) {
            validationMessageContainer.addError(MessageCode.sprint_field_already_exists.getMessage("name", object.getName()), "name");
        }
        
        if ( object.getStart() == null ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Start date"), "start");
        }
        
        if ( object.getEnd() == null ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("End date"), "end");
        }
        
        if ( object.getStart() != null & object.getEnd() != null && object.getStart().after(object.getEnd()) ) {
            validationMessageContainer.addError(MessageCode.sprint_invalid_date.getMessage(), "start, end");
        }
        
    }
    
}
