package pl.taskyers.taskybase.registration.slo;

import com.sun.mail.util.MailConnectException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import pl.taskyers.taskybase.core.validator.Validator;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationSLOImpl implements RegistrationSLO {
    
    private final Validator<AccountDTO> registrationValidator;
    
    private final UserSLO userSLO;
    
    private final EmailSLO emailSLO;
    
    private final TokenSLO verificationTokenSLO;
    
    @Override
    @Transactional(rollbackOn = MailConnectException.class)
    public ResponseEntity register(AccountDTO accountDTO) {
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        registrationValidator.validate(accountDTO, validationMessageContainer);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        
        UserEntity savedUser = saveUser(AccountConverter.convertFromDTO(accountDTO));
        boolean emailWasSent = emailSLO.sendEmailWithTemplateToSingleAddressee(accountDTO, MessageCode.email_subject_registration.getMessage(),
                EmailConstants.REGISTER_PATH, new String[]{ "name", "surname", "token" },
                new Object[]{ accountDTO.getName(), accountDTO.getSurname(),
                        EmailConstants.REGISTER_URL_TOKEN.replace("{tokenPlaceholder}", verificationTokenSLO.getToken(savedUser)) });
        
        return emailWasSent ? ResponseEntity.created(UriUtils.createURIFromId(savedUser.getId()))
                .body(new ResponseMessage<>(MessageCode.registration_successful.getMessage(), MessageType.SUCCESS, savedUser)) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseMessage<>(MessageCode.server_problem_occured.getMessage(), MessageType.ERROR));
    }
    
    @Override
    public boolean userExistsByUsername(String username) {
        return userSLO.getEntityByUsername(username).isPresent();
    }
    
    @Override
    public boolean userExistsByEmail(String email) {
        return userSLO.getEntityByEmail(email).isPresent();
    }
    
    private UserEntity saveUser(UserEntity userEntity) {
        UserEntity savedUser = userSLO.registerUser(userEntity);
        verificationTokenSLO.createToken(savedUser);
        return savedUser;
    }
    
}
