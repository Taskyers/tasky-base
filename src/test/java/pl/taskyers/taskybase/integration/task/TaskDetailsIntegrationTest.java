package pl.taskyers.taskybase.integration.task;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.core.utils.UserUtils;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskDetailsIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingKeyWhenGettingTaskDetailsShouldReturnStatus404() throws Exception {
        final String key = "notExisting";
        
        mockMvc.perform(get("/secure/tasks/" + key))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Task with key " + key + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserNotInProjectWhenGettingTaskDetailsShouldReturnStatus403() throws Exception {
        final String key = "PROJECT-1";
        
        mockMvc.perform(get("/secure/tasks/" + key))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
        
        assertTrue(taskRepository.findByKey(key).isPresent());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProperUserWhenGettingTaskDetailsShouldReturnStatus200() throws Exception {
        final TaskEntity taskEntity = taskRepository.findByKey("PROJECT-4").get();
        
        mockMvc.perform(get("/secure/tasks/" + taskEntity.getKey()))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(Math.toIntExact(taskEntity.getId()))))
                .andExpect(jsonPath("$.key", is(taskEntity.getKey())))
                .andExpect(jsonPath("$.name", is(taskEntity.getName())))
                .andExpect(jsonPath("$.description", is(taskEntity.getDescription())))
                .andExpect(jsonPath("$.assignee", is(UserUtils.getPersonals(taskEntity.getAssignee()))))
                .andExpect(jsonPath("$.creator", is(UserUtils.getPersonals(taskEntity.getCreator()))))
                .andExpect(jsonPath("$.fixVersion", is(taskEntity.getFixVersion())))
                .andExpect(jsonPath("$.creationDate", is(DateUtils.parseStringDatetime(taskEntity.getCreationDate()))))
                .andExpect(jsonPath("$.updateDate", is(DateUtils.parseStringDatetime(taskEntity.getUpdateDate()))))
                .andExpect(jsonPath("$.resolution", is(taskEntity.getResolution())))
                .andExpect(jsonPath("$.status.value", is(taskEntity.getStatus().getValue())))
                .andExpect(jsonPath("$.status.textColor", is(taskEntity.getStatus().getTextColor())))
                .andExpect(jsonPath("$.status.backgroundColor", is(taskEntity.getStatus().getBackgroundColor())))
                .andExpect(jsonPath("$.type.value", is(taskEntity.getType().getValue())))
                .andExpect(jsonPath("$.type.textColor", is(taskEntity.getType().getTextColor())))
                .andExpect(jsonPath("$.type.backgroundColor", is(taskEntity.getType().getBackgroundColor())))
                .andExpect(jsonPath("$.priority.value", is(taskEntity.getPriority().getValue())))
                .andExpect(jsonPath("$.priority.textColor", is(taskEntity.getPriority().getTextColor())))
                .andExpect(jsonPath("$.priority.backgroundColor", is(taskEntity.getPriority().getBackgroundColor())))
                .andExpect(jsonPath("$.sprint.name", is(taskEntity.getSprint().getName())))
                .andExpect(jsonPath("$.sprint.current",
                        is(DateUtils.checkIfDateBetweenTwoDates(DateUtils.getCurrentDate(), taskEntity.getSprint().getStart(),
                                taskEntity.getSprint().getEnd()))))
                .andExpect(jsonPath("$.watchers.length()", is(taskEntity.getWatchers().size())))
                .andExpect(jsonPath("$.comments.length()", is(taskEntity.getComments().size())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProperUserWhenGettingStatusesShouldReturnStatus200() throws Exception {
        final ProjectEntity projectEntity = projectRepository.findById(2L).get();
        final int size = entryEntityRepository.findAllByProjectAndEntryType(projectEntity, EntryType.STATUS).size();
        
        mockMvc.perform(get("/secure/tasks/PROJECT-4/statuses"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(size)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProperUserWhenGettingTypesShouldReturnStatus200() throws Exception {
        final ProjectEntity projectEntity = projectRepository.findById(2L).get();
        final int size = entryEntityRepository.findAllByProjectAndEntryType(projectEntity, EntryType.TYPE).size();
        
        mockMvc.perform(get("/secure/tasks/PROJECT-4/types"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(size)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProperUserWhenGettingPrioritiesShouldReturnStatus200() throws Exception {
        final ProjectEntity projectEntity = projectRepository.findById(2L).get();
        final int size = entryEntityRepository.findAllByProjectAndEntryType(projectEntity, EntryType.PRIORITY).size();
        
        mockMvc.perform(get("/secure/tasks/PROJECT-4/priorities"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(size)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
}
