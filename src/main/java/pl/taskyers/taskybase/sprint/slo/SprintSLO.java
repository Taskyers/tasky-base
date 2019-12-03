package pl.taskyers.taskybase.sprint.slo;

import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface for operations on sprint entity
 *
 * @author Jakub Sildatk
 */
public interface SprintSLO {
    
    /**
     * Save new sprint to the database
     *
     * @param sprintEntity sprint
     * @return saved sprint entity
     * @since 0.0.3
     */
    SprintEntity addNew(SprintEntity sprintEntity);
    
    /**
     * Update existing sprint
     *
     * @param sprintEntity old sprint with id
     * @return updated sprint entity
     * @since 0.0.3
     */
    SprintEntity update(SprintEntity sprintEntity);
    
    /**
     * Delete sprint
     *
     * @param id sprint id
     * @since 0.0.3
     */
    void delete(Long id);
    
    /**
     * Check if sprint with given name already exists in given project
     *
     * @param name    sprint name
     * @param project project entity
     * @return true if name already exists, otherwise false
     * @since 0.0.3
     */
    boolean doesNameExistsInProject(String name, ProjectEntity project);
    
    /**
     * Get sprint entity by id
     *
     * @param id id
     * @return entity as optional
     * @since 0.0.3
     */
    Optional<SprintEntity> getById(Long id);
    
    /**
     * Get list of all sprints assigned to the project
     *
     * @param projectEntity project
     * @return list of sprint entities
     * @since 0.0.3
     */
    List<SprintEntity> getAllByProject(ProjectEntity projectEntity);
    
}
