package pl.taskyers.taskybase.recovery.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.converters.AccountConverter;
import pl.taskyers.taskybase.core.emails.EmailConstants;
import pl.taskyers.taskybase.core.emails.EmailSLO;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.dao.TokenDAO;
import pl.taskyers.taskybase.core.users.dao.UserDAO;
import pl.taskyers.taskybase.recovery.entity.PasswordRecoveryTokenEntity;

@Service
@AllArgsConstructor
@Slf4j
public class PasswordRecoverySLOImpl implements PasswordRecoverySLO {
    
    private final UserDAO userDAO;
    
    private final EmailSLO emailSLO;
    
    private final TokenDAO passwordRecoveryTokenDAO;
    
    @Override
    public ResponseEntity sendEmailWithToken(String email) {
        if ( userDAO.getEntityByEmail(email).isPresent() ) {
            UserEntity userEntity = userDAO.getEntityByEmail(email).get();
            passwordRecoveryTokenDAO.createToken(userEntity);
            boolean emailWasSent = emailSLO.sendEmailWithTemplateToSingleAddressee(AccountConverter.convertToDTO(userEntity),
                    MessageCode.email_subject_password_recovery.getMessage(),
                    EmailConstants.PASSWORD_RECOVERY_PATH,
                    new String[]{ "token" },
                    new Object[]{
                            EmailConstants.PASSWORD_RECOVERY_URL_TOKEN.replace("{tokenPlaceholder}",
                                    passwordRecoveryTokenDAO.getToken(userEntity)) });
            
            return emailWasSent ?
                    ResponseEntity.ok(new ResponseMessage<String>(MessageCode.email_with_token_sent.getMessage(email), MessageType.SUCCESS)) :
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseMessage<>(MessageCode.server_problem_occured.getMessage(), MessageType.ERROR));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(MessageCode.field_not_found.getMessage("Email", email), MessageType.WARN));
    }
    
    @Override
    public ResponseEntity setNewPassword(String token, String password) {
        PasswordRecoveryTokenEntity passwordRecoveryTokenEntity = (PasswordRecoveryTokenEntity) passwordRecoveryTokenDAO.getTokenEntity(token);
        if ( passwordRecoveryTokenEntity == null ) {
            log.warn("Token " + token + " was not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<String>(MessageCode.field_not_found.getMessage("Token", token), MessageType.WARN));
        }
        userDAO.updatePassword(passwordRecoveryTokenEntity.getUser(), password);
        passwordRecoveryTokenDAO.deleteToken(token);
        return ResponseEntity.ok(new ResponseMessage<String>(MessageCode.field_updated.getMessage("Password"), MessageType.SUCCESS));
    }
    
}
