package pl.taskyers.taskybase.integration.task;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import javax.transaction.Transactional;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EditTaskIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingTaskIdWhenEditingTaskShouldReturnStatus404() throws Exception {
        final long id = 213;
        
        mockMvc.perform(patch("/secure/tasks/edit/assignToMe/" + id))
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
    public void givenUserNotInProjectWhenEditingTaskShouldReturnStatus403() throws Exception {
        final long id = 1L;
        
        mockMvc.perform(patch("/secure/tasks/edit/assignToMe/" + id))
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
    public void givenSameUserWhenAssignTaskToMeShouldReturnStatus400() throws Exception {
        final long id = 5L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final UserEntity assigneeBefore = taskEntity.getAssignee();
        final Date updateDateBefore = taskEntity.getUpdateDate();
        
        mockMvc.perform(patch("/secure/tasks/edit/assignToMe/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You are already assigned to this task")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final TaskEntity taskEntityAfter = taskRepository.findById(id).get();
        final UserEntity assigneeAfter = taskEntityAfter.getAssignee();
        final Date updateDateAfter = taskEntityAfter.getUpdateDate();
        
        assertEquals(taskEntity, taskEntityAfter);
        assertEquals(assigneeBefore, assigneeAfter);
        assertEquals(updateDateBefore, updateDateAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProperUserWhenAssignTaskToMeShouldReturnStatus200() throws Exception {
        final long id = 4L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final UserEntity assigneeBefore = taskEntity.getAssignee();
        final Date updateDateBefore = taskEntity.getUpdateDate();
        
        mockMvc.perform(patch("/secure/tasks/edit/assignToMe/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Task has been assigned")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntityAfter = taskRepository.findById(id).get();
        final UserEntity assigneeAfter = taskEntityAfter.getAssignee();
        final Date updateDateAfter = taskEntityAfter.getUpdateDate();
        
        assertNotEquals(assigneeBefore, assigneeAfter);
        assertNotEquals(updateDateBefore, updateDateAfter);
    }
    
}
