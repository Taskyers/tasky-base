package pl.taskyers.taskybase.core.roles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.taskybase.core.roles.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
}
