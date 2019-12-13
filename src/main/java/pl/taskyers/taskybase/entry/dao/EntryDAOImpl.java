package pl.taskyers.taskybase.entry.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.entry.repository.EntryEntityRepository;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

@Service("statusEntrySLO")
@AllArgsConstructor
@Slf4j
public class EntryDAOImpl implements EntryDAO {
    
    private final EntryEntityRepository entryEntityRepository;
    
    @Override
    public EntryEntity addNewEntry(EntryEntity entry) {
        return entryEntityRepository.save(entry);
    }
    
    @Override
    public EntryEntity updateEntry(Long id, EntryEntity entry) {
        entry.setId(id);
        return entryEntityRepository.save(entry);
    }
    
    @Override
    public void deleteEntry(Long id) {
        log.debug("Trying to remove {} entry with id = {}", entryEntityRepository.findById(id).get().getEntryType(), id);
        entryEntityRepository.deleteById(id);
    }
    
    @Override
    public List<EntryEntity> getAllByProjectAndEntryType(ProjectEntity projectEntity, EntryType entryType) {
        return entryEntityRepository.findAllByProjectAndEntryType(projectEntity, entryType);
    }
    
    @Override
    public Optional<EntryEntity> getEntryByEntryTypeAndValueAndProject(EntryType entryType, String value, ProjectEntity projectEntity) {
        return entryEntityRepository.findByEntryTypeAndValueAndProject(entryType, value, projectEntity);
    }
    
    @Override
    public Optional<EntryEntity> getEntryById(Long id) {
        return entryEntityRepository.findById(id);
    }
    
}
