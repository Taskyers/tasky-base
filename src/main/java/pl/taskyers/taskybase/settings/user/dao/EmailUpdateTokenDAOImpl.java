package pl.taskyers.taskybase.settings.user.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.dao.TokenDAO;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.settings.user.entity.EmailUpdateTokenEntity;
import pl.taskyers.taskybase.settings.user.repository.EmailUpdateTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Service("emailUpdateTokenDAO")
@AllArgsConstructor
@Slf4j
public class EmailUpdateTokenDAOImpl implements TokenDAO<EmailUpdateTokenEntity> {
    
    private final EmailUpdateTokenRepository emailUpdateTokenRepository;
    
    @Override
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        while ( emailUpdateTokenRepository.findByToken(token).isPresent() ) {
            log.warn("Generated token already exists in database: " + token + ". Generating another one");
            token = UUID.randomUUID().toString();
        }
        return token;
    }
    
    @Override
    public String getToken(UserEntity userEntity) {
        return emailUpdateTokenRepository.findByUser(userEntity).isPresent() ?
                emailUpdateTokenRepository.findByUser(userEntity).get().getToken() : null;
    }
    
    @Override
    public EmailUpdateTokenEntity getTokenEntity(String token) {
        return emailUpdateTokenRepository.findByToken(token).isPresent() ? emailUpdateTokenRepository.findByToken(token).get() : null;
    }
    
    @Override
    public void createToken(UserEntity userEntity) {
        EmailUpdateTokenEntity emailUpdateTokenEntity;
        if ( emailUpdateTokenRepository.findByUser(userEntity).isPresent() ) {
            emailUpdateTokenEntity = emailUpdateTokenRepository.findByUser(userEntity).get();
            emailUpdateTokenEntity.setToken(generateToken());
            emailUpdateTokenEntity.setEmail(userEntity.getEmail());
            log.debug("Updating token for user: " + userEntity.getUsername());
        } else {
            emailUpdateTokenEntity = new EmailUpdateTokenEntity();
            emailUpdateTokenEntity.setToken(generateToken());
            emailUpdateTokenEntity.setUser(userEntity);
            emailUpdateTokenEntity.setEmail(userEntity.getEmail());
            log.debug("Generating new token for user: " + userEntity.getUsername());
        }
        emailUpdateTokenRepository.save(emailUpdateTokenEntity);
    }
    
    @Deprecated
    @Override
    public void createToken(UserEntity userEntity, ProjectEntity projectEntity) {
    
    }
    
    @Override
    public void deleteToken(String token) {
        Optional<EmailUpdateTokenEntity> emailUpdateTokenEntity = emailUpdateTokenRepository.findByToken(token);
        emailUpdateTokenEntity.ifPresent(recoveryTokenEntity -> emailUpdateTokenRepository.deleteById(recoveryTokenEntity.getId()));
    }
    
}
