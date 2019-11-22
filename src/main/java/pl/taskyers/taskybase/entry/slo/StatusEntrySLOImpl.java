package pl.taskyers.taskybase.entry.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.entry.entity.StatusEntryEntity;
import pl.taskyers.taskybase.entry.repository.StatusEntryRepository;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

@Service("statusEntrySLO")
@AllArgsConstructor
@Slf4j
public class StatusEntrySLOImpl implements CustomizableEntrySLO<StatusEntryEntity> {
    
    private final StatusEntryRepository statusEntryRepository;
    
    @Override
    public StatusEntryEntity addNewEntry(StatusEntryEntity entry) {
        return statusEntryRepository.save(entry);
    }
    
    @Override
    public StatusEntryEntity updateEntry(Long id, StatusEntryEntity entry) {
        entry.setId(id);
        return statusEntryRepository.save(entry);
    }
    
    @Override
    public void deleteEntry(Long id) {
        log.debug("Trying to remove status entry with id = {}", id);
        statusEntryRepository.deleteById(id);
    }
    
    @Override
    public List<StatusEntryEntity> getAllByProject(ProjectEntity projectEntity) {
        return statusEntryRepository.findAllByProject(projectEntity);
    }
    
    @Override
    public Optional<StatusEntryEntity> getEntryByValueAndProject(String value, ProjectEntity projectEntity) {
        return statusEntryRepository.findByValueAndProject(value, projectEntity);
    }
    
    @Override
    public Optional<StatusEntryEntity> getEntryById(Long id) {
        return statusEntryRepository.findById(id);
    }
    
}
