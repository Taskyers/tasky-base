package pl.taskyers.taskybase.task.slo;

import org.springframework.http.ResponseEntity;

/**
 * Interface for adding, editing and removing comments
 *
 * @author Jakub Sildatk
 */
public interface CommentsSLO {
    
    String COMMENTS_PREFIX = "/secure/tasks/comments";
    
    String GET_TASK_BY_ID = "/{taskId}";
    
    String GET_COMMENT_BY_ID = "/{commentId}";
    
    /**
     * Add new comment to task
     *
     * @param taskId  task's id
     * @param content comment's content
     * @return status 404 if task was not found, 403 if user is not in the project otherwise 201
     * @since 0.0.7
     */
    ResponseEntity addComment(Long taskId, String content);
    
    /**
     * Edit comment's content
     *
     * @param commentId  comment's id
     * @param newContent new comment's content
     * @return status 404 if comment was not found, 403 if user is no the author of the comment otherwise 200
     * @since 0.0.7
     */
    ResponseEntity editComment(Long commentId, String newContent);
    
    /**
     * Delete comment
     *
     * @param commentId comment's id
     * @return status 404 if comment was not found, 403 if user is not in the project or if user is no the author of the comment otherwise 200
     * @since 0.0.7
     */
    ResponseEntity deleteComment(Long commentId);
    
}
