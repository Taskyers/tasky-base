package pl.taskyers.taskybase.task.dao;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.List;

/**
 * Interface for database operations on task entity
 *
 * @author Jakub Sildatk
 */
public interface TaskDAO {
    
    /**
     * Get all tasks as entities assigned to given user and project
     *
     * @param userEntity    user
     * @param projectEntity project
     * @return all tasks as list
     * @since 0.0.7
     */
    List<TaskEntity> getTasksAssignedToUserInProject(UserEntity userEntity, ProjectEntity projectEntity);
    
}
