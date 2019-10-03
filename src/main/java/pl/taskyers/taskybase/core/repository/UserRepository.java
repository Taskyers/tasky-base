package pl.taskyers.taskybase.core.repository;

import org.springframework.data.repository.CrudRepository;
import pl.taskyers.taskybase.core.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
