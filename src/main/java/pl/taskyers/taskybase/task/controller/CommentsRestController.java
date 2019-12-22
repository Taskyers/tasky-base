package pl.taskyers.taskybase.task.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.taskyers.taskybase.task.slo.CommentsSLO;

import static pl.taskyers.taskybase.task.slo.CommentsSLO.*;

@RestController
@RequestMapping(value = COMMENTS_PREFIX)
@AllArgsConstructor
public class CommentsRestController {
    
    private final CommentsSLO commentsSLO;
    
    @RequestMapping(value = GET_TASK_BY_ID, method = RequestMethod.POST)
    public ResponseEntity createComment(@PathVariable Long taskId, @RequestParam String content) {
        return commentsSLO.addComment(taskId, content);
    }
    
    @RequestMapping(value = GET_COMMENT_BY_ID, method = RequestMethod.PATCH)
    public ResponseEntity updateComment(@PathVariable Long commentId, @RequestParam String content) {
        return commentsSLO.editComment(commentId, content);
    }
    
    @RequestMapping(value = GET_COMMENT_BY_ID, method = RequestMethod.DELETE)
    public ResponseEntity deleteComment(@PathVariable Long commentId) {
        return commentsSLO.deleteComment(commentId);
    }
    
}
