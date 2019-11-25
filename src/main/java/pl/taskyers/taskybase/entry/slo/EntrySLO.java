package pl.taskyers.taskybase.entry.slo;

import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface for operations on all customizable entries
 *
 * @author Jakub Sildatk
 */
public interface EntrySLO {
    
    /**
     * Add new customizable entry
     *
     * @param entry customizable entry
     * @return saved customizable entry
     * @since 0.0.3
     */
    EntryEntity addNewEntry(EntryEntity entry);
    
    /**
     * Update existing customizable entry
     *
     * @param id    id of entry
     * @param entry entry to be changed
     * @return updated entry
     * @since 0.0.3
     */
    EntryEntity updateEntry(Long id, EntryEntity entry);
    
    /**
     * Delete customizable entry by id
     *
     * @param id id of entry
     * @since 0.0.3
     */
    void deleteEntry(Long id);
    
    /**
     * Get all customizable entries of given type assigned to project
     *
     * @param projectEntity project that have entries
     * @param entryType     entry type value
     * @return all entries of type as list
     * @since 0.0.3
     */
    List<EntryEntity> getAllByProjectAndEntryType(ProjectEntity projectEntity, EntryType entryType);
    
    /**
     * Get entry of type by entry type and project
     *
     * @param entryType     entry type value
     * @param projectEntity project
     * @return optional of entry entity
     * @since 0.0.3
     */
    Optional<EntryEntity> getEntryByEntryTypeAndValueAndProject(EntryType entryType, String value, ProjectEntity projectEntity);
    
    /**
     * Get entry of type by id
     *
     * @param id id
     * @return optional of entry entity
     * @since 0.0.3
     */
    Optional<EntryEntity> getEntryById(Long id);
    
}
