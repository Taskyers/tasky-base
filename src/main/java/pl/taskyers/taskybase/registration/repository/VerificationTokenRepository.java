package pl.taskyers.taskybase.registration.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.registration.entity.VerificationTokenEntity;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationTokenEntity, Long> {
    
    Optional<VerificationTokenEntity> findByToken(String token);
    
    Optional<VerificationTokenEntity> findByUser(UserEntity userEntity);
    
}
