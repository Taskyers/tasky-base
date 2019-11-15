package pl.taskyers.taskybase.core.roles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.taskybase.core.roles.entity.RoleEntity;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleLinkerRepository extends JpaRepository<RoleLinkerEntity, Long> {
    
    Optional<RoleLinkerEntity> findByUserAndProjectAndRole(UserEntity user, ProjectEntity project, RoleEntity role);
    
    Optional<RoleLinkerEntity> findByUserAndProjectAndRole_Key(UserEntity userEntity, ProjectEntity projectEntity, String roleKey);
    
    List<RoleLinkerEntity> findAllByUserAndProject(UserEntity userEntity, ProjectEntity projectEntity);
    
}
