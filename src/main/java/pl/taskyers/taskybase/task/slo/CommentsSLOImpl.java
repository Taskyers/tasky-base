package pl.taskyers.taskybase.task.slo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.messages.MessageCode;
import pl.taskyers.taskybase.core.messages.MessageType;
import pl.taskyers.taskybase.core.messages.ResponseMessage;
import pl.taskyers.taskybase.core.slo.AuthProvider;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.UriUtils;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.project.dao.ProjectDAO;
import pl.taskyers.taskybase.task.converter.CommentConverter;
import pl.taskyers.taskybase.task.dao.CommentDAO;
import pl.taskyers.taskybase.task.dao.TaskDAO;
import pl.taskyers.taskybase.task.entity.CommentEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentsSLOImpl implements CommentsSLO {
    
    private final AuthProvider authProvider;
    
    private final CommentDAO commentDAO;
    
    private final TaskDAO taskDAO;
    
    private final ProjectDAO projectDAO;
    
    @Override
    public ResponseEntity addComment(Long taskId, String content) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isTaskFound = checkForTask(taskId, userEntity);
        if ( isTaskFound == null ) {
            final TaskEntity taskEntity = taskDAO.getTaskById(taskId).get();
            final CommentEntity savedComment = commentDAO.createComment(taskEntity, userEntity, content);
            return ResponseEntity.created(UriUtils.createURIFromId(savedComment.getId()))
                    .body(new ResponseMessage<>(MessageCode.comment_created.getMessage(), MessageType.SUCCESS,
                            CommentConverter.convertToDTO(savedComment,
                                    UserUtils.getPersonals(userEntity))));
        }
        return isTaskFound;
    }
    
    @Override
    public ResponseEntity editComment(Long commentId, String newContent) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isCommentFound = checkForComment(commentId, userEntity);
        if ( isCommentFound == null ) {
            final CommentEntity updated = commentDAO.editComment(commentId, newContent);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.comment_edited.getMessage(), MessageType.SUCCESS,
                    CommentConverter.convertToDTO(updated, UserUtils.getPersonals(userEntity))));
        }
        return isCommentFound;
    }
    
    @Override
    public ResponseEntity deleteComment(Long commentId) {
        final UserEntity userEntity = authProvider.getUserEntity();
        ResponseEntity isCommentFound = checkForComment(commentId, userEntity);
        if ( isCommentFound == null ) {
            commentDAO.deleteComment(commentId);
            return ResponseEntity.ok(new ResponseMessage<>(MessageCode.comment_deleted.getMessage(), MessageType.SUCCESS));
        }
        return isCommentFound;
    }
    
    private ResponseEntity checkForTask(Long taskId, UserEntity userEntity) {
        if ( !taskDAO.getTaskById(taskId).isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.task_not_found.getMessage("id", taskId), MessageType.WARN));
        } else if ( !projectDAO.getProjectByNameAndUser(taskDAO.getTaskById(taskId).get().getProject().getName(), userEntity)
                .isPresent() ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.project_permission_not_granted.getMessage(), MessageType.ERROR));
        }
        return null;
    }
    
    private ResponseEntity checkForComment(Long commentId, UserEntity userEntity) {
        final Optional<CommentEntity> comment = commentDAO.getCommentById(commentId);
        if ( !comment.isPresent() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage<>(MessageCode.comment_not_found.getMessage("id", commentId), MessageType.WARN));
        } else if ( !comment.get().getAuthor().equals(userEntity) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseMessage<>(MessageCode.comment_not_yours.getMessage("id", commentId), MessageType.ERROR));
        }
        return null;
    }
    
}
