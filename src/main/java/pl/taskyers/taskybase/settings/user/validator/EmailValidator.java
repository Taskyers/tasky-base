package pl.taskyers.taskybase.settings.user.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.users.dao.UserDAO;
import pl.taskyers.taskybase.core.utils.ValidationUtils;
import pl.taskyers.taskybase.core.validator.Validator;

@Component("emailValidator")
@AllArgsConstructor
@Slf4j
public class EmailValidator implements Validator<String> {
    
    private final UserDAO userDAO;
    
    @Override
    public void validate(String object, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        if ( StringUtils.isBlank(object) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Email"), "email");
        } else if ( !ValidationUtils.isUserEmailValid(object) ) {
            validationMessageContainer.addError(MessageCode.field_invalid_format.getMessage("email"), "email");
        } else if ( userDAO.getEntityByEmail(object).isPresent() ) {
            String message = MessageCode.user_field_already_exists.getMessage(object, "email");
            validationMessageContainer.addError(message, "email");
            log.warn(message);
        }
    }
    
}
