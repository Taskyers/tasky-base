package pl.taskyers.taskybase.entry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

public interface EntryEntityRepository extends JpaRepository<EntryEntity, Long> {
    
    Optional<EntryEntity> findByEntryTypeAndValueAndProject(EntryType entryType, String value, ProjectEntity project);
    
    List<EntryEntity> findAllByProjectAndEntryType(ProjectEntity project, EntryType entryType);
    
}
