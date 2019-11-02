package pl.taskyers.taskybase.integration.dashboard.main;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import pl.taskyers.taskybase.integration.IntegrationBase;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MainDashboardIntegrationTest extends IntegrationBase {
    
    private final static String USER_WITHOUT_PROJECTS = "userWithoutProjects";
    
    private final static String USER_WITH_4_PROJECTS = "userWith4Projects";
    
    private final static String USER_WITH_5_PROJECTS = "userWith5Projects";
    
    private final static String USER_WITH_6_PROJECTS = "userWith6Projects";
    
    @Test
    @WithMockUser(value = USER_WITHOUT_PROJECTS)
    @Transactional
    public void givenUserWithoutProjectsShouldReturnEmptySetWithStatus200() throws Exception {
        mockMvc.perform(get("/secure/mainDashboard/projects"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(empty())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        assertEquals(0, userRepository.findByUsername(USER_WITHOUT_PROJECTS).get().getProjects().size());
    }
    
    @Test
    @WithMockUser(value = USER_WITH_4_PROJECTS)
    @Transactional
    public void givenUserWith4ProjectsShouldReturn4ProjectsWithStatus200() throws Exception {
        mockMvc.perform(get("/secure/mainDashboard/projects"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        assertEquals(4, userRepository.findByUsername(USER_WITH_4_PROJECTS).get().getProjects().size());
    }
    
    @Test
    @WithMockUser(value = USER_WITH_5_PROJECTS)
    @Transactional
    public void givenUserWith5ProjectsShouldReturn5ProjectsWithStatus200() throws Exception {
        mockMvc.perform(get("/secure/mainDashboard/projects"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        assertEquals(5, userRepository.findByUsername(USER_WITH_5_PROJECTS).get().getProjects().size());
    }
    
    @Test
    @WithMockUser(value = USER_WITH_6_PROJECTS)
    @Transactional
    public void givenUserWith6ProjectsShouldReturn5ProjectsWithStatus200() throws Exception {
        mockMvc.perform(get("/secure/mainDashboard/projects"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
        
        assertEquals(6, userRepository.findByUsername(USER_WITH_6_PROJECTS).get().getProjects().size());
    }
    
}
