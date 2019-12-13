package pl.taskyers.taskybase.registration.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.dao.TokenDAO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.registration.entity.VerificationTokenEntity;
import pl.taskyers.taskybase.registration.repository.VerificationTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Service("verificationTokenDAO")
@AllArgsConstructor
@Slf4j
public class VerificationTokenDAOImpl implements TokenDAO<VerificationTokenEntity> {
    
    private final VerificationTokenRepository verificationTokenRepository;
    
    @Override
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        while ( verificationTokenRepository.findByToken(token).isPresent() ) {
            log.warn("Generated verification token already exists in database: " + token + ". Generating another one");
            token = UUID.randomUUID().toString();
        }
        return token;
    }
    
    @Override
    public void createToken(UserEntity userEntity) {
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity();
        verificationTokenEntity.setToken(generateToken());
        verificationTokenEntity.setUser(userEntity);
        verificationTokenRepository.save(verificationTokenEntity);
    }
    
    @Deprecated
    @Override
    public void createToken(UserEntity userEntity, ProjectEntity projectEntity) {
    }
    
    @Override
    public String getToken(UserEntity userEntity) {
        return verificationTokenRepository.findByUser(userEntity).isPresent() ? verificationTokenRepository.findByUser(userEntity).get().getToken() :
                null;
    }
    
    @Override
    public VerificationTokenEntity getTokenEntity(String token) {
        return verificationTokenRepository.findByToken(token).isPresent() ? verificationTokenRepository.findByToken(token).get() : null;
    }
    
    @Override
    public void deleteToken(String token) {
        Optional<VerificationTokenEntity> verificationTokenEntity = verificationTokenRepository.findByToken(token);
        verificationTokenEntity.ifPresent(tokenEntity -> verificationTokenRepository.deleteById(tokenEntity.getId()));
    }
    
}
