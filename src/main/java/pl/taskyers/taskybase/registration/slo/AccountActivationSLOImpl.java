package pl.taskyers.taskybase.registration.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.message.MessageCode;
import pl.taskyers.taskybase.core.message.MessageType;
import pl.taskyers.taskybase.core.message.ResponseMessage;
import pl.taskyers.taskybase.core.repository.UserRepository;
import pl.taskyers.taskybase.core.slo.TokenSLO;
import pl.taskyers.taskybase.registration.entity.VerificationTokenEntity;

@Service
@AllArgsConstructor
@Slf4j
public class AccountActivationSLOImpl implements AccountActivationSLO {
    
    private final TokenSLO verificationTokenSLO;
    
    private final UserRepository userRepository;
    
    @Override
    public ResponseEntity activateAccount(String token) {
        VerificationTokenEntity verificationTokenEntity = (VerificationTokenEntity) verificationTokenSLO.getTokenEntity(token);
        if ( verificationTokenEntity != null ) {
            return ResponseEntity.ok(activateAccount(verificationTokenEntity));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(logWarn(token));
    }
    
    private ResponseMessage activateAccount(VerificationTokenEntity verificationTokenEntity) {
        UserEntity userEntity = verificationTokenEntity.getUser();
        verificationTokenSLO.deleteToken(verificationTokenEntity.getToken());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        return new ResponseMessage<>(MessageCode.account_activated.getMessage(), MessageType.SUCCESS);
    }
    
    private ResponseMessage logWarn(String token) {
        String message = MessageCode.field_not_found.getMessage("Token", token);
        log.warn(message);
        return new ResponseMessage<>(message, MessageType.WARN);
    }
    
}