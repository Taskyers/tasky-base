package pl.taskyers.taskybase.task.dao;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.entry.entity.EntryEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;
import pl.taskyers.taskybase.task.ResolutionType;
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
    
    /**
     * Get task entity by id
     *
     * @param id id
     * @return task entity as optional
     * @since 0.0.7
     */
    Optional<TaskEntity> getTaskById(Long id);
    
    /**
     * Set new assignee to given task
     *
     * @param taskEntity  task
     * @param newAssignee user entity
     * @return updated task entity
     * @since 0.0.7
     */
    TaskEntity setAssignee(TaskEntity taskEntity, UserEntity newAssignee);
    
    /**
     * Add new user to watchers collection to given task
     *
     * @param taskEntity task
     * @param userEntity user
     * @return updated task entity
     * @since 0.0.7
     */
    TaskEntity addWatcher(TaskEntity taskEntity, UserEntity userEntity);
    
    /**
     * Update entry in task
     *
     * @param taskEntity  task
     * @param entryEntity entry
     * @return updated task entity
     * @since 0.0.7
     */
    TaskEntity updateEntry(TaskEntity taskEntity, EntryEntity entryEntity);
    
    /**
     * Update other task data
     *
     * @param taskEntity   task
     * @param name         new task's name
     * @param description  new task's description
     * @param fixVersion   new task's fix version
     * @param sprintEntity new task's sprint
     * @param resolution   new task's resolution type
     * @return updated task entity
     * @since 0.0.7
     */
    TaskEntity updateTask(TaskEntity taskEntity, String name, String description, String fixVersion, SprintEntity sprintEntity,
            ResolutionType resolution);
    
    /**
     * Get all tasks by project, status and sprint
     *
     * @param projectEntity project
     * @param status        status value
     * @param sprintEntity  sprint
     * @return list of tasks entities
     * @since 0.0.7
     */
    List<TaskEntity> getAllTasksByProjectAndStatusAndSprint(ProjectEntity projectEntity, String status, SprintEntity sprintEntity);
    
}
