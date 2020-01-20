package pl.taskyers.taskybase.settings.user.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.utils.ValidationUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.settings.user.dto.UserDTO;

@Component("userValidator")
@AllArgsConstructor
@Slf4j
public class UserValidator implements Validator<UserDTO> {
    
    private static final String FIELD_NAME = "name";
    
    private static final String FIELD_SURNAME = "surname";
    
    @Override
    public void validate(UserDTO userDTO, ValidationMessageContainer validationMessageContainer, boolean validateEmail) {
        String name = userDTO.getName();
        String surname = userDTO.getSurname();
        validateName(name, validationMessageContainer);
        validateSurname(surname, validationMessageContainer);
    }
    
    private void validateName(String name, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(name) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Name"), FIELD_NAME);
        } else if ( !ValidationUtils.isUserNameValid(name) ) {
            validationMessageContainer.addError(MessageCode.field_invalid_format.getMessage("name"), FIELD_NAME);
        }
    }
    
    private void validateSurname(String surname, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(surname) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Surname"), FIELD_SURNAME);
        } else if ( !ValidationUtils.isUserSurnameValid(surname) ) {
            validationMessageContainer.addError(MessageCode.field_invalid_format.getMessage("surname"), FIELD_SURNAME);
        }
    }
    
}
