package pl.taskyers.taskybase.task.validator;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.task.dto.UpdateTaskData;

@Component("updateTaskDataValidator")
@AllArgsConstructor
public class UpdateTaskDataValidator implements Validator<UpdateTaskData> {
    
    @Override
    public void validate(UpdateTaskData object, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        if ( StringUtils.isBlank(object.getName()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Name"), "name");
        }
    }
    
}
