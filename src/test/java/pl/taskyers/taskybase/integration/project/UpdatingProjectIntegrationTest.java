package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.dto.ProjectDTO;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import javax.transaction.Transactional;

import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UpdatingProjectIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectIdWhenUpdatingProjectShouldReturnStatus404() throws Exception {
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO("tessttt", "asdasd"));
        long id = 453L;
        mockMvc.perform(put("/secure/projects/settings/" + id).contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project with id " + id + " was not found")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(value = "userWithNoPermissionToUpdateProject")
    @Transactional
    public void givenUserWithoutPermissionToEditProjectShouldReturnStatus403() throws Exception {
        long id = 1L;
        int sizeBefore = projectRepository.findAll().size();
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO("tessttt", "asdasd"));
        
        mockMvc.perform(put("/secure/projects/settings/" + id).contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have no permission for requested operation")))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isForbidden());
        
        int sizeAfter = projectRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenProjectWithDuplicatedNameWhenUpdatingProjectShouldReturnStatus400() throws Exception {
        long id = 1L;
        long duplicatedId = 2L;
        int sizeBefore = projectRepository.findAll().size();
        ProjectEntity projectEntityBefore = projectRepository.findById(id).get();
        String duplicatedName = projectRepository.findById(duplicatedId).get().getName();
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO(duplicatedName, "asdasd"));
        
        mockMvc.perform(put("/secure/projects/settings/" + id).contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Project with name " + duplicatedName + " already exists")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        int sizeAfter = projectRepository.findAll().size();
        ProjectEntity projectEntityAfter = projectRepository.findById(id).get();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(projectEntityBefore.getName(), projectEntityAfter.getName());
        assertEquals(projectEntityBefore.getDescription(), projectEntityAfter.getDescription());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenEmptyProjectNameWhenUpdatingProjectShouldReturnStatus400() throws Exception {
        long id = 1L;
        int sizeBefore = projectRepository.findAll().size();
        ProjectEntity projectEntityBefore = projectRepository.findById(id).get();
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO("", "asdasd"));
        
        mockMvc.perform(put("/secure/projects/settings/" + id).contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", is("Name cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
        
        int sizeAfter = projectRepository.findAll().size();
        ProjectEntity projectEntityAfter = projectRepository.findById(id).get();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(projectEntityBefore.getName(), projectEntityAfter.getName());
        assertEquals(projectEntityBefore.getDescription(), projectEntityAfter.getDescription());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidProjectWhenUpdatingProjectShouldReturnStatus200() throws Exception {
        Long id = 1L;
        final String newName = "uniquedNames";
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO(newName, "asdasd"));
        
        int sizeBefore = projectRepository.findAll().size();
        ProjectEntity projectEntityBefore = projectRepository.findById(id).get();
        String nameBefore = projectEntityBefore.getName();
        String descriptionBefore = projectEntityBefore.getDescription();
        Date creationDateBefore = projectEntityBefore.getCreationDate();
        Long ownerIdBefore = projectEntityBefore.getOwner().getId();
        int usersSizeBefore = projectEntityBefore.getUsers().size();
        
        mockMvc.perform(put("/secure/projects/settings/" + id).contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        int sizeAfter = projectRepository.findAll().size();
        ProjectEntity projectEntityAfter = projectRepository.findByName(newName).get();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(id, projectEntityAfter.getId());
        assertEquals(creationDateBefore, projectEntityAfter.getCreationDate());
        assertEquals(ownerIdBefore, projectEntityAfter.getOwner().getId());
        assertEquals(usersSizeBefore, projectEntityAfter.getUsers().size());
        assertNotEquals(nameBefore, projectEntityAfter.getName());
        assertNotEquals(descriptionBefore, projectEntityAfter.getDescription());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenValidProjectWithoutDescriptionWhenUpdatingProjectShouldReturnStatus200() throws Exception {
        Long id = 1L;
        final String newName = "uniquedNames1";
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO(newName, ""));
        
        int sizeBefore = projectRepository.findAll().size();
        ProjectEntity projectEntityBefore = projectRepository.findById(id).get();
        String nameBefore = projectEntityBefore.getName();
        String descriptionBefore = projectEntityBefore.getDescription();
        Date creationDateBefore = projectEntityBefore.getCreationDate();
        Long ownerIdBefore = projectEntityBefore.getOwner().getId();
        int usersSizeBefore = projectEntityBefore.getUsers().size();
        
        mockMvc.perform(put("/secure/projects/settings/" + id).contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        int sizeAfter = projectRepository.findAll().size();
        ProjectEntity projectEntityAfter = projectRepository.findByName(newName).get();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(id, projectEntityAfter.getId());
        assertEquals(creationDateBefore, projectEntityAfter.getCreationDate());
        assertEquals(ownerIdBefore, projectEntityAfter.getOwner().getId());
        assertEquals(usersSizeBefore, projectEntityAfter.getUsers().size());
        assertNotEquals(nameBefore, projectEntityAfter.getName());
        assertNotEquals(descriptionBefore, projectEntityAfter.getDescription());
        assertThat(projectEntityAfter.getDescription(), isEmptyString());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    @Transactional
    public void givenSameProjectWhenUpdatingProjectShouldReturnStatus200() throws Exception {
        Long id = 12L;
        final String newName = "toBeUpdated";
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO(newName, "xd"));
        
        int sizeBefore = projectRepository.findAll().size();
        ProjectEntity projectEntityBefore = projectRepository.findById(id).get();
        String nameBefore = projectEntityBefore.getName();
        String descriptionBefore = projectEntityBefore.getDescription();
        Date creationDateBefore = projectEntityBefore.getCreationDate();
        Long ownerIdBefore = projectEntityBefore.getOwner().getId();
        int usersSizeBefore = projectEntityBefore.getUsers().size();
        
        mockMvc.perform(put("/secure/projects/settings/" + id).contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project has been updated")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        int sizeAfter = projectRepository.findAll().size();
        ProjectEntity projectEntityAfter = projectRepository.findByName(newName).get();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(id, projectEntityAfter.getId());
        assertEquals(creationDateBefore, projectEntityAfter.getCreationDate());
        assertEquals(ownerIdBefore, projectEntityAfter.getOwner().getId());
        assertEquals(usersSizeBefore, projectEntityAfter.getUsers().size());
        assertEquals(nameBefore, projectEntityAfter.getName());
        assertEquals(descriptionBefore, projectEntityAfter.getDescription());
    }
    
}
