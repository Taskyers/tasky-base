package pl.taskyers.taskybase.registration.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.message.MessageCode;
import pl.taskyers.taskybase.core.message.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.repository.UserRepository;
import pl.taskyers.taskybase.core.utils.ValidationUtils;
import pl.taskyers.taskybase.core.validator.Validator;

@Component
@AllArgsConstructor
@Slf4j
public class RegistrationValidator implements Validator<UserEntity> {
    
    private static final String FIELD_USERNAME = "username";
    
    private static final String FIELD_EMAIL = "email";
    
    private static final String FIELD_PASSWORD = "password";
    
    private static final String FIELD_NAME = "name";
    
    private static final String FIELD_SURNAME = "surname";
    
    private final UserRepository userRepository;
    
    @Override
    public void validate(UserEntity userEntity, ValidationMessageContainer validationMessageContainer) {
        String username = userEntity.getUsername();
        String email = userEntity.getEmail();
        String password = userEntity.getPassword();
        String name = userEntity.getName();
        String surname = userEntity.getSurname();
        validateUsername(username, validationMessageContainer);
        validateEmail(email, validationMessageContainer);
        validatePassword(password, validationMessageContainer);
        validateName(name, validationMessageContainer);
        validateSurname(surname, validationMessageContainer);
    }
    
    private void validateUsername(String username, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(username) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Username"), FIELD_USERNAME);
        } else if ( userRepository.findByUsername(username).isPresent() ) {
            String message = MessageCode.user_field_already_exists.getMessage(username, "username");
            validationMessageContainer.addError(message, FIELD_USERNAME);
            log.warn(message);
        }
    }
    
    private void validateEmail(String email, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(email) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Email"), FIELD_EMAIL);
        } else if ( !ValidationUtils.isUserEmailValid(email) ) {
            validationMessageContainer.addError(MessageCode.field_invalid_format.getMessage("email"), FIELD_EMAIL);
        } else if ( userRepository.findByEmail(email).isPresent() ) {
            String message = MessageCode.user_field_already_exists.getMessage(email, "email");
            validationMessageContainer.addError(message, FIELD_EMAIL);
            log.warn(message);
        }
    }
    
    private void validatePassword(String password, ValidationMessageContainer validationMessageContainer) {
        if ( StringUtils.isBlank(password) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Password"), FIELD_PASSWORD);
        } else if ( !ValidationUtils.isUserPasswordValid(password) ) {
            validationMessageContainer.addError(MessageCode.password_invalid_format.getMessage(), FIELD_PASSWORD);
        }
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
