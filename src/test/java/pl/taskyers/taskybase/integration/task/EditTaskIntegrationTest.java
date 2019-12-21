package pl.taskyers.taskybase.integration.task;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.task.ResolutionType;
import pl.taskyers.taskybase.task.dto.UpdateTaskData;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import javax.transaction.Transactional;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingValueWhenUpdatingStatusEntryShouldReturnStatus404() throws Exception {
        final long id = 5L;
        final String value = "asdsadfdsf";
        
        mockMvc.perform(
                patch("/secure/tasks/edit/status/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Entry " + value + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenSameEntryWhenUpdatingStatusEntryShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final String value = "test5";
        
        mockMvc.perform(patch("/secure/tasks/edit/status/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("STATUS has been updated in task")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(taskEntity.getStatus().getValue(), value);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenSameEntryWhenUpdatingTypeEntryShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final String value = "test6";
        
        mockMvc.perform(patch("/secure/tasks/edit/type/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("TYPE has been updated in task")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(taskEntity.getType().getValue(), value);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenSameEntryWhenUpdatingPriorityEntryShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final String value = "test4";
        
        mockMvc.perform(patch("/secure/tasks/edit/priority/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("PRIORITY has been updated in task")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(taskEntity.getPriority().getValue(), value);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWhenUpdatingStatusEntryShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final String value = "test55";
        
        mockMvc.perform(patch("/secure/tasks/edit/status/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("STATUS has been updated in task")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(taskEntity.getStatus().getValue(), value);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWhenUpdatingTypeEntryShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final String value = "test66";
        
        mockMvc.perform(patch("/secure/tasks/edit/type/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("TYPE has been updated in task")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(taskEntity.getType().getValue(), value);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEntryWhenUpdatingPriorityEntryShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final String value = "test44";
        
        mockMvc.perform(patch("/secure/tasks/edit/priority/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("PRIORITY has been updated in task")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(taskEntity.getPriority().getValue(), value);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingSprintNameWhenEditingTaskShouldReturnStatus404() throws Exception {
        final long id = 5L;
        final String name = "asdffg";
        final String content = objectMapper.writeValueAsString(new UpdateTaskData(null, null, null, name, null));
        
        mockMvc.perform(put("/secure/tasks/edit/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Sprint with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNullNameWhenEditingTaskShouldReturnStatus400() throws Exception {
        final long id = 5L;
        final String content = objectMapper.writeValueAsString(new UpdateTaskData(null, null, null, "test", null));
        
        mockMvc.perform(put("/secure/tasks/edit/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message", is("Name cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenSameUpdateDataWhenEditingTaskShouldReturnStatus200() throws Exception {
        final Long id = 5L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final String sprintNameBefore = taskEntity.getSprint().getName();
        final String nameBefore = taskEntity.getName();
        final String descriptionBefore = taskEntity.getDescription();
        final String fixVersionBefore = taskEntity.getFixVersion();
        final ResolutionType resolutionTypeBefore = taskEntity.getResolution();
        final Date dateBefore = taskEntity.getUpdateDate();
        final String content = objectMapper.writeValueAsString(
                new UpdateTaskData(nameBefore, descriptionBefore, fixVersionBefore, sprintNameBefore,
                        resolutionTypeBefore == null ? "" : resolutionTypeBefore.toString()));
        
        mockMvc.perform(put("/secure/tasks/edit/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Task has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntityAfter = taskRepository.findById(id).get();
        
        assertEquals(taskEntityAfter.getId(), id);
        assertEquals(taskEntityAfter.getResolution(), resolutionTypeBefore);
        assertEquals(taskEntityAfter.getName(), nameBefore);
        assertEquals(taskEntityAfter.getDescription(), descriptionBefore);
        assertEquals(taskEntityAfter.getSprint().getName(), sprintNameBefore);
        assertEquals(taskEntityAfter.getFixVersion(), fixVersionBefore);
        assertNotEquals(taskEntityAfter.getUpdateDate(), dateBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidUpdateDataWhenEditingTaskShouldReturnStatus200() throws Exception {
        final Long id = 5L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final String sprintNameBefore = taskEntity.getSprint().getName();
        final String nameBefore = taskEntity.getName();
        final String descriptionBefore = taskEntity.getDescription();
        final String fixVersionBefore = taskEntity.getFixVersion();
        final ResolutionType resolutionTypeBefore = taskEntity.getResolution();
        final Date dateBefore = taskEntity.getUpdateDate();
        final String content = objectMapper.writeValueAsString(
                new UpdateTaskData("new name", "new Description", "new fix version", "current", ResolutionType.FIXED.getValue()));
        
        mockMvc.perform(put("/secure/tasks/edit/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Task has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntityAfter = taskRepository.findById(id).get();
        
        assertEquals(taskEntityAfter.getId(), id);
        assertNotEquals(taskEntityAfter.getName(), nameBefore);
        assertNotEquals(taskEntityAfter.getDescription(), descriptionBefore);
        assertNotEquals(taskEntityAfter.getResolution(), resolutionTypeBefore);
        assertNotEquals(taskEntityAfter.getSprint().getName(), sprintNameBefore);
        assertNotEquals(taskEntityAfter.getFixVersion(), fixVersionBefore);
        assertNotEquals(taskEntityAfter.getUpdateDate(), dateBefore);
    }
    
}
