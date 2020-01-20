package pl.taskyers.taskybase.settings.user.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.dao.TokenDAO;
import pl.taskyers.taskybase.core.emails.EmailConstants;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.converters.AccountConverter;
import pl.taskyers.taskybase.core.users.dao.UserDAO;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.validator.Validator;
import pl.taskyers.taskybase.settings.user.converter.UserConverter;
import pl.taskyers.taskybase.settings.user.dto.PasswordDTO;
import pl.taskyers.taskybase.settings.user.dto.UserDTO;
import pl.taskyers.taskybase.settings.user.entity.EmailUpdateTokenEntity;

@Service
@AllArgsConstructor
@Slf4j
public class UserSettingsSLOImpl implements UserSettingsSLO {
    
    private final UserDAO userDAO;
    
    private final AuthProvider authProvider;
    
    private final Validator<UserDTO> userValidator;
    
    private final Validator<PasswordDTO> passwordValidator;
    
    private final Validator<String> emailValidator;
    
    private final TokenDAO emailUpdateTokenDAO;
    
    private final EmailSLO emailSLO;
    
    @Override
    public UserDTO getUserDetails() {
        UserEntity userEntity = authProvider.getUserEntity();
        UserDTO userDTO = UserConverter.convertToDTO(userEntity);
        return userDTO;
    }
    
    @Override
    public ResponseEntity updateUser(UserDTO userDTO) {
        UserEntity userEntity = authProvider.getUserEntity();
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        userValidator.validate(userDTO, validationMessageContainer, false);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        UserEntity updatedUser = userDAO.updateUser(userEntity, userDTO);
        
        return ResponseEntity.ok(new ResponseMessage<>(MessageCode.user_updated.getMessage(), MessageType.SUCCESS, updatedUser));
    }
    
    @Override
    public ResponseEntity updatePassword(PasswordDTO passwordDTO) {
        UserEntity userEntity = authProvider.getUserEntity();
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        passwordValidator.validate(passwordDTO, validationMessageContainer, false);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        userDAO.updatePassword(userEntity, passwordDTO.getNewPassword());
        return ResponseEntity.ok(new ResponseMessage<String>(MessageCode.field_updated.getMessage("Password"), MessageType.SUCCESS));
    }
    
    @Override
    public ResponseEntity sendTokenToNewEmail(String email) {
        UserEntity userEntity = authProvider.getUserEntity();
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        emailValidator.validate(email, validationMessageContainer, false);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        userEntity.setEmail(email);
        emailUpdateTokenDAO.createToken(userEntity);
        boolean emailWasSent = emailSLO.sendEmailWithTemplateToSingleAddressee(AccountConverter.convertToDTO(userEntity),
                MessageCode.email_subject_email_update.getMessage(),
                EmailConstants.EMAIL_UPDATE_PATH,
                new String[]{ "token" },
                new Object[]{
                        EmailConstants.EMAIL_UPDATE_URL_TOKEN.replace("{tokenPlaceholder}",
                                emailUpdateTokenDAO.getToken(userEntity)) });
        
        return emailWasSent ?
                ResponseEntity.ok(new ResponseMessage<String>(MessageCode.email_with_token_sent.getMessage(email), MessageType.SUCCESS)) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMessage<>(MessageCode.server_problem_occured.getMessage(), MessageType.ERROR));
        
    }
    
    @Override
    public ResponseEntity acceptNewEmail(String token) {
        EmailUpdateTokenEntity emailUpdateTokenEntity = (EmailUpdateTokenEntity) emailUpdateTokenDAO.getTokenEntity(token);
        if ( emailUpdateTokenEntity == null ) {
            log.warn("Token " + token + " was not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<String>(MessageCode.field_not_found.getMessage("Token", token), MessageType.WARN));
        }
        userDAO.updateEmail(emailUpdateTokenEntity.getUser(), emailUpdateTokenEntity.getEmail());
        emailUpdateTokenDAO.deleteToken(token);
        return ResponseEntity.ok(new ResponseMessage<String>(MessageCode.field_updated.getMessage("Email"), MessageType.SUCCESS));
    }
    
}
