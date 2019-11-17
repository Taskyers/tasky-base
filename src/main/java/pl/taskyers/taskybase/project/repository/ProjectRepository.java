package pl.taskyers.taskybase.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    
    Optional<ProjectEntity> findByName(String name);
    
    List<ProjectEntity> findAllByUsersContainingOrderByCreationDateDesc(Set<UserEntity> users, Pageable pageable);
    
    List<ProjectEntity> findAllByUsersContaining(Set<UserEntity> users);
    
    Optional<ProjectEntity> findByNameAndUsers(String name, UserEntity userEntity);
    
}
