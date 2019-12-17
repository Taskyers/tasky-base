package pl.taskyers.taskybase.integration.task;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.task.dto.TaskDTO;
import pl.taskyers.taskybase.task.entity.TaskEntity;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AddingTaskIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = "enabled")
    @Transactional
    public void givenUserWithNoProjectsWhenGettingProjectsWithUserShouldReturnEmptyList() throws Exception {
        final UserEntity userEntity = userRepository.findByUsername("enabled").get();
        final int projectSize = projectRepository.findAllByUsersContaining(Sets.newHashSet(userEntity)).size();
        
        mockMvc.perform(get("/secure/tasks/create"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(empty())))
                .andExpect(jsonPath("$.length()", is(projectSize)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithProjectsWhenGettingProjectsWithUserShouldReturnOnlyProjectNames() throws Exception {
        final UserEntity userEntity = userRepository.findByUsername(DEFAULT_USERNAME).get();
        final int projectSize = projectRepository.findAllByUsersContaining(Sets.newHashSet(userEntity)).size();
        
        mockMvc.perform(get("/secure/tasks/create"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(projectSize)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenNotExistingProjectNameWhenGettingTaskEntryDataShouldReturnStatus404() throws Exception {
        final String name = "notExisting";
        
        mockMvc.perform(get("/secure/tasks/create/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
        
        assertFalse(projectRepository.findByName(name).isPresent());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserNotInProjectWhenGettingTaskEntryDataShouldReturnStatus403() throws Exception {
        final String name = "test1";
        
        mockMvc.perform(get("/secure/tasks/create/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
        
        assertTrue(projectRepository.findByName(name).isPresent());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProjectNameWhenGettingTaskEntryDataShouldReturnStatus200() throws Exception {
        final String name = "test12";
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final int entryTypeSize = entryEntityRepository.findAllByProjectAndEntryType(projectEntity, EntryType.TYPE).size();
        final int entryStatusSize = entryEntityRepository.findAllByProjectAndEntryType(projectEntity, EntryType.STATUS).size();
        final int entryPrioritySize = entryEntityRepository.findAllByProjectAndEntryType(projectEntity, EntryType.PRIORITY).size();
        final int sprintSize = sprintRepository.findAllByProject(projectEntity).size();
        
        mockMvc.perform(get("/secure/tasks/create/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(jsonPath("$.types.length()", is(entryTypeSize)))
                .andExpect(jsonPath("$.priorities.length()", is(entryPrioritySize)))
                .andExpect(jsonPath("$.statuses.length()", is(entryStatusSize)))
                .andExpect(jsonPath("$.sprints.length()", is(sprintSize)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenEmptyDTOWhenCreatingTaskShouldReturnStatus400() throws Exception {
        final int sizeBefore = taskRepository.findAll().size();
        final String content = objectMapper.writeValueAsString(new TaskDTO());
        
        mockMvc.perform(post("/secure/tasks/create/test12").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(jsonPath("$[0].message", is("Name cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[1].message", is("Type cannot be empty")))
                .andExpect(jsonPath("$[2].message", is("Status cannot be empty")))
                .andExpect(jsonPath("$[3].message", is("Priority cannot be empty")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int sizeAfter = taskRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenExistingTaskNameWhenCreatingTaskShouldReturnStatus400() throws Exception {
        final int sizeBefore = taskRepository.findAll().size();
        final String name = "Testing tasks1";
        final String projectName = "test12";
        TaskDTO dto = new TaskDTO(name, "sadf", "asdf", "asdf", "asdf", "1.0", "asdf");
        final String content = objectMapper.writeValueAsString(dto);
        
        mockMvc.perform(post("/secure/tasks/create/" + projectName).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Task with name " + dto.getName() + " in " + projectName + " project already exists")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int sizeAfter = taskRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenValidTaskWhenCreatingTaskShouldReturnStatus201() throws Exception {
        final int sizeBefore = taskRepository.findAll().size();
        final String projectName = "test12";
        final ProjectEntity projectEntity = projectRepository.findByName(projectName).get();
        TaskDTO dto = new TaskDTO("new task", "sadf", "test5", "test4", "test6", "1.0", "current");
        final String content = objectMapper.writeValueAsString(dto);
        
        mockMvc.perform(post("/secure/tasks/create/" + projectName).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Task has been created")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrlPattern("**/{id}"))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isCreated());
        
        final int sizeAfter = taskRepository.findAll().size();
        final TaskEntity taskEntity = taskRepository.findByNameAndProject(dto.getName(), projectEntity).get();
        
        assertNotEquals(sizeBefore, sizeAfter);
        assertEquals(taskEntity.getName(), dto.getName());
        assertNotNull(taskEntity.getKey());
        assertEquals(taskEntity.getCreationDate(), taskEntity.getUpdateDate());
        assertNull(taskEntity.getAssignee());
        assertNull(taskEntity.getResolution());
        assertNotNull(taskEntity.getWatchers());
        assertNotNull(taskEntity.getCreator());
        assertTrue(assertKeyFormat(taskEntity.getKey()));
    }
    
    private boolean assertKeyFormat(String key) {
        return key.matches("[A-Z0-9]+-[0-9]+");
    }
    
}
