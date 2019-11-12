package pl.taskyers.taskybase.recovery.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.slo.TokenSLO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.recovery.entity.PasswordRecoveryTokenEntity;
import pl.taskyers.taskybase.recovery.repository.PasswordRecoveryTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Service("passwordRecoveryTokenSLO")
@AllArgsConstructor
@Slf4j
public class PasswordRecoveryTokenSLOImpl implements TokenSLO<PasswordRecoveryTokenEntity> {
    
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    
    @Override
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        while ( passwordRecoveryTokenRepository.findByToken(token).isPresent() ) {
            log.warn("Generated token already exists in database: " + token + ". Generating another one");
            token = UUID.randomUUID().toString();
        }
        return token;
    }
    
    @Override
    public String getToken(UserEntity userEntity) {
        return passwordRecoveryTokenRepository.findByUser(userEntity).isPresent() ?
                passwordRecoveryTokenRepository.findByUser(userEntity).get().getToken() : null;
    }
    
    @Override
    public void createToken(UserEntity userEntity) {
        PasswordRecoveryTokenEntity passwordRecoveryTokenEntity;
        if ( passwordRecoveryTokenRepository.findByUser(userEntity).isPresent() ) {
            passwordRecoveryTokenEntity = passwordRecoveryTokenRepository.findByUser(userEntity).get();
            passwordRecoveryTokenEntity.setToken(generateToken());
            log.debug("Updating token for user: " + userEntity.getUsername());
        } else {
            passwordRecoveryTokenEntity = new PasswordRecoveryTokenEntity();
            passwordRecoveryTokenEntity.setToken(generateToken());
            passwordRecoveryTokenEntity.setUser(userEntity);
            log.debug("Generating new token for user: " + userEntity.getUsername());
        }
        passwordRecoveryTokenRepository.save(passwordRecoveryTokenEntity);
    }
    
    @Deprecated
    @Override
    public void createToken(UserEntity userEntity, ProjectEntity projectEntity) {
    }
    
    @Override
    public PasswordRecoveryTokenEntity getTokenEntity(String token) {
        return passwordRecoveryTokenRepository.findByToken(token).isPresent() ? passwordRecoveryTokenRepository.findByToken(token).get() : null;
    }
    
    @Override
    public void deleteToken(String token) {
        Optional<PasswordRecoveryTokenEntity> passwordRecoveryTokenEntity = passwordRecoveryTokenRepository.findByToken(token);
        passwordRecoveryTokenEntity.ifPresent(recoveryTokenEntity -> passwordRecoveryTokenRepository.deleteById(recoveryTokenEntity.getId()));
    }
    
}
