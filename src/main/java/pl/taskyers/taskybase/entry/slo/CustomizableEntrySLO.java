package pl.taskyers.taskybase.entry.slo;

import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface for operations on all customizable entries
 *
 * @param <T> type of customizable entry: STATUS, PRIORITY, TYPE
 * @author Jakub Sildatk
 */
public interface CustomizableEntrySLO<T> {
    
    /**
     * Add new customizable entry
     *
     * @param entry customizable entry
     * @return saved customizable entry
     * @since 0.0.3
     */
    T addNewEntry(T entry);
    
    /**
     * Update existing customizable entry
     *
     * @param id    id of entry
     * @param entry entry to be changed
     * @return updated entry
     * @since 0.0.3
     */
    T updateEntry(Long id, T entry);
    
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
     * @return all entries of type as list
     * @since 0.0.3
     */
    List<T> getAllByProject(ProjectEntity projectEntity);
    
    /**
     * Get entry of type by value and project
     *
     * @param value value
     * @param projectEntity project
     * @return optional of entry entity
     * @since 0.0.3
     */
    Optional<T> getEntryByValueAndProject(String value, ProjectEntity projectEntity);
    
    /**
     * Get entry of type by id
     *
     * @param id id
     * @return optional of entry entity
     * @since 0.0.3
     */
    Optional<T> getEntryById(Long id);
    
}
