package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.integration.IntegrationBase;
import pl.taskyers.taskybase.project.dto.ProjectDTO;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.taskyers.taskybase.project.dao.ProjectDAO.PROJECTS_LIMIT;

public class AddingNewProjectIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = "enabled")
    @Transactional
    public void givenExistingProjectNameWhenAddingNewProjectShouldReturnStatus400() throws Exception {
        int sizeBefore = projectRepository.findAll().size();
        int roleLinkerSizeBefore = roleLinkerRepository.findAll().size();
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO("test1", "test"));
        
        mockMvc.perform(post("/secure/projects")
                .contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message", is("Project with name test1 already exists")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
        
        int sizeAfter = projectRepository.findAll().size();
        int roleLinkerSizeAfter = roleLinkerRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(roleLinkerSizeBefore, roleLinkerSizeAfter);
    }
    
    @Test
    @WithMockUser(value = "enabled")
    @Transactional
    public void givenProjectWithEmptyNameWhenAddingNewProjectShouldReturnStatus400() throws Exception {
        int sizeBefore = projectRepository.findAll().size();
        int roleLinkerSizeBefore = roleLinkerRepository.findAll().size();
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO("", "test"));
        
        mockMvc.perform(post("/secure/projects")
                .contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].message", is("Name cannot be empty")))
                .andExpect(jsonPath("$[0].type", is("ERROR")))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
        
        int sizeAfter = projectRepository.findAll().size();
        int roleLinkerSizeAfter = roleLinkerRepository.findAll().size();
        
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(roleLinkerSizeBefore, roleLinkerSizeAfter);
    }
    
    @Test
    @WithMockUser(value = "enabled")
    @Transactional
    public void givenValidProjectWhenAddingNewProjectShouldReturnStatus201() throws Exception {
        int sizeBefore = projectRepository.findAll().size();
        int roleSizeBefore = roleRepository.findAll().size();
        int roleLinkerSizeBefore = roleLinkerRepository.findAll().size();
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO("uniqueName", "test"));
        
        mockMvc.perform(post("/secure/projects")
                .contentType(MediaType.APPLICATION_JSON).content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project has been created")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object.id", is(notNullValue())))
                .andExpect(jsonPath("$.object.owner", is(notNullValue())))
                .andExpect(jsonPath("$.object.owner.username", is("enabled")))
                .andExpect(jsonPath("$.object.users", is(notNullValue())))
                .andExpect(jsonPath("$.object.users.length()", is(notNullValue())))
                .andExpect(jsonPath("$.object.name", is("uniqueName")))
                .andExpect(jsonPath("$.object.description", is("test")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrlPattern("**/projects/{id}"))
                .andExpect(header().exists("Location"))
                .andExpect(status().isCreated());
        
        int sizeAfter = projectRepository.findAll().size();
        int roleLinkerSizeAfter = roleLinkerRepository.findAll().size();
        
        assertNotEquals(sizeBefore, sizeAfter);
        assertEquals(roleLinkerSizeAfter, roleLinkerSizeBefore + roleSizeBefore);
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectWhenCheckingForProjectByNameShouldReturnFalse() throws Exception {
        mockMvc.perform(get("/secure/projects/uniqueProjectName"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenExistingProjectWhenCheckingForProjectByNameShouldReturnTrue() throws Exception {
        mockMvc.perform(get("/secure/projects/test1"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(value = "userWith5Projects")
    @Transactional
    public void givenUserWith5ProjectsWhenAddingNewProjectShouldReturnStatus400() throws Exception {
        int sizeBefore = projectRepository.findAll().size();
        int roleLinkerSizeBefore = roleLinkerRepository.findAll().size();
        final String message = "You cannot add new project! Limit is " + PROJECTS_LIMIT;
        String projectJSON = objectMapper.writeValueAsString(new ProjectDTO("dfghdfg", "test"));
    
        mockMvc.perform(post("/secure/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.type", is("ERROR")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    
        int sizeAfter = projectRepository.findAll().size();
        int roleLinkerSizeAfter = roleLinkerRepository.findAll().size();
    
        assertEquals(sizeBefore, sizeAfter);
        assertEquals(roleLinkerSizeBefore, roleLinkerSizeAfter);
    }
    
}
