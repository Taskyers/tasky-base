package pl.taskyers.taskybase.registration.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.converters.AccountConverter;
import pl.taskyers.taskybase.core.dto.AccountDTO;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.message.MessageCode;
import pl.taskyers.taskybase.core.message.MessageType;
import pl.taskyers.taskybase.core.message.ResponseMessage;
import pl.taskyers.taskybase.core.message.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.repository.UserRepository;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.registration.validator.RegistrationValidator;

@Service
@AllArgsConstructor
public class RegistrationSLOImpl implements RegistrationSLO {
    
    private final RegistrationValidator registrationValidator;
    
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public ResponseEntity register(AccountDTO accountDTO) {
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        registrationValidator.validate(accountDTO, validationMessageContainer);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        ResponseMessage<UserEntity> resultMessage = saveUser(accountDTO);
        return ResponseEntity.created(UriUtils.createURIFromId(resultMessage.getObject().getId())).body(resultMessage);
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
    
}
