package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.core.utils.DateUtils;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.entity.ProjectEntity;
import pl.taskyers.taskybase.sprint.dto.SprintDTO;
import pl.taskyers.taskybase.sprint.entity.SprintEntity;

import javax.transaction.Transactional;

import java.util.Date;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

public class SprintManagementIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameToEntryPointShouldReturnStatus404() throws Exception {
        final String name = "notExisting";
        mockMvc.perform(get("/secure/project/settings/sprints/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutProperRoleToEntryPointShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/project/settings/sprints/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenUserWithProperRoleToEntryPointShouldReturnStatus200WithData() throws Exception {
        final String name = "test1";
        final ProjectEntity projectEntity = projectRepository.findByName(name).get();
        final int size = sprintRepository.findAllByProject(projectEntity).size();
        
        mockMvc.perform(get("/secure/project/settings/sprints/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(size)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].current").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].start").exists())
                .andExpect(jsonPath("$[0].end").exists())
                .andExpect(jsonPath("$[0].project").doesNotExist())
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenExistingIdAndNotActiveSprintWhenGettingDataForSingleSprintShouldReturnStatus200() throws Exception {
        final long id = 1L;
        final SprintEntity sprintEntity = sprintRepository.findById(id).get();
        
        mockMvc.perform(get("/secure/project/settings/sprints/data/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$.id", is(Math.toIntExact(id))))
                .andExpect(jsonPath("$.current", is(false)))
                .andExpect(jsonPath("$.name", is(sprintEntity.getName())))
                .andExpect(jsonPath("$.start", is(DateUtils.parseString(sprintEntity.getStart()))))
                .andExpect(jsonPath("$.end", is(DateUtils.parseString(sprintEntity.getEnd()))))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenExistingIdAndActiveSprintWhenGettingDataForSingleSprintShouldReturnStatus200() throws Exception {
        final long id = 6L;
        final SprintEntity sprintEntity = sprintRepository.findById(id).get();
        
        mockMvc.perform(get("/secure/project/settings/sprints/data/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$.id", is(Math.toIntExact(id))))
                .andExpect(jsonPath("$.current", is(true)))
                .andExpect(jsonPath("$.name", is(sprintEntity.getName())))
                .andExpect(jsonPath("$.start", is(DateUtils.parseString(sprintEntity.getStart()))))
                .andExpect(jsonPath("$.end", is(DateUtils.parseString(sprintEntity.getEnd()))))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenNotExistingProjectNameWhenAddingNewSprintShouldReturnStatus404() throws Exception {
        final String name = "notExisting";
        final int sizeBefore = sprintRepository.findAll().size();
        final String content = objectMapper.writeValueAsString(new SprintDTO());
        
        mockMvc.perform(post("/secure/project/settings/sprints/" + name).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
        
        final int sizeAfter = sprintRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingSprintNameWhenAddingNewSprintShouldReturnStatus400() throws Exception {
        final String projectName = "test1";
        final String sprintName = "test";
        final int sizeBefore = sprintRepository.findAll().size();
        final String content = objectMapper.writeValueAsString(new SprintDTO(sprintName, "2019-11-10", "2019-11-13"));
        
        mockMvc.perform(post("/secure/project/settings/sprints/" + projectName).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Sprint with name " + sprintName + " already exists")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int sizeAfter = sprintRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenInvalidDatesWhenAddingNewSprintShouldReturnStatus400() throws Exception {
        final int sizeBefore = sprintRepository.findAll().size();
        final String content = objectMapper.writeValueAsString(
                new SprintDTO("unique", "2019-11-11", "2019-10-11"));
        
        mockMvc.perform(post("/secure/project/settings/sprints/test1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Start date cannot be after end date")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("start, end")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int sizeAfter = sprintRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEmptySprintWhenAddingNewSprintShouldReturnStatus400() throws Exception {
        final int sizeBefore = sprintRepository.findAll().size();
        final String content = objectMapper.writeValueAsString(new SprintDTO(null, "2019-08-10", "2019-09-23"));
        
        mockMvc.perform(post("/secure/project/settings/sprints/test1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message", is("Name cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int sizeAfter = sprintRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenSprintWithExistingPeriodWhenAddingNewSprintShouldReturnStatus400() throws Exception {
        final int sizeBefore = sprintRepository.findAll().size();
        final SprintDTO dto = new SprintDTO("unique", "2019-11-05", "2019-11-08");
        final String content = objectMapper.writeValueAsString(dto);
        
        mockMvc.perform(post("/secure/project/settings/sprints/test1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message", is("Sprint with period " + dto.getStart() + " - " + dto.getEnd() + " already exists")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("start, end")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        final int sizeAfter = sprintRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidSprintWhenAddingNewSprintShouldReturnStatus201() throws Exception {
        final int sizeBefore = sprintRepository.findAll().size();
        final String projectName = "test1";
        final int sprintsInProjectBefore = sprintRepository.findAllByProject(projectRepository.findByName(projectName).get()).size();
        final String sprintName = "unique";
        final SprintDTO dto = new SprintDTO(sprintName, "2019-05-11", "2019-06-11");
        final String content = objectMapper.writeValueAsString(dto);
        
        mockMvc.perform(post("/secure/project/settings/sprints/" + projectName).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Sprint has been created")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.name", is(sprintName)))
                .andExpect(jsonPath("$.object.start").exists())
                .andExpect(jsonPath("$.object.end").exists())
                .andExpect(jsonPath("$.object.project.name", is(projectName)))
                .andExpect(redirectedUrlPattern("**/sprints/" + projectName + "/{id}"))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isCreated());
        
        final int sizeAfter = sprintRepository.findAll().size();
        final int sprintsInProjectAfter = sprintRepository.findAllByProject(projectRepository.findByName(projectName).get()).size();
        final SprintEntity sprintEntityAfter =
                sprintRepository.findByNameAndProject(sprintName, projectRepository.findByName(projectName).get()).get();
        
        assertNotEquals(sizeAfter, sizeBefore);
        assertNotEquals(sprintsInProjectAfter, sprintsInProjectBefore);
        assertEquals(DateUtils.parseDate(dto.getStart()), sprintEntityAfter.getStart());
        assertEquals(DateUtils.parseDate(dto.getEnd()), sprintEntityAfter.getEnd());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingIdNameWhenUpdatingSprintShouldReturnStatus404() throws Exception {
        final long id = 234L;
        final String content = objectMapper.writeValueAsString(new SprintDTO(null, "2019-11-10", "2019-11-23"));
        
        mockMvc.perform(put("/secure/project/settings/sprints/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Sprint with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutRoleWhenUpdatingSprintShouldReturnStatus403() throws Exception {
        final String content = objectMapper.writeValueAsString(new SprintDTO());
        
        mockMvc.perform(put("/secure/project/settings/sprints/1").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenInvalidSprintWhenUpdatingSprintShouldReturnStatus400() throws Exception {
        final String content = objectMapper.writeValueAsString(new SprintDTO(null, "2019-06-10", "2019-07-23"));
        
        mockMvc.perform(put("/secure/project/settings/sprints/1").contentType(MediaType.APPLICATION_JSON).content(content))
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
    public void givenSameObjectWhenUpdatingSprintShouldReturnStatus200() throws Exception {
        final int sizeBefore = sprintRepository.findAll().size();
        final Long id = 4L;
        final SprintEntity sprintEntityBefore = sprintRepository.findById(id).get();
        final String nameBefore = sprintEntityBefore.getName();
        final Date startBefore = sprintEntityBefore.getStart();
        final Date endBefore = sprintEntityBefore.getEnd();
        final ProjectEntity projectEntityBefore = sprintEntityBefore.getProject();
        final String content =
                objectMapper.writeValueAsString(new SprintDTO(sprintEntityBefore.getName(), DateUtils.parseString(sprintEntityBefore.getStart()),
                        DateUtils.parseString(sprintEntityBefore.getEnd())));
        
        mockMvc.perform(put("/secure/project/settings/sprints/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Sprint has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.id", is(Math.toIntExact(id))))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int sizeAfter = sprintRepository.findAll().size();
        final SprintEntity sprintEntityAfter = sprintRepository.findById(id).get();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(sprintEntityAfter.getId(), id);
        assertEquals(nameBefore, sprintEntityAfter.getName());
        assertEquals(endBefore, sprintEntityAfter.getEnd());
        assertEquals(projectEntityBefore, sprintEntityAfter.getProject());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenValidSprintWhenUpdatingSprintShouldReturnStatus200() throws Exception {
        final int sizeBefore = sprintRepository.findAll().size();
        final Long id = 4L;
        final SprintEntity sprintEntityBefore = sprintRepository.findById(id).get();
        final String nameBefore = sprintEntityBefore.getName();
        final Date startBefore = sprintEntityBefore.getStart();
        final Date endBefore = sprintEntityBefore.getEnd();
        final ProjectEntity projectEntityBefore = sprintEntityBefore.getProject();
        final SprintDTO dto = new SprintDTO("updating", "2019-01-23", "2019-02-25");
        final String content = objectMapper.writeValueAsString(dto);
        
        mockMvc.perform(put("/secure/project/settings/sprints/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Sprint has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.id", is(Math.toIntExact(id))))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int sizeAfter = sprintRepository.findAll().size();
        final SprintEntity sprintEntityAfter = sprintRepository.findById(id).get();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(sprintEntityAfter.getId(), id);
        assertNotEquals(nameBefore, sprintEntityAfter.getName());
        assertNotEquals(startBefore, sprintEntityAfter.getStart());
        assertNotEquals(endBefore, sprintEntityAfter.getEnd());
        assertEquals(projectEntityBefore, sprintEntityAfter.getProject());
        assertEquals(dto.getStart(), DateUtils.parseString(sprintEntityAfter.getStart()));
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingIdWhenDeletingSprintShouldReturnStatus404() throws Exception {
        final long id = 234L;
        
        mockMvc.perform(delete("/secure/project/settings/sprints/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Sprint with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenExistingIdWhenDeletingSprintShouldReturnStatus200() throws Exception {
        final long id = 5L;
        final int sizeBefore = sprintRepository.findAll().size();
        
        assertTrue(sprintRepository.findById(id).isPresent());
        
        mockMvc.perform(delete("/secure/project/settings/sprints/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Sprint has been deleted")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        final int sizeAfter = sprintRepository.findAll().size();
        
        assertFalse(sprintRepository.findById(id).isPresent());
        assertNotEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = "enabled")
    public void givenUserWithoutProperRoleWhenCheckingForNameShouldReturnStatus403() throws Exception {
        mockMvc.perform(get("/secure/project/settings/sprints/test/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectNameWhenCheckingForNameShouldReturnStatus404() throws Exception {
        final String name = "asdasdasd";
        mockMvc.perform(get("/secure/project/settings/sprints/test/" + name))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with name " + name + " was not found")))
                .andExpect(jsonPath("$.type", is("WARN")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenExistingSprintNameInProjectWhenCheckingForNameShouldReturnTrue() throws Exception {
        mockMvc.perform(get("/secure/project/settings/sprints/test/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingSprintNameInProjectWhenCheckingForNameShouldReturnFalse() throws Exception {
        mockMvc.perform(get("/secure/project/settings/sprints/testasdasfds/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}
