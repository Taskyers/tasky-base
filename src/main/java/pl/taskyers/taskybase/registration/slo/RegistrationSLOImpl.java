package pl.taskyers.taskybase.registration.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    public ResponseEntity register(UserEntity userEntity) {
        ValidationMessageContainer validationMessageContainer = new ValidationMessageContainer();
        registrationValidator.validate(userEntity, validationMessageContainer);
        if ( validationMessageContainer.hasErrors() ) {
            return ResponseEntity.badRequest().body(validationMessageContainer.getErrors());
        }
        
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);
        ResponseMessage<UserEntity> resultMessage =
                new ResponseMessage<UserEntity>(MessageCode.registration_successful.getMessage(), MessageType.SUCCESS, savedUser);
        
        return ResponseEntity.created(UriUtils.createURIFromId(savedUser.getId())).body(resultMessage);
    }
    
}
