package pl.taskyers.taskybase.entry.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.entry.entity.StatusEntryEntity;
import pl.taskyers.taskybase.entry.slo.CustomizableEntrySLO;

@Component("statusEntryValidator")
@AllArgsConstructor
@Slf4j
public class StatusEntryValidator implements Validator<StatusEntryEntity> {
    
    private final CustomizableEntrySLO<StatusEntryEntity> statusEntrySLO;
    
    @Override
    public void validate(StatusEntryEntity object, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(object.getValue()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Status value"), "value");
        } else if ( statusEntrySLO.getEntryByValueAndProject(object.getValue(), object.getProject()).isPresent() ) {
            final String message =
                    MessageCode.entry_field_already_exists.getMessage("status", object.getValue(), "value", object.getProject().getName());
            log.error(message);
            validationMessageContainer.addError(message, "value");
        }
        
        if ( StringUtils.isBlank(object.getTextColor()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Text color"), "textColor");
        }
        
        if ( StringUtils.isBlank(object.getBackgroundColor()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Background color"), "backgroundColor");
        }
    }
    
}
