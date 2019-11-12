package pl.taskyers.taskybase.project.repository;

import org.springframework.data.repository.CrudRepository;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.project.entity.ProjectInvitationTokenEntity;

import java.util.Optional;

public interface ProjectInvitationTokenRepository extends CrudRepository<ProjectInvitationTokenEntity, Long> {
    
    Optional<ProjectInvitationTokenEntity> findByUser(UserEntity userEntity);
    
    Optional<ProjectInvitationTokenEntity> findByToken(String token);
    
    Optional<ProjectInvitationTokenEntity> findByProjectAndUser(ProjectEntity projectEntity, UserEntity userEntity);
    
}
