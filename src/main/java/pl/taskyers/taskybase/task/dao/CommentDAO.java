package pl.taskyers.taskybase.task.dao;

import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.task.entity.CommentEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.Optional;

/**
 * Interface for database operations on comment entity
 *
 * @author Jakub Sildatk
 */
public interface CommentDAO {
    
    /**
     * Add new comment to the database
     *
     * @param taskEntity task where comment will be added
     * @param userEntity author
     * @param content    comment's content
     * @return saved comment as entity
     * @since 0.0.7
     */
    CommentEntity createComment(TaskEntity taskEntity, UserEntity userEntity, String content);
    
    /**
     * Update comment content
     *
     * @param id      comment's id
     * @param content new content
     * @return updated comment as entity
     * @since 0.0.7
     */
    CommentEntity editComment(Long id, String content);
    
    /**
     * Delete comment by id
     *
     * @param id comment's id
     * @since 0.0.7
     */
    void deleteComment(Long id);
    
    /**
     * Get comment by id
     *
     * @param commentId id
     * @return comment entity as optional
     * @since 0.0.7
     */
    Optional<CommentEntity> getCommentById(Long commentId);
    
}
