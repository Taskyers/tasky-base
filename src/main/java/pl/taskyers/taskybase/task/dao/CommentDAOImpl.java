package pl.taskyers.taskybase.task.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.task.entity.CommentEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;
import pl.taskyers.taskybase.task.repository.CommentRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CommentDAOImpl implements CommentDAO {
    
    private final CommentRepository commentRepository;
    
    @Override
    public CommentEntity createComment(TaskEntity taskEntity, UserEntity userEntity, String content) {
        log.debug("Saving new comment in task {}", taskEntity.getKey());
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(userEntity);
        commentEntity.setTask(taskEntity);
        commentEntity.setContent(content);
        commentEntity.setCreationDate(DateUtils.getCurrentTimestamp());
        return commentRepository.save(commentEntity);
    }
    
    @Override
    public CommentEntity editComment(Long id, String content) {
        log.debug("Updating comment with id {}", id);
        CommentEntity commentEntity = getCommentById(id).get();
        commentEntity.setContent(content);
        commentEntity.setEdited(true);
        return commentRepository.save(commentEntity);
    }
    
    @Override
    public void deleteComment(Long id) {
        log.debug("Deleting comment with id {}", id);
        commentRepository.deleteById(id);
    }
    
    @Override
    public Optional<CommentEntity> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }
    
}
