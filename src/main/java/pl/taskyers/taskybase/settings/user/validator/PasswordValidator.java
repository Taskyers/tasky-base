package pl.taskyers.taskybase.settings.user.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.utils.ValidationUtils;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.settings.user.dto.PasswordDTO;

@Component("passwordValidator")
@AllArgsConstructor
@Slf4j
public class PasswordValidator implements Validator<PasswordDTO> {
    
    private final AuthProvider authProvider;
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void validate(PasswordDTO object, ValidationMessageContainer validationMessageContainer, boolean checkForDuplicates) {
        System.out.println(passwordEncoder.matches(authProvider.getUserEntity().getPassword(), object.getCurrentPassword()));
        if ( StringUtils.isBlank(object.getCurrentPassword()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("Current password"), "currentPassword");
        } else if ( !passwordEncoder.matches(object.getCurrentPassword(), authProvider.getUserEntity().getPassword()) ) {
            validationMessageContainer.addError(MessageCode.password_invalid_current.getMessage(), "currentPassword");
        }
        
        if ( StringUtils.isBlank(object.getNewPassword()) ) {
            validationMessageContainer.addError(MessageCode.field_empty.getMessage("New password"), "newPassword");
        } else if ( !ValidationUtils.isUserPasswordValid(object.getNewPassword()) ) {
            validationMessageContainer.addError(MessageCode.password_invalid_format.getMessage(), "newPassword");
        } else if ( passwordEncoder.matches(object.getNewPassword(), authProvider.getUserEntity().getPassword()) ) {
            validationMessageContainer.addError(MessageCode.password_new_repeated.getMessage(), "newPassword");
        }
        
    }
    
}
