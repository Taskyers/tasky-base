package pl.taskyers.taskybase.registration.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.registration.entity.VerificationTokenEntity;
import pl.taskyers.taskybase.registration.repository.VerificationTokenRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class VerificationTokenSLOImpl implements VerificationTokenSLO {
    
    private final VerificationTokenRepository verificationTokenRepository;
    
    @Override
    public void createVerificationToken(UserEntity userEntity) {
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity();
        verificationTokenEntity.setToken(generateVerificationToken());
        verificationTokenEntity.setUser(userEntity);
        verificationTokenRepository.save(verificationTokenEntity);
    }
    
    @Override
    public String getVerificationToken(UserEntity userEntity) {
        return verificationTokenRepository.findByUser(userEntity).isPresent() ? verificationTokenRepository.findByUser(userEntity).get().getToken() :
                null;
    }
    
    private String generateVerificationToken() {
        String token = UUID.randomUUID().toString();
        while ( verificationTokenRepository.findByToken(token).isPresent() ) {
            log.warn("Generated token already exists in database: " + token);
            token = UUID.randomUUID().toString();
        }
        return token;
    }
    
}
