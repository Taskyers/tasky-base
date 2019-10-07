package pl.taskyers.taskybase.core.repository;

import org.springframework.data.repository.CrudRepository;
import pl.taskyers.taskybase.core.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    Optional<UserEntity> findByEmail(String email);
    
}
