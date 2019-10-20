package pl.taskyers.taskybase.recovery.repository;

import org.springframework.data.repository.CrudRepository;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.recovery.entity.PasswordRecoveryTokenEntity;

import java.util.Optional;

public interface PasswordRecoveryTokenRepository extends CrudRepository<PasswordRecoveryTokenEntity, Long> {
    
    Optional<PasswordRecoveryTokenEntity> findByUser(UserEntity userEntity);
    
    Optional<PasswordRecoveryTokenEntity> findByToken(String token);
    
}
