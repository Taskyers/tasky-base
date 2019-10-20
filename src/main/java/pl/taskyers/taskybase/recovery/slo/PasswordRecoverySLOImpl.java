package pl.taskyers.taskybase.recovery.slo;

import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.message.MessageCode;
import pl.taskyers.taskybase.core.message.MessageType;
import pl.taskyers.taskybase.core.message.ResponseMessage;
import pl.taskyers.taskybase.core.repository.UserRepository;
import pl.taskyers.taskybase.recovery.entity.PasswordRecoveryTokenEntity;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

import static pl.taskyers.taskybase.core.emails.EmailConstants.EMAIL_RECOVERY_PASSWORD_PATH;

@Service
@AllArgsConstructor
@Slf4j
public class PasswordRecoverySLOImpl implements PasswordRecoverySLO {
    
    private final UserRepository userRepository;
    
    private final EmailSLO emailSLO;
    
    private final PasswordRecoveryTokenSLO passwordRecoveryTokenSLO;
    
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public ResponseEntity sendEmailWithToken(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if ( userEntity.isPresent() ) {
            passwordRecoveryTokenSLO.createPasswordRecoveryToken(userEntity.get());
            sendEmail(userEntity.get());
            return ResponseEntity.ok(new ResponseMessage<String>("Email with token was sent to provided address", MessageType.SUCCESS));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<>("Email was not found", MessageType.WARN));
    }
    
    @Override
    public ResponseEntity setNewPassword(String token, String password) {
        PasswordRecoveryTokenEntity passwordRecoveryTokenEntity = passwordRecoveryTokenSLO.getTokenEntity(token);
        if ( passwordRecoveryTokenEntity == null ) {
            log.warn("Token " + token + " was not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage<String>("Token was not found", MessageType.WARN));
        }
        UserEntity userEntity = passwordRecoveryTokenEntity.getUser();
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
        passwordRecoveryTokenSLO.deleteToken(token);
        return ResponseEntity.ok(new ResponseMessage<String>("Password successfully changed", MessageType.SUCCESS));
    }
    
    private void sendEmail(UserEntity userEntity) {
        String token = passwordRecoveryTokenSLO.getPasswordRecoveryToken(userEntity);
        String address = userEntity.getEmail();
        String personal = userEntity.getName() + " " + userEntity.getSurname();
        try {
            Map<String, Object> model = emailSLO.createModel(new String[]{ "token" }, new Object[]{ "/passwordRecovery/" + token });
            emailSLO.sendEmailWithTemplateToSingleAddressee(address, personal, MessageCode.email_subject_password_recovery.getMessage(),
                    EMAIL_RECOVERY_PASSWORD_PATH, model);
            log.debug("Password recovery email was sent to: " + address);
        } catch ( UnsupportedEncodingException | CannotSendEmailException e ) {
            e.printStackTrace();
            log.error("Cannot send password recovery email to: " + address);
        }
    }
    
}
