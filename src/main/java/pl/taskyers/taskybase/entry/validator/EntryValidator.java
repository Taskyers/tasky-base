package pl.taskyers.taskybase.entry.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.entry.slo.EntrySLO;

@Component("statusEntryValidator")
@AllArgsConstructor
@Slf4j
public class EntryValidator implements Validator<EntryEntity> {
    
    private final EntrySLO entityEntrySLO;
    
    @Override
    public void validate(EntryEntity object, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(object.getValue()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Entry value"), "value");
        } else if ( object.getEntryType() != null &&
                    entityEntrySLO.getEntryByEntryTypeAndValueAndProject(object.getEntryType(), object.getValue(), object.getProject())
                            .isPresent() ) {
            final String message =
                    MessageCode.entry_field_already_exists.getMessage(object.getEntryType(), object.getValue(), "value",
                            object.getProject().getName());
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
