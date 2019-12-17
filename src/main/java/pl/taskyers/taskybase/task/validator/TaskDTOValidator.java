package pl.taskyers.taskybase.task.validator;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.task.dto.TaskDTO;

@Component("taskDTOValidator")
@AllArgsConstructor
public class TaskDTOValidator implements Validator<TaskDTO> {
    
    @Override
    public void validate(TaskDTO object, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        if ( StringUtils.isBlank(object.getName()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Name"), "name");
        }
        
        if ( StringUtils.isBlank(object.getType()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Type"), "type");
        }
        
        if ( StringUtils.isBlank(object.getStatus()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Status"), "status");
        }
        
        if ( StringUtils.isBlank(object.getPriority()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Priority"), "priority");
        }
    }
    
}
