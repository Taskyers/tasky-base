package pl.taskyers.taskybase.registration.slo;

import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.converters.AccountConverter;
import pl.taskyers.taskybase.core.dto.AccountDTO;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.message.MessageCode;
import pl.taskyers.taskybase.core.message.MessageType;
import pl.taskyers.taskybase.core.message.ResponseMessage;
import pl.taskyers.taskybase.core.message.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.repository.UserRepository;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.registration.validator.RegistrationValidator;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static pl.taskyers.taskybase.core.emails.EmailConstants.EMAIL_REGISTER_PATH;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationSLOImpl implements RegistrationSLO {
    
    private final RegistrationValidator registrationValidator;
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final EmailSLO emailSLO;
    
    private final VerificationTokenSLO verificationTokenSLO;
    
    @Override
    public ResponseEntity register(AccountDTO accountDTO) {
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        registrationValidator.validate(accountDTO, validationMessageContainer);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        
        ResponseMessage<UserEntity> resultMessage = saveUser(accountDTO);
        UserEntity object = resultMessage.getObject();
        verificationTokenSLO.createVerificationToken(object);
        sendEmail(accountDTO, verificationTokenSLO.getVerificationToken(object));
        return ResponseEntity.created(UriUtils.createURIFromId(object.getId())).body(resultMessage);
    }
    
    @Override
    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    private ResponseMessage<UserEntity> saveUser(AccountDTO accountDTO) {
        accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        UserEntity savedUser = AccountConverter.convertFromDTO(accountDTO);
        userRepository.save(savedUser);
        return new ResponseMessage<UserEntity>(MessageCode.registration_successful.getMessage(), MessageType.SUCCESS, savedUser);
    }
    
    private void sendEmail(AccountDTO accountDTO, String token) {
        final String address = accountDTO.getEmail();
        final String personal = accountDTO.getName() + " " + accountDTO.getSurname();
        try {
            Map<String, Object> model = emailSLO
                    .createModel(new String[]{ "name", "surname", "token" },
                            new Object[]{ accountDTO.getName(), accountDTO.getSurname(), "/activateAccount/" + token });
            emailSLO.sendEmailWithTemplateToSingleAddressee(address, personal, MessageCode.email_subject_registration.getMessage(),
                    EMAIL_REGISTER_PATH, model);
            log.debug("Email was sent to: " + address);
        } catch ( UnsupportedEncodingException | CannotSendEmailException e ) {
            log.error("Email could not be send to: " + address);
        }
    }
    
}
