package pl.taskyers.taskybase.sprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<SprintEntity, Long> {
    
    Optional<SprintEntity> findByNameAndProject(String name, ProjectEntity project);
    
    List<SprintEntity> findAllByProject(ProjectEntity project);
    
}
