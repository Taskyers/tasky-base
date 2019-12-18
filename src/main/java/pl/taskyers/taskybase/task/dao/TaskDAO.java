package pl.taskyers.taskybase.task.dao;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

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
    
    /**
     * Get task entity as optional by name and project
     *
     * @param name          name
     * @param projectEntity project
     * @return task entity as optional
     * @since 0.0.7
     */
    Optional<TaskEntity> getTaskByNameAndProject(String name, ProjectEntity projectEntity);
    
    /**
     * Get task entity as optional by key and project
     *
     * @param key           key
     * @param projectEntity project
     * @return task entity as optional
     * @since 0.0.7
     */
    Optional<TaskEntity> getTaskByKey(String key, ProjectEntity projectEntity);
    
    /**
     * Save new task to database - currently logged user will be creator and watchers collection will only contain creator
     *
     * @param taskEntity task containing fix version, name and description
     * @param userEntity user that will be creator
     * @param type       EntryType.TYPE as String
     * @param status     EntryType.STATUS as String
     * @param priority   EntryType.PRIORITY as String
     * @param sprint     sprint name
     * @return saved task
     * @since 0.0.7
     */
    TaskEntity createTask(TaskEntity taskEntity, UserEntity userEntity, String type, String status, String priority, String sprint);
    
    /**
     * Get task entity only by key
     *
     * @param key key
     * @return task entity as optional
     * @since 0.0.7
     */
    Optional<TaskEntity> getTaskByKey(String key);
    
}
