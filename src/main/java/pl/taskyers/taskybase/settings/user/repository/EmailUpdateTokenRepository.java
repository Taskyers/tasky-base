package pl.taskyers.taskybase.settings.user.repository;

import org.springframework.data.repository.CrudRepository;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.settings.user.entity.EmailUpdateTokenEntity;

import java.util.Optional;

public interface EmailUpdateTokenRepository extends CrudRepository<EmailUpdateTokenEntity, Long> {
    
    Optional<EmailUpdateTokenEntity> findByUser(UserEntity userEntity);
    
    Optional<EmailUpdateTokenEntity> findByToken(String token);
    
}
