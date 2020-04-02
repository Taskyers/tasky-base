package pl.taskyers.taskybase.task.converter;

import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.task.dto.CommentDTO;
import pl.taskyers.taskybase.task.entity.CommentEntity;

public class CommentConverter {
    
    private CommentConverter() {
    }
    
    public static CommentDTO convertToDTO(CommentEntity commentEntity, String personals) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setAuthor(UserUtils.getPersonals(commentEntity.getAuthor()));
        commentDTO.setContent(commentEntity.getContent());
        commentDTO.setCreationDate(DateUtils.parseStringDatetime(commentEntity.getCreationDate()));
        commentDTO.setEdited(commentEntity.isEdited());
        commentDTO.setYours(commentDTO.getAuthor()
                .equals(personals));
        return commentDTO;
    }
    
}
