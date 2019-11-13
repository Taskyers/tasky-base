package pl.taskyers.taskybase.integration.project;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.integration.IntegrationBase;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DeletingProjectIntegrationTest extends IntegrationBase {
    
    @Test
    @WithMockUser(value = DEFAULT_USERNAME)
    public void givenNotExistingProjectIdWhenDeletingProjectShouldReturnStatus404() throws Exception {
        long id = 453L;
        mockMvc.perform(delete("/secure/projects/settings/" + id))
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
    @WithMockUser(value = "userWithNoPermissionToDeleteProject")
    @Transactional
    public void givenUserWithoutPermissionToDeleteProjectShouldReturnStatus403() throws Exception {
        long id = 1L;
        int sizeBefore = projectRepository.findAll().size();
        
        mockMvc.perform(delete("/secure/projects/settings/" + id))
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
    public void givenExistingIdWhenDeletingProjectShouldReturnStatus200() throws Exception {
        long id = 10L;
        int projectsSizeBefore = projectRepository.findAll().size();
        int roleLinkersSizeBefore = roleLinkerRepository.findAll().size();
        
        mockMvc.perform(delete("/secure/projects/settings/" + id))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Project has been deleted")))
                .andExpect(jsonPath("$.type", is("SUCCESS")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isOk());
        
        int projectsSizeAfter = projectRepository.findAll().size();
        int roleLinkersSizeAfter = roleLinkerRepository.findAll().size();
        
        assertNotEquals(projectsSizeBefore, projectsSizeAfter);
        assertNotEquals(roleLinkersSizeBefore, roleLinkersSizeAfter);
        assertEquals(projectsSizeBefore, projectsSizeAfter + 1);
        assertFalse(projectRepository.findById(id).isPresent());
    }
    
}
