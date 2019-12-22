package pl.taskyers.taskybase.integration.task;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.task.entity.CommentEntity;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommentsIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingTaskIdWhenAddingNewCommentShouldReturnStatus404() throws Exception {
        final long id = 213;
        
        mockMvc.perform(post("/secure/tasks/comments/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("content", "sdd"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Task with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
        
        assertFalse(taskRepository.findById(id).isPresent());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserNotInProjectWhenAddingCommentShouldReturnStatus403() throws Exception {
        final long id = 1L;
        
        mockMvc.perform(post("/secure/tasks/comments/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("content", "sdd"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
        
        assertTrue(taskRepository.findById(id).isPresent());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidCommentWhenAddingCommentShouldReturnStatus201() throws Exception {
        final Long taskId = 5L;
        final String content = "test";
        final String personals = UserUtils.getPersonals(userRepository.findByUsername(DEFAULT_USERNAME).get());
        final int sizeBefore = commentRepository.findAll().size();
        
        mockMvc.perform(post("/secure/tasks/comments/" + taskId).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("content", content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Comment has been created")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(jsonPath("$.object.content", is(content)))
                .andExpect(jsonPath("$.object.author", is(personals)))
                .andExpect(jsonPath("$.object.creationDate", is(DateUtils.parseStringDatetime(DateUtils.getCurrentTimestamp()))))
                .andExpect(jsonPath("$.object.edited", is(false)))
                .andExpect(jsonPath("$.object.yours", is(true)))
                .andExpect(redirectedUrlPattern("**/comments/{taskId}/{commentId}"))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isCreated());
        
        final int sizeAfter = commentRepository.findAll().size();
        
        assertNotEquals(sizeAfter, sizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotYoursCommentIdWhenEditingCommentShouldReturnStatus403() throws Exception {
        final long commentId = 3L;
        
        mockMvc.perform(patch("/secure/tasks/comments/" + commentId).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("content", "sadf"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Comment with id " + commentId + " is not yours")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidCommentWhenEditingCommentShouldReturnStatus200() throws Exception {
        final long id = 4L;
        final CommentEntity commentEntityBefore = commentRepository.findById(id).get();
        final String contentBefore = commentEntityBefore.getContent();
        final UserEntity authorBefore = commentEntityBefore.getAuthor();
        final String newContent = "asdghgh";
        
        mockMvc.perform(patch("/secure/tasks/comments/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("content", newContent))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Comment has been edited")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object.content", is(newContent)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final CommentEntity commentEntityAfter = commentRepository.findById(id).get();
        
        assertEquals(commentEntityAfter.getContent(), newContent);
        assertNotEquals(commentEntityAfter.getContent(), contentBefore);
        assertEquals(commentEntityAfter.getAuthor(), authorBefore);
        assertTrue(commentEntityAfter.isEdited());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingIdWhenDeletingCommentShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final int sizeBefore = commentRepository.findAll().size();
        
        mockMvc.perform(delete("/secure/tasks/comments/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Comment has been deleted")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int sizeAfter = commentRepository.findAll().size();
        
        assertFalse(commentRepository.findById(id).isPresent());
        assertNotEquals(sizeBefore, sizeAfter);
    }
    
}
