package pl.taskyers.taskybase.entry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.taskyers.taskybase.entry.entity.StatusEntryEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

public interface StatusEntryRepository extends JpaRepository<StatusEntryEntity, Long> {
    
    List<StatusEntryEntity> findAllByProject(ProjectEntity project);
    
    Optional<StatusEntryEntity> findByValueAndProject(String value, ProjectEntity project);
    
}
