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
    public void givenTaskWithNoAssigneeWhenAssignTaskToMeShouldReturnStatus200() throws Exception {
        final long id = 8L;
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
        
        assertNotNull(assigneeAfter);
        assertNotEquals(assigneeBefore, assigneeAfter);
        assertNotEquals(updateDateBefore, updateDateAfter);
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
    public void givenNotExistingTaskIdWhenUnassignFromTaskShouldReturnStatus404() throws Exception {
        final long id = 213;
        
        mockMvc.perform(patch("/secure/tasks/edit/unassignFromMe/" + id))
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
    public void givenUserNotInProjectWhenUnassigningFromTaskShouldReturnStatus403() throws Exception {
        final long id = 1L;
        
        mockMvc.perform(patch("/secure/tasks/edit/unassignFromMe/" + id))
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
    public void givenNotAssignTaskWhenUnassignFromTaskShouldReturnStatus400() throws Exception {
        final long id = 8L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final UserEntity assigneeBefore = taskEntity.getAssignee();
        final Date updateDateBefore = taskEntity.getUpdateDate();
        
        mockMvc.perform(patch("/secure/tasks/edit/unassignFromMe/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You cannot unassign from this task")))
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
    public void givenTaskWithNotValidAssigneeWhenUnssignFromTaskShouldReturnStatus400() throws Exception {
        final long id = 4L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final UserEntity assigneeBefore = taskEntity.getAssignee();
        final Date updateDateBefore = taskEntity.getUpdateDate();
        
        mockMvc.perform(patch("/secure/tasks/edit/unassignFromMe/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You cannot unassign from this task")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final TaskEntity taskEntityAfter = taskRepository.findById(id).get();
        final UserEntity assigneeAfter = taskEntityAfter.getAssignee();
        final Date updateDateAfter = taskEntityAfter.getUpdateDate();
        
        assertNotNull(assigneeAfter);
        assertEquals(assigneeBefore, assigneeAfter);
        assertEquals(updateDateBefore, updateDateAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProperUserWhenUnassignFromTaskShouldReturnStatus200() throws Exception {
        final long id = 7L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final UserEntity assigneeBefore = taskEntity.getAssignee();
        final Date updateDateBefore = taskEntity.getUpdateDate();
        
        mockMvc.perform(patch("/secure/tasks/edit/unassignFromMe/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Task has been unassigned")))
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
    @Transactional
    public void givenUserAlreadyWatchingTaskWhenWatchingTaskShouldReturnStatus400() throws Exception {
        final long id = 4L;
        
        mockMvc.perform(patch("/secure/tasks/edit/watch/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You already watch this task")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEmptyListOfWatchersWhenWatchingTaskShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        mockMvc.perform(patch("/secure/tasks/edit/watch/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have been added to watchers")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        assertTrue(taskEntity.getWatchers().contains(userEntity));
        assertEquals(1, taskEntity.getWatchers().size());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenNotEmptyListOfWatchersWhenWatchingTaskShouldReturnStatus200() throws Exception {
        final long id = 6L;
        final UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final int sizeBefore = taskEntity.getWatchers().size();
        
        mockMvc.perform(patch("/secure/tasks/edit/watch/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have been added to watchers")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        assertTrue(taskEntity.getWatchers().contains(userEntity));
        assertNotEquals(sizeBefore, taskRepository.findById(id).get().getWatchers().size());
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
        final String content = objectMapper.writeValueAsString(new UpdateTaskData(null, null, null, name));
        
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
        final String content = objectMapper.writeValueAsString(new UpdateTaskData(null, null, null, "test"));
        
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
        final Date dateBefore = taskEntity.getUpdateDate();
        final String content = objectMapper.writeValueAsString(
                new UpdateTaskData(nameBefore, descriptionBefore, fixVersionBefore, sprintNameBefore));
        
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
        final Date dateBefore = taskEntity.getUpdateDate();
        final String content = objectMapper.writeValueAsString(
                new UpdateTaskData("new name", "new Description", "new fix version", "current"));
        
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
        assertNotEquals(taskEntityAfter.getSprint().getName(), sprintNameBefore);
        assertNotEquals(taskEntityAfter.getFixVersion(), fixVersionBefore);
        assertNotEquals(taskEntityAfter.getUpdateDate(), dateBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingResolutionValueWhenUpdatingResolutionShouldReturnStatus404() throws Exception {
        final long id = 5L;
        final String value = "tasdasd";
        
        mockMvc.perform(patch("/secure/tasks/edit/resolution/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", value))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Resolution with value " + value + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingResolutionValueWithOneWordWhenUpdatingResolutionShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final ResolutionType resolution = ResolutionType.FIXED;
        
        mockMvc.perform(
                patch("/secure/tasks/edit/resolution/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", resolution.getValue()))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Resolution has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(resolution, taskEntity.getResolution());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingResolutionValueWithMultipleWordsWhenUpdatingResolutionShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final ResolutionType resolution = ResolutionType.NO_ACTION_REQUIRED;
        
        mockMvc.perform(
                patch("/secure/tasks/edit/resolution/" + id).contentType(MediaType.APPLICATION_FORM_URLENCODED).param("value", resolution.getValue()))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Resolution has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        
        assertEquals(resolution, taskEntity.getResolution());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserNotInWatchersWhenRemovingFromWatchersShouldReturnStatus400() throws Exception {
        final long id = 5L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        
        mockMvc.perform(patch("/secure/tasks/edit/stopWatching/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You are not watching this task")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        assertFalse(taskEntity.getWatchers().contains(userEntity));
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserInWatchersWhenRemovingFromWatchersShouldReturnStatus200() throws Exception {
        final long id = 4L;
        final TaskEntity taskEntity = taskRepository.findById(id).get();
        final UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        final int watchersSizeBefore = taskEntity.getWatchers().size();
        
        assertTrue(taskEntity.getWatchers().contains(userEntity));
        
        mockMvc.perform(patch("/secure/tasks/edit/stopWatching/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have stopped watching this task")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        assertFalse(taskEntity.getWatchers().contains(userEntity));
        assertNotEquals(watchersSizeBefore, taskRepository.findById(id).get().getWatchers().size());
    }
    
}
