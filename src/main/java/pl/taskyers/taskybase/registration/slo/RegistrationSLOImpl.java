package pl.taskyers.taskybase.registration.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.converters.AccountConverter;
import pl.taskyers.taskybase.core.users.dto.AccountDTO;
import pl.taskyers.taskybase.core.emails.EmailConstants;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.slo.TokenSLO;
import pl.taskyers.taskybase.core.users.slo.UserSLO;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.registration.validator.RegistrationValidator;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationSLOImpl implements RegistrationSLO {
    
    private final RegistrationValidator registrationValidator;
    
    private final UserSLO userSLO;
    
    private final EmailSLO emailSLO;
    
    private final TokenSLO verificationTokenSLO;
    
    @Override
    public ResponseEntity register(AccountDTO accountDTO) {
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        registrationValidator.validate(accountDTO, validationMessageContainer);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        
        ResponseMessage<UserEntity> resultMessage = saveUser(accountDTO);
        UserEntity userEntity = resultMessage.getObject();
        verificationTokenSLO.createToken(userEntity);
        emailSLO.sendEmailWithTemplateToSingleAddressee(accountDTO, MessageCode.email_subject_registration.getMessage(),
                EmailConstants.REGISTER_PATH, new String[]{ "name", "surname", "token" },
                new Object[]{ accountDTO.getName(), accountDTO.getSurname(),
                        EmailConstants.REGISTER_URL_TOKEN.replace("{tokenPlaceholder}", verificationTokenSLO.getToken(userEntity)) });
        
        return ResponseEntity.created(UriUtils.createURIFromId(userEntity.getId())).body(resultMessage);
    }
    
    @Override
    public boolean userExistsByUsername(String username) {
        return userSLO.getEntityByUsername(username).isPresent();
    }
    
    @Override
    public boolean userExistsByEmail(String email) {
        return userSLO.getEntityByEmail(email).isPresent();
    }
    
    private ResponseMessage<UserEntity> saveUser(AccountDTO accountDTO) {
        UserEntity savedUser = AccountConverter.convertFromDTO(accountDTO);
        userSLO.updatePassword(savedUser, savedUser.getPassword());
        return new ResponseMessage<UserEntity>(MessageCode.registration_successful.getMessage(), MessageType.SUCCESS, savedUser);
    }
    
}
