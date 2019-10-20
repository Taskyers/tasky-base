package pl.taskyers.taskybase.recovery.slo;

import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.message.MessageCode;
import pl.taskyers.taskybase.core.message.MessageType;
import pl.taskyers.taskybase.core.message.ResponseMessage;
import pl.taskyers.taskybase.core.slo.TokenSLO;
import pl.taskyers.taskybase.core.slo.UserSLO;
import pl.taskyers.taskybase.recovery.entity.PasswordRecoveryTokenEntity;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

import static pl.taskyers.taskybase.core.emails.EmailConstants.EMAIL_RECOVERY_PASSWORD_PATH;

@Service
@AllArgsConstructor
@Slf4j
public class PasswordRecoverySLOImpl implements PasswordRecoverySLO {
    
    private final UserSLO userSLO;
    
    private final EmailSLO emailSLO;
    
    private final TokenSLO passwordRecoveryTokenSLO;
    
    @Override
    public ResponseEntity sendEmailWithToken(String email) {
        Optional<UserEntity> userEntity = userSLO.getEntityByEmail(email);
        if ( userEntity.isPresent() ) {
            passwordRecoveryTokenSLO.createToken(userEntity.get());
            sendEmail(userEntity.get());
            return ResponseEntity.ok(new ResponseMessage<String>(MessageCode.email_with_token_sent.getMessage(email), MessageType.SUCCESS));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.field_not_found.getMessage("Email", email), MessageType.WARN));
    }
    
    @Override
    public ResponseEntity setNewPassword(String token, String password) {
        PasswordRecoveryTokenEntity passwordRecoveryTokenEntity = (PasswordRecoveryTokenEntity) passwordRecoveryTokenSLO.getTokenEntity(token);
        if ( passwordRecoveryTokenEntity == null ) {
            log.warn("Token " + token + " was not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<String>(MessageCode.field_not_found.getMessage("Token", token), MessageType.WARN));
        }
        userSLO.updatePassword(passwordRecoveryTokenEntity.getUser(), password);
        passwordRecoveryTokenSLO.deleteToken(token);
        return ResponseEntity.ok(new ResponseMessage<String>(MessageCode.field_updated.getMessage("Password"), MessageType.SUCCESS));
    }
    
    private void sendEmail(UserEntity userEntity) {
        String token = passwordRecoveryTokenSLO.getToken(userEntity);
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
